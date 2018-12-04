package com.checkmarx.plugin.common.webBrowsing;

public class LoginData {

    private boolean wasCanceled;
    private String accessToken;
    private String refreshToken;

    public String getIdToken() {
        return idToken;
    }

    private String idToken;

    private Long accessTokenExpirationInMillis;

    public LoginData(String accessToken, String refreshToken, Long accessTokenExpirationInMillis, String idToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpirationInMillis = accessTokenExpirationInMillis;
        this.idToken = idToken;
    }

    public LoginData(boolean wasCanceled) {
        this.wasCanceled = wasCanceled;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean wasCanceled() {
        return wasCanceled;
    }

    public Long getAccessTokenExpirationInMillis() {
        return accessTokenExpirationInMillis;
    }

}