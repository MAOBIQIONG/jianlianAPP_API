package org.yx.services.sxy.inter;   
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.entity.PageData;  
public interface AppSxyServiceInter { 
	
	/**
	 * 商学院、推荐列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String querySxy(PageData pd) throws Exception;
	
	 
	/**
	 * 根据ID查询文章详情页
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querySxyDetails(PageData pd) throws Exception;
	/**
	 * 修改文章查看次数
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String addView(PageData pd) throws Exception;
	
	
	/**
	 * 添加评论
	 * /
	 */
	public String addComment(PageData pd) throws Exception;
	
	
	/**
	 * 获取评论列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryComments(PageData pd) throws Exception;
	
	/**
	 * 分享回调（修改分享数量）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String addShare(PageData pd) throws Exception;
	
	/**
	 * 教授列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryProfessors(PageData pd) throws Exception;
	
	
	/**
	 * 点赞/取消点赞
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String editLike(PageData pd) throws Exception;
}
