package com.babyak.babyak.domain.user;

import com.babyak.babyak.DTO.user.SignUpRequestDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotNull
    @Column(name = "student_id")
    private Integer studentId;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    private String depart;

    @NotNull
    private String major;

    @NotNull
    @Column(name = "no_show")
    private Integer noShow;

    @NotNull
    private String role;


    public void signup(SignUpRequestDTO reqDTO) {
        this.studentId = reqDTO.getStudentId();
        this.nickname = reqDTO.getNickname();
        this.depart = reqDTO.getDepart();
        this.major = reqDTO.getMajor();
        this.role = "ROLE_USER";
    }
}
