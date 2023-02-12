package com.babyak.babyak.config.oauth2;

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

        String email = oAuth2User.getAttribute("email");
        Boolean isBlocked = false;
        Boolean isEwha = false;


        // 이미 가입한 적 있는 유저인 경우
        User userEntity = userRepository.findByEmail(email);
        if(userEntity != null) {
            response.sendRedirect("/user/info");
            return;
        }


        // idBlocked 확인





        // isEwha 확인
        String domain = email.substring(email.indexOf('@') + 1);
        if(domain.equals("ewhain.net")) isEwha = true;


        // Entity 생성
        if(!isBlocked && isEwha) {
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
            response.sendRedirect("/user/signup/" + email);
        }

        else if(!isEwha) response.sendRedirect("/user/reject/" + email + "/domain");
        else if(isBlocked) response.sendRedirect("/user/reject" + email + "/blocked");

    }
}
