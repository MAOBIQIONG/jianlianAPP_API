package org.yx.services.index.inter; 

import org.yx.common.entity.PageData;

public interface AppIndexServiceInter {   
	
	/**
	 * 根据项目名称、活动名称、资讯名称搜索
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProAndActAndMed(PageData pd) throws Exception;
	
	/**
	 * 查询首页所有信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryIndexInfo(PageData pd) throws Exception; 
	
	/**
	 * 查询是否有消息通知信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryHasMessage(PageData pd) throws Exception;  
}

 

