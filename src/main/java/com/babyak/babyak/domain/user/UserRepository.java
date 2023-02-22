package com.babyak.babyak.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String email);

    public User findByNickname(String nickname);

    public User findByUserId(Integer userId);
}
