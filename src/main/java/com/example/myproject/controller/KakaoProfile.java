package com.example.myproject.controller;

import lombok.Data;

@Data
public class KakaoProfile {

    public String id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Data
    public static class Properties{
        public String nickname;
    }

    @Data
    public static class KakaoAccount{
        public Boolean profile_nickname_needs_agreement;
        public Profile profile;
    }

    @Data
    public static class Profile{
        public String nickname;
        public Boolean is_default_nickname;
    }

}
