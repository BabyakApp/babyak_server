package com.babyak.babyak.controller;

import com.babyak.babyak.DTO.post.PostDTO;
import com.babyak.babyak.DTO.post.ShowPostDTO;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    @Autowired
    PostService postService;


    @PostMapping
    public ResponseEntity<Integer> createPost(@RequestBody PostDTO postDTO, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = principalDetails.getUser();
        return ResponseEntity.ok(postService.createPost(postDTO, user.getUserId()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ShowPostDTO> showPost(@PathVariable Integer postId){
        return ResponseEntity.ok(postService.showPost(postId));
    }

    @GetMapping
    public ResponseEntity<List<ShowPostDTO>> showAllPostId(){
        return ResponseEntity.ok(postService.showAllPosts());
    }

}
