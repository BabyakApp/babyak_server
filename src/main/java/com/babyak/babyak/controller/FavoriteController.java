package com.babyak.babyak.controller;

import com.babyak.babyak.DTO.ResponseDTO;
import com.babyak.babyak.DTO.favorite.FavoriteDTO;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    FavoriteService favoriteService;

    @PutMapping("/{postId}")
    public ResponseEntity clickLike(@PathVariable Integer postId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = principalDetails.getUser();
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setPostId(postId);
        favoriteDTO.setUserId(user.getUserId());
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                favoriteService.clickLike(favoriteDTO)
        ), HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity isLike(@PathVariable Integer postId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = principalDetails.getUser();
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setPostId(postId);
        favoriteDTO.setUserId(user.getUserId());

        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                favoriteService.isLike(favoriteDTO)
        ), HttpStatus.OK);
    }

    @GetMapping // 즐겨찾기 설정한 PostId list 반환
    public ResponseEntity findLikes(@AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = principalDetails.getUser();
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                favoriteService.findLikes(user.getUserId())
                ), HttpStatus.OK);
    }

}
