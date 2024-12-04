package com.nhnacademy.bookstore.common.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.common.error.dto.ErrorResponseDto;
import com.nhnacademy.bookstore.common.error.exception.base.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * GlobalExceptionHandlerTest 클래스는 GlobalExceptionHandler의 예외 처리 로직을 검증하기 위한 테스트 클래스입니다.
 * 주요 테스트 항목:
 * - 각종 커스텀 예외 처리 (`BadRequestException`, `UnauthorizedException`, `ForbiddenException`, `NotFoundException`, `ConflictException`)
 * - Validation 예외 처리 및 JSON 메시지 생성 확인
 * - 모든 일반적인 예외 처리 로직 검증
 * - 예외 처리 중 JSON 변환 실패 시 동작 확인
 *
 * @author Luha
 * @version 1.0
 */
class GlobalExceptionHandlerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler(objectMapper);


    @Test
    @DisplayName("BadRequestException 처리 테스트")
    void handleBadRequestExceptions() {
        BadRequestException exception = new BadRequestException("잘못된 요청입니다.", null, null, null);

        ResponseEntity<ErrorResponseDto<Object>> response = exceptionHandler.handleBadRequestExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("BAD_REQUEST", Objects.requireNonNull(response.getBody()).errorCode());
        assertEquals("잘못된 요청입니다.", response.getBody().errorMessage());
    }

    @Test
    @DisplayName("Validation 예외 처리 테스트")
    void handleValidationExceptions()  {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("objectName", "fieldName", "유효하지 않은 값입니다.");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponseDto<Object>> response = exceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALIDATION_FAILED", Objects.requireNonNull(response.getBody()).errorCode());
        assertTrue(response.getBody().errorMessage().contains("\"fieldName\":\"유효하지 않은 값입니다.\""));
    }

    @Test
    @DisplayName("UnauthorizedException 처리 테스트")
    void handleUnauthorizedExceptions() {
        UnauthorizedException exception = new UnauthorizedException("인증 실패", null, null, null);

        ResponseEntity<ErrorResponseDto<Object>> response = exceptionHandler.handleUnauthorizedExceptions(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("UNAUTHORIZED", Objects.requireNonNull(response.getBody()).errorCode());
        assertEquals("인증 실패", response.getBody().errorMessage());
    }

    @Test
    @DisplayName("ForbiddenException 처리 테스트")
    void handleForbiddenExceptions() {
        ForbiddenException exception = new ForbiddenException("접근 금지", null, null, null);

        ResponseEntity<ErrorResponseDto<Object>> response = exceptionHandler.handleForbiddenExceptions(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("FORBIDDEN", Objects.requireNonNull(response.getBody()).errorCode());
        assertEquals("접근 금지", response.getBody().errorMessage());
    }

    @Test
    @DisplayName("NotFoundException 처리 테스트")
    void handleNotFoundExceptions() {
        NotFoundException exception = new NotFoundException("찾을 수 없음", null, null, null);

        ResponseEntity<ErrorResponseDto<Object>> response = exceptionHandler.handleNotFoundExceptions(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NOT_FOUND", Objects.requireNonNull(response.getBody()).errorCode());
        assertEquals("찾을 수 없음", response.getBody().errorMessage());
    }

    @Test
    @DisplayName("ConflictException 처리 테스트")
    void handleConflictExceptions() {
        ConflictException exception = new ConflictException("충돌 발생", null, null, null);

        ResponseEntity<ErrorResponseDto<Object>> response = exceptionHandler.handleConflictExceptions(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("CONFLICT", Objects.requireNonNull(response.getBody()).errorCode());
        assertEquals("충돌 발생", response.getBody().errorMessage());
    }

    @Test
    @DisplayName("모든 예외 처리 테스트")
    void handleAllExceptions() {
        Exception exception = new Exception("서버 에러 발생");

        ResponseEntity<ErrorResponseDto<Object>> response = exceptionHandler.handleAllExceptions(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_SERVER_ERROR", Objects.requireNonNull(response.getBody()).errorCode());
        assertEquals("서버 에러 발생", response.getBody().errorMessage());
    }
    @Test
    @DisplayName("Validation 중복 필드 에러 처리 테스트")
    void testValidationDuplicateFieldError() {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError error1 = new FieldError("objectName", "fieldName", "첫 번째 메시지");
        FieldError error2 = new FieldError("objectName", "fieldName", "두 번째 메시지"); // 같은 키지만 다른 메시지

        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // Act
        ResponseEntity<ErrorResponseDto<Object>> response = exceptionHandler.handleValidationExceptions(exception);

        // Assert
        String responseMessage = Objects.requireNonNull(response.getBody()).errorMessage();
        assertTrue(responseMessage.contains("\"fieldName\":\"첫 번째 메시지\""));
        assertFalse(responseMessage.contains("\"fieldName\":\"두 번째 메시지\"")); // 두 번째 메시지가 포함되지 않음
    }

    @Test
    @DisplayName("Validation 예외 처리 - JSON 변환 실패")
    void testHandleValidationExceptionsJsonError() throws Exception {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError error = new FieldError("objectName", "fieldName", "유효하지 않은 값입니다.");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        when(mockObjectMapper.writeValueAsString(any(Map.class)))
                .thenThrow(new RuntimeException("JSON 변환 오류"));

        GlobalExceptionHandler handler = new GlobalExceptionHandler(mockObjectMapper);

        // Act
        ResponseEntity<ErrorResponseDto<Object>> response = handler.handleValidationExceptions(exception);

        // Assert
        assertEquals(400, Objects.requireNonNull(response.getBody()).status());
        assertEquals("VALIDATION_FAILED", response.getBody().errorCode());
        assertEquals("JSON 변환 중 오류가 발생했습니다.", response.getBody().errorMessage());
    }
}