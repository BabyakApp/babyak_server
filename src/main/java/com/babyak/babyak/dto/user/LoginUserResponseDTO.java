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

    private Integer userId;
    private Boolean isBlocked;
    private Boolean isJoined;
    private Boolean isEwha;

}
