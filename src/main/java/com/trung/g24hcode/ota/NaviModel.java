/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trung.g24hcode.ota;

/**
 *
 * @author nhatn
 */
public class NaviModel {
  private String id;
  private String hostName;
  private String recodeType;
  private String recValue;
  private int mxPreference;
  
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getHostName()
  {
    return this.hostName;
  }
  
  public void setHostName(String hostName)
  {
    this.hostName = hostName;
  }
  
  public String getRecodeType()
  {
    return this.recodeType;
  }
  
  public void setRecodeType(String recodeType)
  {
    this.recodeType = recodeType;
  }
  
  public String getRecValue()
  {
    return this.recValue;
  }
  
  public void setRecValue(String recValue)
  {
    this.recValue = recValue;
  }
  
  public int getMxPreference()
  {
    return this.mxPreference;
  }
  
  public void setMxPreference(int mxPreference)
  {
    this.mxPreference = mxPreference;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("id: ").append(getId()).append("\n")
      .append("host: ").append(getHostName()).append("\n")
      .append("recodeType: ").append(getRecodeType()).append("\n")
      .append("recValue: ").append(getRecValue()).append("\n")
      .append("prio: ").append(getMxPreference()).append("\n");
    return sb.toString();
  }
  
  public String encodeToParams()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("id: ").append(getId()).append("\n")
      .append("host: ").append(getHostName()).append("\n")
      .append("recodeType: ").append(getRecodeType()).append("\n")
      .append("recValue: ").append(getRecValue()).append("\n")
      .append("prio: ").append(getMxPreference()).append("\n");
    return sb.toString();
  }
}
