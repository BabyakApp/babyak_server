package com.babyak.babyak.domain.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    boolean existsByUserIdAndPostId(Integer userId, Integer postId);
    Favorite findByUserIdAndPostId(Integer userId, Integer postId);

    @Query(value = "select favorite.postId from Favorite favorite where favorite.userId = :userId")
    List<Integer> selectAllPosts(@Param(value = "userId") Integer userId);
}
