package org.yx.services.wd.inter;

import org.yx.common.entity.PageData;

public interface AppUpgradeServiceInter {

	/**
	 * 修改升级入会申请信息状态，返回查询是否完善信息--未开发
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String updateUpStatus(PageData pd) throws Exception;
	
	/**
	 * 根据id查询升级入会信息--未开发
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryById(PageData pd) throws Exception; 
	
	/**
	 * 查询某个用户某个等级是否已经申请
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String checkIsApply(PageData pd) throws Exception; 
	
	/**
	 * 修改订单支付状态和生成建联卡号
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String editStatusAndCardNo(PageData pd) throws Exception; 
	
}
