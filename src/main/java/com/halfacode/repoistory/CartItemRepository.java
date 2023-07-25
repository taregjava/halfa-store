package com.halfacode.repoistory;

import com.halfacode.entity.CartItem;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);

    List<CartItem> findByUser_Id(Long userId);

/*
    @Query("SELECT c FROM CartItem c WHERE c.user = :user AND c.product.id IN :productIds")
    List<CartItem> findByProductIdsAndUser(List<Long> productIds, User user);
*/

   /* *//*  @Query("SELECT c FROM CartItem c WHERE c.user = :user AND c.products IN :products")
    List<CartItem> findCartItemsByProductsAndUser(@Param("products") List<Product> products, @Param("user") User user);
*/

 /*   @Query("SELECT c FROM CartItem c WHERE :product MEMBER OF c.products AND c.user = :user")
    CartItem findCartItemByProductAndUser(@Param("product") Product product, @Param("user") User user);*/
}