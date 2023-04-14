package org.davincischools.leo.server.controllers;

import com.google.common.base.Joiner;
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
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.Database.StudentRepository.StudentAssignment;
import org.davincischools.leo.protos.class_management.GenerateAssignmentProjectsRequest;
import org.davincischools.leo.protos.class_management.GenerateAssignmentProjectsResponse;
import org.davincischools.leo.protos.class_management.GetStudentAssignmentsRequest;
import org.davincischools.leo.protos.class_management.GetStudentAssignmentsResponse;
import org.davincischools.leo.protos.open_ai.OpenAiMessage;
import org.davincischools.leo.protos.open_ai.OpenAiRequest;
import org.davincischools.leo.protos.open_ai.OpenAiResponse;
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClassManagementService {

  private static final Joiner COMMA_AND_JOINER = Joiner.on(", and ");

  private static final Pattern AI_PROJECT_NAME = Pattern.compile("[^:]:\\s*[\"“]?([^\"“]+)[\"“]?");
  private static final Pattern AI_PROJECT_SHORT_DESCR = Pattern.compile("[^:]:\\s*(.+)");
  private static final Pattern AI_PROJECT_LONG_DESCR = Pattern.compile("[^:]:\\s*(.+)");

  @Autowired Database db;
  @Autowired OpenAiUtils openAiUtils;

  @PostMapping(value = "/api/protos/ClassManagementService/GetStudentAssignments")
  @ResponseBody
  public GetStudentAssignmentsResponse getStudentAssignments(
      @RequestBody Optional<GetStudentAssignmentsRequest> optionalRequest) {
    var request = optionalRequest.orElse(GetStudentAssignmentsRequest.getDefaultInstance());
    var response = GetStudentAssignmentsResponse.newBuilder();

    for (StudentAssignment assignment :
        db.getStudentRepository().findAllAssignmentsByStudentUserId(request.getUserId())) {
      response.addAssignments(
          DataAccess.convertAssignmentToProto(assignment.classField(), assignment.assignment()));
    }

    return response.build();
  }

  @PostMapping(value = "/api/protos/ClassManagementService/GenerateAssignmentProjects")
  @ResponseBody
  public GenerateAssignmentProjectsResponse generateAssignmentProjects(
      @RequestBody Optional<GenerateAssignmentProjectsRequest> optionalRequest) throws IOException {
    var request = optionalRequest.orElse(GenerateAssignmentProjectsRequest.getDefaultInstance());
    var response = GenerateAssignmentProjectsResponse.newBuilder();

    User user = db.getUserRepository().findFullUserByUserId(request.getUserId()).orElseThrow();

    // Save the Ikigai settings.
    IkigaiInput ikigaiInput =
        db.getIkigaiInputRepository()
            .save(
                new IkigaiInput()
                    .setCreationTime(Instant.now())
                    .setUser(user)
                    .setAssignment(new Assignment().setId(request.getAssignmentId()))
                    .setSomethingYouLove(request.getSomethingYouLove())
                    .setWhatYouAreGoodAt(request.getWhatYouAreGoodAt()));

    // Get knowledge and skill contribution.
    List<KnowledgeAndSkill> knowledgeAndSkills =
        db.getAssignmentRepository().findAllKnowledgeAndSkillsById(request.getAssignmentId());
    String knowledgeAndSkillsTextList =
        COMMA_AND_JOINER.join(
            Lists.transform(
                knowledgeAndSkills,
                knowledgeAndSkill ->
                    ("\""
                        + StringEscapeUtils.escapeJava(knowledgeAndSkill.getShortDescr())
                        + "\"")));

    // Query OpenAI for projects.
    OpenAiRequest aiRequest =
        OpenAiRequest.newBuilder()
            .setModel(OpenAiUtils.GPT_3_5_TURBO_MODEL)
            .addMessages(
                OpenAiMessage.newBuilder()
                    .setRole("system")
                    .setContent(
                        String.format(
                            "You are a senior student who wants to spend 60 hours to build a"
                                + " project that demonstrates your mastery of %s. You are"
                                + " passionate about \"%s\" and good at \"%s\".",
                            knowledgeAndSkillsTextList,
                            StringEscapeUtils.escapeJava(request.getSomethingYouLove()),
                            StringEscapeUtils.escapeJava(request.getWhatYouAreGoodAt()))))
            .addMessages(
                OpenAiMessage.newBuilder()
                    .setRole("user")
                    .setContent(
                        "Generate 5 projects that would fit the criteria. Place a title, a short"
                            + " description, and a full description on separate lines."))
            .build();

    OpenAiResponse aiResponse =
        openAiUtils
            .sendOpenAiRequest(aiRequest, OpenAiResponse.newBuilder(), Optional.of(user.getId()))
            .build();

    // Parse the result into separate lines.
    List<String> lines = new ArrayList();
    BufferedReader reader =
        new BufferedReader(new StringReader(aiResponse.getChoices(0).getMessage().getContent()));
    String line;
    while ((line = reader.readLine()) != null) {
      lines.add(line);
    }

    // Process the lines and extract projects.
    lines = lines.stream().map(String::trim).filter(str -> !str.isEmpty()).toList();
    for (int i = 0; i < lines.size() - 2; ++i) {
      Matcher name = AI_PROJECT_NAME.matcher(lines.get(i));
      Matcher shortDescr = AI_PROJECT_SHORT_DESCR.matcher(lines.get(i + 1));
      Matcher longDescr = AI_PROJECT_LONG_DESCR.matcher(lines.get(i + 2));
      if (name.find() && shortDescr.find() && longDescr.find()) {
        db.getProjectRepository()
            .save(
                new Project()
                    .setCreationTime(Instant.now())
                    .setIkigaiInput(ikigaiInput)
                    .setName(name.group(1))
                    .setShortDescr(shortDescr.group(1))
                    .setLongDescr(longDescr.group(1)));
        i += 2;
      }
    }

    return response.build();
  }
}
