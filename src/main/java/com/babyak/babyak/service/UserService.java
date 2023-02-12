package com.babyak.babyak.service;

import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import com.babyak.babyak.dto.user.SignUpRequestDTO;
import com.babyak.babyak.dto.user.SignUpResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public SignUpResponseDTO signup(SignUpRequestDTO reqDTO) {
        User user = userRepository.findByEmail(reqDTO.getEmail());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ " + user.getNickname());

        if(user != null) {
            user.signup(reqDTO);
            userRepository.save(user);
        } else {
            System.out.println("로그인 X 사용자");
        }

        return new SignUpResponseDTO("access", "refresh");
    }

}
