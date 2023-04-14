package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity(name = User.ENTITY_NAME)
@Table(
    name = User.TABLE_NAME,
    schema = "leo_temp",
    indexes = {
      @Index(name = "email_address", columnList = "email_address", unique = true),
      @Index(name = "teacher_id", columnList = "teacher_id", unique = true),
      @Index(name = "admin_id", columnList = "admin_id", unique = true),
      @Index(name = "student_id", columnList = "student_id", unique = true)
    })
public class User {

  public static final String ENTITY_NAME = "User";
  public static final String TABLE_NAME = "user";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_FIRSTNAME_NAME = "first_name";
  public static final String COLUMN_LASTNAME_NAME = "last_name";
  public static final String COLUMN_EMAILADDRESS_NAME = "email_address";
  public static final String COLUMN_ENCODEDPASSWORDUTF8_NAME = "encoded_password_utf8";

  private Integer id;

  private Instant creationTime;

  private String firstName;

  private String lastName;

  private String emailAddress;

  private byte[] encodedPasswordUtf8;

  private District district;

  private Admin admin;

  private Teacher teacher;

  private Student student;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public User setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public User setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_FIRSTNAME_NAME, nullable = false)
  public String getFirstName() {
    return firstName;
  }

  public User setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  @Column(name = COLUMN_LASTNAME_NAME, nullable = false)
  public String getLastName() {
    return lastName;
  }

  public User setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  @Column(name = COLUMN_EMAILADDRESS_NAME, nullable = false, length = 254)
  public String getEmailAddress() {
    return emailAddress;
  }

  public User setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  @Column(name = COLUMN_ENCODEDPASSWORDUTF8_NAME, nullable = false)
  public byte[] getEncodedPasswordUtf8() {
    return encodedPasswordUtf8;
  }

  public User setEncodedPasswordUtf8(byte[] encodedPasswordUtf8) {
    this.encodedPasswordUtf8 = encodedPasswordUtf8;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "district_id")
  public District getDistrict() {
    return district;
  }

  public User setDistrict(District district) {
    this.district = district;
    return this;
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id")
  public Admin getAdmin() {
    return admin;
  }

  public User setAdmin(Admin admin) {
    this.admin = admin;
    return this;
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teacher_id")
  public Teacher getTeacher() {
    return teacher;
  }

  public User setTeacher(Teacher teacher) {
    this.teacher = teacher;
    return this;
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id")
  public Student getStudent() {
    return student;
  }

  public User setStudent(Student student) {
    this.student = student;
    return this;
  }
}
