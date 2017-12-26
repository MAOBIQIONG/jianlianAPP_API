 package org.yx.common.entity;
 
 public class Role
 {
   private String ROLE_ID;
   private String ROLE_NAME;
   private String RIGHTS;
   private String PARENT_ID;
   private String ADD_QX;
   private String DEL_QX;
   private String EDIT_QX;
   private String CHA_QX;
   private String QX_ID;
 
   public String getQX_ID()
   {
     return this.QX_ID;
   }
   public void setQX_ID(String qX_ID) {
     this.QX_ID = qX_ID;
   }
   public String getROLE_ID() {
     return this.ROLE_ID;
   }
   public void setROLE_ID(String rOLE_ID) {
     this.ROLE_ID = rOLE_ID;
   }
   public String getROLE_NAME() {
     return this.ROLE_NAME;
   }
   public void setROLE_NAME(String rOLE_NAME) {
     this.ROLE_NAME = rOLE_NAME;
   }
   public String getRIGHTS() {
     return this.RIGHTS;
   }
   public void setRIGHTS(String rIGHTS) {
     this.RIGHTS = rIGHTS;
   }
   public String getPARENT_ID() {
     return this.PARENT_ID;
   }
   public void setPARENT_ID(String pARENT_ID) {
     this.PARENT_ID = pARENT_ID;
   }
   public String getADD_QX() {
     return this.ADD_QX;
   }
   public void setADD_QX(String aDD_QX) {
     this.ADD_QX = aDD_QX;
   }
   public String getDEL_QX() {
     return this.DEL_QX;
   }
   public void setDEL_QX(String dEL_QX) {
     this.DEL_QX = dEL_QX;
   }
   public String getEDIT_QX() {
     return this.EDIT_QX;
   }
   public void setEDIT_QX(String eDIT_QX) {
     this.EDIT_QX = eDIT_QX;
   }
   public String getCHA_QX() {
     return this.CHA_QX;
   }
   public void setCHA_QX(String cHA_QX) {
     this.CHA_QX = cHA_QX;
   }
 }

/* Location:           F:\掌上幼儿园\源码\yzy_web\WEB-INF\classes\
 * Qualified Name:     com.fh.entity.system.Role
 * JD-Core Version:    0.6.2
 */