package com.example.myproject.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import java.util.Date;

import static com.example.myproject.service.jwtKey.key;

@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        Cookie jwtCookie = WebUtils.getCookie(request, "jwtToken");
        if (jwtCookie == null){
            log.info("jwt 쿠키값 null 로그인 하세요");
            log.info("이전주소 url = {}", requestURI);
            Cookie cookie = new Cookie("url", requestURI);
            response.addCookie(cookie);
            response.sendRedirect("/loginInterceptor");
            return false;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtCookie.getValue());
        } catch (Exception e) {
            log.info("jwt Exception 확인 = {}", e.toString());
            log.info("jwt 유효시간 초과 로그인 하세요");
            log.info("이전주소 url = {}", requestURI);
            Cookie cookie = new Cookie("url", requestURI);
            response.addCookie(cookie);
            response.sendRedirect("/loginInterceptor");
            return false;
        }

        String loginId = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtCookie.getValue()).getPayload().getSubject();
        long expiredTime = 1000 * 60L * 1000L; // 토큰 유효 시간 (30분)
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
        Object payload = claimsJws.getPayload();
        log.info("jwt 검증 = {}", claimsJws);
        log.info("jwt 유효시간 = {}",claimsJws.getPayload().getExpiration());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

}
