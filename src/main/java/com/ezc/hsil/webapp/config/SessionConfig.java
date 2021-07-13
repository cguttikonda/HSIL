package com.ezc.hsil.webapp.config;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
class SessionConfig implements HttpSessionListener { 

    @Value("${session.timeout}")
    private Integer sessionTimeout;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
    	log.debug("sessionCreated:::"+sessionTimeout);
        event.getSession().setMaxInactiveInterval(sessionTimeout);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {}

}