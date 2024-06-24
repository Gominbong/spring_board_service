package com.example.myproject.controller;

import lombok.Data;
import org.apache.catalina.connector.Response;

@Data
public class NaverProfile {

    public String resultcode;
    public String message;
    public Response response;

    @Data
    public static class Response{
        public String id;
        public String email;
        public String name;
    }
}
