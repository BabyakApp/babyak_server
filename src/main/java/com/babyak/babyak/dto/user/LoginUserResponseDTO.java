package com.babyak.babyak.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserResponseDTO {

    private String email;
    private Boolean isBlocked;
    private Boolean isJoined;
    private Boolean isEwha;

}
