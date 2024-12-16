package com.nhnacademy.bookstore.common.aop;

import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import com.nhnacademy.bookstore.common.aop.pathvariable.PathvariableAspect;
import com.nhnacademy.bookstore.common.error.common.InvalidPathVariableException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PathvariableAspectTest는 PathvariableAspect의 기능을 검증하기 위한 테스트 클래스입니다.
 * 주요 테스트 항목:
 * - @ValidPathVariable 어노테이션이 적용된 PathVariable의 유효성 검증
 * - 음수 또는 null 값을 전달했을 때 예외 처리 검증
 * - 어노테이션이 없는 메서드 호출 시 정상 동작 여부 확인
 *
 * @author Luha
 * @version 1.0
 */
class PathvariableAspectTest {

    private PathvariableAspect pathvariableAspect;
    private JoinPoint joinPoint;

    /**
     * 각 테스트 메서드 실행 전에 호출되는 설정 메서드입니다.
     * AOP 객체와 JoinPoint를 초기화합니다.
     */
    @BeforeEach
    void setUp() {
        pathvariableAspect = new PathvariableAspect();
        joinPoint = Mockito.mock(JoinPoint.class);
    }


    /**
     * 유효한 Long 값이 주어졌을 때, 예외가 발생하지 않는지 확인하는 테스트입니다.
     */
    @Test
    @DisplayName("유효한 Long PathVariable 처리 테스트")
    void testCheckValue_WithValidLong() throws Throwable {
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
    @DisplayName("음수 Long PathVariable 처리 예외 테스트")
    void testCheckValue_WithNegativeLong() throws Throwable {
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
    @DisplayName("음수 PathVariable 전달 시 AOP 적용 테스트")
    void testValidPathVariableMethodIsIntercepted() throws Throwable {
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
    @DisplayName("유효한 PathVariable 전달 시 예외 발생 안 함 테스트")
    void testValidPathVariableMethodIsNotIntercepted() throws Throwable {
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
    @DisplayName("유효한 Integer PathVariable 처리 테스트")
    void testValidPathVariable_WithValidInteger() {
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
    @DisplayName("음수 Integer PathVariable 처리 예외 테스트")
    void testValidPathVariable_WithNegativeInteger() {
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

    @Test
    @DisplayName("PathVariable이 null일 때 예외 발생 테스트")
    void testValidPathVariable_WithNullValue() {
        // Arrange
        AspectJProxyFactory factory = new AspectJProxyFactory(new TestService());
        factory.addAspect(pathvariableAspect);
        TestService proxy = factory.getProxy();

        // Act & Assert
        InvalidPathVariableException exception = assertThrows(InvalidPathVariableException.class, () -> {
            proxy.addMemberAddress(null); // null 값 전달
        });
        assertEquals("값이 null입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("PathVariable이 null인 Integer 값 테스트")
    void testNullIntegerValue() {
        // Arrange
        AspectJProxyFactory factory = new AspectJProxyFactory(new TestService());
        factory.addAspect(pathvariableAspect);
        TestService proxy = factory.getProxy();

        // Act & Assert
        InvalidPathVariableException exception = Assertions.<InvalidPathVariableException>assertThrows(InvalidPathVariableException.class, () -> {
            proxy.integerMethod(null); // null 값 전달
        });
        assertEquals("값이 null입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("Integer가 아닌 타입 처리 테스트")
    void testNonIntegerTypeValue() {
        assertDoesNotThrow(() -> pathvariableAspect.checkValue("dd")); // Long 타입
    }

    @Test
    @DisplayName("파라미터에 @ValidPathVariable이 없는 경우 테스트")
    void testParameterWithoutValidPathVariableAnnotation() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("noAnnotationMethod", Long.class);
        Parameter[] parameters = method.getParameters();

        // Mock JoinPoint
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(signature.getMethod()).thenReturn(method);
        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{5L});

        // Act
        pathvariableAspect.checkPathVariable(joinPoint);

        // Assert
        assertFalse(parameters[0].isAnnotationPresent(ValidPathVariable.class));
    }


    @Test
    @DisplayName("포인트컷 정의 메서드 직접 호출 테스트")
    void testPointcutDefinitionMethod() {
        // Arrange & Act
        pathvariableAspect.validPathVariableMethods();

        // Assert
        // 단순 호출이므로 별도 Assertion 필요 없음
        assertDoesNotThrow(() -> pathvariableAspect.validPathVariableMethods()); // 호출에 대한 검증 추가

    }

    /**
     * 테스트를 위한 서비스 클래스
     */
    public static class TestService {
        public void addMemberAddress(@ValidPathVariable Long memberId) {
            // 현재 구현 필요 없음. PathvariableAspect가 이 메서드의 호출을 가로챕니다.

        }

        public void intMethod(@ValidPathVariable int memberId) {
            // 현재 구현 필요 없음. PathvariableAspect가 이 메서드의 호출을 가로챕니다.

        }

        public void integerMethod(@ValidPathVariable Integer memberId) {
            // 현재 구현 필요 없음. PathvariableAspect가 이 메서드의 호출을 가로챕니다.

        }

        public void noAnnotationMethod(Long memberId) {
            // ValidPathVariable 어노테이션이 없는 메서드
        }
    }
}