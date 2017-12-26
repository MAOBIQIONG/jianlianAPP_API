package org.yx.services.jy.impl;

import java.sql.SQLException;
import java.util.ArrayList;
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
import org.yx.common.utils.Logger;
import org.yx.common.utils.UuidUtil;
import org.yx.services.AppCommonService;
import org.yx.services.jy.inter.AppProjectServiceInter;  
import org.yx.util.ConstantUtil;
import org.yx.util.DataConvertUtil;
import org.yx.util.ProNoUtil;
import org.yx.util.PushUtil;
import org.yx.util.ResultResetUtil;
import org.yx.util.noticePushutil;
import org.yx.wyyx.WyUtil;
import org.yx.util.noticeConfig;
import com.alibaba.fastjson.JSONArray;   
import com.gexin.rp.sdk.template.TransmissionTemplate;

@Service("appProService")
public class AppProjectService implements AppProjectServiceInter{

	Logger log=Logger.getLogger(this.getClass());

	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	
	@Resource(name="appCommonService")
	private AppCommonService appCommonService;
	
	ChineseToEnglish  cte=new ChineseToEnglish();
	
	public static String noticeText1 = noticeConfig.getString("noticeText1");
	public static String noticeText2 = noticeConfig.getString("noticeText2");
	public static String noticeText3 = noticeConfig.getString("noticeText3");
	
