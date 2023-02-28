package org.davincischools.leo.server;

import static org.davincischools.leo.server.SpringConstants.LOCAL_SERVER_PORT_PROPERTY;

import com.google.common.base.Charsets;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
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

@SpringBootApplication
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
    CommandLineArguments.initialize(args);

    // Read and set java properties from resources/java.properties.
    try (InputStream in =
        ServerApplication.class.getClassLoader().getResourceAsStream("java.properties")) {
      if (in != null) {
        Properties props = new Properties();
        props.load(new InputStreamReader(in, Charsets.UTF_8));
        for (String name : props.stringPropertyNames()) {
          String value = props.getProperty(name, "");
          // If the property was already set, then we want to warn that it's value may be
          // unexpected.
          if (System.getProperty(name) != null) {
            log.atWarn()
                .log(
                    "System property is already set. Check resources/java.properties: Name: {},"
                        + " System Value: {}, Properties File Value: {}",
                    name,
                    System.getProperty(name),
                    value);
          }
          System.setProperty(name, value);
        }
      }
    }

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
