package com.babyak.babyak.service;

import com.babyak.babyak.DTO.favorite.FavoriteDTO;
import com.babyak.babyak.domain.favorite.Favorite;
import com.babyak.babyak.domain.favorite.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public boolean isLike(FavoriteDTO favoriteDTO){
        return favoriteRepository.existsByUserIdAndPostId(favoriteDTO.getUserId(), favoriteDTO.getPostId());
    }
    public Integer existsLike(FavoriteDTO favoriteDTO){
        if(isLike(favoriteDTO)){
            Favorite favorite = favoriteRepository.findByUserIdAndPostId(favoriteDTO.getUserId(), favoriteDTO.getPostId());
            return favorite.getLikeId();
        }
        return -1;
    }

    public Integer clickLike(FavoriteDTO favoriteDTO){
        Integer flag = existsLike(favoriteDTO);
        if(flag == -1){
            createLike(favoriteDTO);
            return 1;
        }
        deleteLike(flag);
        return 0;
    }
    public void createLike(FavoriteDTO favoriteDTO){
        Favorite favorite = new Favorite();
        favorite.setPostId(favoriteDTO.getPostId());
        favorite.setUserId(favoriteDTO.getUserId());
        favoriteRepository.save(favorite);
    }
    public void deleteLike(Integer flag){
        favoriteRepository.deleteById(flag);
    }
}
