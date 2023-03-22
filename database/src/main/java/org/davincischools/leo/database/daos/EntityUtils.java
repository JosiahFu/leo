package org.davincischools.leo.database.daos;

import java.util.function.Consumer;

public final class EntityUtils {

  public static boolean checkRequiredMaxLength(
      String value, String fieldNameForSentenceStart, int maxLength, Consumer<String> errorSetter) {
    return checkRequired(value, fieldNameForSentenceStart, errorSetter)
        && checkMaxLength(value, fieldNameForSentenceStart, maxLength, errorSetter);
  }

  public static boolean checkRequired(
      String value, String fieldNameForSentenceStart, Consumer<String> errorSetter) {
    return checkThat(!value.isEmpty(), errorSetter, "%s is required.", fieldNameForSentenceStart);
  }

  public static boolean checkMaxLength(
      String value, String fieldNameForSentenceStart, int maxLength, Consumer<String> errorSetter) {
    return checkThat(
        value.length() <= maxLength,
        errorSetter,
        "%s must be no longer than %s characters.",
        fieldNameForSentenceStart,
        maxLength);
  }

  public static boolean checkThat(
      boolean precondition, Consumer<String> errorSetter, String errorTemplate, Object... args) {
    if (!precondition) {
      errorSetter.accept(String.format(errorTemplate, args));
    }
    return precondition;
  }
}
