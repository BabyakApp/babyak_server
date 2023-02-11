package com.babyak.babyak.controller;

import com.babyak.babyak.dto.user.AuthResponseDTO;
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

    @GetMapping("/signup/{email}")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseDTO signup(@PathVariable String email) {
        AuthResponseDTO resDTO = new AuthResponseDTO(email, "");
        return resDTO;
    }

    @GetMapping("/reject/{email}/{reason}")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthResponseDTO reject(@PathVariable String email, @PathVariable String reason) {
        AuthResponseDTO resDTO;
        if(reason.equals("blocked")) resDTO = new AuthResponseDTO(email, "blocked");
        else resDTO = new AuthResponseDTO(email, "domain");

        return resDTO;
    }


}
