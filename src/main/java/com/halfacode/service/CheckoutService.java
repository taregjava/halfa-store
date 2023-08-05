package com.halfacode.service;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.OrderConfirmationDTO;
import com.halfacode.dto.OrderDTO;
import com.halfacode.entity.CartItem;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import com.halfacode.entity.order.Order;
import com.halfacode.entity.order.OrderDetail;
import com.halfacode.entity.order.OrderStatus;
import com.halfacode.entity.order.OrderTrack;
import com.halfacode.exception.UserNotFoundException;
import com.halfacode.repoistory.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderTrackRepository orderTrackRepository;

    @Autowired
    private MailService mailService;

    @Transactional
    public ApiResponse<OrderConfirmationDTO> createOrder(OrderDTO orderDTO) {
        User user = getUserById(orderDTO.getUserId());
        List<CartItem> cartItems = getUncheckedOutCartItems(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("No cart items found for the user.");
        }

        Order order = createOrderFromDTO(orderDTO);
        order = orderRepository.save(order);

        float productCostTotal = calculateProductCostTotal(order, cartItems);

        saveOrderDetails(order, cartItems, productCostTotal);

        createOrderTrack(order, user);
        float shippingCost = order.getShippingCost();
        float subtotal = productCostTotal + shippingCost;
        order.setSubtotal(subtotal);
        order.setProductCost(productCostTotal);

        order = orderRepository.save(order);

        // Fetch the product names and set them in the OrderConfirmationDTO
        List<String> productNames = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product != null) {
                productNames.add(product.getName());
            } else {
                productNames.add("Unknown Product");
            }
        }

   //     order.setProduct((Product) productNames);
        // Create the OrderConfirmationDTO and set its properties
        OrderConfirmationDTO orderConfirmation = new OrderConfirmationDTO();
        orderConfirmation.setId(order.getId());
        orderConfirmation.setOrderTime(order.getOrderTime());
        orderConfirmation.setCountry(order.getCountry());
        orderConfirmation.setStatus(order.getStatus());
        orderConfirmation.setShippingCost(order.getShippingCost());
        orderConfirmation.setProductCost(order.getProductCost());
        orderConfirmation.setSubtotal(order.getSubtotal());
        orderConfirmation.setProductNames(productNames);

        ApiResponse<OrderConfirmationDTO> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), orderConfirmation, "Order placed successfully");

        // Send order confirmation email to the user after the order details have been saved
        sendOrderConfirmationEmail(order, user);

        return apiResponse;
    }
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
    }

    private List<CartItem> getUncheckedOutCartItems(User user) {
        return user.getCartItems().stream()
                .filter(cartItem -> !cartItem.isCheckedOut())
                .collect(Collectors.toList());
    }

    private Order createOrderFromDTO(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderTime(new Date()); // Set current timestamp in milliseconds
        order.setCountry(orderDTO.getCountry());
        order.setStatus(OrderStatus.NEW); // Assuming the default status is NEW for a new order
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        return order;
    }

    private float calculateProductCostTotal(Order order, List<CartItem> cartItems) {
        float productCostTotal = 0.0f;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product == null) {
                throw new RuntimeException("Product is null for cart item with ID: " + cartItem.getId());
            }

            Float productCost = product.getCost();
            float cartItemProductCost = productCost != null ? productCost * cartItem.getQuantity() : 0.0f;
            productCostTotal += cartItemProductCost;
        }
        return productCostTotal;
    }

    private void saveOrderDetails(Order order, List<CartItem> cartItems, float productCostTotal) {
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Float productCost = product.getCost();
            float cartItemProductCost = productCost != null ? productCost * cartItem.getQuantity() : 0.0f;

            OrderDetail orderDetail = new OrderDetail(product, cartItem.getQuantity(), cartItemProductCost, cartItem.getShippingCost());
            orderDetail.setOrder(order); // Associate the order with the order detail
            orderDetail.setCartItem(cartItem);

            orderDetailRepository.save(orderDetail);

            cartItem.setCheckedOut(true);
        }
    }

    private void createOrderTrack(Order order, User user) {
        OrderTrack track = new OrderTrack();
        track.setOrder(order);
        track.setStatus(OrderStatus.NEW);
        track.setNotes(OrderStatus.NEW.defaultDescription());
        track.setUpdatedTime(new Date());
        track.setUpdatedBy(user);
        orderTrackRepository.save(track);
    }

    @Async
    private void sendOrderConfirmationEmail(Order order, User user) {
        String userEmail = user.getEmail(); // Assuming there's an getEmail() method in the User class

        // Fetch the order details from the database to ensure we have the latest information
        Order fetchedOrder = orderRepository.findByIdWithOrderDetails(order.getId())
                .orElseThrow(() -> new RuntimeException("Failed to fetch order details for Order ID: " + order.getId()));

        // Check if the orderDetails set is populated with the associated OrderDetail entities
        Set<OrderDetail> orderDetails = fetchedOrder.getOrderDetails();
        System.out.println("Order Details: " + orderDetails);

        // Loop through the OrderDetail entities and log the associated Product names
        for (OrderDetail orderDetail : orderDetails) {
            Product product = orderDetail.getProduct();
            System.out.println("Product Name: " + product.getName());
        }

        // Construct the email content with the fetched order details
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Dear ").append(user.getName()).append(",\n\n");
        emailContent.append("Your order has been placed successfully with the following details:\n");
        emailContent.append("Order ID: ").append(fetchedOrder.getId()).append("\n");

        // Fetch the product names from the order's orderDetails

        // Get the product names using the getProductNames() method from the Order entity
        List<String> productNames = fetchedOrder.getProductNames();

        // Log the product names to check if they are being correctly fetched
        System.out.println("Product Names: " + productNames);

        emailContent.append("Product Names: ").append(String.join(", ", productNames)).append("\n");


        // Log the product names to check if they are being correctly fetched
        System.out.println("Product Names: " + productNames);

        emailContent.append("Product Names: ").append(String.join(", ", productNames)).append("\n");
        // ... Add other order details as needed

        // Send the email using the MailService
        mailService.sendOrderConfirmationMessage(userEmail, "Order Confirmation", emailContent.toString());
    }
}