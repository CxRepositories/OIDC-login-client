package com.checkmarx.plugin.common.api;

import com.checkmarx.plugin.common.exceptions.CxRestClientException;
import com.checkmarx.plugin.common.exceptions.CxRestLoginException;
import com.checkmarx.plugin.common.exceptions.CxValidateResponseException;
import com.checkmarx.plugin.common.webBrowsing.LoginData;

public interface CxOIDCLoginClient {

    LoginData login() throws Exception;

    boolean isTokenExpired(Long expirationTime);

    LoginData getAccessTokenFromRefreshToken(String accessToken) throws CxRestClientException, CxRestLoginException, CxValidateResponseException;
}
