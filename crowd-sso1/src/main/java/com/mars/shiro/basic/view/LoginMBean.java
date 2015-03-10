/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mars.shiro.basic.view;

import com.cy.security.crowd.CrowdUserToken;
import com.mars.shiro.basic.service.LoginService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.IOException;

/**
 * @author mars
 */
@Slf4j
@ManagedBean
@RequestScoped
public class LoginMBean {

    private LoginService loginService = new LoginService();

    @Getter
    @Setter
    private String userId;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private boolean rememberMe = false;

    /**
     * Creates a new instance of LoginMBean
     */
    public LoginMBean() {
    }

    public String authenticate() {
        CrowdUserToken token = new CrowdUserToken(Faces.getRequest(), Faces.getResponse(), userId, password, rememberMe);
        try {
            loginService.authenticate(token);
            Subject currentUser = SecurityUtils.getSubject();
            log.info("User :" + currentUser.getPrincipal() + "has login");
            Faces.redirect(Faces.getRequestContextPath() + "/system/secret.xhtml");
        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
            Messages.addFlashGlobalError("登入失敗 ,您輸入的帳號或密碼有誤。");
        } catch (IOException e) {
            Messages.addFlashGlobalError("導向有誤");
        }
        return "";
    }

}
