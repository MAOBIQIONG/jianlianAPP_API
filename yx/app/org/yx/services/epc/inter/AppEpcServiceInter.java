package org.yx.services.epc.inter;   
import org.yx.common.entity.PageData;  
public interface AppEpcServiceInter { 
	
	
	
	/**
	 * 查询交易列表信息(可以根据条件搜索：地区、阶段、类型、项目名称模糊查询)
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryIndexByParam(PageData pd) throws Exception; 
	
}
