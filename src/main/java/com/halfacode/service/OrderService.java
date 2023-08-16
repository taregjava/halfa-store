package com.halfacode.service;

import com.halfacode.dto.CheckoutInfo;
import com.halfacode.dto.OrderRequest;
import com.halfacode.entity.Address;
import com.halfacode.entity.CartItem;
import com.halfacode.entity.order.*;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import com.halfacode.exception.ProductNotFoundException;
import com.halfacode.exception.UserNotFoundException;
import com.halfacode.repoistory.CartItemRepository;
import com.halfacode.repoistory.OrderRepository;
import com.halfacode.repoistory.ProductRepository;
import com.halfacode.repoistory.UserRepository;
//import com.maxmind.geoip2.exception.AddressNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
//private
    private final CartItemRepository cartItemRepository;
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, UserService userService, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Order checkout(Long userId, PaymentMethod paymentMethod) {
        try {
            // Step 1: Check if the user exists
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));

            // Step 2: Fetch all cart items associated with the user
            List<CartItem> cartItems = user.getCartItems().stream()
                    .filter(cartItem -> !cartItem.isCheckedOut())
                    .collect(Collectors.toList());

            // Step 3: Create the order and set its properties
            Order newOrder = new Order();
            newOrder.setOrderTime(new Date());
            newOrder.setCountry("USA"); // Replace with actual country
            newOrder.setStatus(OrderStatus.NEW);
            newOrder.setPaymentMethod(paymentMethod);
            newOrder.setUser(user);

            // Step 4: Calculate the total product cost for the order
            float productCostTotal = 0.0f;
            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                if (product != null) {
                    float cost = product.getCost();
                    int quantity = cartItem.getQuantity();
                    productCostTotal += cost * quantity;
                }
            }
            newOrder.setProductCost(productCostTotal);

            // Step 5: Set other order properties like shipping cost, tax, total, etc.
            float shippingCost = 0.0f; // You may calculate the shipping cost based on the address or any other logic
            newOrder.setShippingCost(shippingCost);

            // Calculate and set the subtotal, tax, and total if applicable

            // Step 6: Create the order details for each cart item and add them to the order
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                if (product != null) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setQuantity(cartItem.getQuantity());
                    float productCost = product.getCost();
                    orderDetail.setProductCost(productCost);
                    orderDetail.setShippingCost(0.0f); // Set the shipping cost (you may calculate it if required)
                    orderDetail.setUnitPrice(product.getCost()); // Set the unit price (you may calculate it if required)

// Calculate the subtotal for the order detail
                    float subtotal = productCost * cartItem.getQuantity();
                    orderDetail.setSubtotal(subtotal);

                    orderDetail.setProduct(product); // Set the product
                    orderDetail.setOrder(newOrder); // Set the order
