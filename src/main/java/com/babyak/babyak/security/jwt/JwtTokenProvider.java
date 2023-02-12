package com.babyak.babyak.security.jwt;

import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.security.oauth2.PrincipalDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;
    private Long tokenPeriod = 1000L * 60L * 60L;                     // 1 hour
    private Long refreshPeriod = 1000L * 60L * 60L * 24L * 30L;       // 30 days


    // Encode secretKey
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    // 토큰 생성
    public String createToken(Integer userId, String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.tokenPeriod);

        Claims claims = Jwts.claims()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity);

        claims.put("userId", userId);
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

//    public Authentication getAuthentication(String token) {
//        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//
//        User user = new User()
//    }


}
