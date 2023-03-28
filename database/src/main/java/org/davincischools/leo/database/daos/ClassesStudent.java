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

@Entity(name = ClassesStudent.ENTITY_NAME)
@Table(name = ClassesStudent.TABLE_NAME, schema = "leo_temp", indexes = {
    @Index(name = "class_id", columnList = "class_id"),
    @Index(name = "student_id", columnList = "student_id")
})
public class ClassesStudent implements Serializable {

  public static final String ENTITY_NAME = "ClassesStudent";
  public static final String TABLE_NAME = "classes_students";
  private static final long serialVersionUID = -9201231937981988005L;

  private ClassesStudentId id;

  private Class classField;

  private Student student;

  @EmbeddedId
  public ClassesStudentId getId() {
    return id;
  }

  public ClassesStudent setId(ClassesStudentId id) {
    this.id = id;
    return this;
  }

  @MapsId("classId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_id", nullable = false)
  public Class getClassField() {
    return classField;
  }

  public ClassesStudent setClassField(Class classField) {
    this.classField = classField;
    return this;
  }

  @MapsId("studentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id", nullable = false)
  public Student getStudent() {
    return student;
  }

  public ClassesStudent setStudent(Student student) {
    this.student = student;
    return this;
  }

}