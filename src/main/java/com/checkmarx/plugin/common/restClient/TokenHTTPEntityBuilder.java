package com.checkmarx.plugin.common.restClient;

import com.checkmarx.plugin.common.constants.AccessTokenConsts;
import com.checkmarx.plugin.common.exceptions.CxRestClientException;
import com.checkmarx.plugin.common.exceptions.CxRestLoginException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TokenHTTPEntityBuilder {

    private static final String ERROR_MESSAGE_PREFIX = "Failed to create body entity, due to: ";

    public static StringEntity createGetAccessTokenFromCodeParamsEntity(String code) throws CxRestClientException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair(AccessTokenConsts.GRANT_TYPE_KEY, AccessTokenConsts.AUTHORIZATION_CODE_GRANT_TYPE));
        urlParameters.add(new BasicNameValuePair(AccessTokenConsts.CLIENT_ID_KEY, AccessTokenConsts.CLIENT_VALUE));
        urlParameters.add(new BasicNameValuePair(AccessTokenConsts.REDIRECT_URI_KEY, "http://10.31.1.67:8080"));
        urlParameters.add(new BasicNameValuePair(AccessTokenConsts.CODE_KEY, code));

        try {
            return new UrlEncodedFormEntity(urlParameters, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new CxRestClientException(ERROR_MESSAGE_PREFIX + e.getMessage());
        }
    }

    public static StringEntity createGetAccessTokenFromRefreshTokenParamsEntity(String refreshToken) throws CxRestClientException {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair(AccessTokenConsts.GRANT_TYPE_KEY, AccessTokenConsts.REFRESH_TOKEN));
        urlParameters.add(new BasicNameValuePair(AccessTokenConsts.CLIENT_ID_KEY, AccessTokenConsts.CLIENT_VALUE));
        urlParameters.add(new BasicNameValuePair(AccessTokenConsts.REFRESH_TOKEN, refreshToken));

        try {
            return new UrlEncodedFormEntity(urlParameters, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new CxRestClientException(ERROR_MESSAGE_PREFIX + e.getMessage());
        }
    }
}