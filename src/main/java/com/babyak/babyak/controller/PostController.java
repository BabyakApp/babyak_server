package com.babyak.babyak.controller;

import com.babyak.babyak.DTO.post.PostDTO;
import com.babyak.babyak.DTO.post.ShowPostDTO;
import com.babyak.babyak.domain.post.Post;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    @Autowired
    PostService postService;


    @PostMapping
    public Integer createPost(@RequestBody PostDTO postDTO, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = principalDetails.getUser();
        return postService.createPost(postDTO, user.getUserId());
    }

    @GetMapping("/{postId}")
    public ShowPostDTO showPost(@PathVariable Integer postId){
        return postService.showPost(postId);
    }

    @GetMapping
    public List<ShowPostDTO> showAllPostId(){
        return postService.showAllPosts();
    }

}
