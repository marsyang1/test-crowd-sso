package com.mars.shiro.basic.service;

import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticatorImpl;
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelper;
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelperImpl;
import com.atlassian.crowd.integration.http.util.CrowdHttpValidationFactorExtractorImpl;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.service.client.ClientProperties;
import com.atlassian.crowd.service.client.ClientPropertiesImpl;
import com.atlassian.crowd.service.client.ClientResourceLocator;
import com.atlassian.crowd.service.client.CrowdClient;
import com.atlassian.crowd.service.factory.CrowdClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mars on 2015/2/16.
 */
@Slf4j
@Service
public class LogoutService {

    @Autowired
    private CrowdHttpAuthenticator authenticator;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            String userId = currentUser.getPrincipal().toString();
            currentUser.logout();
            ensureAuthenticator();
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

    private void ensureAuthenticator() {
        if (authenticator == null) {
            log.debug("Enter create crowdHttpClient");
            ClientResourceLocator clientResourceLocator = new ClientResourceLocator("crowd.properties");
            ClientProperties props = ClientPropertiesImpl.newInstanceFromResourceLocator(clientResourceLocator);
            CrowdClientFactory clientFactory = new RestCrowdClientFactory();
            CrowdClient client = clientFactory.newInstance(props);
            CrowdHttpTokenHelper tokenHelper = CrowdHttpTokenHelperImpl
                    .getInstance(CrowdHttpValidationFactorExtractorImpl.getInstance());
            authenticator = new CrowdHttpAuthenticatorImpl(client, props, tokenHelper);
        }
    }
}
