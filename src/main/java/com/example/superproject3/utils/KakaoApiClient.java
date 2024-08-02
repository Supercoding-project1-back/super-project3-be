package com.example.superproject3.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;

@Component
public class KakaoApiClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String clientId = "your-kakao-client-id";
    private final String clientSecret = "your-kakao-client-secret";
    private final String redirectUri = "your-redirect-uri";

    public String getEmailFromKakao(String code) {
        // 토큰 요청
        String tokenUri = UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code)
                .queryParam("client_secret", clientSecret)
                .toUriString();

        String response = restTemplate.postForObject(tokenUri, null, String.class);
        JSONObject jsonObject = new JSONObject(response);

        String accessToken = jsonObject.getString("access_token");

        // 사용자 정보 요청
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
        String userInfoResponse = restTemplate.getForObject(
                userInfoUri, String.class, accessToken);

        JSONObject userInfoJson = new JSONObject(userInfoResponse);
        return userInfoJson.getJSONObject("kakao_account").getString("email");
    }
}