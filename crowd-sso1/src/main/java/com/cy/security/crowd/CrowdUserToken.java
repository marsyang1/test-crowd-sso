package com.cy.security.crowd;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mars on 2015/3/5.
 */
@Slf4j
public class CrowdUserToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private String userId;
    private char[] password;
    private boolean rememberMe = false;
    private String host;

    public CrowdUserToken(final HttpServletRequest request, final HttpServletResponse response, final String userId, final char[] password) {
        this(request, response, userId, password, false, null);
    }

    public CrowdUserToken(final HttpServletRequest request, final HttpServletResponse response, final String userId, final String password) {
        this(request, response, userId, password != null ? password.toCharArray() : null, false, null);
    }

    public CrowdUserToken(final HttpServletRequest request, final HttpServletResponse response, final String userId, final char[] password, final String host) {
        this(request, response, userId, password, false, host);
    }


    public CrowdUserToken(final HttpServletRequest request, final HttpServletResponse response, final String userId, final String password, final String host) {
        this(request, response, userId, password != null ? password.toCharArray() : null, false, host);
    }

    public CrowdUserToken(final HttpServletRequest request, final HttpServletResponse response, final String userId, final char[] password, final boolean rememberMe) {
        this(request, response, userId, password, rememberMe, null);
    }

    public CrowdUserToken(final HttpServletRequest request, final HttpServletResponse response, final String userId, final String password, final boolean rememberMe) {
        this(request, response, userId, password != null ? password.toCharArray() : null, rememberMe, null);
    }

    public CrowdUserToken(final HttpServletRequest request, final HttpServletResponse response, final String userId, final char[] password,
                          final boolean rememberMe, final String host) {
        this.request = request;
        this.response = response;
        this.userId = userId;
        this.password = password;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    public CrowdUserToken(final HttpServletRequest request, final HttpServletResponse response, final String userId, final String password,
                          final boolean rememberMe, final String host) {
        this(request, response, userId, password != null ? password.toCharArray() : null, rememberMe, host);
    }

    public Object getPrincipal() {
        return userId;
    }

    public Object getCredentials() {
        return password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public String getUserId() {
        return userId;
    }

    public char[] getPassword() {
        return password;
    }

    @Override
    public String getHost() {
        return host;
    }
}
