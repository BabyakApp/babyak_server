package com.babyak.babyak.controller;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import com.babyak.babyak.DTO.favorite.FavoriteDTO;
import com.babyak.babyak.domain.favorite.Favorite;
import com.babyak.babyak.domain.post.Post;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    FavoriteService favoriteService;

    @PutMapping("/{postId}")
    public Integer clickLike(@PathVariable Integer postId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = principalDetails.getUser();
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setPostId(postId);
        favoriteDTO.setUserId(user.getUserId());
        return favoriteService.clickLike(favoriteDTO);
    }

    @GetMapping("/{postId}")
    public boolean isLike(@PathVariable Integer postId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = principalDetails.getUser();
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setPostId(postId);
        favoriteDTO.setUserId(user.getUserId());
        return favoriteService.isLike(favoriteDTO);
    }

    @GetMapping // 즐겨찾기 설정한 PostId list 반환
    public List<Integer> findLikes(@AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = principalDetails.getUser();
        return favoriteService.findLikes(user.getUserId());
    }

}
