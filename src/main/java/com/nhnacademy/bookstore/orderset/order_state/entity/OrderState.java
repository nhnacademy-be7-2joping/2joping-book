package com.nhnacademy.bookstore.orderset.order_state.entity;

/**
 * 주문상태 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_state")
@Getter
@Setter
@NoArgsConstructor
public class OrderState {

    @Id
    @Column(name = "order_state_id", nullable = false)
    private Long orderStateId;

    @Column(name = "name", length = 20, nullable = false)
    private String name;
}
