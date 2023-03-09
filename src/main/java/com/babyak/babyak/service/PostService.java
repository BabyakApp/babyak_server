package com.babyak.babyak.service;

import com.babyak.babyak.DTO.post.PostDTO;
import com.babyak.babyak.DTO.post.ShowPostDTO;
import com.babyak.babyak.domain.post.Post;
import com.babyak.babyak.domain.post.PostRepository;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

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

        return showPostDTO;
    }

    public List<Integer> showAllPostId(){
        return postRepository.findAllPostId();
    }

    public List<ShowPostDTO> showAllPosts(){
        List<Integer> postId = showAllPostId();
        List<ShowPostDTO> allPosts = new ArrayList<>();
        for(int i=0; i<postId.size();i++){
            allPosts.add(showPost(i+1));
        }
        return allPosts;
    }
}

