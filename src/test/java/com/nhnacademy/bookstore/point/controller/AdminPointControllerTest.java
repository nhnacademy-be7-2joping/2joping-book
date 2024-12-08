package com.nhnacademy.bookstore.point.controller;

import com.nhnacademy.bookstore.point.dto.request.CreatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.request.UpdatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.response.GetPointTypeResponse;
import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;
import com.nhnacademy.bookstore.point.dto.response.UpdatePointTypeResponseDto;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(AdminPointController.class)
class AdminPointControllerTest {

    @Autowired
    private AdminPointController adminPointController;

    @MockBean
    private PointTypeServiceImpl pointTypeServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPointType() {
        CreatePointTypeRequestDto requestDto = new CreatePointTypeRequestDto(
                PointTypeEnum.ACTUAL, 100, "Test Point", true
        );

        when(pointTypeServiceImpl.createPointType(any(CreatePointTypeRequestDto.class))).thenReturn(1L);

        ResponseEntity<Void> response = adminPointController.createPointType(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(URI.create("/api/v1/admin/pointtypes/1"), response.getHeaders().getLocation());
    }

    @Test
    void updatePointType() {
        Long typeId = 1L;
        UpdatePointTypeRequestDto requestDto = new UpdatePointTypeRequestDto(
                PointTypeEnum.ACTUAL, 150, "Updated Point", false
        );
        UpdatePointTypeResponseDto responseDto = new UpdatePointTypeResponseDto(typeId, PointTypeEnum.ACTUAL, 150, "Updated Point", false);

        when(pointTypeServiceImpl.updatePointType(eq(typeId), any(UpdatePointTypeRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<UpdatePointTypeResponseDto> response = adminPointController.updatePointType(typeId, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void readPointType() {
        GetPointTypeResponse pointTypeDto = new GetPointTypeResponse(1L, PointTypeEnum.ACTUAL, 100, "Test Point", true);
        List<GetPointTypeResponse> pointTypes = List.of(pointTypeDto);

        when(pointTypeServiceImpl.getAllActivePointTypes()).thenReturn(pointTypes);

        ResponseEntity<List<GetPointTypeResponse>> response = adminPointController.readPointType();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pointTypes, response.getBody());
    }

    @Test
    void getPointTypeById() {
        Long typeId = 1L;
        ReadPointTypeResponseDto responseDto = new ReadPointTypeResponseDto(typeId, PointTypeEnum.ACTUAL, 100, "Test Point", true);

        when(pointTypeServiceImpl.getPointTypeById(typeId)).thenReturn(responseDto);

        ResponseEntity<ReadPointTypeResponseDto> response = adminPointController.getPointTypeById(typeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }
}
