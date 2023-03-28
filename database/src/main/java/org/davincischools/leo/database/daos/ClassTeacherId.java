package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class ClassTeacherId implements Serializable {

  public static final String COLUMN_CLASSID_NAME = "class_id";
  public static final String COLUMN_TEACHERID_NAME = "teacher_id";
  private static final long serialVersionUID = -6184512459630289767L;

  private Integer classId;

  private Integer teacherId;

  @Column(name = COLUMN_CLASSID_NAME, nullable = false)
  public Integer getClassId() {
    return classId;
  }

  public ClassTeacherId setClassId(Integer classId) {
    this.classId = classId;
    return this;
  }

  @Column(name = COLUMN_TEACHERID_NAME, nullable = false)
  public Integer getTeacherId() {
    return teacherId;
  }

  public ClassTeacherId setTeacherId(Integer teacherId) {
    this.teacherId = teacherId;
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
    ClassTeacherId entity = (ClassTeacherId) o;
    return Objects.equals(this.classId, entity.classId)
        && Objects.equals(this.teacherId, entity.teacherId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId, teacherId);
  }
}
