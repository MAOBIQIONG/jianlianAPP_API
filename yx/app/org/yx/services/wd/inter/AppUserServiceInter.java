package org.yx.services.wd.inter; 

import org.springframework.transaction.annotation.Transactional;
import org.yx.common.entity.PageData;

public interface AppUserServiceInter {   
	
	/**
	 * 查看个人首页信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryUserIndexInfo(PageData pd) throws Exception;
	/**
	 * 查看个人首页详细信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryUserIndexInfoDetail(PageData pd) throws Exception;
	
	/**
	 * 查看个人详细信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryUserInfo(PageData pd) throws Exception;
	
	/**
	 * 查看个人信息用于修改展示
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryUsers(PageData pd) throws Exception;
	
	/**
	 * 查看个人信息用于修改展示(新)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewUsers(PageData pd) throws Exception;
	
	/**
	 * 个人信息修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editUserInfo(PageData pd) throws Exception;
	
	/**
	 * 查询公司信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCompany(PageData pd) throws Exception;
	
	/**
	 * 查询公司信息用于修改展示
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCompanys(PageData pd) throws Exception;
	
	/**
	 * 查询公司信息用于修改展示(新)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewCompanys(PageData pd) throws Exception;
	
	/**
	 * 公司信息修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editCompany(PageData pd) throws Exception;
	
	/**
	 * 个人头像修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editUserHeadImg(PageData pd) throws Exception;
	
	/**
	 *查询个人基本信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryUserBaseInfo(PageData pd) throws Exception;
	
	/**
	 * 查询我的积分列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryPoints(PageData pd) throws Exception;
	
	/**
	 * 查询我的订单列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryOrders(PageData pd) throws Exception;
	
	/**
	 * 查询用户订单详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String orderDetail(PageData pd) throws Exception;
	
	/**
	 * 新增支付信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String savePay(PageData pd) throws Exception;
	
	/**
	 * 查询我的消息列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryMessages(PageData pd) throws Exception;

	/**
	 * 添加问题反馈信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editfeedBack(PageData pd) throws Exception;
	
	/**
	 * 查询我的权益及证件审核信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryCertCheckInfo(PageData pd) throws Exception;
	
	/**
	 * 查询证件信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryCertInfo(PageData pd) throws Exception;
	
	/**
	 * 修改证件信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editCertInfo(PageData pd) throws Exception;
	
	/**
	 * 发布需求
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String saveNeeds(PageData pd) throws Exception;
	
	/**
	 * 发布资源
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String saveRESOURCE(PageData pd) throws Exception;
	
	/**
	 * 查询用户的资源信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryResources(PageData pd) throws Exception;
	
	/**
	 * 查询用户的需求信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryNeeds(PageData pd) throws Exception;
	
	/**
	 * 查询用户的荣誉信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryHonors(PageData pd) throws Exception;
	
	/**
	 * 添加荣誉
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String saveHonors(PageData pd) throws Exception;
	
	/**
	 * 升级入会
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String goToVip(PageData pd) throws Exception;
	
	/**
	 * 查询法律服务电话
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryLawPhone(PageData pd) throws Exception;
	
	/**
	 * 查询合同信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryContracts(PageData pd) throws Exception;
	
	/**
	 * 查询证件审核信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryshCertInfo(PageData pd) throws Exception;
	
	
	/**
	 * 提交证件审核信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String saveshCertInfo(PageData pd) throws Exception;
	
	/**
	 * 修改密码
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editPwd(PageData pd) throws Exception;
	
	/**
	 * 修改绑定手机号
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editUserPhone(PageData pd) throws Exception;
	
	/**
	 * 修改绑定邮箱
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editUserEmail(PageData pd) throws Exception;
	
	/**
	 * 修改消息已读状态
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editMassages(PageData pd) throws Exception;
	
	/**
	 * 第三方登录绑定手机号
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String thirdBindUser(PageData pd) throws Exception;
	
	/**
	 * 添加城市合伙人申请信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addClanApplicant(PageData pd) throws Exception;
	
	/**
	 * 添加发票信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addInvoice(PageData pd) throws Exception;

	
	/**
	 * 删除发票信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String delInvoice(PageData pd) throws Exception;

	
	/**
	 * 根据订单ID查询发票信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryInvoiceByOid(PageData pd) throws Exception;


	
	/**
	 *查询升级入会信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querySjrh(PageData pd) throws Exception;
	
	/**
	 *查询升级入会信息(新）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewSjrh(PageData pd) throws Exception;
	
	/**
	 * 入会升级修改所属建联、所属行业
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editUCInfo(PageData pd) throws Exception;
	
	/**
	 * 查询对应等级空缺人数
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryVacancy(PageData pd) throws Exception;
	
	/**
	 * 修改订单状态
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editOrderPayType(PageData pd) throws Exception;
	
	/**
	 * 查询某个用户某个等级是否已经存在订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryOrdersByUid(PageData pd) throws Exception;

	
	/**
	 * 查询用户的名片、手机号、公司名称、所属建联、所属行业
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String querySjxx(PageData pd) throws Exception;
	
	/**
	 * 查询用户的名片、手机号、公司名称、所属建联、所属行业(新）
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryNewSjxx(PageData pd) throws Exception;
	
	/**
	 * 入会升级修改所属建联、所属行业、公司名称、手机号、名片
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editUserxinxi(PageData pd) throws Exception;
	
	/**
	 * 查询会员的卡号、等级等信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryUserBaseinfo(PageData pd) throws Exception; 
	
	/**
	 * 第三方第一次登录绑定手机号
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String firstBindUser(PageData pd) throws Exception;
}

 

