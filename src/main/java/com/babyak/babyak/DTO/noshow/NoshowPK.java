package com.babyak.babyak.DTO.noshow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NoshowPK implements Serializable {
    private Integer postId;
    private Integer userId;
}
