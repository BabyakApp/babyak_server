package com.babyak.babyak.service;

import com.babyak.babyak.DTO.chat.CheckResponse;
import com.babyak.babyak.DTO.favorite.FavoriteDTO;
import com.babyak.babyak.domain.favorite.Favorite;
import com.babyak.babyak.domain.favorite.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public CheckResponse isLike(FavoriteDTO favoriteDTO){
        if(favoriteRepository.existsByUserIdAndPostId(favoriteDTO.getUserId(), favoriteDTO.getPostId())){
            return new CheckResponse(true, "즐겨찾기가 설정되어있습니다.");
        }
        return new CheckResponse(false, "즐겨찾기가 설정되어있지 않습니다.");
    }

    public Integer existsLike(FavoriteDTO favoriteDTO){
        if(favoriteRepository.existsByUserIdAndPostId(favoriteDTO.getUserId(), favoriteDTO.getPostId())){
            Favorite favorite = favoriteRepository.findByUserIdAndPostId(favoriteDTO.getUserId(), favoriteDTO.getPostId());
            return favorite.getLikeId();
        }
        return -1;
    }

    public CheckResponse clickLike(FavoriteDTO favoriteDTO){
        Integer flag = existsLike(favoriteDTO);
        if(flag == -1){
            createLike(favoriteDTO);
            return new CheckResponse(true, "즐겨찾기가 설정되었습니다.");
        }
        deleteLike(flag);
        return new CheckResponse(false, "즐겨찾기가 해제되었습니다.");
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

    public List<Integer> findLikes(Integer userId){
        return favoriteRepository.selectAllPosts(userId);
    }
}
