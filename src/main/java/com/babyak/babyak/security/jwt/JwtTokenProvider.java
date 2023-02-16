package com.babyak.babyak.security.jwt;

import com.babyak.babyak.dto.token.TokenResponseDTO;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.security.oauth2.PrincipalDetailsService;
import com.babyak.babyak.security.oauth2.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsServiceImpl userDetailsService;

    @Value("${jwt.secretKey}")
    private String secretKey;
    private Long tokenPeriod = 1000L * 60L * 60L;                     // 1 hour
    private Long refreshPeriod = 1000L * 60L * 60L * 24L * 30L;       // 30 days


    // Encode secretKey
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    // Access Token 생성
    public String createAccessToken(Integer userId, String email) {
        return createToken(userId, email, tokenPeriod);
    }

    // Access Token 생성
    public String createRefreshToken(Integer userId, String email) {
        return createToken(userId, email, refreshPeriod);
    }


    // Token 생성
    public String createToken(Integer userId, String email, Long validTime) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validTime);

        Claims claims = Jwts.claims()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity);

        claims.put("userId", userId);
        claims.put("email", email);

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        return token;
    }


    // Token 유효성 확인
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰");
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    // Token ->> Authentication 객체
    public Authentication getAuthentication(String token) {
        PrincipalDetails principalDetails = userDetailsService.loadUserByUsername(this.getTokenEmail(token));
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }


    // Token ->> User Id 꺼내기
    public Integer getTokenUserId(String token) {
        return (Integer) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userId");
    }


    // Token ->> User Email 꺼내기
    public String getTokenEmail(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email");
    }


}
