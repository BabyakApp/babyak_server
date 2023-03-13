package com.babyak.babyak.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoUpdateRequestDTO {

    @NotBlank(message = "닉네임을 입력해주세요")
    @Pattern(regexp = "^[가-힣0-9]{2,12}$", message = "한글이나 숫자로만 이루어진 2~12자 닉네임을 입력해주세요")
    private String nickname;

    private Integer studentId;

    private String depart;

    private String major;
}
