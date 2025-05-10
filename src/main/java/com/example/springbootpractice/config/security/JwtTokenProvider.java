package com.example.springbootpractice.config.security;

import com.example.springbootpractice.model.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenExpTime;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(
        @Value("${jwt.secret}")
        String secretKey,
        @Value("${jwt.expiration_time}")
        long accessTokenExpTime,
        UserDetailsService userDetailsService
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.userDetailsService = userDetailsService;
    }


    // JWT 토큰 생성
    public String createToken(String email, Role role) {
        Date now = new Date();
        return Jwts.builder()
            .claims()
            .add("email", email)
            .add("role", role)
            .and()
            .issuedAt(now)
            .expiration(new Date(now.getTime() + this.accessTokenExpTime)) // set Expire Time
            .signWith(this.key)
            .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(
            userDetails,
            "",
            userDetails.getAuthorities()
        );
    }

    // 토큰에서 회원 정보 추출
    public String getUserEmail(String token) {
        return Jwts.parser()
            .verifyWith(this.key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("email", String.class);
    }

    // Request의 Header에서 token 값을 가져옵니다.
    public String getAuthorizationHeaderValue(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    // 토큰의 유효성 체크
    public boolean isValidToken(String token) {
        try {
            parseClaims(token);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    public boolean isNotValidToken(String token) {
        return !isValidToken(token);
    }

    // 토큰의 만료 유무 체크
    public boolean isExpiredToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * JWT Claims 추출
     *
     * @param token
     * @return JWT Claims
     */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean isBearerToken(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    public boolean isNotBearerToken(String token) {
        return !isBearerToken(token);
    }

    public String getToken(String token) {
        return token.split(" ")[1];
    }

}
