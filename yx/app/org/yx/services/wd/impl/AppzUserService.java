package org.yx.services.wd.impl; 

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource; 

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData;
import org.yx.common.utils.DateUtil;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;
import org.yx.services.wd.inter.AppUserServiceInter; 
import org.yx.util.CommonUtil;
import org.yx.util.ConstantUtil;
import org.yx.util.DataConvertUtil;
import org.yx.util.PushUtil;
import org.yx.util.ResultUtil;
import org.yx.util.noticeConfig;
import org.yx.util.noticePushutil;

import com.alibaba.fastjson.JSONArray;   
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
@Service("appzUserService")
public class AppzUserService implements AppUserServiceInter{   
	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	public static String noticeText4 = noticeConfig.getString("noticeText4");
	public static String noticeText5 = noticeConfig.getString("noticeText5");
	public static String noticeText14 = noticeConfig.getString("noticeText14");
	
	
	Logger log=Logger.getLogger(this.getClass()); 
    
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
			PageData clan=null;
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
	 * 查看个人首页详细信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryUserIndexInfoDetail(PageData pd){
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData clan=null;
			try{
				if(data.get("USER_ID")!=null&&!"".equals(data.get("USER_ID"))){
					clan =(PageData) dao.findForObject("AppzUserMapper.findUserByidDetail", data);
				}else{
					clan=new PageData();
					clan.put("REAL_NAME","匿名用户");
				} 
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
				PageData user =(PageData) dao.findForObject("AppUsersMapper.findById", data); 
				String clanId=user.getString("CLAN_ID");
				object = dao.update("AppzUserMapper.updateUser", data);
				Object re1=null;
				Object re2=null;
				if(Integer.valueOf(object.toString()) >= 1){ 
					if(clanId!=null&&!"".equals(clanId)){
						PageData newClan=new PageData();
						newClan.put("CLAN_ID",clanId);
						PageData clan =(PageData) dao.findForObject("AppClanMapper.queryById", newClan);  
		 				if(clan!=null){
		 					int  count=Integer.parseInt(clan.get("MEMBER_COUNT")+"");  
			 				if(count>1){ 
			 					newClan.put("MEMBER_COUNT",count-1);  
							}else{ 
								newClan.put("MEMBER_COUNT",0);  
							} 
			 				re1=this.dao.update("AppClanMapper.updateCounts", newClan); 
		 				}else{
		 					re1=1;
		 				} 
					}
					String clan_id=data.getString("CLAN_ID");
					if(clan_id!=null&&!"".equals(clan_id)){   
		 				PageData clan =(PageData) dao.findForObject("AppClanMapper.queryById", data);  
		 				int  count=Integer.parseInt(clan.get("MEMBER_COUNT")+"");  
		 				data.put("MEMBER_COUNT",count+1);  
						re2=this.dao.update("AppClanMapper.updateCounts", data); 
					}  
					if(Integer.valueOf(re1.toString()) >= 1&&Integer.valueOf(re2.toString()) >= 1){
						PageData res=new PageData();
						res.put("code","200");
						return JSONArray.toJSONString(res).toString(); 
					}else{
						return null;  
					} 
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
				PageData com=(PageData) dao.findForObject("AppzUserMapper.queryComById", data);
			    if(com.getString("COMPANY_ID")!=null&&!"".equals(com.getString("COMPANY_ID"))){
			    	//修改公司信息
				    object = dao.update("AppzUserMapper.updateUserGs", data);
				    data.put("COMPANY_ID",com.getString("COMPANY_ID"));
				    //修改用户职务
				    object = dao.update("AppzUserMapper.updatePosition", data);
			    }else{
			    	//新增公司信息
			    	data.put("ID", UuidUtil.get32UUID()); 
				    object = dao.save("AppzUserMapper.saveCompany", data);
				    data.put("COMPANY_ID",data.getString("ID"));
				    //修改用户职务
				    object = dao.update("AppzUserMapper.updatePosition", data);
			    } 
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
			if(data.getString("USER_ID")==null||"".equals(data.getString("USER_ID"))){
				return null;
			}
			PageData clan=null;
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
	 *查询升级入会信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querySjrh(PageData pd) throws Exception{
		PageData res=new PageData();
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData clan=new PageData();
			List<PageData> upgrades=null;
			Object vacancy=null;
			PageData complete=null;
			try{
				//用户信息
				if( data.get("USER_ID") != null ){
					clan =(PageData) dao.findForObject("AppzUserMapper.findSjrh", data);
					//查询当前用户以前申请的信息（不包括审核通过的记录)
				    upgrades=(List<PageData>) dao.findForList("AppUpgradeMapper.queryByUid", data);
				    for(PageData grade:upgrades){
				    	if("00".equals(grade.getString("STATUS"))){
				    		PageData order =(PageData) dao.findForObject("AppOrdersMapper.findByOid", grade);
				    		grade.put("ID", order.getString("ID")); 
				    		grade.put("STATUS_NAME", order.getString("STATUS_NAME"));
				    	} 
				    }
				    //当前用户所属建联、行业对应的会员等级空缺人数
				    vacancy =(PageData) dao.findForObject("AppUsersMapper.queryVacancyByUid", data);
				    //用户信息是否完善
				    complete =(PageData) dao.findForObject("AppzUserMapper.queryusxx",data);
					if(upgrades!=null&&upgrades.size()>0){
						 if(complete.get("PHONE")!=null&&!"".equals(complete.get("PHONE"))&&complete.get("COMPANY_NAME")!=null&&!"".equals(complete.get("COMPANY_NAME"))&&complete.get("CLID")!=null&&!"".equals(complete.get("CLID"))&&complete.get("HYID")!=null&&!"".equals(complete.get("HYID"))&&complete.get("IMG_PATH")!=null&&!"".equals(complete.get("IMG_PATH"))){
								res.put("isComplete",1);//信息已完善
							}else{
								res.put("isComplete",0);//信息未完善
							} 
					}else{
						res.put("isComplete",1);//信息已完善
					} 
				}
			    //查询省份
			    List<PageData> areas =(List<PageData>) this.dao.findForList("SysAreaMapper.listByPId","0"); //查询全部的省级地区
				for(int i=0;i<areas.size();i++){ 
					List<PageData> child=(List<PageData>) this.dao.findForList("AppClanMapper.queryClans",areas.get(i)); //查询全部的省级地区
					areas.get(i).put("children",child);
				}
				//获取行业信息
			    List<PageData> cates=(List<PageData>) dao.findForList("AppCategoryMapper.listByPId","0");
				res.put("clan",clan);
				res.put("vacancy",vacancy);
				res.put("areas",areas);
				res.put("cates",cates);
				res.put("upgrades",upgrades);  
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(res).toString();  
		}
		
		return null;
		
		
	}
	
	/**
	 *查询升级入会信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewSjrh(PageData pd) throws Exception{
		long startTime=System.currentTimeMillis();
		PageData res=new PageData();
		PageData data = pd.getObject("params");
		log.info("Service:【appzUserService】 方法：【queryNewSjrh】 开始消耗时间为："+(System.currentTimeMillis()-startTime)+"毫秒,参数为："+JSONArray.toJSONString(data));
		if(!EmptyUtil.isNullOrEmpty(data)){  
			//用户信息
			if( data.get("USER_ID") != null ){
				PageData clan =(PageData) dao.findForObject("AppzUserMapper.findSjrh", data);
				//查询当前用户以前申请的信息（不包括审核通过的记录)
				List<PageData> upgrades=(List<PageData>) dao.findForList("AppUpgradeMapper.queryByUid", data);
				if(upgrades!=null&&upgrades.size()>0){
					for(PageData grade:upgrades){
				    	if("00".equals(grade.getString("STATUS"))){
				    		PageData order =(PageData) dao.findForObject("AppOrdersMapper.findByOid", grade);
				    		if(order!=null){
				    			grade.put("ID", order.getString("ID")); 
					    		grade.put("STATUS_NAME", order.getString("STATUS_NAME"));
				    		}else{
				    			/*grade.put("ID", ); 
					    		grade.put("STATUS_NAME", order.getString("STATUS_NAME"));*/
				    		} 
				    	} 
				    }
				} 
			    long fourTime=System.currentTimeMillis();
			    fourTime=fourTime-startTime;
				log.info("Service:【appzUserService】 方法：【queryNewSjrh】 fourTime消耗时间为："+fourTime+"毫秒！攻击："+fourTime/1000+"秒！");
			    //当前用户所属建联、行业对应的会员等级空缺人数
			    Object vacancy =(PageData) dao.findForObject("AppUsersMapper.queryVacancyByUid", data);
			    //用户信息是否完善
			    PageData complete =(PageData) dao.findForObject("AppzUserMapper.queryusxx",data);
				if(upgrades!=null&&upgrades.size()>0){
					if(complete==null||"".equals(complete)){
						res.put("isComplete",0);//信息未完善
					}else{
						if(complete.get("PHONE")!=null&&!"".equals(complete.get("PHONE"))&&complete.get("COMPANY_NAME")!=null&&!"".equals(complete.get("COMPANY_NAME"))&&complete.get("CLID")!=null&&!"".equals(complete.get("CLID"))&&complete.get("HYID")!=null&&!"".equals(complete.get("HYID"))&&complete.get("IMG_PATH")!=null&&!"".equals(complete.get("IMG_PATH"))){
							res.put("isComplete",1);//信息已完善
						}else{
							res.put("isComplete",0);//信息未完善
						} 
					} 
				}else{
					res.put("isComplete",1);//信息已完善
				} 
				res.put("clan",clan);
				res.put("vacancy",vacancy);
				res.put("upgrades",upgrades);   
			} 
			long secTime=System.currentTimeMillis();
			secTime=secTime-startTime;
			log.info("Service:【appzUserService】 方法：【queryNewSjrh】 secTime消耗时间为："+secTime+"毫秒！攻击："+secTime/1000+"秒！");
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
			res.put("clans",clans);
			res.put("cates",cates);
			long thirdTime=System.currentTimeMillis();
			thirdTime=thirdTime-startTime;
			log.info("Service:【appzUserService】 方法：【queryNewSjrh】 thirdTime消耗时间为："+thirdTime+"毫秒！攻击："+thirdTime/1000+"秒！");
			return JSONArray.toJSONString(res).toString();  
		}
		long lastTime=System.currentTimeMillis();
		lastTime=lastTime-startTime;
		log.info("Service:【appzUserService】 方法：【queryNewSjrh】 参数为空lastTime消耗时间为："+lastTime+"毫秒！攻击："+lastTime/1000+"秒！");
		return null; 
	}
	
	/**
	 * 修改用户的基本信息（手机号、密码、绑定邮箱）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editUserBaseInfo(PageData pd) throws Exception{
		/*try {
			PageData data = pd.getObject("params");
			String pwd =data.getString("PASSWORD");
			
			if(pwd != null && !"".equals(pwd)){
				pwd = 
			}
			
			if(!EmptyUtil.isNullOrEmpty(data)){
			    object = dao.update("AppzUserMapper.updateDetail", data);
			    if(Integer.parseInt(object.toString()) > 0){
				    return JSONObject.fromObject(object).toString();
			    }
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
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
				List<PageData> conns =(List<PageData>) dao.findForList("AppzUserMapper.findPoints",data);
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
			
			List<PageData> conns = null;
			try{

				conns =(List<PageData>) dao.findForList("AppzUserMapper.findOrders",data);
				
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(conns).toString();
		}
	
		return null;
	}
	/**
	 * 获取用户订单详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String orderDetail(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData clan=null;
			try{
			    clan =(PageData) dao.findForObject("AppzUserMapper.orderDetail", data);
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clan).toString();
		} 
		return null;
		
		
	}
	/**
	 * 新增支付信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String savePay(PageData pd) throws Exception{
		try {
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
				data.put("ID", UuidUtil.get32UUID());
			    object = dao.save("AppzUserMapper.addOrderPay", data);
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
	 * 查询我的消息列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryMessages(PageData pd) throws Exception{
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
			
			List<PageData> conns = null;
			try{

				conns =(List<PageData>) dao.findForList("AppzUserMapper.findNews",data);
				
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(conns).toString();
		}
		
		return null;
	}

	/**
	 * 添加问题反馈信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editfeedBack(PageData pd) throws Exception{
		try {
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
				data.put("ID", UuidUtil.get32UUID());
				data.put("CREATE_DATE",new Date());
				data.put("STATUS","01");//已提交
				data.put("ISSYSLOOK", "0");
			    object = dao.save("AppFeedBackMapper.save", data);
			    if(Integer.valueOf(object.toString()) >= 1){  
			    	//获取notice.properties noticeText14内容。
	 			    String vv3=new String(noticeText14.getBytes("ISO-8859-1"),"UTF-8");
			    	String notice1=vv3;
	    	        //实时更新后台管理消息
	    	 		noticePushutil notutil=new noticePushutil();
	    	        notutil.toNotice(notice1);
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
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			
			List<PageData> conns = null;
			try{
				conns =(List<PageData>) dao.findForList("AppzUserMapper.findCetif",data);
				
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(conns).toString();
		}
		
		return null;
	}
	
	/**
	 * 修改证件信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editCertInfo(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			List<PageData> cert = data.getList("certs");
			PageData com=new PageData();
			Object type=3;
			for (int i = 0; i < cert.size(); i++) {
				cert.get(i).put("USER_ID", data.get("USER_ID"));
				
				PageData ce = (PageData) dao.findForObject("AppzUserMapper.findCetifbyId", cert.get(i));
				/**USER_ID,CERTIFICATE_NAME,CERTIFICATE_TYPE,IMG_PATH
				 * 空时为新增,否则修改
				 */
				if(ce != null && !"".equals(ce)){
					ce.put("CERTIFICATE_NAME", cert.get(i).get("CERTIFICATE_NAME"));
					ce.put("CERTIFICATE_TYPE", cert.get(i).get("CERTIFICATE_TYPE"));
					ce.put("IMG_PATH", cert.get(i).get("IMG_PATH"));
					ce.put("ISSYSLOOK", "0");
					object =  dao.update("AppzUserMapper.editCetif",ce); 
					if(cert.get(i).get("CERTIFICATE_TYPE")==type){
						com.put("LOGO",cert.get(i).get("IMG_PATH"));
						com.put("COM_ID",ce.get("COMPANY_ID"));
					}  
				}else{
					PageData userD = (PageData) dao.findForObject("AppzUserMapper.findUserDetail", cert.get(i));
					String COMPANY_ID = userD.getString("COMPANY_ID");
					cert.get(i).put("COMPANY_ID", COMPANY_ID);
					cert.get(i).put("ID", UuidUtil.get32UUID());
					cert.get(i).put("ISSYSLOOK", "0");
					object =  dao.save("AppzUserMapper.saveCetif",cert.get(i));
					if(cert.get(i).get("CERTIFICATE_TYPE")==type){
						com.put("LOGO",cert.get(i).get("IMG_PATH"));
						com.put("COM_ID",COMPANY_ID);
					}  
				}
			} 
			if(Integer.valueOf(object.toString()) >= 1){
				//获取notice.properties noticeText4内容。 
				String vv3=new String(noticeText4.getBytes("ISO-8859-1"),"UTF-8");
			       String notice1=vv3;
	    	        noticePushutil notutil=new noticePushutil();
	    	        notutil.toNotice(notice1);
				if(com.getString("COM_ID")!=null&&!"".equals(com.getString("COM_ID"))){
					Object ob =  dao.update("AppUsersMapper.editComLogo",com); 
					if(Integer.valueOf(ob.toString()) >= 1){
						PageData res=new PageData();
						res.put("code","200");
						return JSONArray.toJSONString(res).toString(); 
					}else{
						return null;  
					}
				}else{
					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString(); 
				}  
 			}else{
 				return null;  
 			}  
		}
		return null;
	}
	
	/**
	 * 升级入会
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String goToVip(PageData pd) throws Exception{
		/*PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			
			object =  dao.update("AppzUserMapper.updateUserLevel",data);
			
			if(Integer.valueOf(object.toString()) >= 1){  
					PageData res=new PageData();
				res.put("code","200");
				return JSONArray.toJSONString(res).toString();  
 			}else{
 				return null;  
 			}  
		}*/
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

	/**
	 * 发布需求
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String saveNeeds(PageData pd) throws Exception { 
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object object=0;
			int mun=0;
			List<PageData> needs =(List<PageData>) this.dao.findForList("AppUserNeedsMapper.queryUserNeeds",data); //查询用户需求信息
		    if(needs!=null&&needs.size()>0){
		    	object = this.dao.delete("AppUserNeedsMapper.delete",data); //删除用户荣誉
		    }else{
		    	object=1;
		    }
		    if(Integer.valueOf(object.toString()) >= 1){
		    	List<PageData> nds = data.getList("fbxq"); 
		    	for(PageData nd:nds){
					if(nd.getString("NEED_NAME")!=null&&!"".equals(nd.getString("NEED_NAME"))){
						nd.put("ID",UuidUtil.get32UUID());
						nd.put("USER_ID",data.get("USER_ID")); 
						Object ob=dao.save("AppzUserMapper.saveNeeds",nd);
						mun+=Integer.valueOf(ob.toString());  
					}else{
						mun+=1;  
					} 
				}
				if(mun==nds.size()){
					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();
				}else{
					return null;  
				} 
		    } 
		}
		return null; 
	}

	/**
	 * 发布资源
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String saveRESOURCE(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object object=0;
			int mun=0;
			List<PageData> resous =(List<PageData>) this.dao.findForList("AppUserResourceMapper.queryUserResources",data); //查询用户资源信息
		    if(resous!=null&&resous.size()>0){
		    	object = this.dao.delete("AppUserResourceMapper.delete",data); //删除用户资源
		    }else{
		    	object=1;
		    }
		    if(Integer.valueOf(object.toString()) >= 1){
		    	List<PageData> zys = data.getList("fbzy");
		    	for(PageData zy:zys){
					if(zy.getString("RESOURCE")!=null&&!"".equals(zy.getString("RESOURCE"))){
						zy.put("ID",UuidUtil.get32UUID());
						zy.put("USER_ID",data.get("USER_ID")); 
						Object ob=dao.save("AppzUserMapper.saveRESOURCE",zy);
						mun+=Integer.valueOf(ob.toString());  
					}else{
						mun+=1;  
					} 
				}
				if(mun==zys.size()){
					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();
				}else{
					return null;  
				} 
		    } 
		}
		return null;  
	}
	
	/**
	 * 查询用户的荣誉信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryHonors(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			List<PageData> honors =(List<PageData>) this.dao.findForList("AppUserHonorMapper.queryUserHonors",data); //查询用户荣誉信息
			return JSONArray.toJSONString(honors).toString(); 
		}
		return null; 
	}

	/**
	 * 添加荣誉
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String saveHonors(PageData pd) throws Exception { 
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object object=0;
			int mun=0;
			List<PageData> honors =(List<PageData>) this.dao.findForList("AppUserHonorMapper.queryUserHonors",data); //查询用户荣誉信息
		    if(honors!=null&&honors.size()>0){
		    	object = this.dao.delete("AppUserHonorMapper.delete",data); //删除用户荣誉
		    }else{
		    	object=1;
		    }
		    if(Integer.valueOf(object.toString()) >= 1){
		    	List<PageData> hrs=data.getList("honors");  
				for(PageData hr:hrs){
					if(hr.getString("HONOR")!=null&&!"".equals(hr.getString("HONOR"))){
						hr.put("ID",UuidUtil.get32UUID());
						hr.put("USER_ID",data.get("USER_ID")); 
						Object ob=dao.save("AppUserHonorMapper.saveHonors",hr);
						mun+=Integer.valueOf(ob.toString());  
					}else{
						mun+=1;  
					} 
				}
				if(mun==hrs.size()){
					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();
				}else{
					return null;  
				}
		    }
		} 
		return null;
	}
	
	
	/**
	 * 查询证件审核信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryshCertInfo(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			
			PageData conns = new PageData();
			try{
				conns =(PageData) dao.findForObject("AppzUserMapper.findCetifshbyId",data);
				
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(conns).toString();
		}
		
		return null;
	}
	
	/**
	 * 提交证件审核信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String saveshCertInfo(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			//删除相关信息
			PageData pFd = new PageData();
			pFd.put("USER_ID", data.get("USER_ID"));
			object =  dao.delete("AppzUserMapper.delNotification",pFd);
			object =  dao.delete("AppzUserMapper.delOrder",pFd);
			object =  dao.delete("AppzUserMapper.delUpgrade",pFd);
			
			//获取用户公司信息
			String COMPANY_ID = "";
			PageData gs = (PageData) dao.findForObject("AppzUserMapper.findgs", data);
			
			//已存在公司信息则更新，无则添加
			if(gs != null && !"".equals(gs)){
				COMPANY_ID = gs.getString("ID");
				gs.put("COMPANY_NAME", data.get("COMPANY_NAME"));
				gs.put("CREATE_BY", data.get("USER_ID"));
				gs.put("CREATE_DATE", DateUtil.getTime());
				object =  dao.update("AppzUserMapper.editGs",gs);
			}else{
				gs = new PageData();
				COMPANY_ID = UuidUtil.get32UUID();
				gs.put("ID", COMPANY_ID);
				gs.put("COMPANY_NAME", data.get("COMPANY_NAME"));
				gs.put("CREATE_BY", data.get("USER_ID"));
				gs.put("CREATE_DATE", DateUtil.getTime());
				object =  dao.save("AppzUserMapper.saveGs",gs);
			}
			
			PageData pad = new PageData();
			pad.put("CERTIFICATE_TYPE", "2");
			pad.put("USER_ID", data.get("USER_ID"));
			PageData ce = (PageData) dao.findForObject("AppzUserMapper.findCetifbyId", pad);
			String mid = "";
			
			//已存在名片信息则更新，无则添加
			if(ce != null && !"".equals(ce)){
				ce.put("IMG_PATH", data.get("IMG_PATH"));
				object =  dao.update("AppzUserMapper.editCetif",ce);
			}else{
				pad.put("COMPANY_ID", COMPANY_ID);
				pad.put("ID", UuidUtil.get32UUID());
				pad.put("CERTIFICATE_NAME", "名片");
				pad.put("IMG_PATH", data.get("IMG_PATH"));
				object =  dao.save("AppzUserMapper.saveCetif",pad);
			}
			//修改用户信息
			PageData pbd = new PageData();
			pbd.put("COMPANY_ID", COMPANY_ID);
			pbd.put("USER_ID", data.get("USER_ID"));
			pbd.put("PHONE", data.get("PHONE"));
			pbd.put("POSITION", data.get("POSITION"));
			pbd.put("REAL_NAME", data.get("REAL_NAME"));
			object =  dao.update("AppzUserMapper.updateDetail",pbd);  
			 
			//添加用户会员审核消息
			PageData pnd = new PageData();

			mid = UuidUtil.get32UUID();
			pnd.put("ID", mid);
			pnd.put("USER_ID", data.get("USER_ID"));
			pnd.put("UPGRADE_LEVEL", data.get("UPGRADE_LEVEL"));
			pnd.put("PRICE_TOPAY", data.get("PRICE_TOPAY")); 
			pnd.put("LEVEL", data.get("LEVEL")); 
			object =  dao.update("AppzUserMapper.saveUserhy",pnd);
			
			//添加用户系统消息
			PageData pdd = new PageData();
			pdd.put("ID", UuidUtil.get32UUID());
			pdd.put("USER_ID", data.get("USER_ID"));
			pdd.put("CONTENT", "会员升级信息提交成功，我们的工作人员正在审核，请耐心等待");
			pdd.put("CREATE_DATE", DateUtil.getTime());
			pdd.put("TABLE_NAME", "jl_upgrade");
			pdd.put("STATUS", "01");
			pdd.put("OBJECT_ID",mid); 
			object =  dao.update("AppzUserMapper.saveUserMsg",pdd);
			
			//修改订单状态
//			PageData od = new PageData();
//			od.put("USER_ID", data.get("USER_ID"));
//			od.put("TYPE", "01");
//			List<PageData> orders =(List<PageData>) dao.findForList("UmbsMapper.queryByUid", od);
//	        if( orders.size() > 0 ){
//	        	for( PageData order : orders  ){
//	        		order.put("ISDEL","2");
//	        		dao.update("UmbsMapper.editOrder",order);
//	        	}
//	        }
			/**
	         * 生成订单信息
	         */
		    String ORDER_ID = UuidUtil.get32UUID();
	        PageData pagedata = new PageData();
	        pagedata.put("ID",ORDER_ID);
	        pagedata.put("USER_ID", data.get("USER_ID"));
	        pagedata.put("PRICE", data.get("PRICE_TOPAY"));
	        pagedata.put("EVENT", "升级入会");
	        pagedata.put("OBJECT_ID", "");
	        pagedata.put("STATUS", "01");
	        pagedata.put("TYPE", "01");
	        pagedata.put("DATE", DateUtil.getTime());
	        object =  dao.save("UmbsMapper.saveOrder",pagedata);  
	        
	        PageData ur =(PageData) dao.findForObject("AppUsersMapper.queryByPhone","13636534637");
	        String content="有用户正在申请会员，请注意去后台审核！";
	        //向会员管理中心经理推送审核消息
	        String jsonStr = "{'type':'notice','title':'上海建联','content':'"+content+"'}";//透传内容
			TransmissionTemplate tmTemplate = PushUtil.transmissionTemplateDemo("上海建联",content,jsonStr);
			PushUtil pushApp=new PushUtil();
			String alias = ur.getString("ID");
			pushApp.pushToSingle(tmTemplate,alias); 
			
			
			
			//添加用户系统消息
			PageData nofi = new PageData();
			nofi.put("ID", UuidUtil.get32UUID());
			nofi.put("USER_ID", ur.get("ID"));
			nofi.put("CONTENT",content);
			nofi.put("CREATE_DATE", DateUtil.getTime()); 
			nofi.put("STATUS", "01"); 
			object =  dao.update("AppzUserMapper.saveUserMsg",nofi);
			
			
			if(Integer.valueOf(object.toString()) >= 1){  
				//获取notice.properties noticeText2内容。
			       String vv3=new String(noticeText4.getBytes("ISO-8859-1"),"UTF-8");
			    String notice1=vv3;
	   	        noticePushutil notutil=new noticePushutil();
	   	        notutil.toNotice(notice1);
	   	        
				PageData res=new PageData();
				res.put("code","200");
				res.put("orderInfo",pagedata);
				return JSONArray.toJSONString(res).toString();  
			}else{
				return null;  
			}  
		} 
		return null;
	}

	@Override
	public String editPwd(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String editUserPhone(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String editUserEmail(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String editMassages(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryResources(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryNeeds(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String thirdBindUser(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addClanApplicant(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 添加发票信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addInvoice(PageData pd) throws Exception{
		try {
			PageData data = pd.getObject("params");
			//IS_PAY,CREATE_DATE
			if(!EmptyUtil.isNullOrEmpty(data)){  
				data.put("ID",UuidUtil.get32UUID()); 
				data.put("STATUS","01"); 
				data.put("CREATE_DATE",new Date()); 
				Object rel=dao.save("AppClanApplicantMapper.save",data); 
				if(Integer.parseInt(rel.toString())>=1){
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
	 * 入会升级修改所属建联、所属行业
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editUCInfo(PageData pd) throws Exception{ 
		PageData data = pd.getObject("params");
		if(EmptyUtil.isNullOrEmpty(data)){
			return null;  
		}
		PageData user =(PageData) dao.findForObject("AppUsersMapper.findById", data); 
		String clanId=user.getString("CLAN_ID");
		Object re1=0;
		Object re2=0;
		Object object=0;
		if(data.get("CLAN_ID")!=null&&!"".equals(data.get("CLAN_ID"))){
			object = dao.update("AppzUserMapper.editUserInfo", data);
		}else{
			object=1;
		} 
		Object ob=0;
		if(data.get("CATEGORY_ID")!=null&&!"".equals(data.get("CATEGORY_ID"))){
			if(user.get("COMPANY_ID")!=null&&!"".equals(user.get("COMPANY_ID"))){ 
				 ob = dao.update("AppzUserMapper.editCompanyInfo", data);
			}else{
				PageData com=new PageData();
				com.put("ID",UuidUtil.get32UUID());
				com.put("CATEGORY_ID",data.get("CATEGORY_ID"));
				com.put("CREATE_DATE",new Date());
				com.put("CREATE_BY",data.get("USER_ID"));
				ob = dao.save("AppzUserMapper.saveGs",com);
				if(Integer.valueOf(ob.toString()) >= 1){
					data.put("COMPANY_ID",com.getString("ID"));
					ob = dao.update("AppzUserMapper.editUserCom",data); 
				} 
			}
		}else{
			ob=1;
		} 
		if( Integer.valueOf(object.toString()) >= 1 && Integer.valueOf(ob.toString()) >= 1 ){ 
			if(clanId!=null&&!"".equals(clanId)){
				PageData newClan=new PageData();
				newClan.put("CLAN_ID",clanId);
				PageData clan =(PageData) dao.findForObject("AppClanMapper.queryById", newClan);  
 				int  count=Integer.parseInt(clan.get("MEMBER_COUNT")+"");  
 				if(count>1){ 
 					newClan.put("MEMBER_COUNT",count-1);  
				}else{ 
					newClan.put("MEMBER_COUNT",0);  
				} 
 				re1=this.dao.update("AppClanMapper.updateCounts", newClan); 
			}else{
				re1=1;
			}
			String clan_id=data.getString("CLAN_ID");
			if(clan_id!=null&&!"".equals(clan_id)){   
 				PageData clan =(PageData) dao.findForObject("AppClanMapper.queryById", data);  
 				int  count=Integer.parseInt(clan.get("MEMBER_COUNT")+"");  
 				data.put("MEMBER_COUNT",count+1);  
 				data.put("ISSYSLOOK","0");  
				re2=this.dao.update("AppClanMapper.updateCounts", data); 
			}else{
				re2=1;
			}  
			if(Integer.valueOf(re1.toString()) >= 1&&Integer.valueOf(re2.toString()) >= 1){
				PageData res=new PageData();
				res.put("code","200");
				return JSONArray.toJSONString(res).toString(); 
			}else{
				return null;  
			} 
		}else{
			return null;  
		} 
	}
	
	/**
	 * 查询对应等级空缺人数
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryVacancy(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData clan=null;
			try{
			    clan =(PageData) dao.findForObject("AppUsersMapper.queryVacancy", data);
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clan).toString();
		}
		
		return null;
	}
	
	/**
	 * 修改订单支付方式
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editOrderPayType(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			object = dao.update("AppUsersMapper.editOrderPayType", data);
		    if(Integer.valueOf(object.toString()) >= 1){  
				PageData res=new PageData();
				res.put("code","200");
				return JSONArray.toJSONString(res).toString();  
 			}else{
 				return null;  
 			} 
		}
		return null; 
	}

	@Override
	public String delInvoice(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String queryInvoiceByOid(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryOrdersByUid(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 查询用户的名片、手机号、公司名称、所属建联、所属行业
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String querySjxx(PageData pd) throws Exception{
		return null;
		
	}
	
	/**
	 * 入会升级修改所属建联、所属行业、公司名称、手机号、名片
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Transactional(readOnly = false, rollbackFor = Exception.class) 
	public String editUserxinxi(PageData pd) throws Exception{ 
		PageData data = pd.getObject("params"); 
		if(EmptyUtil.isNullOrEmpty(data)) 
			return null;
		PageData user =(PageData) dao.findForObject("AppUsersMapper.findById", data); 
		boolean object=false;
		boolean re1=false;
		boolean re2=false;
		Object clan_id=data.get("CLAN_ID");
		Object clanId=user.get("CLAN_ID");  
		if(clan_id!=null&&!"".equals(clan_id)){
			if(clanId!=null&&clan_id.toString().equals(clanId.toString())){
				re1=true;
				re2=true;
			}else{
				object = dao.update_("AppzUserMapper.editUserInfo", data);
				log.info("object==="+object+"编辑用户信息："+data.getString("USER_ID"));
				if(!object){
					return ResultUtil.failMsg("完善信息失败！");  
				} 
				if(clanId!=null&&!"".equals(clanId)){
					PageData newClan=new PageData();
					newClan.put("CLAN_ID",clanId);
					PageData clan =(PageData) dao.findForObject("AppClanMapper.queryById", newClan);  
	 				int  count=Integer.parseInt(clan.get("MEMBER_COUNT")+"");  
	 				if(count>1){ 
	 					newClan.put("MEMBER_COUNT",count-1);  
					}else{ 
						newClan.put("MEMBER_COUNT",0);  
					} 
	 				re1=this.dao.update_("AppClanMapper.updateCounts", newClan); 
				}else{
					re1=true;
				} 
				if(clan_id!=null&&!"".equals(clan_id)){   
	 				PageData clan =(PageData) dao.findForObject("AppClanMapper.queryById", data);  
	 				int  count=Integer.parseInt(clan.get("MEMBER_COUNT")+"");  
	 				data.put("MEMBER_COUNT",count+1);  
					re2=this.dao.update_("AppClanMapper.updateCounts", data); 
				}else{ 
					re2=true;
				} 
			} 
		}else{
			re1=true;
			re2=true;
		} 
		log.info("re1==="+re1+"re2====="+re2);
		if(!re1||!re2){
			return ResultUtil.failMsg("完善信息失败！"); 
		} 
		boolean ob=false;
		String com_id="";
		if(data.get("CATEGORY_ID")!=null&&!"".equals(data.get("CATEGORY_ID"))){
			if(user.get("COMPANY_ID")!=null&&!"".equals(user.get("COMPANY_ID"))){ 
				com_id=user.getString("COMPANY_ID");
				data.put("CID",com_id);
				ob = dao.update_("AppzUserMapper.editcomname", data);
			}else{
				PageData com=new PageData();
				com_id=UuidUtil.get32UUID();
				com.put("ID",com_id);
				com.put("COMPANY_NAME",data.getString("COMPANY_NAME"));
				com.put("CATEGORY_ID",data.get("CATEGORY_ID"));
				com.put("CREATE_DATE",new Date());
				com.put("CREATE_BY",data.get("USER_ID"));
				ob = dao.save_("AppzUserMapper.saveGs",com);
				log.info("修改公司信息：ob==="+ob+"com:"+com);
				if(!ob){
					return ResultUtil.failMsg("完善信息失败！"); 
				} 
				data.put("COMPANY_ID",com_id);
				ob = dao.update_("AppzUserMapper.editUserCom",data); 
				log.info("修改用户表公司信息：ob==="+ob+"data:"+data);
				if(!ob){
					return ResultUtil.failMsg("完善信息失败！"); 
				} 
			}
		} 
		PageData pad = new PageData();
		pad.put("CERTIFICATE_TYPE", "2");
		pad.put("USER_ID", data.get("USER_ID"));
		PageData ce = (PageData) dao.findForObject("AppzUserMapper.findCetifbyId", pad);
		String mid = ""; 
		boolean resut=false;
		//已存在名片信息则更新，无则添加
		if(ce != null && !"".equals(ce)){
			ce.put("IMG_PATH", data.get("IMG_PATH"));
			resut =  dao.update_("AppzUserMapper.editCetif",ce);
			log.info("修改证件信息：resut==="+resut+"名片信息:"+JSONArray.toJSONString(ce).toString() );
		}else{
			pad.put("COMPANY_ID", com_id);
			pad.put("ID", UuidUtil.get32UUID());
			pad.put("CERTIFICATE_NAME", "名片");
			pad.put("IMG_PATH", data.get("IMG_PATH"));
			resut =  dao.save_("AppzUserMapper.saveCetif",pad);
			log.info("保存证件信息：resut==="+resut+"名片信息:"+JSONArray.toJSONString(pad).toString() );
		} 
		if(!resut){
			return ResultUtil.failMsg("完善信息失败！"); 
		} 
		return bindPhone(data); 
	}
	
	public String bindPhone(PageData data) throws Exception{
		/*List<PageData> users =(List<PageData>) dao.findForList("AppUsersMapper.queryUserByPhone", data); 
		//手机号没有注册过
		if(users==null||users.size()==0){*/
			boolean result1 =dao.update_("AppUsersMapper.editPwdAndEmailAndPhone", data);
			log.info("保存手机号码：result1"+result1);
			if(result1){
				 //判断用户是否是第一次登录 是新增token信息，否则更新token信息  
				data.put("userid",data.getString("USER_ID"));
				PageData utk=(PageData)dao.findForObject("AppUsertokenMapper.checkToken", data); 
				// 用户token信息
				PageData topd = CommonUtil.getToken(data.get("userid"));
				boolean editToken=false;
				if (utk != null) {
					editToken=dao.update_("AppUsertokenMapper.editToken",topd); 
				} else {
					editToken=dao.save_("AppUsertokenMapper.saveToken",topd); 
				} 
				if(!editToken){
					return ResultUtil.failMsg("完善信息失败！");   
				}
				data.put("ID",data.getString("USER_ID"));
				PageData userinfo= (PageData)dao.findForObject("AppUsersMapper.thirdLogin",data);
				return ResultUtil.successMsg("完善信息成功！",topd.get("token").toString(),userinfo); 
			}else{
				log.error("保存手机号码：result1"+result1+"用户信息："+JSONArray.toJSONString(data).toString());
				return ResultUtil.failMsg("完善信息失败！");  
			}  
		/*}else{
			log.error("要绑定的手机号已存在，请联系客服！用户信息："+JSONArray.toJSONString(data.getString("PHONE")).toString());
			return ResultUtil.failMsg("要绑定的手机号已存在，请联系客服！");
		} */
	}
	
	public void reset(Map a ,Map b,String[] keys){ 
		for(int i=0;i<keys.length;i++){
			if(a.get(keys[i])==null||"".equals(a.get(keys[i]))){
				 a.put(keys[i],b.get(keys[i]));
			 } 
		} 
	}

	@Override
	public String queryNewUsers(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryNewCompanys(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryNewSjxx(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查询会员的卡号、等级等信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	@Override
	public String queryUserBaseinfo(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData user = (PageData) dao.findForObject("AppUsersMapper.findById", data); 
			return JSONArray.toJSONString(user).toString();  
		}
		return null; 
	}

	@Override
	public String firstBindUser(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

 

