package com.cy.security.crowd;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by mars on 2015/3/6.
 */
public class CrowdAuthenticationException extends AuthenticationException {

    public CrowdAuthenticationException() {
        super();
    }

    public CrowdAuthenticationException(String message) {
        super(message);
    }

    public CrowdAuthenticationException(Throwable cause) {
        super(cause);
    }

    public CrowdAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
