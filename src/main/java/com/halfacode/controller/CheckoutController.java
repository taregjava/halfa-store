package com.halfacode.controller;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.OrderConfirmationDTO;
import com.halfacode.dto.OrderDTO;
import com.halfacode.entity.CartItem;
import com.halfacode.entity.User;
import com.halfacode.entity.order.Order;
import com.halfacode.exception.UserNotFoundException;
import com.halfacode.repoistory.CartItemRepository;
import com.halfacode.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private CartItemRepository cartItemRepository;

   /* @PostMapping("")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO, @RequestParam("cartItemIds") List<Long> cartItemIds) {
        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);

        // Check if all cart items belong to the same user (optional)
        Long userId = cartItems.get(0).getUser().getId();

        boolean sameUser = cartItems.stream().allMatch(cartItem -> {
            User user = cartItem.getUser();
            return user != null && user.getId() != null && userId != null && user.getId().equals(userId);
        });

        if (!sameUser) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Order createdOrder = checkoutService.createOrder(orderDTO, cartItems);
        return ResponseEntity.ok(createdOrder);
    }*/

 /*   @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO, @RequestParam("userId") Long userId) {
        Order createdOrder = checkoutService.createOrder(orderDTO, userId);
        return ResponseEntity.ok(createdOrder);
    }*/

    @PostMapping
    public ResponseEntity<ApiResponse<OrderConfirmationDTO>> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            // Call the createOrder method from the OrderService
            ApiResponse<OrderConfirmationDTO> response = checkoutService.createOrder(orderDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            // Handle the case where the user is not found
            ApiResponse<OrderConfirmationDTO> errorResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (RuntimeException ex) {
            // Handle other runtime exceptions, e.g., if no cart items found
            ApiResponse<OrderConfirmationDTO> errorResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
