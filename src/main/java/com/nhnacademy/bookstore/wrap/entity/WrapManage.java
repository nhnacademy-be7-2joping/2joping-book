package com.nhnacademy.bookstore.wrap.entity;

/**
 * 주문포장관리 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wrap_manage", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"wrap_id", "order_detail_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class WrapManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wrap_manage_id", nullable = false)
    private Long wrapManageId;

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private Wrap wrap;

    @ManyToOne
    @JoinColumn(name = "order_detail_id", nullable = false)
    private OrderDetail orderDetail;
}

