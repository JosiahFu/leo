package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.StringEscapeUtils;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectInput;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.class_management.GenerateAssignmentProjectsRequest;
import org.davincischools.leo.protos.class_management.GenerateAssignmentProjectsResponse;
import org.davincischools.leo.protos.class_management.GenerateAssignmentProjectsResponse.Builder;
import org.davincischools.leo.protos.class_management.GetProjectsRequest;
import org.davincischools.leo.protos.class_management.GetProjectsResponse;
import org.davincischools.leo.protos.class_management.GetStudentAssignmentsRequest;
import org.davincischools.leo.protos.class_management.GetStudentAssignmentsResponse;
import org.davincischools.leo.protos.open_ai.OpenAiMessage;
import org.davincischools.leo.protos.open_ai.OpenAiRequest;
import org.davincischools.leo.protos.open_ai.OpenAiResponse;
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.LogUtils;
import org.davincischools.leo.server.utils.LogUtils.LogExecutionError;
import org.davincischools.leo.server.utils.LogUtils.LogOperations;
import org.davincischools.leo.server.utils.LogUtils.Status;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClassManagementService {

  private static final Joiner COMMA_AND_JOINER = Joiner.on(", and ");

  private static final Pattern REMOVE_FLUFF =
      Pattern.compile(
          // Match newlines with the '.' character.
          "(?s)"
              // Match the start of the line.
              + "^"
              // A number, e.g., "1: " or "1. "
              + "([0-9]+[:.])?\\s*"
              // A label, e.g., "Title - " or "Full Description: "
              + "(([Pp]roject\\s*[0-9]*|[Tt]itle|[Ss]hort|[Ff]ull|[Ss]ummary)?\\s*"
              + "([Dd]escription)?)?\\s*"
              // The end of the label, e.g., the ":" or " - " from above.
              + "(\\.|:| -)?\\s*" //
              // A second number for, e.g., "Project: 1. "
              + "([0-9]+[:.])?\\s*" //
              // A title after the label, e.g., "Project 1 - Title: "
              + "(Title:)?\\s*" //
              // The main text, either quoted or not. Quotes are removed.
              + "([\"“”](?<quotedText>.*)[\"“”]|(?<unquotedText>.*))"
              // Match the end of the line.
              + "$");
  private static final ImmutableList<String> TEXT_GROUPS =
      ImmutableList.of("quotedText", "unquotedText");

  // Do NOT have any special Regex characters in these delimiters.
  static final String START_OF_PROJECT = "~~prj:ProjectLeoDelimiter~~";
  static final String END_OF_TITLE = "~~title:ProjectLeoDelimiter~~";
  static final String END_OF_SHORT = "~~short:ProjectLeoDelimiter~~";

  @Autowired Database db;
  @Autowired OpenAiUtils openAiUtils;

  @PostMapping(value = "/api/protos/ClassManagementService/GetStudentAssignments")
  @ResponseBody
  public GetStudentAssignmentsResponse getStudentAssignments(
      @RequestBody Optional<GetStudentAssignmentsRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            optionalRequest.orElse(GetStudentAssignmentsRequest.getDefaultInstance()),
            (request, log) -> {
              checkArgument(request.hasUserXId());
              UserX user = db.getUserXRepository().findById(request.getUserXId()).orElseThrow();
              var response = GetStudentAssignmentsResponse.newBuilder();

              for (Assignment assignment :
                  db.getAssignmentRepository().findAllByStudentId(user.getStudent().getId())) {
                response.addAssignments(
                    DataAccess.convertAssignmentToProto(assignment.getClassX(), assignment));
              }

              return response.build();
            })
        .finish();
  }

  @PostMapping(value = "/api/protos/ClassManagementService/GenerateAssignmentProjects")
  @ResponseBody
  public GenerateAssignmentProjectsResponse generateAssignmentProjects(
      @RequestBody Optional<GenerateAssignmentProjectsRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db, optionalRequest.orElse(GenerateAssignmentProjectsRequest.getDefaultInstance()))
        .retryNextStep(2, 1000)
        .andThen(
            (request, log) -> {
              checkArgument(request.hasUserXId());
              var response = GenerateAssignmentProjectsResponse.newBuilder();

              UserX user = db.getUserXRepository().findById(request.getUserXId()).orElseThrow();

              // Save the Project input settings.
              ProjectInput projectInput =
                  db.getProjectInputRepository()
                      .save(
                          new ProjectInput()
                              .setCreationTime(Instant.now())
                              .setStudent(user.getStudent())
                              .setAssignment(new Assignment().setId(request.getAssignmentId()))
                              .setSomethingYouLove(request.getSomethingYouLove())
                              .setWhatYouAreGoodAt(request.getWhatYouAreGoodAt()));
              log.addProjectInput(projectInput);

              // Get knowledge and skill contribution.
              String knowledgeAndSkillsTextList =
                  COMMA_AND_JOINER.join(
                      Iterables.transform(
                          db.getKnowledgeAndSkillRepository()
                              .findAllByAssignmentId(request.getAssignmentId()),
                          knowledgeAndSkill ->
                              "\""
                                  + StringEscapeUtils.escapeJava(
                                      DataAccess.getShortDescr(knowledgeAndSkill))
                                  + "\""));

              // Query OpenAI for projects.
              // TODO: Is there an asynchronous way to do this?
              OpenAiRequest aiRequest =
                  OpenAiRequest.newBuilder()
                      .setModel(OpenAiUtils.GPT_3_5_TURBO_MODEL)
                      .addMessages(
                          OpenAiMessage.newBuilder()
                              .setRole("system")
                              .setContent(
                                  String.format(
                                      "You are a senior student who wants to spend 60 hours to"
                                          + " build a project that demonstrates your mastery of"
                                          + " %s. You are passionate about \"%s\" and good at"
                                          + " \"%s\".",
                                      knowledgeAndSkillsTextList,
                                      StringEscapeUtils.escapeJava(request.getSomethingYouLove()),
                                      StringEscapeUtils.escapeJava(request.getWhatYouAreGoodAt()))))
                      .addMessages(
                          OpenAiMessage.newBuilder()
                              .setRole("user")
                              .setContent(
                                  String.format(
                                      // Notes:
                                      //
                                      // "then a declarative summary of the project in one
                                      // sentence" caused the short description to be skipped.
                                      //
                                      // Ending the block with the project delimiter caused it
                                      // to be included prematurely, before the full description
                                      // was finished generating.
                                      "Generate 5 projects that would fit the criteria. For each"
                                          + " project, return: 1) the text \"%s\", 2) then a"
                                          + " title, 3) then the text \"%s\", 4) then a short"
                                          + " declarative command statement that summarizes the"
                                          + " project, 5) then the text \"%s\", 6) then a detailed"
                                          + " description of the project followed by major steps"
                                          + " to complete it. Do not return any text before the"
                                          + " first project and do not format the output.",
                                      StringEscapeUtils.escapeJava(START_OF_PROJECT),
                                      StringEscapeUtils.escapeJava(END_OF_TITLE),
                                      StringEscapeUtils.escapeJava(END_OF_SHORT))))
                      .build();

              OpenAiResponse aiResponse =
                  openAiUtils
                      .sendOpenAiRequest(
                          aiRequest, OpenAiResponse.newBuilder(), Optional.of(user.getId()))
                      .build();

              List<Project> projects =
                  extractProjects(
                      log,
                      response,
                      projectInput,
                      aiResponse.getChoices(0).getMessage().getContent());
              db.getProjectRepository().saveAll(projects);
              projects.forEach(log::addProject);

              return response.build();
            })
        .finish();
  }

  static String normalizeAndCheckString(String input) {
    input = input.trim().replaceAll("\\n\\n", "\n");
    Matcher fluffMatcher = REMOVE_FLUFF.matcher(input);
    if (fluffMatcher.matches()) {
      for (String groupName : TEXT_GROUPS) {
        String groupText = fluffMatcher.group(groupName);
        if (groupText != null) {
          input = groupText;
          break;
        }
      }
    }
    return input.trim().replaceAll("\\n\\n", "\n");
  }

  static List<Project> extractProjects(
      LogOperations log, Builder response, ProjectInput projectInput, String aiResponse) {
    List<Project> projects = new ArrayList<>();
    for (String projectText : aiResponse.split(START_OF_PROJECT)) {
      try {
        if (normalizeAndCheckString(projectText).isEmpty()) {
          continue;
        }
        String[] pieces_of_information =
            projectText.trim().split(END_OF_TITLE + "|" + END_OF_SHORT);
        checkArgument(pieces_of_information.length == 3);

        projects.add(
            new Project()
                .setCreationTime(Instant.now())
                .setProjectInput(projectInput)
                .setName(normalizeAndCheckString(pieces_of_information[0]))
                .setShortDescr(normalizeAndCheckString(pieces_of_information[1]))
                .setLongDescr(normalizeAndCheckString(pieces_of_information[2])));
        response.addProjects(DataAccess.convertProjectToProto(projects.get(projects.size() - 1)));
      } catch (Throwable e) {
        log.setStatus(Status.ERROR);
        log.addNote("Could not parse project text because \"%s\": %s", e.getMessage(), projectText);
      }
    }
    return projects;
  }

  @PostMapping(value = "/api/protos/ClassManagementService/GetProjects")
  @ResponseBody
  public GetProjectsResponse getProjects(@RequestBody Optional<GetProjectsRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            optionalRequest.orElse(GetProjectsRequest.getDefaultInstance()),
            (request, log) -> {
              UserX userX = db.getUserXRepository().findById(request.getUserXId()).orElseThrow();
              var response = GetProjectsResponse.newBuilder();

              response.addAllProjects(DataAccess.getProtoProjectsByUserXId(db, userX));

              return response.build();
            })
        .finish();
  }
}
