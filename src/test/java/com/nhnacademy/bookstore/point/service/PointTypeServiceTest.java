package com.nhnacademy.bookstore.point.service;

import com.nhnacademy.bookstore.common.error.exception.point.PointTypeNotFoundException;
import com.nhnacademy.bookstore.point.dto.request.CreatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.request.UpdatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.response.GetPointTypeResponse;
import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;
import com.nhnacademy.bookstore.point.dto.response.UpdatePointTypeResponseDto;
import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import com.nhnacademy.bookstore.point.repository.PointTypeRepository;
import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointTypeServiceTest {

    @Mock
    private PointTypeRepository pointTypeRepository;

    @InjectMocks
    private PointTypeServiceImpl pointTypeService;

    @Test
    @DisplayName("포인트 타입 생성 테스트 - Reflection으로 ID 설정")
    void testCreatePointTypeWithReflection() throws Exception {
        // 테스트 입력 데이터 준비
        CreatePointTypeRequestDto requestDto = new CreatePointTypeRequestDto(PointTypeEnum.PERCENT, 10, "구매 포인트", true);

        // Mock 객체 생성
        PointType savedPointType = PointType.builder()
                .type(requestDto.type())
                .accVal(requestDto.accVal())
                .name(requestDto.name())
                .isActive(requestDto.isActive())
                .build();

        // Reflection으로 ID 강제 설정
        Field field = PointType.class.getDeclaredField("pointTypeId");
        field.setAccessible(true);
        field.set(savedPointType, 1L); // ID 값을 강제로 설정

        // save 호출 시 강제로 ID 설정된 객체 반환
        when(pointTypeRepository.save(any(PointType.class))).thenReturn(savedPointType);

        // 서비스 호출
        Long id = pointTypeService.createPointType(requestDto);

        // 검증
        assertThat(id).isNotNull();
        assertThat(id).isEqualTo(1L);
    }



    @Test
    @DisplayName("포인트 타입 업데이트 테스트")
    void testUpdatePointType() {
        UpdatePointTypeRequestDto requestDto = new UpdatePointTypeRequestDto(PointTypeEnum.ACTUAL, 15, "리뷰 포인트", true);
        PointType existingPointType = PointType.builder()
                .type(PointTypeEnum.PERCENT)
                .accVal(10)
                .name("구매 포인트")
                .isActive(true)
                .build();

        when(pointTypeRepository.findById(anyLong())).thenReturn(Optional.of(existingPointType));
        when(pointTypeRepository.save(any(PointType.class))).thenReturn(existingPointType);

        UpdatePointTypeResponseDto responseDto = pointTypeService.updatePointType(1L, requestDto);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.pointTypeId()).isEqualTo(existingPointType.getPointTypeId());
        assertThat(responseDto.name()).isEqualTo("리뷰 포인트");
    }

    @Test
    @DisplayName("활성화된 포인트 타입 조회 테스트")
    void testGetAllActivePointTypes() {
        GetPointTypeResponse responseDto1 = new GetPointTypeResponse(1L, PointTypeEnum.PERCENT, 10, "포인트1", true);
        GetPointTypeResponse responseDto2 = new GetPointTypeResponse(2L, PointTypeEnum.ACTUAL, 15, "포인트2", true);

        when(pointTypeRepository.findAllActivePointTypes()).thenReturn(List.of(responseDto1, responseDto2));

        List<GetPointTypeResponse> result = pointTypeService.getAllActivePointTypes();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("포인트1");
        assertThat(result.get(1).name()).isEqualTo("포인트2");
    }

    @Test
    @DisplayName("특정 포인트 타입 조회 테스트")
    void testGetPointTypeById() {
        PointType pointType = PointType.builder()
                .type(PointTypeEnum.ACTUAL)
                .accVal(10)
                .name("리뷰 포인트")
                .isActive(true)
                .build();

        when(pointTypeRepository.findById(anyLong())).thenReturn(Optional.of(pointType));

        ReadPointTypeResponseDto responseDto = pointTypeService.getPointTypeById(1L);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.pointTypeId()).isEqualTo(pointType.getPointTypeId());
        assertThat(responseDto.name()).isEqualTo("리뷰 포인트");
    }

    @Test
    @DisplayName("존재하지 않는 포인트 타입 조회 시 예외 발생 테스트")
    void testGetPointTypeById_NotFound() {
        when(pointTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pointTypeService.getPointTypeById(1L))
                .isInstanceOf(PointTypeNotFoundException.class);
    }
}
