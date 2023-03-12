package com.babyak.babyak.security.jwt;

import com.babyak.babyak.common.error.CustomException;
import com.babyak.babyak.common.error.ErrorCode;
import com.babyak.babyak.dto.token.TokenDTO;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.security.oauth2.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final UserDetailsServiceImpl userDetailsService;
    private final RedisUtil redisUtil;

    @Value("${spring.jwt.secret}")
    private String secretKey;

//    private long accessTokenValidTime = 60 * 60 * 1000L;                     // 1 hour
    private long accessTokenValidTime = 5 * 60 * 1000L;                      // 5 min - for test
    private long refreshTokenValidTime = 60 * 60 * 24 * 30 * 1000L;          // 30 days


    // Encode secretKey
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    // Access Token 생성
    public String createAccessToken(Integer userId, String email) {
        return createToken(userId, email, accessTokenValidTime);
    }


    // Refresh Token 생성
    public String createRefreshToken(Integer userId, String email) {
        String refreshToken = createToken(userId, email, refreshTokenValidTime);
        redisUtil.setRedisRefreshToken(email, refreshToken);

        return refreshToken;
    }


    // Token 생성
    public String createToken(Integer userId, String email, long validTime) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validTime);

        // register claims
        Claims claims = Jwts.claims()
                .setSubject(email);

        // private claims
        claims.put("userId", userId);
        claims.put("email", email);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }


    // Header에서 Bearer 토큰 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }


    // Token 유효성 확인
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return claims != null;
        } catch (SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다." + e);
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        } catch (SignatureException e) {
            logger.info("SignatureException " + e);
        }
        return false;
    }


    // Token -> Authentication 객체
    public Authentication getAuthentication(String token) {
        try {
            PrincipalDetails principalDetails = userDetailsService.loadUserByUsername(this.getTokenEmail(token));
            return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;

    }


    // Token -> User Id 꺼내기
    public Integer getTokenUserId(String token) {
        return (Integer) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userId");
    }


    // Token -> User Email 꺼내기
    public String getTokenEmail(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email");
    }


    // Redis 에 해당 유저의 RefreshToken 존재하는지 여부
    public boolean existRefreshToken(String refreshToken) {
        String key = getTokenEmail(refreshToken);  // key = email
        String redisRefreshToken = redisUtil.getRedisRefreshToken(key);

        if(refreshToken.equals(redisRefreshToken)) return true;
        return false;
    }


    // Access & Refresh Token 재발급
    public TokenDTO reissueToken(String refreshToken) {
        boolean validRefreshToken = this.validateToken(refreshToken);
        boolean existRefreshToken = this.existRefreshToken(refreshToken);

        if(validRefreshToken && existRefreshToken) {
            String email = this.getTokenEmail(refreshToken);
            Integer userId = this.getTokenUserId(refreshToken);

            String newAccessToken = this.createAccessToken(userId, email);
            String newRefreshToken = this.createRefreshToken(userId, email);

            // Redis 업데이트
            redisUtil.setRedisRefreshToken(email, newRefreshToken);

            return new TokenDTO(newAccessToken, newRefreshToken);

        }

        // refresh token도 만료 ->  로그인 필요
        throw new RuntimeException("리프레시 토큰 만료, 재로그인 해주세요");
    }


    // token 의 남은 유효 시간 구하기
    public long getExpiration(String token) {
        Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
        Long now = new Date().getTime();

        return (expiration.getTime() - now);
    }


}
