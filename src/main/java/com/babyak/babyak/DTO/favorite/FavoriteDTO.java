package com.babyak.babyak.dto.favorite;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FavoriteDTO {
    private Integer userId;
    private Integer postId;
}
