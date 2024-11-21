package com.nhnacademy.bookstore.user.member.entity;

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

    @Column(nullable = false, length = 10)
    private String postalCode;

    @Column(nullable = false, length = 100)
    private String roadAddress;

    @Column(length = 100)
    private String detailAddress;

    @Column(length = 50)
    private String addressAlias;

    @Setter
    @Column(name="is_default_address", columnDefinition = "TINYINT(1)")

    private boolean defaultAddress;

    @Column(nullable = false, length = 20)
    private String receiver;

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
        this.postalCode = requestDto.getPostalCode();
        this.roadAddress = requestDto.getRoadAddress();
        this.detailAddress = requestDto.getDetailAddress();
        this.addressAlias = requestDto.getAddressAlias();
        this.defaultAddress = requestDto.isDefaultAddress();
        this.receiver = requestDto.getReceiver();
        this.member = member;
    }
}
