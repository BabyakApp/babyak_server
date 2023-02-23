package com.babyak.babyak.controller;

import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.service.NoshowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class NoshowController {
    @Autowired
    NoshowService noshowService;

    @GetMapping("/{postId}")
    public void findUserList(@PathVariable Integer postId){
        noshowService.findUserList(postId);
    }
    @PostMapping("/{postId}")
    public void reportUser(@PathVariable Integer postId, Integer userId){
        noshowService.reportUser(postId, userId);
    }
}
