package org.yx.services.jy.inter;   
import java.util.List;

import org.yx.common.entity.PageData;  
import org.yx.common.utils.EmptyUtil;

import com.alibaba.fastjson.JSONArray;
public interface AppProjectServiceInter { 
	
	/**
	 * 查询项目查询条件列表（地域、项目类型、项目阶段）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String querySearchParam(PageData pd) throws Exception; 
	
	/**
	 * 根据订阅条件搜索项目信息
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryProByDy(PageData pd) throws Exception; 
	
	/**
	 * 查询交易列表信息(可以根据条件搜索：地区、阶段、类型、项目名称模糊查询)
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryIndexByParam(PageData pd) throws Exception; 
	
	/**
	 * 查询会员收藏的项目信息
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryColls(PageData pd) throws Exception; 
	
	/**
	 * 查询会员查看的项目信息
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryCkPros(PageData pd) throws Exception; 
	
	/**
	 * 查询会员参与的项目信息
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryCyPros(PageData pd) throws Exception; 
	
	/**
	 * 查询会员发布的项目信息
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryFbPros(PageData pd) throws Exception;
	
	/**
	 * 根据项目id查询项目详细信息 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryById(PageData pd) throws Exception; 
	
	/**
	 * 参与/取消参与项目的竞标 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String editBidder(PageData pd) throws Exception; 
	
	/**
	 * 加入/取消加入项目讨论小组 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String editXz(PageData pd) throws Exception; 
	
	/**
	 * 判断是否可以加入项目讨论小组 
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String isCanApply(PageData pd) throws Exception;
	
	/**
	 * 修改项目讨论小组成员的权限
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String editXzQx(PageData pd) throws Exception;
	
	/**
	 * 下单/修改项目信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String editPro(PageData pd) throws Exception; 
	
	/**
	 * 删除项目
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String deletePro(PageData pd) throws Exception; 
	
	/**
	 * 添加查看项目信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String AddViews(PageData pd) throws Exception;  
	
	/**
	 * 查询订阅项目地区
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryDyDq(PageData pd) throws Exception; 
	
	/**
	 * 查询订阅项目类型
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryDyLx(PageData pd) throws Exception;  
	
	/**
	 * 查询订阅项目阶段
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryDySche(PageData pd) throws Exception; 
	
	/**
	 * 查询订阅项目行业
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryDyCate(PageData pd) throws Exception;

	/**
	 * 查询项目的所有类型，二级
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryXmLx(PageData pd) throws Exception; 
	
	/**
	 * 查询项目的所有类型和地区，二级
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewXmLx(PageData pd) throws Exception; 
	
	/**
	 * 查询搜索热词
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryHotWords(PageData pd) throws Exception; 
	
	/**
	 * 查询搜索热词(新）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryNewHotWords(PageData pd) throws Exception;
	
	/**
	 * 查询订阅信息显示在页面
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryDys(PageData pd) throws Exception;
	
	/**
	 * 编辑（添加、修改）订阅地区信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String editDyDq(PageData pd) throws Exception;
	
	/**
	 * 编辑（添加、修改）订阅类型信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String editDyLx(PageData pd) throws Exception;
	
	
	/**
	 * 编辑（添加、删除）订阅行业信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String editDyHy(PageData pd) throws Exception;
	
	/**
	 * 编辑（添加、修改）订阅阶段信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String editDyJd(PageData pd) throws Exception;
	
	/**
	 * 参与项目时需验证用户的三证是否已经审核通过，
	 * 已经通过才可以进行参与
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String validCerts(PageData pd) throws Exception;
	
	/**
	 * 查询项目的参与人列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryBidders(PageData pd) throws Exception;
	
	/**
	 * 未登录参与交易
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String addBid(PageData pd) throws Exception;
	
	/**
	 * 未登录取消参与交易
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String delBid(PageData pd) throws Exception;
	
	/**添加项目代发信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String saveXmDf(PageData pd) throws Exception;
	
	/**查询项目流程进度
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryProProce(PageData pd) throws Exception;
	
	/**查询下单页面的客服电话
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryServiceTel(PageData pd) throws Exception;
	
	/**
	 * 查询项目查询条件列表（地域、项目类型、项目阶段）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewSearchParam(PageData pd) throws Exception;
	 
}
