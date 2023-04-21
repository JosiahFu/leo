package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class StudentClassXId implements Serializable {

  public static final String COLUMN_STUDENTID_NAME = "student_id";
  public static final String COLUMN_CLASSXID_NAME = "class_x_id";
  private static final long serialVersionUID = -5385720289826446713L;

  private Integer studentId;

  private Integer classXId;

  @Column(name = COLUMN_STUDENTID_NAME, nullable = false)
  public Integer getStudentId() {
    return studentId;
  }

  public StudentClassXId setStudentId(Integer studentId) {
    this.studentId = studentId;
    return this;
  }

  @Column(name = COLUMN_CLASSXID_NAME, nullable = false)
  public Integer getClassXId() {
    return classXId;
  }

  public StudentClassXId setClassXId(Integer classXId) {
    this.classXId = classXId;
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
    StudentClassXId entity = (StudentClassXId) o;
    return Objects.equals(this.studentId, entity.studentId)
        && Objects.equals(this.classXId, entity.classXId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, classXId);
  }
}
