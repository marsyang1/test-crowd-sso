package com.mars.shiro.basic.service;

/**
 * Created by mars on 2015/3/2.
 */

import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdHttpAuthenticationFactory;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.service.client.CrowdClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.omnifaces.util.Faces;

import javax.servlet.http.HttpSession;

@Slf4j
public class BaseAction {

    protected User remoteUser;
    protected Boolean authenticated = null;

    protected CrowdClient crowdClient;

    protected static final String CONTEXT_ID_PREFIX = "CROWD_ID_";


    @Getter
    @Setter
    protected CrowdHttpAuthenticator crowdHttpAuthenticator = RestCrowdHttpAuthenticationFactory
            .getAuthenticator();

    protected static final String SUCCESS = "welcomePrimefaces.xhtml";
    protected static final String NONE = "index.xhtml";
    protected static final String INPUT = "login.xhtml";

    public String getDisplayName() throws InvalidAuthenticationException {
        if (!isAuthenticated())
            return null;
        String displayName = "";
        User user = getRemoteUser();
        if (user != null) {
            displayName = user.getDisplayName();
        }
        return displayName;
    }

    public User getRemoteUser() throws InvalidAuthenticationException {
        if (!isAuthenticated())
            return null;
        if (remoteUser == null) {
            try {
                // find the user from the authenticated token key.
                remoteUser = crowdHttpAuthenticator.getUser(Faces.getRequest());
            } catch (Exception e) {
                log.info(e.getMessage(), e);
                throw new InvalidAuthenticationException("", e);
            }
        }
        return remoteUser;
    }

    /**
     * Checks if a user is currently authenticated verses the Crowd server.
     *
     * @return <code>true</code> if and only if the user is currently
     * authenticated, otherwise <code>false</code>.
     */
    public boolean isAuthenticated() {
        if (authenticated == null) {
            try {
                authenticated = crowdHttpAuthenticator.isAuthenticated(
                        Faces.getRequest(), Faces.getResponse());
            } catch (Exception e) {
                log.info(e.getMessage(), e);
                authenticated = Boolean.FALSE;
            }
        }
        return authenticated;
    }

    protected HttpSession getSession() {
        return Faces.getRequest().getSession();
    }

}
