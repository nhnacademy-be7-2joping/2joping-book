package com.nhnacademy.bookstore.user.member;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberDuplicateException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberPasswordNotEqualException;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.controller.MemberController;
import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberWithdrawRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberCreateSuccessResponseDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberPointResponse;
import com.nhnacademy.bookstore.user.member.dto.response.MemberUpdateResponseDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberWithdrawResponseDto;
import com.nhnacademy.bookstore.user.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Objects;

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
 class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    /**
     * 테스트: 회원 생성 성공 시 응답 검증
     * 예상 결과: 200 OK 응답 코드와 생성된 회원 정보 반환
     */
    @Test
     void testRegisterNewMember_Success() {
        // given
        MemberCreateRequestDto requestDto = new MemberCreateRequestDto(
                "testuser", "Test@1234", "이한빈", "010-1234-5678",
                "dlgksqls7218@naver.com", "루하", Gender.M, LocalDate.of(1996, 6, 23));

        MemberCreateSuccessResponseDto responseDto = new MemberCreateSuccessResponseDto("루하");
        when(memberService.registerNewMember(requestDto)).thenReturn(responseDto);

        // when
        ResponseEntity<MemberCreateSuccessResponseDto> response = memberController.addMember(requestDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("루하", Objects.requireNonNull(response.getBody()).getNickname());
        assertEquals("님 회원가입을 축하드립니다. 로그인해주세요", response.getBody().getMessage());
    }

    /**
     * 테스트: 회원 가입 시 중복 아이디 예외 발생 확인
     * 예상 결과: MemberDuplicateException 발생
     */
    @Test
     void testRegisterNewMember_DuplicateIdException() {
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
     void testUpdateMember_Success() {
        // given
        String customerId = "84";
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto("010-5678-1234","newemail@example.com", "루하업데이트"
        );

        MemberUpdateResponseDto responseDto = new MemberUpdateResponseDto(
                "이한빈", Gender.M, LocalDate.of(1996, 6, 23), "010-5678-1234","newemail@example.com", "루하업데이트"
        );

        when(memberService.updateMember(Long.parseLong(customerId), requestDto))
                .thenReturn(responseDto);

        // when
        ResponseEntity<MemberUpdateResponseDto> response = memberController.updateMember(customerId, requestDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("010-5678-1234", Objects.requireNonNull(response.getBody()).phone());
        assertEquals("newemail@example.com", response.getBody().email());
        assertEquals("루하업데이트", response.getBody().nickName());
    }

    /**
     * 테스트: 잘못된 헤더 또는 요청 데이터로 인해 업데이트 실패
     * 예상 결과: 적절한 예외 처리 및 응답 코드 반환
     */
    @Test
     void testUpdateMember_Failure() {
        // given
        String customerId = "84";
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto("010-5678-1234","newemail@example.com", "루하업데이트"
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
    /**
     * 테스트: 회원 정보 조회 성공
     * 예상 결과: 200 OK 응답 코드와 조회된 회원 정보 반환
     */
    @Test
     void testMemberInfo_Success() {
        // given
        String customerId = "84";
        MemberUpdateResponseDto responseDto = new MemberUpdateResponseDto(
                "이한빈", Gender.M, LocalDate.of(1996, 6, 23), "010-5678-1234", "test@example.com", "루하"
        );

        when(memberService.getMemberInfo(Long.parseLong(customerId))).thenReturn(responseDto);

        // when
        ResponseEntity<MemberUpdateResponseDto> response = memberController.memberInfo(customerId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("이한빈", Objects.requireNonNull(response.getBody()).name());
        assertEquals(Gender.M, response.getBody().gender());
        assertEquals(LocalDate.of(1996, 6, 23), response.getBody().birthday());
        assertEquals("010-5678-1234", response.getBody().phone());
        assertEquals("test@example.com", response.getBody().email());
        assertEquals("루하", response.getBody().nickName());
    }

    /**
     * 테스트: 회원 정보 조회 실패 (존재하지 않는 회원 ID)
     * 예상 결과: MemberNotFoundException 발생
     */
    @Test
     void testMemberInfo_Failure() {
        // given
        String customerId = "999"; // 존재하지 않는 ID
        doThrow(new MemberNotFoundException("해당 멤버가 존재하지 않습니다.", RedirectType.REDIRECT, "/members"))
                .when(memberService).getMemberInfo(Long.parseLong(customerId));

        // when & then
        MemberNotFoundException exception = assertThrows(
                MemberNotFoundException.class,
                () -> memberController.memberInfo(customerId)
        );

        assertEquals("해당 멤버가 존재하지 않습니다.", exception.getMessage());
    }
   @Test
   void testWithdrawMember_Success() {
      // given
      String customerId = "84";
      MemberWithdrawRequesteDto requestDto = new MemberWithdrawRequesteDto("CorrectPassword");

      MemberWithdrawResponseDto responseDto = new MemberWithdrawResponseDto("루하");
      when(memberService.withdrawMember(Long.parseLong(customerId), requestDto))
              .thenReturn(responseDto);

      // when
      ResponseEntity<MemberWithdrawResponseDto> response = memberController.withdrawMember(customerId, requestDto);

      // then
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(responseDto, response.getBody());
   }

   /**
    * 테스트: 회원 탈퇴 요청 실패 - 비밀번호 불일치
    * 예상 결과: MemberPasswordNotEqualException 발생
    */
   @Test
   void testWithdrawMember_Failure_PasswordNotEqual() {
      // given
      String customerId = "84";
      MemberWithdrawRequesteDto requestDto = new MemberWithdrawRequesteDto("WrongPassword");

      doThrow(MemberPasswordNotEqualException.class)
              .when(memberService).withdrawMember(Long.parseLong(customerId), requestDto);

      // when & then
      assertThrows(MemberPasswordNotEqualException.class,
              () -> memberController.withdrawMember(customerId, requestDto));
   }

   @Test
   @DisplayName("멤버 포인트 조회")
   void testMemberPoint() {
      Long customerId = 84L;
      MemberPointResponse mockPoint = new MemberPointResponse(100);

      when(memberService.getPointsOfMember(customerId)).thenReturn(mockPoint);
      ResponseEntity<MemberPointResponse> actualResponse = memberController.getPoint(customerId);

      assertEquals(actualResponse.getStatusCode(), HttpStatus.OK);
      assertEquals(mockPoint, actualResponse.getBody());
   }

   @Test
   @DisplayName("없는 멤버의 포인트 조회")
   void testMemberPoint_MemberNotFound() {
      Long customerId = 84L;

      when(memberService.getPointsOfMember(customerId)).thenThrow(MemberNotFoundException.class);
      assertThrows(MemberNotFoundException.class, () -> memberController.getPoint(customerId));
   }
}