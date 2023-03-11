package com.babyak.babyak.domain.noshow;

import com.babyak.babyak.dto.noshow.NoshowPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoshowRepository extends JpaRepository<Noshow, NoshowPK> {
    boolean existsByPostIdAndUserId(Integer postId, Integer userId);
    List<Integer> findUserIdByPostId(Integer postId);
    Noshow findByPostIdAndUserId(Integer postId, Integer userId);
}
