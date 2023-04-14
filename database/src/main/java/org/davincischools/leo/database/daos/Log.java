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

@Entity(name = Log.ENTITY_NAME)
@Table(name = Log.TABLE_NAME, schema = "leo_temp")
public class Log {

  public static final String ENTITY_NAME = "Log";
  public static final String TABLE_NAME = "log";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_SOURCE_NAME = "source";
  public static final String COLUMN_NOTES_NAME = "notes";
  public static final String COLUMN_REQUEST_NAME = "request";
  public static final String COLUMN_UNPROCESSEDRESPONSE_NAME = "unprocessed_response";
  public static final String COLUMN_RESPONSE_NAME = "response";
  public static final String COLUMN_STATUS_NAME = "status";


  private Integer id;

  private Instant creationTime;

  private String source;

  private String notes;

  private String request;

  private String unprocessedResponse;

  private String response;

  private String status;

  private User user;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Log setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public Log setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_SOURCE_NAME, nullable = false, length = 1024)
  public String getSource() {
    return source;
  }

  public Log setSource(String source) {
    this.source = source;
    return this;
  }

  @Lob
  @Column(name = COLUMN_NOTES_NAME)
  public String getNotes() {
    return notes;
  }

  public Log setNotes(String notes) {
    this.notes = notes;
    return this;
  }

  @Lob
  @Column(name = COLUMN_REQUEST_NAME, nullable = false)
  public String getRequest() {
    return request;
  }

  public Log setRequest(String request) {
    this.request = request;
    return this;
  }

  @Lob
  @Column(name = COLUMN_UNPROCESSEDRESPONSE_NAME)
  public String getUnprocessedResponse() {
    return unprocessedResponse;
  }

  public Log setUnprocessedResponse(String unprocessedResponse) {
    this.unprocessedResponse = unprocessedResponse;
    return this;
  }

  @Lob
  @Column(name = COLUMN_RESPONSE_NAME)
  public String getResponse() {
    return response;
  }

  public Log setResponse(String response) {
    this.response = response;
    return this;
  }

  @Lob
  @Column(name = COLUMN_STATUS_NAME)
  public String getStatus() {
    return status;
  }

  public Log setStatus(String status) {
    this.status = status;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User getUser() {
    return user;
  }

  public Log setUser(User user) {
    this.user = user;
    return this;
  }

}