package org.davincischools.leo.server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public final class CommandLineArguments {

  public static final CommandLineArguments COMMAND_LINE_ARGUMENTS = new CommandLineArguments();

  public static void initialize(String[] args) {
    JCommander.newBuilder()
        .addObject(COMMAND_LINE_ARGUMENTS)
        .acceptUnknownOptions(true)
        .build()
        .parse(args);
  }

  @Parameter(
      names = {"--react_port"},
      description = "Port to running React web server (that was started with 'npm start').")
  public Integer reactPort = null;

  public static final String ADDITIONAL_PROPERTIES_FILE_ENV_VAR = "ADDITIONAL_PROPERTIES_FILE";
  public static final String ADDITIONAL_PROPERTIES_FILE_FLAG = "--additional_properties_file";

  @Parameter(
      names = {ADDITIONAL_PROPERTIES_FILE_FLAG},
      description =
          "Additional Java properties to load. This may also be specified using the '"
              + ADDITIONAL_PROPERTIES_FILE_ENV_VAR
              + "' environment variable. The flag value and properties values are interpreted by "
              + "https://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/StringSubstitutor.html")
  public String additionalPropertiesFile = null;

  private CommandLineArguments() {}
}
