package com.halfacode.repoistory;

import com.halfacode.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    // Additional custom query methods or overrides can be added here if needed

}
