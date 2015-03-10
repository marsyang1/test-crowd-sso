package com.cy.security.crowd;

import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.InvalidTokenException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticatorImpl;
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelper;
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelperImpl;
import com.atlassian.crowd.integration.http.util.CrowdHttpValidationFactorExtractorImpl;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.model.authentication.ValidationFactor;
import com.atlassian.crowd.service.client.ClientProperties;
import com.atlassian.crowd.service.client.ClientPropertiesImpl;
import com.atlassian.crowd.service.client.ClientResourceLocator;
import com.atlassian.crowd.service.client.CrowdClient;
import com.atlassian.crowd.service.factory.CrowdClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by mars on 2015/3/6.
 */
@Slf4j
public class CrowdSSORealm extends AuthorizingRealm {

    @Autowired
    private CrowdHttpAuthenticator crowdHttpClient;
    @Autowired
    private CrowdClient crowdClient;

    public CrowdSSORealm() {
        super();
        setAuthenticationTokenClass(CrowdSSOToken.class);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        ensureAuthenticator();
        CrowdSSOToken token = (CrowdSSOToken) authenticationToken;
        String ticket = token.getCredentials().toString();
        try {
            crowdClient.validateSSOAuthentication(ticket, getValidationFactors(token.getRequest()));
            String userId = crowdClient.findUserFromSSOToken(ticket).getName();
            return new SimpleAuthenticationInfo(userId, ticket, getName());
        } catch (InvalidAuthenticationException iae) {
            throw new IncorrectCredentialsException("Unable to authenticate principal " + ",Crowd token is " + ticket + " in Crowd.", iae);
        } catch (InvalidTokenException e) {
            throw new AuthenticationException("InvalidTokenException ", e);
        } catch (OperationFailedException e) {
            throw new AuthenticationException(" OperationFailedException ", e);
        } catch (ApplicationPermissionException e) {
            throw new AuthenticationException(" ApplicationPermissionException ", e);
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        final String userId = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        addRole(userId, info);
        return info;
    }

    private void addRole(String userId, SimpleAuthorizationInfo info) {
        switch (userId) {
            case "admin":
                addRoleAndPermission("admin", info);
                break;
            case "pixie":
                addRoleAndPermission("employee", info);
                addRoleAndPermission("hr", info);
                break;
            case "lulu":
                addRoleAndPermission("employee", info);
                addRoleAndPermission("fin", info);
                addRoleAndPermission("manager", info);
                addRoleAndPermission("manager_fin", info);
                break;
            case "connie":
                addRoleAndPermission("employee", info);
                addRoleAndPermission("fin", info);
                break;
        }
    }

    private void addRoleAndPermission(String roleName, SimpleAuthorizationInfo info) {
        switch (roleName) {
            case "admin":
                info.addRole("admin");
                info.addStringPermission("*");
                break;
            case "employee":
                info.addRole("employee");
                info.addStringPermission("employee:*");
                break;
            case "hr":
                info.addRole("hr");
                info.addStringPermission("hr:*");
                break;
            case "fin":
                info.addRole("fin");
                info.addStringPermission("fin:gl:create");
                info.addStringPermission("fin:gl:update");
                break;
            case "manager_fin":
                info.addRole("manager_fin");
                info.addStringPermission("fin:manager:*");
                info.addStringPermission("fin:gl:*");
                break;
            case "manager":
                info.addRole("manager");
                info.addStringPermission("manager:*");
                break;
        }

    }

    private void ensureAuthenticator() {
        if (crowdHttpClient == null) {
            log.debug("Enter create crowdHttpClient");
            ClientResourceLocator clientResourceLocator = new ClientResourceLocator("crowd.properties");
            ClientProperties props = ClientPropertiesImpl.newInstanceFromResourceLocator(clientResourceLocator);
            CrowdClientFactory clientFactory = new RestCrowdClientFactory();
            crowdClient = clientFactory.newInstance(props);
            CrowdHttpTokenHelper tokenHelper = CrowdHttpTokenHelperImpl
                    .getInstance(CrowdHttpValidationFactorExtractorImpl.getInstance());
            crowdHttpClient = new CrowdHttpAuthenticatorImpl(crowdClient, props, tokenHelper);
        }
    }

    private List<ValidationFactor> getValidationFactors(HttpServletRequest request) {
        return CrowdHttpValidationFactorExtractorImpl.getInstance().getValidationFactors(request);
    }

}
