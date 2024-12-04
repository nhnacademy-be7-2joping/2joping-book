package com.nhnacademy.bookstore.common.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.common.error.dto.ErrorResponseDto;
import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;
import com.nhnacademy.bookstore.common.error.exception.base.ForbiddenException;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
import com.nhnacademy.bookstore.common.error.exception.base.UnauthorizedException;
import com.nhnacademy.bookstore.common.error.exception.base.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;


/**
 * GlobalExceptionHandler
 * 이 클래스는 애플리케이션에서 발생하는 다양한 예외들을 전역적으로 처리하는 핸들러입니다.
 * 각 예외 유형에 따라 HTTP 상태 코드와 커스텀 메시지를 포함한 응답을 생성하며,
 * 추가적인 리다이렉션 타입, URL 및 데이터를 사용할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler implements BaseExceptionHandler{

    private final ObjectMapper objectMapper;

    // 400 - Bad Request 예외 처리
    @Override
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto<Object>> handleBadRequestExceptions(BadRequestException ex) {

        ErrorResponseDto<Object> errorResponse = new ErrorResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                ex.getMessage(),
                ex.getRedirectType(),
                ex.getUrl(),
                ex.getData()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    // 400 - Validation Error 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();

        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing // 기존 값 유지
                ));

        // JSON 변환
        String errorMessage;
        try {
            errorMessage = objectMapper.writeValueAsString(errors);
        } catch (Exception e) {
            errorMessage = "JSON 변환 중 오류가 발생했습니다.";
        }

        ErrorResponseDto<Object> errorResponse = new ErrorResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_FAILED",
                errorMessage,
                null,
                null,
                null
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 401 - Unauthorized 예외 처리
    @Override
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto<Object>> handleUnauthorizedExceptions(UnauthorizedException ex) {

        ErrorResponseDto<Object> errorResponse = new ErrorResponseDto<>(
                HttpStatus.UNAUTHORIZED.value(),
                "UNAUTHORIZED",
                ex.getMessage(),
                ex.getRedirectType(),
                ex.getUrl(),
                ex.getData()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);

    }

    // 403 - Forbidden 예외 처리
    @Override
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDto<Object>> handleForbiddenExceptions(ForbiddenException ex) {

        ErrorResponseDto<Object> errorResponse = new ErrorResponseDto<>(
                HttpStatus.FORBIDDEN.value(),
                "FORBIDDEN",
                ex.getMessage(),
                ex.getRedirectType(),
                ex.getUrl(),
                ex.getData()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);

    }

    // 404 - Not Found 예외 처리
    @Override
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto<Object>> handleNotFoundExceptions(NotFoundException ex) {

        ErrorResponseDto<Object> errorResponse = new ErrorResponseDto<>(
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                ex.getMessage(),
                ex.getRedirectType(),
                ex.getUrl(),
                ex.getData()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

    // 409 - Conflict 예외 처리
    @Override
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto<Object>> handleConflictExceptions(ConflictException ex) {

        ErrorResponseDto<Object> errorResponse = new ErrorResponseDto<>(
                HttpStatus.CONFLICT.value(),
                "CONFLICT",
                ex.getMessage(),
                ex.getRedirectType(),
                ex.getUrl(),
                ex.getData()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);

    }

    // 500 - Internal Server Error 예외 처리
    @Override
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto<Object>> handleAllExceptions(Exception ex) {

        ErrorResponseDto<Object> errorResponse = new ErrorResponseDto<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                ex.getMessage(),
                null,
                null,
                null
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
