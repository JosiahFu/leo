package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class ClassesStudentId implements Serializable {

  public static final String COLUMN_CLASSID_NAME = "class_id";
  public static final String COLUMN_STUDENTID_NAME = "student_id";
  private static final long serialVersionUID = -7953479969182746356L;

  private Integer classId;

  private Integer studentId;

  @Column(name = COLUMN_CLASSID_NAME, nullable = false)
  public Integer getClassId() {
    return classId;
  }

  public ClassesStudentId setClassId(Integer classId) {
    this.classId = classId;
    return this;
  }

  @Column(name = COLUMN_STUDENTID_NAME, nullable = false)
  public Integer getStudentId() {
    return studentId;
  }

  public ClassesStudentId setStudentId(Integer studentId) {
    this.studentId = studentId;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    ClassesStudentId entity = (ClassesStudentId) o;
    return Objects.equals(this.studentId, entity.studentId) &&
        Objects.equals(this.classId, entity.classId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, classId);
  }

}