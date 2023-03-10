package com.babyak.babyak.service;

import com.babyak.babyak.DTO.post.PostDTO;
import com.babyak.babyak.DTO.post.ShowPostDTO;
import com.babyak.babyak.mongo.ChatroomRepository;
import com.babyak.babyak.domain.post.Post;
import com.babyak.babyak.domain.post.PostRepository;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;

    public Integer createPost(PostDTO postDTO, Integer userId) {
        Post newPost = new Post();

        newPost.setTitle(postDTO.getTitle());
        newPost.setMeetingDate(postDTO.getMeetingDate());
        newPost.setMeetingTime(postDTO.getMeetingTime());
        newPost.setMeetingSite(postDTO.getMeetingSite());
        newPost.setMaxPeople(postDTO.getMaxPeople());
        newPost.setPreferredFood(postDTO.getPreferredFood());
        newPost.setIntroduce(postDTO.getIntroduce());
        newPost.setUserId(userId);

        Post post = postRepository.save(newPost);
        return post.getPostId();
    }

    public ShowPostDTO showPost(Integer postId) {
        Post post = postRepository.findPostByPostId(postId);
        ShowPostDTO showPostDTO = new ShowPostDTO();
        showPostDTO.setPostId(postId);
        showPostDTO.setTitle(post.getTitle());
        showPostDTO.setMeetingDate(post.getMeetingDate());
        showPostDTO.setMeetingTime(post.getMeetingTime());
        showPostDTO.setMeetingSite(post.getMeetingSite());
        showPostDTO.setMaxPeople(post.getMaxPeople());
        showPostDTO.setPreferredFood(post.getPreferredFood());
        showPostDTO.setIntroduce(post.getIntroduce());

        Integer hostUserId = post.getUserId();
        showPostDTO.setHostUserId(hostUserId);

        User user = userRepository.findByUserId(hostUserId);
        showPostDTO.setHostName(user.getNickname());
        showPostDTO.setHostDept(user.getDepart());
        showPostDTO.setHostStudentId(user.getStudentId() / 100000);

        showPostDTO.setCurrentUser(getCurrentUserNumber(postId));

        return showPostDTO;
    }

    public List<Integer> showAllPostId(){
        return postRepository.findAllPostId();
    }

    public List<ShowPostDTO> showAllPosts(){
        List<Integer> postIds = showAllPostId();
        List<ShowPostDTO> allPosts = new ArrayList<>();
        for(Integer postId : postIds){
            allPosts.add(showPost(postId));
        }
        return allPosts;
    }

    private int getCurrentUserNumber(Integer postId) {
        return chatroomRepository.findByIdx(postId.longValue()).getCurrentNumber();
    }

    public Integer countMyPosts(Integer userId){
        return postRepository.findPostsByUserId(userId).size();
    }
}

