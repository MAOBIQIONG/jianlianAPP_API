package org.yx.services.ppp.impl;

import java.sql.SQLException;
import java.util.Date; 
import java.util.List; 

import javax.annotation.Resource; 

import org.json.JSONObject;
import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData;
import org.yx.common.utils.ChineseToEnglish;
import org.yx.common.utils.EmptyUtil; 
import org.yx.common.utils.UuidUtil;
import org.yx.services.AppCommonService;
import org.yx.services.ppp.inter.AppPppServiceInter;
import org.yx.util.ConstantUtil;
import org.yx.util.DataConvertUtil;
import org.yx.util.PushUtil;
import org.yx.util.noticeConfig;
import org.yx.util.noticePushutil;
import org.yx.wyyx.WyUtil;

import com.alibaba.fastjson.JSONArray;   
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

@Service("appPppService")
public class AppPppService implements AppPppServiceInter{


	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	
	@Resource(name="appCommonService")
	private AppCommonService appCommonService;
	
	ChineseToEnglish  cte=new ChineseToEnglish();
	
	public static String noticeText1 = noticeConfig.getString("noticeText1");
	public static String noticeText2 = noticeConfig.getString("noticeText2");
	public static String noticeText3 = noticeConfig.getString("noticeText3");
	
	/**
	 * 查询项目类型和项目阶段+第一页列表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public String querPPPindexlist(PageData pd) throws Exception{
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
			List<PageData> pros=null; 
			try{
				data.put("pid","0"); 
				pros =(List<PageData>) dao.findForList("AppPppMapper.queryBydiylist",data);//第一页列表
				List<PageData> types =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","industry_owned"); //所属行业
				List<PageData> stages =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","pppstage"); //阶段
			    List<PageData> areas =(List<PageData>) this.dao.findForList("SysAreaMapper.querybyPid",data); //查询全部的省级地区
			    for(PageData pagedata:areas){
			    	data.put("pid",pagedata.getString("areacode"));
			    	List<PageData> citys =(List<PageData>) this.dao.findForList("SysAreaMapper.querybyPid",data); //查询全部的省级地区
			    	pagedata.put("citys",citys);
			    }  
			    PageData res=new PageData();
			    res.put("types",types);
			    res.put("areas",areas); 
			    res.put("stages",stages);
			    res.put("pros", pros); 
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
	 * 查询项目类型和项目阶段+第一页列表(新)
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public String queryNewPPPindexlist(PageData pd){
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			try{
				PageData res=new PageData();
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
				List<PageData> pros =(List<PageData>) dao.findForList("AppPppMapper.queryBydiylist",data);//第一页列表
				
				PageData dics=new PageData();
				//比较版本号
				boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_DIC, data);
				//获取返回数据
				DataConvertUtil.getDicRes(version,new String[]{"industry_owned","pppstage"},dics); 
				
				PageData areas=new PageData();
				//比较版本号
				boolean version2=CacheHandler.getCompareVersion(ConstantUtil.VERSION_AREA, data);
				//获取返回数据
				DataConvertUtil.getRes(version2, ConstantUtil.VERSION_AREA, areas);  
				 
			    res.put("pros", pros); 
			    res.put("dics", dics); 
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
	 * 项目列表（根据条件查询,分页）
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querPPPslist(PageData pd) throws Exception{
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
			List<PageData> products=null;
			try{
				products =(List<PageData>) dao.findForList("AppPppMapper.queryBydiylist",data);//第一页列表
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(products).toString();
		}
		return null;  
	} 
	
	/**
	 * 根据项目idPPP项目详细信息 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryById(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData pro =(PageData) this.dao.findForObject("AppPppMapper.queryByPPPXQ",data); 
		    List<PageData> imgs =(List<PageData>) this.dao.findForList("AppPppMapper.queryByPPPXQimg",data); //查询项目的图片
		    List<PageData> sches =(List<PageData>) this.dao.findForList("AppPppMapper.queryByPPPJD",data); //查询项目的进度信息
		    PageData isbid =(PageData) this.dao.findForObject("AppPppMapper.chenckIsInByUid",data);//判断是否参与
		    data.put("OBJECT_ID",data.getString("PID"));
			data.put("TYPE","PPP"); 
		    PageData iscol =(PageData) this.dao.findForObject("AppCollectionMapper.checkCollection",data);//判断是否收藏
			if(iscol!=null&&pro!=null){
				pro.put("isCollect",1);//已经收藏
			}else{
				pro.put("isCollect",0);//未收藏
			} 
		    if(isbid!=null){
				pro.put("isBid",1);//已经参与
			}else{
				pro.put("isBid",0);//未参与
			}
			pro.put("imgs",imgs);//图片信息
			pro.put("sches",sches);//阶段信息
			return JSONArray.toJSONString(pro).toString(); 
		}
		return null;   
	}  
	
	/**查询ppp项目流程进度
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryPppProProce(PageData pd) throws Exception{
		PageData data = pd.getObject("params");  
		PageData res=new PageData();
		if(!EmptyUtil.isNullOrEmpty(data)){   
			/*data.put("LOCATION_NO","15");
			List<PageData> advs=(List<PageData>) dao.findForList("AppRotationMapper.queryByColno",data);//查询轮换大图
*/			PageData pro =(PageData) this.dao.findForObject("AppPppMapper.queryByPPPXQ",data);//查询交易信息
			/*PageData phone =(PageData) this.dao.findForObject("ServiceTelMapper.findBm","06");//查询交易下单成功的客服电话
			if(phone!=null){
				pro.put("SERVICE_PHONE",phone.getString("PHONE"));
			}else{
				pro.put("SERVICE_PHONE",null);
			} */
			res.put("pro",pro);
			//res.put("advs",advs);
			Object userid=data.get("USER_ID");
			PageData bidder=null;
			if(userid!=null&&!"".equals(userid)&&pro!=null){
				if(userid.toString().equals(pro.getString("USER_ID"))){//是发布人自己查看
					res.put("code","200");
					res.put("bidder",null);  
					res.put("isCy",null); 
				}else{
					bidder =(PageData) this.dao.findForObject("AppPppMapper.chenckIsInByUid",data);//查询参与信息
					PageData isCy =(PageData) this.dao.findForObject("AppPppMapper.chenckIsInXz",data);//查询讨论小组信息
					if(bidder!=null){
						res.put("bidder",bidder); 
					}else{
						res.put("bidder",null); 
					}
					if(isCy!=null){
						res.put("isCy",isCy); 
					}else{
						res.put("isCy",null); 
					}  
				}
			}else{
				if(data.get("PHONE")!=null&&!"".equals(data.get("PHONE"))){
					bidder =(PageData) this.dao.findForObject("AppPppMapper.chenckIsInByPhone",data);//查询参与信息
				} 
				if(bidder!=null){
					res.put("bidder",bidder); 
				}else{
					res.put("bidder",null); 
				} 
				res.put("isCy",null);  
			} 
			return JSONArray.toJSONString(res).toString();  
		}
		return null;   
	}
	
	/**
	 * 查询我参与的项目信息（分页)
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public String querPPPMyCYlist(PageData pd) throws Exception{
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
			List<PageData> products=null;
			try{
				products =(List<PageData>) dao.findForList("AppPppMapper.queryMYCYPRO",data);//第一页列表
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(products).toString();
		}
		return null;  
	}
	
	/**
	 * 查询项目的参与人列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryBidders(PageData pd) throws Exception{
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
			List<PageData> bidders=null;
			try{
				bidders =(List<PageData>) dao.findForList("AppPppMapper.queryBidders",data);
				for(PageData bid:bidders){
					if(bid.get("ID")==null||"".equals(bid.get("ID"))){
						bid.put("REAL_NAME","匿名用户");
					} 
				}
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(bidders).toString();
		}
		return null;  
	}
	
	/**
	 * 参与交易（未登录/已登陆）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addBid(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			try{
				Object userid=data.get("USER_ID");
				if(userid!=null&&!"".equals(userid)){//已经登录  
					return loginBidder(data);   
				}else{//未登录
					return bidder(data); 
				}
			}catch(Exception e){
				e.printStackTrace();
			} 
		}
		return null; 
	} 
	
	public String loginBidder(PageData data) throws Exception{
		PageData isbid =(PageData) this.dao.findForObject("AppPppMapper.chenckIsInByUid",data);//根据用户id判断是否参与
		if(isbid==null){//没有参与
			String id=UuidUtil.get32UUID();
			data.put("ID", id);
			data.put("BID_DATE", new Date()); 
			Object obj=dao.save("AppPppMapper.addBidder",data);
			if(Integer.valueOf(obj.toString()) >= 1){
				PageData pro=(PageData) this.dao.findForObject("AppPppMapper.queryById",data);//查询项目信息
 				int  count=Integer.parseInt(pro.get("TOTAL_BIDDER")+"");  
				data.put("TOTAL_BIDDER",count+1);   
				data.put("ISSYSLOOK","0");   
 				Object ob=dao.update("AppPppMapper.updateCounts",data);
 				if(Integer.valueOf(ob.toString()) >= 1){
 					//获取notice.properties noticeText2内容。
 			       String vv3=new String(noticeText1.getBytes("ISO-8859-1"),"UTF-8");
 					//推送消息
					PageData user=(PageData) this.dao.findForObject("AppPppMapper.queryManager",data);//查询
	    	 		if( user != null ){
	    	 			String content="又有用户参与"+user.getString("PROJECT_NAME")+"项目了，快去后台查看吧！";
	    	 			push(user,content); 
	 				} 
	    	 		String notice1=user.getString("PROJECT_NAME")+vv3;
	    	        noticePushutil notutil=new noticePushutil();
	    	        notutil.toNotice(notice1);
	    	 		
	    	 		return result("200");
				}else{
					return result("400");
				} 
			}else{
				return result("400");
			}
		}else{//已经参与
			return result("200");
		}
	}
	
	public String bidder(PageData data) throws Exception{ 
		PageData isbid=null; 
		PageData user =(PageData) this.dao.findForObject("AppUsersMapper.queryByPhone",data.getString("PHONE"));//根据手机号码查询会员信息
		if(user!=null){//用户存在
			data.put("USER_ID",user.getString("ID"));
			isbid =(PageData) this.dao.findForObject("AppPppMapper.chenckIsInByUid",data);//判断是否参与
			if(isbid==null){//没有参与
				String id=UuidUtil.get32UUID();
				data.put("ID", id);
				data.put("BID_DATE", new Date()); 
				Object obj=dao.save("AppPppMapper.addBidder",data);
				if(Integer.valueOf(obj.toString()) >= 1){
					PageData pro=(PageData) this.dao.findForObject("AppPppMapper.queryById",data);//查询项目信息
	 				int  count=Integer.parseInt(pro.get("TOTAL_BIDDER")+"");  
 					data.put("TOTAL_BIDDER",count+1);
 					data.put("ISSYSLOOK","0"); 
	 				Object ob=dao.update("AppPppMapper.updateCounts",data);
	 				if(Integer.valueOf(ob.toString()) >= 1){ 
	 					return result("200");
	 				}else{
	 					return result("400");   
	 				} 
				}else{
					return result("400");
				} 
			}else{
				return result("200");
			} 
		}else{//用户不存在，添加进数据库  
			data.put("ID",UuidUtil.get32UUID());
			data.put("USER_ID",null);
			data.put("BID_DATE", new Date()); 
			Object obj=dao.save("AppPppMapper.addBidder",data);
			if(Integer.valueOf(obj.toString()) >= 1){
				PageData pro=(PageData) this.dao.findForObject("AppPppMapper.queryById",data);//查询竞标总数
 				int  count=Integer.parseInt(pro.get("TOTAL_BIDDER")+"");  
				data.put("TOTAL_BIDDER",count+1);   
				data.put("ISSYSLOOK","0"); 
 				Object ob=dao.update("AppPppMapper.updateCounts",data);
 				if(Integer.valueOf(ob.toString()) >= 1){
 					//获取notice.properties noticeText2内容。
  			       String vv3=new String(noticeText1.getBytes("ISO-8859-1"),"UTF-8");
 					//推送消息
					PageData manager=(PageData) this.dao.findForObject("AppProjectMapper.queryManager",data);//查询
	    	 		if( manager != null ){
	    	 			String content="又有用户参与"+manager.getString("PROJECT_NAME")+"项目了，快去后台查看吧！";
	    	 			push(user,content); 
	    	 		}
	    	 		String notice1=vv3;
	    	        noticePushutil notutil=new noticePushutil();
	    	        notutil.toNotice(notice1);
	    	 		
	    	 		return result("200");   
 				}else{
 					return result("400");  
 				} 
			}else{
				return result("400");
			}  
		}  
	}
	 
	/**
	 * 取消参与交易（已登录/未登录）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String delBid(PageData pd) throws Exception{
		PageData data = pd.getObject("params");  
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			Object userid=data.get("USER_ID");
			if(userid!=null&&!"".equals(userid)){//用户已登录，根据用户id取消参与
				PageData user =(PageData) this.dao.findForObject("AppUsersMapper.findById",data);//根据用户id查询会员信息
				if(user!=null){//用户存在
					data.put("USER_ID",user.getString("ID"));  
					Object obj =dao.delete("AppPppMapper.cancleBidderByUid",data);
		 		     if(Integer.valueOf(obj.toString()) >= 1){ 
		 		    	PageData pro=(PageData) this.dao.findForObject("AppPppMapper.queryById",data);//查询点赞数
		 				int  count=Integer.parseInt(pro.get("TOTAL_BIDDER")+"");  
		 				if(count>1){ 
							data.put("TOTAL_BIDDER",count-1);  
						}else{ 
							data.put("TOTAL_BIDDER",0);  
						}   
		 				Object ob=dao.update("AppPppMapper.updateCounts",data); 
		 				if(Integer.valueOf(ob.toString()) >= 1){ 
		 					//推送消息
							/*PageData manager=(PageData) this.dao.findForObject("AppPppMapper.queryManager",data);//查询
			    	 		if( manager != null ){
			    	 			String content="有用户取消参与"+manager.getString("PROJECT_NAME")+"项目了，快去后台查看吧！";
			    	 			push(manager,content); 
			    	 		}*/
			    	 		
			    	 		return result("200"); 
		 				}else{
		 					return result("400"); 
		 				}
		 			}else{
		 				return result("400"); 
		 			}  
				}else{//用户已登录，根据用户id没有查找到改用户
					return result("400");
				} 
			}else{//用户未登录，根据留的手机号取消参与 
				Object phone=data.get("PHONE");
				if(phone!=null&&!"".equals(phone)){
					 Object obj =dao.delete("AppPppMapper.cancleBidderByPhone",data);
		 		     if(Integer.valueOf(obj.toString()) >= 1){ 
		 		    	PageData pro=(PageData) this.dao.findForObject("AppPppMapper.queryById",data);//查询项目信息
		 				int  count=Integer.parseInt(pro.get("TOTAL_BIDDER")+"");  
		 				if(count>1){ 
							data.put("TOTAL_BIDDER",count-1);  
						}else{ 
							data.put("TOTAL_BIDDER",0);  
						}   
		 				Object ob=dao.update("AppPppMapper.updateCounts",data); 
		 				if(Integer.valueOf(ob.toString()) >= 1){
		 					//推送消息
							/*PageData manager=(PageData) this.dao.findForObject("AppPppMapper.queryManager",data);//查询
			    	 		if( manager != null ){
			    	 			String content="有用户取消参与"+manager.getString("PROJECT_NAME")+"项目了，快去后台查看吧！";
			    	 			push(manager,content); 
			    	 		}*/
			    	 		return result("200"); 
		 				}else{
		 					return result("400");
		 				}
		 			}else{
		 				return result("400");
		 			}  
				}else{//手机号不存在 
					return result("400");
				} 
			}  
		}
		return null;   
	} 
	
	/**
	 * 判断是否可以加入项目讨论小组 
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String isCanApply(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData isbid =(PageData) this.dao.findForObject("AppPppMapper.chenckIsInByUid",data);//判断是否参与
			if(isbid==null){//没有参与
				PageData res=new PageData();
				res.put("code","400");
				res.put("isbid","0"); 
				res.put("isCy","0"); //没有加入讨论小组
				return JSONArray.toJSONString(res).toString();    
			}else{//已经加入,可以参加讨论小组
				PageData pro =(PageData) this.dao.findForObject("AppPppMapper.queryById",data); 
				PageData isCy =(PageData) this.dao.findForObject("AppPppMapper.chenckIsInXz",data);//判断是否已经加入
				PageData res=new PageData();
				if(isCy!=null){
					res.put("isCy","1");//已经加入讨论小组
				}else{
					res.put("isCy","0"); //没有加入讨论小组
				} 
				res.put("code","200");
				res.put("isbid","1");
				res.put("pro",pro);
				return JSONArray.toJSONString(res).toString();
			} 
		}
		return null;   
	} 
	
	//像某个用户推送消息
	public void push(PageData user,String content){  
		String jsonStr = "{'type':'notice','title':'上海建联','content':'"+content+"'}";//透传内容
		TransmissionTemplate tmTemplate = PushUtil.transmissionTemplateDemo("上海建联",content,jsonStr);
		PushUtil pushApp=new PushUtil();
		String alias = user.getString("ID");
		try {
			pushApp.pushToSingle(tmTemplate,alias);
		} catch (Exception e) { 
			e.printStackTrace();
		} 
	}
	
	//返回成功参数
	public String result(String code){  
		PageData res=new PageData();
		res.put("code",code);
		return JSONArray.toJSONString(res).toString();  
	} 
}
