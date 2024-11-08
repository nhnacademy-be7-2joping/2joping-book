package com.nhnacademy.bookstore.shipment.controller;

import com.nhnacademy.bookstore.shipment.dto.request.ShipmentPolicyRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.service.ShipmentPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 배송 정책 관리 기능을 제공하는 ShipmentPolicyController 클래스.
 * 새로운 배송 정책을 생성, 조회, 수정, 활성화 및 비활성화하는 기능을 제공합니다.
 *
 * <p>API 경로: /bookstore/shipment-policies</p>
 *
 * @author 양준하
 */
@Tag(name = "Shipment Policy", description = "배송 정책 API")
@RestController
@RequestMapping("/api/v1/bookstore/shipment-policies")
@RequiredArgsConstructor
public class ShipmentPolicyController {

    private final ShipmentPolicyService shipmentPolicyService;

    /**
     * 새로운 배송 정책을 생성하는 메서드.
     *
     * @param requestDto 생성할 배송 정책 정보를 담은 DTO
     * @return 생성된 배송 정책 정보를 포함한 ResponseEntity
     */
    @Operation(summary = "Create a new shipment policy", description = "새로운 배송 정책을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "배송 정책 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<ShipmentPolicyResponseDto> createShipmentPolicy(
            @Valid @RequestBody ShipmentPolicyRequestDto requestDto) {
        ShipmentPolicyResponseDto responseDto = shipmentPolicyService.createShipmentPolicy(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 모든 활성화된 배송 정책을 조회하는 메서드.
     *
     * @return 활성화된 모든 배송 정책 목록을 포함한 ResponseEntity
     */
    @Operation(summary = "Get all shipment policies", description = "모든 활성화된 배송 정책을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 정책 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<ShipmentPolicyResponseDto>> getAllShipmentPolicies() {
        List<ShipmentPolicyResponseDto> responseDtos = shipmentPolicyService.getAllShipmentPolicies();
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 특정 배송 정책을 조회하는 메서드.
     *
     * @param shipmentPolicyId 조회할 배송 정책의 ID
     * @return 조회된 배송 정책 정보를 포함한 ResponseEntity
     */
    @Operation(summary = "Get a shipment policy", description = "특정 배송 정책을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 정책 조회 성공"),
            @ApiResponse(responseCode = "404", description = "배송 정책을 찾을 수 없음")
    })
    @GetMapping("/{shipmentPolicyId}")
    public ResponseEntity<ShipmentPolicyResponseDto> getShipmentPolicy(@PathVariable @Positive Long shipmentPolicyId) {
        ShipmentPolicyResponseDto responseDto = shipmentPolicyService.getShipmentPolicy(shipmentPolicyId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 배송 정책을 수정하는 메서드.
     *
     * @param shipmentPolicyId 수정할 배송 정책의 ID
     * @param requestDto 수정할 배송 정책 정보를 담은 DTO
     * @return 수정된 배송 정책 정보를 포함한 ResponseEntity
     */
    @Operation(summary = "Update a shipment policy", description = "특정 배송 정책을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 정책 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "배송 정책을 찾을 수 없음")
    })
    @PutMapping("/{shipmentPolicyId}")
    public ResponseEntity<ShipmentPolicyResponseDto> updateShipmentPolicy(
            @PathVariable @Positive Long shipmentPolicyId,
            @Valid @RequestBody ShipmentPolicyRequestDto requestDto) {
        ShipmentPolicyResponseDto responseDto = shipmentPolicyService.updateShipmentPolicy(shipmentPolicyId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 배송 정책을 비활성화하는 메서드.
     *
     * @param shipmentPolicyId 비활성화할 배송 정책의 ID
     * @return 비활성화 성공 상태를 포함한 ResponseEntity
     */
    @Operation(summary = "Deactivate a shipment policy", description = "특정 배송 정책을 비활성화(약삭제)합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 정책 비활성화 성공"),
            @ApiResponse(responseCode = "404", description = "배송 정책을 찾을 수 없음")
    })
    @PutMapping("/{shipmentPolicyId}/deactivate")
    public ResponseEntity<Void> deactivateShipmentPolicy(@PathVariable @Positive Long shipmentPolicyId) {
        shipmentPolicyService.deactivateShipmentPolicy(shipmentPolicyId);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 배송 정책을 활성화하는 메서드.
     *
     * @param shipmentPolicyId 활성화할 배송 정책의 ID
     * @return 활성화 성공 상태를 포함한 ResponseEntity
     */
    @Operation(summary = "Activate a shipment policy", description = "특정 배송 정책을 활성화 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 정책 활성화 성공"),
            @ApiResponse(responseCode = "404", description = "배송 정책을 찾을 수 없음")
    })
    @PutMapping("/{shipmentPolicyId}/activate")
    public ResponseEntity<Void> activateShipmentPolicy(@PathVariable @Positive Long shipmentPolicyId) {
        shipmentPolicyService.activateShipmentPolicy(shipmentPolicyId);
        return ResponseEntity.ok().build();
    }
}