	/**
	 * 查询交易列表信息(可以根据条件搜索：地区、阶段、类型、项目名称模糊查询)
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryIndexByParam(PageData pd) throws Exception { 
		long startTime=System.currentTimeMillis();
		PageData data = pd.getObject("params"); 
		log.info("进入接口 service【appProService】方法【queryIndexByParam】参数【"+JSONArray.toJSONString(data).toString()+"】");
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("startPage", 0);
				data.put("pageSize",8);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("startPage",currentPage-1);
				data.put("pageSize",pageSize); 
			} 
			List<PageData> pros=null;
			try{
				Object qf = data.get("DISTINGUISH");
				if( qf != null){
					if("2".equals(qf.toString())){
						data.put("DISTINGUISH", qf);
					}else {
						data = new PageData();
						data.put("DISTINGUISH","1");
					}
				}else { 
					data.put("DISTINGUISH","1"); 
				} 
				pros =(List<PageData>) dao.findForList("AppProjectMapper.queryProIndexByParam",data);
				for(PageData pro:pros){
					if("1".equals(pro.getString("IS_RESOURCE"))){
						if(pro.get("pro_imgs")!=null&&!"".equals(pro.get("pro_imgs"))){
							ResultResetUtil.resetImgs(pro.getString("pro_imgs"),"IMG_PATH","imgs",pro);
						} 
						/* List<PageData> imgs =(List<PageData>) this.dao.findForList("AppXmtpMapper.queryImgsByPid",pro); //查询项目的图片
						 pro.put("imgs",imgs);*/
					}
				} 
				if(pros.size()>0){
					data.put("LOCATION_NO", "12"); 
					PageData adv=(PageData) dao.findForObject("AppRotationMapper.queryAdvByCol",data);//查询交易列表的广告信息
					pros.add(adv);
				} 
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
			long entTime=System.currentTimeMillis();
			long spendMills=entTime-startTime; 
			log.info("查询交易列表信息(可以根据条件搜索：地区、阶段、类型、项目名称模糊查询)。耗费时间"+ spendMills+" 毫秒 ，合计"+(spendMills/1000.00) +"秒！ service：【appProService】方法：【queryIndexByParam】返回值数量：【"+pros.size()+"】");
			return JSONArray.toJSONString(pros).toString();
		} 
		return null; 
	}

	
	/**
	 * 根据订阅条件搜索项目信息
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryProByDy(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("startPage", 0);
				data.put("pageSize",8);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("startPage",currentPage-1);
				data.put("pageSize",pageSize); 
			} 
		    /*List<PageData> sches =(List<PageData>) this.dao.findForList("AppProScheMapper.queryBianMa",data); //查询用户订阅的阶段编码-bianma
		    List<PageData> lxs =(List<PageData>) this.dao.findForList("AppXmlxMapper.queryDyCid",data); //查询用户订阅所有类型-lxid
		    List<PageData> dqs =(List<PageData>) this.dao.findForList("SysAreaMapper.queryDyName",data); //查询用户订阅所有地区名称*/
			if(data.get("USER_ID")==null||"".equals(data.get("USER_ID"))){
				return null;  
			}
			List<PageData>  hys=(List<PageData>) this.dao.findForList("AppProjectMapper.queryDyHy",data); //查询用户订阅的行业信息
			if(hys!=null&&hys.size()>0){
				List<PageData> pros =(List<PageData>) this.dao.findForList("AppProjectMapper.queryDys",data); //查询用户订阅所有地区-code
				for(PageData pro:pros){
					if("1".equals(pro.getString("IS_RESOURCE"))){
						 List<PageData> imgs =(List<PageData>) this.dao.findForList("AppXmtpMapper.queryImgsByPid",pro); //查询项目的图片
						 pro.put("imgs",imgs);
					}
				}
				if(pros.size()>0){
					data.put("LOCATION_NO", "12"); 
					PageData adv=(PageData) dao.findForObject("AppRotationMapper.queryAdvByCol",data);//查询交易列表的广告信息
					pros.add(adv);
				} 
			    return JSONArray.toJSONString(pros).toString(); 
			} 
		}
		return null;  
	}
	
	/**
	 * 查询会员收藏的项目信息
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryColls(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("startPage",0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("startPage",currentPage-1);
				data.put("pageSize",pageSize); 
			} 
			List<PageData> pros=null;
			try{
				pros =(List<PageData>) dao.findForList("AppProjectMapper.queryCollects",data);
				/*for(PageData pro:pros){
					if("1".equals(pro.getString("IS_RESOURCE"))){
						 List<PageData> imgs =(List<PageData>) this.dao.findForList("AppXmtpMapper.queryImgsByPid",pro); //查询项目的图片
						 pro.put("imgs",imgs);
					}
				} */
				for(PageData pro:pros){
					if("1".equals(pro.getString("IS_RESOURCE"))){
						if(pro.get("pro_imgs")!=null&&!"".equals(pro.get("pro_imgs"))){
							ResultResetUtil.resetImgs(pro.getString("pro_imgs"),"IMG_PATH","imgs",pro);
						}  
					}
				}
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(pros).toString();
		}
		return null;  
	} 
	
	/**
	 * 查询会员查看的项目信息
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryCkPros(PageData pd) throws Exception{ 
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("startPage",0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("startPage",currentPage-1);
				data.put("pageSize",pageSize); 
			} 
			List<PageData> pros=null;
			try{
				pros =(List<PageData>) dao.findForList("AppProjectMapper.queryCks",data);
				for(PageData pro:pros){
					if("1".equals(pro.getString("IS_RESOURCE"))){
						 List<PageData> imgs =(List<PageData>) this.dao.findForList("AppXmtpMapper.queryImgsByPid",pro); //查询项目的图片
						 pro.put("imgs",imgs);
					}
				} 
				PageData counts =(PageData) dao.findForObject("AppXmckMapper.queryCount",data);
				PageData res=new PageData();
				res.put("pros",pros);
				res.put("counts",counts);
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
	 * 查询会员参与的项目信息
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryCyPros(PageData pd) throws Exception{
		long startTime=System.currentTimeMillis();
		PageData data = pd.getObject("params"); 
		log.info("查询会员参与的项目信息。service【appProService】方法【queryCyPros】参数【"+JSONArray.toJSONString(data).toString()+"】");
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("startPage",0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("startPage",currentPage-1);
				data.put("pageSize",pageSize); 
			} 
			List<PageData> pros=null;
			try{
				Object epcor=data.get("DISTINGUISH");
				if(epcor!=null&&!"".equals(epcor)){
					if(!"2".equals(epcor.toString())){
						data.put("DISTINGUISH",1);
					} 
				}else{
					data.put("DISTINGUISH",1);
				}
				pros =(List<PageData>) dao.findForList("AppProjectMapper.queryCys",data);
				long sec=System.currentTimeMillis();
				long sectime=sec-startTime;
				log.info("消耗时间为："+sectime+"毫秒！");
				/*for(PageData pro:pros){
					if("1".equals(pro.getString("IS_RESOURCE"))){
						 List<PageData> imgs =(List<PageData>) this.dao.findForList("AppXmtpMapper.queryImgsByPid",pro); //查询项目的图片
						 pro.put("imgs",imgs);
					}
				} */
				for(PageData pro:pros){
					if("1".equals(pro.getString("IS_RESOURCE"))){
						if(pro.get("pro_imgs")!=null&&!"".equals(pro.get("pro_imgs"))){
							ResultResetUtil.resetImgs(pro.getString("pro_imgs"),"IMG_PATH","imgs",pro);
						}  
					}
				}
				long third=System.currentTimeMillis();
				long thirdtime=third-startTime;
				log.info("消耗时间为："+thirdtime+"毫秒！");
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
			long last=System.currentTimeMillis();
			long lastTime=last-startTime; 
			log.info("查询会员参与的项目信息。消耗时间为："+lastTime+"毫秒！ service【appProService】方法【queryCyPros】返回值数量：【"+pros.size()+"】");
			return JSONArray.toJSONString(pros).toString();
		}
		return null;  
	} 
	
	/**
	 * 查询会员发布的项目信息
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryFbPros(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("startPage",0);
				data.put("pageSize",8);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("startPage",currentPage-1);
				data.put("pageSize",pageSize); 
			} 
			List<PageData> pros=null;
			try{
				pros =(List<PageData>) dao.findForList("AppProjectMapper.queryFbs",data);
				/*for(PageData pro:pros){
					if("1".equals(pro.getString("IS_RESOURCE"))){
						 List<PageData> imgs =(List<PageData>) this.dao.findForList("AppXmtpMapper.queryImgsByPid",pro); //查询项目的图片
						 pro.put("imgs",imgs);
					}
				} */
				for(PageData pro:pros){
					if("1".equals(pro.getString("IS_RESOURCE"))){
						if(pro.get("pro_imgs")!=null&&!"".equals(pro.get("pro_imgs"))){
							ResultResetUtil.resetImgs(pro.getString("pro_imgs"),"IMG_PATH","imgs",pro);
						}  
					}
				}
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(pros).toString();
		}
		return null;  
	} 
	
	/**
	 * 根据项目id查询项目详细信息 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryById(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData pro =(PageData) this.dao.findForObject("AppProjectMapper.queryById",data); 
		    List<PageData> imgs =(List<PageData>) this.dao.findForList("AppXmtpMapper.queryByPid",data); //查询项目的图片
//		    Object jd=data.get("DISTINGUISH");
		    List<PageData> stages=null;
		    if("2".equals(pro.getString("DISTINGUISH"))){
		    	stages =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","epcstage"); //查询EPC项目的所有阶段
		    }else{
		    	stages =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","stage"); //查询项目的所有阶段
		    }
		    List<PageData> sches =(List<PageData>) this.dao.findForList("AppProScheMapper.findByPid",data); //查询项目的进度信息
		    PageData isbid =(PageData) this.dao.findForObject("AppProBidderMapper.chenckIsIn",data);//判断是否参与
			if(isbid!=null){
				pro.put("isBid",1);//已经参与
			}else{
				pro.put("isBid",0);//未参与
			}
			data.put("OBJECT_ID",data.getString("PRO_ID"));
			data.put("TYPE","PROJECT"); 
		    PageData iscol =(PageData) this.dao.findForObject("AppCollectionMapper.checkCollection",data);//判断是否收藏
			if(iscol!=null){
				pro.put("isCollect",1);//已经收藏
			}else{
				pro.put("isCollect",0);//未收藏
			}
			List<PageData> recomms=null;
			if("1".equals(pro.getString("IS_RESOURCE"))){
				recomms =(List<PageData>) this.dao.findForList("AppProjectMapper.queryReqRecomm",pro); //查询推荐需求
			}else{
				recomms =(List<PageData>) this.dao.findForList("AppProjectMapper.queryProRecomm",pro); //查询推荐项目
			} 	
			pro.put("imgs",imgs);//图片信息
			pro.put("sches",sches);//阶段信息
			pro.put("stages", stages);
			pro.put("recomms", recomms);
			return JSONArray.toJSONString(pro).toString(); 
		}
		return null;   
	} 
	
	/**
	 * 参与/取消参与项目的竞标 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editBidder(PageData pd) throws Exception{ 
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){   
			//获取notice.properties noticeText1内容。
	 		//Properties prop = PropertiesLoaderUtils.loadAllProperties("resources/notice.properties");
			//String notice =prop.getProperty("noticeText1").trim(); 
			//System.out.println("notice.properties noticeText1:"+notice);
			
			PageData isbid =(PageData) this.dao.findForObject("AppProBidderMapper.chenckIsIn",data);//判断是否参与
			if(isbid==null){//没有参与
				String id=UuidUtil.get32UUID();
				data.put("ID", id);
				data.put("BID_DATE", new Date()); 
				Object obj=dao.save("AppProBidderMapper.addbidder",data);
				if(Integer.valueOf(obj.toString()) >= 1){
					PageData pro=(PageData) this.dao.findForObject("AppProjectMapper.queryByProId",data);//查询点赞数
	 				int  count=Integer.parseInt(pro.get("TOTAL_BIDDER")+"");  
 					data.put("TOTAL_BIDDER",count+1);  
 					data.put("MODIFY_DATE", new Date());
 					//查询当前项目查看状态
 					if(pro.getString("ISSYSLOOK")==null && "".equals(pro.getString("ISSYSLOOK")) && "1".equals(pro.getString("ISSYSLOOK"))){
 						data.put("ISSYSLOOK", "0");
 					} 
	 				Object ob=dao.update("AppProjectMapper.updateCounts",data);
	 				if(Integer.valueOf(ob.toString()) >= 1){
	 					//获取notice.properties noticeText2内容。
	 			       String vv3=new String(noticeText1.getBytes("ISO-8859-1"),"UTF-8");
	 			      //String vv3=noticeText1;
	 					//推送消息
						PageData user=(PageData) this.dao.findForObject("AppProjectMapper.queryManager",data);//查询
		    	 		if( user != null ){
		    	 			String content="又有用户参与"+user.getString("PROJECT_NAME")+"项目了，快去后台查看吧！";
		    	 			//推送审核消息
		    	 			String jsonStr = "{'type':'notice','title':'上海建联','content':'"+content+"'}";//透传内容
		    				TransmissionTemplate tmTemplate = PushUtil.transmissionTemplateDemo("上海建联",content,jsonStr);
							PushUtil pushApp=new PushUtil();
							String alias = user.getString("ID");
							pushApp.pushToSingle(tmTemplate,alias);  
		 				} 
		    	 		
		    	 		String notice1=user.getString("PROJECT_NAME")+vv3;
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
			}else{//已经参与，取消参与
				 Object obj =dao.delete("AppProBidderMapper.deletebidder",data);
	 		     if(Integer.valueOf(obj.toString()) >= 1){ 
	 		    	PageData pro=(PageData) this.dao.findForObject("AppProjectMapper.queryByProId",data);//查询点赞数
	 				int  count=Integer.parseInt(pro.get("TOTAL_BIDDER")+"");  
	 				if(count>1){ 
 						data.put("TOTAL_BIDDER",count-1);  
 					}else{ 
 						data.put("TOTAL_BIDDER",0);  
 					} 
	 				Object ob=dao.update("AppProjectMapper.updateCounts",data); 
	 				if(Integer.valueOf(ob.toString()) >= 1){
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
		}
		return null;   
	}
	
	/**
	 * 加入/取消加入项目讨论小组 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editXz(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData isCy =(PageData) this.dao.findForObject("AppXzcyMapper.chenckIsIn",data);//判断是否已经加入
			if(isCy==null){//没有加入
				String id=UuidUtil.get32UUID();
				data.put("ID", id);
				data.put("DATE", new Date()); 
				Object obj=dao.save("AppXzcyMapper.addXz",data);
				if(Integer.valueOf(obj.toString()) >= 1){ 
 					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();   
				}else{
					return null;
				} 
			}else{//已经加入，退出讨论小组
				 Object obj =dao.delete("AppXzcyMapper.deleteXz",data);
	 		     if(Integer.valueOf(obj.toString()) >= 1){ 
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
	 * 判断是否可以加入项目讨论小组 
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String isCanApply(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData isbid =(PageData) this.dao.findForObject("AppProBidderMapper.chenckIsIn",data);//判断是否参与
			if(isbid==null){//没有参与
				PageData res=new PageData();
				res.put("code","400");
				res.put("isbid","0"); 
				res.put("isCy","0"); //没有加入讨论小组
				return JSONArray.toJSONString(res).toString();    
			}else{//已经加入,可以参加讨论小组
				PageData pro =(PageData) this.dao.findForObject("AppProjectMapper.queryByProId",data); 
				PageData isCy =(PageData) this.dao.findForObject("AppXzcyMapper.chenckIsIn",data);//判断是否已经加入
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
	
	/**
	 * 修改项目讨论小组成员的权限
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editXzQx(PageData pd) throws Exception{ 
		return null;   
	} 
	
	/**
	 * 下单/修改项目信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editPro(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			ResultResetUtil.resetInt(data,new String[]{"START_PRICE","FLOOR_AREA","BUILD_LAYERS","BUILD_AREA"});
	        data.put("STATUS","1");
	        if(data.getString("PRO_ID")!=null&&!"".equals(data.getString("PRO_ID"))){ //修改
	           data.put("MODIFY_BY",data.getString("USER_ID"));
	    	   data.put("MODIFY_DATE",new Date());
	    	   Object obj=dao.update("AppProjectMapper.edit",data);
	    	   if(Integer.valueOf(obj.toString()) >= 1){
	    		   int mun=0;
					List<PageData> imgs=data.getList("imgs");
					PageData delImg=new PageData();
					delImg.put("PRO_ID",data.getString("PRO_ID"));
					List<PageData> is =(List<PageData>) this.dao.findForList("AppXmtpMapper.queryByPid",delImg);//判断是否存在图片 
					Object object=0;
					if(is.size()>0){
					    object = this.dao.delete("AppXmtpMapper.del",delImg); //删除项目的图片
					}else{
						object=1;
					} 
					if(Integer.valueOf(object.toString()) >= 1){
						for(PageData img:imgs){ 
							img.put("ID",UuidUtil.get32UUID());
							img.put("PRO_ID",data.getString("PRO_ID"));
							img.put("DATE",new Date());
							if(img.getString("IMG_PATH").contains("{")){
								PageData path=(PageData) img.get("IMG_PATH");
								img.put("IMG_PATH",path.getString("result")); 
							} 
							Object ob=dao.save("AppXmtpMapper.save",img); 
							mun+=Integer.valueOf(ob.toString()); 
						} 
						if(mun==imgs.size()){ 
			    		   PageData backup=new PageData(); 
			    		   backup.put("STATUS",1);  
			    		   backup.put("DATE", new Date()); 
			    		   backup.put("OPER_BY","");
			    		   backup.put("DESCRIPTION","项目审核中！");  
			    		   backup.put("TARGET_ID",""); 
			    		   backup.put("PRO_ID",data.getString("PRO_ID")); 
			    		   backup.put("USER_ID",data.getString("USER_ID")); 
						   //保存项目反馈信息   
						   Object re=dao.update("AppProBackupMapper.edit",backup);
						   if(Integer.valueOf(re.toString()) >= 1){ 
			 					PageData res=new PageData();
								res.put("code","200");
								res.put("PRO_ID",data.getString("PRO_ID"));
								return JSONArray.toJSONString(res).toString();   
				 			}else{
				 				return null;  
				 			} 
			 			}else{
			 				return null;  
			 			}  
					}else{
						return null;  
					} 
    	        }else{
    	        	return null;  
    	        }
	        }else{
	           String id=UuidUtil.get32UUID();
			   data.put("PRO_ID", id);  
	    	   data.put("MODIFY_BY",data.getString("USER_ID"));
	    	   data.put("MODIFY_DATE",new Date());
	    	   data.put("CREATE_DATE",new Date());
	    	   if(Integer.parseInt(data.get("IS_RESOURCE").toString())==1){
	    		   getResNo(data);
	    	   }else{
	    		   getProNo(data);
	    	   } 
	    	   //data.put("RELEASE_DATE",new Date());
	    	   //data.put("PROJECT_STAGE_ID","13");//默认属于工程筹备阶段
	    	   Object obj=dao.update("AppProjectMapper.save",data); 
	    	   if(Integer.valueOf(obj.toString()) >= 1){
	    		    int mun=0;
					List<PageData> imgs=data.getList("imgs");
					for(PageData img:imgs){ 
						img.put("ID",UuidUtil.get32UUID());
						img.put("PRO_ID", id);
						img.put("DATE",new Date());
						Object ob=dao.save("AppXmtpMapper.save",img);
						mun+=Integer.valueOf(ob.toString());
					}  
					if(mun==imgs.size()){ 
						//获取notice.properties noticeText2内容。
						String vv3=new String(noticeText2.getBytes("ISO-8859-1"),"UTF-8");
		    		   //向项目阶段表中添加信息
		        	   PageData sc=new PageData();
		        	   sc.put("ID",UuidUtil.get32UUID()); 
		      		   sc.put("DATE",new Date());
		      		   sc.put("OPER_BY",data.getString("USER_ID"));
		      		   sc.put("PRO_ID",data.getString("PRO_ID"));
		      		   sc.put("SCHEDULE",data.getString("PROJECT_STAGE_ID")); 
		      		   sc.put("DESCRIPTION","项目处于筹备阶段！");  
		      		   Object sche=dao.save("AppProScheMapper.save",sc);
		      		   PageData backup=new PageData(); 
			      	   backup.put("STATUS",1); 
			      	   backup.put("ID",UuidUtil.get32UUID());
			      	   backup.put("PRO_ID",data.getString("PRO_ID")); 
			      	   backup.put("USER_ID",data.getString("USER_ID"));
			      	   backup.put("DATE", new Date()); 
			      	   backup.put("OPER_BY",data.getString("USER_ID"));
			      	   backup.put("DESCRIPTION","项目审核中！");  
			      	   backup.put("TARGET_ID",""); 
		    		   //保存项目反馈信息  
			      	   Object re=dao.save("AppProBackupMapper.save",backup); 
			      	   if(Integer.valueOf(re.toString()) >= 1){ 
			      		    PageData type=new PageData();
			      		    type.put("SERVICE_TYPE","10");
			      		    List<PageData> users =(List<PageData>) dao.findForList("AppUsersMapper.queryByType",type);
			      		    if( users != null && users.size()>=1){
								String content="有用户发布项目信息，快去后台查看吧！";
								push(users.get(0),content);
							}
			      		  String notice1=vv3;
			              //实时更新后台管理消息
			      		noticePushutil notutil=new noticePushutil();
		    	        notutil.toNotice(notice1);
			      		    
			      		    
		 					PageData res=new PageData();
							res.put("code","200");
							res.put("PRO_ID",id);
							return JSONArray.toJSONString(res).toString();   
			 			}else{
			 				return null;  
			 			}
					}else{
						return null;  
					} 
	    	   }else{
	    		   return null;  
	    	   }
	        }
	     }
	    return null;  
	}  
		 
	/**
	 * 删除项目
	 * @param pd 
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String deletePro(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object obj =dao.delete("AppProjectMapper.delete",data);
		    if(Integer.valueOf(obj.toString()) >= 1){ 
		    	PageData res=new PageData();
		    	res.put("code","200");
		    	return JSONArray.toJSONString(res).toString();   
			}else{
				return null;  
			} 
		}  
		return null; 
	} 
	
	/**
	 * 添加查看项目信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String AddViews(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData isCk =(PageData) this.dao.findForObject("AppXmckMapper.chenckIsCk",data);//判断是否已经查看
			if(isCk==null){//没有查看
				String id=UuidUtil.get32UUID();
				data.put("ID", id);
				data.put("DATE", new Date()); 
				data.put("VIEW_COUNT",0);  
				Object obj=dao.save("AppXmckMapper.save",data);
				if(Integer.valueOf(obj.toString()) >= 1){ 
 					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();   
				}else{
					return null;
				} 
			}else{//已经查看，修改查看次数 
 				Object ob=dao.update("AppXmckMapper.updateCounts",data); 
 				if(Integer.valueOf(ob.toString()) >= 1){
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
	 * 查询项目查询条件列表（地域、项目类型、项目阶段）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
//	@Override
	public String querySearchParam(PageData pd) throws Exception {
		long startTime=System.currentTimeMillis();
		if(!EmptyUtil.isNullOrEmpty(pd)){ 
			PageData datas= pd.getObject("params"); 
			if( datas != null ){
				if( datas.get("DISTINGUISH")==null || "".equals(datas.get("DISTINGUISH"))){
					datas.put("DISTINGUISH", 1);
				}
			}else{
				datas = new PageData();
				datas.put("DISTINGUISH", 1);
			}
			
			PageData data=new PageData();
			data.put("pid","0"); 
		    List<PageData> areas =(List<PageData>) this.dao.findForList("SysAreaMapper.querybyPid",data); //查询全部的省级地区
		    List<PageData> cates=(List<PageData>) this.dao.findForList("AppCategoryMapper.listByPId","0");//查询一级类型
		    /*for(PageData pagedata:cates){
		    	List<PageData> child=(List<PageData>) this.dao.findForList("AppCategoryMapper.listByPId",pagedata.getString("value"));//查询一级类型
		    	pagedata.put("children",child); 
		    } */
		    List<PageData> types =(List<PageData>) this.dao.findForList("AppXmlxMapper.queryByParam",data); //查询项目的一级类型
		    for(PageData pagedata:types){
		    	data.put("pid",pagedata.getString("ID"));
		    	List<PageData> lxs =(List<PageData>) this.dao.findForList("AppXmlxMapper.queryByParam",data); //查询全部的省级地区
		    	pagedata.put("lxs",lxs);
		    } 
		    for(PageData pagedata:areas){
		    	data.put("pid",pagedata.getString("areacode"));
		    	List<PageData> citys =(List<PageData>) this.dao.findForList("SysAreaMapper.querybyPid",data); //查询全部的省级地区
		    	pagedata.put("citys",citys);
		    } 
		    List<PageData> stages=null;
		    if(datas.get("DISTINGUISH") != null){
		    	 Object DISTINGUISH=datas.get("DISTINGUISH");  
				    if(DISTINGUISH!=null&&!"".equals(DISTINGUISH)){
				    	if("2".equals(DISTINGUISH)){
				    		stages =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","pppstage"); //查询EPC项目的阶段
				    	}else {
				    		stages =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","stage"); //查询项目的阶段
						}
//					   data.put("DISTINGUISH",Double.parseDouble(asd+""));
				    	
			        }else{
			        	stages =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","stage"); //查询项目的阶段
			        }
		    }else{
		    	stages =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","stage"); //查询项目的阶段
		    }
		   
	    	   
	        
