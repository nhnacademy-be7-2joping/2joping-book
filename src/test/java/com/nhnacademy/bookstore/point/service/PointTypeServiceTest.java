//package com.nhnacademy.bookstore.point.service;
//
//import com.nhnacademy.bookstore.common.error.exception.point.PointTypeNotFoundException;
//import com.nhnacademy.bookstore.point.dto.request.CreatePointTypeRequestDto;
//import com.nhnacademy.bookstore.point.dto.request.UpdatePointTypeRequestDto;
//import com.nhnacademy.bookstore.point.dto.response.GetPointTypeResponse;
//import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;
//import com.nhnacademy.bookstore.point.dto.response.UpdatePointTypeResponseDto;
//import com.nhnacademy.bookstore.point.entity.PointType;
//import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
//import com.nhnacademy.bookstore.point.repository.PointTypeRepository;
//import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.net.URI;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PointTypeServiceTest {
//
//    @Mock
//    private PointTypeRepository pointTypeRepository;
//
//    @InjectMocks
//    private PointTypeServiceImpl pointTypeService;
//
//    @Test
//    @DisplayName("포인트 타입 생성 테스트")
//    void createPointType() {
//        CreatePointTypeRequestDto requestDto = new CreatePointTypeRequestDto(PointTypeEnum.PERCENT, 10, "구매 포인트", true);
//        PointType savedPointType = PointType.builder()
//                .type(requestDto.type())
//                .accVal(requestDto.accVal())
//                .name(requestDto.name())
//                .isActive(requestDto.isActive())
//                .build();
//        savedPointType.setPointTypeId(1L); // ID 설정
//
//        when(pointTypeRepository.save(any(PointType.class))).thenReturn(savedPointType);
//
//        Long id = pointTypeService.createPointType(requestDto);
//
//        assertNotNull(id);
//        assertEquals(1L, id);
//    }
//
//    @Test
//    @DisplayName("포인트 타입 수정 테스트")
//    void updatePointType() {
//        UpdatePointTypeRequestDto requestDto = new UpdatePointTypeRequestDto(PointTypeEnum.ACTUAL, 15, "리뷰 포인트", true);
//        PointType existingPointType = PointType.builder()
//                .type(PointTypeEnum.PERCENT)
//                .accVal(10)
//                .name("구매 포인트")
//                .isActive(true)
//                .build();
//        existingPointType.setPointTypeId(1L);
//
//        when(pointTypeRepository.findById(anyLong())).thenReturn(Optional.of(existingPointType));
//        when(pointTypeRepository.save(existingPointType)).thenAnswer(invocation -> {
//            PointType pointType = invocation.getArgument(0);
//            return pointType;
//        });
//
//        UpdatePointTypeResponseDto responseDto = pointTypeService.updatePointType(1L, requestDto);
//
//        assertNotNull(responseDto);
//        assertEquals(1L, responseDto.pointTypeId());
//        assertEquals("리뷰 포인트", responseDto.name());
//        assertEquals(PointTypeEnum.ACTUAL, responseDto.type());
//        assertEquals(15, responseDto.accVal());
//    }
//
//    @Test
//    @DisplayName("활성화된 포인트 타입 목록 조회 테스트")
//    void getAllActivePointTypes() {
//        GetPointTypeResponse responseDto1 = new GetPointTypeResponse(1L, PointTypeEnum.ACTUAL, 10, "포인트1", true);
//        GetPointTypeResponse responseDto2 = new GetPointTypeResponse(2L, PointTypeEnum.PERCENT, 15, "포인트2", true);
//
//        when(pointTypeRepository.findAllActivePointTypes()).thenReturn(List.of(responseDto1, responseDto2));
//
//        List<GetPointTypeResponse> activePointTypes = pointTypeService.getAllActivePointTypes();
//
//        assertNotNull(activePointTypes);
//        assertEquals(2, activePointTypes.size());
//        assertEquals("포인트1", activePointTypes.get(0).name());
//        assertEquals("포인트2", activePointTypes.get(1).name());
//    }
//
//    @Test
//    @DisplayName("특정 포인트 타입 조회 테스트")
//    void getPointTypeById() {
//        PointType pointType = PointType.builder()
//                .type(PointTypeEnum.ACTUAL)
//                .accVal(10)
//                .name("특정 포인트")
//                .isActive(true)
//                .build();
//        pointType.setPointTypeId(1L);
//
//        when(pointTypeRepository.findById(1L)).thenReturn(Optional.of(pointType));
//
//        ReadPointTypeResponseDto responseDto = pointTypeService.getPointTypeById(1L);
//
//        assertNotNull(responseDto);
//        assertEquals(1L, responseDto.pointTypeId());
//        assertEquals("특정 포인트", responseDto.name());
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 포인트 타입 조회 시 예외 발생 테스트")
//    void getPointTypeByIdNotFound() {
//        when(pointTypeRepository.findById(1L)).thenReturn(Optional.empty());
//        assertThrows(PointTypeNotFoundException.class, () -> pointTypeService.getPointTypeById(1L));
//    }
//}
//
