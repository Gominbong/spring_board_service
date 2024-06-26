package com.example.myproject.service;

import com.example.myproject.controller.*;
import com.example.myproject.domain.Member;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
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
        cookie.setMaxAge((int) expiredTime);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);
        log.info("jwt 검증 = {}", claimsJws);
        log.info("jwt 유효시간 = {}", claimsJws.getPayload().getExpiration());

    }

    public String loginIdCheck(HttpServletRequest request, HttpServletResponse response) {

        String loginId;
        Cookie jwtCookie = WebUtils.getCookie(request, "jwtToken");
        if (jwtCookie == null) {
            log.info("jwt 쿠키 값 null");
            return null;
        }
        try {
            loginId = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtCookie.getValue()).getPayload().getSubject();
        } catch (Exception e) {
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
        cookie.setMaxAge((int) expiredTime);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);
        log.info("jwt 검증 = {}", claimsJws);
        log.info("jwt 유효시간 = {}", claimsJws.getPayload().getExpiration());
        return loginId;

    }

    public Member kakaoLogin(KakaoApi kakaoApi, String code) throws JsonProcessingException, ParseException {
        String state = UUID.randomUUID().toString().substring(0, 8);
        String url = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code" +
                "&client_id=" + kakaoApi.getNaverClientId() +
                "&client_secret=" + kakaoApi.getNaverClientSecret() +
                "&code=" + code +
                "&state=" + state;
        WebClient wc = WebClient.create();

        String kakaoToken = wc.post()
                .uri(url)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("카카오 로그인 Json = {}", kakaoToken);
        JSONParser parserAccessToken = new JSONParser();
        JSONObject jsonObj = (JSONObject) parserAccessToken.parse(kakaoToken);
        String access_token = (String) jsonObj.get("access_token");
        log.info("카카오 엑세스 토큰 = {}", access_token);

        String userInfo = wc.post()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + access_token)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("카카오 유저 정보 Json = {}", userInfo);
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = objectMapper.readValue(userInfo, KakaoProfile.class);
        log.info("카카오 유저 프로필 = {}", kakaoProfile.kakao_account);
        Member result = memberRepository.findByLoginId(kakaoProfile.id);
        if (result == null) {
            String uuid = UUID.randomUUID().toString().substring(0, 4);
            String pw = UUID.randomUUID().toString().substring(0, 8);
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");

            Member member = new Member();
            member.setCreateTime(createTime);
            member.setLoginId("kakao_" + kakaoProfile.id);
            member.setPassword(passwordEncoder.encode(pw));
            member.setNickname(kakaoProfile.properties.nickname + "_" + uuid);
            member.setCash(20000);
            member.setRevenue(0);
            memberRepository.save(member);
            log.info("네이버 회원가입 완료");
            return member;
        }
        return result;
    }

    public Member naverLogin(NaverApi naverApi, String code) throws JsonProcessingException, ParseException {
        String state = UUID.randomUUID().toString().substring(0, 8);
        String url = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code" +
                "&client_id=" + naverApi.getNaverClientId() +
                "&client_secret=" + naverApi.getNaverClientSecret() +
                "&code=" + code +
                "&state=" + state;
        WebClient wc = WebClient.create();

        String naverToken = wc.post()
                .uri(url)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("네이버 로그인 Json = {}", naverToken);
        JSONParser parserAccessToken = new JSONParser();
        JSONObject jsonObj = (JSONObject) parserAccessToken.parse(naverToken);
        String access_token = (String) jsonObj.get("access_token");
        log.info("네이버 엑세스 토큰 = {}", access_token);

        String userInfo = wc.post()
                .uri("https://openapi.naver.com/v1/nid/me")
                .header("Authorization", "Bearer " + access_token)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("네이버 유저 정보 Json = {}", userInfo);
        ObjectMapper objectMapper = new ObjectMapper();
        NaverProfile naverProfile = objectMapper.readValue(userInfo, NaverProfile.class);
        log.info("네이버 유저 프로필 = {}", naverProfile.getResponse());

        Member result = memberRepository.findByLoginId(naverProfile.getResponse().id);
        if (result == null) {
            String uuid = UUID.randomUUID().toString().substring(0, 4);
            String pw = UUID.randomUUID().toString().substring(0, 8);
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");

            Member member = new Member();
            member.setCreateTime(createTime);
            member.setLoginId("naver_" + naverProfile.getResponse().id);
            member.setPassword(passwordEncoder.encode(pw));
            member.setNickname(naverProfile.getResponse().name + "_" + uuid);
            member.setCash(20000);
            member.setRevenue(0);
            memberRepository.save(member);
            log.info("네이버 회원가입 완료");
            return member;
        }
        return result;
    }

    public Member googleLogin(GoogleApi googleApi, String code) throws JsonProcessingException, ParseException {
        WebClient wc = WebClient.create();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleApi.getGoogleClientId());
        params.add("client_secret", googleApi.getGoogleClientSecret());
        params.add("redirect_uri", googleApi.getGoogleRedirectUri());
        params.add("grant_type", "authorization_code");

        String googleToken = wc.post()
                .uri("https://oauth2.googleapis.com/token")
                .body(BodyInserters.fromFormData(params))
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("구글 로그인 Json = {}", googleToken);
        JSONParser parserAccessToken = new JSONParser();
        JSONObject jsonObj = (JSONObject) parserAccessToken.parse(googleToken);
        String access_token = (String) jsonObj.get("access_token");
        log.info("구글 엑세스 토큰 = {}", access_token);

        String userInfo = wc.get()
                .uri("https://www.googleapis.com/userinfo/v2/me")
                .header("Authorization", "Bearer" + access_token)
                .header("Content-type", "application/xml;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("구글 유저 정보 Json = {}", userInfo);
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleProfile googleProfile = objectMapper.readValue(userInfo, GoogleProfile.class);
        log.info("구글 유저 프로필 = {}", googleProfile.getName());

        Member result = memberRepository.findByLoginId(googleProfile.getId());
        if (result == null) {
            String uuid = UUID.randomUUID().toString().substring(0, 4);
            String pw = UUID.randomUUID().toString().substring(0, 8);
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");

            Member member = new Member();
            member.setCreateTime(createTime);
            member.setLoginId("google_" + googleProfile.getId());
            member.setPassword(passwordEncoder.encode(pw));
            member.setNickname(googleProfile.getName() + "_" + uuid);
            member.setCash(20000);
            member.setRevenue(0);
            memberRepository.save(member);
            log.info("구글 회원가입 완료");
            return member;
        }
        return result;
    }
}



