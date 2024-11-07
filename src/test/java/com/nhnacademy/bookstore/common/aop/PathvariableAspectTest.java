package com.nhnacademy.bookstore.common.aop;

import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import com.nhnacademy.bookstore.common.aop.pathvariable.PathvariableAspect;
import com.nhnacademy.bookstore.common.error.common.InvalidPathVariableException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PathvariableAspectTest는 PathvariableAspect의 기능을 검증하기 위한 테스트 클래스입니다.
 * @author Luha
 * @version 1.0
 */
public class PathvariableAspectTest {

    private PathvariableAspect pathvariableAspect;
    private JoinPoint joinPoint;

    /**
     * 각 테스트 메서드 실행 전에 호출되는 설정 메서드입니다.
     * AOP 객체와 JoinPoint를 초기화합니다.
     */
    @BeforeEach
    public void setUp() {
        pathvariableAspect = new PathvariableAspect();
        joinPoint = Mockito.mock(JoinPoint.class);
    }


    /**
     * 유효한 Long 값이 주어졌을 때, 예외가 발생하지 않는지 확인하는 테스트입니다.
     */
    @Test
    public void testCheckValue_WithValidLong() throws Throwable {
        // given
        long validValue = 5L;
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{validValue});
        Method method = TestService.class.getMethod("addMemberAddress", Long.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(signature.getMethod()).thenReturn(method);
        Mockito.when(joinPoint.getSignature()).thenReturn(signature);

        // when
        pathvariableAspect.checkPathVariable(joinPoint);

        // then no exception should be thrown
    }

    /**
     * 음수 Long 값이 주어졌을 때, InvalidPathVariableException이 발생하는지 확인하는 테스트입니다.
     */
    @Test
    public void testCheckValue_WithNegativeLong() throws Throwable {
        // given
        long negativeValue = -5L;
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{negativeValue});
        Method method = TestService.class.getMethod("addMemberAddress", Long.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(signature.getMethod()).thenReturn(method);
        Mockito.when(joinPoint.getSignature()).thenReturn(signature);

        // when & then
        Exception exception = assertThrows(InvalidPathVariableException.class, () -> {
            pathvariableAspect.checkPathVariable(joinPoint);
        });

        assertEquals("0 또는 음수 값은 허용되지 않습니다.", exception.getMessage());
    }

    /**
     * AOP가 적용된 메서드에서 음수 값을 전달했을 때, InvalidPathVariableException이 발생하는지 확인하는 테스트입니다.
     */
    @Test
    public void testValidPathVariableMethodIsIntercepted() throws Throwable {
        // Arrange
        AspectJProxyFactory factory = new AspectJProxyFactory(new TestService());
        factory.addAspect(pathvariableAspect);
        TestService proxy = factory.getProxy();

        // Act
        Exception exception = assertThrows(InvalidPathVariableException.class, () -> {
            proxy.addMemberAddress(-1L);
        });

        // Assert
        assertEquals("0 또는 음수 값은 허용되지 않습니다.", exception.getMessage());
    }

    /**
     * AOP가 적용된 메서드에서 유효한 값을 전달했을 때, 예외가 발생하지 않는지 확인하는 테스트입니다.
     */
    @Test
    public void testValidPathVariableMethodIsNotIntercepted() throws Throwable {
        // Arrange
        AspectJProxyFactory factory = new AspectJProxyFactory(new TestService());
        factory.addAspect(pathvariableAspect);
        TestService proxy = factory.getProxy();

        // Act & Assert
        assertDoesNotThrow(() -> proxy.addMemberAddress(1L));
    }

    /**
     * AOP가 적용된 메서드에서 유효한 Integer 값을 전달했을 때, 예외가 발생하지 않는지 확인하는 테스트입니다.
     */
    @Test
    public void testValidPathVariable_WithValidInteger() {
        // Arrange
        AspectJProxyFactory factory = new AspectJProxyFactory(new TestService());
        factory.addAspect(pathvariableAspect);
        TestService proxy = factory.getProxy();

        // Act & Assert
        assertDoesNotThrow(() -> proxy.integerMethod(10)); // 유효한 Integer 값
    }

    /**
     * AOP가 적용된 메서드에서 음수 Integer 값을 전달했을 때, InvalidPathVariableException이 발생하는지 확인하는 테스트입니다.
     */
    @Test
    public void testValidPathVariable_WithNegativeInteger() {
        // Arrange
        AspectJProxyFactory factory = new AspectJProxyFactory(new TestService());
        factory.addAspect(pathvariableAspect);
        TestService proxy = factory.getProxy();

        // Act & Assert
        InvalidPathVariableException exception = assertThrows(InvalidPathVariableException.class, () -> {
            proxy.integerMethod(-1); // 음수 Integer 값
        });
        assertEquals("0 또는 음수 값은 허용되지 않습니다.", exception.getMessage());
    }


    /**
     * 테스트를 위한 서비스 클래스
     */
    public static class TestService {
        public void addMemberAddress(@ValidPathVariable Long memberId) {
        }

        public void integerMethod(@ValidPathVariable int memberId) {
        }
    }
}