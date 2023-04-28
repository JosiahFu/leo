package org.davincischools.leo.server.controllers;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

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

  @Test
  public void testFormatWithLabelColon() throws IOException {
    List<Project> projects =
        ClassManagementService.extractProjects(
            log,
            GenerateAssignmentProjectsResponse.newBuilder(),
            null,
            "Project 1: \"Asdf Adventure: The Element Connection\"\n\n"
                + "Short Description: Build a game that teaches players about element valence"
                + " electrons and bonding through an Asdf Adventure!\n\n"
                + "Full Description: In this project, you will be building a game that takes the"
                + " player through an Asdf Adventure! Along the way, the game will teach the player"
                + " about element valence electrons and bonding. You'll need to research the"
                + " properties of elements to create challenges in the game that require different"
                + " types of bonds to be formed. You can use any programming language or platform"
                + " to build the game, but be sure to keep in mind the user experience and make"
                + " sure the game is engaging and educational!\n\n"
                + "Project 2: \"Asdf Elements Connect\"\n\n"
                + "Short Description: Create an interactive platform that allows users to connect"
                + " elements using valence electrons!\n\n"
                + "Full Description: For this project, you will be creating an interactive platform"
                + " that allows the user to connect different elements using valence electrons."
                + " Users should be able to drag and drop elements onto a \"board\", and then"
                + " connect them by drawing lines between valence electrons. You'll need to"
                + " research the properties of different elements and their valence electron"
                + " configurations to ensure that the platform is accurate. Consider using visual"
                + " aids such as color coding for different elements or highlighting the valence"
                + " electrons, so the user can easily see and understand the connections they are"
                + " making.\n\n"
                + "Project 3: \"Element Connection Puzzle\"\n\n"
                + "Short Description: Challenge players to solve puzzles by correctly connecting"
                + " elements using valence electrons!\n\n"
                + "Full Description: In this project, you will be building a puzzle game where"
                + " players are tested on their knowledge of element valence electrons and bonding."
                + " The player will be given a set of elements and must connect them by drawing"
                + " lines between valence electrons, resulting in a completed molecule. You'll"
                + " need to create puzzles that get progressively more difficult and make sure that"
                + " the platform allows for user feedback and hints. This project is a great way to"
                + " combine gaming and education while challenging players to think critically and"
                + " logically.\n\n"
                + "Project 4: \"Element Connect Card Game\"\n\n"
                + "Short Description: Design a card game that teaches players about element valence"
                + " electrons and bonding!\n\n"
                + "Full Description: In this project, you will be designing a card game that"
                + " teaches players about element valence electrons and bonding. Players will be"
                + " dealt cards with different elements and must connect them using valence"
                + " electrons, similar to the puzzle game in Project 3. The game can be played with"
                + " multiple players, and each player will have to use their knowledge of element"
                + " bonding to outsmart their opponents. You'll need to research the properties of"
                + " different elements to ensure the game is accurate and create multiple sets of"
                + " cards with varying difficulty levels. This project is an excellent way to"
                + " combine education and fun while challenging players to think strategically.\n\n"
                + "Project 5: \"Asdf Lab: Exploring Element Bonds\"\n\n"
                + "Short Description: Build an interactive lab that allows users to explore element"
                + " bonding through Asdf!\n\n"
                + "Full Description: For this project, you will be building an interactive lab that"
                + " allows the user to explore element bonding by conducting different experiments."
                + " Users will be able to select two or more elements, and the platform will show"
                + " them the molecular structure that results from different types of bonds"
                + " (covalent, ionic, metallic, etc.). You'll need to research different"
                + " experiments that can be conducted and create an interface that allows for user"
                + " interaction and feedback. This project is a great way to combine Asdf with"
                + " education and allow users to experiment and discover concepts related to"
                + " element bonding.");
    assertThat(projects).hasSize(5);
    assertThat(DataAccess.convertProjectToProto(projects.get(0)))
        .isEqualTo(
            org.davincischools.leo.protos.class_management.Project.newBuilder()
                .setId(-1)
                .setName("Asdf Adventure: The Element Connection")
                .setShortDescr(
                    "Build a game that teaches players about element valence electrons and bonding"
                        + " through an Asdf Adventure!")
                .setLongDescr(
                    "In this project, you will be building a game that takes the player through an"
                        + " Asdf Adventure! Along the way, the game will teach the player about"
                        + " element valence electrons and bonding. You'll need to research the"
                        + " properties of elements to create challenges in the game that require"
                        + " different types of bonds to be formed. You can use any programming"
                        + " language or platform to build the game, but be sure to keep in mind the"
                        + " user experience and make sure the game is engaging and educational!")
                .build());
  }

  @Test
  public void testFormatWithNumberDocLabelColon() throws IOException {
    List<Project> projects =
        ClassManagementService.extractProjects(
            log,
            GenerateAssignmentProjectsResponse.newBuilder(),
            null,
            "1. Title: Asdf Adventures: Connect the Elements\n"
                + "Short Description: A game where players connect elements using their valence"
                + " electrons to create compounds.\n\n"
                + "Full Description:\n"
                + "In this project, we will create a game called \"Asdf Adventures: Connect the"
                + " Elements\". Players will be given a set of elements and must use their"
                + " knowledge of valence electrons to connect them and create compounds. The game"
                + " will have different levels of difficulty and will include a leaderboard to"
                + " track players' progress.\n\n"
                + "2. Title: Asdf-Driven Molecule Builder\n"
                + "Short Description: A program that allows users to build different molecules"
                + " using valence electrons.\n\n"
                + "Full Description:\n"
                + "In this project, we will create a program that allows users to build different"
                + " molecules using valence electrons. Users will be able to select different"
                + " elements and arrange them in different ways to create various compounds. The"
                + " program will show the Lewis dot diagram and other useful information about the"
                + " molecule. The program will be built using the asdf language.\n\n"
                + "3. Title: Valence Electron Quiz\n"
                + "Short Description: A quiz that tests users' knowledge of valence electrons for"
                + " different elements.\n\n"
                + "Full Description:\n"
                + "In this project, we will create a quiz that tests users' knowledge of valence"
                + " electrons for different elements. The quiz will show users the element and ask"
                + " how many valence electrons it has. Users will be able to select their answer"
                + " from a multiple-choice list. The quiz will be timed, and users will receive a"
                + " score at the end of the quiz.\n\n"
                + "4. Title: Asdf-Driven Periodic Table\n"
                + "Short Description: A program that displays the periodic table and allows users"
                + " to view how many valence electrons each element has.\n\n"
                + "Full Description:\n"
                + "In this project, we will create a program that displays the periodic table and"
                + " allows users to view how many valence electrons each element has. Users will be"
                + " able to click on an element and see its valence electron count, as well as"
                + " other information about the element. The periodic table will be displayed in a"
                + " user-friendly interface and will be built using the asdf language.\n\n"
                + "5. Title: Asdf-Driven Bonding Simulator\n"
                + "Short Description: A program that simulates the bonding of different elements"
                + " using valence electrons.\n\n"
                + "Full Description:\n"
                + "In this project, we will create a program that simulates the bonding of"
                + " different elements using valence electrons. Users will be able to select two"
                + " elements and see how their valence electrons interact to create a compound. The"
                + " program will show the final Lewis dot diagram of the compound and other useful"
                + " information. The program will be built using the asdf language.");
    assertThat(projects).hasSize(5);
    assertThat(DataAccess.convertProjectToProto(projects.get(0)))
        .isEqualTo(
            org.davincischools.leo.protos.class_management.Project.newBuilder()
                .setId(-1)
                .setName("Asdf Adventures: Connect the Elements")
                .setShortDescr(
                    "A game where players connect elements using their valence"
                        + " electrons to create compounds.")
                .setLongDescr(
                    "In this project, we will create a game called \"Asdf Adventures: Connect the"
                        + " Elements\". Players will be given a set of elements and must use their"
                        + " knowledge of valence electrons to connect them and create compounds."
                        + " The game will have different levels of difficulty and will include a"
                        + " leaderboard to track players' progress.")
                .build());
  }
}
