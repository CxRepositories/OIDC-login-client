package com.checkmarx.plugin.common.webBrowsing;

public class AuthenticationData {

    public String code;
    public boolean wasCanceled;

    public AuthenticationData(String code) {
        this.code = code;
    }

    public AuthenticationData(boolean wasCanceled) {
        this.wasCanceled = wasCanceled;
    }
}