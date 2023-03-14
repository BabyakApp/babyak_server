package com.babyak.babyak.DTO.token;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class TokenDTO {

    private String accessToken;
    private String refreshToken;

    public TokenDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
