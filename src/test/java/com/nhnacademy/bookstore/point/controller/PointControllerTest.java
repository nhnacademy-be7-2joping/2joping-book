package com.nhnacademy.bookstore.point.controller;

import com.nhnacademy.bookstore.point.dto.response.*;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import com.nhnacademy.bookstore.point.service.impl.PointServiceImpl;
import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private PointController pointController;

    @MockBean
    private PointServiceImpl pointServiceImpl;

    @MockBean
    private PointTypeServiceImpl pointTypeServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSimplePointHistories() {
        String customerId = "1";
        int memberPoint = 100; // 가짜 멤버 포인트 !!
        GetSimplePointHistoriesResponse simplePointHistory = new GetSimplePointHistoriesResponse("Test Point", 50, LocalDateTime.now());
        List<GetSimplePointHistoriesResponse> simplePointHistories = List.of(simplePointHistory);

        GetMyPageSimplePointHistoriesResponse expectedResponse = new GetMyPageSimplePointHistoriesResponse(memberPoint, simplePointHistories);

        when(pointServiceImpl.getMyPageSimplePointHistories(anyLong())).thenReturn(expectedResponse);

        ResponseEntity<GetMyPageSimplePointHistoriesResponse> response = pointController.getSimplePointHistories(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void getDetailPointHistories() {
        String customerId = "1";
        int memberPoint = 100; // 가짜 멤버 포인트
        GetDetailPointHistoriesResponse detailPointHistory = new GetDetailPointHistoriesResponse("Detailed Point", 75, PointTypeEnum.ACTUAL, true, LocalDateTime.now());
        List<GetDetailPointHistoriesResponse> detailPointHistories = List.of(detailPointHistory);

        GetMyPageDetailPointHistoriesResponse expectedResponse = new GetMyPageDetailPointHistoriesResponse(memberPoint, detailPointHistories);

        when(pointServiceImpl.getMyPageDetailPointHistories(anyLong())).thenReturn(expectedResponse);

        ResponseEntity<GetMyPageDetailPointHistoriesResponse> response = pointController.getDetailPointHistories(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void getActivePointTypes() {
        GetPointTypeResponse pointTypeResponse = new GetPointTypeResponse(1L, PointTypeEnum.ACTUAL, 100, "Test Point", true);
        List<GetPointTypeResponse> expectedResponses = List.of(pointTypeResponse);

        when(pointTypeServiceImpl.getAllActivePointTypes()).thenReturn(expectedResponses);

        ResponseEntity<List<GetPointTypeResponse>> response = pointController.getActivePointTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponses, response.getBody());
    }
}
