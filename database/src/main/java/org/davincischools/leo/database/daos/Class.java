package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = Class.ENTITY_NAME)
@Table(name = Class.TABLE_NAME, schema = "leo_temp")
public class Class {

  public static final String ENTITY_NAME = "Class";
  public static final String TABLE_NAME = "class";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";
  public static final String JOINTABLE_STUDENTS_NAME = "class_student";
  public static final String JOINCOLUMNS_JOINCOLUMN_STUDENTS_NAME = "class_id";
  public static final String INVERSEJOINCOLUMNS_JOINCOLUMN_STUDENTS_NAME = "student_id";
  public static final String JOINTABLE_TEACHERS_NAME = "class_teacher";
  public static final String JOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME = "class_id";
  public static final String INVERSEJOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME = "teacher_id";

  private Integer id;

  private String name;

  private byte[] shortDescrQuill;

  private byte[] longDescrQuill;

  private School school;

  private Set<Student> students = new LinkedHashSet<>();

  private Set<Assignment> assignments = new LinkedHashSet<>();

  private Set<KnowledgeAndSkill> knowledgeAndSkills = new LinkedHashSet<>();

  private Set<Teacher> teachers = new LinkedHashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Class setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public Class setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCRQUILL_NAME, nullable = false)
  public byte[] getShortDescrQuill() {
    return shortDescrQuill;
  }

  public Class setShortDescrQuill(byte[] shortDescrQuill) {
    this.shortDescrQuill = shortDescrQuill;
    return this;
  }

  @Column(name = COLUMN_LONGDESCRQUILL_NAME, nullable = false)
  public byte[] getLongDescrQuill() {
    return longDescrQuill;
  }

  public Class setLongDescrQuill(byte[] longDescrQuill) {
    this.longDescrQuill = longDescrQuill;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "school_id", nullable = false)
  public School getSchool() {
    return school;
  }

  public Class setSchool(School school) {
    this.school = school;
    return this;
  }

  @ManyToMany
  @JoinTable(
      name = JOINTABLE_STUDENTS_NAME,
      joinColumns = @JoinColumn(name = JOINCOLUMNS_JOINCOLUMN_STUDENTS_NAME),
      inverseJoinColumns = @JoinColumn(name = INVERSEJOINCOLUMNS_JOINCOLUMN_STUDENTS_NAME))
  public Set<Student> getStudents() {
    return students;
  }

  public Class setStudents(Set<Student> students) {
    this.students = students;
    return this;
  }

  @OneToMany(mappedBy = "classField")
  public Set<Assignment> getAssignments() {
    return assignments;
  }

  public Class setAssignments(Set<Assignment> assignments) {
    this.assignments = assignments;
    return this;
  }

  @OneToMany(mappedBy = "classField")
  public Set<KnowledgeAndSkill> getKnowledgeAndSkills() {
    return knowledgeAndSkills;
  }

  public Class setKnowledgeAndSkills(Set<KnowledgeAndSkill> knowledgeAndSkills) {
    this.knowledgeAndSkills = knowledgeAndSkills;
    return this;
  }

  @ManyToMany
  @JoinTable(
      name = JOINTABLE_TEACHERS_NAME,
      joinColumns = @JoinColumn(name = JOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME),
      inverseJoinColumns = @JoinColumn(name = INVERSEJOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME))
  public Set<Teacher> getTeachers() {
    return teachers;
  }

  public Class setTeachers(Set<Teacher> teachers) {
    this.teachers = teachers;
    return this;
  }
}
