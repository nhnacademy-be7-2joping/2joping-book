package com.nhnacademy.bookstore.user.member.service.impl;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberDuplicateException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberPasswordNotEqualException;
import com.nhnacademy.bookstore.common.error.exception.user.member.status.MemberNothingToUpdateException;
import com.nhnacademy.bookstore.common.error.exception.user.member.status.MemberStatusNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.tier.MemberTierNotFoundException;
import com.nhnacademy.bookstore.coupon.service.MemberCouponService;
import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberWithdrawRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.*;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.member.service.MemberService;
import com.nhnacademy.bookstore.user.memberstatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.memberstatus.repository.MemberStatusRepository;
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
 * 회원 서비스 구현 클래스입니다. 회원 가입 시, ID, 이메일, 전화번호 중복 여부를 검사하고
 * 기본 회원 상태 및 등급을 설정합니다. 회원 가입 성공 시, 환영 메시지를 포함한 DTO를 반환합니다.
 *
 * @author Luha
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    static final String MEMBER_SIGNUP_URL = "/signup";
    static final String MEMBER_EDIT_PROFILE_URL = "/mypage/edit-profile";
    static final String MEMBER_WITHDRAW_URL = "/mypage/withdraw";
    private static final int INITIAL_PAGE_SIZE = 10;

    private final MemberRepository memberRepository;
    private final MemberStatusRepository statusRepository;
    private final MemberTierRepository tierRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberCouponService memberCouponService;

    /**
     * 신규 회원을 등록하는 메서드.
     * <p>
     * ID, 이메일, 전화번호 중복 여부를 검증하고, 기본 회원 상태 및 등급을 설정하여 저장합니다.
     *
     * @param memberDto 신규 회원 정보가 담긴 DTO
     * @return MemberCreateSuccessResponseDto 회원 가입 성공 메시지와 닉네임을 포함한 DTO
     * @throws MemberDuplicateException      ID, 이메일, 전화번호 중복 발생 시 예외
     * @throws MemberStatusNotFoundException 기본 회원 상태가 존재하지 않을 경우 예외
     * @throws MemberTierNotFoundException   기본 회원 등급이 존재하지 않을 경우 예외
     */
    @Override
    @Transactional
    public MemberCreateSuccessResponseDto registerNewMember(MemberCreateRequestDto memberDto) {

        if (memberRepository.existsByLoginId(memberDto.loginId())) {
            throw new MemberDuplicateException(
                    "이미 사용 중인 아이디입니다.",
                    RedirectType.REDIRECT,
                    MEMBER_SIGNUP_URL,
                    memberDto
            );
        }

        if (memberRepository.existsByEmail(memberDto.email())) {
            throw new MemberDuplicateException(
                    "이미 존재하는 이메일입니다.",
                    RedirectType.REDIRECT,
                    MEMBER_SIGNUP_URL,
                    memberDto
            );
        }

        if (memberRepository.existsByPhone(memberDto.phone())) {
            throw new MemberDuplicateException(
                    "이미 존재하는 전화번호입니다.",
                    RedirectType.REDIRECT,
                    MEMBER_SIGNUP_URL,
                    memberDto
            );
        }

        MemberStatus defaultStatus = statusRepository.findById(1L)
                .orElseThrow(() -> new MemberStatusNotFoundException(
                        "기본 상태를 찾을 수 없습니다.",
                        RedirectType.REDIRECT,
                        MEMBER_SIGNUP_URL
                ));

        MemberTier defaultTier = tierRepository.findById(1L)
                .orElseThrow(() -> new MemberTierNotFoundException(
                        "기본 회원등급을 찾을 수 없습니다.",
                        RedirectType.REDIRECT,
                        MEMBER_SIGNUP_URL,
                        memberDto
                ));

        Member requestMember = new Member();
        String encodedPassword = passwordEncoder.encode(memberDto.password());
        requestMember.toEntity(memberDto, encodedPassword);
        requestMember.setStatus(defaultStatus);
        requestMember.setTier(defaultTier);

        Member member = memberRepository.save(requestMember);

        memberCouponService.saveWelcomeCoupon(member);
        return new MemberCreateSuccessResponseDto(member.getNickname());
    }


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

    /**
     * 회원 정보를 업데이트하고, 업데이트된 정보를 반환합니다.
     *
     * @param customerId 수정할 회원의 고유 ID
     * @param memberDto  수정 요청 데이터를 담은 DTO
     * @return 업데이트된 회원 정보를 담은 DTO
     * @throws MemberNotFoundException  회원 ID가 존재하지 않을 경우 발생
     * @throws MemberDuplicateException 중복된 이메일 또는 전화번호가 존재할 경우 발생
     */
    @Transactional
    @Override
    public MemberUpdateResponseDto updateMember(long customerId, MemberUpdateRequesteDto memberDto) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다." + customerId,
                                                               RedirectType.REDIRECT, MEMBER_EDIT_PROFILE_URL
                ));

        String phone = null;
        String email = null;
        String nickName = null;
        boolean isUpdated = false;

        if (!member.getPhone().equals(memberDto.phone())) {

            if (memberRepository.existsByPhone(memberDto.phone())) {
                throw new MemberDuplicateException(
                        "이미 존재하는 전화번호입니다.",
                        RedirectType.REDIRECT,
                        MEMBER_EDIT_PROFILE_URL,
                        memberDto
                );
            }
            phone = memberDto.phone();
            isUpdated = true;

        }

        if (!member.getEmail().equals(memberDto.email())) {

            if (memberRepository.existsByEmail(memberDto.email())) {
                throw new MemberDuplicateException(
                        "이미 존재하는 이메일입니다.",
                        RedirectType.REDIRECT,
                        MEMBER_EDIT_PROFILE_URL,
                        memberDto
                );
            }
            email = memberDto.email();
            isUpdated = true;


        }
        if (!member.getNickname().equals(memberDto.nickName())) {
            nickName = memberDto.nickName();
            isUpdated = true;

        }
        if (!isUpdated) {
            throw new MemberNothingToUpdateException("업데이트할 데이터가 없습니다.", RedirectType.REDIRECT,
                                                     MEMBER_EDIT_PROFILE_URL
            );
        }
        MemberUpdateRequesteDto realUpdateDto = new MemberUpdateRequesteDto(phone, email, nickName);

        return memberRepository.updateMemberDetails(realUpdateDto, customerId);
    }

    /**
     * 회원 정보를 조회합니다.
     *
     * @param customerId 조회할 회원의 고유 ID
     * @return 조회된 회원 정보를 담은 DTO
     */
    @Override
    @Transactional(readOnly = true)
    public MemberUpdateResponseDto getMemberInfo(long customerId) {
        return memberRepository.getMemberInfo(customerId);
    }

    @Transactional
    @Override
    public MemberWithdrawResponseDto withdrawMember(long customerId, MemberWithdrawRequesteDto memberDto) {

        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다.", RedirectType.REDIRECT,
                                                               MEMBER_WITHDRAW_URL
                ));

        if (!passwordEncoder.matches(memberDto.password(), member.getPassword())) {
            throw new MemberPasswordNotEqualException("비밀번호가 일치하지 않습니다.", RedirectType.REDIRECT, MEMBER_WITHDRAW_URL);
        }

        MemberStatus status = statusRepository.findById(3L)
                .orElseThrow(() -> new MemberStatusNotFoundException("회원 상태가 존재하지 않습니다.", RedirectType.REDIRECT,
                                                                     MEMBER_WITHDRAW_URL
                ));
        member.withdrawStatus(status);


        return new MemberWithdrawResponseDto(member.getName());
    }

    /**
     * 회원이 가진 포인트를 조회합니다.
     *
     * @param customerId 조회할 회원의 고객 ID
     * @return 회원이 가진 포인트
     */
    @Override
    @Transactional(readOnly = true)
    public MemberPointResponse getPointsOfMember(Long customerId) {
        return memberRepository.findPointById(customerId).orElseThrow(
                () -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다.", RedirectType.REDIRECT, "/"));
    }
}
