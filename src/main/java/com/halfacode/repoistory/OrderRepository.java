package com.halfacode.repoistory;

import com.halfacode.entity.order.Order;
import com.halfacode.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
   List<Order> findByUser(User user);

   @Override
   @Transactional
   default <S extends Order> S saveAndFlush(S entity) {
      // Save the entity and flush immediately
      S savedEntity = save(entity);
      flush();
      return savedEntity;
   }

}
