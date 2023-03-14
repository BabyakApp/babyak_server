package com.babyak.babyak.controller;

import com.babyak.babyak.DTO.ResponseDTO;
import com.babyak.babyak.DTO.post.PostDTO;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    @Autowired
    PostService postService;


    @PostMapping
    public ResponseEntity createPost(@RequestBody PostDTO postDTO, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = principalDetails.getUser();
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                postService.createPost(postDTO, user.getUserId())
                ), HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity showPost(@PathVariable Integer postId){
        return new ResponseEntity(ResponseDTO.response(
            HttpStatus.OK.value(),
            HttpStatus.OK.getReasonPhrase(),
            postService.showPost(postId)
            ), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity showAllPosts(){
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                postService.showAllPosts()
                ), HttpStatus.OK);
    }

    @GetMapping("/myposts")
    public ResponseEntity countMyPosts(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                postService.countMyPosts(principalDetails.getUser().getUserId())
        ), HttpStatus.OK);
    }

}
