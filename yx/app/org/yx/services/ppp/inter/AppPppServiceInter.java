package org.yx.services.ppp.inter;   
import org.yx.common.entity.PageData;  
public interface AppPppServiceInter { 
	
	/**
	 * 查询项目类型和项目阶段+第一页列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String querPPPindexlist(PageData pd) throws Exception;
	
	/**
	 * 查询项目类型和项目阶段+第一页列表(新)
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public String queryNewPPPindexlist(PageData pd);
	
	
	/**
	 * 根据项目idPPP项目详细信息 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryById(PageData pd) throws Exception;
	
	/**
	 * 项目列表（根据条件查询,分页）
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public String querPPPslist(PageData pd) throws Exception;
	
	/**查询ppp项目流程进度
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryPppProProce(PageData pd) throws Exception;
	/**
	 * 查询我参与的项目信息（分页)
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public String querPPPMyCYlist(PageData pd) throws Exception;
	
	/**
	 * 查询项目的参与人列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryBidders(PageData pd) throws Exception;
	
	/**
	 * 未登录参与交易
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String addBid(PageData pd) throws Exception;
	
	/**
	 * 未登录取消参与交易
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String delBid(PageData pd) throws Exception;
	
	/**
	 * 判断是否可以加入项目讨论小组 
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String isCanApply(PageData pd) throws Exception;
}
