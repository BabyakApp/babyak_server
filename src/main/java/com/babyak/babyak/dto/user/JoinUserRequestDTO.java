package com.babyak.babyak.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinUserRequestDTO {

    private String email;
    private Integer studentId;
    private String nickname;
    private String depart;
    private String major;
}
