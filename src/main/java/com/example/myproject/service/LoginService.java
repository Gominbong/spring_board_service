package com.example.myproject.service;

import com.example.myproject.controller.LoginController;
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
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private static String loginId;

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

        SecretKey key = Keys.hmacShaKeyFor("c3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwK".getBytes(StandardCharsets.UTF_8));
        long expiredTime = 1000 * 60L * 30L; // 토큰 유효 시간 (30분)
        Date ext = new Date();
        ext.setTime(ext.getTime() + expiredTime);

        String jwt = Jwts.builder()
                .header()
                .keyId("jwt")
                .and()
                .subject(loginFormDto.getId())
                .signWith(key, Jwts.SIG.HS512)
                .expiration(ext)
                .compact();
        log.info("jwt 생성 = {}", jwt);
        Cookie cookie = new Cookie("jwtToken", jwt);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        response.addCookie(cookie);

        Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);
        Object payload = claimsJws.getPayload();
        log.info("jwt 검증 = {}", claimsJws);
        log.info("jwt 검증 = {}", payload);


        return jwt;

    }

    public String loginIdCheck(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Cookie jwtCookie = WebUtils.getCookie(request, "jwtToken");

        if (session != null && jwtCookie != null ) {
            LoginService.loginId = (String) session.getAttribute("loginId");
            log.info("세션 로그인 id = {}", loginId);

            SecretKey key = Keys.hmacShaKeyFor("c3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwK".getBytes(StandardCharsets.UTF_8));
            try{
                Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtCookie.getValue());

                log.info("jwt 만료 확인 = {}", claimsJws);
            } catch (Exception e){
                log.info("Jwt Exception 확인 = {}", e.toString());
                log.info("Jwt 유효시간 초과 로그아웃 됨");
                loginId = null;
            }

            long expiredTime = 1000 * 60L * 30L; // 토큰 유효 시간 (30분)
            Date ext = new Date();
            ext.setTime(ext.getTime() + expiredTime);

            String jwt = Jwts.builder()
                    .header()
                    .keyId("jwt")
                    .and()
                    .subject(loginId)
                    .signWith(key, Jwts.SIG.HS512)
                    .expiration(ext)
                    .compact();
            log.info("jwt 생성 = {}", jwt);
            Cookie cookie = new Cookie("jwtToken", jwt);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            response.addCookie(cookie);

        }

        return LoginService.loginId;

    }

    public void logout(){
        loginId = null;
        log.info("로그아웃 완료 = {}", loginId);
    }

}




