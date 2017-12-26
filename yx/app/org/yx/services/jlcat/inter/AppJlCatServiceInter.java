package org.yx.services.jlcat.inter;

import org.yx.common.entity.PageData;

public interface AppJlCatServiceInter {

	
	/**
	 * 查询会话
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryConversation(PageData pd) throws Exception;
	
	/**
	 * 创建会话
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addConversation(PageData pd) throws Exception;
	
	
	/**
	 * 保存消息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addmessage(PageData pd) throws Exception;
	
	/**
	 * 查询会话列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryConverlist(PageData pd) throws Exception;
	
	/**
	 * 查询消息列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String querymesslist(PageData pd) throws Exception;
	
	/**
	 * 修改接收人状态
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String upJScount(PageData pd) throws Exception;
}
