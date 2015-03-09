package com.mars.shiro.basic.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Created by mars on 2015/2/16.
 */
@Slf4j
public class LogoutService {

    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        String userId = currentUser.getPrincipal().toString();
        currentUser.logout();
        log.info("User " + userId + "has logout");
    }
}
