package com.babyak.babyak.domain.noshow;

import com.babyak.babyak.DTO.noshow.NoshowPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface NoshowRepository extends JpaRepository<Noshow, NoshowPK> {
    boolean existsByPostIdAndUserId(Integer postId, Integer userId);
    Noshow findByPostIdAndUserId(Integer postId, Integer userId);

    List<Noshow> findByPostId(Integer postId);
}
