package com.babyak.babyak.DTO.user;

import com.babyak.babyak.DTO.token.TokenDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String email;
    private String status;
    private TokenDTO tokenDTO;
}
