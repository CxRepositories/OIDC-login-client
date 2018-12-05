package com.checkmarx.plugin.common.api;

import com.checkmarx.plugin.common.CxOIDCConnector;
import com.checkmarx.plugin.common.exceptions.CxRestClientException;
import com.checkmarx.plugin.common.exceptions.CxRestLoginException;
import com.checkmarx.plugin.common.exceptions.CxValidateResponseException;
import com.checkmarx.plugin.common.restClient.CxServerImpl;
import com.checkmarx.plugin.common.restClient.ICxServer;
import com.checkmarx.plugin.common.restClient.entities.Permissions;
import com.checkmarx.plugin.common.webBrowsing.IOIDCWebBrowser;
import com.checkmarx.plugin.common.webBrowsing.LoginData;
import com.checkmarx.plugin.common.webBrowsing.OIDCWebBrowser;
import java.net.URL;

public class CxOIDCLoginClientImpl implements CxOIDCLoginClient {

    private String clientName;
    private ICxServer server;


    public CxOIDCLoginClientImpl(URL serverUrl, String clientName) {
        this.clientName = clientName;
        this.server = new CxServerImpl(serverUrl.toString());
    }


    public LoginData login() throws Exception {
        IOIDCWebBrowser webBrowser = new OIDCWebBrowser();
        CxOIDCConnector connector = new CxOIDCConnector(server, webBrowser, clientName);
        return connector.connect();
    }

    @Override
    public boolean isTokenExpired(Long expirationTime) {
        boolean isTokenExpired;
        if(expirationTime == null){
            isTokenExpired = true;
        } else {
            isTokenExpired =  (expirationTime.compareTo(System.currentTimeMillis()) < 0 ) ? true : false;
        }
        return isTokenExpired;
    }

    public LoginData getAccessTokenFromRefreshToken(String refreshToken) throws CxRestClientException, CxRestLoginException, CxValidateResponseException {
        return server.getAccessTokenFromRefreshToken(refreshToken);
    }

    public Permissions getPermissions(String accessToken) throws CxValidateResponseException {
        return server.getPermissionsFromUserInfo(accessToken);
    }
}