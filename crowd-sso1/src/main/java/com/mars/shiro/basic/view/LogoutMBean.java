package com.mars.shiro.basic.view;

import com.mars.shiro.basic.service.LogoutService;
import lombok.extern.slf4j.Slf4j;
import org.omnifaces.util.Faces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.IOException;

/**
 * Created by mars on 2015/2/16.
 */
@Slf4j
@ManagedBean
@RequestScoped
public class LogoutMBean {

    private LogoutService logoutService = new LogoutService();

    public void logout() {
        try {
            logoutService.logout(Faces.getRequest(), Faces.getResponse());
            Faces.redirect(Faces.getRequestContextPath() + "/index.xhtml");
        } catch (IOException e) {
            log.error("redirect fail ", e);
        }
    }

}
