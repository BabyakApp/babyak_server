package com.babyak.babyak.DTO.post;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostKeyDTO implements Serializable {
    private Integer postId;
    private Integer userId;
}
