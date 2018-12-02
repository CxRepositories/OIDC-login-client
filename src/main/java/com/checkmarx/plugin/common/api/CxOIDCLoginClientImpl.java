package com.checkmarx.plugin.common.api;

import com.checkmarx.plugin.common.CxOIDCConnector;
import com.checkmarx.plugin.common.restClient.CxServerImpl;
import com.checkmarx.plugin.common.restClient.ICxServer;
import com.checkmarx.plugin.common.webBrowsing.IOIDCWebBrowser;
import com.checkmarx.plugin.common.webBrowsing.LoginData;
import com.checkmarx.plugin.common.webBrowsing.OIDCWebBrowser;
import java.net.URL;

public class CxOIDCLoginClientImpl implements CxOIDCLoginClient {

    private URL serverUrl;
    private String clientName;
    private LoginData loginData = null;
    private ICxServer server;


    public CxOIDCLoginClientImpl(URL serverUrl, String clientName) {
        this.serverUrl = serverUrl;
        this.clientName = clientName;
        this.server = new CxServerImpl(serverUrl.toString());
    }


    public LoginData login() throws Exception {
        IOIDCWebBrowser webBrowser = new OIDCWebBrowser();
        CxOIDCConnector connector = new CxOIDCConnector(server, webBrowser, clientName);
        loginData = connector.connect();
        return loginData;
    }

    @Override
    public boolean isTokenExpired() {
        boolean isTokenExpired = false;
        if(loginData == null){
            isTokenExpired = false;
        } else if(loginData.getAccessTokenExpirationInMillis() != null){
            isTokenExpired =  (loginData.getAccessTokenExpirationInMillis().compareTo(System.currentTimeMillis()) < 0 ) ? true : false;
        }
        return isTokenExpired;
    }

    public LoginData getAccessTokenFromRefreshToken(String refreshToken) throws Exception{
        return server.getAccessTokenFromRefreshToken(refreshToken);
    }
}