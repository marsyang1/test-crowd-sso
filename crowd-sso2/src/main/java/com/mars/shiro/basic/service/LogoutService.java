package com.mars.shiro.basic.service;

import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mars on 2015/2/16.
 */
@Slf4j
public class LogoutService {

    @Inject
    private CrowdHttpAuthenticator authenticator;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            String userId = currentUser.getPrincipal().toString();
            currentUser.logout();
            authenticator.logout(request, response);
            request.getSession().invalidate();
            removeAllCookies(request, response);
            log.info("User " + userId + "has logout");
        } catch (ApplicationPermissionException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidAuthenticationException e) {
            log.error(e.getMessage(), e);
        } catch (OperationFailedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void removeAllCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setValue("");
                response.addCookie(cookie);
            }
        }
    }
}
