/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mars.shiro.basic.view;

import org.apache.shiro.SecurityUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @author mars
 */
@ManagedBean
@RequestScoped
public class SubjectMBean {

    /**
     * Creates a new instance of SubjectMBean
     */
    public SubjectMBean() {
    }

    public String getId() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return SecurityUtils.getSubject().getPrincipal().toString();
        }
        return "";
    }

}
