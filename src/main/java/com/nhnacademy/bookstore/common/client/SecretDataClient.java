package com.nhnacademy.bookstore.common.client;

import com.nhnacademy.bookstore.common.dto.response.SecretResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "secretClient", url = "${keymanager.url}")
@Profile("!test") // 테스트 환경에서는 비활성화
public interface SecretDataClient {

    @GetMapping("/appkey/{appKey}/secrets/{keyId}")
    SecretResponseDto getSecret(
            @PathVariable("appKey") String appkey,
            @PathVariable("keyId") String keyId,
            @RequestHeader("X-TC-AUTHENTICATION-ID") String authenticationId,
            @RequestHeader("X-TC-AUTHENTICATION-SECRET") String authenticationSecret
    );
}
