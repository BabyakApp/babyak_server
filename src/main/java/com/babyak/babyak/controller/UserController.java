package com.babyak.babyak.controller;

import com.babyak.babyak.dto.user.JoinUserRequestDTO;
import com.babyak.babyak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public @ResponseBody String user(@AuthenticationPrincipal OAuth2User oAuth2User) {
        System.out.println("========== OAuth2User Attributes : " + oAuth2User.getAttributes());
        return "user";
    }



}
