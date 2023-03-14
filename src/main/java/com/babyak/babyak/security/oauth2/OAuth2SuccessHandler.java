package com.babyak.babyak.security.oauth2;

import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import com.babyak.babyak.domain.withdrawal.Withdrawal;
import com.babyak.babyak.domain.withdrawal.WithdrawalRepository;
import com.babyak.babyak.DTO.token.TokenDTO;
import com.babyak.babyak.DTO.user.AuthResponseDTO;
import com.babyak.babyak.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        String targetUrl, result;

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();


        // (1) 이미 가입한 적 있는 유저인 경우
        User userEntity = userRepository.findByEmail(email);
        if(userEntity != null) {

            // (1-1) 탈퇴 이력 O
            if(withdrawalRepository.findByUser(userEntity) != null) {
                Withdrawal withdrawal = withdrawalRepository.findByUser(userEntity);

                // (1-1-1) 강제 탈퇴
                if(withdrawal.getBlocked()) {
                    result = objectMapper.writeValueAsString(new AuthResponseDTO(email, "blocked", null));
                }

                // (1-1-2) 자진 탈퇴
                else {
                    result = objectMapper.writeValueAsString(new AuthResponseDTO(email, "withdraw", null));
                }

                targetUrl = UriComponentsBuilder.fromUriString("/user/auth")
                        .queryParam("result", result)
                        .build().toUriString();
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
                return;
            }


            // (1-2) 탈퇴 이력 X

            // (1-2-1) 기존 사용자가 로그인
            if(userEntity.getRole().equals("ROLE_USER")) {
                String accessToken = jwtTokenProvider.createAccessToken(userEntity.getUserId(), userEntity.getEmail());
                String refreshToken = jwtTokenProvider.createRefreshToken(userEntity.getUserId(), userEntity.getEmail());
                TokenDTO tokenDTO = new TokenDTO(accessToken, refreshToken);
                result = objectMapper.writeValueAsString(new AuthResponseDTO(email, "existed", tokenDTO));
            }

            // (1-2-2) 인증 완료된 신규 사용자가 로그인
            else {
                result = objectMapper.writeValueAsString(new AuthResponseDTO(email, "new", null));
            }

            targetUrl = UriComponentsBuilder.fromUriString("/user/auth")
                    .queryParam("result", result)
                    .build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            return;
        }


        // (2) 가입한 적 없는 사용자
        String domain = email.substring(email.indexOf('@') + 1);

        // (2-1) 이화인 계정 O
        if(domain.equals("ewhain.net")) {
            userRepository.save(principalDetails.getUser());
            result = objectMapper.writeValueAsString(new AuthResponseDTO(email, "new", null));
        }

        // (2-2) 이화인 계정 X
        else {
            result = objectMapper.writeValueAsString(new AuthResponseDTO(email, "domain", null));
        }

        targetUrl = UriComponentsBuilder.fromUriString("/user/auth")
                .queryParam("result", result)
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
