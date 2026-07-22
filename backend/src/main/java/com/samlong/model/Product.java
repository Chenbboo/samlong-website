package com.samlong.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="products")
public class Product {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @NotBlank(message="车型标题不能为空") @Size(max=255,message="车型标题不能超过255个字符") @Column(nullable=false) private String title;
  @Size(max=20000,message="车型介绍不能超过20000个字符") @Column(columnDefinition="text") private String content;
  @Size(max=5000,message="产品卖点不能超过5000个字符") @Column(columnDefinition="text") private String highlights;
  @DecimalMin(value="0",message="价格不能小于0") private BigDecimal price;
  @Size(max=500,message="图片地址不能超过500个字符") @Pattern(regexp="^$|^https?://.+|^/(uploads|images)/.+",message="图片地址格式不正确") private String imageUrl;
  @Size(max=64,message="角标不能超过64个字符") private String badge="MỚI";
  @Size(max=100) private String rangeText;
  @Size(max=100) private String powerText;
  @Size(max=100) private String speedText;
  @Size(max=100) private String batteryText;
  @Size(max=100) private String chargeTimeText;
  @Size(max=100) private String warrantyText;
  @Size(max=100) private String weightText;
  private Boolean featured=false;
  @NotBlank(message="车型状态不能为空") @Pattern(regexp="ACTIVE|DRAFT",message="车型状态只能是发布或草稿") private String status="DRAFT";
  @NotNull(message="排序不能为空") @Min(value=0,message="排序不能小于0") private Integer sortOrder=0;

  @ElementCollection(fetch=FetchType.EAGER)
  @CollectionTable(name="product_images",joinColumns=@JoinColumn(name="product_id"))
  @OrderColumn(name="sort_order")
  @Column(name="image_url",nullable=false)
  private List<@Size(max=500,message="图库图片地址不能超过500个字符") String> imageUrls=new ArrayList<String>();

  @ElementCollection(fetch=FetchType.EAGER)
  @CollectionTable(name="product_colors",joinColumns=@JoinColumn(name="product_id"))
  @OrderColumn(name="sort_order")
  @Valid private List<ProductColor> colors=new ArrayList<ProductColor>();

  public Long getId(){return id;} public void setId(Long v){id=v;}
  public String getTitle(){return title;} public void setTitle(String v){title=v;}
  public String getContent(){return content;} public void setContent(String v){content=v;}
  public String getHighlights(){return highlights;} public void setHighlights(String v){highlights=v;}
  public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
  public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
  public String getBadge(){return badge;} public void setBadge(String v){badge=v;}
  public String getRangeText(){return rangeText;} public void setRangeText(String v){rangeText=v;}
  public String getPowerText(){return powerText;} public void setPowerText(String v){powerText=v;}
  public String getSpeedText(){return speedText;} public void setSpeedText(String v){speedText=v;}
  public String getBatteryText(){return batteryText;} public void setBatteryText(String v){batteryText=v;}
  public String getChargeTimeText(){return chargeTimeText;} public void setChargeTimeText(String v){chargeTimeText=v;}
  public String getWarrantyText(){return warrantyText;} public void setWarrantyText(String v){warrantyText=v;}
  public String getWeightText(){return weightText;} public void setWeightText(String v){weightText=v;}
  public Boolean getFeatured(){return featured;} public void setFeatured(Boolean v){featured=v;}
  public String getStatus(){return status;} public void setStatus(String v){status=v;}
  public Integer getSortOrder(){return sortOrder;} public void setSortOrder(Integer v){sortOrder=v;}
  public List<String> getImageUrls(){return imageUrls;} public void setImageUrls(List<String> v){imageUrls=v==null?new ArrayList<String>():v;}
  public List<ProductColor> getColors(){return colors;} public void setColors(List<ProductColor> v){colors=v==null?new ArrayList<ProductColor>():v;}
}
