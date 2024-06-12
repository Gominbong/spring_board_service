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

import static com.example.myproject.service.jwtKey.key;

@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        Cookie jwtCookie = WebUtils.getCookie(request, "jwtToken");
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtCookie.getValue());
        } catch (Exception e) {
            log.info("jwt Exception 확인 = {}", e.toString());
            log.info("쿠키값 = null 혹은 jwt 유효시간 초과");
            Cookie cookie = new Cookie("url", requestURI);
            response.addCookie(cookie);
            response.sendRedirect("/loginInterceptor");
        }

        log.info("로그인 유지 상태 입니다");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

}
