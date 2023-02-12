package com.babyak.babyak.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO {

    @NotNull
    @Email
    private String email;

    @NotNull
    private Integer studentId;

    @NotNull
    private String nickname;

    @NotNull
    private String depart;

    @NotNull
    private String major;

}
