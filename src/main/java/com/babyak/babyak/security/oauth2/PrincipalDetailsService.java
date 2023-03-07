package com.babyak.babyak.security.oauth2;

import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public PrincipalDetails loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        User userEntity = userRepository.findByEmail(email);

        if(userEntity == null) {
            userEntity = User.builder()
                    .studentId(0)
                    .email(email)
                    .nickname(email)
                    .depart("depart")
                    .major("major")
                    .noShow(0)
                    .role("ROLE_AUTH")
                    .build();
        }

        return new PrincipalDetails(userEntity);
    }

}
