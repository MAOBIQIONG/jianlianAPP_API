package org.yx.services.pay.inter; 

import org.yx.common.entity.PageData;

public interface AppOrdersServiceInter {   
	
	/**
	 * 查看订单列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryOrders(PageData pd) throws Exception;
	
	/**
	 * 查看订单详细信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryOrdersInfo(PageData pd) throws Exception;
	
	/**
	 * 升级入会生成订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String addOrders(PageData pd) throws Exception; 
	
	/**
	 * 申请退费
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String cancleOrders(PageData pd) throws Exception;
	
	/**
	 * 取消订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String toCancle(PageData pd) throws Exception;
	
	/**
	 * 索取发票信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String applyInvoice(PageData pd) throws Exception; 
	 
}

 

