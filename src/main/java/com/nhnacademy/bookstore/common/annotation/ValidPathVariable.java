package com.nhnacademy.bookstore.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이 어노테이션은 경로 변수(Path Variable) 유효성을 검사하기 위한 커스텀 어노테이션입니다.
 * 주로 AOP와 함께 사용되어 메서드 파라미터가 유효한 값인지 검사합니다.
 * 예를 들어, 음수 값이 허용되지 않는 경로 변수를 정의하고 싶을 때, 해당 변수에 이 어노테이션을 추가하고
 * AOP를 통해 유효성을 검사할 수 있습니다.
 * .
 * &#64;ValidPathVariable
 * public ResponseEntity<?> getUser(@ValidPathVariable Integer userId) {
 *     // userId가 음수이면 AOP에서 예외 처리됨
 * }
 * 이 어노테이션은 메서드 파라미터에만 사용할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPathVariable {
}
