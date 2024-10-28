package com.nhnacademy.bookstore.common.error.handler;

import com.nhnacademy.bookstore.common.error.dto.ErrorResponseDto;
import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;
import com.nhnacademy.bookstore.common.error.exception.base.ForbiddenException;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
import com.nhnacademy.bookstore.common.error.exception.base.UnauthorizedException;
import com.nhnacademy.bookstore.common.error.exception.base.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler implements BaseExceptionHandler{
    // 401 error
    @Override
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestExceptions(BadRequestException ex) {

        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    // 401 validation error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_FAILED",
                errors.toString()  // 상세한 오류 메시지
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 402 error
    @Override
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedExceptions(UnauthorizedException ex) {

        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);

    }
    // 403 error
    @Override
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDto> handleForbiddenExceptions(ForbiddenException ex) {

        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.FORBIDDEN, "FORBIDDEN", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);

    }
    // 404 error
    @Override
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundExceptions(NotFoundException ex) {

        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

    // 409 error
    @Override
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleConflictExceptions(ConflictException ex) {

        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);

    }
    // 500 error
    @Override
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex) {

        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
