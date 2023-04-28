package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.StringEscapeUtils;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.IkigaiInput;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.Database.StudentRepository.StudentAssignment;
import org.davincischools.leo.database.utils.QuillInitializer;
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
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClassManagementService {

  private static final Joiner COMMA_AND_JOINER = Joiner.on(", and ");

  private static final Pattern AI_PROJECT_LABEL = Pattern.compile("([0-9]+[.])?[^:]{0,18}[:]");
  private static final Pattern AI_REMOVE_LABEL_AND_QUOTES =
      Pattern.compile("([0-9]+[.])?([^:.]{0,18}[:.])?\\s*[\"“]?(?<text>[^\"“]+)[\"“]?|.*");

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
              var response = GetStudentAssignmentsResponse.newBuilder();

              for (StudentAssignment assignment :
                  db.getStudentRepository()
                      .findAllAssignmentsByStudentUserXId(request.getUserXId())) {
                response.addAssignments(
                    DataAccess.convertAssignmentToProto(
                        assignment.classX(), assignment.assignment()));
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

              UserX user =
                  db.getUserXRepository()
                      .findFullUserXByUserXId(request.getUserXId())
                      .orElseThrow();

              // Save the Ikigai settings.
              IkigaiInput ikigaiInput =
                  db.getIkigaiInputRepository()
                      .save(
                          new IkigaiInput()
                              .setCreationTime(Instant.now())
                              .setUserX(user)
                              .setAssignment(new Assignment().setId(request.getAssignmentId()))
                              .setSomethingYouLove(request.getSomethingYouLove())
                              .setWhatYouAreGoodAt(request.getWhatYouAreGoodAt()));
              log.addIkigaiInput(ikigaiInput);

              // Get knowledge and skill contribution.
              List<KnowledgeAndSkill> knowledgeAndSkills =
                  db.getAssignmentRepository()
                      .findAllKnowledgeAndSkillsById(request.getAssignmentId());
              String knowledgeAndSkillsTextList =
                  COMMA_AND_JOINER.join(
                      Lists.transform(
                          knowledgeAndSkills,
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
                      ikigaiInput,
                      aiResponse.getChoices(0).getMessage().getContent());
              db.getProjectRepository().saveAll(projects);
              projects.forEach(log::addProject);

              return response.build();
            })
        .finish();
  }

  public static List<Project> extractProjects(
      LogOperations log, Builder response, IkigaiInput ikigaiInput, String aiResponse)
      throws IOException {
    List<Project> projects = new ArrayList<>();

    // Parse the result into separate lines.
    List<String> lines = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new StringReader(aiResponse));
    String line;
    while ((line = reader.readLine()) != null) {
      lines.add(line);
    }

    // Process the lines and extract projects.
    lines =
        lines.stream()
            .map(String::trim)
            .filter(str -> !str.isEmpty())
            .filter(str -> !AI_PROJECT_LABEL.matcher(str).matches())
            .toList();
    for (int i = 0; i < lines.size() - 2; i += 3) {
      boolean addedNotes = false;
      Matcher nameMatcher = AI_REMOVE_LABEL_AND_QUOTES.matcher(lines.get(i));
      Matcher shortDescrMatcher = AI_REMOVE_LABEL_AND_QUOTES.matcher(lines.get(i + 1));
      Matcher longDescrMatcher = AI_REMOVE_LABEL_AND_QUOTES.matcher(lines.get(i + 2));

      checkArgument(nameMatcher.matches());
      checkArgument(shortDescrMatcher.matches());
      checkArgument(longDescrMatcher.matches());

      String name = lines.get(i);
      if (!Strings.isNullOrEmpty(nameMatcher.group("text"))) {
        name = nameMatcher.group("text");
      } else {
        log.addNote("Name failed to match: %s", lines.get(i));
        addedNotes = true;
      }

      String shortDescr = lines.get(i + 1);
      if (!Strings.isNullOrEmpty(shortDescrMatcher.group("text"))) {
        shortDescr = shortDescrMatcher.group("text");
      } else {
        log.addNote("Short description failed to match: %s", lines.get(i + 1));
        addedNotes = true;
      }

      String longDescr = lines.get(i + 2);
      if (!Strings.isNullOrEmpty(longDescrMatcher.group("text"))) {
        longDescr = longDescrMatcher.group("text");
      } else {
        log.addNote("Long description failed to match: %s", lines.get(i + 2));
        addedNotes = true;
      }

      Project project =
          new Project()
              .setCreationTime(Instant.now())
              .setIkigaiInput(ikigaiInput)
              .setName(name)
              .setShortDescr(shortDescr)
              .setShortDescrQuill(QuillInitializer.toQuillDelta(shortDescr))
              .setLongDescr(longDescr)
              .setLongDescrQuill(QuillInitializer.toQuillDelta(longDescr));
      projects.add(project);
      response.addProjects(DataAccess.convertProjectToProto(project));
      if (addedNotes) {
        log.addNote("");
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
              checkArgument(request.hasUserXId());
              var response = GetProjectsResponse.newBuilder();

              response.addAllProjects(
                  DataAccess.getProtoProjectsByUserXId(db, request.getUserXId()));

              return response.build();
            })
        .finish();
  }
}
