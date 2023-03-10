package com.babyak.babyak.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDTO {

    @NotNull
    private String refreshToken;
}
