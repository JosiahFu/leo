package org.davincischools.leo.database.daos;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity(name = TeacherClass.ENTITY_NAME)
@Table(name = TeacherClass.TABLE_NAME, schema = "leo_temp")
public class TeacherClass {

  public static final String ENTITY_NAME = "TeacherClass";
  public static final String TABLE_NAME = "teacher_class";

  private TeacherClassId id;

  private Teacher teacher;

  private Class classField;

  @EmbeddedId
  public TeacherClassId getId() {
    return id;
  }

  public TeacherClass setId(TeacherClassId id) {
    this.id = id;
    return this;
  }

  @MapsId("teacherId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "teacher_id", nullable = false)
  public Teacher getTeacher() {
    return teacher;
  }

  public TeacherClass setTeacher(Teacher teacher) {
    this.teacher = teacher;
    return this;
  }

  @MapsId("classId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_id", nullable = false)
  public Class getClassField() {
    return classField;
  }

  public TeacherClass setClassField(Class classField) {
    this.classField = classField;
    return this;
  }
}
