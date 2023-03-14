package com.babyak.babyak.controller;

import com.babyak.babyak.DTO.ResponseDTO;
import com.babyak.babyak.DTO.user.IdAndNicknameDTO;
import com.babyak.babyak.service.NoshowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity findUserList(@PathVariable Integer postId){
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                noshowService.findUserList(postId)
        ), HttpStatus.OK);

    }

    @PostMapping("/{postId}")
    public ResponseEntity reportUser(@PathVariable Integer postId, Integer userId){
        noshowService.reportUser(postId, userId);
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                noshowService.reportUser(postId, userId)
        ), HttpStatus.OK);
    }
}
