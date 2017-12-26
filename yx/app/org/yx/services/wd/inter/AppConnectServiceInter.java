package org.yx.services.wd.inter; 

import java.util.List;

import org.yx.common.entity.PageData;

import com.alibaba.fastjson.JSONArray;

public interface AppConnectServiceInter {   
	
	/**
	 * 我的所有人脉列表（可以根据条件查询）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryMyConnects(PageData pd) throws Exception;
	
	/**
	 * 人脉首页--我的人脉列表（5条，无条件查询）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryIndexMyConnects(PageData pd) throws Exception;
	
	/**
	 * 推荐人脉查询（根据行业推荐）,如果没有推荐人脉，
	 * 任意查询5条除了我的人脉和自己以外的其余会员信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryRecommendConnects(PageData pd) throws Exception;
	
	/**
	 * 关注/取消关注人脉
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String editConnect(PageData pd) throws Exception;
	
	/**
	 * 查看他人信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryConnectInfo(PageData pd) throws Exception;
	
	/**
	 * 添加朋友印象
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editConnectInfo(PageData pd) throws Exception;
	
	/**
	 * 查询人脉搜索条件
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryConnectParams(PageData pd) throws Exception;
	
	/**
	 * 根据条件搜索人脉（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryConnects(PageData pd) throws Exception;
	
	/**
	 * 查询城市建联（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryClans(PageData pd) throws Exception;
	
	/**
	 * 查询用户关注的城市建联列表(分页)
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryClanByUid(PageData pd) throws Exception;
	
	/**
	 * 查询城市建联详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryClanInfo(PageData pd) throws Exception;
	
	/**
	 * 查询城市建联关注列表(分页)
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryClanUsers(PageData pd) throws Exception;
	
	/**
	 *查询城市建联成员列表(分页)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryClanMembers(PageData pd) throws Exception;
	
	/**
	 * 关注/取消关注城市建联
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String editClan(PageData pd) throws Exception;
	
	/**
	 * 查询一级城市分类
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryFirstCity(PageData pd) throws Exception;
	
	/**
	 * 查询某个人的城市合伙人申请记录
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryClanApply(PageData pd) throws Exception;
	
}

 

