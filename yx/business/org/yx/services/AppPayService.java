package org.yx.services;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.AppUtil;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;

import com.alibaba.fastjson.JSONArray;

/**
 * 
 * @author zandezhang
 * 店铺信息
 */

@Service("appPayService")
public class AppPayService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
    
	PageData _result = AppUtil.success();
	Object object = null;
	
	
	/**
	 * 新增订单支付信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object save(PageData pd)throws Exception{
		object = dao.save("AppPayMapper.saveOrderPay",pd);
		return object;
	}
	
	/**
	 * 新增订单爱上岗支付信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object asave(PageData pd)throws Exception{
		
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			
		    data.put("ID", UuidUtil.get32UUID());
		    data.put("PAY_TYPE","爱上岗钱包");
			data.put("PAY_STATE","00");//支付成功
			object = dao.save("AppPayMapper.saveOrderPay",data);
			
			//修改新状态为待发货
			data.put("ORDER_STATU","01");//待发货
			object = dao.update("AppOrderMapper.editOrderStatu", data);
			return JSONArray.toJSONString(object);
		}
		return null;
	}
	
}
