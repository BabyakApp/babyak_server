package com.babyak.babyak.service;

import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import com.babyak.babyak.dto.user.SignUpRequestDTO;
import com.babyak.babyak.dto.token.TokenResponseDTO;
import com.babyak.babyak.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Boolean availableNickname(String nickname) {
        User user = userRepository.findByNickname(nickname);

        if(user == null) return true;
        return false;
    }

    @Transactional
    public TokenResponseDTO signup(SignUpRequestDTO reqDTO) {
        User user = userRepository.findByEmail(reqDTO.getEmail());

        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getEmail());
        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO(accessToken, refreshToken);

        user.signup(reqDTO);
        user.setToken(accessToken);
        userRepository.save(user);

        return tokenResponseDTO;
    }

}
