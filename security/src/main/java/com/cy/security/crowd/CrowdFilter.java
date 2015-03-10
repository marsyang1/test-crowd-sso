package com.cy.security.crowd;

import com.atlassian.crowd.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by mars on 2015/3/10.
 */
@Slf4j
public class CrowdFilter extends AuthenticatingFilter {

    // the name of the parameter service ticket in url
    private static final String TOKEN_PARAMETER = "crowd.token_key";

    // the url where the application is redirected if the CAS service ticket validation failed (example : /mycontextpatch/cas_error.jsp)
    private String failureUrl;

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = getCookie(httpRequest, TOKEN_PARAMETER);
        if (token == null) {
            token = httpRequest.getParameter(TOKEN_PARAMETER);
        }
        if (token != null) {
            return new CrowdSSOToken(token, httpRequest);
        }
        throw new InvalidTokenException("Crowd token.to_key is null");
    }

    /**
     * Execute login by creating {@link #createToken(javax.servlet.ServletRequest, javax.servlet.ServletResponse) token} and logging subject
     * with this token.
     *
     * @param request  the incoming request
     * @param response the outgoing response
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        try {
            return executeLogin(request, response);
        } catch (InvalidTokenException tokenException) {
            WebUtils.issueRedirect(request, response, failureUrl);
            return false;
        }
    }

    /**
     * Returns <code>false</code> to always force authentication (user is never considered authenticated by this filter).
     *
     * @param request     the incoming request
     * @param response    the outgoing response
     * @param mappedValue the filter-specific config value mapped to this filter in the URL rules mappings.
     * @return <code>false</code>
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    /**
     * If login has failed, redirect user to the CAS error page (no ticket or ticket validation failed) except if the user is already
     * authenticated, in which case redirect to the default success url.
     *
     * @param token    the token representing the current authentication
     * @param ae       the current authentication exception
     * @param request  the incoming request
     * @param response the outgoing response
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
                                     ServletResponse response) {
        // is user authenticated or in remember me mode ?
        Subject subject = getSubject(request, response);
        if (subject.isAuthenticated() || subject.isRemembered()) {
            try {
                issueSuccessRedirect(request, response);
            } catch (Exception e) {
                log.error("Cannot redirect to the default success url", e);
            }
        } else {
            try {
                WebUtils.issueRedirect(request, response, failureUrl);
            } catch (IOException e) {
                log.error("Cannot redirect to failure url : {}", failureUrl, e);
            }
        }
        return false;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    private String getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
