package org.yx.services.zfzs.inter;

import org.yx.common.entity.PageData;

public interface AppEntrustServiceInter {

	/**
	 * 基本信息提交
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String AddEntrust(PageData pd) throws Exception;
	
	
	/**
	 * 首页广告图+第一次分页数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryzfzsIndexInfo(PageData pd) throws Exception;
	
	/**
	 * 查询搜索条件+第一次分页数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querySearchParam(PageData pd) throws Exception;
	
	/**
	 * 查询搜索条件+第一次分页数据（新）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewSearchParam(PageData pd);
	
	/**
	 * 列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querxmlist(PageData pd) throws Exception;
	
	/**
	 * 详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryxmxq(PageData pd) throws Exception;
}
