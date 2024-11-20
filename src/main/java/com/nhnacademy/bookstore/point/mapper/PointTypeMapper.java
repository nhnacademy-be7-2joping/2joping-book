package com.nhnacademy.bookstore.point.mapper;

import com.nhnacademy.bookstore.point.dto.request.CreatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.response.CreatePointTypeResponseDto;
import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;
import com.nhnacademy.bookstore.point.dto.response.UpdatePointTypeResponseDto;
import com.nhnacademy.bookstore.point.entity.PointType;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PointTypeMapper {

    PointType toEntity(CreatePointTypeRequestDto requestDto);

    CreatePointTypeResponseDto toCreateResponseDto(PointType entity);

    ReadPointTypeResponseDto toReadResponseDto(PointType entity);

    UpdatePointTypeResponseDto toUpdateResponseDto(PointType entity);
}
