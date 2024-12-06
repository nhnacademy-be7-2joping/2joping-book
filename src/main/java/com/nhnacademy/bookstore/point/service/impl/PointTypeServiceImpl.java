package com.nhnacademy.bookstore.point.service.impl;

import com.nhnacademy.bookstore.common.error.exception.point.PointTypeNotFoundException;
import com.nhnacademy.bookstore.point.dto.request.CreatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.request.UpdatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.response.GetPointTypeResponse;
import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;
import com.nhnacademy.bookstore.point.dto.response.UpdatePointTypeResponseDto;
import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.repository.PointTypeRepository;
import com.nhnacademy.bookstore.point.service.PointTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointTypeServiceImpl implements PointTypeService {

    private final PointTypeRepository pointTypeRepository;

    @Transactional
    public Long createPointType(CreatePointTypeRequestDto dto) {
        PointType pointType = PointType.builder()
                .type(dto.type())
                .accVal(dto.accVal())
                .name(dto.name())
                .isActive(dto.isActive())
                .build();

        return pointTypeRepository.save(pointType).getPointTypeId();
    }

    @Transactional
    public UpdatePointTypeResponseDto updatePointType(Long pointTypeId, UpdatePointTypeRequestDto dto) {
        PointType pointType = pointTypeRepository.findById(pointTypeId)
                .orElseThrow(PointTypeNotFoundException::new);

        // 수동 업데이트
        pointType.updatePointType(dto.type(), dto.accVal(), dto.name(), dto.isActive());
        PointType updatedPointType = pointTypeRepository.save(pointType);

        // 수동 매핑
        return new UpdatePointTypeResponseDto(
                updatedPointType.getPointTypeId(),
                updatedPointType.getType(),
                updatedPointType.getAccVal(),
                updatedPointType.getName(),
                updatedPointType.isActive()
        );
    }

    @Transactional
    @Override
    public List<GetPointTypeResponse> getAllActivePointTypes() {
        return pointTypeRepository.findAllActivePointTypes();
    }

    @Transactional
    public ReadPointTypeResponseDto getPointTypeById(Long typeId) {
        PointType pointType = pointTypeRepository.findById(typeId)
                .orElseThrow(PointTypeNotFoundException::new);

        return new ReadPointTypeResponseDto(
                pointType.getPointTypeId(),
                pointType.getType(),
                pointType.getAccVal(),
                pointType.getName(),
                pointType.isActive()
        );
    }
}
