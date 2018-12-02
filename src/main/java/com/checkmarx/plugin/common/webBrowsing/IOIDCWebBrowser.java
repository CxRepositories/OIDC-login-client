package com.checkmarx.plugin.common.webBrowsing;

public interface IOIDCWebBrowser {

    AuthenticationData browseAuthenticationData(String restUrl, String clientName) throws Exception;
}
