package com.babyak.babyak.controller;

import com.babyak.babyak.DTO.post.PostDTO;
import com.babyak.babyak.domain.post.Post;
import com.babyak.babyak.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    @Autowired
    PostService postService;


    @PostMapping
    public Integer createPost(@RequestBody PostDTO postDTO){
        return postService.createPost(postDTO);
    }

    @GetMapping
    public Optional<Post> showPost(Integer postId){
        return postService.showPost(postId);
    }

}
