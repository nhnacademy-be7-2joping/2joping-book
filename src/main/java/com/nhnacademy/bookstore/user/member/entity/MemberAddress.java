package com.nhnacademy.bookstore.user.member.entity;

import com.nhnacademy.bookstore.user.member.dto.request.AddressUpdateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

/**
 * MemberAddress
 * 이 클래스는 회원의 주소 정보를 나타내는 엔티티 클래스입니다. 회원이 소유할 수 있는 주소의 기본 정보를 포함하며,
 * 여러 개의 주소를 관리하고, 기본 주소 여부를 설정할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
@Entity
@Table(name = "member_address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DynamicUpdate
public class MemberAddress {

    @Id
    @Column(name = "member_address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "postal_code", columnDefinition = "CHAR(5)")
    private String postalCode;

    private String roadAddress;

    private String detailAddress;

    private String addressAlias;

    @Setter
    @Column(name="is_default_address")
    private boolean defaultAddress;

    private String receiver;

    @Setter
    private boolean available;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @Setter
    private Member member;

    /**
     * MemberAddress 엔티티의 필드를 MemberAddressRequestDto를 통해 업데이트합니다.
     *
     * @param requestDto 요청에서 전달된 주소 정보 DTO
     * @param member     주소를 소유한 회원 객체
     */
    public void toEntity(MemberAddressRequestDto requestDto, Member member){
        this.postalCode = requestDto.postalCode();
        this.roadAddress = requestDto.roadAddress();
        this.detailAddress = requestDto.detailAddress();
        this.addressAlias = requestDto.addressAlias();
        this.defaultAddress = requestDto.defaultAddress();
        this.receiver = requestDto.receiver();
        this.member = member;
        this.available = true;
    }
    public void updateAddress(AddressUpdateRequestDto requestDto){
        this.postalCode = requestDto.postalCode();
        this.roadAddress = requestDto.roadAddress();
        this.detailAddress = requestDto.detailAddress();
        this.addressAlias = requestDto.addressAlias();
        this.receiver = requestDto.receiver();
    }
}
