package com.nhnacademy.bookstore.orderset.order_state.entity;

/**
 * 주문상태 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import com.nhnacademy.bookstore.orderset.order_state.entity.vo.OrderStateType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_state")
@NoArgsConstructor
public class OrderState {

    @Id
    @Column(name = "order_state_id", nullable = false)
    private Long orderStateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 20, nullable = false)
    private OrderStateType name;
}
