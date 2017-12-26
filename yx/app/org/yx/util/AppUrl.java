package org.yx.util;

import java.util.List;

public class AppUrl {

	private List<String> refuse ;
	
	private List<String> permitted;

	public List<String> getRefuse() {
		return refuse;
	}

	public void setRefuse(List<String> refuse) {
		this.refuse = refuse;
	}

	public List<String> getPermitted() {
		return permitted;
	}

	public void setPermitted(List<String> permitted) {
		this.permitted = permitted;
	}
}
