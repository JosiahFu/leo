package org.davincischools.leo.database.daos;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity(name = ClassTeacher.ENTITY_NAME)
@Table(
    name = ClassTeacher.TABLE_NAME,
    schema = "leo_temp",
    indexes = {
      @Index(name = "teacher_id", columnList = "teacher_id"),
      @Index(name = "class_id", columnList = "class_id")
    })
public class ClassTeacher {

  public static final String ENTITY_NAME = "ClassTeacher";
  public static final String TABLE_NAME = "class_teacher";

  private ClassTeacherId id;

  private Class classField;

  private Teacher teacher;

  @EmbeddedId
  public ClassTeacherId getId() {
    return id;
  }

  public ClassTeacher setId(ClassTeacherId id) {
    this.id = id;
    return this;
  }

  @MapsId("classId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_id", nullable = false)
  public Class getClassField() {
    return classField;
  }

  public ClassTeacher setClassField(Class classField) {
    this.classField = classField;
    return this;
  }

  @MapsId("teacherId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "teacher_id", nullable = false)
  public Teacher getTeacher() {
    return teacher;
  }

  public ClassTeacher setTeacher(Teacher teacher) {
    this.teacher = teacher;
    return this;
  }
}
