package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity(name = IkigaiInput.ENTITY_NAME)
@Table(name = IkigaiInput.TABLE_NAME, schema = "leo_temp")
public class IkigaiInput {

  public static final String ENTITY_NAME = "IkigaiInput";
  public static final String TABLE_NAME = "ikigai_input";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_SOMETHINGYOULOVE_NAME = "something_you_love";
  public static final String COLUMN_WHATYOUAREGOODAT_NAME = "what_you_are_good_at";
  public static final String COLUMN_PENDINGCOMPLETION_NAME = "pending_completion";

  private Integer id;

  private Instant creationTime;

  private String somethingYouLove;

  private String whatYouAreGoodAt;

  private Instant pendingCompletion;

  private Assignment assignment;

  private UserX userX;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public IkigaiInput setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public IkigaiInput setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Lob
  @Column(name = COLUMN_SOMETHINGYOULOVE_NAME, nullable = false)
  public String getSomethingYouLove() {
    return somethingYouLove;
  }

  public IkigaiInput setSomethingYouLove(String somethingYouLove) {
    this.somethingYouLove = somethingYouLove;
    return this;
  }

  @Lob
  @Column(name = COLUMN_WHATYOUAREGOODAT_NAME, nullable = false)
  public String getWhatYouAreGoodAt() {
    return whatYouAreGoodAt;
  }

  public IkigaiInput setWhatYouAreGoodAt(String whatYouAreGoodAt) {
    this.whatYouAreGoodAt = whatYouAreGoodAt;
    return this;
  }

  @Column(name = COLUMN_PENDINGCOMPLETION_NAME)
  public Instant getPendingCompletion() {
    return pendingCompletion;
  }

  public IkigaiInput setPendingCompletion(Instant pendingCompletion) {
    this.pendingCompletion = pendingCompletion;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  public Assignment getAssignment() {
    return assignment;
  }

  public IkigaiInput setAssignment(Assignment assignment) {
    this.assignment = assignment;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_x_id", nullable = false)
  public UserX getUserX() {
    return userX;
  }

  public IkigaiInput setUserX(UserX userX) {
    this.userX = userX;
    return this;
  }
}
