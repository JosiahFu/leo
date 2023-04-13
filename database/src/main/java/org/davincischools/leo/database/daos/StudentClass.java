package org.davincischools.leo.database.daos;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity(name = StudentClass.ENTITY_NAME)
@Table(name = StudentClass.TABLE_NAME, schema = "leo_temp")
public class StudentClass {

  public static final String ENTITY_NAME = "StudentClass";
  public static final String TABLE_NAME = "student_class";

  private StudentClassId id;

  private Student student;

  private Class classField;

  @EmbeddedId
  public StudentClassId getId() {
    return id;
  }

  public StudentClass setId(StudentClassId id) {
    this.id = id;
    return this;
  }

  @MapsId("studentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id", nullable = false)
  public Student getStudent() {
    return student;
  }

  public StudentClass setStudent(Student student) {
    this.student = student;
    return this;
  }

  @MapsId("classId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_id", nullable = false)
  public Class getClassField() {
    return classField;
  }

  public StudentClass setClassField(Class classField) {
    this.classField = classField;
    return this;
  }
}
