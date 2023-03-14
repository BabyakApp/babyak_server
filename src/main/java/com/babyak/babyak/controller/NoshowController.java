package com.babyak.babyak.controller;

import com.babyak.babyak.dto.user.IdAndNicknameDTO;
import com.babyak.babyak.service.NoshowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class NoshowController {
    @Autowired
    NoshowService noshowService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<IdAndNicknameDTO>> findUserList(@PathVariable Integer postId){
        return ResponseEntity.ok(noshowService.findUserList(postId));
    }

    @PostMapping("/{postId}")
    public ResponseEntity reportUser(@PathVariable Integer postId, Integer userId){
        noshowService.reportUser(postId, userId);
        return ResponseEntity.ok().build();
    }
}
