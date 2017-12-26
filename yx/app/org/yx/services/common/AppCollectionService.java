package org.yx.services.common;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;

import com.alibaba.fastjson.JSONArray;

@Service("appCollectionService")
public class AppCollectionService {
     
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	Logger log=Logger.getLogger(this.getClass());//日志信息
	
	/**
	 * 收藏(新闻、项目、活动）/取消收藏
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editCollect(PageData pd) throws Exception {
		PageData data = pd.getObject("params");  
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData page =(PageData) this.dao.findForObject("AppCollectionMapper.checkCollection",data);//判断是否收藏
			if(page==null){//没有收藏
				String id=UuidUtil.get32UUID();
				data.put("ID", id);
				data.put("COLLECT_DATE", new Date());
				Object obj=dao.save("AppCollectionMapper.saveCollection",data);
				PageData collect=new PageData();
				Object ob=null;
				if(Integer.valueOf(obj.toString()) >= 1){
					if("JL_APP_MEDIA".equals(data.getString("TYPE"))||"jl_app_media".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.queryMedByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+"");  
	 					data.put("COLLECT_COUNT",count+1);   
	 					ob=dao.update("AppCollectionMapper.updateMedCounts",data);
					}else if("PROJECT".equals(data.getString("TYPE"))||"project".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.queryProByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+""); 
	 					data.put("COLLECT_COUNT",count+1);
	 					ob=dao.update("AppCollectionMapper.updateProCounts",data);
					}else if("activity".equals(data.getString("TYPE"))||"ACTIVITY".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.queryActByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+"");  
	 					data.put("COLLECT_COUNT",count+1); 
	 					ob=dao.update("AppCollectionMapper.updateActCounts",data);
					}else if("jl_dp_shop".equals(data.getString("TYPE"))||"JL_DP_SHOP".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.queryShopByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+"");  
	 					data.put("COLLECT_COUNT",count+1); 
	 					ob=dao.update("AppCollectionMapper.updateShopCounts",data);
					}else if("PPP".equals(data.getString("TYPE"))||"ppp".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.queryPppByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+"");  
	 					data.put("COLLECT_COUNT",count+1); 
	 					ob=dao.update("AppCollectionMapper.updatePppCounts",data);
					}else if("JL_JLB_SXY".equals(data.getString("TYPE"))||"JL_JLB_SXY".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.querySxyByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+"");  
	 					data.put("COLLECT_COUNT",count+1); 
	 					ob=dao.update("AppCollectionMapper.updateSxyCounts",data);
					}
	 				if(Integer.valueOf(ob.toString()) >= 1){
	 					log.info("function  editCollect is success!");
	 					PageData res=new PageData();
	 					res.put("code","200");
	 					return JSONArray.toJSONString(res).toString(); 
	 				}else{
	 					log.info("function  editCollect is fail!");
	 					return null;  
	 				} 
				}else{
					log.info("function  editCollect is fail!");
					return null;
				} 
			}else{//已经收藏，取消收藏
				 Object obj =dao.delete("AppCollectionMapper.delCollection",data);
	 		     if(Integer.valueOf(obj.toString()) >= 1){ 
	 		    	PageData collect=new PageData();
					Object ob=null;  
	 				if("JL_APP_MEDIA".equals(data.getString("TYPE"))||"jl_app_media".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.queryMedByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+""); 
		 				if(count>1){ 
	 						data.put("COLLECT_COUNT",count-1);  
	 					}else{ 
	 						data.put("COLLECT_COUNT",0);  
	 					}  
	 					ob=dao.update("AppCollectionMapper.updateMedCounts",data);
					}else if("PROJECT".equals(data.getString("TYPE"))||"project".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.queryProByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+""); 
		 				if(count>1){ 
	 						data.put("COLLECT_COUNT",count-1);  
	 					}else{ 
	 						data.put("COLLECT_COUNT",0);  
	 					} 
						ob=dao.update("AppCollectionMapper.updateProCounts",data); 
					}else if("activity".equals(data.getString("TYPE"))||"ACTIVITY".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.queryActByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+""); 
		 				if(count>1){ 
	 						data.put("COLLECT_COUNT",count-1);  
	 					}else{ 
	 						data.put("COLLECT_COUNT",0);  
	 					}  
	 					ob=dao.update("AppCollectionMapper.updateActCounts",data);
					}else if("jl_dp_shop".equals(data.getString("TYPE"))||"JL_DP_SHOP".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.queryShopByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+""); 
		 				if(count>1){ 
	 						data.put("COLLECT_COUNT",count-1);  
	 					}else{ 
	 						data.put("COLLECT_COUNT",0);  
	 					}  
	 					ob=dao.update("AppCollectionMapper.updateShopCounts",data);
					}else if("PPP".equals(data.getString("TYPE"))||"ppp".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.queryPppByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+""); 
		 				if(count>1){ 
	 						data.put("COLLECT_COUNT",count-1);  
	 					}else{ 
	 						data.put("COLLECT_COUNT",0);  
	 					}  
	 					ob=dao.update("AppCollectionMapper.updatePppCounts",data);
					}else if("JL_JLB_SXY".equals(data.getString("TYPE"))||"JL_JLB_SXY".equals(data.getString("TYPE"))){
						collect=(PageData) this.dao.findForObject("AppCollectionMapper.querySxyByParam",data);//查询收藏数量
						int  count=Integer.parseInt(collect.get("COLLECT_COUNT")+""); 
		 				if(count>1){ 
	 						data.put("COLLECT_COUNT",count-1);  
	 					}else{ 
	 						data.put("COLLECT_COUNT",0);  
	 					}  
	 					ob=dao.update("AppCollectionMapper.updateSxyCounts",data);
					}       
	 				if(Integer.valueOf(ob.toString()) >= 1){
	 					log.info("function  editCollect is success!");
	 					PageData res=new PageData();
	 					res.put("code","200");
	 					return JSONArray.toJSONString(res).toString(); 
	 				}else{
	 					log.info("function  editCollect is fail!");
	 					return null;  
	 				}
	 			}else{
	 				log.info("function  editCollect is fail!");
	 				return null;  
	 			} 
			} 
		}
		log.info("function  editCollect is fail!");
		return null;  
	}
	
	/**
	 * 根据条件搜索收藏信息（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCollects(PageData pd) throws Exception{
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
			List<PageData> colls=null;
			try{
				if("PROJECT".equals(data.getString("TYPE"))){//查询交易信息
					colls =(List<PageData>) dao.findForList("AppCollectionMapper.queryPros",data);
					
				}else if("JL_DP_SHOP".equals(data.getString("TYPE"))){//查询收藏的店铺信息
					colls =(List<PageData>) dao.findForList("AppCollectionMapper.queryShops",data);
				}else if("PPP".equals(data.getString("TYPE"))){//查询收藏的店铺信息
					colls =(List<PageData>) dao.findForList("AppCollectionMapper.queryPpps",data);
				}else if("JL_JLB_SXY".equals(data.getString("TYPE"))){//查询商学院信息
					colls =(List<PageData>) dao.findForList("AppCollectionMapper.querySxyxx",data);
				}else{//查询资讯信息
					colls =(List<PageData>) dao.findForList("AppCollectionMapper.queryMeds",data);
				}  		
			}catch(SQLException sql){
				log.info(sql.getMessage());
				sql.printStackTrace();
			}catch(Exception e){
				log.info(e.getMessage());
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(colls).toString();
		}
		log.info("function  queryCollects is fail!");
		return null; 
	}
	
	
}
