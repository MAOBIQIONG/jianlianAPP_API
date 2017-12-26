package org.yx.services.jlq.inter; 

import org.yx.common.entity.PageData;

public interface AppMagazineServiceInter {  
	 
	/**
	 * 查看期刊列表信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryQks(PageData pd) throws Exception;
	
	/**
	 * 查询某期期刊的全部页数
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryQkxqById(PageData pd) throws Exception; 
}

 

