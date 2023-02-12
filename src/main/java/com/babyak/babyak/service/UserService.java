package com.babyak.babyak.service;

import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import com.babyak.babyak.dto.user.SignUpRequestDTO;
import com.babyak.babyak.dto.user.TokenResponseDTO;
import com.babyak.babyak.security.jwt.JwtTokenProvider;
import com.nimbusds.oauth2.sdk.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenResponseDTO signup(SignUpRequestDTO reqDTO) {
        User user = userRepository.findByEmail(reqDTO.getEmail());
        String accessToken = jwtTokenProvider.createToken(user.getUserId(), user.getEmail());

        user.signup(reqDTO);
        user.setToken(accessToken);

        userRepository.save(user);

        return new TokenResponseDTO(accessToken, "refresh");
    }

}
