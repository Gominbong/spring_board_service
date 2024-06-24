package com.example.myproject.service;

import com.example.myproject.controller.NaverProfile;
import com.example.myproject.domain.Member;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static com.example.myproject.service.jwtKey.key;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    long expiredTime = 1000 * 60L * 30L; // 토큰 유효 시간 (30분)




    public Member naverSignupCheck(NaverProfile naverProfile) {
        Member result = memberRepository.findByLoginId(naverProfile.getResponse().id);

        if (result == null){
            String uuid = UUID.randomUUID().toString().substring(0, 4);
            String pw = UUID.randomUUID().toString().substring(0, 8);
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");

            Member member = new Member();
            member.setCreateTime(createTime);
            member.setLoginId(naverProfile.getResponse().id);
            member.setPassword(pw);
            member.setNickname(naverProfile.getResponse().name + uuid);
            member.setCash(20000);
            member.setRevenue(0);
            memberRepository.save(member);
            log.info("네이버 회원가입 완료");
        }
        return result;
    }

    public Member login(LoginFormDto loginFormDto) {

        Member result = memberRepository.findEncodePasswordQueryDsl(loginFormDto.getId());
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

    public void createJwt(String loginId, HttpServletResponse response) {

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
        cookie.setPath("/");
        cookie.setMaxAge((int)expiredTime);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);
        log.info("jwt 검증 = {}", claimsJws);
        log.info("jwt 유효시간 = {}",claimsJws.getPayload().getExpiration());

    }

    public String loginIdCheck(HttpServletRequest request, HttpServletResponse response) {

        String loginId;
        Cookie jwtCookie = WebUtils.getCookie(request, "jwtToken");
        if (jwtCookie == null){
            log.info("jwt 쿠키 값 null");
            return null;
        }
        try{
            loginId = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtCookie.getValue()).getPayload().getSubject();
        }catch (Exception e){
            log.info("jwt Exception 확인 = {}", e.toString());
            log.info("jwt 유효시간 초과 비로그인 상태");
            return null;
        }

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
        cookie.setPath("/");
        cookie.setMaxAge((int)expiredTime);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);
        log.info("jwt 검증 = {}", claimsJws);
        log.info("jwt 유효시간 = {}",claimsJws.getPayload().getExpiration());
        Member member = memberRepository.findByLoginIdQueryDsl(loginId);
        return loginId;

    }
}




