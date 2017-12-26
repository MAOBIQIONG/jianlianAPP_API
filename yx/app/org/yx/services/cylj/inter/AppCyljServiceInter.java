package org.yx.services.cylj.inter;   
import org.yx.common.entity.PageData;  
public interface AppCyljServiceInter { 
	
	/**
	 * 查询链接的信息（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryCyljBYindex(PageData pd) throws Exception;
	
}
