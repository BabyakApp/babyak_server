package com.babyak.babyak.controller;

import com.babyak.babyak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<String> join() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/reject/blocked")
    public ResponseEntity<String> blocked() {
        return new ResponseEntity("Blocked User", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/reject/ewhain")
    public ResponseEntity<String> ewhain() {
        return new ResponseEntity("Not Ewhain User", HttpStatus.UNAUTHORIZED);
    }





}
