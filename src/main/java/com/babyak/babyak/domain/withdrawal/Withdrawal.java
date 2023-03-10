package com.babyak.babyak.domain.withdrawal;

import com.babyak.babyak.domain.user.User;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Withdrawal {

    @Id
    @Column(name = "withdrawal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer withdrawalId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Boolean blocked;
}
