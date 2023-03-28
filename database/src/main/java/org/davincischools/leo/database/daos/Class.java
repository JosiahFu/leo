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
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = Class.ENTITY_NAME)
@Table(name = Class.TABLE_NAME, schema = "leo_temp")
public class Class implements Serializable {

  public static final String ENTITY_NAME = "Class";
  public static final String TABLE_NAME = "classes";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_TITLE_NAME = "title";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";
  public static final String JOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME = "class_id";
  public static final String INVERSEJOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME = "teacher_id";
  public static final String JOINCOLUMNS_JOINCOLUMN_STUDENTS_NAME = "class_id";
  public static final String INVERSEJOINCOLUMNS_JOINCOLUMN_STUDENTS_NAME = "student_id";
  private static final long serialVersionUID = -1487822773589610429L;


  private Integer id;

  private String title;

  private byte[] shortDescrQuill;

  private byte[] longDescrQuill;

  private School school;

  private Set<KnowledgeAndSkill> knowledgeAndSkills = new LinkedHashSet<>();

  private Set<Teacher> teachers = new LinkedHashSet<>();

  private Set<Student> students = new LinkedHashSet<>();

  private Set<Assignment> assignments = new LinkedHashSet<>();

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

  @Column(name = COLUMN_TITLE_NAME, nullable = false)
  public String getTitle() {
    return title;
  }

  public Class setTitle(String title) {
    this.title = title;
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

  @OneToMany(mappedBy = "classField")
  public Set<KnowledgeAndSkill> getKnowledgeAndSkills() {
    return knowledgeAndSkills;
  }

  public Class setKnowledgeAndSkills(Set<KnowledgeAndSkill> knowledgeAndSkills) {
    this.knowledgeAndSkills = knowledgeAndSkills;
    return this;
  }

  @ManyToMany
  @JoinTable(joinColumns = @JoinColumn(name = JOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME),
      inverseJoinColumns = @JoinColumn(name = INVERSEJOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME))
  public Set<Teacher> getTeachers() {
    return teachers;
  }

  public Class setTeachers(Set<Teacher> teachers) {
    this.teachers = teachers;
    return this;
  }

  @ManyToMany
  @JoinTable(joinColumns = @JoinColumn(name = JOINCOLUMNS_JOINCOLUMN_STUDENTS_NAME),
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

}