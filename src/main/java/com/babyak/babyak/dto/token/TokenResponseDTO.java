package com.babyak.babyak.dto.token;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;

    public TokenResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
