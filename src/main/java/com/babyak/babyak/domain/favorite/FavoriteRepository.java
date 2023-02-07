package com.babyak.babyak.domain.favorite;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    boolean existsByUserIdAndPostId(Integer userId, Integer postId);
    Favorite findByUserIdAndPostId(Integer userId, Integer postId);
}
