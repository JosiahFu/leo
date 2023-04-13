package org.davincischools.leo.database.daos;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity(name = TeacherSchool.ENTITY_NAME)
@Table(name = TeacherSchool.TABLE_NAME, schema = "leo_temp")
public class TeacherSchool {

  public static final String ENTITY_NAME = "TeacherSchool";
  public static final String TABLE_NAME = "teacher_school";

  private TeacherSchoolId id;

  private Teacher teacher;

  private School school;

  @EmbeddedId
  public TeacherSchoolId getId() {
    return id;
  }

  public TeacherSchool setId(TeacherSchoolId id) {
    this.id = id;
    return this;
  }

  @MapsId("teacherId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "teacher_id", nullable = false)
  public Teacher getTeacher() {
    return teacher;
  }

  public TeacherSchool setTeacher(Teacher teacher) {
    this.teacher = teacher;
    return this;
  }

  @MapsId("schoolId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "school_id", nullable = false)
  public School getSchool() {
    return school;
  }

  public TeacherSchool setSchool(School school) {
    this.school = school;
    return this;
  }
}
