package com.halfacode.service;

import com.halfacode.dto.ShipmentDTO;
import com.halfacode.entity.order.Order;
import com.halfacode.entity.Shipment;
import com.halfacode.exception.OrderNotFoundException;
import com.halfacode.exception.ShipmentNotFoundException;
import com.halfacode.repoistory.OrderRepository;
import com.halfacode.repoistory.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {
    private ShipmentRepository shipmentRepository;
    private OrderRepository orderRepository;
    private static int lastShipmentNumber = 0;

    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository,OrderRepository orderRepository) {
        this.shipmentRepository = shipmentRepository;
        this.orderRepository = orderRepository;
    }

    public Shipment createShipment(ShipmentDTO shipmentDTO) {
        String shipmentId = generateShipmentId();
        //shipment.setShipmentId(shipmentId);

        Order order = orderRepository.findById(shipmentDTO.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found")); // Custom exception handling if product not found

        Shipment shipment= new Shipment(shipmentDTO.getShipmentId(),shipmentDTO.getEstimatedDeliveryDate(),order);
        // Extract the order details from the shipment object
       // Order order = shipment.getOrder();
        // Perform any necessary operations with the order details
        shipment.setShipmentId(shipmentId);
        return shipmentRepository.save(shipment);
    }

    public Shipment getShipmentById(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with ID: " + shipmentId));

        return shipment;
    }

    private String generateShipmentId() {
        lastShipmentNumber++;
        return "SH" + String.format("%06d", lastShipmentNumber);
    }
}
