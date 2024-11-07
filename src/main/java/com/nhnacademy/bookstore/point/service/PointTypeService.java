package com.nhnacademy.bookstore.point.service;

import com.nhnacademy.bookstore.common.error.exception.point.PointPolicyNotFoundException;
import com.nhnacademy.bookstore.point.dto.request.PointTypeDto;
import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.repository.PointTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointTypeService {

    private final PointTypeRepository pointTypeRepository;

    @Transactional
    public PointTypeDto createPointType(PointTypeDto dto) {
        PointType pointType = PointType.builder()
                .type(dto.type())
                .accVal(dto.accVal())
                .name(dto.name())
                .build();

        return PointTypeDto.from(pointTypeRepository.save(pointType));
    }

    @Transactional
    public PointTypeDto updatePointType(Long id, PointTypeDto dto) {
        PointType pointType = pointTypeRepository.findById(id)
                .orElseThrow(() -> new PointPolicyNotFoundException("포인트 정책을 찾을 수 없습니다."));

        pointType.updateAccVal(dto.accVal());

        return PointTypeDto.from(pointType);
    }

    @Transactional
    public List<PointTypeDto> getAllActivePointTypes() {
        return pointTypeRepository.findAllByIsActiveTrue().stream()
                .map(PointTypeDto::from)
                .toList();
    }
}
