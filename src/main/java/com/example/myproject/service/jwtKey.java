package com.example.myproject.service;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public interface jwtKey {

   public static SecretKey key = Keys.hmacShaKeyFor("c3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwK".getBytes(StandardCharsets.UTF_8));

}
