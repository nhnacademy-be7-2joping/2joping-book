package com.nhnacademy.bookstore.user.member;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberDuplicateException;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.controller.MemberController;
import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberCreateSuccessResponseDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberUpdateResponseDto;
import com.nhnacademy.bookstore.user.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * 회원 컨트롤러 테스트
 *
 * @since 1.0
 * @author Luha
 */
@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    /**
     * 테스트: 회원 생성 성공 시 응답 검증
     * 예상 결과: 200 OK 응답 코드와 생성된 회원 정보 반환
     */
    @Test
    public void testRegisterNewMember_Success() {
        // given
        MemberCreateRequestDto requestDto = new MemberCreateRequestDto(
                "testuser", "Test@1234", "이한빈", "010-1234-5678",
                "dlgksqls7218@naver.com", "루하", Gender.M, LocalDate.of(1996, 6, 23));

        MemberCreateSuccessResponseDto responseDto = new MemberCreateSuccessResponseDto("루하");
        when(memberService.registerNewMember(requestDto)).thenReturn(responseDto);

        // when
        ResponseEntity<MemberCreateSuccessResponseDto> response = memberController.addMember(requestDto);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("루하", response.getBody().getNickname());
        assertEquals("님 회원가입을 축하드립니다. 로그인해주세요", response.getBody().getMESSAGE());
    }

    /**
     * 테스트: 회원 가입 시 중복 아이디 예외 발생 확인
     * 예상 결과: MemberDuplicateException 발생
     */
    @Test
    public void testRegisterNewMember_DuplicateIdException() {
        // given
        MemberCreateRequestDto requestDto = new MemberCreateRequestDto(
                "testuser", "Test@1234", "이한빈", "010-1234-5678",
                "dlgksqls7218@naver.com", "루하", Gender.M, LocalDate.of(1996, 6, 23));

        doThrow(new MemberDuplicateException("이미 사용 중인 아이디입니다.", RedirectType.REDIRECT, "/members", requestDto))
                .when(memberService).registerNewMember(requestDto);

        // when & then
        assertThrows(MemberDuplicateException.class, () -> memberController.addMember(requestDto));
    }

    @Test
    public void testUpdateMember_Success() {
        // given
        String customerId = "84";
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto(
                "이한빈", Gender.M, LocalDate.of(1996, 6, 23), "010-5678-1234","newemail@example.com", "루하업데이트"
        );

        MemberUpdateResponseDto responseDto = new MemberUpdateResponseDto(
                "이한빈", Gender.M, LocalDate.of(1996, 6, 23), "010-5678-1234","newemail@example.com", "루하업데이트"
        );

        when(memberService.updateMember(Long.parseLong(customerId), requestDto))
                .thenReturn(responseDto);

        // when
        ResponseEntity<MemberUpdateResponseDto> response = memberController.updateMember(customerId, requestDto);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("010-5678-1234", response.getBody().phone());
        assertEquals("newemail@example.com", response.getBody().email());
        assertEquals("루하업데이트", response.getBody().nickName());
    }

    /**
     * 테스트: 잘못된 헤더 또는 요청 데이터로 인해 업데이트 실패
     * 예상 결과: 적절한 예외 처리 및 응답 코드 반환
     */
    @Test
    public void testUpdateMember_Failure() {
        // given
        String customerId = "84";
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto(
                "이한빈", Gender.M, LocalDate.of(1996, 6, 23), "010-5678-1234","newemail@example.com", "루하업데이트"
        );

        doThrow(new IllegalArgumentException("유효하지 않은 이메일 형식입니다."))
                .when(memberService).updateMember(Long.parseLong(customerId), requestDto);

        // when & then
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> memberController.updateMember(customerId, requestDto)
        );

        assertEquals("유효하지 않은 이메일 형식입니다.", exception.getMessage());
    }


}