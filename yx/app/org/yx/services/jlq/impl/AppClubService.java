package org.yx.services.jlq.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData; 
import org.yx.common.utils.DateUtil;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;
import org.yx.services.jlq.inter.AppClubServiceInter; 
import org.yx.util.ConstantUtil;
import org.yx.util.DataConvertUtil;
import org.yx.util.ResultResetUtil;
import org.yx.util.noticeConfig;
import org.yx.util.noticePushutil;

import com.alibaba.fastjson.JSONArray;

@Service("appClubService")
public class AppClubService  implements AppClubServiceInter{ 

	@Resource(name = "daoSupport")
	private DaoSupport dao;  
	
	
	public static String noticeText6 = noticeConfig.getString("noticeText6");
	public static String noticeText7 = noticeConfig.getString("noticeText7");
	public static String noticeText9 = noticeConfig.getString("noticeText9");
	public static String noticeText13 = noticeConfig.getString("noticeText13");
	/**
	 * 查看俱乐部首页列表信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryIndexInfo(PageData pd) throws Exception{ 
		if(!EmptyUtil.isNullOrEmpty(pd)){ 
			pd.put("LOCATION_NO","04");
		    List<PageData> rotations=(List<PageData>) this.dao.findForList("AppRotationMapper.queryByColno", pd);//获取俱乐部页面顶部的轮换大图
		    List<PageData> acts =(List<PageData>) dao.findForList("AppActivityMapper.queryIndexInfo",null);
		    PageData res=new PageData();
		    res.put("rotations", rotations);
		    res.put("acts", acts);
		    return JSONArray.toJSONString(res).toString();
		}
		return null;  
	}
	
	
	
	/**
	 * 添加活动/修改活动
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editActivity(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			/*data.put("LIMIT_COUNT",data.get("LIMIT_COUNT")==null?0:Integer.parseInt(data.get("LIMIT_COUNT").toString()));
			data.put("PRICE",data.get("PRICE")==null?0:Double.parseDouble(data.get("PRICE").toString()));
			data.put("VIP_PRICE",data.get("VIP_PRICE")==null?0:Double.parseDouble(data.get("VIP_PRICE").toString()));
			data.put("VICE_PRE_PRICE",data.get("VICE_PRE_PRICE")==null?0:Double.parseDouble(data.get("VICE_PRE_PRICE").toString()));
			data.put("EXE_VICE_PRE_PRICE",data.get("EXE_VICE_PRE_PRICE")==null?0:Double.parseDouble(data.get("EXE_VICE_PRE_PRICE").toString()));
			data.put("PRESIDENT_PRICE",data.get("PRESIDENT_PRICE")==null?0:Double.parseDouble(data.get("PRESIDENT_PRICE").toString()));*/
			ResultResetUtil.resetInt(data,new String[]{"LIMIT_COUNT","VIP_PRICE","VICE_PRE_PRICE","EXE_VICE_PRE_PRICE","PRESIDENT_PRICE"});
	        data.put("STATUS","01");
	        data.put("CREATE_DATE",new Date()); 
	        if(data.getString("ACT_ID")!=null&&!"".equals(data.getString("ACT_ID"))){//修改 
	    	   Object obj=dao.update("AppActivityMapper.edit",data);
	    	   if(Integer.valueOf(obj.toString()) >= 1){  
	    		    int mun=0;
					List<PageData> levels=data.getList("levels");
					PageData dels=new PageData();
					dels.put("ACT_ID",data.getString("ACT_ID"));
					Object object = this.dao.delete("AppActLevelMapper.delete",dels); //删除项目的图片
					if(Integer.valueOf(object.toString()) >= 1){
						for(PageData level:levels){ 
							level.put("ID",UuidUtil.get32UUID());
							level.put("ACT_ID", data.getString("ACT_ID"));
							level.put("DATE",new Date());
							Object ob=dao.save("AppActLevelMapper.save",level);
							mun+=Integer.valueOf(ob.toString());
						}  
						if(mun==levels.size()){
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
	        }else{//新增活动信息 
			   data.put("ACT_ID",UuidUtil.get32UUID());    
	    	   Object obj=dao.update("AppActivityMapper.save",data); 
	    	   if(Integer.valueOf(obj.toString()) >= 1){
	    		   int mun=0;
					List<PageData> levels=data.getList("levels");
					for(PageData level:levels){ 
						level.put("ID",UuidUtil.get32UUID());
						level.put("ACT_ID", data.getString("ACT_ID"));
						level.put("DATE",new Date());
						Object ob=dao.save("AppActLevelMapper.save",level);
						mun+=Integer.valueOf(ob.toString());
					}  
					if(mun==levels.size()){
						//获取notice.properties noticeText2内容。
					       String vv3=new String(noticeText7.getBytes("ISO-8859-1"),"UTF-8");
					    String notice1=vv3;
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
	        }
	     }
	    return null;  
	}
	
	/**
	 * 删除活动
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String delActivity(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object obj =dao.delete("AppActivityMapper.delete",data);
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
	 * 查询会员等级
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryVipLevel(PageData pd) throws Exception{ 
		if(!EmptyUtil.isNullOrEmpty(pd)){   
		    List<PageData> viptype =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","viptype"); //查询会员等级
		    List<PageData> actType =(List<PageData>) this.dao.findForList("AppDicMapper.queryBM","activitytype"); //查询活动类型
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
		    res.put("viptype",viptype);
		    res.put("actType",actType);
		    res.put("areas",areas);
			return JSONArray.toJSONString(res).toString();   
		}
		return null;    
	}
	
	/**
	 * 查询会员等级(新)
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryNewVipLevel(PageData pd){  
		if(!EmptyUtil.isNullOrEmpty(pd)){  
			PageData data = pd.getObject("params");  
			PageData res=new PageData();
			
			PageData dics=new PageData(); 
			//比较版本号
			boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_DIC, data);
			//获取返回数据
			DataConvertUtil.getDicRes(version,new String[]{"viptype","activitytype"},dics);
			
			PageData areas=new PageData(); 
			//比较版本号
			boolean version2=CacheHandler.getCompareVersion(ConstantUtil.VERSION_AREA, data);
			//获取返回数据
			DataConvertUtil.getRes(version2, ConstantUtil.VERSION_AREA, areas);  
			
			res.put("dics",dics);
			res.put("areas",areas);
			return JSONArray.toJSONString(res).toString();   
		}
		return null;    
	}
	
	/**
	 * 根据ID查询活动详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryById(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData act =(PageData) this.dao.findForObject("AppActivityMapper.queryById",data);
		    if(act!=null){
		    	 List<PageData> isApply =(List<PageData>) this.dao.findForList("AppActUserMapper.checkIsApply",data);//判断是否已经参与
		    	 List<PageData> levels =(List<PageData>) this.dao.findForList("AppActLevelMapper.queryLevelByAId",data);//查询活动参与等级信息
		    	 if(isApply.size()<=0){
			    	act.put("isApply","0");//没有参与
		    	 }else{
			    	act.put("isApply","1");//已经参与
		    	 }
		    	 act.put("levels",levels);//已经参与
		    	 return JSONArray.toJSONString(act).toString();
		    }else{
		    	return null;  
		    } 
		}
		return null;  
	}
	
	/**
	 * 报名参加活动/取消参与
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String toPartAct(PageData pd) throws Exception{ 
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			List<PageData> acts =(List<PageData>) this.dao.findForList("AppActUserMapper.checkIsApply",data);//判断是否已经参与
			if(acts.size()<=0){//如果活动需要付费，则生成订单信息，订单支付成功，再新增参加信息，如果活动免费，直接新增参与信息
				PageData act=(PageData) this.dao.findForObject("AppActivityMapper.querySimpleById",data);//查询参与的活动信息
				
				PageData user=(PageData) this.dao.findForObject("AppUsersMapper.findById",data);//查询参与人信息
 				String level=user.getString("VIP_LEVEL");
 				if("01".equals(level)){
 					if(act.get("VIP_PRICE")!=null&&Double.parseDouble(act.get("VIP_PRICE")+"")>0){
 						PageData yzzf=new PageData();
 						yzzf.put("OBJECT_ID", act.getString("ID"));
 						yzzf.put("USER_ID", data.getString("USER_ID")); 
 						List<PageData> big=(List<PageData>) this.dao.findForList("AppOrdersMapper.queryByObj",yzzf);
 						if(big.size()>0){
 							for(PageData leobj:big){
 								Object delobj=this.dao.update("AppOrdersMapper.obdelete", leobj);
 							}
 								PageData order=new PageData(); 
 	 	 		 				order.put("ORDER_ID",UuidUtil.get32UUID());
 	 	 		 				order.put("USER_ID", data.getString("USER_ID")); 
 	 	 		 				order.put("STATUS","01");  
 	 	 		 				order.put("TYPE","02");  
 	 	 		 				order.put("OBJECT_ID",act.getString("ID"));  
 	 	 		 				order.put("EVENT","参加"+act.getString("ACT_NAME")+"活动费用");
 	 	 		 				order.put("DATE", new Date());
 	 	 						order.put("PRICE",Double.parseDouble(act.get("VIP_PRICE").toString()));
 	 	 						Object res=dao.save("AppOrdersMapper.save",order);
 	 	 						if(Integer.valueOf(res.toString()) >= 1){
 	 	 							return JSONArray.toJSONString(order).toString();
 	 	 						}else{
 	 	 							return null;
 	 	 						} 
 						}else {
 							PageData order=new PageData(); 
 	 		 				order.put("ORDER_ID",UuidUtil.get32UUID());
 	 		 				order.put("USER_ID", data.getString("USER_ID")); 
 	 		 				order.put("STATUS","01");  
 	 		 				order.put("TYPE","02");  
 	 		 				order.put("OBJECT_ID",act.getString("ID"));  
 	 		 				order.put("EVENT","参加"+act.getString("ACT_NAME")+"活动费用");
 	 		 				order.put("DATE", new Date());
 	 						order.put("PRICE",Double.parseDouble(act.get("VIP_PRICE").toString()));
 	 						Object res=dao.save("AppOrdersMapper.save",order);
 	 						if(Integer.valueOf(res.toString()) >= 1){
 	 							return JSONArray.toJSONString(order).toString();
 	 						}else{
 	 							return null;
 	 						} 
						}
 					}else{
 						String id=UuidUtil.get32UUID();
 						data.put("ID", id);
 						data.put("STATUS","01");
 						data.put("APPLY_DATE", new Date()); 
 						Object obj=dao.save("AppActUserMapper.addActUser",data);
 						if(Integer.valueOf(obj.toString()) >= 1){
 							int  count=Integer.parseInt(act.get("PART_COUNT")+"");  
 		 					data.put("PART_COUNT",count+1);   
 			 				Object ob=dao.update("AppActivityMapper.updateCounts",data);
 			 				if(Integer.valueOf(ob.toString()) >= 1){
 			 				//获取notice.properties noticeText10内容。
		 					       String vv3=new String(noticeText6.getBytes("ISO-8859-1"),"UTF-8");
			 					    String notice1=vv3;
			 				        noticePushutil notutil=new noticePushutil();
			 				        notutil.toNotice(notice1); 
 			 					PageData res=new PageData();
 			 					res.put("code","200");
	 							return JSONArray.toJSONString(res).toString();
	 						}else{
	 							return null;
	 						} 
 						}
 					} 
 				}
 				if("02".equals(level)){
 					if(act.get("PRICE")!=null&&Double.parseDouble(act.get("PRICE")+"")>0){
 						PageData yzzf=new PageData();
 						yzzf.put("OBJECT_ID", act.getString("ID"));
 						yzzf.put("USER_ID", data.getString("USER_ID")); 
 						List<PageData> big=(List<PageData>) this.dao.findForList("AppOrdersMapper.queryByObj",yzzf);
 						if(big.size()>0){
 							for(PageData leobj:big){
 								Object delobj=this.dao.update("AppOrdersMapper.obdelete", leobj);
 							}
 								PageData order=new PageData(); 
 		 		 				order.put("ORDER_ID",UuidUtil.get32UUID());
 		 		 				order.put("USER_ID", data.getString("USER_ID")); 
 		 		 				order.put("STATUS","01");  
 		 		 				order.put("TYPE","02");  
 		 		 				order.put("OBJECT_ID",act.getString("ID"));  
 		 		 				order.put("EVENT","参加"+act.getString("ACT_NAME")+"活动费用");
 		 		 				order.put("DATE", new Date());
 		 						order.put("PRICE",Double.parseDouble(act.get("PRICE").toString()));
 		 						Object res=dao.save("AppOrdersMapper.save",order);
 		 						if(Integer.valueOf(res.toString()) >= 1){
 		 							return JSONArray.toJSONString(order).toString();
 		 						}else{
 		 							return null;
 		 						}
 							
 						}else{
	 						PageData order=new PageData(); 
	 		 				order.put("ORDER_ID",UuidUtil.get32UUID());
	 		 				order.put("USER_ID", data.getString("USER_ID")); 
	 		 				order.put("STATUS","01");  
	 		 				order.put("TYPE","02");  
	 		 				order.put("OBJECT_ID",act.getString("ID"));  
	 		 				order.put("EVENT","参加"+act.getString("ACT_NAME")+"活动费用");
	 		 				order.put("DATE", new Date());
	 						order.put("PRICE",Double.parseDouble(act.get("PRICE").toString()));
	 						Object res=dao.save("AppOrdersMapper.save",order);
	 						if(Integer.valueOf(res.toString()) >= 1){
	 							return JSONArray.toJSONString(order).toString();
	 						}else{
	 							return null;
	 						}
 						} 
 					}else{
 						String id=UuidUtil.get32UUID();
 						data.put("ID", id);
 						data.put("STATUS","01");
 						data.put("APPLY_DATE", new Date()); 
 						Object obj=dao.save("AppActUserMapper.addActUser",data);
 						if(Integer.valueOf(obj.toString()) >= 1){
 							int  count=Integer.parseInt(act.get("PART_COUNT")+"");  
 		 					data.put("PART_COUNT",count+1);   
 			 				Object ob=dao.update("AppActivityMapper.updateCounts",data);
 			 				if(Integer.valueOf(ob.toString()) >= 1){
 			 				//获取notice.properties noticeText10内容。
		 					       String vv3=new String(noticeText6.getBytes("ISO-8859-1"),"UTF-8");
			 					    String notice1=vv3;
			 				        noticePushutil notutil=new noticePushutil();
			 				        notutil.toNotice(notice1); 
 			 					PageData res=new PageData();
 			 					res.put("code","200");
	 							return JSONArray.toJSONString(res).toString();
	 						}else{
	 							return null;
	 						} 
 						}
 					}  
 				}
 				if("03".equals(level)){
 					if(act.get("VICE_PRE_PRICE")!=null&&Double.parseDouble(act.get("VICE_PRE_PRICE")+"")>0){
 						PageData yzzf=new PageData();
 						yzzf.put("OBJECT_ID", act.getString("ID"));
 						yzzf.put("USER_ID", data.getString("USER_ID")); 
 						List<PageData> big= (List<PageData>) this.dao.findForList("AppOrdersMapper.queryByObj",yzzf);
 						if(big.size()>0){
 							for(PageData leobj:big){
 								Object delobj=this.dao.update("AppOrdersMapper.obdelete", leobj);
 							}
 								PageData order=new PageData(); 
 		 		 				order.put("ORDER_ID",UuidUtil.get32UUID());
 		 		 				order.put("USER_ID", data.getString("USER_ID")); 
 		 		 				order.put("STATUS","01");  
 		 		 				order.put("TYPE","02");  
 		 		 				order.put("OBJECT_ID",act.getString("ID"));  
 		 		 				order.put("EVENT","参加"+act.getString("ACT_NAME")+"活动费用");
 		 		 				order.put("DATE", new Date());
 		 						order.put("PRICE",Double.parseDouble(act.get("VICE_PRE_PRICE").toString()));
 		 						Object res=dao.save("AppOrdersMapper.save",order);
 		 						if(Integer.valueOf(res.toString()) >= 1){
 		 							return JSONArray.toJSONString(order).toString();
 		 						}else{
 		 							return null;
 		 						} 
 							
 						}else{
	 						PageData order=new PageData(); 
	 		 				order.put("ORDER_ID",UuidUtil.get32UUID());
	 		 				order.put("USER_ID", data.getString("USER_ID")); 
	 		 				order.put("STATUS","01");  
	 		 				order.put("TYPE","02");  
	 		 				order.put("OBJECT_ID",act.getString("ID"));  
	 		 				order.put("EVENT","参加"+act.getString("ACT_NAME")+"活动费用");
	 		 				order.put("DATE", new Date());
	 						order.put("PRICE",Double.parseDouble(act.get("VICE_PRE_PRICE").toString()));
	 						Object res=dao.save("AppOrdersMapper.save",order);
	 						if(Integer.valueOf(res.toString()) >= 1){
	 							return JSONArray.toJSONString(order).toString();
	 						}else{
	 							return null;
	 						} 
 						}
 					}else{
 						String id=UuidUtil.get32UUID();
 						data.put("ID", id);
 						data.put("STATUS","01");
 						data.put("APPLY_DATE", new Date()); 
 						Object obj=dao.save("AppActUserMapper.addActUser",data);
 						if(Integer.valueOf(obj.toString()) >= 1){
 							int  count=Integer.parseInt(act.get("PART_COUNT")+"");  
 		 					data.put("PART_COUNT",count+1);   
 			 				Object ob=dao.update("AppActivityMapper.updateCounts",data);
 			 				if(Integer.valueOf(ob.toString()) >= 1){
 			 				//获取notice.properties noticeText10内容。
		 					       String vv3=new String(noticeText6.getBytes("ISO-8859-1"),"UTF-8");
			 					    String notice1=vv3;
			 				        noticePushutil notutil=new noticePushutil();
			 				        notutil.toNotice(notice1); 
 			 					PageData res=new PageData();
 			 					res.put("code","200");
	 							return JSONArray.toJSONString(res).toString();
	 						}else{
	 							return null;
	 						} 
 						}
 					}  
 				}
 				if("04".equals(level)){
 					if(act.get("EXE_VICE_PRE_PRICE")!=null&&Double.parseDouble(act.get("EXE_VICE_PRE_PRICE")+"")>0){
 						PageData yzzf=new PageData();
 						yzzf.put("OBJECT_ID", act.getString("ID"));
 						yzzf.put("USER_ID", data.getString("USER_ID")); 
 						List<PageData> big=(List<PageData>) this.dao.findForList("AppOrdersMapper.queryByObj",yzzf);
 						if(big.size()>0){
 							for(PageData leobj:big){
 								Object delobj=this.dao.update("AppOrdersMapper.obdelete", leobj);
 							}
 								PageData order=new PageData(); 
 		 		 				order.put("ORDER_ID",UuidUtil.get32UUID());
 		 		 				order.put("USER_ID", data.getString("USER_ID")); 
 		 		 				order.put("STATUS","01");  
 		 		 				order.put("TYPE","02");  
 		 		 				order.put("OBJECT_ID",act.getString("ID"));  
 		 		 				order.put("EVENT","参加"+act.getString("ACT_NAME")+"活动费用");
 		 		 				order.put("DATE", new Date());
 		 						order.put("PRICE",Double.parseDouble(act.get("EXE_VICE_PRE_PRICE").toString()));
 		 						Object res=dao.save("AppOrdersMapper.save",order);
 		 						if(Integer.valueOf(res.toString()) >= 1){
 		 							return JSONArray.toJSONString(order).toString();
 		 						}else{
 		 							return null;
 		 						} 
 							
 						}else{
	 						PageData order=new PageData(); 
	 		 				order.put("ORDER_ID",UuidUtil.get32UUID());
	 		 				order.put("USER_ID", data.getString("USER_ID")); 
	 		 				order.put("STATUS","01");  
	 		 				order.put("TYPE","02");  
	 		 				order.put("OBJECT_ID",act.getString("ID"));  
	 		 				order.put("EVENT","参加"+act.getString("ACT_NAME")+"活动费用");
	 		 				order.put("DATE", new Date());
	 						order.put("PRICE",Double.parseDouble(act.get("EXE_VICE_PRE_PRICE").toString()));
	 						Object res=dao.save("AppOrdersMapper.save",order);
	 						if(Integer.valueOf(res.toString()) >= 1){
	 							return JSONArray.toJSONString(order).toString();
	 						}else{
	 							return null;
	 						} 
 						}
 					}else{
 						String id=UuidUtil.get32UUID();
 						data.put("ID", id);
 						data.put("STATUS","01");
 						data.put("APPLY_DATE", new Date()); 
 						Object obj=dao.save("AppActUserMapper.addActUser",data);
 						if(Integer.valueOf(obj.toString()) >= 1){
 							int  count=Integer.parseInt(act.get("PART_COUNT")+"");  
 		 					data.put("PART_COUNT",count+1);   
 			 				Object ob=dao.update("AppActivityMapper.updateCounts",data);
 			 				if(Integer.valueOf(ob.toString()) >= 1){
 			 				//获取notice.properties noticeText10内容。
		 					       String vv3=new String(noticeText6.getBytes("ISO-8859-1"),"UTF-8");
			 					    String notice1=vv3;
			 				        noticePushutil notutil=new noticePushutil();
			 				        notutil.toNotice(notice1); 
 			 					PageData res=new PageData();
 			 					res.put("code","200");
	 							return JSONArray.toJSONString(res).toString();
	 						}else{
	 							return null;
	 						} 
 						}
 					}  
 				}
 				if("05".equals(level)){
 					if(act.get("PRESIDENT_PRICE")!=null&&Double.parseDouble(act.get("PRESIDENT_PRICE")+"")>0){
 						PageData yzzf=new PageData();
 						yzzf.put("OBJECT_ID", act.getString("ID"));
 						yzzf.put("USER_ID", data.getString("USER_ID")); 
 						List<PageData> big=(List<PageData>) this.dao.findForList("AppOrdersMapper.queryByObj",yzzf);
 						if(big.size()>0){
 							for(PageData leobj:big){
 								Object delobj=this.dao.update("AppOrdersMapper.obdelete", leobj);
 							}
 								PageData order=new PageData(); 
 		 		 				order.put("ORDER_ID",UuidUtil.get32UUID());
 		 		 				order.put("USER_ID", data.getString("USER_ID")); 
 		 		 				order.put("STATUS","01");  
 		 		 				order.put("TYPE","02");  
 		 		 				order.put("OBJECT_ID",act.getString("ID"));  
 		 		 				order.put("EVENT","参加"+act.getString("ACT_NAME")+"活动费用");
 		 		 				order.put("DATE", new Date());
 		 						order.put("PRICE",Double.parseDouble(act.get("PRESIDENT_PRICE").toString()));
 		 						Object res=dao.save("AppOrdersMapper.save",order);
 		 						if(Integer.valueOf(res.toString()) >= 1){
 		 							return JSONArray.toJSONString(order).toString();
 		 						}else{
 		 							return null;
 		 						} 
 							
 						}else{
	 						PageData order=new PageData(); 
	 		 				order.put("ORDER_ID",UuidUtil.get32UUID());
	 		 				order.put("USER_ID", data.getString("USER_ID")); 
	 		 				order.put("STATUS","01");  
	 		 				order.put("TYPE","02");  
	 		 				order.put("OBJECT_ID",act.getString("ID"));  
	 		 				order.put("EVENT","参加"+act.getString("ACT_NAME")+"活动费用");
	 		 				order.put("DATE", new Date());
	 						order.put("PRICE",Double.parseDouble(act.get("PRESIDENT_PRICE").toString()));
	 						Object res=dao.save("AppOrdersMapper.save",order);
	 						if(Integer.valueOf(res.toString()) >= 1){
	 							return JSONArray.toJSONString(order).toString();
	 						}else{
	 							return null;
	 						} 
 						}
 					}else{
 						String id=UuidUtil.get32UUID();
 						data.put("ID", id);
 						data.put("STATUS","01");
 						data.put("APPLY_DATE", new Date()); 
 						Object obj=dao.save("AppActUserMapper.addActUser",data);
 						if(Integer.valueOf(obj.toString()) >= 1){
 							int  count=Integer.parseInt(act.get("PART_COUNT")+"");  
 		 					data.put("PART_COUNT",count+1);   
 			 				Object ob=dao.update("AppActivityMapper.updateCounts",data);
 			 				if(Integer.valueOf(ob.toString()) >= 1){
 			 				//获取notice.properties noticeText10内容。
		 					       String vv3=new String(noticeText6.getBytes("ISO-8859-1"),"UTF-8");
			 					    String notice1=vv3;
			 				        noticePushutil notutil=new noticePushutil();
			 				        notutil.toNotice(notice1); 
 			 					PageData res=new PageData();
 			 					res.put("code","200");
	 							return JSONArray.toJSONString(res).toString();
	 						}else{
	 							return null;
	 						} 
 						}
 					}  
 				}  
			}else{//已经参与，取消参与
				 Object obj =dao.delete("AppActUserMapper.delete",data);
	 		     if(Integer.valueOf(obj.toString()) >= 1){ 
	 		    	PageData act=(PageData) this.dao.findForObject("AppActivityMapper.querySimpleById",data);//查询参与的活动信息
	 		    	int count =Integer.parseInt(act.get("PART_COUNT")+"");
	 		    	int num = count>=1 ? -1 : 0;
	 		    	data.put("num", num);
	 				Object ob=dao.update("AppActivityMapper.updPartCount",data);
	 				if(Integer.valueOf(ob.toString()) >= 1){
	 					PageData ord=new PageData();
	 					ord.put("USER_ID",data.getString("USER_ID"));
	 					ord.put("OBJECT_ID",data.getString("ACT_ID"));
	 					
	 					PageData order = (PageData) this.dao.findForObject("AppOrdersMapper.queryByObj",ord);
	 					if( order != null && order.size() > 0 ){
	 						Object or=dao.update("AppOrdersMapper.editStatus",ord);
		 					if(Integer.valueOf(or.toString()) >= 1){
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
	 			}else{
	 				return null;  
	 			} 
			} 
		}
		return null;  
	}  
	
	/**
	 * 活动列表信息(根据条件查询)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryByParam(PageData pd) throws Exception{
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
		    List<PageData> acts =(List<PageData>) dao.findForList("AppActivityMapper.queryByParam",data);
			return JSONArray.toJSONString(acts).toString();
		}
		return null; 
	}
	
	/**
	 * 查询我发布的活动
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryListActs(PageData pd) throws Exception{
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
		    List<PageData> acts =(List<PageData>) dao.findForList("AppActivityMapper.queryActList",data);
			return JSONArray.toJSONString(acts).toString();
		}
		return null; 
	}
	
	/**
	 * 查询我参与的活动列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryPartInActs(PageData pd) throws Exception{
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
		    List<PageData> acts =(List<PageData>) dao.findForList("AppActivityMapper.queryActByUId",data);
			return JSONArray.toJSONString(acts).toString();
		}
		return null; 
	}
	
	/**
	 * 查询首页列表热搜词（活动类型）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryActType(PageData pd) throws Exception {
		if(!EmptyUtil.isNullOrEmpty(pd)){   
		    List<PageData> actType =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","activitytype"); //查询活动类型
		    return JSONArray.toJSONString(actType).toString(); 
		}
		return null;    
	}


	/**
	 * 查询首页列表热搜词（活动类型）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewActType(PageData pd) throws Exception {
		if(!EmptyUtil.isNullOrEmpty(pd)){  
			PageData data = pd.getObject("params"); 
			PageData res=new PageData(); 
			 
			//比较版本号
			boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_DIC, data);
			//获取返回数据
			DataConvertUtil.getDicRes(version,new String[]{"activitytype"},res); 
			 
			return JSONArray.toJSONString(res).toString(); 
		}
		return null;    
	}

	/**
	 * 查询活动列表的搜索条件
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querySearchParam(PageData pd) throws Exception {
		if(!EmptyUtil.isNullOrEmpty(pd)){   
		    List<PageData> actType =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","activitytype"); //查询活动类型
		    PageData data=new PageData();
			data.put("pid","0"); 
		    List<PageData> areas =(List<PageData>) this.dao.findForList("SysAreaMapper.querybyPid",data); //查询全部的省级地区
		    for(PageData pagedata:areas){
		    	data.put("pid",pagedata.getString("areacode"));
		    	List<PageData> citys =(List<PageData>) this.dao.findForList("SysAreaMapper.querybyPid",data); //查询全部的省级地区
		    	pagedata.put("citys",citys);
		    } 
		    PageData res=new PageData();
		    res.put("actType", actType);
		    res.put("areas", areas);
		    return JSONArray.toJSONString(res).toString(); 
		}
		return null;    
	}
	
	/**
	 * 查询活动列表的搜索条件(新)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewSearchParam(PageData pd) throws Exception {
		if(!EmptyUtil.isNullOrEmpty(pd)){  
			PageData data = pd.getObject("params"); 
			PageData res=new PageData();  
			
			PageData dics=new PageData();  
			//比较版本号
			boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_DIC, data);
			//获取返回数据
			DataConvertUtil.getDicRes(version,new String[]{"activitytype"},dics); 
			
			PageData areas=new PageData();  
			//比较版本号
			boolean version2=CacheHandler.getCompareVersion(ConstantUtil.VERSION_AREA, data);
			//获取返回数据
			DataConvertUtil.getRes(version2, ConstantUtil.VERSION_AREA, areas);  
			
			res.put("areas", areas);
			res.put("dics",dics);
		    return JSONArray.toJSONString(res).toString(); 
		}
		return null;    
	} 
	
	/**
	 * 添加预订信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addReservations(PageData pd) throws Exception{
		if(!EmptyUtil.isNullOrEmpty(pd)){ 
			PageData data = pd.getObject("params"); 
			data.put("ID",UuidUtil.get32UUID());
			data.put("CREATE_DATE",new Date());
			data.put("STATUS",1);
			data.put("ISDEL",1);
			data.put("ISSYSLOOK", 0);
			Object res=dao.save("AppClubMapper.save",data);
			if(Integer.valueOf(res.toString()) >= 1){
				//获取notice.properties noticeText10内容。
		        String vv3=new String(noticeText13.getBytes("ISO-8859-1"),"UTF-8");
			    String notice1=vv3;
		        noticePushutil notutil=new noticePushutil();
		        notutil.toNotice(notice1); 
				pd.put("code","200");
				return JSONArray.toJSONString(pd).toString();
			}else{
				return null;
			} 
		} 
		return null;  
	}


	@Override
	public String queryClubRoattions(PageData pd) throws Exception {
		PageData result = new PageData(); 
		if(!EmptyUtil.isNullOrEmpty(pd)){  
			pd.put("LOCATION_NO","05");
		    List<PageData> firRots=(List<PageData>) this.dao.findForList("AppRotationMapper.queryByColno",pd);//获取俱乐部轮换大图1
		    pd.put("LOCATION_NO","06");
		    List<PageData> secRots=(List<PageData>) this.dao.findForList("AppRotationMapper.queryByColno",pd);//获取俱乐部轮换大图2
		    result.put("firRots",firRots);
		    result.put("secRots",secRots);  
			return JSONArray.toJSONString(result).toString();
		}
		return null;  
	}

	@Override
	public String queryDateArr(PageData pd) throws Exception {
		PageData result = new PageData(); 
		if(!EmptyUtil.isNullOrEmpty(pd)){   
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		    String dateStr = pd.getString("date");
		    if( dateStr != null && !"".equals(dateStr)  ){
		    	Date date = sdf.parse(dateStr);
			    PageData pa = new PageData();
				pa.put("START_DATE",DateUtil.getYMM(date)); 
				List<PageData> dates =(List<PageData>) this.dao.findForList("AppClubMapper.findByYearAndMonth",pa); //查询全部的省级地区
				StringBuffer sb = new StringBuffer("");  
				for( PageData data :  dates ){
					String str = data.getString("START_DATE");
	                if( str != null && !"".equals(str) ){
	                	sb.append(","+str);
	                }
				}
				String[] dateArr = sb.toString().split(","); 
				result.put("dateArr",dateArr);
			    return JSONArray.toJSONString(result).toString();
		    }
		}
		return null;  
	}


    /**
     * 查询预订场地页面数据
     * */    
	@Override
	public String queryClubVenue(PageData pd) throws Exception {
		PageData result = new PageData(); 
		if(!EmptyUtil.isNullOrEmpty(pd)){   
			List<PageData> actType =(List<PageData>) this.dao.findForList("AppDicMapper.queryBM","activitytype"); //查询活动类型
		    result.put("actType",actType);
		    
		    PageData pa = new PageData();
			pa.put("START_DATE",DateUtil.getYMM(new Date())); 
			List<PageData> dates =(List<PageData>) this.dao.findForList("AppClubMapper.findByYearAndMonth",pa); //查询全部的省级地区
			StringBuffer sb = new StringBuffer();  
			for( PageData data :  dates ){
				String str = data.getString("START_DATE");
                if( str != null && !"".equals(str) ){
                	sb.append(","+str);
                }
			}
			String[] dateArr = sb.toString().split(","); 
			result.put("dateArr",dateArr);
			result.put("today",DateUtil.getDay());
		    return JSONArray.toJSONString(result).toString();
		}
		return null; 
	}
	
	/**
	 * 查询活动详情用户修改展示使用
	 * @param pd
	 * 
	 * @return
	 * @throws Exception
	 */  
	public String queryActById(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData act =(PageData) this.dao.findForObject("AppActivityMapper.queryById",data);//查询活动详情信息
		    List<PageData> viptype =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","viptype"); //查询会员等级
		    List<PageData> actType =(List<PageData>) this.dao.findForList("AppDicMapper.queryBM","activitytype"); //查询活动类型
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
		    List<PageData> levels =(List<PageData>) this.dao.findForList("AppActLevelMapper.queryLevelByAId",data);//查询活动参与等级信息
		    for(PageData type:viptype){ 
		    	if(levels!=null&&levels.size()>0){
		    		for(PageData level:levels){ 
			    		 if(type.getString("BIANMA").equals(level.getString("BIANMA"))){
			    			 type.put("flag",1);//选中
							 break;
						 } 
					 } 
		    	}else{  
		    		type.put("flag",0);//未选中 
		    	}
		    } 
		    PageData res=new PageData();
		    res.put("viptype",viptype);
		    res.put("actType",actType);
		    res.put("areas",areas);
		    res.put("act",act);
			return JSONArray.toJSONString(res).toString();   
		}
		return null;    
	}
	
	/**
	 * 查询活动详情用户修改展示使用(新)
	 * @param pd
	 * 
	 * @return
	 * @throws Exception
	 */  
	public String queryNewActById(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData res=new PageData();
			PageData act =(PageData) this.dao.findForObject("AppActivityMapper.queryById",data);//查询活动详情信息
			
			PageData dics=new PageData();
			//比较版本号
			boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_DIC, data);
			//获取返回数据
			DataConvertUtil.getDicRes(version,new String[]{"activitytype"},dics); 
			
			PageData areas=new PageData();
			//比较版本号
			boolean version2=CacheHandler.getCompareVersion(ConstantUtil.VERSION_AREA, data);
			//获取返回数据
			DataConvertUtil.getRes(version2, ConstantUtil.VERSION_AREA, areas);  
			
			List<PageData> viptype =CacheHandler.getDics("viptype");//(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","viptype"); //查询会员等级
		    List<PageData> levels =(List<PageData>) this.dao.findForList("AppActLevelMapper.queryLevelByAId",data);//查询活动参与等级信息
		    for(PageData type:viptype){ 
		    	if(levels!=null&&levels.size()>0){
		    		for(PageData level:levels){ 
			    		 if(type.getString("value").equals(level.getString("BIANMA"))){
			    			 type.put("flag",1);//选中 
			    			 break;
						 } 
					 } 
		    	}else{  
		    		type.put("flag",0);//未选中 
		    	}
		    } 
		    res.put("dics",dics);
			res.put("areas",areas);
		    res.put("act",act);
		    res.put("viptype",viptype);
			return JSONArray.toJSONString(res).toString();   
		}
		return null;    
	}
	
