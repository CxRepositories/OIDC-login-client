package com.checkmarx.plugin.common.restClient;

import com.checkmarx.plugin.common.exceptions.CxRestClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.checkmarx.plugin.common.dto.AccessTokenDTO;
import com.checkmarx.plugin.common.exceptions.CxRestLoginException;
import com.checkmarx.plugin.common.exceptions.CxValidateResponseException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import com.checkmarx.plugin.common.webBrowsing.LoginData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CxServerImpl implements ICxServer {

    private String serverURL;
    private String tokenEndpointURL;
    private HttpClient client;
    private List<Header> headers = new ArrayList<Header>();
    private String tokenEndpoint = ":8080/identity/connect/token";
    private static final String FAIL_TO_VALIDATE_TOKEN_RESPONSE_ERROR = " User authentication failed";

    public CxServerImpl(String serverURL){
        this.serverURL = serverURL;
        this.tokenEndpointURL = serverURL + tokenEndpoint;
        client = HttpClientBuilder.create().setDefaultHeaders(headers).build();
    }

    public String getServerURL() {
        return serverURL;
    }

    public LoginData login(String code) throws CxRestLoginException, CxValidateResponseException, CxRestClientException {
        HttpUriRequest postRequest;
        HttpResponse loginResponse = null;
        try {
            postRequest = RequestBuilder.post()
                    .setUri(tokenEndpointURL)
                    .setHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.toString())
                    .setEntity(TokenHTTPEntityBuilder.createGetAccessTokenFromCodeParamsEntity(code))
                    .build();
            loginResponse = client.execute(postRequest);
            validateTokenResponse(loginResponse, 200, FAIL_TO_VALIDATE_TOKEN_RESPONSE_ERROR);
            AccessTokenDTO jsonResponse = parseJsonFromResponse(loginResponse, AccessTokenDTO.class);
            Long accessTokenExpirationInMilli = getAccessTokenExpirationInMilli(jsonResponse.getExpiresIn());
            return new LoginData(jsonResponse.getAccessToken(), jsonResponse.getRefreshToken(), accessTokenExpirationInMilli, jsonResponse.getIdToken());
        } catch (IOException e) {
            throw new CxRestLoginException("Fail to login: " + e.getMessage());
        } finally {
            HttpClientUtils.closeQuietly(loginResponse);
        }
    }


    @Override
    public LoginData getAccessTokenFromRefreshToken(String refreshToken) throws CxRestClientException, CxRestLoginException, CxValidateResponseException{
        HttpUriRequest postRequest;
        HttpResponse loginResponse = null;
        try {
            postRequest = RequestBuilder.post()
                    .setUri(tokenEndpointURL)
                    .setHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.toString())
                    .setEntity(TokenHTTPEntityBuilder.createGetAccessTokenFromRefreshTokenParamsEntity(refreshToken))
                    .build();
            loginResponse = client.execute(postRequest);
            validateTokenResponse(loginResponse, 200, FAIL_TO_VALIDATE_TOKEN_RESPONSE_ERROR);
            AccessTokenDTO jsonResponse = parseJsonFromResponse(loginResponse, AccessTokenDTO.class);
            Long accessTokenExpirationInMilli = getAccessTokenExpirationInMilli(jsonResponse.getExpiresIn());
            return new LoginData(jsonResponse.getAccessToken(), jsonResponse.getRefreshToken(), accessTokenExpirationInMilli, jsonResponse.getIdToken());
        } catch (IOException e) {
            throw new CxRestLoginException("Failed to get new access token from refresh token: " + e.getMessage());
        } finally {
            HttpClientUtils.closeQuietly(loginResponse);
        }
    }

    private Long getAccessTokenExpirationInMilli(int accessTokenExpirationInSec) {
        long currentTime = System.currentTimeMillis();
        long accessTokenExpInMilli = accessTokenExpirationInSec * 1000;
        return currentTime + accessTokenExpInMilli;
    }



    private static void validateTokenResponse(HttpResponse response, int status, String message) throws CxValidateResponseException {
        try {
            if (response.getStatusLine().getStatusCode() != status) {
                String responseBody = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
                responseBody = responseBody.replace("{", "").replace("}", "").replace(System.getProperty("line.separator"), " ").replace("  ", "");
                if (responseBody.contains("<!DOCTYPE html>")) {
                    throw new CxValidateResponseException(message + ": " + "status code: 500. Error message: Internal Server Error");
                } else if (responseBody.contains("\"error\":\"invalid_grant\"")) {
                    throw new CxValidateResponseException(message);
                } else {
                    throw new CxValidateResponseException(message + ": " + "status code: " + response.getStatusLine() + ". Error message:" + responseBody);
                }
            }
        } catch (IOException e) {
            throw new CxValidateResponseException("Error parse REST response body: " + e.getMessage());
        }
    }

    private static <ResponseObj> ResponseObj parseJsonFromResponse(HttpResponse response, Class<ResponseObj> dtoClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(createStringFromResponse(response).toString(), dtoClass);
    }

    private static StringBuilder createStringFromResponse(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result;
    }
}