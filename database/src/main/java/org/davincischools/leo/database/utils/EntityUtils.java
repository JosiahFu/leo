package org.davincischools.leo.database.utils;

import jakarta.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class EntityUtils {

  public static Column getColumn(Class<?> entity, String columnName) {
    List<Column> columns = new ArrayList<>();
    for (Field field : entity.getDeclaredFields()) {
      Optional.ofNullable(field.getAnnotation(Column.class)).ifPresent(columns::add);
    }
    for (Method method : entity.getDeclaredMethods()) {
      Optional.ofNullable(method.getAnnotation(Column.class)).ifPresent(columns::add);
    }
    for (Column column : columns) {
      if (column.name().equals(columnName)) {
        return column;
      }
    }
    throw new IllegalArgumentException(
        "Entity " + entity.getSimpleName() + " does not have column " + columnName + ".");
  }

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
