package com.samlong.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="audit_logs", indexes={@Index(name="idx_audit_created_at", columnList="createdAt")})
public class AuditLog {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,length=64) private String username;
  @Column(nullable=false,length=32) private String action;
  @Column(nullable=false,length=32) private String resourceType;
  private Long resourceId;
  @Column(length=500) private String summary;
  @Column(nullable=false) private LocalDateTime createdAt=LocalDateTime.now();

  public Long getId(){return id;} public void setId(Long v){id=v;}
  public String getUsername(){return username;} public void setUsername(String v){username=v;}
  public String getAction(){return action;} public void setAction(String v){action=v;}
  public String getResourceType(){return resourceType;} public void setResourceType(String v){resourceType=v;}
  public Long getResourceId(){return resourceId;} public void setResourceId(Long v){resourceId=v;}
  public String getSummary(){return summary;} public void setSummary(String v){summary=v;}
  public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
