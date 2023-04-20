package org.davincischools.leo.database.utils;

public class QuillInitializer {

  private record DeltaContent(String text) {}

  private record Delta(DeltaContent[] ops) {}

  public static String toQuillDelta(String text) {
    // TODO: Create a quill Delta.
    return "";
  }
}
