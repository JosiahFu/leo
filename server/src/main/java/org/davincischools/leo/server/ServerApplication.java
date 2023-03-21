package org.davincischools.leo.server;

import static org.davincischools.leo.server.SpringConstants.LOCAL_SERVER_PORT_PROPERTY;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication(scanBasePackages = "org.davincischools.leo")
public class ServerApplication {

  private static final Logger log = LogManager.getLogger();

  @Configuration
  static class ServerApplicationConfigurer extends WebMvcConfigurationSupport {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
      // Add the Protobuf converters to the list of converters.
      //
      // These need to be at the beginning of the list to take precedence over
      // other converters. Otherwise, a different, built-in converter fails
      // with a "Direct self-reference leading to cycle" error.
      converters.add(0, new ProtobufHttpMessageConverter());
      converters.add(1, new ProtobufJsonFormatHttpMessageConverter());
    }
  }

  public static void main(String[] args) throws IOException {
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
