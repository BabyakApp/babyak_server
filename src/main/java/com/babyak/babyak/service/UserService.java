package com.babyak.babyak.service;

import com.babyak.babyak.common.error.CustomException;
import com.babyak.babyak.common.error.ErrorCode;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import com.babyak.babyak.domain.withdrawal.Withdrawal;
import com.babyak.babyak.domain.withdrawal.WithdrawalRepository;
import com.babyak.babyak.dto.user.InfoUpdateRequestDTO;
import com.babyak.babyak.dto.user.SignUpRequestDTO;
import com.babyak.babyak.dto.token.TokenDTO;
import com.babyak.babyak.security.jwt.JwtTokenProvider;
import com.babyak.babyak.security.jwt.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Transactional
    public Boolean availableNickname(String nickname) {
        if(nickname.equals("(알수없음)")) return false;

        User user = userRepository.findByNickname(nickname);
        if(user == null) return true;
        return false;
    }

    @Transactional
    public TokenDTO signup(SignUpRequestDTO reqDTO) {
        User user = userRepository.findByEmail(reqDTO.getEmail());
        if(user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getEmail());
        TokenDTO tokenDTO = new TokenDTO(accessToken, refreshToken);

        user.signup(reqDTO);
        userRepository.save(user);

        return tokenDTO;
    }


    @Transactional
    public User updateInfo(User user, InfoUpdateRequestDTO reqDTO) {
        User currentUser = user;
        if(reqDTO.getNickname() != null) currentUser.setNickname(reqDTO.getNickname());
        if(reqDTO.getStudentId() != null) currentUser.setStudentId(reqDTO.getStudentId());
        if(reqDTO.getMajor() != null) currentUser.setMajor(reqDTO.getMajor());
        if(reqDTO.getDepart() != null) currentUser.setDepart(reqDTO.getDepart());

        return userRepository.save(currentUser);
    }

    public TokenDTO reissueToken(String refreshToken) {
        return jwtTokenProvider.reissueToken(refreshToken);
    }

    @Transactional
    public TokenDTO reissueToken(Integer userId, String email) {
        String accessToken = jwtTokenProvider.createAccessToken(userId, email);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, email);
        TokenDTO resDTO = new TokenDTO(accessToken, refreshToken);
        return resDTO;
    }


    public String logout(TokenDTO tokenDTO) {
        if(!jwtTokenProvider.validateToken(tokenDTO.getAccessToken())) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(tokenDTO.getAccessToken());

        // 현재 Redis에 저장된 Refresh Token 삭제
        if(redisUtil.getRedisRefreshToken(authentication.getName()) != null) {
            redisUtil.deleteData(authentication.getName());
        }

        Long expiration = jwtTokenProvider.getExpiration(tokenDTO.getAccessToken());
        redisUtil.setRedisLogoutAccTkn(tokenDTO.getAccessToken(), expiration);

        return "success";
    }


    public String withdraw(User user, TokenDTO tokenDTO) {
        String logout = logout(tokenDTO);
        if(logout == "success") {
            Withdrawal withdrawal = Withdrawal.builder()
                    .user(user)
                    .blocked(false)
                    .build();

            withdrawalRepository.save(withdrawal);
            return "success";
        }

        return "fail";
    }
}
