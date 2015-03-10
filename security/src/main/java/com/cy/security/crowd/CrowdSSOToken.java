package com.cy.security.crowd;

import org.apache.shiro.authc.RememberMeAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mars on 2015/3/10.
 */
public class CrowdSSOToken implements RememberMeAuthenticationToken {

    // the service ticket returned by the CAS server
    private String ticket = null;

    // the user identifier
    private String userId = null;

    // is the user in a remember me mode ?
    private boolean isRememberMe = false;

    private HttpServletRequest request;

    private HttpServletResponse response;

    public CrowdSSOToken(String ticket, HttpServletRequest request) {
        this.ticket = ticket;
        this.request = request;
    }

    public Object getPrincipal() {
        return userId;
    }

    public Object getCredentials() {
        return ticket;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRememberMe() {
        return isRememberMe;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setRememberMe(boolean isRememberMe) {
        this.isRememberMe = isRememberMe;
    }
}
