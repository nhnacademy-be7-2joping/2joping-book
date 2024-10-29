package com.nhnacademy.bookstore.shipment.controller;

import com.nhnacademy.bookstore.shipment.dto.request.ShipmentRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentResponseDto;
import com.nhnacademy.bookstore.shipment.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Shipment", description = "배송 API")
@RestController
@RequestMapping("/bookstore/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Operation(summary = "Create a new shipment", description = "새로운 배송을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "배송 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<ShipmentResponseDto> createShipment(@Valid @RequestBody ShipmentRequestDto requestDto) {
        ShipmentResponseDto responseDto = shipmentService.createShipment(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Get all shipments", description = "모든 배송을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<ShipmentResponseDto>> getAllShipments() {
        List<ShipmentResponseDto> responseDtos = shipmentService.getAllShipments();
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(summary = "Get a shipment", description = "특정 배송을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 조회 성공"),
            @ApiResponse(responseCode = "404", description = "배송을 찾을 수 없음")
    })
    @GetMapping("/{shipmentId}")
    public ResponseEntity<ShipmentResponseDto> getShipment(@PathVariable Long shipmentId) {
        ShipmentResponseDto responseDto = shipmentService.getShipment(shipmentId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Get completed shipments", description = "배송 완료된 정보들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 완료된 정보 조회 성공")
    })
    @GetMapping("/completed")
    public ResponseEntity<List<ShipmentResponseDto>> getCompletedShipments() {
        List<ShipmentResponseDto> responseDtos = shipmentService.getCompletedShipments();
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(summary = "Get pending shipments", description = "아직 배송이 완료되지 않은 정보들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 미완료 정보 조회 성공")
    })
    @GetMapping("/pending")
    public ResponseEntity<List<ShipmentResponseDto>> getPendingShipments() {
        List<ShipmentResponseDto> responseDtos = shipmentService.getPendingShipments();
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(summary = "Update a shipment", description = "특정 배송을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "배송을 찾을 수 없음")
    })
    @PutMapping("/{shipmentId}")
    public ResponseEntity<ShipmentResponseDto> updateShipment(
            @PathVariable Long shipmentId,
            @Valid @RequestBody ShipmentRequestDto requestDto) {
        ShipmentResponseDto responseDto = shipmentService.updateShipment(shipmentId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Delete a shipment", description = "특정 배송을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "배송을 찾을 수 없음")
    })
    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteShipment(@PathVariable Long shipmentId) {
        shipmentService.deleteShipment(shipmentId);
        return ResponseEntity.ok().build();
    }
}
