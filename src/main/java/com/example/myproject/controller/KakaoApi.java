package com.example.myproject.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Slf4j
@Component(value = "kakaoApi")
public class KakaoApi {

    @Value("${kakao.client_id}")
    private String naverClientId;

    @Value("${kakao.client_secret}")
    private String naverClientSecret;

    @Value("${kakao.redirect_uri}")
    private String naverRedirectUri;

}
