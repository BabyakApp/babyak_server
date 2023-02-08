package com.babyak.babyak.config.oauth;

import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        System.out.println("----------------------- " + oAuth2User.getAttributes());

        Integer userId = 0;
        String email = oAuth2User.getAttribute("email");
        Boolean isBlocked = false;
        Boolean isJoined = false;
        Boolean isEwha = false;


        // idBlocked 확인


        // isJoined 확인
        User userEntity = userRepository.findByEmail(email);
        if(userEntity != null) isJoined = true;


        // 이화인 이메일 여부
        String domain = email.substring(email.indexOf('@') + 1);
        if(domain.equals("ewhain.net")) isEwha = true;


        if(!isBlocked && !isJoined && isEwha) {
            userEntity = User.builder()
                    .studentId(0)
                    .email(email)
                    .nickname("nickname")
                    .depart("depart")
                    .major("major")
                    .noShow(0)
                    .role("ROLE_AUTH")
                    .build();

            userRepository.save(userEntity);
            response.sendRedirect("/auth");
        }

        else if(isBlocked) response.sendRedirect("/auth/reject/blocked");
        else if(!isEwha) response.sendRedirect("/auth/reject/ewhain");

        else if(isJoined) response.sendRedirect("/user/info");




    }
}
