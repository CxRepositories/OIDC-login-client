package com.checkmarx.plugin.common.restClient;

import com.checkmarx.plugin.common.exceptions.CxRestClientException;
import com.checkmarx.plugin.common.exceptions.CxRestLoginException;
import com.checkmarx.plugin.common.exceptions.CxValidateResponseException;
import com.checkmarx.plugin.common.restClient.entities.Permissions;
import com.checkmarx.plugin.common.webBrowsing.LoginData;

public interface ICxServer {

    String getServerURL();

    LoginData login(String code) throws CxRestLoginException, CxValidateResponseException, CxRestClientException;

    LoginData getAccessTokenFromRefreshToken(String refreshToken) throws CxRestLoginException, CxValidateResponseException, CxRestClientException;

    Permissions getPermissionsFromUserInfo(String accessToken) throws CxValidateResponseException;
}
