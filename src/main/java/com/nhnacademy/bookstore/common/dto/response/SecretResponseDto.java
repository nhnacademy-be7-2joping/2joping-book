package com.nhnacademy.bookstore.common.dto.response;

public record SecretResponseDto(

        Header header,
        Body body
) {

    public record Header(int resultCode, String resultMessage, boolean isSuccessful) {
    }

    public record Body(String secret) {
    }
}

