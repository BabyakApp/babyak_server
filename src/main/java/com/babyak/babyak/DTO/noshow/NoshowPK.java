package com.babyak.babyak.dto.noshow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NoshowPK implements Serializable {
    private Integer postId;
    private Integer userId;
}
