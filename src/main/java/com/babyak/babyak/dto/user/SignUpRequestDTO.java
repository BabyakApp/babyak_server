package com.babyak.babyak.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

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
    @NotBlank(message = "닉네임을 입력해주세요")
    @Pattern(regexp = "^[가-힣0-9]{2,12}$", message = "한글이나 숫자로만 이루어진 2~12자 닉네임을 입력해주세요")
    private String nickname;

    @NotNull
    private String depart;

    @NotNull
    private String major;

}
