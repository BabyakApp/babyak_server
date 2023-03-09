package com.babyak.babyak.domain.noshow;

import com.babyak.babyak.DTO.noshow.NoshowPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoshowRepository extends JpaRepository<Noshow, NoshowPK> {
    boolean existsByPostIdAndUserId(Integer postId, Integer userId);
}
