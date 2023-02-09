package org.davincischools.leo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class ServerApplication {
    private static final Logger log = LogManager.getLogger();
    private static final String LOCAL_SERVER_PORT_PROPERTY = "local.server.port";

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServerApplication.class, args);

        log.atInfo().log("Bean names available:");
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            log.atInfo().log("  - {} ({})", beanName, bean.getClass().getName());
        }
    }
}
