package org.yx.services.cyl.inter;

import org.yx.common.entity.PageData;

public interface AppCompanyServiceInter {

	/**
	 * 查询所有的行业信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCates(PageData pd) throws Exception;
	
	/**
	 * 查询所有的行业信息(新)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewCates(PageData pd) throws Exception;
	
	/**
	 * 根据行业查询公司列表信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCompanysByCate(PageData pd) throws Exception;
	
	/**
	 * 查询公司详细信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCompanyInfo(PageData pd) throws Exception;
	
	/**
	 * 查询首页轮换图片
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryRotations(PageData pd) throws Exception;
	
	/**
	 * 根据条件查询店铺列表信息(店铺名称、行业)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryShops(PageData pd) throws Exception;
	
	/**
	 * 根据参数查询商品信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProducts(PageData pd) throws Exception;
	
	/**
	 * 查询所有行业信息，查出默认第一个行业的店铺信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCate(PageData pd) throws Exception;
	
	/**
	 * 根据参数查询店铺基本信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProductByParam(PageData pd) throws Exception; 
	
	/**
	 * 根据参数查询商品详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProductDetail(PageData pd) throws Exception;
	
	/**
	 * 查询商品信息和店铺统计等信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProductAndShop(PageData pd) throws Exception;   
	
	/**
	 *查询商品规格信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryGuige(PageData pd) throws Exception;
	
	/**
	 *查询商属性信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProperty(PageData pd) throws Exception;
	
	/**
	 * 下单
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String addOrders(PageData pd) throws Exception;
	
	/**
	 * 查询店铺信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryShopInfo(PageData pd) throws Exception;
	
	/**
	 * 查询我的订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryOrdersByUid(PageData pd) throws Exception;
	
	/**
	 * 添加评论
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String AddComments(PageData pd) throws Exception;
	
	/**
	 * 查看评论,分页
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryComments(PageData pd) throws Exception;
	
	/**
	 * 查询评论总数
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCommentsCounts(PageData pd) throws Exception;
	
	/**
	 * 查询是否收藏店铺
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryIsCollect(PageData pd) throws Exception;
	
	/**
	 * 删除订单/取消订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String editOrderStatus(PageData pd) throws Exception;
	
	/**
	 * 下单成功的客服电话
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String ServiceTel(PageData pd) throws Exception;
}
