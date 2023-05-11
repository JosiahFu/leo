package org.davincischools.leo.server;

import static org.davincischools.leo.server.SpringConstants.LOCAL_SERVER_PORT_PROPERTY;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.post_environment_processors.LoadCustomProjectLeoProperties;
import org.davincischools.leo.database.test.TestDatabase;
import org.davincischools.leo.database.utils.Database;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication(
    scanBasePackages = "org.davincischools.leo.server",
    scanBasePackageClasses = {Database.class, UserX.class, TestDatabase.class})
public class ServerApplication {

  private static final Logger logger = LogManager.getLogger();

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
    // Load custom Project Leo properties into the environment.
    ConfigurableEnvironment environment = new StandardEnvironment();
    LoadCustomProjectLeoProperties.loadCustomProjectLeoProperties(environment);

    // Start the Project Leo server.
    SpringApplication sa = new SpringApplication(ServerApplication.class);
    sa.setEnvironment(environment);
    ApplicationContext context = sa.run(args);

    // Dump the list of beans in the context.
    logger.atInfo().log("Bean names available:");
    String[] beanNames = context.getBeanDefinitionNames();
    Arrays.sort(beanNames);
    for (String beanName : beanNames) {
      Object bean = context.getBean(beanName);
      logger.atInfo().log("  - {} ({})", beanName, bean.getClass().getName());
    }

    // Log the port the server is running on.
    int serverPort =
        context.getEnvironment().getProperty(LOCAL_SERVER_PORT_PROPERTY, Integer.class, 0);
    logger.atInfo().log("Leo server started on port http://localhost:{}.", serverPort);
  }
}
