package com.halfacode.service;

import com.halfacode.entity.Order;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import com.halfacode.enums.OrderStatus;
import com.halfacode.exception.UserNotFoundException;
import com.halfacode.repoistory.OrderRepository;
import com.halfacode.repoistory.ProductRepository;
import com.halfacode.repoistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(Long userId, List<Long> productIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        List<Product> products = productRepository.findAllById(productIds);

        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        // Set other order details

        order.setStatus(OrderStatus.CREATED);
        // Save the order in the repository
        return orderRepository.save(order);
    }
        public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    public Optional<Order> getOrderById(Long orderId) {
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

    public List<Order> getOrdersByUser(User user) {
        // Retrieve orders for a specific user from the repository
        return orderRepository.findByUser(user);
    }
    public Order updateOrder(Order updatedOrder) {
        // Update an existing order in the repository
        return orderRepository.save(updatedOrder);
    }

    public void cancelOrder(Long orderId) {
        // Cancel an order by its ID
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        optionalOrder.ifPresent(order -> {
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
        });
    }

    public void deleteOrder(Long id){
        orderRepository.deleteById(id);
    }

    // Other methods for order management (e.g., updateOrder, cancelOrder, etc.)
}