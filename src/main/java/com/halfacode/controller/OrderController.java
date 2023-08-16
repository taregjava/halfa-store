package com.halfacode.controller;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.OrderCreateRequest;
import com.halfacode.dto.OrderRequest;
import com.halfacode.entity.order.Order;
import com.halfacode.entity.order.PaymentMethod;
import com.halfacode.exception.UserNotFoundException;
import com.halfacode.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Order> checkout(@PathVariable Long userId, @RequestParam PaymentMethod paymentMethod) {
        try {
            Order newOrder = orderService.checkout(userId, paymentMethod);
            return ResponseEntity.ok(newOrder);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

  /*  @PostMapping("/createOrder")
    public ResponseEntity<ApiResponse<Order>> saveOrder2(@RequestBody OrderCreateRequest request) {
        try {
            Order createdOrder = orderService.saveOrder(request.getUserId(), request.getAddress());
            ApiResponse<Order> response = new ApiResponse<>(HttpStatus.OK.value(), createdOrder, null, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Order> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }*/
    /*@PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrder1(orderRequest.getUserId(), orderRequest.getProductIds());
        return ResponseEntity.ok(order);
    }*/
    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id);
        return orderService.updateOrder(order);
    }
  /*  @PostMapping("/saveOrder")
    public Order createOrder2(@RequestBody Order order){
        return orderService.createOrder(order);
    }*/

  /*  @PostMapping("/createOrder")
    public ResponseEntity<ApiResponse<Order>> saveOrder(@RequestBody OrderCreateRequest request) {
        try {
            Order createdOrder = orderService.saveOrder(request.getUserId());
            ApiResponse<Order> response = new ApiResponse<>(HttpStatus.OK.value(), createdOrder, null, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Order> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }*/

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}