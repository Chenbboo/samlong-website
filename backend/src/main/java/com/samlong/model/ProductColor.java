package com.samlong.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Embeddable
public class ProductColor {
  @NotBlank(message="颜色名称不能为空") @Size(max=100,message="颜色名称不能超过100个字符") private String colorName;
  @NotBlank(message="颜色值不能为空") @Pattern(regexp="^#[0-9a-fA-F]{6}$",message="颜色值必须是六位十六进制色值") private String hexCode;
  public ProductColor(){}
  public ProductColor(String colorName,String hexCode){this.colorName=colorName;this.hexCode=hexCode;}
  public String getColorName(){return colorName;} public void setColorName(String v){colorName=v;}
  public String getHexCode(){return hexCode;} public void setHexCode(String v){hexCode=v;}
}
