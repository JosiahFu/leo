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
  public static final String COLUMN_OPERATION_NAME = "operation";
  public static final String COLUMN_REQUEST_NAME = "request";
  public static final String COLUMN_INITIALRESPONSE_NAME = "initial_response";
  public static final String COLUMN_INITIALRESPONSETIME_NAME = "initial_response_time";
  public static final String COLUMN_FINALRESPONSE_NAME = "final_response";
  public static final String COLUMN_FINALRESPONSETIME_NAME = "final_response_time";
  public static final String COLUMN_STACKTRACE_NAME = "stack_trace";
  public static final String COLUMN_STATUS_NAME = "status";
  public static final String COLUMN_NOTES_NAME = "notes";

  private Integer id;

  private Instant creationTime;

  private String operation;

  private String request;

  private byte[] initialResponse;

  private Instant initialResponseTime;

  private String finalResponse;

  private Instant finalResponseTime;

  private String stackTrace;

  private String status;

  private String notes;

  private UserX userX;

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

  @Column(name = COLUMN_OPERATION_NAME, nullable = false, length = 511)
  public String getOperation() {
    return operation;
  }

  public Log setOperation(String operation) {
    this.operation = operation;
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

  @Column(name = COLUMN_INITIALRESPONSE_NAME)
  public byte[] getInitialResponse() {
    return initialResponse;
  }

  public Log setInitialResponse(byte[] initialResponse) {
    this.initialResponse = initialResponse;
    return this;
  }

  @Column(name = COLUMN_INITIALRESPONSETIME_NAME)
  public Instant getInitialResponseTime() {
    return initialResponseTime;
  }

  public Log setInitialResponseTime(Instant initialResponseTime) {
    this.initialResponseTime = initialResponseTime;
    return this;
  }

  @Lob
  @Column(name = COLUMN_FINALRESPONSE_NAME)
  public String getFinalResponse() {
    return finalResponse;
  }

  public Log setFinalResponse(String finalResponse) {
    this.finalResponse = finalResponse;
    return this;
  }

  @Column(name = COLUMN_FINALRESPONSETIME_NAME)
  public Instant getFinalResponseTime() {
    return finalResponseTime;
  }

  public Log setFinalResponseTime(Instant finalResponseTime) {
    this.finalResponseTime = finalResponseTime;
    return this;
  }

  @Lob
  @Column(name = COLUMN_STACKTRACE_NAME)
  public String getStackTrace() {
    return stackTrace;
  }

  public Log setStackTrace(String stackTrace) {
    this.stackTrace = stackTrace;
    return this;
  }

  @Column(name = COLUMN_STATUS_NAME, nullable = false, length = 7)
  public String getStatus() {
    return status;
  }

  public Log setStatus(String status) {
    this.status = status;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_x_id")
  public UserX getUserX() {
    return userX;
  }

  public Log setUserX(UserX userX) {
    this.userX = userX;
    return this;
  }
}
