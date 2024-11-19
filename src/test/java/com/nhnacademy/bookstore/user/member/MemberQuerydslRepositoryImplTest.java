package com.nhnacademy.bookstore.user.member;

import com.nhnacademy.bookstore.common.error.exception.user.member.status.MemberNothingToUpdateException;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberUpdateResponseDto;
import com.nhnacademy.bookstore.user.member.repository.MemberQuerydslRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@SpringBootTest
class MemberQuerydslRepositoryImplTest {

    @Autowired
    @Qualifier("memberQuerydslRepositoryImpl") // 정확한 빈 이름으로 변경
    private MemberQuerydslRepository memberQuerydslRepository;

    private long testMemberId;

    @BeforeEach
    @Transactional
    void setUp() {
        // 테스트 데이터를 세팅하는 부분입니다.
        // 실제로는 데이터베이스에 기본 데이터를 삽입하거나 Mock을 사용할 수 있습니다.
        testMemberId = 1L; // 예시로 멤버 ID를 1로 설정
    }

    @Test
    @Transactional
    void testUpdateMemberDetails_Success() {
        // Given
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto(
                "010-1234-5678", "john.doe@example.com", "John");

        // When
        MemberUpdateResponseDto responseDto = memberQuerydslRepository.updateMemberDetails(requestDto, testMemberId);

        // Then
        assertNotNull(responseDto);
        assertEquals("010-1234-5678", responseDto.phone());
        assertEquals("john.doe@example.com", responseDto.email());
        assertEquals("John", responseDto.nickName());
    }



    @Test
    void testUpdateMemberDetails_NoFieldsToUpdate() {
        // Given: 업데이트할 데이터가 없는 요청
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto(null, null, null);

        // When & Then: 예외 발생 검증
        MemberNothingToUpdateException exception = assertThrows(
                MemberNothingToUpdateException.class,
                () -> memberQuerydslRepository.updateMemberDetails(requestDto, testMemberId)
        );

        assertTrue(exception.getMessage().contains("업데이트할 데이터가 없습니다."));
    }
    @Test
    @Transactional
    void testGetMemberInfo_Success() {
        // Given
        MemberUpdateResponseDto expectedResponse = new MemberUpdateResponseDto(
                "John Doe", Gender.M, LocalDate.of(1990, 1, 1),
                "010-1234-5678", "johndoe@example.com", "USER1"
        );

        MemberUpdateResponseDto responseDto = memberQuerydslRepository.getMemberInfo(testMemberId);

        // Then
        assertNotNull(responseDto);
        assertEquals(expectedResponse.name(), responseDto.name());
        assertEquals(expectedResponse.gender(), responseDto.gender());
        assertEquals(expectedResponse.birthday(), responseDto.birthday());
        assertEquals(expectedResponse.phone(), responseDto.phone());
        assertEquals(expectedResponse.email(), responseDto.email());
        assertEquals(expectedResponse.nickName(), responseDto.nickName());
    }
}