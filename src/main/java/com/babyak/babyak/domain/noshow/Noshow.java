package com.babyak.babyak.domain.noshow;

import com.babyak.babyak.DTO.noshow.NoshowPK;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(NoshowPK.class)
public class Noshow {
    @Id
    @Column(name = "post_id")
    private Integer postId;

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "report_number")
    private Integer reportNumber;

    @Column(name = "reported")
    private boolean reported;

}
