package com.checkmarx.plugin.common.webBrowsing;

public interface IOIDCWebBrowser {

    AuthenticationData browseAuthenticationData(String serverUrl, String clientName) throws Exception;
}
