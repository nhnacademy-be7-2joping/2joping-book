package com.nhnacademy.bookstore.common.error.handler;

import com.nhnacademy.bookstore.common.error.dto.ErrorResponseDto;
import com.nhnacademy.bookstore.common.error.exception.base.*;
import org.springframework.http.ResponseEntity;

/**
 * BaseExceptionHandler
 * 이 인터페이스는 다양한 예외 상황에서 공통적으로 처리할 예외 핸들러 메서드를 정의합니다.
 * 각 예외 타입에 따라 적절한 HTTP 응답을 생성하며, 기본적으로는 ErrorResponseDto 형태로 클라이언트에 전달됩니다.
 *
 * @author Luha
 * @since 1.0
 */
public interface BaseExceptionHandler {
    ResponseEntity<ErrorResponseDto<Object>> handleBadRequestExceptions(BadRequestException ex);
    ResponseEntity<ErrorResponseDto<Object>> handleUnauthorizedExceptions(UnauthorizedException ex);
    ResponseEntity<ErrorResponseDto<Object>> handleForbiddenExceptions(ForbiddenException ex);
    ResponseEntity<ErrorResponseDto<Object>> handleNotFoundExceptions(NotFoundException ex);
    ResponseEntity<ErrorResponseDto<Object>> handleConflictExceptions(ConflictException ex);
    ResponseEntity<ErrorResponseDto<Object>> handleAllExceptions(Exception ex);
}
