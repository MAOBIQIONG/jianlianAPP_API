package org.yx.common.entity;

import java.io.Serializable;
import java.util.List;

public class Menu implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String MENU_ID;
	private String MENU_NAME;
	private String MENU_URL;
	private String PARENT_ID;
	private String MENU_ORDER;
	private String MENU_ICON;
	private String MENU_TYPE;
	private String target;
	private Menu parentMenu;
	private List<Menu> subMenu;
	private boolean hasMenu = false;

	public String getMENU_ID() {
		return this.MENU_ID;
	}

	public void setMENU_ID(String mENU_ID) {
		this.MENU_ID = mENU_ID;
	}

	public String getMENU_NAME() {
		return this.MENU_NAME;
	}

	public void setMENU_NAME(String mENU_NAME) {
		this.MENU_NAME = mENU_NAME;
	}

	public String getMENU_URL() {
		return this.MENU_URL;
	}

	public void setMENU_URL(String mENU_URL) {
		this.MENU_URL = mENU_URL;
	}

	public String getPARENT_ID() {
		return this.PARENT_ID;
	}

	public void setPARENT_ID(String pARENT_ID) {
		this.PARENT_ID = pARENT_ID;
	}

	public String getMENU_ORDER() {
		return this.MENU_ORDER;
	}

	public void setMENU_ORDER(String mENU_ORDER) {
		this.MENU_ORDER = mENU_ORDER;
	}

	public Menu getParentMenu() {
		return this.parentMenu;
	}

	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}

	public List<Menu> getSubMenu() {
		return this.subMenu;
	}

	public void setSubMenu(List<Menu> subMenu) {
		this.subMenu = subMenu;
	}

	public boolean isHasMenu() {
		return this.hasMenu;
	}

	public void setHasMenu(boolean hasMenu) {
		this.hasMenu = hasMenu;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getMENU_ICON() {
		return this.MENU_ICON;
	}

	public void setMENU_ICON(String mENU_ICON) {
		this.MENU_ICON = mENU_ICON;
	}

	public String getMENU_TYPE() {
		return this.MENU_TYPE;
	}

	public void setMENU_TYPE(String mENU_TYPE) {
		this.MENU_TYPE = mENU_TYPE;
	}
}

