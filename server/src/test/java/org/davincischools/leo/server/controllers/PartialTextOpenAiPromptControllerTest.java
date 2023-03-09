package org.davincischools.leo.server.controllers;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class PartialTextOpenAiPromptControllerTest {
  String LIST_RESPONSE_CONTENT =
      "\n\n"
          // We want responses that look like the following.
          + "1. Typing challenges\n"
          + "\n"
          + "1. Typing exams";

  @Test
  public void parseContentIntoListTest() {
    assertThat(PartialTextOpenAiPromptController.parseContentIntoList(LIST_RESPONSE_CONTENT))
        .containsExactly("Typing challenges", "Typing exams")
        .inOrder();
  }
}
