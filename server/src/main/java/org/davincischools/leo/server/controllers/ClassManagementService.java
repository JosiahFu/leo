package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
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
  private static final Joiner EOL_JOINER = Joiner.on("\n");

  private static final Pattern OPEN_AI_PROJECT_RESPONSE_LINE =
      Pattern.compile(
          "([Pp]roject [0-9]+[:.]\\s*)?(?<bullet>- )?([0-9]+\\.\\s*)?(([Ss]hort|[Ff]ull)"
              + " [Dd]escription:|[Tt]itle:)?\\s*[\"“]?(?<text>[^\"“]*)[\"“]?");
  private static final String TEXT_GROUP = "text";
  private static final String BULLET_GROUP = "bullet";

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
            db,
            optionalRequest.orElse(GenerateAssignmentProjectsRequest.getDefaultInstance()),
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
                              ("\"" + DataAccess.getShortDescr(knowledgeAndSkill) + "\"")));

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
                                          + " build a project that demonstrates your mastery of %s."
                                          + " You are passionate about \"%s\" and good at \"%s\".",
                                      knowledgeAndSkillsTextList,
                                      StringEscapeUtils.escapeJava(request.getSomethingYouLove()),
                                      StringEscapeUtils.escapeJava(request.getWhatYouAreGoodAt()))))
                      .addMessages(
                          OpenAiMessage.newBuilder()
                              .setRole("user")
                              .setContent(
                                  "Generate 5 projects that would fit the criteria. For each"
                                      + " project, return a title, a short description, and a full"
                                      + " description on separate lines."))
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

  private static String extractText(List<String> lines, AtomicInteger i) {
    ArrayList<String> result = new ArrayList<>();
    boolean inBulletedList = false;
    while (i.get() < lines.size()) {
      String line = lines.get(i.getAndIncrement());
      Matcher matcher = OPEN_AI_PROJECT_RESPONSE_LINE.matcher(line);
      if (!matcher.matches() || Strings.isNullOrEmpty(matcher.group(TEXT_GROUP))) {
        continue;
      }
      result.add(matcher.group(TEXT_GROUP));
      if (matcher.group(BULLET_GROUP) == null) {
        if (inBulletedList) {
          i.decrementAndGet();
          result.remove(result.size() - 1);
        }
        break;
      }
      inBulletedList = true;
    }
    return EOL_JOINER.join(result);
  }

  public static List<Project> extractProjects(
      LogOperations log, Builder response, ProjectInput projectInput, String aiResponse)
      throws IOException {
    // Parse the result into separate lines.
    List<String> lines = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new StringReader(aiResponse));
    String line;
    while ((line = reader.readLine()) != null) {
      lines.add(line);
    }
    lines = lines.stream().map(String::trim).filter(str -> !str.isEmpty()).toList();

    // Process the lines and extract project triplets.
    AtomicInteger i = new AtomicInteger(0);
    ArrayList<String> results = new ArrayList<>();
    while (i.get() < lines.size()) {
      results.add(extractText(lines, i));
    }

    if (results.size() != 15) {
      log.setStatus(Status.ERROR);
      log.addNote("Parsed out number of lines other than 15 from OpenAI's response.");
      log.addNote("The lines processed were:");
      for (String noteLine : lines) {
        Matcher m = OPEN_AI_PROJECT_RESPONSE_LINE.matcher(noteLine);
        if (m.matches()) {
          log.addNote(
              "+ [%s]  --  BULLET: [%s], TEXT: [%s]",
              noteLine, m.group(BULLET_GROUP), m.group(TEXT_GROUP));
        } else {
          log.addNote("- [%s]", noteLine);
        }
      }
    }

    List<Project> projects = new ArrayList<>();
    for (int j = 0; j < results.size() - 2; j += 3) {
      projects.add(
          new Project()
              .setCreationTime(Instant.now())
              .setProjectInput(projectInput)
              .setName(results.get(j))
              .setShortDescr(results.get(j + 1))
              .setLongDescr(results.get(j + 2)));
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
