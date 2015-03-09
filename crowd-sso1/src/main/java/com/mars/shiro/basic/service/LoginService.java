package com.mars.shiro.basic.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;

/**
 * Created by mars on 2015/2/16.
 */
@Slf4j
public class LoginService {

    public void authenticate(AuthenticationToken token) throws AuthenticationException {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(token);
        } catch (IncorrectCredentialsException | UnknownAccountException ie) {
            throw new AuthenticationException();
        } catch (AuthenticationException e) {
            log.warn(e.getMessage(), e);
            throw e;
        }
    }
}
