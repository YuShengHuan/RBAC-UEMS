package com.ru.app.common.config;

import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;


public class CustomSpringConfigurator extends ServerEndpointConfig.Configurator {
    private final ApplicationContext context;
    public CustomSpringConfigurator(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) {
        return context.getBean(clazz);
    }
}