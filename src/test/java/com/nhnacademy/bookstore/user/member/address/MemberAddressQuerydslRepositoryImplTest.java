package com.nhnacademy.bookstore.user.member.address;


import com.nhnacademy.bookstore.user.member.dto.response.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.repository.MemberAddressQuerydslRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@SpringBootTest
 class MemberAddressQuerydslRepositoryImplTest {

    @Autowired
    @Qualifier("memberAddressQuerydslRepositoryImpl") // 정확한 빈 이름으로 변경
    private MemberAddressQuerydslRepository memberAddressQuerydslRepository;

    private long testMemberId;

    @BeforeEach
    @Transactional
    void setUp() {
        // 테스트 데이터 세팅
        testMemberId = 84L; // 테스트 멤버 ID를 설정
        // 데이터베이스에 샘플 주소 데이터를 추가하는 작업을 수행
        // 필요한 경우 JPA Repository 또는 다른 데이터 삽입 방법 사용
    }

    @Test
    @Transactional
    void testFindAddressesByMemberId_Success() {
        // Given
        // 사전에 설정된 testMemberId와 연결된 데이터가 있다고 가정

        // When
        List<MemberAddressResponseDto> addressList = memberAddressQuerydslRepository.findAddressesByMemberId(testMemberId);

        // Then
        assertNotNull(addressList);
        assertFalse(addressList.isEmpty(), "멤버의 주소가 조회되어야 합니다.");
    }

    @Test
    @Transactional
    void testFindAddressesByMemberId_NoAddresses() {
        // Given
        long invalidMemberId = -1L; // 존재하지 않는 멤버 ID

        // When
        List<MemberAddressResponseDto> addressList = memberAddressQuerydslRepository.findAddressesByMemberId(invalidMemberId);

        // Then
        assertNotNull(addressList, "결과 리스트는 null이 아니어야 합니다.");
        assertEquals(0, addressList.size(), "주소가 없으므로 결과 리스트 크기는 0이어야 합니다.");
    }


}