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
       // order.setSubtotal(order.getProductCostTotal() + order.getShippingCost());

        order = orderRepository.save(order);

        // Create the OrderConfirmationDTO and set its properties
        OrderConfirmationDTO orderConfirmation = new OrderConfirmationDTO();
        orderConfirmation.setId(order.getId());
        orderConfirmation.setOrderTime(order.getOrderTime());
        orderConfirmation.setCountry(order.getCountry());
        orderConfirmation.setStatus(order.getStatus());
     //   orderConfirmation.setPaymentMethod(order.getPaymentMethod());
        orderConfirmation.setShippingCost(order.getShippingCost());
        orderConfirmation.setProductCost(order.getProductCost());
        orderConfirmation.setSubtotal(order.getSubtotal());
       // orderConfirmation.setDeliverDays(order.getDeliverDays());
       // orderConfirmation.setDeliverDate(order.getDeliverDate());
     //   orderConfirmation.setOrderDetails(order.getOrderDetails());
      //  orderConfirmation.setOrderTracks(order.getOrderTracks());

        // Fetch the product names and set them in the OrderConfirmationDTO
        List<String> productNames = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product != null) {
                productNames.add(product.getName());
            } else {
                // Handle case when product is null (optional)
                productNames.add("Unknown Product");
            }
        }
        orderConfirmation.setProductNames(productNames);

        // Return the ApiResponse with the OrderConfirmationDTO as payload
        return new ApiResponse<>(HttpStatus.OK.value(), orderConfirmation, "Order placed successfully");
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
        // order.setDeliverDays(orderDTO.getDeliverDays()); // Set delivery days from OrderDTO
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
            orderDetail.setOrder(order);
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

}
