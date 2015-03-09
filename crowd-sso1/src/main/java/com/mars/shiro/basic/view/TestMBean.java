package com.mars.shiro.basic.view;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Created by mars on 2015/3/5.
 */
@Slf4j
@ManagedBean
@RequestScoped
public class TestMBean {


    public void go() {
        Subject user1 = SecurityUtils.getSubject();
        log.info("Login user :" + user1.getPrincipal());
        log.info("User status is isAuthenticated = " + user1.isAuthenticated());
        log.info("User has role'admin'  = " + user1.hasRole("admin"));
        log.info("User has role'employee'  = " + user1.hasRole("employee"));
        log.info("User has role'fin'  = " + user1.hasRole("fin"));
        log.info("User has role'manager_fin'  = " + user1.hasRole("manager_fin"));
        log.info("User has role'hr'  = " + user1.hasRole("hr"));
    }
}
