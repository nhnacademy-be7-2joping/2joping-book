//package com.nhnacademy.bookstore.refund.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/refund")
//public class refundController {
//    private final RefundService refundService;
//
//    public RefundController(RefundService refundService) {
//        this.refundService = refundService;
//    }
//
//    // 반품 신청 API (Create + Update)
//    @PostMapping("/create-refund")
//    public ResponseEntity<String> applyRefund(@RequestBody RefundRequestDto requestDto) {
//        refundService.applyRefund(requestDto);
//        return ResponseEntity.ok("반품 신청이 완료되었습니다.");
//    }
//
//    @PutMapping("/edit-refund")
//
//
//}
