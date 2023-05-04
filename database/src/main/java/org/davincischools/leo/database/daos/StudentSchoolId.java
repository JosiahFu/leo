package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class StudentSchoolId implements Serializable {

  public static final String COLUMN_STUDENTID_NAME = "student_id";
  public static final String COLUMN_SCHOOLID_NAME = "school_id";
  private static final long serialVersionUID = -1746202361451317095L;

  private Integer studentId;

  private Integer schoolId;

  @Column(name = COLUMN_STUDENTID_NAME, nullable = false)
  public Integer getStudentId() {
    return studentId;
  }

  public StudentSchoolId setStudentId(Integer studentId) {
    this.studentId = studentId;
    return this;
  }

  @Column(name = COLUMN_SCHOOLID_NAME, nullable = false)
  public Integer getSchoolId() {
    return schoolId;
  }

  public StudentSchoolId setSchoolId(Integer schoolId) {
    this.schoolId = schoolId;
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
    StudentSchoolId entity = (StudentSchoolId) o;
    return Objects.equals(this.studentId, entity.studentId)
        && Objects.equals(this.schoolId, entity.schoolId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, schoolId);
  }
}
