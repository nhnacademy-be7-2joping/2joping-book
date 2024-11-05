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

public class PathvariableAspectTest {

    private PathvariableAspect pathvariableAspect;
    private JoinPoint joinPoint;

    @BeforeEach
    public void setUp() {
        pathvariableAspect = new PathvariableAspect();
        joinPoint = Mockito.mock(JoinPoint.class);
    }

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

        assertEquals("음수 값은 허용되지 않습니다.", exception.getMessage());
    }

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
        assertEquals("음수 값은 허용되지 않습니다.", exception.getMessage());
    }

    @Test
    public void testValidPathVariableMethodIsNotIntercepted() throws Throwable {
        // Arrange
        AspectJProxyFactory factory = new AspectJProxyFactory(new TestService());
        factory.addAspect(pathvariableAspect);
        TestService proxy = factory.getProxy();

        // Act & Assert
        assertDoesNotThrow(() -> proxy.addMemberAddress(1L));
    }

    @Test
    public void testValidPathVariable_WithValidInteger() {
        // Arrange
        AspectJProxyFactory factory = new AspectJProxyFactory(new TestService());
        factory.addAspect(pathvariableAspect);
        TestService proxy = factory.getProxy();

        // Act & Assert
        assertDoesNotThrow(() -> proxy.integerMethod(10)); // 유효한 Integer 값
    }

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
        assertEquals("음수 값은 허용되지 않습니다.", exception.getMessage());
    }



    // 테스트를 위한 서비스 클래스
    public static class TestService {
        public void addMemberAddress(@ValidPathVariable Long memberId) {
            // AOP가 적용된 메서드
        }

        public void integerMethod(@ValidPathVariable int memberId) {
            // AOP가 적용된 메서드
        }
    }
}