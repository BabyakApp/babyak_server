package com.babyak.babyak.dto.user;

import com.babyak.babyak.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class SignUpResponseDTO {

    private String accessToken;
    private String refreshToken;

    public SignUpResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
