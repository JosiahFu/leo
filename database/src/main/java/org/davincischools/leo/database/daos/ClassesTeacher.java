package org.davincischools.leo.database.daos;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity(name = ClassesTeacher.ENTITY_NAME)
@Table(name = ClassesTeacher.TABLE_NAME, schema = "leo_temp", indexes = {
    @Index(name = "teacher_id", columnList = "teacher_id"),
    @Index(name = "class_id", columnList = "class_id")
})
public class ClassesTeacher implements Serializable {

  public static final String ENTITY_NAME = "ClassesTeacher";
  public static final String TABLE_NAME = "classes_teachers";
  private static final long serialVersionUID = 1939616650011436311L;

  private ClassesTeacherId id;

  private Class classField;

  private Teacher teacher;

  @EmbeddedId
  public ClassesTeacherId getId() {
    return id;
  }

  public ClassesTeacher setId(ClassesTeacherId id) {
    this.id = id;
    return this;
  }

  @MapsId("classId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_id", nullable = false)
  public Class getClassField() {
    return classField;
  }

  public ClassesTeacher setClassField(Class classField) {
    this.classField = classField;
    return this;
  }

  @MapsId("teacherId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "teacher_id", nullable = false)
  public Teacher getTeacher() {
    return teacher;
  }

  public ClassesTeacher setTeacher(Teacher teacher) {
    this.teacher = teacher;
    return this;
  }

}