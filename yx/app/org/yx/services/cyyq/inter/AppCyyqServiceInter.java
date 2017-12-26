package org.yx.services.cyyq.inter;   
import org.yx.common.entity.PageData;  
public interface AppCyyqServiceInter {  
	
	/**
	 * 查询招商信息（分页）
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public String queryZslist(PageData pd) throws Exception; 
	
	/**
	 * 根据项目id查询园区详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryyqxq(PageData pd) throws Exception;
	
	/**
	 * 查询入驻园区信息用于修改
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryYqInfo(PageData pd) throws Exception;
	
	/**
	 * 查询园区信息用于修改
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewYqInfo(PageData pd);
	
	/**
	 * 招商信息发布+修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String editZsfb(PageData pd) throws Exception;
	
	/**
	 * 我发布的招商信息（分页）
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querymyfblist(PageData pd) throws Exception; 
	
	/**
	 * 招商信息删除
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String delete(PageData pd) throws Exception;
	
}
