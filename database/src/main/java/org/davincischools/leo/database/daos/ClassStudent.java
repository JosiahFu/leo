package org.davincischools.leo.database.daos;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity(name = ClassStudent.ENTITY_NAME)
@Table(
    name = ClassStudent.TABLE_NAME,
    schema = "leo_temp",
    indexes = {
      @Index(name = "class_id", columnList = "class_id"),
      @Index(name = "student_id", columnList = "student_id")
    })
public class ClassStudent {

  public static final String ENTITY_NAME = "ClassStudent";
  public static final String TABLE_NAME = "class_student";

  private ClassStudentId id;

  private Class classField;

  private Student student;

  @EmbeddedId
  public ClassStudentId getId() {
    return id;
  }

  public ClassStudent setId(ClassStudentId id) {
    this.id = id;
    return this;
  }

  @MapsId("classId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_id", nullable = false)
  public Class getClassField() {
    return classField;
  }

  public ClassStudent setClassField(Class classField) {
    this.classField = classField;
    return this;
  }

  @MapsId("studentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id", nullable = false)
  public Student getStudent() {
    return student;
  }

  public ClassStudent setStudent(Student student) {
    this.student = student;
    return this;
  }
}
