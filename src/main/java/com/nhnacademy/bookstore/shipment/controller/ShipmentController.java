package com.nhnacademy.bookstore.shipment.controller;

import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentResponseDto;
import com.nhnacademy.bookstore.shipment.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 배송 관리 기능을 제공하는 ShipmentController 클래스.
 * 새로운 배송을 생성, 조회, 수정 및 삭제하는 기능을 제공합니다.
 *
 * <p>API 경로: /bookstore/shipments</p>
 *
 * @author 양준하
 */
@Tag(name = "Shipment", description = "배송 API")
@RestController
@RequestMapping("/api/v1/bookstore/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    /**
     * 새로운 배송을 생성하는 메서드.
     *
     * @param requestDto 생성할 배송 정보를 담은 DTO
     * @return 생성된 배송 정보를 포함한 ResponseEntity
     */
    @Operation(summary = "배송 생성", description = "새로운 배송을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "배송 생성 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping
    public ResponseEntity<ShipmentResponseDto> createShipment(@Valid @RequestBody ShipmentRequestDto requestDto) {
        ShipmentResponseDto responseDto = shipmentService.createShipment(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 모든 배송을 조회하는 메서드.
     *
     * @return 모든 배송 목록을 포함한 ResponseEntity
     */
    @Operation(summary = "모든 배송 조회", description = "모든 배송을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "배송 조회 성공")
    @GetMapping
    public ResponseEntity<List<ShipmentResponseDto>> getAllShipments() {
        List<ShipmentResponseDto> responseDtos = shipmentService.getAllShipments();
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 특정 배송을 조회하는 메서드.
     *
     * @param shipmentId 조회할 배송의 ID
     * @return 조회된 배송 정보를 포함한 ResponseEntity
     */
    @Operation(summary = "배송 조회", description = "특정 배송을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "배송 조회 성공")
    @ApiResponse(responseCode = "404", description = "배송을 찾을 수 없음")
    @GetMapping("/{shipmentId}")
    public ResponseEntity<ShipmentResponseDto> getShipment(@PathVariable @ValidPathVariable Long shipmentId) {
        ShipmentResponseDto responseDto = shipmentService.getShipment(shipmentId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 배송이 완료된 모든 배송 정보를 조회하는 메서드.
     *
     * @return 배송 완료된 정보 목록을 포함한 ResponseEntity
     */
    @Operation(summary = "완료된 배송 조회", description = "배송 완료된 정보들을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "배송 완료된 정보 조회 성공")
    @GetMapping("/completed")
    public ResponseEntity<List<ShipmentResponseDto>> getCompletedShipments() {
        List<ShipmentResponseDto> responseDtos = shipmentService.getCompletedShipments();
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 아직 배송이 완료되지 않은 모든 배송 정보를 조회하는 메서드.
     *
     * @return 배송 미완료 정보 목록을 포함한 ResponseEntity
     */
    @Operation(summary = "미완료된 배송 조회", description = "아직 배송이 완료되지 않은 정보들을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "배송 미완료 정보 조회 성공")
    @GetMapping("/pending")
    public ResponseEntity<List<ShipmentResponseDto>> getPendingShipments() {
        List<ShipmentResponseDto> responseDtos = shipmentService.getPendingShipments();
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 특정 배송을 수정하는 메서드.
     *
     * @param shipmentId 수정할 배송의 ID
     * @param requestDto 수정할 배송 정보를 담은 DTO
     * @return 수정된 배송 정보를 포함한 ResponseEntity
     */
    @Operation(summary = "배송 수정", description = "특정 배송을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "배송 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "404", description = "배송을 찾을 수 없음")
    @PutMapping("/{shipmentId}")
    public ResponseEntity<ShipmentResponseDto> updateShipment(
            @PathVariable @ValidPathVariable Long shipmentId,
            @Valid @RequestBody ShipmentRequestDto requestDto) {
        ShipmentResponseDto responseDto = shipmentService.updateShipment(shipmentId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 배송을 삭제하는 메서드.
     *
     * @param shipmentId 삭제할 배송의 ID
     * @return 삭제 성공 상태를 포함한 ResponseEntity
     */
    @Operation(summary = "배송 삭제", description = "특정 배송을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "배송 삭제 성공")
    @ApiResponse(responseCode = "404", description = "배송을 찾을 수 없음")
    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteShipment(@PathVariable @ValidPathVariable Long shipmentId) {
        shipmentService.deleteShipment(shipmentId);
        return ResponseEntity.ok().build();
    }
}
