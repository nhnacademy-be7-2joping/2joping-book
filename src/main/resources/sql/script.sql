DROP TABLE IF EXISTS book_image;
DROP TABLE IF EXISTS review_image;
DROP TABLE IF EXISTS wrap_image;
DROP TABLE IF EXISTS image;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS point_history;
DROP TABLE IF EXISTS point_type;
DROP TABLE IF EXISTS payment_history;
DROP TABLE IF EXISTS payment_method;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS shipment;
DROP TABLE IF EXISTS carrier;
DROP TABLE IF EXISTS shipment_policy;
DROP TABLE IF EXISTS wrap_manage;
DROP TABLE IF EXISTS wrap;
DROP TABLE IF EXISTS order_detail;
DROP TABLE IF EXISTS refund_history;
DROP TABLE IF EXISTS refund_policy;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS order_state;
DROP TABLE IF EXISTS coupon_category;
DROP TABLE IF EXISTS coupon_book;
DROP TABLE IF EXISTS member_coupon;
DROP TABLE IF EXISTS coupon;
DROP TABLE IF EXISTS coupon_policy;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS book_category;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS book_contributor;
DROP TABLE IF EXISTS contributor;
DROP TABLE IF EXISTS contributor_role;
DROP TABLE IF EXISTS book_tag;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS publisher;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS member_address;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS member_tier;
DROP TABLE IF EXISTS member_status;
DROP TABLE IF EXISTS non_member;
DROP TABLE IF EXISTS customer;

-- 고객 테이블 생성 DDL
CREATE TABLE customer
(
    customer_id BIGINT      NOT NULL AUTO_INCREMENT, -- 고객 아이디
    name        VARCHAR(20) NOT NULL,                -- 고객 이름
    phone       VARCHAR(20) NOT NULL UNIQUE,         -- 연락처
    email       VARCHAR(50) NOT NULL UNIQUE,         -- 이메일
    PRIMARY KEY (customer_id)
);

-- 비밀번호 테이블 생성 DDL
CREATE TABLE non_member
(
    customer_id BIGINT       NOT NULL,                          -- 고객 아이디 (외래 키)
    password    VARCHAR(255) NOT NULL,                          -- 비밀번호
    PRIMARY KEY (customer_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id) -- 고객 아이디와 연결
);

-- 회원 상태 테이블 생성 DDL
CREATE TABLE member_status
(
    member_status_id BIGINT      NOT NULL AUTO_INCREMENT, -- 회원 상태 아이디
    status           VARCHAR(10) NOT NULL,                -- 상태 (가입, 휴면, 탈퇴 등)
    PRIMARY KEY (member_status_id)
);

-- 회원 등급 테이블 생성 DDL
CREATE TABLE member_tier
(
    member_tier_id BIGINT       NOT NULL AUTO_INCREMENT, -- 회원 등급 아이디
    name           VARCHAR(20)  NOT NULL,                -- 등급 이름 (일반, 골드 등)
    status         BOOLEAN      NOT NULL DEFAULT TRUE,   -- 상태 (활성/비활성)
    acc_rate       TINYINT(100) NOT NULL,                -- 적립률
    promotion      INT          NOT NULL,                -- 승급 기준
    PRIMARY KEY (member_tier_id)
);

