package com.samlong.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity @Table(name="bookings")
public class Booking {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @NotBlank(message="姓名不能为空") @Size(max=100,message="姓名不能超过100个字符") private String name;
  @NotBlank(message="手机号不能为空") @Pattern(regexp="^(?:\\+?84|0)[0-9 .-]{8,14}$",message="请输入正确的越南手机号") private String phone;
  @Size(max=128,message="城市不能超过128个字符") private String city;
  @Size(max=128,message="区县不能超过128个字符") private String district;
  @NotBlank(message="预约状态不能为空") @Pattern(regexp="NEW|CONTACTED|VISITED|COMPLETED|CANCELLED",message="预约状态不正确") private String status="NEW";
  @Size(max=5000,message="客户备注不能超过5000个字符") @Column(columnDefinition="text") private String notes;
  @Size(max=10000,message="跟进记录不能超过10000个字符") @Column(columnDefinition="text") private String followUp;
  private Boolean unread=true;
  private LocalDateTime createdAt=LocalDateTime.now();
  private LocalDateTime updatedAt=LocalDateTime.now();
  public Long getId(){return id;} public void setId(Long v){id=v;}
  public String getName(){return name;} public void setName(String v){name=v;}
  public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
  public String getCity(){return city;} public void setCity(String v){city=v;}
  public String getDistrict(){return district;} public void setDistrict(String v){district=v;}
  public String getStatus(){return status;} public void setStatus(String v){status=v;}
  public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
  public String getFollowUp(){return followUp;} public void setFollowUp(String v){followUp=v;}
  public Boolean getUnread(){return unread;} public void setUnread(Boolean v){unread=v;}
  public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
  public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime v){updatedAt=v;}
}
