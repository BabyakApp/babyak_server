package com.babyak.babyak.controller;

import com.babyak.babyak.DTO.favorite.FavoriteDTO;
import com.babyak.babyak.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    FavoriteService favoriteService;

    @PutMapping
    public Integer clickLike(@RequestBody FavoriteDTO favoriteDTO){
        return favoriteService.clickLike(favoriteDTO);
    }

    @GetMapping
    public boolean isLike(@RequestBody FavoriteDTO favoriteDTO){
        return favoriteService.isLike(favoriteDTO);
    }
}