-- 회원 테이블 생성 DDL (고객 아이디와 외래키 관계 추가)
CREATE TABLE member
(
    customer_id       BIGINT       NOT NULL,
    member_status_id  BIGINT       NOT NULL,
    member_tier_id    BIGINT       NOT NULL,
    nickname          VARCHAR(20)  NOT NULL,
    login_id          VARCHAR(20)  NOT NULL,
    password          VARCHAR(255) NOT NULL,
    gender            ENUM ('M', 'F'),
    birthday          DATE,
    join_date         DATE         NOT NULL,
    recent_login_date DATE         NULL,
    is_payco_login    BOOLEAN      NOT NULL,
    point             INT          NOT NULL DEFAULT 0,
    acc_purchase      INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (customer_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    FOREIGN KEY (member_status_id) REFERENCES member_status (member_status_id),
    FOREIGN KEY (member_tier_id) REFERENCES member_tier (member_tier_id)
);

-- 회원 배송지
CREATE TABLE member_address
(
    member_address_id  BIGINT AUTO_INCREMENT,
    customer_id        BIGINT       NOT NULL,               -- 회원 아이디
    postal_code        CHAR(5)      NOT NULL,               -- 우편 번호
    road_address       VARCHAR(100) NOT NULL,               -- 도로명 주소
    detail_address     VARCHAR(100) NULL,                   -- 상세 주소
    address_alias      VARCHAR(50)  NULL,                   -- 별칭
    is_default_address BOOLEAN      NOT NULL DEFAULT false, -- 기본 배송지 여부
    receiver           VARCHAR(20)  NOT NULL,               -- 받는이
    PRIMARY KEY (member_address_id),
    FOREIGN KEY (customer_id) REFERENCES member (customer_id)
);

-- Admin 테이블 생성 DDL
create table admin
(
    admin_id BIGINT       NOT NULL AUTO_INCREMENT,
    id       VARCHAR(20)  NOT NULL UNIQUE,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (admin_id)
);

-- 출판사
CREATE TABLE publisher
(
    publisher_id BIGINT AUTO_INCREMENT,
    name         VARCHAR(50) NOT NULL UNIQUE,
    is_active    BOOLEAN     NOT NULL DEFAULT true,
    PRIMARY KEY (publisher_id)
);

-- 도서
CREATE TABLE book
(
    book_id         BIGINT AUTO_INCREMENT,              -- 도서 id
    publisher_id    BIGINT       NOT NULL,              -- 출판사 id
    title           VARCHAR(200) NOT NULL,              -- 제목
    description     TEXT,                               -- 설명
    published_date  DATE         NOT NULL,              -- 출판일시
    isbn            VARCHAR(13)  NOT NULL UNIQUE,       -- ISBN
    retail_price    INT          NOT NULL,              -- 정가
    selling_price   INT          NOT NULL,              -- 판매가
    gift_wrappable  BOOLEAN      NOT NULL,              -- 선물 포장가능 여부
    is_active       BOOLEAN      NOT NULL DEFAULT true, -- 활성 여부
    remain_quantity INT          NOT NULL DEFAULT 0,    -- 잔여 수량
    views           INT          NOT NULL DEFAULT 0,    -- 조회수
    likes           INT          NOT NULL DEFAULT 0,    -- 좋아요 수
    PRIMARY KEY (book_id),
    FOREIGN KEY (publisher_id) REFERENCES publisher (publisher_id)
);

-- 태그
CREATE TABLE tag
(
    tag_id BIGINT AUTO_INCREMENT,
    name   VARCHAR(25) NOT NULL UNIQUE,
    PRIMARY KEY (tag_id)
);

-- 도서 태그
CREATE TABLE book_tag
(
    tag_id  BIGINT,
    book_id BIGINT,
    PRIMARY KEY (tag_id, book_id),
    FOREIGN KEY (tag_id) REFERENCES tag (tag_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id)
);

-- 기여자 역할
CREATE TABLE contributor_role
(
    contributor_role_id BIGINT AUTO_INCREMENT,
    name                VARCHAR(30) NOT NULL UNIQUE, -- 기여자 종류 (작가/엮은이/번역가)
    PRIMARY KEY (contributor_role_id)
);

-- 기여자
CREATE TABLE contributor
(
    contributor_id      BIGINT AUTO_INCREMENT,
    contributor_role_id BIGINT      NOT NULL,
    name                VARCHAR(30) NOT NULL,
    is_active           BOOLEAN     NOT NULL DEFAULT true,
    PRIMARY KEY (contributor_id),
    UNIQUE (contributor_role_id, name),
    FOREIGN KEY (contributor_role_id) REFERENCES contributor_role (contributor_role_id)
);

-- 도서 기여자
CREATE TABLE book_contributor
(
    contributor_id BIGINT,
    book_id        BIGINT,
    PRIMARY KEY (contributor_id, book_id),
    FOREIGN KEY (contributor_id) REFERENCES contributor (contributor_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id)
);

-- 카테고리
CREATE TABLE category
(
    category_id    BIGINT AUTO_INCREMENT,
    subcategory_id BIGINT,
    name           VARCHAR(50) NOT NULL,
    is_active      BOOLEAN     NOT NULL DEFAULT true,
    PRIMARY KEY (category_id),
    UNIQUE (subcategory_id, name),
    FOREIGN KEY (subcategory_id) REFERENCES category (category_id)
);

-- 도서의 카테고리
CREATE TABLE book_category
(
    book_id     BIGINT,
    category_id BIGINT,
    PRIMARY KEY (book_id, category_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id),
    FOREIGN KEY (category_id) REFERENCES category (category_id)
);

-- 장바구니
CREATE TABLE cart
(
    book_id     BIGINT,
    customer_id BIGINT,
    quantity    INT CHECK (quantity > 0),
    PRIMARY KEY (book_id, customer_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
);

-- 좋아요
CREATE TABLE likes
(
    like_id     BIGINT AUTO_INCREMENT,
    book_id     BIGINT,
    customer_id BIGINT,
    PRIMARY KEY (like_id),
    UNIQUE (book_id, customer_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
);

-- 쿠폰 정책
CREATE TABLE coupon_policy
(
    coupon_policy_id BIGINT AUTO_INCREMENT,
    name             VARCHAR(20)                                                   NOT NULL, -- 정책 이름
    discount_type    ENUM ('PERCENT', 'ACTUAL')                                    NOT NULL, -- 할인 유형
    discount_value   INT                                                           NOT NULL, -- 할인값
    -- limit은 keyword -> usage_limit 으로 변경
    usage_limit      INT                                                           NULL,
    -- period는 키워드 -> duration으로 변경
    duration         INT                                                           NULL,
    detail           TEXT                                                          NULL,     -- 세부 사항
    max_discount     INT                                                           NULL,     -- 최대 할인 금액
    is_active        BOOLEAN                                                       NOT NULL DEFAULT true,
    type             ENUM ('BIRTHDAY', 'WELCOME', 'BOOK', 'CATEGORY', 'PROMOTION') NOT NULL, -- 쿠폰 타입
    PRIMARY KEY (coupon_policy_id)
);

-- 쿠폰
CREATE TABLE coupon
(
    coupon_id        BIGINT AUTO_INCREMENT,
    coupon_policy_id BIGINT,
    name             VARCHAR(30) NOT NULL UNIQUE, -- 이름
    created_at       DATE        NOT NULL,        -- 발금 가능 시작일
    expired_at       DATE        NOT NULL,        -- 발급 만료일
    -- 쿠폰 수량 (null = 제한 없음 || else = 제한 수량)
    quantity         INT         NULL,
    PRIMARY KEY (coupon_id),
    FOREIGN KEY (coupon_policy_id) REFERENCES coupon_policy (coupon_policy_id)
);

-- 회원 보유 쿠폰
CREATE TABLE member_coupon
(
    `coupon_usage_id` BIGINT    NOT NULL AUTO_INCREMENT,
    `coupon_id`       BIGINT    NOT NULL,
    `customer_id`     BIGINT    NOT NULL, -- 회원 아이디
    `receive_time`    TIMESTAMP NOT NULL, -- 수령일
    `invalid_time`    TIMESTAMP NOT NULL, -- 사용 기한
    `is_used`         BOOLEAN   NOT NULL, -- 사용 여부
    `used_date`       TIMESTAMP NULL,     -- 사용 일자
    PRIMARY KEY (coupon_usage_id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (coupon_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
);

-- 쿠폰-도서
CREATE TABLE coupon_book
(
    book_id   BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, coupon_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (coupon_id)
);

-- 쿠폰-카테고리
CREATE TABLE coupon_category
(
    category_id BIGINT NOT NULL,
    coupon_id   BIGINT NOT NULL,
    PRIMARY KEY (category_id, coupon_id),
    FOREIGN KEY (category_id) REFERENCES category (category_id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (coupon_id)
);

CREATE TABLE order_state
(
    `order_state_id` BIGINT      NOT NULL AUTO_INCREMENT COMMENT '0,1,2,3,4',
    `name`           VARCHAR(20) NOT NULL UNIQUE COMMENT '대기, 배송중, 완료, 반품, 주문취소',
    PRIMARY KEY (order_state_id)
);

CREATE TABLE orders
(
    `order_id`              BIGINT       NOT NULL AUTO_INCREMENT,
    `order_state_id`        BIGINT       NOT NULL,
    `customer_id`           BIGINT       NOT NULL,
    `coupon_usage_id`       BIGINT       NOT NULL,
    `order_date`            TIMESTAMP    NOT NULL,
    `receiver`              VARCHAR(20)  NOT NULL,
    `postal_code`           CHAR(5)      NOT NULL,
    `road_address`          VARCHAR(100) NOT NULL,
    `detail_address`        VARCHAR(100) NULL,
    `point_usage`           INT          NOT NULL DEFAULT 0,
    `shipping_fee`          INT          NOT NULL,
    `coupon_sale_price`     INT          NOT NULL DEFAULT 0,
    `total_price`           INT          NOT NULL,
    `desired_delivery_date` DATE         NULL,
    PRIMARY KEY (order_id),
    FOREIGN KEY (order_state_id) REFERENCES order_state (order_state_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    FOREIGN KEY (coupon_usage_id) REFERENCES member_coupon (coupon_usage_id)
);

CREATE TABLE refund_policy
(
    refund_policy_id BIGINT AUTO_INCREMENT,
    duration         INT                    NOT NULL, -- 기간(일)
    policy           ENUM ('단순변심','파손(파본)') NOT NULL, -- 반품 정책
    description      TEXT                   NOT NULL, -- 정책 설명
    refund_fee       INT                    NOT NULL, -- 반품 택배비
    is_active        BOOLEAN                NOT NULL DEFAULT true,
    PRIMARY KEY (refund_policy_id)
);

CREATE TABLE refund_history
(
    refund_history_id BIGINT AUTO_INCREMENT,
    order_id          BIGINT NOT NULL,
    refund_policy_id  BIGINT NOT NULL,
    save_point        BIGINT NOT NULL DEFAULT 0, -- 반품시 결제 금액은 포인트 전환됨
    refund_fee        INT    NOT NULL,
    PRIMARY KEY (refund_history_id),
    FOREIGN KEY (order_id) REFERENCES orders (order_id),
    FOREIGN KEY (refund_policy_id) REFERENCES refund_policy (refund_policy_id)
);

CREATE TABLE order_detail
(
    `order_detail_id` BIGINT AUTO_INCREMENT,
    `order_id`        BIGINT NOT NULL,
    `book_id`         BIGINT NOT NULL,
    `quantity`        INT    NOT NULL,
    `final_price`     INT    NOT NULL DEFAULT 0,
    `sell_price`      INT    NOT NULL,
    PRIMARY KEY (order_detail_id),
    FOREIGN KEY (order_id) REFERENCES orders (order_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id)
);

CREATE TABLE wrap
(
    wrap_id    BIGINT AUTO_INCREMENT,
    name       VARCHAR(32) NOT NULL UNIQUE,
    wrap_price INT         NOT NULL DEFAULT 0,
    is_active  BOOLEAN     NOT NULL DEFAULT true,
    PRIMARY KEY (wrap_id)
);

CREATE TABLE wrap_manage
(
    `wrap_manage_id`  BIGINT NOT NULL AUTO_INCREMENT,
    `wrap_id`         BIGINT NOT NULL,
    `order_detail_id` BIGINT NOT NULL,
    PRIMARY KEY (wrap_manage_id),
    UNIQUE (wrap_id, order_detail_id),
    FOREIGN KEY (wrap_id) REFERENCES wrap (wrap_id),
    FOREIGN KEY (order_detail_id) REFERENCES order_detail (order_detail_id)
);

CREATE TABLE shipment_policy
(
    `shipment_policy_id` BIGINT AUTO_INCREMENT,
    `name`               VARCHAR(255) NOT NULL UNIQUE,
    `min_order_amount`   INT          NOT NULL,
    `is_member_only`     BOOLEAN      NOT NULL,
    `created_at`         TIMESTAMP    NULL     DEFAULT CURRENT_TIMESTAMP,
    `updated_at`         TIMESTAMP    NULL,
    `shipping_fee`       INT          NOT NULL,
    `is_active`          BOOLEAN      NOT NULL DEFAULT true,
    PRIMARY KEY (shipment_policy_id)
);

CREATE TABLE carrier
(
    `carrier_id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `name`           VARCHAR(100) NOT NULL,
    `contact_number` VARCHAR(20)  NULL,
    `contact_email`  VARCHAR(255) NULL,
    `website_url`    VARCHAR(255) NULL,
    `tracking_url`   VARCHAR(255) NOT NULL,
    PRIMARY KEY (carrier_id)
);

CREATE TABLE shipment
(
    shipment_id           BIGINT AUTO_INCREMENT,
    carrier_id            BIGINT       NOT NULL, -- 배송 업체 id
    shipment_policy_id    BIGINT       NOT NULL, -- 배송 정책 id
    order_id              BIGINT       NOT NULL, -- 주문 id
    requirement           VARCHAR(32)  NULL,     -- 요청 사항
    desired_delivery_date TIMESTAMP    NULL,     -- 희망 배송일
    shipping_date         TIMESTAMP    NULL,     -- 출고일
    delivery_date         TIMESTAMP    NULL,     -- 배송 완료일
    tracking_number       VARCHAR(255) NOT NULL, -- 운송장 번호

    PRIMARY KEY (shipment_id),
    FOREIGN KEY (carrier_id) REFERENCES carrier (carrier_id),
    FOREIGN KEY (shipment_policy_id) REFERENCES shipment_policy (shipment_policy_id),
    FOREIGN KEY (order_id) REFERENCES orders (order_id)
);

-- 결제 상태
CREATE TABLE status
(
    status_id BIGINT AUTO_INCREMENT,
    name      VARCHAR(20) NOT NULL UNIQUE,
    PRIMARY KEY (status_id)
);

-- 결제 수단
CREATE TABLE payment_method
(
    payment_method_id BIGINT AUTO_INCREMENT,
    name              VARCHAR(20) NOT NULL,
    PRIMARY KEY (payment_method_id)
);

CREATE TABLE payment_history
(
    `payment_history_id` BIGINT AUTO_INCREMENT,
    `payment_method_id`  BIGINT    NOT NULL, -- 결제 수단 id
    `status_id`          BIGINT    NOT NULL, -- 결제 상태 id
    `order_id`           BIGINT    NOT NULL, -- 주문 id
    `payment_date`       TIMESTAMP NOT NULL, -- 결제일
    `amount_paid`        INT       NOT NULL, -- 결제 금액
    PRIMARY KEY (payment_history_id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_method (payment_method_id),
    FOREIGN KEY (status_id) REFERENCES status (status_id),
    FOREIGN KEY (order_id) REFERENCES orders (order_id)
);

CREATE TABLE point_type
(
    point_type_id BIGINT                     NOT NULL,
    type          ENUM ('PERCENT', 'ACTUAL') NOT NULL,
    acc_val       INT                        NOT NULL,
    name          VARCHAR(20)                NOT NULL UNIQUE,
    is_active     BOOLEAN                    NOT NULL DEFAULT true,
    PRIMARY KEY (point_type_id)
);

CREATE TABLE point_history
(
    `point_history_id`  BIGINT    NOT NULL AUTO_INCREMENT,
    `point_type_id`     BIGINT    NULL,
    `order_detail_id`   BIGINT    NULL,
    `refund_history_id` BIGINT    NULL,
    `order_id`          BIGINT    NULL,
    `customer_id`       BIGINT    NOT NULL,
    `point_val`         INT       NOT NULL,
    `register_date`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (point_history_id),
    FOREIGN KEY (point_type_id) REFERENCES point_type (point_type_id),
    FOREIGN KEY (order_detail_id) REFERENCES order_detail (order_detail_id),
    FOREIGN KEY (refund_history_id) REFERENCES refund_history (refund_history_id),
    FOREIGN KEY (order_id) REFERENCES orders (order_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
);

CREATE TABLE review
(
    `order_detail_id` BIGINT       NOT NULL,
    `customer_id`     BIGINT       NOT NULL,
    `book_id`         BIGINT       NOT NULL,
    `title`           VARCHAR(255) NOT NULL,
    `text`            TEXT         NOT NULL,
    `rating_value`    TINYINT(5)   NOT NULL, -- 평점(1 ~ 5)
    `created_at`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP    NULL,
    `image_url`       VARCHAR(255) NULL,

    PRIMARY KEY (order_detail_id),
    FOREIGN KEY (order_detail_id) REFERENCES order_detail (order_detail_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id)
);

-- 도서 평점
CREATE TABLE rating
(
    `order_detail_id` BIGINT     NOT NULL,
    `book_id`         BIGINT     NOT NULL, -- 도서 id
    `rating_value`    TINYINT(5) NOT NULL, -- 평점(1 ~ 5)
    `created_at`      TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP  NULL,
    PRIMARY KEY (order_detail_id),
    FOREIGN KEY (order_detail_id) REFERENCES review (order_detail_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id)
);

-- 이미지
CREATE TABLE image
(
    image_id BIGINT AUTO_INCREMENT,
    url      VARCHAR(255) NOT NULL, -- 이미지 경로
    PRIMARY KEY (image_id)
);

-- 포장 이미지
CREATE TABLE wrap_image
(
    package_image_id BIGINT NOT NULL AUTO_INCREMENT,
    image_id         BIGINT NOT NULL,
    wrap_id          BIGINT NOT NULL,
    PRIMARY KEY (package_image_id),
    UNIQUE (image_id, wrap_id),
    FOREIGN KEY (image_id) REFERENCES image (image_id),
    FOREIGN KEY (wrap_id) REFERENCES wrap (wrap_id)
);

-- 리뷰 이미지
CREATE TABLE review_image
(
    review_image_id BIGINT NOT NULL AUTO_INCREMENT,
    image_id        BIGINT NOT NULL,
    order_detail_id BIGINT NOT NULL,
    PRIMARY KEY (review_image_id),
    UNIQUE (image_id, order_detail_id),
    FOREIGN KEY (image_id) REFERENCES image (image_id),
    FOREIGN KEY (order_detail_id) REFERENCES order_detail (order_detail_id)
);

-- 도서 이미지
CREATE TABLE book_image
(
    book_image_id BIGINT      NOT NULL AUTO_INCREMENT,
    book_id       BIGINT      NOT NULL,
    image_id      BIGINT      NOT NULL,
    image_type    VARCHAR(50) NOT NULL,
    PRIMARY KEY (book_image_id),
    UNIQUE (book_id, image_id),
    FOREIGN KEY (book_id) REFERENCES book (book_id),
    FOREIGN KEY (image_id) REFERENCES image (image_id)
);

### Indices
CREATE UNIQUE INDEX member_login_id_idx ON member (login_id);