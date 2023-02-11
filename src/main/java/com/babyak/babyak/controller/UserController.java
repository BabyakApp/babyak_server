package com.babyak.babyak.controller;

import com.babyak.babyak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

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
