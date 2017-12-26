package org.yx.services.wd.impl; 

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource; 

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.ReturnCode;
import org.yx.common.utils.UuidUtil;
import org.yx.services.wd.inter.AppUserServiceInter;
import org.yx.util.CommonUtil;
import org.yx.util.ConstantUtil;
import org.yx.util.DataConvertUtil;
import org.yx.util.ResultUtil;
import org.yx.util.noticeConfig;
import org.yx.util.noticePushutil;

import com.alibaba.fastjson.JSONArray;

@Service("appUsersService")
public class AppUserService implements AppUserServiceInter{   
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	public static String noticeText12 = noticeConfig.getString("noticeText12");
	Object object = null;
	/**
	 * 查看个人首页信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryUserIndexInfo(PageData pd){
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData clan=new PageData();
			try{
				clan =(PageData) dao.findForObject("AppzUserMapper.findUserByid", data);
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clan).toString();
		}
		
		return null;
	}
	
	/**
	 * 查看个人详细信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryUserInfo(PageData pd) throws Exception{
		
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData clan=null;
			try{
			    clan =(PageData) dao.findForObject("AppzUserMapper.findUsergr", data);
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clan).toString();
		}
		
		return null;
	}
	/**
	 * 查看个人信息用于修改展示
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryUsers(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData user=new PageData();
			try{
			    user =(PageData) dao.findForObject("AppUsersMapper.findByUId", data);
			    
			    //查询省份
			    List<PageData> areas =(List<PageData>) this.dao.findForList("SysAreaMapper.listByPId","0"); //查询全部的省级地区
				for(int i=0;i<areas.size();i++){ 
					List<PageData> child=(List<PageData>) this.dao.findForList("AppClanMapper.queryClans",areas.get(i)); //查询全部的省级地区
					areas.get(i).put("children",child);
				}
				PageData res=new PageData();
				res.put("user",user);
				res.put("areas",areas);
				return JSONArray.toJSONString(res).toString();  
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		} 	
		return null;
	}
	
	/**
	 * 查看个人信息用于修改展示(新)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewUsers(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData user=new PageData();
			try{
				PageData res=new PageData();
			    user =(PageData) dao.findForObject("AppUsersMapper.findByUId", data);
				
			    PageData clans=new PageData();
				//比较版本号
				boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_CLAN, data);
				//获取返回数据
				DataConvertUtil.getRes(version, ConstantUtil.VERSION_CLAN, clans);  
				 
				res.put("clans",clans); 
				res.put("user",user); 
				return JSONArray.toJSONString(res).toString();  
			}catch(Exception e){
				e.printStackTrace();
			}  
		} 	
		return null;
	} 
	
	
	/**
	 * 个人信息修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editUserInfo(PageData pd) throws Exception{
		try {
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
				object = dao.update("AppzUserMapper.updateUser", data);
				if(Integer.valueOf(object.toString()) >= 1){  
 					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();  
	 			}else{
	 				return null;  
	 			}  
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询公司信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCompany(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData clan=new PageData();
			try{
			    clan =(PageData) dao.findForObject("AppzUserMapper.findUsergs", data);
			    //获取行业信息
			    List<PageData> category =(List<PageData>) dao.findForList("AppzUserMapper.findCategory", data);
			    clan.put("CATEGORY", category);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clan).toString();
		}
		
		return null;
	}
	
	/**
	 * 查询公司信息用于修改展示
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCompanys(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData com=new PageData();
			try{
			    com =(PageData) dao.findForObject("AppzUserMapper.findUsergs", data);
			    //获取行业信息
			    List<PageData> cates=(List<PageData>) dao.findForList("AppCategoryMapper.listByPId","0");
				/*for(int i=0;i<cates.size();i++){ 
					List<PageData> child=(List<PageData>) dao.findForList("AppCategoryMapper.listByPId",cates.get(i).getString("value"));
					cates.get(i).put("children",child);
				} */
				List<PageData> areas =(List<PageData>) this.dao.findForList("SysAreaMapper.listByPId","0"); //查询全部的省级地区
			    for(PageData pagedata:areas){
			    	String value=pagedata.getString("value");
			    	if("110000".equals(value)||"120000".equals(value)||"310000".equals(value)||"500000".equals(value)){
			    		List<PageData> childs=new ArrayList<PageData>();
			    		childs.add(pagedata);
			    		pagedata.put("children",childs);
			    	}else{
			    		List<PageData> citys =(List<PageData>) this.dao.findForList("SysAreaMapper.listByPId",pagedata.getString("value")); //查询全部的省级地区
			    		pagedata.put("children",citys);
			    	} 
			    }  
			    PageData res=new PageData();
				res.put("com",com);
				res.put("cates",cates);
				res.put("areas",areas); 
				return JSONArray.toJSONString(res).toString(); 
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
		} 
		return null;
	}
	
	/**
	 * 查询公司信息用于修改展示(新)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewCompanys(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData com=new PageData();
			try{
				PageData res=new PageData();
			    com =(PageData) dao.findForObject("AppzUserMapper.findUsergs", data);
			    
			    PageData cates=new PageData();
				//比较版本号
				boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_CATE, data);
				//获取返回数据
				DataConvertUtil.getRes(version, ConstantUtil.VERSION_CATE, cates);  
				
				PageData areas=new PageData();
				//比较版本号
				boolean version2=CacheHandler.getCompareVersion(ConstantUtil.VERSION_AREA, data);
				//获取返回数据
				DataConvertUtil.getRes(version2, ConstantUtil.VERSION_AREA, areas);   
				
				res.put("com",com); 
				res.put("areas",areas); 
				res.put("cates",cates); 
				return JSONArray.toJSONString(res).toString(); 
			}catch(Exception e){
				e.printStackTrace();
			} 
		} 
		return null;
	}
	
	/**
	 * 公司信息修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editCompany(PageData pd) throws Exception{
		try {
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
			   //修改公司信息
			    object = dao.update("AppzUserMapper.updateUserGs", data);
			    //修改用户职务
			    object = dao.update("AppzUserMapper.updatePosition", data);
			    if(Integer.valueOf(object.toString()) >= 1){  
 					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();  
	 			}else{
	 				return null;  
	 			}  
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 个人头像修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editUserHeadImg(PageData pd) throws Exception{
		try {
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
			    object = dao.update("AppzUserMapper.updateUserHider", data);
			    if(Integer.valueOf(object.toString()) >= 1){  
 					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();  
	 			}else{
	 				return null;  
	 			}  
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *查询个人基本信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryUserBaseInfo(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData clan=new PageData();
			try{
			    clan =(PageData) dao.findForObject("AppzUserMapper.findUserDetail", data);
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clan).toString();
		} 
		return null; 
	}
	
	/**
	 * 修改密码
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editPwd(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		try { 
			if(!EmptyUtil.isNullOrEmpty(data)){
				PageData user =(PageData) dao.findForObject("AppUsersMapper.findById", data);
				if(user!=null){
					String password=new SimpleHash("SHA-1",user.getString("USER_NAME"),data.getString("JIUPASSWORD")).toString();
				    if(password.equals(user.getString("PASSWORD"))){
				    	String pwd=new SimpleHash("SHA-1",user.getString("USER_NAME"),data.getString("PASSWORD")).toString();
				    	data.put("PASSWORD",pwd);
				    	object = dao.update("AppUsersMapper.editPwdAndEmailAndPhone", data);
					    if(Integer.valueOf(object.toString()) >= 1){  
		 					PageData res=new PageData();
							res.put("code","200");
							return JSONArray.toJSONString(res).toString();  
			 			}else{
			 				PageData res=new PageData();
							res.put("code","201");
							res.put("msg","修改密码失败！");
							return JSONArray.toJSONString(res).toString();
			 			}  
				    }else{
				    	PageData res=new PageData();
						res.put("code","201");
						res.put("msg","密码错误！");
						return JSONArray.toJSONString(res).toString();
				    } 
				}else{
					PageData res=new PageData();
					res.put("code","201");
					res.put("msg","用户错误！");
					return JSONArray.toJSONString(res).toString();
				} 
			}
		} catch (Exception e) { 
			e.printStackTrace();
			PageData res=new PageData();
			res.put("code","400");
			res.put("msg","服务器繁忙！");
			return JSONArray.toJSONString(res).toString();
		}
		return null;
	} 
	
	/**
	 * 修改绑定手机号
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editUserPhone(PageData pd) throws Exception{
		try {
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
				PageData user =(PageData) dao.findForObject("AppUsersMapper.queryUserByPhone", data);
				if(user!=null){
					PageData us=new PageData();
					us.put("PHONE",data.getString("newPhone"));
					PageData userinfo =(PageData) dao.findForObject("AppUsersMapper.queryUserByPhone",us);
					if(userinfo!=null){
						PageData res=new PageData();
						res.put("code","202");
						res.put("msg","新手机号已被绑定，请联系客服！");
						return JSONArray.toJSONString(res).toString();
					}else{
						String password=new SimpleHash("SHA-1",user.getString("USER_NAME"),data.getString("PASSWORD")).toString();
					    if(password.equals(user.getString("PASSWORD"))){ 
					    	us.put("USER_ID",user.getString("ID")); 
					    	object = dao.update("AppUsersMapper.editPwdAndEmailAndPhone", us);
						    if(Integer.valueOf(object.toString()) >= 1){  
			 					PageData res=new PageData();
								res.put("code","200");
								return JSONArray.toJSONString(res).toString();  
				 			}else{
				 				PageData res=new PageData();
								res.put("code","201");
								res.put("msg","修改手机号错误！");
								return JSONArray.toJSONString(res).toString();
				 			}  
					    }else{
					    	PageData res=new PageData();
							res.put("code","201");
							res.put("msg","密码错误！");
							return JSONArray.toJSONString(res).toString();
					    } 
					} 
				}else{
					PageData res=new PageData();
					res.put("code","201");
					res.put("msg","用户错误！");
					return JSONArray.toJSONString(res).toString();
				} 
			}
		} catch (Exception e) { 
			e.printStackTrace();
			PageData res=new PageData();
			res.put("code","400");
			res.put("msg","服务器繁忙！");
			return JSONArray.toJSONString(res).toString();
		}
		return null;
	} 
	
	/**
	 * 修改绑定邮箱
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editUserEmail(PageData pd) throws Exception{
		try {
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
				PageData user =(PageData) dao.findForObject("AppUsersMapper.queryByEmail",data);
				if(user!=null){//该邮箱已被绑定
					PageData res=new PageData();
					res.put("code","202");
					res.put("msg","邮箱已被绑定，请联系客服！");
					return JSONArray.toJSONString(res).toString();
				}else{
					object = dao.update("AppUsersMapper.editPwdAndEmailAndPhone", data);
				    if(Integer.valueOf(object.toString()) >= 1){  
	 					PageData res=new PageData();
						res.put("code","200");
						return JSONArray.toJSONString(res).toString();  
		 			}else{
		 				PageData res=new PageData();
						res.put("code","400");
						res.put("msg","修改邮箱失败！");
						return JSONArray.toJSONString(res).toString();
		 			}  
				} 
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询我的积分列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryPoints(PageData pd){
		
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("pageSize",pageSize); 
			}  
			
			PageData de = new PageData();
			try{

				//获取积分总数
				de =(PageData) dao.findForObject("AppzUserMapper.findTotalPoints", data);
				//获取积分列表
				List<PageData> conns =(List<PageData>) dao.findForList("AppConnectsMapper.queryExtendsByParam",data);
				de.put("POLIST", conns);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(de).toString();
		}
	
		return null;
	}
	
	/**
	 * 查询我的订单列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryOrders(PageData pd){ 
		return null;
	}
	
	/**
	 * 查询我的消息列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryMessages(PageData pd) throws Exception{
		return null;
	}

	/**
	 * 添加问题反馈信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editfeedBack(PageData pd) throws Exception{
		return null;
	}
	
	/**
	 * 查询我的权益及证件审核信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryCertCheckInfo(PageData pd) throws Exception{
		return null;
	}
	
	/**
	 * 查询证件信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryCertInfo(PageData pd) throws Exception{
		return null;
	}
	
	/**
	 * 修改证件信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editCertInfo(PageData pd) throws Exception{
		return null;
	}
	
	/**
	 * 升级入会
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String goToVip(PageData pd) throws Exception{
		return null;
	}
	
	/**
	 * 查询法律服务电话
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryLawPhone(PageData pd) throws Exception{
		if(!EmptyUtil.isNullOrEmpty(pd)){  
			List<PageData> lawPhone =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","LawPhone"); //查询法律服务电话
			return JSONArray.toJSONString(lawPhone).toString(); 
		}
		return null; 
	} 
	
	/**
	 * 查询合同信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryContracts(PageData pd) throws Exception{
		if(!EmptyUtil.isNullOrEmpty(pd)){  
			List<PageData> hts =(List<PageData>) this.dao.findForList("JlHtMapper.queryHt",null); //查询合同信息
			return JSONArray.toJSONString(hts).toString(); 
		}
		return null; 
	}

	@Override
	public String orderDetail(PageData pd) throws Exception { 
		return null;
	}

	
	@Override
	public String savePay(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryUserIndexInfoDetail(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveNeeds(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveRESOURCE(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveHonors(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryshCertInfo(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveshCertInfo(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryHonors(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 修改消息已读状态
	 * @param pd
	 * @return
	 * @throws Exception
	 */   
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editMassages(PageData pd) throws Exception {
		try {
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
				int num=0;
				List<PageData> nos=data.getList("nos");  
				for(PageData no:nos){  
					Object ob=dao.update("AppNotiMapper.hasRead",no);
					num+=Integer.valueOf(ob.toString());  
				}
				
				String uid = data.getString("USER_ID");
				PageData param = new PageData();
				param.put("USER_ID", uid);
				//剩余未读数量通知、聊天
				int remainNoticeCount = (Integer) dao.findForObject("AppNotiMapper.queryUnreadNum",param);
				int remainChatCount = (Integer) dao.findForObject("AppNotiMapper.queryUnreadById",param);
				if( remainChatCount > 0 ){
					int updFlg = (Integer) dao.update("AppNotiMapper.updUnread",param);
				    if( updFlg > 0 ){
				    	remainChatCount -= 1;
				    }
				}
				if(nos.size()==num){ 
					PageData res=new PageData();
					res.put("rnc", remainChatCount+remainNoticeCount);
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();  
	 			}else{
	 				return null;  
	 			}  
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询用户的资源信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryResources(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			List<PageData> resources =null;
			try{ 
				//获取用户需求
				resources =(List<PageData>) dao.findForList("AppUserResourceMapper.queryUserResources", data);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(resources).toString();
		} 
		return null; 
	}
	
	/**
	 * 查询用户的需求信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryNeeds(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			List<PageData> needs =null;
			try{ 
				//获取用户需求
				needs =(List<PageData>) dao.findForList("AppUserNeedsMapper.queryUserNeeds", data);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(needs).toString();
		} 
		return null; 
	}

	/**
	 * 第三方登录绑定手机号
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String thirdBindUser(PageData pd) throws Exception {
		PageData data = pd.getObject("params");
		if(EmptyUtil.isNullOrEmpty(data)) 
			return null; 
		List<PageData> users =(List<PageData>) dao.findForList("AppUsersMapper.queryUserByPhone", data); 
		//手机号没有注册过
		if(users==null||users.size()==0){
			boolean result1 =dao.update_("AppUsersMapper.editPwdAndEmailAndPhone", data);
			if(result1){
				return ResultUtil.successMsg("绑定手机号成功！"); 
			}else{
				return ResultUtil.failMsg("绑定手机号失败！");  
			}  
		}
		//手机号已经注册且不止一条记录
		if(users.size()>1){
			return ResultUtil.failMsg("要绑定的手机号重复，请联系客服！"); 
		}
		//手机号已注册且只有一条记录
		PageData uu=users.get(0);
		PageData user =(PageData) dao.findForObject("AppUsersMapper.findById", data); 
		reset(uu,user,new String[]{"POSITION","COMPANY_ID","PHONE","IMG","CARD_NO","SUMMARY","WEBCHAT","QQ","CLAN_ID"});
		uu.put("NICK_NAME",uu.getString("REAL_NAME"));
		uu.put("USER_ID",uu.get("ID")); 
		boolean result1=dao.update_("AppUsersMapper.edit",uu);
		if(!result1){
			return ResultUtil.failMsg("绑定手机号失败！");  
		}
		boolean result2 =dao.update_("AppUsersMapper.deleteUser",data);
		if(!result2){
			return ResultUtil.failMsg("绑定手机号失败！"); 
		} 
		PageData tpd=new PageData();
		tpd.put("userid", uu.getString("ID")); 
		 //判断用户是否是第一次登录 是新增token信息，否则更新token信息 
		PageData utk=(PageData)dao.findForObject("AppUsertokenMapper.checkToken", uu); 
		// 用户token信息
		PageData topd = CommonUtil.getToken(users.get(0).get("ID"));
		boolean editToken=false;
		if (utk != null) {
			editToken=dao.update_("AppUsertokenMapper.editToken",topd); 
		} else {
			editToken=dao.save_("AppUsertokenMapper.saveToken",topd); 
		} 
		if(!editToken){
			return ResultUtil.failMsg("绑定手机号失败！");   
		}
		PageData userinfo= (PageData)dao.findForObject("AppUsersMapper.thirdLogin",users.get(0));
		return ResultUtil.successMsg("绑定手机号成功！",topd.get("token").toString(),userinfo); 
	}
	
	/**
	 * 添加发票/修改发票信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addInvoice(PageData pd) throws Exception{
		try { 
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){ 
				PageData orders= (PageData)dao.findForObject("AppInvoiceMapper.queryInvoiceAndOrder",data);
				if(orders.getString("INID")!=null&&!"".equals(orders.getString("INID"))){//已经存在发票信息
					data.put("CREATE_DATE",new Date()); 
					Object rel=dao.update("AppInvoiceMapper.edit",data); 
					if(Integer.parseInt(rel.toString())>=1){
						data.put("STATUS","07");
						boolean rest =dao.update_("AppOrdersMapper.editOrdersStaus",data); 
						if(rest){
							PageData res=new PageData();
							res.put("code","200");
							return JSONArray.toJSONString(res).toString(); 
						}else{
							PageData res=new PageData();
							res.put("code","400");
							return JSONArray.toJSONString(res).toString();  
						} 
					}else{
						PageData res=new PageData();
						res.put("code","400");
						return JSONArray.toJSONString(res).toString();  
					} 
				}else{//新增发票信息  
					data.put("ID",data.getString("INVOICE_ID"));
					data.put("CREATE_DATE",new Date()); 
					Object rele=dao.save("AppInvoiceMapper.save",data); 
					if(Integer.parseInt(rele.toString())>=1){ 
						data.put("STATUS","07");
						boolean rest =dao.update_("AppOrdersMapper.editOrdersStaus",data); 
						if(rest){
							PageData res=new PageData();
							res.put("code","200");
							return JSONArray.toJSONString(res).toString(); 
						}else{
							PageData res=new PageData();
							res.put("code","400");
							return JSONArray.toJSONString(res).toString();  
						}   
					}else{
						PageData res=new PageData();
						res.put("code","400");
						return JSONArray.toJSONString(res).toString();  
					}  
				} 
 			}else{
 				return null;  
 			}  
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	} 
	
	/**
	 * 删除发票信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String delInvoice(PageData pd) throws Exception{
		try { 
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){   
				Object rel=dao.delete("AppInvoiceMapper.delete",data); 
				if(Integer.parseInt(rel.toString())>=1){ 
					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();  
				}else{
					PageData res=new PageData();
					res.put("code","400");
					return JSONArray.toJSONString(res).toString();  
				} 
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 添加城市合伙人申请信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addClanApplicant(PageData pd) throws Exception {
		try {
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){   
				data.put("ID",UuidUtil.get32UUID());
				data.put("APPLY_DATE",new Date());
				data.put("STATUS","01");
				Object rel=dao.save("AppClanApplicantMapper.save",data); 
				if(Integer.parseInt(rel.toString())>=1){
					//获取notice.properties noticeText10内容。
			        String vv3=new String(noticeText12.getBytes("ISO-8859-1"),"UTF-8");
				    String notice1=vv3;
			        noticePushutil notutil=new noticePushutil();
			        notutil.toNotice(notice1); 
					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();  
				}else{
					PageData res=new PageData();
					res.put("code","400");
					return JSONArray.toJSONString(res).toString();  
				}  
 			}else{
 				return null;  
 			}  
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据订单ID查询发票信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryInvoiceByOid(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			try{ 
				//获取用户需求
				PageData invoices = (PageData) dao.findForObject("AppInvoiceMapper.queryInvoiceByOid",data);
				if( invoices != null ){
					PageData res=new PageData();
					res.put("code","200");
			    	res.put("invoice",invoices);
			    	return JSONArray.toJSONString(res).toString();
			    }
			}catch(Exception e){
				e.printStackTrace();
			} 
		} 
		return null; 
	}

	@Override
	public String querySjrh(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String editUCInfo(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryVacancy(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String editOrderPayType(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 入会升级修改所属建联、所属行业、公司名称、手机号、名片
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Override
	public String editUserxinxi(PageData pd) throws Exception{
		return null;
	}
	
	/**
	 * 查询某个用户某个等级是否已经存在订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryOrdersByUid(PageData pd) throws Exception{
		try {
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){    
				PageData orders = (PageData) dao.findForObject("AppOrdersMapper.queryHasOrders",data);
				PageData res=new PageData();
				res.put("code","200");
		    	res.put("orders",orders);
		    	return JSONArray.toJSONString(res).toString(); 
 			}else{
 				return null;  
 			}  
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}
	
	public void reset(Map a ,Map b,String[] keys){ 
		for(int i=0;i<keys.length;i++){
			if(a.get(keys[i])==null||"".equals(a.get(keys[i]))){
				Object ob=b.get(keys[i]);
				if(ob!=null&&!"".equals(ob)){
					a.put(keys[i],ob.toString());
				}else{
					a.put(keys[i],"");
				} 
			 } 
		} 
	}
	
	/**
	 * 查询用户的名片、手机号、公司名称、所属建联、所属行业
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String querySjxx(PageData pd) throws Exception{
		PageData userdata=new PageData();
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData conns = new PageData();
			try{
				conns =(PageData) dao.findForObject("AppzUserMapper.queryusxx",data);
				//获取行业信息
			    List<PageData> cates=(List<PageData>) dao.findForList("AppCategoryMapper.listByPId","0");
			    //查询省份
			    List<PageData> areas =(List<PageData>) this.dao.findForList("SysAreaMapper.listByPId","0"); //查询全部的省级地区
				for(int i=0;i<areas.size();i++){ 
					List<PageData> child=(List<PageData>) this.dao.findForList("AppClanMapper.queryClans",areas.get(i)); //查询全部的省级地区
					areas.get(i).put("children",child);
				}
			    userdata.put("conns", conns);
			    userdata.put("cates", cates);
			    userdata.put("areas", areas);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(userdata).toString();
		}
		
		return null;
	}
	
	/**
	 * 查询用户的名片、手机号、公司名称、所属建联、所属行业(新）
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryNewSjxx(PageData pd) throws Exception{
		PageData userdata=new PageData();
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData conns = new PageData();
			try{
				conns =(PageData) dao.findForObject("AppzUserMapper.queryusxx",data); 
				
				PageData cates=new PageData();
				//比较版本号
				boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_CATE, data);
				//获取返回数据
				DataConvertUtil.getRes(version, ConstantUtil.VERSION_CATE, cates);  
				
				PageData clans=new PageData();
				//比较版本号
				boolean version2=CacheHandler.getCompareVersion(ConstantUtil.VERSION_CLAN, data);
				//获取返回数据
				DataConvertUtil.getRes(version2, ConstantUtil.VERSION_CLAN, clans);  
				
			    userdata.put("clans", clans); 
			    userdata.put("cates", cates); 
			    userdata.put("conns", conns); 
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(userdata).toString();
		}
		
		return null;
	}

	@Override
	public String queryNewSjrh(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryUserBaseinfo(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 第三方第一次登录绑定手机号
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String firstBindUser(PageData pd) throws Exception {
		PageData data = pd.getObject("params");
		if(EmptyUtil.isNullOrEmpty(data)) 
			return null; 
		List<PageData> users =(List<PageData>) dao.findForList("AppUsersMapper.queryUserByPhone", data); 
		//手机号没有注册过
		if(users==null||users.size()==0){ 
			return ResultUtil.failMsg("该账号不存在！");   
		}
		//手机号已经注册且不止一条记录
		if(users.size()>1){
			return ResultUtil.failMsg("要绑定的手机号重复，请联系客服！"); 
		}
		//手机号已注册且只有一条记录
		PageData uu=users.get(0);
		PageData user =(PageData) dao.findForObject("AppUsersMapper.findById", data); 
		if(user!=null&&!"".equals(user)){
			reset(uu,user,new String[]{"POSITION","COMPANY_ID","PHONE","IMG","CARD_NO","SUMMARY","WEBCHAT","QQ","CLAN_ID"});
		}else{
			return ResultUtil.failMsg("绑定手机号失败！"); 
		} 
		uu.put("NICK_NAME",uu.getString("REAL_NAME"));
		uu.put("USER_ID",uu.get("ID")); 
		boolean result1=dao.update_("AppUsersMapper.edit",uu);
		if(!result1){
			return ResultUtil.failMsg("绑定手机号失败！");  
		}
		boolean result2 =dao.update_("AppUsersMapper.deleteUser",data);
		if(!result2){
			return ResultUtil.failMsg("绑定手机号失败！"); 
		}
		PageData tpd=new PageData();
		tpd.put("userid", uu.getString("ID")); 
		//判断用户是否是第一次登录 是新增token信息，否则更新token信息 
		PageData utk=(PageData)dao.findForObject("AppUsertokenMapper.checkToken", tpd); 
		// 用户token信息
		PageData topd = CommonUtil.getToken(users.get(0).get("ID"));
		boolean editToken=false;
		if (utk != null) {
			editToken=dao.update_("AppUsertokenMapper.editToken",topd); 
		} else {
			editToken=dao.save_("AppUsertokenMapper.saveToken",topd); 
		} 
		if(!editToken){
			return ResultUtil.failMsg("绑定手机号失败！");   
		}
		PageData userinfo= (PageData)dao.findForObject("AppUsersMapper.thirdLogin",users.get(0));
		return ResultUtil.successMsg("绑定手机号成功！",topd.get("token").toString(),userinfo); 
	} 
}

 

