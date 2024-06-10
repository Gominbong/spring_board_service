package com.example.myproject.service;

import com.example.myproject.domain.Member;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member login(LoginFormDto loginFormDto) {

        Member result = memberRepository.findEncodePassword(loginFormDto.getId());
        log.info("암호화 비밀번호 가져오기 확인  = '{}'", result);

        if (result == null) {
            return null;
        }
        boolean matches = passwordEncoder.matches(loginFormDto.getPw(), result.getPassword());
        if (matches) {
            log.info("loginId 값으로 db에 있는 pw 조회성공");
            return result;
        }
        return null;
    }

    public String createJwt(LoginFormDto loginFormDto, HttpServletRequest request, HttpServletResponse response) {

        String signatureSecretKey = "c3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwK";
        SecretKey key = Keys.hmacShaKeyFor(signatureSecretKey.getBytes(StandardCharsets.UTF_8));
        long expiredTime = 1000 * 60L * 30L; // 토큰 유효 시간 (30분)
        Date ext = new Date();
        ext.setTime(ext.getTime() + expiredTime );

        String jwt = Jwts.builder()
                .header()
                .keyId("jwt")
                .and()
                .subject(loginFormDto.getId())
                .signWith(key,Jwts.SIG.HS512)
                .expiration(ext)
                .compact();
        log.info("jwt 생성 = {}", jwt);
        Cookie cookie = new Cookie("jwt",jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);
        Object payload = claimsJws.getPayload();
        log.info("jwt 검증 = {}", claimsJws);
        log.info("jwt 검증 = {}", payload);


        return jwt;

    }
}








