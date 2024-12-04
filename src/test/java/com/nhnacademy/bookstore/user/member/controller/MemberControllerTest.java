package com.nhnacademy.bookstore.user.member.controller;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberDuplicateException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberPasswordNotEqualException;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberWithdrawRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.*;
import com.nhnacademy.bookstore.user.member.service.MemberService;
import com.nhnacademy.bookstore.user.tier.enums.Tier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.*;

/**
 * MemberControllerTest
 * 이 클래스는 MemberController의 REST API를 테스트합니다.
 * 회원 등록, 업데이트, 조회, 삭제, 포인트 조회 등 주요 기능을 검증합니다.
 *
 * @since 1.0
 * author Luha
 */
@ExtendWith(MockitoExtension.class)
 class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // MockMvc를 StandaloneSetup으로 초기화
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    /**
     * 테스트: 회원 생성 성공 시 응답 검증
     * 예상 결과: 200 OK 응답 코드와 생성된 회원 정보 반환
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 생성 - 성공")
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
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 생성 - 중복 아이디 예외")
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

    /**
     * 테스트: 회원 정보 업데이트 성공
     * 예상 결과: 200 OK 응답 코드와 업데이트된 회원 정보 반환
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 정보 업데이트 - 성공")
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
     * 테스트: 회원 정보 업데이트 실패 - 유효하지 않은 요청
     * 예상 결과: IllegalArgumentException 발생
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 정보 업데이트 - 실패")
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
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 정보 조회 - 성공")
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
     * 테스트: 회원 탈퇴 실패
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 탈퇴 - 실패")
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

    /**
     * 테스트: 회원 탈퇴 성공
     * 예상 결과: 200 OK 응답 코드와 탈퇴 성공 메시지 반환
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 탈퇴 - 성공")
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
    * @since 1.0
    * author Luha
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

    /**
     * 테스트: 회원 포인트 조회 성공
     * 예상 결과: 200 OK 응답 코드와 포인트 정보 반환
     */
    @Test
    @DisplayName("회원 포인트 조회 - 성공")
    void testMemberPoint() {
      Long customerId = 84L;
      MemberPointResponse mockPoint = new MemberPointResponse(100);

      when(memberService.getPointsOfMember(customerId)).thenReturn(mockPoint);
      ResponseEntity<MemberPointResponse> actualResponse = memberController.getPoint(customerId);

      assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
      assertEquals(mockPoint, actualResponse.getBody());
   }

    /**
     * 테스트: 없는 멤버의 포인트 조회
     * 예상 결과: 존재하지 않는 멤버 ID로 요청 시 MemberNotFoundException 발생
     */
   @Test
   @DisplayName("없는 멤버의 포인트 조회")
   void testMemberPoint_MemberNotFound() {
      Long customerId = 84L;

      when(memberService.getPointsOfMember(customerId)).thenThrow(MemberNotFoundException.class);
      assertThrows(MemberNotFoundException.class, () -> memberController.getPoint(customerId));
   }

    /**
     * 테스트: 모든 회원 조회 성공
     * 예상 결과: 200 OK 응답 코드와 조회된 회원 목록 반환
     *
     * @throws Exception MockMvc 요청 중 발생할 수 있는 예외
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("모든 회원 조회 - 성공")
    void testGetAllMembers_Success() throws Exception {
        // given
        int page = 0;
        List<GetAllMembersResponse> mockResponses = List.of(
                new GetAllMembersResponse(
                        1L, "이한빈", "loginId1", "test1@example.com", "010-1234-5678",
                        "루하", LocalDate.of(1996, 6, 23), LocalDate.of(2023, 1, 1),
                        LocalDate.of(2023, 11, 30), 100, 50000,
                        null, Tier.GOLD
                ),
                new GetAllMembersResponse(
                        2L, "김철수", "loginId2", "test2@example.com", "010-5678-1234",
                        "철수", LocalDate.of(1990, 3, 15), LocalDate.of(2022, 5, 20),
                        LocalDate.of(2023, 11, 29), 200, 70000,
                        null, Tier.NORMAL
                )
        );

        when(memberService.getAllMembers(page)).thenReturn(mockResponses);

        // when & then
        mockMvc.perform(get("/api/v1/members/details")
                        .param("page", String.valueOf(page))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("이한빈")))
                .andExpect(jsonPath("$[0].loginId", is("loginId1")))
                .andExpect(jsonPath("$[0].email", is("test1@example.com")))
                .andExpect(jsonPath("$[0].phone", is("010-1234-5678")))
                .andExpect(jsonPath("$[0].nickname", is("루하")))
                .andExpect(jsonPath("$[0].birthday", is(Arrays.asList(1996, 6, 23))))
                .andExpect(jsonPath("$[0].joinDate", is(Arrays.asList(2023, 1, 1))))
                .andExpect(jsonPath("$[0].recentLoginDate", is(Arrays.asList(2023, 11, 30))))
                .andExpect(jsonPath("$[0].point", is(100)))
                .andExpect(jsonPath("$[0].accPurchase", is(50000)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("김철수")))
                .andExpect(jsonPath("$[1].loginId", is("loginId2")))
                .andExpect(jsonPath("$[1].email", is("test2@example.com")))
                .andExpect(jsonPath("$[1].phone", is("010-5678-1234")))
                .andExpect(jsonPath("$[1].nickname", is("철수")))
                .andExpect(jsonPath("$[1].birthday", is(Arrays.asList(1990, 3, 15))))
                .andExpect(jsonPath("$[1].joinDate", is(Arrays.asList(2022, 5, 20))))
                .andExpect(jsonPath("$[1].recentLoginDate", is(Arrays.asList(2023, 11, 29))))
                .andExpect(jsonPath("$[1].point", is(200)))
                .andExpect(jsonPath("$[1].accPurchase", is(70000)));
    }


}