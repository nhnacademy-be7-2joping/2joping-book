package com.nhnacademy.bookstore.common.error.common;

import com.nhnacademy.bookstore.common.error.exception.base.BadRequestException;

/**
 * InvalidPathVariableException은 유효하지 않은 경로 변수 값이 입력되었을 때 발생하는 예외입니다.
 * 예를 들어, PathVariable로 전달된 값이 음수인 경우 이 예외가 발생하도록 설정할 수 있습니다.
 * @author Luha
 * @since 1.0
 */
public class InvalidPathVariableException extends BadRequestException {

    /**
     * 예외 메시지를 받아 RuntimeException의 생성자로 전달합니다.
     *
     * @param message 예외 발생 시 출력할 상세 메시지
     */
    public InvalidPathVariableException(String message) {
        super(message);
    }

}
