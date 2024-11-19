package com.nhnacademy.bookstore.user.member.service.impl;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberDuplicateException;
import com.nhnacademy.bookstore.common.error.exception.user.member.status.MemberStatusNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.tier.MemberTierNotFoundException;
import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.GetAllMembersResponse;
import com.nhnacademy.bookstore.user.member.dto.response.MemberCreateSuccessResponseDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.member.service.MemberService;
import com.nhnacademy.bookstore.user.memberStatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.memberStatus.repository.MemberStatusRepository;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.repository.MemberTierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * MemberServiceImpl
 *
 * 회원 서비스 구현 클래스입니다. 회원 가입 시, ID, 이메일, 전화번호 중복 여부를 검사하고
 * 기본 회원 상태 및 등급을 설정합니다. 회원 가입 성공 시, 환영 메시지를 포함한 DTO를 반환합니다.
 *
 * @author Luha
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final int INITIAL_PAGE_SIZE = 10;

    private final MemberRepository memberRepository;
    private final MemberStatusRepository statusRepository;
    private final MemberTierRepository tierRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 신규 회원을 등록하는 메서드.
     *
     * ID, 이메일, 전화번호 중복 여부를 검증하고, 기본 회원 상태 및 등급을 설정하여 저장합니다.
     *
     * @param memberDto 신규 회원 정보가 담긴 DTO
     * @return MemberCreateSuccessResponseDto 회원 가입 성공 메시지와 닉네임을 포함한 DTO
     * @throws MemberDuplicateException ID, 이메일, 전화번호 중복 발생 시 예외
     * @throws MemberStatusNotFoundException 기본 회원 상태가 존재하지 않을 경우 예외
     * @throws MemberTierNotFoundException 기본 회원 등급이 존재하지 않을 경우 예외
     */
    @Override
    public MemberCreateSuccessResponseDto registerNewMember(MemberCreateRequestDto memberDto) {

        if (memberRepository.existsByLoginId(memberDto.loginId())) {
            throw new MemberDuplicateException(
                    "이미 사용 중인 아이디입니다.",
                    RedirectType.REDIRECT,
                    "/members",
                    memberDto
            );
        }

        if (memberRepository.existsByEmail(memberDto.email())) {
            throw new MemberDuplicateException(
                    "이미 존재하는 이메일입니다.",
                    RedirectType.REDIRECT,
                    "/members",
                    memberDto
            );
        }

        if (memberRepository.existsByPhone(memberDto.phone())) {
            throw new MemberDuplicateException(
                    "이미 존재하는 전화번호입니다.",
                    RedirectType.REDIRECT,
                    "/members",
                    memberDto
            );
        }

        MemberStatus defaultStatus = statusRepository.findById(1L)
                .orElseThrow(() -> new MemberStatusNotFoundException(
                        "기본 상태를 찾을 수 없습니다.",
                        RedirectType.REDIRECT,
                        "/members",
                        memberDto
                ));

        MemberTier defaultTier = tierRepository.findById(1L)
                .orElseThrow(() -> new MemberTierNotFoundException(
                        "기본 회원등급을 찾을 수 없습니다.",
                        RedirectType.REDIRECT,
                        "/members",
                        memberDto
                ));

        Member requestMember = new Member();
        String encodedPassword = passwordEncoder.encode(memberDto.password());
        requestMember.toEntity(memberDto, encodedPassword);
        requestMember.setStatus(defaultStatus);
        requestMember.setTier(defaultTier);

        Member member = memberRepository.save(requestMember);

        return new MemberCreateSuccessResponseDto(member.getNickname());
    }

    // TODO: 전체 회원 조회 메서드 구현
    @Transactional(readOnly = true)
    @Override
    public List<GetAllMembersResponse> getAllMembers(
            final int page
    ) {
        final Pageable pageable = PageRequest.of(page, INITIAL_PAGE_SIZE);
        return memberRepository.findAllByOrderByNicknameDesc(pageable).stream()
                .map(GetAllMembersResponse::from)
                .toList();
    }
}
