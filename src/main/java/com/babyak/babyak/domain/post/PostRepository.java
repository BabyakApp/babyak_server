package com.babyak.babyak.domain.post;

import com.babyak.babyak.DTO.post.PostKeyDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, PostKeyDTO> {
}
