package com.babyak.babyak.domain.withdrawal;

import com.babyak.babyak.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Integer> {

    public Withdrawal findByUser(User user);
}