//		    List<PageData> stages =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","stage"); //查询项目的阶段
		    PageData res=new PageData();
		    res.put("types",types);
		    res.put("areas",areas);
		    res.put("cates",cates);
		    res.put("stages",stages);
		    long entTime=System.currentTimeMillis();
			long spendMills=entTime-startTime; 
			System.out.print("querySearchParam接口 ：耗费时间"+ spendMills+" 毫秒 ，合计"+(spendMills/1000.00) +"秒！！！！！！！");
			return JSONArray.toJSONString(res).toString(); 
		}
		return null;    
	} 

	/**
	 * 查询订阅项目地区
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public String queryDyDq(PageData pd) throws Exception {

		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			data.put("pid","0"); 
		    List<PageData> areas =(List<PageData>) this.dao.findForList("SysAreaMapper.queryDybyPid",data); //查询全部的省级地区和订阅信息
		    for(PageData pagedata:areas){
		    	data.put("pid",pagedata.getString("areacode"));
		    	List<PageData> citys =(List<PageData>) this.dao.findForList("SysAreaMapper.queryDybyPid",data); //查询全部的省级地区
		    	pagedata.put("citys",citys);
		    }  
			return JSONArray.toJSONString(areas).toString(); 
		}
		return null;  
	}
	
	/**
	 * 查询订阅项目类型
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public String queryDyLx(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			data.put("pid","0"); 
		    List<PageData> types =(List<PageData>) this.dao.findForList("AppXmlxMapper.queryDyLx",data); //查询一级类型和订阅信息
		    for(PageData pagedata:types){
		    	data.put("pid",pagedata.getString("ID"));
		    	List<PageData> childs =(List<PageData>) this.dao.findForList("AppXmlxMapper.queryDyLx",data); //查询二级类型和订阅信息
		    	pagedata.put("childs",childs);
		    }  
			return JSONArray.toJSONString(types).toString(); 
		}
		return null;  
	}
	
	/**
	 * 查询订阅项目阶段
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public String queryDySche(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			List<PageData> sches =(List<PageData>) this.dao.findForList("AppProScheMapper.queryDysche",data); //查询用户订阅的阶段
			return JSONArray.toJSONString(sches).toString(); 
		}
		return null;  
	}
	
	/**
	 * 查询订阅项目行业
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryDyCate(PageData pd) throws Exception {  
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			data.put("pid","0"); 
		    List<PageData> cates =(List<PageData>) this.dao.findForList("AppDyHyMapper.queryDyHy",data); //查询一级行业和订阅信息
		   /* for(PageData pagedata:cates){
		    	data.put("pid",pagedata.getString("ID"));
		    	List<PageData> childs =(List<PageData>) this.dao.findForList("AppDyHyMapper.queryDyHy",data); //查询二级行业和订阅信息
		    	pagedata.put("childs",childs);
		    }  */
			return JSONArray.toJSONString(cates).toString(); 
		}
		return null;  
	}

	/**
	 * 查询项目的所有类型和地区，二级
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryXmLx(PageData pd) throws Exception { 
		if(!EmptyUtil.isNullOrEmpty(pd)){  
			List<PageData> types=(List<PageData>) this.dao.findForList("AppXmlxMapper.listByPId","0");//查询一级类型
		    for(PageData pagedata:types){
		    	List<PageData> child=(List<PageData>) this.dao.findForList("AppXmlxMapper.listByPId",pagedata.getString("value"));//查询一级类型
		    	pagedata.put("children",child); 
		    } 
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
		    List<PageData> cates=(List<PageData>) this.dao.findForList("AppCategoryMapper.listByPId","0");//查询一级类型
		   /* for(PageData pagedata:cates){
		    	List<PageData> child=(List<PageData>) this.dao.findForList("AppCategoryMapper.listByPId",pagedata.getString("value"));//查询一级类型
		    	pagedata.put("children",child); 
		    } */
		    PageData res=new PageData();
		    res.put("types",types);
		    res.put("areas",areas);
		    res.put("cates",cates);
			return JSONArray.toJSONString(res).toString(); 
		}
		return null;  
	} 
	
	/**
	 * 查询项目的所有类型和地区，二级(新）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryNewXmLx(PageData pd) throws Exception { 
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData res=new PageData();
			
			PageData xmlxs=new PageData();
			//比较版本号
			boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_XMLX, data);
			//获取返回数据
			DataConvertUtil.getRes(version, ConstantUtil.VERSION_XMLX, xmlxs);  
			
			PageData areas=new PageData();
			//比较版本号
			boolean version2=CacheHandler.getCompareVersion(ConstantUtil.VERSION_AREA, data);
			//获取返回数据
			DataConvertUtil.getRes(version2, ConstantUtil.VERSION_AREA, areas);  
			 
			PageData cates=new PageData();
			//比较版本号
			boolean version3=CacheHandler.getCompareVersion(ConstantUtil.VERSION_CATE, data);
			//获取返回数据
			DataConvertUtil.getRes(version3, ConstantUtil.VERSION_CATE, cates);  
		  
			res.put("xmlxs",xmlxs);
			res.put("areas",areas);
			res.put("cates",cates);
			return JSONArray.toJSONString(res).toString();   
		}
		return null;   
	} 
	
	/**
	 * 查询搜索热词
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryHotWords(PageData pd) throws Exception{
		if(!EmptyUtil.isNullOrEmpty(pd)){  
			List<PageData> words=(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","hot_word");//查询项目搜索热词
			return JSONArray.toJSONString(words).toString(); 
		}
		return null;  
	}
	
	/**
	 * 查询搜索热词(新）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryNewHotWords(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			PageData res=new PageData();
		 
			//比较版本号
			boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_DIC, data);
			//获取返回数据
			DataConvertUtil.getDicRes(version,new String[]{"hot_word"},res); 
			
			return JSONArray.toJSONString(res).toString(); 
		}
		return null;  
	} 
	
	/**
	 * 查询订阅信息显示在页面
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryDys(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			data.put("BIANMA","stage"); 
		    List<PageData> sches =(List<PageData>) this.dao.findForList("AppProScheMapper.queryDysName",data); //查询用户订阅的阶段名称
		    List<PageData> types =(List<PageData>) this.dao.findForList("AppXmlxMapper.queryDyCname",data); //查询订阅类型名称信息
		    List<PageData> areas =(List<PageData>) this.dao.findForList("SysAreaMapper.queryDyName",data); //查询订阅地区名称信息
		    List<PageData> cates =(List<PageData>) this.dao.findForList("AppCategoryMapper.queryDysName",data); //查询订阅行业信息
		    PageData res=new PageData();
		    res.put("sches",sches);
		    res.put("types",types);
		    res.put("areas",areas);
		    res.put("cates",cates);
		    return JSONArray.toJSONString(res).toString(); 
		} 
		return null;  
	}
	
	/**
	 * 编辑（添加、修改）订阅地区信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editDyDq(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			List<PageData> dqs=data.getList("dqs"); 
			int mun=0;
			for(PageData dq:dqs){
				if(dq.getString("DQ_ID")!=null&&!"".equals(dq.getString("DQ_ID"))&&!"undefined".equals(dq.getString("DQ_ID"))){
					if("0".equals(((Object)dq.get("FLAG")).toString())){//删除 
						Object ob=dao.delete("AppDyDqMapper.delete",dq);
						mun+=Integer.valueOf(ob.toString());
					}else{//保留
						mun+=Integer.valueOf(1);
					} 
				}else{ 
					dq.put("ID",UuidUtil.get32UUID());
					dq.put("USER_ID",data.getString("USER_ID")); 
					dq.put("DATE",new Date());
					Object ob=dao.save("AppDyDqMapper.save",dq);
					mun+=Integer.valueOf(ob.toString());
				}
			} 
			if(mun==dqs.size()){ 
				PageData res=new PageData();
				res.put("code","200");
				return JSONArray.toJSONString(res).toString();   
 			}else{
 				return null;  
 			}
		}
		return null;    
	}
	
	/**
	 * 编辑（添加、删除）订阅类型信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editDyLx(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			List<PageData> lxs=data.getList("lxs");
			int mun=0;
			for(PageData lx:lxs){
				if(lx.getString("LX_ID")!=null&&!"".equals(lx.getString("LX_ID"))&&!"undefined".equals(lx.getString("LX_ID"))){
					if("0".equals(((Object)lx.get("FLAG")).toString())){//删除 
						Object ob=dao.delete("AppDyLxMapper.delete",lx);
						mun+=Integer.valueOf(ob.toString());
					}else{//保留
						mun+=Integer.valueOf(1);
					} 
				}else{ 
					lx.put("ID",UuidUtil.get32UUID());
					lx.put("USER_ID",data.getString("USER_ID")); 
					lx.put("DATE",new Date());
					Object ob=dao.save("AppDyLxMapper.save",lx);
					mun+=Integer.valueOf(ob.toString());
				}
			} 
			if(mun==lxs.size()){ 
				PageData res=new PageData();
				res.put("code","200");
				return JSONArray.toJSONString(res).toString();   
 			}else{
 				return null;  
 			}
		}
		return null;  
	}
	
	/**
	 * 编辑（添加、删除）订阅行业信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editDyHy(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			List<PageData> hys=data.getList("hys");
			int mun=0;
			for(PageData hy:hys){
				if(hy.getString("HY_ID")!=null&&!"".equals(hy.getString("HY_ID"))&&!"undefined".equals(hy.getString("HY_ID"))){
					if("0".equals(((Object)hy.get("FLAG")).toString())){//删除 
						Object ob=dao.delete("AppDyHyMapper.delete",hy);
						mun+=Integer.valueOf(ob.toString());
					}else{//保留
						mun+=Integer.valueOf(1);
					} 
				}else{ 
					hy.put("ID",UuidUtil.get32UUID());
					hy.put("USER_ID",data.getString("USER_ID")); 
					hy.put("DATE",new Date());
					Object ob=dao.save("AppDyHyMapper.save",hy);
					mun+=Integer.valueOf(ob.toString());
				}
			} 
			if(mun==hys.size()){ 
				PageData res=new PageData();
				res.put("code","200");
				return JSONArray.toJSONString(res).toString();   
 			}else{
 				return null;  
 			}
		}
		return null;  
	}
	
	/**
	 * 编辑（添加、删除）订阅阶段信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editDyJd(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			List<PageData> jds=data.getList("jds");
			int mun=0;
			for(PageData jd:jds){
				if(jd.getString("JD_ID")!=null&&!"".equals(jd.getString("JD_ID"))&&!"undefined".equals(jd.getString("JD_ID"))){
					if("0".equals(((Object)jd.get("FLAG")).toString())){//删除 
						Object ob=dao.delete("AppDyJdMapper.delete",jd);
						mun+=Integer.valueOf(ob.toString());
					}else{//保留
						mun+=Integer.valueOf(1);
					} 
				}else{ 
					jd.put("ID",UuidUtil.get32UUID());
					jd.put("USER_ID",data.getString("USER_ID")); 
					jd.put("DATE",new Date());
					Object ob=dao.save("AppDyJdMapper.save",jd);
					mun+=Integer.valueOf(ob.toString());
				}
			} 
			if(mun==jds.size()){ 
				PageData res=new PageData();
				res.put("code","200");
				return JSONArray.toJSONString(res).toString();   
 			}else{
 				return null;  
 			}
		}
		return null;  
	}
	
	/**
	 * 参与项目时需验证用户的三证是否已经审核通过，
	 * 已经通过才可以进行参与
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String validCerts(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			data.put("CERTIFICATE_TYPE","1");
		    PageData certs =(PageData) this.dao.findForObject("AppCertsMapper.querySanZh",data); //查询用户的三证信息
			return JSONArray.toJSONString(certs).toString(); 
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
				bidders =(List<PageData>) dao.findForList("AppProBidderMapper.queryBidders",data);
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
	
	/**添加项目代发信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String saveXmDf(PageData pd) throws Exception{ 
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			//获取notice.properties noticeText3内容。
	 		String vv3=new String(noticeText3.getBytes("ISO-8859-1"),"UTF-8");
	 		
			data.put("ID",UuidUtil.get32UUID()); 
			data.put("CREATE_DATE",new Date());
			data.put("STATUS","01"); 
			data.put("ISSYSLOOK","0"); 
			Object ob=dao.save("AppXmDfMapper.save",data); 
			if(Integer.valueOf(ob.toString())>0){ 
				PageData type=new PageData();
      		    type.put("SERVICE_TYPE","10");
      		    List<PageData> users =(List<PageData>) dao.findForList("AppUsersMapper.queryByType",type);
      		    if( users != null && users.size()>=1){
      		    	String content="有用户请求建联代发项目信息，快去后台查看吧！";
					push(users.get(0),content);
				} 
      		    String notice1=vv3;
	  	 		//实时更新后台管理消息
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
		}
		return null;  
	}
	
	/**
	 * 未登录参与交易
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addBid(PageData pd) throws Exception{
		PageData data = pd.getObject("params");   if(pd.get("Token")!=null&&!"".equals(pd.get("Token"))){
			PageData token = (PageData) this.appCommonService.findtokenDetail(pd);
			if(token!=null){
				if(token.getString("userid")!=null){//已经登录
					data.put("USER_ID",token.getString("userid"));
					return loginBidder(data);  
				}else{//未登录
					return bidder(data); 
				} 
			}else{//未登录
				return bidder(data); 
			}
		}else{//未登录
			return bidder(data); 
		}
	} 
	
	public String loginBidder(PageData data) throws Exception{
		PageData isbid =(PageData) this.dao.findForObject("AppProBidderMapper.chenckIsIn",data);//判断是否参与
		if(isbid==null){//没有参与
			//获取notice.properties noticeText1内容。
			String vv3=new String(noticeText1.getBytes("ISO-8859-1"),"UTF-8");
			
			String id=UuidUtil.get32UUID();
			data.put("ID", id);
			data.put("BID_DATE", new Date()); 
			Object obj=dao.save("AppProBidderMapper.addbidder",data);
			if(Integer.valueOf(obj.toString()) >= 1){
				PageData pro=(PageData) this.dao.findForObject("AppProjectMapper.queryByProId",data);//查询点赞数
 				int  count=Integer.parseInt(pro.get("TOTAL_BIDDER")+"");  
					data.put("TOTAL_BIDDER",count+1);   
 				Object ob=dao.update("AppProjectMapper.updateCounts",data);
 				if(Integer.valueOf(ob.toString()) >= 1){
 					//推送消息
					PageData user=(PageData) this.dao.findForObject("AppProjectMapper.queryManager",data);//查询
	    	 		if( user != null ){
	    	 			String content="又有用户参与"+user.getString("PROJECT_NAME")+"项目了，快去后台查看吧！";
	    	 			//推送审核消息
	    	 			String jsonStr = "{'type':'notice','title':'上海建联','content':'"+content+"'}";//透传内容
	    				TransmissionTemplate tmTemplate = PushUtil.transmissionTemplateDemo("上海建联",content,jsonStr);
						PushUtil pushApp=new PushUtil();
						String alias = user.getString("ID");
						pushApp.pushToSingle(tmTemplate,alias);  
	 				} 
	    	 		String notice1=user.getString("PROJECT_NAME")+vv3;
	    	 		//实时更新后台管理消息
	    	 		noticePushutil notutil=new noticePushutil();
	    	        notutil.toNotice(notice1);
	    	 		
 					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();  
				}else{
					return null;
				} 
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public String bidder(PageData data) throws Exception{ 
		PageData isbid=null;
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData user =(PageData) this.dao.findForObject("AppUsersMapper.queryByPhone",data.getString("PHONE"));//根据手机号码查询会员信息
			if(user!=null){//用户存在
				data.put("USER_ID",user.getString("ID"));
				isbid =(PageData) this.dao.findForObject("AppProBidderMapper.checkPhoneIsIn",data);//判断是否参与
				if(isbid==null){//没有参与
					String id=UuidUtil.get32UUID();
					data.put("ID", id);
					data.put("BID_DATE", new Date()); 
					Object obj=dao.save("AppProBidderMapper.addbidder",data);
					if(Integer.valueOf(obj.toString()) >= 1){
						PageData pro=(PageData) this.dao.findForObject("AppProjectMapper.queryByProId",data);//查询点赞数
		 				int  count=Integer.parseInt(pro.get("TOTAL_BIDDER")+"");  
	 					data.put("TOTAL_BIDDER",count+1);   
		 				Object ob=dao.update("AppProjectMapper.updateCounts",data);
		 				if(Integer.valueOf(ob.toString()) >= 1){ 
		 					PageData res=new PageData();
							res.put("code","200");
							return JSONArray.toJSONString(res).toString();   
		 				}else{
		 					return null;  
		 				} 
					}else{
						return null;
					} 
				}else{
					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString(); 
				} 
			}else{//用户不存在，添加进数据库 
				/*PageData pad = new PageData();   
				String id=UuidUtil.get32UUID();
				String REAL_NAME="上海建联"+RandomNum.getFiveRadom();
				pad.put("ID", id);
				pad.put("PHONE",data.getString("PHONE"));  
				pad.put("REAL_NAME",REAL_NAME); 
				pad.put("VALERIE", cte.getPingYinToHeaderChar(REAL_NAME)); 
				pad.put("REGISTER_DATE",new Date());  
				pad.put("USER_NAME",data.getString("PHONE"));  
				pad.put("TOTAL_POINTS",0);
				pad.put("STATUS","01"); 
				pad.put("IS_VIP",0);  
				pad.put("VIP_LEVEL","02"); 
				Object re = this.dao.save("AppUsersMapper.register",pad);  
				if(Integer.valueOf(re.toString()) >= 1){ */
					data.put("ID",UuidUtil.get32UUID());
					data.put("USER_ID",null);
					data.put("BID_DATE", new Date()); 
					Object obj=dao.save("AppProBidderMapper.addbidder",data);
					if(Integer.valueOf(obj.toString()) >= 1){
						PageData pro=(PageData) this.dao.findForObject("AppProjectMapper.queryByProId",data);//查询竞标总数
		 				int  count=Integer.parseInt(pro.get("TOTAL_BIDDER")+"");  
	 					data.put("TOTAL_BIDDER",count+1);   
		 				Object ob=dao.update("AppProjectMapper.updateCounts",data);
		 				if(Integer.valueOf(ob.toString()) >= 1){
		 					//获取notice.properties noticeText2内容。
		 			       String vv3=new String(noticeText1.getBytes("ISO-8859-1"),"UTF-8");
		 					//推送消息
							PageData manager=(PageData) this.dao.findForObject("AppProjectMapper.queryManager",data);//查询
			    	 		if( manager != null ){
			    	 			String content="又有用户参与"+manager.getString("PROJECT_NAME")+"项目了，快去后台查看吧！";
			    	 			//推送审核消息
			    	 			String jsonStr = "{'type':'notice','title':'上海建联','content':'"+content+"'}";//透传内容
			    				TransmissionTemplate tmTemplate = PushUtil.transmissionTemplateDemo("上海建联",content,jsonStr);
								PushUtil pushApp=new PushUtil();
								String alias = manager.getString("ID");
								pushApp.pushToSingle(tmTemplate,alias);
			    	 		}
			    	 		String notice1=manager.getString("PROJECT_NAME")+vv3;
			    	           //实时更新后台管理消息
			    	 		noticePushutil notutil=new noticePushutil();
			    	        notutil.toNotice(notice1);
			    	 		
			    	 		
		 					PageData res=new PageData();
							res.put("code","200"); 
							return JSONArray.toJSONString(res).toString();   
		 				}else{
		 					return null;  
		 				} 
					}else{
						return null;
					} 
			   /* }else{
			    	PageData res=new PageData();
					res.put("code","400");
					return JSONArray.toJSONString(res).toString();  
			    } */
			} 
		}
		return null;  
	}
	 
	/**
	 * 未登录取消参与交易
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String delBid(PageData pd) throws Exception{
		PageData data = pd.getObject("params");  
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData user =(PageData) this.dao.findForObject("AppUsersMapper.queryByPhone",data.getString("PHONE"));//根据手机号码查询会员信息
			Object obj=0;
			if(user!=null){//用户存在
				data.put("USER_ID",user.getString("ID"));  
				obj =dao.delete("AppProBidderMapper.deletebidder",data); 
			}else{
				obj =dao.delete("AppProBidderMapper.canclebidder",data); 
			}
			if(Integer.valueOf(obj.toString()) >= 1){ 
 		    	PageData pro=(PageData) this.dao.findForObject("AppProjectMapper.queryByProId",data);//查询点赞数
 				int  count=Integer.parseInt(pro.get("TOTAL_BIDDER")+"");  
 				if(count>1){ 
					data.put("TOTAL_BIDDER",count-1);  
				}else{ 
					data.put("TOTAL_BIDDER",0);  
				} 
 				Object ob=dao.update("AppProjectMapper.updateCounts",data); 
 				if(Integer.valueOf(ob.toString()) >= 1){
 					//推送消息
					/*PageData manager=(PageData) this.dao.findForObject("AppProjectMapper.queryManager",data);//查询
	    	 		if( manager != null ){
	    	 			String content="有用户取消参与"+manager.getString("PROJECT_NAME")+"项目了，快去后台查看吧！";
	    	 			//推送审核消息
	    	 			String jsonStr = "{'type':'notice','title':'上海建联','content':'"+content+"'}";//透传内容
	    				TransmissionTemplate tmTemplate = PushUtil.transmissionTemplateDemo("上海建联",content,jsonStr);
						PushUtil pushApp=new PushUtil();
						String alias = manager.getString("ID");
						pushApp.pushToSingle(tmTemplate,alias);
	    	 		}*/
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
		return null;   
	} 
	
	/**查询项目流程进度
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryProProce(PageData pd) throws Exception{
		PageData data = pd.getObject("params");  
		PageData res=new PageData();
		if(!EmptyUtil.isNullOrEmpty(data)){  
			data.put("LOCATION_NO","13");
			List<PageData> advs=(List<PageData>) dao.findForList("AppRotationMapper.queryByColno",data);//查询轮换大图
			PageData pro =(PageData) this.dao.findForObject("AppProjectMapper.queryByProId",data);//查询交易信息
			PageData phone =(PageData) this.dao.findForObject("ServiceTelMapper.findBm","02");//查询交易下单成功的客服电话
			if(phone!=null&&pro!=null){
				pro.put("SERVICE_PHONE",phone.getString("PHONE"));
			}else{
				pro.put("SERVICE_PHONE",null);
			} 
			res.put("pro",pro);
			res.put("advs",advs);
			Object userid=data.get("USER_ID");
			PageData bidder=null;
			if(userid!=null&&!"".equals(userid)){
				if(userid.toString().equals(pro.getString("USER_ID"))){//是发布人自己查看
					res.put("code","200");
					res.put("bidder",null);  
					res.put("isCy",null); 
				}else{
					bidder =(PageData) this.dao.findForObject("AppProBidderMapper.chenckIsIn",data);//查询参与信息
					PageData isCy =(PageData) this.dao.findForObject("AppXzcyMapper.chenckIsIn",data);//查询讨论小组信息
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
					bidder =(PageData) this.dao.findForObject("AppProBidderMapper.chenckIsInByPhone",data);//查询参与信息
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
	
	public void push(PageData user,String content){ 
		String jsonStr = "{'type':'notice','title':'上海建联','content':'"+content+"'}";//透传内容
		TransmissionTemplate tmTemplate = PushUtil.transmissionTemplateDemo("上海建联",content,jsonStr);
		PushUtil pushApp=new PushUtil();
		String alias = user.getString("ID");
		try {
			pushApp.pushToSingle(tmTemplate,alias);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
	
	/**查询下单页面的客服电话
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryServiceTel(PageData pd) throws Exception{
		PageData pro =(PageData) this.dao.findForObject("ServiceTelMapper.findBm","01");//查询交易下单页面的客服电话
		return JSONArray.toJSONString(pro).toString();  
	}
	
	/**
	 * 查询项目查询条件列表（地域、项目类型、项目阶段）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewSearchParam(PageData pd) throws Exception { 
		long startTime=System.currentTimeMillis();
		if(!EmptyUtil.isNullOrEmpty(pd)){ 
			PageData data= pd.getObject("params");
			
			PageData res=new PageData(); 
			if(data != null ){
				if( data.get("DISTINGUISH")==null || "".equals(data.get("DISTINGUISH"))){
					res.put("stages",CacheHandler.getDics("stage"));
				}else{
					if("2".equals(data.getString("DISTINGUISH"))){
						res.put("stages",CacheHandler.getDics("epcstage")); 
			    	}else {
			    		res.put("stages",CacheHandler.getDics("stage"));
					} 	
				} 
			}else{ 
				res.put("stages",CacheHandler.getDics("stage"));
			}  
			PageData xmlxs=new PageData(); 
			//比较版本号
			boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_XMLX, data);
			//获取返回数据
			DataConvertUtil.getRes(version, ConstantUtil.VERSION_XMLX, xmlxs);  
			PageData areas=new PageData(); 
			//比较版本号
			boolean version2=CacheHandler.getCompareVersion(ConstantUtil.VERSION_AREA, data);
			//获取返回数据
			DataConvertUtil.getRes(version2, ConstantUtil.VERSION_AREA, areas);  
			 
			PageData cates=new PageData(); 
			//比较版本号
			boolean version3=CacheHandler.getCompareVersion(ConstantUtil.VERSION_CATE, data);
			//获取返回数据
			DataConvertUtil.getRes(version3, ConstantUtil.VERSION_CATE, cates);   
			 
			res.put("xmlxs",xmlxs);
			res.put("areas",areas);
			res.put("cates",cates);
			long last=System.currentTimeMillis();
			long lasttime=last-startTime;
			log.info("接口queryNewSearchParam 最后一次耗费时间"+ lasttime+" 毫秒 ，合计"+(lasttime/1000.00) +"秒！");
			return JSONArray.toJSONString(res).toString(); 
		}
		return null;    
	} 
	
	//获取项目编号
	public PageData getProNo(PageData data){ 
		try {
			PageData cate = (PageData) dao.findForObject("AppCategoryMapper.queryNameById",data);
			if(cate!=null){
				String pro_no=ProNoUtil.getPartProNo(cate.getString("NAME")); 
				data.put("pro_no",pro_no);
		   	    PageData maxNo =(PageData) dao.findForObject("AppProjectMapper.queryMaxProNo",data);
		   	    if(maxNo!=null&&!"".equals(maxNo.getString("PRO_NO"))){
		   		   data.put("PRO_NO",pro_no+ProNoUtil.getProNo(maxNo.getString("PRO_NO")));  
		   	    }else{
		   		   data.put("PRO_NO",pro_no+"0001");
		   	    }  
			}else{
				log.error("项目没有所属行业！");
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		return data;
	}
	
	//获取需求编号
	public PageData getResNo(PageData data){ 
		try { 
			String pro_no=ProNoUtil.getPartResNo();
			data.put("pro_no",pro_no);
	   	    PageData maxNo =(PageData) dao.findForObject("AppProjectMapper.queryMaxProNo",data);
	   	    if(maxNo!=null&&!"".equals(maxNo.getString("PRO_NO"))){
	   		   data.put("PRO_NO",pro_no+ProNoUtil.getProNo(maxNo.getString("PRO_NO")));  
	   	    }else{
	   		   data.put("PRO_NO",pro_no+"0001");
	   	    }  
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		return data;
	} 
}