// Add the orderDetail to the list
                    orderDetails.add(orderDetail);
                }
            }
         //   newOrder.setOrderDetails(orderDetails);

            // Step 7: Save the newOrder to the database
            Order savedOrder = orderRepository.save(newOrder);

            // Step 8: Mark the cart items as checked out
            cartItems.forEach(cartItem -> cartItem.setCheckedOut(true));
            cartItemRepository.saveAll(cartItems);

            // Optionally, you can perform any other necessary actions for the checkout process,
            // such as updating inventory, sending confirmation emails, etc.

            return savedOrder;
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();
            throw new ProductNotFoundException("Error creating the order. Details: " + e.getMessage());
        }
    }
  /*  @Transactional
    public Order createOrder(OrderRequest request) {
        try {
            Long userId = request.getUserId();
            Address address = request.getAddress();
            List<Long> productIds = request.getProductIds();
            int quantity = request.getQuantity();
            float shippingCost = request.getShippingCost();
            PaymentMethod paymentMethod = request.getPaymentMethod();
            CheckoutInfo checkoutInfo = request.getCheckoutInfo();

            Optional<User> optionalUser = userService.getUserById(userId);
            if (!optionalUser.isPresent()) {
                throw new UserNotFoundException("User with ID " + userId + " not found.");
            }

            User customer = optionalUser.get();

            Order newOrder = new Order();
            newOrder.setOrderTime(new Date());
            newOrder.setCountry(request.getCountry());
            if (paymentMethod.equals(PaymentMethod.PAYPAL)) {
                newOrder.setStatus(OrderStatus.PAID);
            } else {
                newOrder.setStatus(OrderStatus.NEW);
            }
            newOrder.setUser(customer);
            newOrder.setProductCost(checkoutInfo.getProductCost());
            newOrder.setSubtotal(checkoutInfo.getProductTotal());
            newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
            newOrder.setTax(0.0f);
            newOrder.setTotal(checkoutInfo.getPaymentTotal());
            newOrder.setPaymentMethod(paymentMethod);
            newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
            newOrder.setDeliverDate(checkoutInfo.getDeliverDate());
            Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
            if (address == null) {
                throw new AddressNotFoundException("Shipping address not provided.");
            } else {
                newOrder.copyShippingAddress(address);
            }

            // Save the newOrder and get the generated ID
            Order savedOrder = orderRepository.save(newOrder);
            Long newOrderId = savedOrder.getId();

            // Assuming you have a method to fetch products by their IDs
            List<Product> products = productRepository.findAllById(productIds);

            for (Product product : products) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(product);
                orderDetail.setOrder(savedOrder);
                orderDetail.setQuantity(quantity);
                orderDetail.setUnitPrice(product.getDiscountPercent());
                orderDetail.setProductCost(product.getCost() * quantity);
                orderDetail.setSubtotal(product.getCost() * quantity);
                orderDetail.setShippingCost(shippingCost);

                // Add the orderDetail to the orderDetails set using the utility method
                savedOrder.addOrderDetail(orderDetail);
            }

            OrderTrack track = new OrderTrack();
            track.setOrder(savedOrder);
            track.setStatus(OrderStatus.NEW);
            track.setNotes(OrderStatus.NEW.defaultDescription());
            track.setUpdatedTime(new Date());

            savedOrder.getOrderTracks().add(track);

            // Save the updated order with the orderDetails
            orderRepository.saveAndFlush(savedOrder);

            return savedOrder;
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();
            throw new ProductNotFoundException("Error creating the order. Details: " + e.getMessage());
        }
    }*/
 /*   @Transactional
    public Order saveOrder(Long userId, Address address) {
        try {
            // Step 1: Check if the user exists
            Optional<User> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent()) {
                throw new UserNotFoundException("User with ID " + userId + " not found.");
            }

            User user = optionalUser.get();

            // Step 2: Fetch all cart items associated with the user
            List<CartItem> cartItems = cartItemRepository.findByUser(user);

            // Step 3: Extract product IDs from cart items
            List<Long> productIds = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
            //    productIds.add(cartItem.getProduct().getId());
            }

            // Step 4: Assuming you have a method to fetch products by their IDs
            List<Product> products = productRepository.findAllById(productIds);

            // Step 5: Create the order and set its properties
            Order newOrder = new Order();
            newOrder.setOrderTime(new Date());
            newOrder.setCountry("USA"); // Replace with actual country
            newOrder.setStatus(OrderStatus.NEW);
            newOrder.setUser(user);

            float productCostTotal = 0.0f;
            List<OrderDetail> orderDetails = new ArrayList<>();

            for (Product product : products) {
                // Calculate the total product cost
                productCostTotal += product.getCost();
              //  CartItem cartItem = cartItemRepository.findCartItemByProductAndUser(product, user);
                // Optionally, you can create and add OrderDetails here
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProductCost(product.getCost()); // Set the product cost
                orderDetail.setShippingCost(0.0f); // Set the shipping cost (you may calculate it if required)
                orderDetail.setUnitPrice(product.getCost()); // Set the unit price (you may calculate it if required)
                orderDetail.setSubtotal(product.getCost()); // Set the subtotal (you may calculate it if required)
                orderDetail.setProduct(product); // Set the product
                orderDetail.setOrder(newOrder); // Set the order
              //  int quantity = (cartItem != null) ? cartItem.getQuantity() : 0;
               // orderDetail.setQuantity(quantity);
                // Add the orderDetail to the list
                orderDetails.add(orderDetail);
            }
            newOrder.getOrderDetails().addAll(orderDetails);

            newOrder.setProductCost(productCostTotal);

            // Set other order properties like shipping cost, tax, total, etc. using checkoutInfo

            // Copy shipping address from the request to the order entity
            newOrder.copyShippingAddress(address);

            // Step 6: Save the newOrder to the database
            Order savedOrder = orderRepository.save(newOrder);

            // Optionally, you can remove the cart items after the order is created
            cartItemRepository.deleteAll(cartItems);

            return savedOrder;
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();
            throw new ProductNotFoundException("Error creating the order. Details: " + e.getMessage());
        }

    }*/
     /*   public Order createOrder1 (Long userId, List < Long > productIds){
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

            List<Product> products = productRepository.findAllById(productIds);

            Order order = new Order();
            order.setUser(user);
            order.setProducts(products);
            // Set other order details

            order.setStatus(OrderStatus.PROCESSING);
            // Save the order in the repository
            return orderRepository.save(order);
        }*/

        public List<Order> getAllOrders () {
            return orderRepository.findAll();
        }

        public Optional<Order> getOrderById (Long orderId){
            return orderRepository.findById(orderId);
        }
   /* public Order createOrder(User user, List<Product> products) {
        // Create a new order
        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        // Set other order details

        order.setStatus(OrderStatus.CREATED);
        // Save the order in the repository
        return orderRepository.save(order);
    }*/

        public List<Order> getOrdersByUser (User user){
            // Retrieve orders for a specific user from the repository
            return orderRepository.findByUser(user);
        }

        public Order updateOrder (Order updatedOrder){
            // Update an existing order in the repository
            return orderRepository.save(updatedOrder);
        }

        public void cancelOrder (Long orderId){
            // Cancel an order by its ID
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            optionalOrder.ifPresent(order -> {
                // order.setStatus(OrderStatus.CANCELED);
                orderRepository.save(order);
            });
        }

        public void deleteOrder (Long id){
            orderRepository.deleteById(id);
        }

        // Other methods for order management (e.g., updateOrder, cancelOrder, etc.)
    }