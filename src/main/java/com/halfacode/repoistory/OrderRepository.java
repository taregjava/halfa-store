package com.halfacode.repoistory;

import com.halfacode.entity.order.Order;
import com.halfacode.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
   List<Order> findByUser(User user);
   @Query("SELECT o FROM Order o JOIN FETCH o.orderDetails od WHERE o.id = :orderId")
   Optional<Order> findByIdWithOrderDetails(@Param("orderId") Long orderId);

   @Override
   @Transactional
   default <S extends Order> S saveAndFlush(S entity) {
      // Save the entity and flush immediately
      S savedEntity = save(entity);
      flush();
      return savedEntity;
   }

}
