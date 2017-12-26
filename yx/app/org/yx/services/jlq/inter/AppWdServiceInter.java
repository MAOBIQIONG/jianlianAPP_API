package org.yx.services.jlq.inter; 

import org.yx.common.entity.PageData;

public interface AppWdServiceInter {   
	
	/**
	 * 查看问答首页列表信息/搜索列表信息(参数：TITLE)/我的提问（参数：USER_ID）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryWdIndexInfo(PageData pd) throws Exception;
	
	/**
	 * 添加问题
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addWenTi(PageData pd) throws Exception;
	
	/**
	 * 根据ID查询问题详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryById(PageData pd) throws Exception;
	/**
	 * 根据问题ID查询问题回答列表（参数：WT_ID）/查询某个回答的列表信息(参数：USER_ID)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryHuiDa(PageData pd) throws Exception;
	
	/**
	 * 根据ID查询回答详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryByHdId(PageData pd) throws Exception; 
	
	/**
	 * 问题添加回答
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addHuiDa(PageData pd) throws Exception;

	/**
	 * 点赞问题/取消赞同
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editWtDz(PageData pd) throws Exception; 
	/**
	 * 关注问题/取消关注
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editWtGz(PageData pd) throws Exception; 
	/**
	 * 查询某个人的问题关注列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryGz(PageData pd) throws Exception; 
	
	/**
	 * 查询某个人回答的问题列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryHdList(PageData pd) throws Exception;  
	
	/**
	 * 查询问答全部标签
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryWdBq(PageData pd) throws Exception; 
}

 

