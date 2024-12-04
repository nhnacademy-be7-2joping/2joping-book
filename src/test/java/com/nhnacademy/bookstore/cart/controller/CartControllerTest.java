package com.nhnacademy.bookstore.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import com.nhnacademy.bookstore.cart.dto.CartDeleteDto;
import com.nhnacademy.bookstore.cart.dto.CartRequestDto;
import com.nhnacademy.bookstore.cart.dto.CartResponseDto;
import com.nhnacademy.bookstore.cart.service.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartController cartController;

    @MockBean
    private CartService cartService;

    @MockBean
    private BookService bookService;

    @MockBean
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 장바구니 조회 테스트")
    void getCartForMember() throws Exception {
        // given
        List<CartResponseDto> cartResponseDtos = List.of(
                new CartResponseDto(1L, "책1", 10000, 2),
                new CartResponseDto(2L, "책2", 15000, 1)
        );
        Mockito.when(cartService.getCartByCustomerId(1L)).thenReturn(cartResponseDtos);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart")
                        .header("X-Customer-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("책1"))
                .andExpect(jsonPath("$[1].title").value("책2"));
    }

    @Test
    @DisplayName("회원 장바구니 상품 추가 테스트")
    void addToCart() throws Exception {
        // given
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 10000, 2);
        Mockito.when(bookService.getBookRemainQuantity(1L)).thenReturn(5);
        Mockito.when(cartService.addCart(any(CartRequestDto.class), eq(1L))).thenReturn(1);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart")
                        .header("X-Customer-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원 장바구니 상품 삭제 테스트")
    void deleteFromCart() throws Exception {
        // given
        CartDeleteDto deleteDto = new CartDeleteDto(1L);

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cart")
                        .header("X-Customer-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteDto)))
                // then
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("회원 장바구니 상품 수정 테스트")
    void updateToCart() throws Exception {
        // given
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 10000, 3);

        Mockito.doNothing().when(cartService).updateCart(any(CartRequestDto.class), eq(1L));

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cart")
                        .header("X-Customer-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    @DisplayName("비회원 장바구니 조회 테스트")
    void getCartForGuest() throws Exception {
        // given
        String cartSessionId = "cart:12345";
        BookResponseDto book1 = new BookResponseDto(1L, "출판사", "책1", "설명", null, "ISBN", 12000, 10000, true, true, 10, 0, 0, null, null, null, "", null);
        BookResponseDto book2 = new BookResponseDto(2L, "출판사", "책2", "설명", null, "ISBN", 15000, 15000, true, true, 5, 0, 0, null, null, null, "", null);

        HashOperations<Object, Object, Object> hashOperations = Mockito.mock(HashOperations.class);
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        Map<Object, Object> redisData = Map.of(
                "1", "책1/10000/2",
                "2", "책2/15000/1"
        );
        Mockito.when(hashOperations.entries(cartSessionId)).thenReturn(redisData);

        Mockito.when(bookService.getBookById(1L)).thenReturn(book1);
        Mockito.when(bookService.getBookById(2L)).thenReturn(book2);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart")
                        .cookie(new jakarta.servlet.http.Cookie("cartSession", cartSessionId))
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[*].title").value(containsInAnyOrder("책1", "책2")));
    }

    @Test
    @DisplayName("비회원 장바구니 추가 성공 테스트 (새 세션 생성)")
    void addToCartForGuestWithNewSession() throws Exception {
        // given
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 10000, 2);

        // bookService 모의 설정
        Mockito.when(bookService.getBookRemainQuantity(requestDto.bookId())).thenReturn(10);

        // redisTemplate 모의 설정
        HashOperations<Object, Object, Object> hashOperations = Mockito.mock(HashOperations.class);
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        // Redis에 데이터를 삽입하는 동작 시뮬레이션
        Mockito.doNothing().when(hashOperations).put(anyString(), eq(String.valueOf(requestDto.bookId())), any(String.class));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @DisplayName("비회원 장바구니 추가 성공 테스트 (세션 존재, 상품 미존재)")
    void addToCartForGuestWithExistingSessionAndNewProduct() throws Exception {
        // given
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 10000, 2);

        String existingCartSessionId = "cart:67890";

        // bookService 모의 설정: 충분한 재고를 반환하도록 설정
        Mockito.when(bookService.getBookRemainQuantity(requestDto.bookId())).thenReturn(10);

        // redisTemplate 모의 설정
        HashOperations<Object, Object, Object> hashOperations = Mockito.mock(HashOperations.class);
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        // Redis에 상품이 없는 경우를 시뮬레이션
        Mockito.when(hashOperations.get(existingCartSessionId, String.valueOf(requestDto.bookId()))).thenReturn(null);

        // Redis에 데이터를 삽입하는 동작 시뮬레이션
        Mockito.doNothing().when(hashOperations).put(eq(existingCartSessionId), eq(String.valueOf(requestDto.bookId())), any(String.class));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart")
                        .cookie(new jakarta.servlet.http.Cookie("cartSession", existingCartSessionId)) // 기존 세션
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("비회원 장바구니 추가 실패 테스트 (상품 이미 존재)")
    void addToCartForGuestWithExistingProduct() throws Exception {
        // given
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 10000, 2);

        String existingCartSessionId = "cart:67890";
        HashOperations<Object, Object, Object> hashOperations = Mockito.mock(HashOperations.class);
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        // bookService 모의 설정 추가: 충분한 재고를 반환하도록 설정
        Mockito.when(bookService.getBookRemainQuantity(requestDto.bookId())).thenReturn(10);

        // Redis에 이미 상품이 있는 경우를 시뮬레이션
        Mockito.when(hashOperations.get(existingCartSessionId, String.valueOf(requestDto.bookId())))
                .thenReturn(requestDto.title() + "/" + requestDto.sellingPrice() + "/" + requestDto.quantity());

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart")
                        .cookie(new jakarta.servlet.http.Cookie("cartSession", existingCartSessionId)) // 기존 세션
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isUnprocessableEntity()); // 422 상태 코드 기대
    }

    @Test
    @DisplayName("비회원 장바구니 추가 실패 테스트 (재고 부족)")
    void addToCartForGuestWithInsufficientStock() throws Exception {
        // given
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 10000, 5);

        Mockito.when(bookService.getBookRemainQuantity(requestDto.bookId())).thenReturn(2); // 재고 부족

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isConflict()); // 재고 부족으로 실패
    }


    @Test
    @DisplayName("비회원 장바구니 수정 테스트")
    void updateCartForGuest() throws Exception {
        // given
        String cartSessionId = "cart:12345";
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 10000, 3);

        HashOperations<Object, Object, Object> hashOperations = Mockito.mock(HashOperations.class);
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        Mockito.when(hashOperations.get(cartSessionId, "1")).thenReturn("책1/10000/2");

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cart")
                        .cookie(new jakarta.servlet.http.Cookie("cartSession", cartSessionId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    @DisplayName("비회원 장바구니 삭제 테스트")
    void deleteCartForGuest() throws Exception {
        // given
        String cartSessionId = "cart:12345";
        CartDeleteDto deleteDto = new CartDeleteDto(1L);

        HashOperations<Object, Object, Object> hashOperations = Mockito.mock(HashOperations.class);
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        Mockito.doAnswer(invocation -> {
            return null;
        }).when(hashOperations).delete(cartSessionId, "1");

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cart")
                        .cookie(new jakarta.servlet.http.Cookie("cartSession", cartSessionId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteDto)))
                // then
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("비회원 장바구니 추가 테스트 (cartSessionId.isEmpty())")
    void addToCartForGuestWithEmptySessionId() throws Exception {
        // given
        CartRequestDto requestDto = new CartRequestDto(1L, "책1", 10000, 2);

        Mockito.when(bookService.getBookRemainQuantity(requestDto.bookId())).thenReturn(10);

        // Redis Mock 설정
        HashOperations<Object, Object, Object> hashOperations = Mockito.mock(HashOperations.class);
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        Mockito.doNothing().when(hashOperations).put(anyString(), eq(String.valueOf(requestDto.bookId())), any(String.class));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart")
                        .cookie(new jakarta.servlet.http.Cookie("cartSession", "")) // 빈 세션 쿠키 제공
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isCreated());
    }
}
