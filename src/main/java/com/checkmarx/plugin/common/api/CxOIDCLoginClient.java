package com.checkmarx.plugin.common.api;

import com.checkmarx.plugin.common.webBrowsing.LoginData;

public interface CxOIDCLoginClient {

    LoginData login() throws Exception;

    boolean isTokenExpired();

    LoginData getAccessTokenFromRefreshToken(String accessToken) throws Exception;
}
