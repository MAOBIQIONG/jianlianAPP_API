package org.yx.services.jlq.inter; 

import org.yx.common.entity.PageData;

public interface AppClubServiceInter {  
	 
	/**
	 * 查看俱乐部首页列表信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryIndexInfo(PageData pd) throws Exception;
	
	/**
	 * 查询首页列表热搜词（活动类型）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryActType(PageData pd) throws Exception;
	
	/**
	 * 查询首页列表热搜词（活动类型）(新)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewActType(PageData pd) throws Exception;
	
	/**
	 * 查询会员等级
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryVipLevel(PageData pd) throws Exception;
	
	/**
	 * 查询会员等级(新)
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryNewVipLevel(PageData pd);
	
	/**
	 * 添加活动/修改活动
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editActivity(PageData pd) throws Exception;
	
	/**
	 * 删除活动
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String delActivity(PageData pd) throws Exception;
	
	/**
	 * 根据ID查询活动详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryById(PageData pd) throws Exception;
	
	/**
	 * 报名参加活动
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String toPartAct(PageData pd) throws Exception; 
	
	/**
	 * 活动列表信息(根据条件查询)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryByParam(PageData pd) throws Exception;
	
	/**
	 * 查询我发布的活动
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryListActs(PageData pd) throws Exception;
	
	/**
	 * 查询活动列表的搜索条件
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querySearchParam(PageData pd) throws Exception;
	
	/**
	 * 查询活动列表的搜索条件(新)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewSearchParam(PageData pd) throws Exception;
	
	/**
	 * 查询我参与的活动列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryPartInActs(PageData pd) throws Exception; 
	
	/**
	 * 查询俱乐部列表轮播图
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryClubRoattions(PageData pd) throws Exception;
	
	/**
	 * 添加预订信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addReservations(PageData pd) throws Exception;
	
	/**
	 * 查询预订页面信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryClubVenue(PageData pd) throws Exception;
    
	/**
	 * 根据日期查询该月已预订日期总数
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryDateArr(PageData pd) throws Exception;
	
	/**
	 * 查询活动详情用户修改展示使用
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryActById(PageData pd) throws Exception;
	
	/**
	 * 查询活动详情用户修改展示使用(新)
	 * @param pd
	 * 
	 * @return
	 * @throws Exception
	 */  
	public String queryNewActById(PageData pd) throws Exception;
	
	/**
	 * 根据ID查询预约信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryByYy(PageData pd) throws Exception;
	
	/**
	 * 根据ID查询活动详情参与人
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryByxqId(PageData pd) throws Exception;
	
	/**
	 * 活动参与人列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String querybyCYRlistsID(PageData pd) throws Exception;
	
	/**
	 *  查询活动海报
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryByhb(PageData pd) throws Exception;
	
	/**
	 * 根据ID查询活动详情(参与人)
	 * @param pd
	 * @return
	 * @throws Exception
	 * 时间：2017/9/13
	 */ 
	public String queryBycyr(PageData pd) throws Exception;
}

 

