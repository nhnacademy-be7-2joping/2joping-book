package com.nhnacademy.bookstore.user.member.entity;

import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

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
    @Column(nullable = false)
    private boolean isDefaultAddress;

    @Column(nullable = false, length = 20)
    private String receiver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @Setter
    private Member member;

    public void toEntity(MemberAddressRequestDto requestDto, Member member){
        this.postalCode = requestDto.getPostalCode();
        this.roadAddress = requestDto.getRoadAddress();
        this.detailAddress = requestDto.getDetailAddress();
        this.addressAlias = requestDto.getAddressAlias();
        this.isDefaultAddress = requestDto.isDefaultAddress();
        this.receiver = requestDto.getReceiver();
        this.member = member;
    }
}
