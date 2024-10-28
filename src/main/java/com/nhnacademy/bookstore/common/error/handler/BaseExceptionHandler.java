package com.nhnacademy.bookstore.common.error.handler;

import com.nhnacademy.bookstore.common.error.dto.ErrorResponseDto;
import com.nhnacademy.bookstore.common.error.exception.base.*;
import org.springframework.http.ResponseEntity;

public interface BaseExceptionHandler {
    ResponseEntity<ErrorResponseDto> handleBadRequestExceptions(BadRequestException ex);
    ResponseEntity<ErrorResponseDto> handleUnauthorizedExceptions(UnauthorizedException ex);
    ResponseEntity<ErrorResponseDto> handleForbiddenExceptions(ForbiddenException ex);
    ResponseEntity<ErrorResponseDto> handleNotFoundExceptions(NotFoundException ex);
    ResponseEntity<ErrorResponseDto> handleConflictExceptions(ConflictException ex);
    ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex);
}
