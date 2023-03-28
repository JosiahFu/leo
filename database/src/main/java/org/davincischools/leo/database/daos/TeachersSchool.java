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

@Entity(name = TeachersSchool.ENTITY_NAME)
@Table(name = TeachersSchool.TABLE_NAME, schema = "leo_temp", indexes = {
    @Index(name = "school_id", columnList = "school_id"),
    @Index(name = "teacher_id", columnList = "teacher_id")
})
public class TeachersSchool implements Serializable {

  public static final String ENTITY_NAME = "TeachersSchool";
  public static final String TABLE_NAME = "teachers_schools";
  private static final long serialVersionUID = -6657373702395542847L;

  private TeachersSchoolId id;

  private Teacher teacher;

  private School school;

  @EmbeddedId
  public TeachersSchoolId getId() {
    return id;
  }

  public TeachersSchool setId(TeachersSchoolId id) {
    this.id = id;
    return this;
  }

  @MapsId("teacherId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "teacher_id", nullable = false)
  public Teacher getTeacher() {
    return teacher;
  }

  public TeachersSchool setTeacher(Teacher teacher) {
    this.teacher = teacher;
    return this;
  }

  @MapsId("schoolId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "school_id", nullable = false)
  public School getSchool() {
    return school;
  }

  public TeachersSchool setSchool(School school) {
    this.school = school;
    return this;
  }

}