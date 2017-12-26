package org.yx.services.jlq.inter;


import org.springframework.transaction.annotation.Transactional;
import org.yx.common.entity.PageData;

public interface AppPyqServiceInter {  
	
	/**
	 * 查询朋友圈列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryIndexByParam(PageData pd) throws Exception; 
	/**
	 * 查询附近朋友圈列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryNearByParam(PageData pd) throws Exception; 
	/**
	 * 查询朋友圈列表评论Or回复
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryIndexByParamor(PageData pd) throws Exception;
	/**
	 * 发布朋友圈信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addPyq(PageData pd) throws Exception;   
	 
	/**
	 * 根据ID查询朋友圈详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryById(PageData pd) throws Exception;  
	
	/**
	 * 根据朋友圈ID查询评论
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryPyqPl(PageData pd) throws Exception;  
	/**
	 * 朋友圈添加评论
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String addPyqPl(PageData pd) throws Exception; 
	/**
	 * 点赞/取消点赞
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editPyqDz(PageData pd) throws Exception;  
	
	/**
	 * 删除朋友圈信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String delPyq(PageData pd) throws Exception;  
	
	/**
	 * 朋友圈添加评论OR回复
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String addPyqPlorhf(PageData pd) throws Exception;
	/**
	 * 删除评论
	 */
	public String delPyqpl(PageData pd) throws Exception;
}

