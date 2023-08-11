package com.halfacode.repoistory;

import com.halfacode.entity.order.OrderTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTrackRepository extends JpaRepository<OrderTrack, Long> {
}
