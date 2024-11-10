package com.nhnacademy.bookstore.user.member.service.impl;

import com.nhnacademy.bookstore.common.error.exception.user.member.MemberDuplicateException;
import com.nhnacademy.bookstore.common.error.exception.user.member.status.MemberStatusNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.tier.MemberTierNotFoundException;
import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberCreateSuccessResponseDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.member.service.MemberService;
import com.nhnacademy.bookstore.user.memberStatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.memberStatus.repository.MemberStatusRepository;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.repository.MemberTierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberStatusRepository statusRepository;
    private final MemberTierRepository tierRepository;

    @Override
    public MemberCreateSuccessResponseDto registerNewMember(MemberCreateRequestDto memberDto) {

        // 아이디 Validation
        if (memberRepository.existsByLoginId(memberDto.getLoginId())) {
            throw new MemberDuplicateException("이미 존재하는 아이디입니다.");
        }

        // 이메일 Validation
        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new MemberDuplicateException("이미 존재하는 이메일입니다.");
        }

        // 전화번호 Validation
        if (memberRepository.existsByPhone(memberDto.getPhone())) {
            throw new MemberDuplicateException("이미 존재하는 전화번호입니다.");
        }

        //기본 회원 상태 가져오기 아이디값 1로 가져옴
        MemberStatus defaultStatus = statusRepository.findById(1L)
                .orElseThrow(() -> new MemberStatusNotFoundException("기본 상태를 찾을 수 없습니다."));

        //기본 회원 등급 가져오기 아이디값 1로 가져옴
        MemberTier defaultTier = tierRepository.findById(1L)
                .orElseThrow(() -> new MemberTierNotFoundException("기본 회원등급을 찾을 수 없습니다."));

        // request dto를 entity로 변환 후 저장
        Member requestMember = new Member();
        requestMember.toEntity(memberDto);
        requestMember.setStatus(defaultStatus);
        requestMember.setTier(defaultTier);
        Member member = memberRepository.save(requestMember);

        //멤버의 닉네임, 회원 가입 메세지 포함을 반환
        return new MemberCreateSuccessResponseDto(member.getNickname());
    }
}
