package org.yx.common.entity;


public class User {
	private String ID;
	private String USERNAME;
	private String PASSWORD;
	private String NAME;
	private String RIGHTS;
	private String ROLE_ID;
	private String LAST_LOGIN;
	private String IP;
	private String STATUS;
	private Role role;
	private Page page; 

	public String getNICKNAME() {
		return NICKNAME;
	}

	public void setNICKNAME(String nICKNAME) {
		NICKNAME = nICKNAME;
	}

	private String NICKNAME; 
 
	public String getID() {
		return this.ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getUSERNAME() {
		return this.USERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		this.USERNAME = uSERNAME;
	}

	public String getPASSWORD() {
		return this.PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		this.PASSWORD = pASSWORD;
	}

	public String getNAME() {
		return this.NAME;
	}

	public void setNAME(String nAME) {
		this.NAME = nAME;
	}

	public String getRIGHTS() {
		return this.RIGHTS;
	}

	public void setRIGHTS(String rIGHTS) {
		this.RIGHTS = rIGHTS;
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String rOLE_ID) {
		this.ROLE_ID = rOLE_ID;
	}

	public String getLAST_LOGIN() {
		return this.LAST_LOGIN;
	}

	public void setLAST_LOGIN(String lAST_LOGIN) {
		this.LAST_LOGIN = lAST_LOGIN;
	}

	public String getIP() {
		return this.IP;
	}

	public void setIP(String iP) {
		this.IP = iP;
	}

	public String getSTATUS() {
		return this.STATUS;
	}

	public void setSTATUS(String sTATUS) {
		this.STATUS = sTATUS;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Page getPage() {
		if (this.page == null)
			this.page = new Page();
		return this.page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
} 