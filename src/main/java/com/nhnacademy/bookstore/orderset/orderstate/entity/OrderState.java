package com.nhnacademy.bookstore.orderset.orderstate.entity;

/**
 * 주문상태 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import com.nhnacademy.bookstore.orderset.orderstate.entity.vo.OrderStateType;
import jakarta.persistence.*;
import lombok.*;

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

    @Builder
    public OrderState(final OrderStateType name) {
        this.name = name;
    }

    public void updateName(OrderStateType newName) {
        if (newName == null) {
            throw new IllegalArgumentException("주문 상태가 없습니다.");
        }
        this.name = newName;
    }
}
