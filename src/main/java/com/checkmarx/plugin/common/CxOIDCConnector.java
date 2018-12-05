package com.checkmarx.plugin.common;


import com.checkmarx.plugin.common.constants.Consts;
import com.checkmarx.plugin.common.restClient.ICxServer;
import com.checkmarx.plugin.common.webBrowsing.AuthenticationData;
import com.checkmarx.plugin.common.webBrowsing.IOIDCWebBrowser;
import com.checkmarx.plugin.common.webBrowsing.LoginData;

public class CxOIDCConnector {


    ICxServer cxServer;
    String clientName;
    IOIDCWebBrowser webBrowser;

    public CxOIDCConnector(ICxServer cxServer, IOIDCWebBrowser webBrowser, String clientName) {
        this.cxServer = cxServer;
        this.webBrowser = webBrowser;
        this.clientName = clientName;
    }

    public LoginData connect() throws Exception {
        AuthenticationData authenticationData = webBrowser.browseAuthenticationData(cxServer.getServerURL() + Consts.PORT + Consts.AUTHORIZATION_ENDPOINT, clientName);

        if (authenticationData.wasCanceled) {
            return new LoginData(true);
        }

        LoginData loginData = cxServer.login(authenticationData.code);
        return loginData;
    }
}