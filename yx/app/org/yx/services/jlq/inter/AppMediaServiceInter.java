package org.yx.services.jlq.inter;

import java.util.List;
import org.yx.common.entity.PageData;

public interface AppMediaServiceInter {  
	
	/**
	 * 查看精选首页
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryJxIndexInfo(PageData pd) throws Exception; 
	/**
	 * 查看资讯首页头部轮换大图
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryMediaRotations(PageData pd)throws Exception; 
	 
	/**
	 * 查询资讯首页的新闻列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryMediaIndexByParam(PageData pd) throws Exception;  
	
	/**
	 * 根据ID查询新闻详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryById(PageData pd) throws Exception; 
	
	/**
	 * 根据新闻ID查询新闻评论
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryMediaComments(PageData pd) throws Exception; 
	/**
	 * 新闻添加评论
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addComments(PageData pd) throws Exception; 
	 
	/**
	 * 新闻添加分享
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String addShare(PageData pd) throws Exception;  
	
	/**
	 * 修改新闻查看次数
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String addView(PageData pd) throws Exception;  
	
	/**
	 * 查看视频列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryVedios(PageData pd) throws Exception;  
		 
	//媒体首页新闻 
	public List<PageData> queryHot() throws Exception; 
		
	//根据标题模糊查询
	public List<PageData> findMediaByTitle(PageData pd)throws Exception;  
	
	//根据ID查询新闻详情
	public PageData queryShareById(PageData pd) throws Exception;
}

