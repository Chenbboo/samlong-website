package com.samlong.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "footer_links")
public class FooterLink {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @JsonIgnore @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "footer_id", nullable = false) private FooterSettings footer;
  @NotBlank @Pattern(regexp = "PRODUCTS|ABOUT|SUPPORT|SOCIAL") private String section;
  @NotBlank @Size(max = 128) private String label;
  @NotBlank @Size(max = 500) @Pattern(regexp = "^(#.*|/.*|https?://.+|mailto:.+|tel:.+)$") private String url;
  @Size(max = 32) private String icon;
  @NotNull @Min(0) private Integer sortOrder = 0;

  public Long getId(){return id;} public void setId(Long v){id=v;}
  public FooterSettings getFooter(){return footer;} public void setFooter(FooterSettings v){footer=v;}
  public String getSection(){return section;} public void setSection(String v){section=v;}
  public String getLabel(){return label;} public void setLabel(String v){label=v;}
  public String getUrl(){return url;} public void setUrl(String v){url=v;}
  public String getIcon(){return icon;} public void setIcon(String v){icon=v;}
  public Integer getSortOrder(){return sortOrder;} public void setSortOrder(Integer v){sortOrder=v;}
}
