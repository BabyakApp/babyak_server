package com.babyak.babyak.security.oauth2;

import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import com.babyak.babyak.domain.withdrawal.Withdrawal;
import com.babyak.babyak.domain.withdrawal.WithdrawalRepository;
import com.babyak.babyak.dto.token.TokenDTO;
import com.babyak.babyak.dto.user.AuthResponseDTO;
import com.babyak.babyak.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();


        // 이미 가입한 적 있는 유저인 경우
        User userEntity = userRepository.findByEmail(email);
        if(userEntity != null) {

            // (1) 탈퇴 이력 X
            if(withdrawalRepository.findByUser(userEntity) == null) {
                String accessToken = jwtTokenProvider.createAccessToken(userEntity.getUserId(), userEntity.getEmail());
                String refreshToken = jwtTokenProvider.createRefreshToken(userEntity.getUserId(), userEntity.getEmail());
                TokenDTO tokenDTO = new TokenDTO(accessToken, refreshToken);

                String result = objectMapper.writeValueAsString(new AuthResponseDTO(email, "existed", tokenDTO));
                response.getWriter().write(result);
            }

            // (2) 탈퇴 이력 O
            Withdrawal withdrawal = withdrawalRepository.findByUser(userEntity);

            // (2-1) 강제 탈퇴
            if(withdrawal.getBlocked()) {
                String result = objectMapper.writeValueAsString(new AuthResponseDTO(email, "blocked", null));
                response.getWriter().write(result);
            }

            // (2-2) 자진 탈퇴
            else {
                String result = objectMapper.writeValueAsString(new AuthResponseDTO(email, "withdraw", null));
                response.getWriter().write(result);
            }

        }


        // 회원가입

        // isEwha 확인
        Boolean isEwha = false;
        String domain = email.substring(email.indexOf('@') + 1);
        if(domain.equals("ewhain.net")) isEwha = true;


        // (1) 이화인 계정 O : Entity 생성
        if(isEwha) {
            userRepository.save(principalDetails.getUser());
            String result = objectMapper.writeValueAsString(new AuthResponseDTO(email, "new", null));
            response.getWriter().write(result);
        }

        // (2) 이화인 계정 X : reject
        else {
            String result = objectMapper.writeValueAsString(new AuthResponseDTO(email, "domain", null));
            response.getWriter().write(result);
        }
    }
}
