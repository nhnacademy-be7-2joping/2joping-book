package com.nhnacademy.bookstore.common.aop.pathvariable;


import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import com.nhnacademy.bookstore.common.error.common.InvalidPathVariableException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * PathvariableAspect는 경로 변수에 대한 유효성 검사를 수행하는 AOP 클래스입니다.
 * 메서드에 선언된 @ValidPathVariable 어노테이션을 통해 경로 변수의 유효성을 검증합니다.
 * 음수 값이 경로 변수로 사용될 경우, InvalidPathVariableException 예외를 발생시켜 잘못된 경로 변수를 처리합니다.
 *
 * @author Luha
 * @since 1.0
 */

@Aspect
@Component
public class PathvariableAspect {

    /**
     * validPathVariableMethods()는 @ValidPathVariable 어노테이션이 적용된 모든 메서드의 포인트컷을 정의합니다.
     */
    @Pointcut("execution(* com.nhnacademy.bookstore..*(.., @com.nhnacademy.bookstore.common.annotation.ValidPathVariable (*), ..))")
    public void validPathVariableMethods() {}


    /**
     * checkPathVariable()는 validPathVariableMethods() 포인트컷에서 지정된 메서드가 성공적으로 실행된 후
     * 경로 변수 값의 유효성을 검사하는 메서드입니다.
     *
     * @param joinPoint 실행된 메서드의 정보를 담고 있는 JoinPoint 객체
     * @throws InvalidPathVariableException 경로 변수에 음수 값이 전달될 경우 발생하는 예외
     */
    @Before("validPathVariableMethods()")
    public void checkPathVariable(JoinPoint joinPoint){

        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(ValidPathVariable.class)) {
                checkValue(args[i]);
            }
        }
    }

    /**
     * checkValue()는 주어진 경로 변수의 값이 유효한지 검사합니다.
     * 값이 음수일 경우 InvalidPathVariableException을 발생시킵니다.
     *
     * @param value 경로 변수 값
     * @param <T>   타입 매개변수
     * @throws InvalidPathVariableException 음수 값이 전달될 경우 발생하는 예외
     */
    public <T> void checkValue(T value) {

        if (value == null) {
            throw new InvalidPathVariableException("값이 null입니다.");
        }

        Class<?> valueType = value.getClass();

        if (valueType == Long.class) {
            long pathVarValue = (Long) value;
            if (pathVarValue <= 0) {
                throw new InvalidPathVariableException("0 또는 음수 값은 허용되지 않습니다.");
            }
        } else if (valueType == Integer.class) {
            int pathVarValue = (Integer) value;
            if (pathVarValue <= 0) {
                throw new InvalidPathVariableException("0 또는 음수 값은 허용되지 않습니다.");
            }
        }
    }
}
