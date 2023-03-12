package com.babyak.babyak.domain.post;

import com.babyak.babyak.DTO.post.ShowPostDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Post findPostByPostId(Integer postId);

    @Query(value = "select post.postId from Post post")
    List<Integer> findAllPostId();

    List<Integer> findPostIdByUserId(Integer userId);
}
