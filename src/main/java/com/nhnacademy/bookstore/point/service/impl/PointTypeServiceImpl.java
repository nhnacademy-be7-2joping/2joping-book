package com.nhnacademy.bookstore.point.service.impl;

import com.nhnacademy.bookstore.common.error.exception.point.PointTypeNotFoundException;
import com.nhnacademy.bookstore.point.dto.request.CreatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.request.UpdatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.response.CreatePointTypeResponseDto;
import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;
import com.nhnacademy.bookstore.point.dto.response.UpdatePointTypeResponseDto;
import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.repository.PointTypeRepository;
import com.nhnacademy.bookstore.point.service.PointTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nhnacademy.bookstore.point.mapper.PointTypeMapper;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointTypeServiceImpl implements PointTypeService {

    private final PointTypeRepository pointTypeRepository;
    private final PointTypeMapper pointTypeMapper;

    @Transactional
    public CreatePointTypeResponseDto createPointType(CreatePointTypeRequestDto dto) {
        PointType pointType = pointTypeMapper.toEntity(dto);
        PointType savedPointType = pointTypeRepository.save(pointType);
        return pointTypeMapper.toCreateResponseDto(savedPointType);
    }

    @Transactional
    public UpdatePointTypeResponseDto updatePointType(Long id, UpdatePointTypeRequestDto dto) {
        PointType pointType = pointTypeRepository.findById(id)
                .orElseThrow(PointTypeNotFoundException::new);
        pointType.updatePointType(dto.type(),dto.accVal(),dto.name(),dto.isActive());
        PointType updatedPointType = pointTypeRepository.save(pointType);
        return pointTypeMapper.toUpdateResponseDto(updatedPointType);
    }


    @Transactional
    @Override
    public List<ReadPointTypeResponseDto> getAllActivePointTypes() {
        return pointTypeRepository.findAllActivePointTypes();
    }

    @Transactional
    public ReadPointTypeResponseDto getPointTypeById(Long typeId) {
        PointType pointType = pointTypeRepository.findById(typeId)
                .orElseThrow(PointTypeNotFoundException::new);
        return pointTypeMapper.toReadResponseDto(pointType);
    }
}
