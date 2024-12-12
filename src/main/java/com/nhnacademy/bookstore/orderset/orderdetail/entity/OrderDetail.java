package com.nhnacademy.bookstore.orderset.orderdetail.entity;

/**
 * 주문상세 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import jakarta.persistence.*;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "final_price", nullable = false)
    private int finalPrice = 0;

    @Column(name = "sell_price", nullable = false)
    private int sellPrice;

    public void apply(Order order, Book book, int quantity, int sellPrice) {
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.finalPrice = quantity * sellPrice;
        this.sellPrice = sellPrice;
    }
}
