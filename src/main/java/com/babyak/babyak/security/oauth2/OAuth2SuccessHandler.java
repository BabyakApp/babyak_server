package com.babyak.babyak.security.oauth2;

import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import com.babyak.babyak.domain.withdrawal.Withdrawal;
import com.babyak.babyak.domain.withdrawal.WithdrawalRepository;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();


        // 이미 가입한 적 있는 유저인 경우
        User userEntity = userRepository.findByEmail(email);
        if(userEntity != null) {

            // (1) 탈퇴 이력 X
            if(withdrawalRepository.findByUser(userEntity) == null) {
                response.sendRedirect("/user/auth/ok/" + email);
                return;
            }

            // (2) 탈퇴 이력 O
            Withdrawal withdrawal = withdrawalRepository.findByUser(userEntity);

            // (2-1) 강제 탈퇴
            if(withdrawal.getBlocked()) {
                response.sendRedirect("/user/reject/" + email + "/blocked");
                return;
            }

            // (2-2) 자진 탈퇴
            else {
                response.sendRedirect("/user/reject/" + email + "/withdraw");
                return;
            }

        }


        // 회원가입

        // isEwha 확인
        Boolean isEwha = false;
        String domain = email.substring(email.indexOf('@') + 1);
        if(domain.equals("ewhain.net")) isEwha = true;


        // (1) 이화인 계정 O : Entity 생성
        if(isEwha) {
            userEntity = principalDetails.getUser();
            userRepository.save(userEntity);

            response.sendRedirect("/user/signup/" + email);
        }

        // (2) 이화인 계정 X : reject
        else if(!isEwha) response.sendRedirect("/user/reject/" + email + "/domain");
    }
}
