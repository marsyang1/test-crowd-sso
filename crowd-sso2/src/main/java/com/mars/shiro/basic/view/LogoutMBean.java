package com.mars.shiro.basic.view;

import com.mars.shiro.basic.service.LogoutService;
import lombok.extern.slf4j.Slf4j;
import org.omnifaces.util.Faces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mars on 2015/2/16.
 */
@Slf4j
@ManagedBean
@RequestScoped
public class LogoutMBean {

    private LogoutService logoutService = new LogoutService();
    private static final String PORTAL_CONTEXT = "/shiro-basic";

    public void logout() {
        try {
            HttpServletRequest request = Faces.getRequest();
            HttpServletResponse response = Faces.getResponse();
            logoutService.logout(request, response);
            Faces.redirect(Faces.getRequestContextPath() + "/index.xhtml");
        } catch (IOException e) {
            log.error("redirect fail ", e);
        }
    }

}
