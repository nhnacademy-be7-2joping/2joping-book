package com.nhnacademy.bookstore.shipment.controller;

import com.nhnacademy.bookstore.shipment.dto.request.CarrierRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.CarrierResponseDto;
import com.nhnacademy.bookstore.shipment.service.CarrierService;
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
 * 배송 업체 관리 기능을 제공하는 CarrierController 클래스.
 * 새로운 배송 업체를 생성, 조회, 수정 및 삭제하는 기능을 제공합니다.
 *
 * <p>API 경로: /bookstore/carriers</p>
 *
 * @author 양준하
 */
@Tag(name = "Carrier", description = "배송 업체 API")
@RestController
@RequestMapping("/api/v1/bookstore/carriers")
@RequiredArgsConstructor
public class CarrierController {

    private final CarrierService carrierService;

    /**
     * 새로운 배송 업체를 생성하는 메서드.
     *
     * @param requestDto 생성할 배송 업체 정보를 담은 DTO
     * @return 생성된 배송 업체 정보를 포함한 ResponseEntity
     */
    @Operation(summary = "Create a new carrier", description = "새로운 배송 업체를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "배송 업체 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<CarrierResponseDto> createCarrier(@Valid @RequestBody CarrierRequestDto requestDto) {
        CarrierResponseDto responseDto = carrierService.createCarrier(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 모든 배송 업체를 조회하는 메서드.
     *
     * @return 모든 배송 업체 목록을 포함한 ResponseEntity
     */
    @Operation(summary = "Get all carriers", description = "모든 배송 업체를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 업체 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<CarrierResponseDto>> getAllCarriers() {
        List<CarrierResponseDto> responseDtos = carrierService.getAllCarriers();
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 특정 배송 업체를 조회하는 메서드.
     *
     * @param carrierId 조회할 배송 업체의 ID
     * @return 조회된 배송 업체 정보를 포함한 ResponseEntity
     */
    @Operation(summary = "Get a carrier", description = "특정 배송 업체를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 업체 조회 성공"),
            @ApiResponse(responseCode = "404", description = "배송 업체를 찾을 수 없음")
    })
    @GetMapping("/{carrierId}")
    public ResponseEntity<CarrierResponseDto> getCarrier(@PathVariable @Positive Long carrierId) {
        CarrierResponseDto responseDto = carrierService.getCarrier(carrierId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 배송 업체를 수정하는 메서드.
     *
     * @param carrierId 수정할 배송 업체의 ID
     * @param requestDto 수정할 배송 업체 정보를 담은 DTO
     * @return 수정된 배송 업체 정보를 포함한 ResponseEntity
     */
    @Operation(summary = "Update a carrier", description = "특정 배송 업체를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 업체 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "배송 업체를 찾을 수 없음")
    })
    @PutMapping("/{carrierId}")
    public ResponseEntity<CarrierResponseDto> updateCarrier(
            @PathVariable @Positive Long carrierId,
            @Valid @RequestBody CarrierRequestDto requestDto) {
        CarrierResponseDto responseDto = carrierService.updateCarrier(carrierId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 배송 업체를 삭제하는 메서드.
     *
     * @param carrierId 삭제할 배송 업체의 ID
     * @return 삭제 성공 상태를 포함한 ResponseEntity
     */
    @Operation(summary = "Delete a carrier", description = "특정 배송 업체를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송 업체 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "배송 업체를 찾을 수 없음")
    })
    @DeleteMapping("/{carrierId}")
    public ResponseEntity<Void> deleteCarrier(@PathVariable @Positive Long carrierId) {
        carrierService.deleteCarrier(carrierId);
        return ResponseEntity.ok().build();
    }
}
