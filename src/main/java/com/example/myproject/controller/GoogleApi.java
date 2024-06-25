package com.example.myproject.controller;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Slf4j
@Component(value = "GoogleApi")
public class GoogleApi {
    @Value("${google.client_id}")
    private String googleClientId;

    @Value("${google.client_secret}")
    private String googleClientSecret;

    @Value("${google.redirect_uri}")
    private String googleRedirectUri;
}
