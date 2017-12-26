package org.yx.common.entity;


public class Page {
	private int pageSize;
	private int curPage;
	private int currentResult;
	private int totalNum;
	private String orderField;
	private String orderDirection;
	private boolean entityOrField;
	private PageData pd = new PageData();

	public Page() {
		try {
			this.pageSize = 30;
		} catch (Exception e) {
			this.pageSize = 15;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	
	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

//	public int getPageCurrent() {
//		//return pageCurrent == 0 ? 1 : pageCurrent;
//		return pageCurrent;
//	}
//
//	public void setPageCurrent(int pageCurrent) {
//		this.pageCurrent = pageCurrent;
//	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public boolean isEntityOrField() {
		return entityOrField;
	}

	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}

	public int getCurrentResult() {
		this.currentResult = ((getCurPage() - 1) * getPageSize());
		if (this.currentResult < 0)
			this.currentResult = 0;
//		this.currentResult = getCurPage();
		return this.currentResult;
	}

	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}

	public PageData getPd() {
		return pd;
	}

	public void setPd(PageData pd) {
		this.pd = pd;
	}

}

/*
 * Location: F:\掌上幼儿园\源码\yzy_web\WEB-INF\classes\ Qualified Name:
 * com.fh.entity.Page JD-Core Version: 0.6.2
 */