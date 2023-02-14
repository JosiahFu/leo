package org.davincischools.leo.server;

import static org.davincischools.leo.server.SpringConstants.LOCAL_SERVER_PORT_PROPERTY;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ServerApplication {
  private static final Logger log = LogManager.getLogger();

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(ServerApplication.class, args);

    log.atInfo().log("Bean names available:");
    String[] beanNames = context.getBeanDefinitionNames();
    Arrays.sort(beanNames);
    for (String beanName : beanNames) {
      Object bean = context.getBean(beanName);
      log.atInfo().log("  - {} ({})", beanName, bean.getClass().getName());
    }

    int serverPort =
        context.getEnvironment().getProperty(LOCAL_SERVER_PORT_PROPERTY, Integer.class, 0);
    log.atInfo().log("Leo server started on port http://localhost:{}.", serverPort);
  }
}
