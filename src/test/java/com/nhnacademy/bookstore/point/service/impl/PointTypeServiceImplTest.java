package com.nhnacademy.bookstore.point.service.impl;

import com.nhnacademy.bookstore.common.error.exception.point.PointTypeNotFoundException;
import com.nhnacademy.bookstore.point.dto.request.CreatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.request.UpdatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.response.GetPointTypeResponse;
import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;
import com.nhnacademy.bookstore.point.dto.response.UpdatePointTypeResponseDto;
import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import com.nhnacademy.bookstore.point.repository.PointTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PointTypeServiceImplTest {

    @InjectMocks
    private PointTypeServiceImpl pointTypeService;

    @Mock
    private PointTypeRepository pointTypeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPointType_Success() {
        CreatePointTypeRequestDto dto = new CreatePointTypeRequestDto(
                PointTypeEnum.ACTUAL,
                100,
                "리뷰작성",
                true
        );

        // PointType 객체 생성 시 ID를 포함해서 생성
        PointType pointType = PointType.builder()
                .pointTypeId(1L)  // 명시적으로 ID 설정
                .type(dto.type())
                .accVal(dto.accVal())
                .name(dto.name())
                .isActive(dto.isActive())
                .build();

        // any() 대신 정확한 DTO로 mocking
        when(pointTypeRepository.save(any(PointType.class))).thenReturn(pointType);

        Long result = pointTypeService.createPointType(dto);

        // 결과 검증
        assertNotNull(result);
        assertEquals(1L, result);
    }

    @Test
    void updatePointType_Success() {
        Long pointTypeId = 1L;
        UpdatePointTypeRequestDto dto = new UpdatePointTypeRequestDto(
                PointTypeEnum.ACTUAL,
                150,
                "리뷰작성",
                true
        );

        PointType existingPointType = PointType.builder()
                .pointTypeId(pointTypeId) // ID 설정
                .type(PointTypeEnum.ACTUAL)
                .accVal(100)
                .name("리뷰작성")
                .isActive(true)
                .build();

        when(pointTypeRepository.findById(pointTypeId)).thenReturn(Optional.of(existingPointType));
        when(pointTypeRepository.save(existingPointType)).thenReturn(existingPointType);

        UpdatePointTypeResponseDto response = pointTypeService.updatePointType(pointTypeId, dto);

        assertEquals(pointTypeId, response.pointTypeId());
        assertEquals(dto.type(), response.type());
        assertEquals(dto.accVal(), response.accVal());
        assertEquals(dto.name(), response.name());
        assertEquals(dto.isActive(), response.isActive());
    }

    @Test
    void updatePointType_NotFound() {
        Long pointTypeId = 1L;
        UpdatePointTypeRequestDto dto = new UpdatePointTypeRequestDto(
                PointTypeEnum.ACTUAL,
                150,
                "리뷰작성",
                true
        );

        when(pointTypeRepository.findById(pointTypeId)).thenReturn(Optional.empty());

        assertThrows(PointTypeNotFoundException.class, () -> pointTypeService.updatePointType(pointTypeId, dto));
    }

    @Test
    void getAllActivePointTypes_Success() {
        List<GetPointTypeResponse> expectedResponses = List.of(
                new GetPointTypeResponse(1L, PointTypeEnum.ACTUAL, 100, "리뷰작성", true)
        );

        when(pointTypeRepository.findAllActivePointTypes()).thenReturn(expectedResponses);

        List<GetPointTypeResponse> actualResponses = pointTypeService.getAllActivePointTypes();

        assertEquals(expectedResponses, actualResponses); // 반환된 리스트가 예상 리스트와 같아야 함
    }

    @Test
    void getPointTypeById_Success() {
        Long pointTypeId = 1L;
        PointType pointType = PointType.builder()
                .pointTypeId(pointTypeId) // ID 설정
                .type(PointTypeEnum.ACTUAL)
                .accVal(100)
                .name("리뷰작성")
                .isActive(true)
                .build();

        when(pointTypeRepository.findById(pointTypeId)).thenReturn(Optional.of(pointType));

        ReadPointTypeResponseDto response = pointTypeService.getPointTypeById(pointTypeId);

        assertEquals(pointTypeId, response.pointTypeId());
        assertEquals(pointType.getType(), response.type());
        assertEquals(pointType.getAccVal(), response.accVal());
        assertEquals(pointType.getName(), response.name());
        assertEquals(pointType.isActive(), response.isActive());
    }

    @Test
    void getPointTypeById_NotFound() {
        Long pointTypeId = 1L;

        when(pointTypeRepository.findById(pointTypeId)).thenReturn(Optional.empty());

        assertThrows(PointTypeNotFoundException.class, () -> pointTypeService.getPointTypeById(pointTypeId));
    }
}
