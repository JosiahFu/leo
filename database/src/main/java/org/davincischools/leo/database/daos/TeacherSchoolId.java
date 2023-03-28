package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class TeacherSchoolId implements Serializable {

  public static final String COLUMN_TEACHERID_NAME = "teacher_id";
  public static final String COLUMN_SCHOOLID_NAME = "school_id";
  private static final long serialVersionUID = 2756930045806278485L;

  private Integer teacherId;

  private Integer schoolId;

  @Column(name = COLUMN_TEACHERID_NAME, nullable = false)
  public Integer getTeacherId() {
    return teacherId;
  }

  public TeacherSchoolId setTeacherId(Integer teacherId) {
    this.teacherId = teacherId;
    return this;
  }

  @Column(name = COLUMN_SCHOOLID_NAME, nullable = false)
  public Integer getSchoolId() {
    return schoolId;
  }

  public TeacherSchoolId setSchoolId(Integer schoolId) {
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
    TeacherSchoolId entity = (TeacherSchoolId) o;
    return Objects.equals(this.teacherId, entity.teacherId)
        && Objects.equals(this.schoolId, entity.schoolId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teacherId, schoolId);
  }
}
