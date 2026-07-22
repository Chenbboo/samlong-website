package com.samlong.model;
import javax.persistence.*;
@Entity @Table(name="admin_users") public class AdminUser {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=64) private String username;
 @Column(nullable=false) private String password;
 @Column(nullable=false,length=32) private String role="ADMIN";
 @Column(nullable=false) private boolean enabled=true;
 public Long getId(){return id;} public void setId(Long v){id=v;} public String getUsername(){return username;} public void setUsername(String v){username=v;} public String getPassword(){return password;} public void setPassword(String v){password=v;} public String getRole(){return role;} public void setRole(String v){role=v;} public boolean isEnabled(){return enabled;} public void setEnabled(boolean v){enabled=v;}
}