	/**
	 * 根据ID查询预约信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryByYy(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData act =(PageData) this.dao.findForObject("AppActivityMapper.querybyYyID",data);
		    if(act!=null){
		    	List<PageData> isApply =(List<PageData>) this.dao.findForList("AppActUserMapper.checkIsApply",data);//判断是否已经参与
			    List<PageData> levels =(List<PageData>) this.dao.findForList("AppActLevelMapper.queryLevelByAId",data);//查询活动参与等级信息
			    if(isApply.size()<=0){
			    	act.put("isApply","0");//没有参与
			    }else{
			    	act.put("isApply","1");//已经参与
			    }
			    act.put("levels",levels);//已经参与
				return JSONArray.toJSONString(act).toString();
		    }else{
		    	return null;  
		    } 
		}
		return null;  
	}
	
	
	/**
	 * 根据ID查询活动详情参与人
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryByxqId(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData act =(PageData) this.dao.findForObject("AppActivityMapper.queryById",data);//活动详情
		    if(act!=null){
		    	if(data.get("USER_ID")==null||"".equals(data.get("USER_ID"))){
			    	act.put("isApply","0");//没有参与
			    }else{
			    	List<PageData> isApply =(List<PageData>) this.dao.findForList("AppActUserMapper.checkIsApply",data);//判断是否已经参与
			    	 if(isApply.size()<=0){
			    		 act.put("isApply","0");//没有参与
					 }else{
					    act.put("isApply","1");//已经参与
					 }
			    } 
			    List<PageData> levels =(List<PageData>) this.dao.findForList("AppActLevelMapper.queryLevelByAId",data);//查询活动参与等级信息
			    List<PageData> CYR =(List<PageData>) this.dao.findForList("AppActivityMapper.querybyCYRID",data);//参与前五条人的信息
			    act.put("CYR",CYR);//已经参与人的前五条
			    act.put("levels",levels);//已经参与
				return JSONArray.toJSONString(act).toString();
		    }else{
		    	return null;  
		    } 
		}
		return null;  
	}
	
	
	/**
	 * 活动参与人列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String querybyCYRlistsID(PageData pd) throws Exception{
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
		    List<PageData> acts =(List<PageData>) dao.findForList("AppActivityMapper.querybyCYRlistID",data);
			return JSONArray.toJSONString(acts).toString();
		}
		return null; 
	} 
	
	
	/**
	 * 根据ID查询活动海报详情
	 * @param pd
	 * @return
	 * @throws Exception
	 * 时间：2017/9/13
	 */ 
	public String queryByhb(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData act =(PageData) this.dao.findForObject("AppActivityMapper.queryByhb",data);
		    if(act!=null){
		    	if(data.get("USER_ID")==null||"".equals(data.get("USER_ID"))){
			    	act.put("isApply","0");//没有参与
			    }else{
			    	List<PageData> isApply =(List<PageData>) this.dao.findForList("AppActUserMapper.checkIsApply",data);//判断是否已经参与
			    	
			    	List<PageData> iszf= (List<PageData>) this.dao.findForList("AppOrdersMapper.querybyddzf", data);
			    	if(iszf.size()<0){
			    		act.put("unpaid","1");//有未支付订单
			    	}else{
					    act.put("unpaid","0");//没有支付订单
					 }
			    	 if(isApply.size()<=0){
			    		 act.put("isApply","0");//没有参与
					 }else{
					    act.put("isApply","1");//已经参与
					 }
			    } 
		    	
		    	 PageData levels = (PageData) this.dao.findForObject("AppActLevelMapper.queryLevelByADJId",data);//查询活动参与等级信息
		    	 act.put("LIMIT_LEVEL",levels.get("nids"));//参与等级
		    	 act.put("appurls", "http://jianlian.shanghai-cu.com/jianlian/appActivity/queryfenxiangById.do?ID="); 
		    	 return JSONArray.toJSONString(act).toString();
		    }else{
		    	return null;  
		    } 
		}
		return null;  
	}
	
	
	/**
	 * 根据ID查询活动详情(参与人)
	 * @param pd
	 * @return
	 * @throws Exception
	 * 时间：2017/9/13
	 */ 
	public String queryBycyr(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData act =(PageData) this.dao.findForObject("AppActivityMapper.queryByhb",data);
		    if(act!=null){
		    	if(data.get("USER_ID")==null||"".equals(data.get("USER_ID"))){
			    	act.put("isApply","0");//没有参与
			    }else{
			    	List<PageData> isApply =(List<PageData>) this.dao.findForList("AppActUserMapper.checkIsApply",data);//判断是否已经参与
			    	List<PageData> iszf=(List<PageData>) this.dao.findForList("AppOrdersMapper.querybyddzf", data);
			    	if(iszf.size()<0){
			    		act.put("unpaid","1");//有未支付订单
			    	}else{
					    act.put("unpaid","0");//没有支付订单
					 }
			    	 if(isApply.size()<=0){
			    		 act.put("isApply","0");//没有参与
					 }else{
					    act.put("isApply","1");//已经参与
					 }
			    } 
		    	 PageData levels = (PageData) this.dao.findForObject("AppActLevelMapper.queryLevelByADJId",data);//查询活动参与等级信息
		    	 List<PageData>	cyrs=(List<PageData>) this.dao.findForList("AppActivityMapper.querybyCYRBYID", data);
		    	 act.put("LIMIT_LEVEL",levels.get("nids"));//参与等级
		    	 act.put("cyrs",cyrs);//五条参与人
		    	 return JSONArray.toJSONString(act).toString();
		    }else{
		    	return null;  
		    } 
		}
		return null;  
	}
	
	
	
}

 

