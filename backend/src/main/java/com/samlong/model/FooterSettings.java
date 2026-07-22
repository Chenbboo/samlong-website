package com.samlong.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "footer_settings")
public class FooterSettings {
  @Id private Long id = 1L;
  @NotBlank @Size(max = 255) private String companyName;
  @Size(max = 64) private String taxCode;
  @Size(max = 500) private String address;
  @Size(max = 64) private String hotline;
  @Email @Size(max = 255) private String email;
  @NotBlank @Size(max = 255) private String copyrightText;
  @Valid
  @OneToMany(mappedBy = "footer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @OrderBy("section asc, sortOrder asc, id asc")
  private List<FooterLink> links = new ArrayList<FooterLink>();

  public Long getId(){return id;} public void setId(Long v){id=v;}
  public String getCompanyName(){return companyName;} public void setCompanyName(String v){companyName=v;}
  public String getTaxCode(){return taxCode;} public void setTaxCode(String v){taxCode=v;}
  public String getAddress(){return address;} public void setAddress(String v){address=v;}
  public String getHotline(){return hotline;} public void setHotline(String v){hotline=v;}
  public String getEmail(){return email;} public void setEmail(String v){email=v;}
  public String getCopyrightText(){return copyrightText;} public void setCopyrightText(String v){copyrightText=v;}
  public List<FooterLink> getLinks(){return links;} public void setLinks(List<FooterLink> v){links=v;}
}
