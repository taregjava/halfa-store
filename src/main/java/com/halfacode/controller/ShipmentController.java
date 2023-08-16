package com.halfacode.controller;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.ShipmentDTO;
import com.halfacode.entity.Shipment;
import com.halfacode.exception.ShipmentNotFoundException;
import com.halfacode.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {
    private ShipmentService shipmentService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ShipmentDTO>> createShipment(@RequestBody ShipmentDTO shipment) {
        Shipment saveToDb = shipmentService.createShipment(shipment);
        shipment.setShipmentId(saveToDb.getShipmentId());
        ApiResponse<ShipmentDTO> response = new ApiResponse<>(HttpStatus.OK.value(), shipment, (String) null);
        return ResponseEntity.ok(response);
       // return ResponseEntity.ok("shipment success add ");
    }

    @GetMapping("/{shipmentId}")
    public ResponseEntity<ApiResponse<Shipment>> getShipmentById(@PathVariable Long shipmentId) {
        try {
            Shipment shipment= shipmentService.getShipmentById(shipmentId);
            ApiResponse<Shipment> response =new ApiResponse<>(HttpStatus.OK.value(), shipment, (String) null);
            return ResponseEntity.ok(response);

        } catch (ShipmentNotFoundException ex) {
            ApiResponse<Shipment> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception ex) {
            ApiResponse<Shipment> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while fetching the shipment.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Other shipment-related endpoints
}