package com.babyak.babyak.controller;

import com.babyak.babyak.DTO.post.PostDTO;
import com.babyak.babyak.domain.post.Post;
import com.babyak.babyak.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping
    public String test(){
        return "hello";
    }
    @PostMapping
    public Post createPost(@RequestBody PostDTO postDTO){
        return postService.createPost(postDTO);
    }
}
