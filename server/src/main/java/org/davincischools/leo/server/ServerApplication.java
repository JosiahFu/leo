package org.davincischools.leo.server;

import static org.davincischools.leo.server.CommandLineArguments.COMMAND_LINE_ARGUMENTS;
import static org.davincischools.leo.server.SpringConstants.LOCAL_SERVER_PORT_PROPERTY;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.commons.text.StringSubstitutor;
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

  private static void loadProperties(InputStream propertiesInputStream) throws IOException {
    if (propertiesInputStream == null) {
      return;
    }
    try (InputStream in = propertiesInputStream) {
      Properties props = new Properties();
      props.load(new InputStreamReader(in, Charsets.UTF_8));
      for (String name : props.stringPropertyNames()) {
        String value = StringSubstitutor.createInterpolator().replace(props.getProperty(name, ""));
        if (System.getProperty(name) != null) {
          log.atWarn().log("System property is already set: {}", name);
        }
        System.setProperty(name, value);
      }
    }
  }

  public static void initializeSystem(String[] args) throws IOException {
    CommandLineArguments.initialize(args);

    loadProperties(
        ServerApplication.class.getClassLoader().getResourceAsStream("application.properties"));
    if (COMMAND_LINE_ARGUMENTS.additionalPropertiesFile != null) {
      File propertiesFile =
          new File(
              StringSubstitutor.createInterpolator()
                  .replace(COMMAND_LINE_ARGUMENTS.additionalPropertiesFile));
      if (!propertiesFile.exists()) {
        throw new IOException("Properties file doesn't exist: " + propertiesFile);
      }
      loadProperties(new FileInputStream(propertiesFile));
    }
    if (System.getenv(CommandLineArguments.ADDITIONAL_PROPERTIES_FILE_ENV_VAR) != null) {
      File propertiesFile =
          new File(
              StringSubstitutor.createInterpolator()
                  .replace(System.getenv(CommandLineArguments.ADDITIONAL_PROPERTIES_FILE_ENV_VAR)));
      if (!propertiesFile.exists()) {
        throw new IOException("Properties file doesn't exist: " + propertiesFile);
      }
      loadProperties(new FileInputStream(propertiesFile));
    }
  }

  public static void main(String[] args) throws IOException {
    initializeSystem(args);

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
