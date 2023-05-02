package org.davincischools.leo.server.controllers;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import java.io.IOException;
import java.util.List;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.protos.class_management.GenerateAssignmentProjectsResponse;
import org.davincischools.leo.server.utils.DataAccess;
import org.davincischools.leo.server.utils.LogUtils.LogOperations;
import org.junit.Test;
import org.mockito.Mockito;

public class ClassManagementServiceTest {
  LogOperations log = Mockito.mock(LogOperations.class);

  org.davincischools.leo.protos.class_management.Project toProject(String protoString)
      throws ParseException {
    return TextFormat.parse(
        protoString, org.davincischools.leo.protos.class_management.Project.class);
  }

  @Test
  public void testFormatWithLabelColon() throws IOException {
    log.addNote(Mockito.anyString());
    List<Project> projects =
        ClassManagementService.extractProjects(
            log,
            GenerateAssignmentProjectsResponse.newBuilder(),
            null,
            "Project 1: \"Title Part: Title Part\"\n\n"
                + "Short Description: Some short description.\n\n"
                + "Full Description: Some full description.\n\n"
                + "Project 2: \"Title Prefix: Main Title\"\n\n"
                + "Short Description: Short Description!\n\n"
                + "Full Description: Full Description.");
    assertThat(projects.stream().map(DataAccess::convertProjectToProto).toList())
        .ignoringRepeatedFieldOrder()
        .containsExactly(
            toProject(
                """
                    id: -1
                    name: "Title Part: Title Part"
                    short_descr: "Some short description."
                    long_descr: "Some full description."
            """),
            toProject(
                """
                    id: -1
                    name: "Title Prefix: Main Title"
                    short_descr: "Short Description!"
                    long_descr: "Full Description."
            """));
  }

  @Test
  public void testFormatWithNumberDotLabelColon() throws IOException {
    List<Project> projects =
        ClassManagementService.extractProjects(
            log,
            GenerateAssignmentProjectsResponse.newBuilder(),
            null,
            "1. Title: Primary Title: Secondary Title\n"
                + "Short Description: Short description 1.\n\n"
                + "Full Description:\n"
                + "Full description 1.\n\n"
                + "2. Title: Something something\n"
                + "Short Description: Short Description 2.\n\n"
                + "Full Description:\n"
                + "Full Description 2.");
    assertThat(projects.stream().map(DataAccess::convertProjectToProto).toList())
        .ignoringRepeatedFieldOrder()
        .containsExactly(
            toProject(
                """
                    id: -1
                    name: "Primary Title: Secondary Title"
                    short_descr: "Short description 1."
                    long_descr: "Full description 1."
            """),
            toProject(
                """
                    id: -1
                    name: "Something something"
                    short_descr: "Short Description 2."
                    long_descr: "Full Description 2."
            """));
  }

  @Test
  public void testFormatWithMultiplePoints() throws IOException {
    List<Project> projects =
        ClassManagementService.extractProjects(
            log,
            GenerateAssignmentProjectsResponse.newBuilder(),
            null,
            "Project 1: Title: Primary Title: Secondary Title\n"
                + "Short Description: Short description 1.\n\n"
                + "Full Description:\n"
                + "- Full description 1a.\n\n"
                + "- Full description 1b.\n\n"
                + "- Full description 1c.\n\n"
                + "Project 2: \"Something something 2\"\n"
                + "Short Description: Short Description 2.\n\n"
                + "Full Description:\n"
                + "- Full description 2a.\n\n"
                + "- Full description 2b.");
    assertThat(projects).hasSize(2);
    assertThat(DataAccess.convertProjectToProto(projects.get(0)))
        .isEqualTo(
            org.davincischools.leo.protos.class_management.Project.newBuilder()
                .setId(-1)
                .setName("Primary Title: Secondary Title")
                .setShortDescr("Short description 1.")
                .setLongDescr("Full description 1a.\nFull description 1b.\nFull description 1c.")
                .build());
    assertThat(DataAccess.convertProjectToProto(projects.get(1)))
        .isEqualTo(
            org.davincischools.leo.protos.class_management.Project.newBuilder()
                .setId(-1)
                .setName("Something something 2")
                .setShortDescr("Short Description 2.")
                .setLongDescr("Full description 2a.\nFull description 2b.")
                .build());
  }
}
