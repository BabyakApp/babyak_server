package com.babyak.babyak.service;

import com.babyak.babyak.DTO.post.PostDTO;
import com.babyak.babyak.domain.post.Post;
import com.babyak.babyak.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post createPost(PostDTO postDTO){
        Post newPost = new Post();

        newPost.setTitle(postDTO.getTitle());
        newPost.setMeetingDate(postDTO.getMeetingDate());
        newPost.setMeetingTime(postDTO.getMeetingTime());
        newPost.setMeetingSite(postDTO.getMeetingSite());
        newPost.setMaxPeople(postDTO.getMaxPeople());
        newPost.setPreferredFood(postDTO.getPreferredFood());
        newPost.setIntroduce(postDTO.getIntroduce());
        newPost.setUserId(postDTO.getUserId());

        postRepository.save(newPost);
        return newPost;
    }
}
