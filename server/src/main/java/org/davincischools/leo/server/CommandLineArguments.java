package org.davincischools.leo.server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public final class CommandLineArguments {

  public static final CommandLineArguments COMMAND_LINE_ARGUMENTS = new CommandLineArguments();

  public static void initialize(String[] args) {
    JCommander.newBuilder().addObject(COMMAND_LINE_ARGUMENTS).build().parse(args);
  }

  @Parameter(
      names = {"--react_port"},
      description = "Port to running React web server (that was started with 'npm start').")
  protected Integer reactPort = null;

  private CommandLineArguments() {}
}
