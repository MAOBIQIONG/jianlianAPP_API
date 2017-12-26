package org.yx.services.cyyq.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date; 
import java.util.List; 

import javax.annotation.Resource; 

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData;
import org.yx.common.utils.ChineseToEnglish;
import org.yx.common.utils.EmptyUtil; 
import org.yx.common.utils.RandomNum;
import org.yx.common.utils.ReturnCode;
import org.yx.common.utils.UuidUtil;
import org.yx.services.AppCommonService;
import org.yx.services.cyyq.inter.AppCyyqServiceInter;
import org.yx.services.cyyq.inter.AppFwuServiceInter;
import org.yx.services.cyyq.inter.AppRzServiceInter;
import org.yx.services.epc.inter.AppEpcServiceInter;
import org.yx.services.jy.inter.AppProjectServiceInter;  
import org.yx.util.ConstantUtil;
import org.yx.util.DataConvertUtil;
import org.yx.util.PushUtil;

import com.alibaba.fastjson.JSONArray;   
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

@Service("appFwService")
public class AppFwuService implements AppFwuServiceInter{


	@Resource(name = "daoSupport")
	private DaoSupport dao;  
	
	/**
	 * 查询服务信息详情用于修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryfwxqToEdit(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			try{
				PageData service=new PageData();
				if(data.getString("SE_ID")!=null&&!"".equals(data.getString("SE_ID"))){
					service =(PageData) dao.findForObject("AppFwMapper.queryByfwbyid",data);//查询园区服务的详细信息用于修改 
				} 
				List<PageData> types =(List<PageData>) this.dao.findForList("AppDicMapper.queryByBM","Service_type"); //查询园区类型
				service.put("types",types); 
				return JSONArray.toJSONString(service).toString();
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		}
		return null;  
	}
	
	/**
	 * 查询服务信息详情用于修改(新）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewfwxqToEdit(PageData pd){
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			try{
				PageData service=new PageData();
				if(data.getString("SE_ID")!=null&&!"".equals(data.getString("SE_ID"))){
					service =(PageData) dao.findForObject("AppFwMapper.queryByfwbyid",data);//查询园区服务的详细信息用于修改 
				}
				
				//PageData dics=new PageData();
				//比较版本号
				boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_DIC, data);
				
				//获取返回数据
				DataConvertUtil.getDicRes(version,new String[]{"Service_type"},service);
				
				//service.put("dics",dics);
				 
				return JSONArray.toJSONString(service).toString();
			}catch(Exception e){
				e.printStackTrace();
			}  
		}
		return null;  
	}
	
	/**
	 * 园区服务信息填写+修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editFw(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
	        data.put("STATUS","01");
	        if(data.getString("SE_ID")!=null&&!"".equals(data.getString("SE_ID"))){//修改 
	           data.put("MODIFY_BY",data.getString("USER_ID"));
	    	   data.put("MODIFY_DATE",new Date());
	    	   Object obj=dao.update("AppFwMapper.editfw",data);
	    	   if(Integer.valueOf(obj.toString()) >= 1){ 
					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();   
	 			}else{
	 				return null;  
	 			}
	        }else{
	           String id=UuidUtil.get32UUID();
			   data.put("SE_ID", id);   
	    	   data.put("CREATE_DATE",new Date());
	    	   Object obj=dao.update("AppFwMapper.savefw",data); 
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
	 * 查询我的服务信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String querymyfwlist(PageData pd) throws Exception{
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
				products =(List<PageData>) dao.findForList("AppFwMapper.queryByfwlist",data);//查询政府招商项目列表
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
	 * 服务信息列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryfwlists(PageData pd) throws Exception{
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
			PageData conets=new PageData();
			try{ 
				List<PageData> fulx =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","Service_type"); //查询项目的阶段
				if(fulx!=null&&fulx.size()>0){
					data.put("JOIN_CLASS",fulx.get(0).getString("BIANMA"));
				} 
				products =(List<PageData>) dao.findForList("AppFwMapper.queryByfwxxlist",data);//查询政府招商项目列表
				conets.put("products", products);
				conets.put("fulx", fulx);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(conets).toString();
		}
		return null;  
	}
	
	
	/**
	 * 根据服务类型查找信息（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryfwlxlists(PageData pd) throws Exception{
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
			PageData conets=new PageData();
			try{
				products =(List<PageData>) dao.findForList("AppFwMapper.queryByfwxxlist",data);//查询政府招商项目列表
				conets.put("products", products);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(conets).toString();
		}
		return null;  
	}
	/**
	 * 服务信息详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryfwxq(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData cont =(PageData) this.dao.findForObject("AppFwMapper.queryByfwbyid",data); 
		    PageData phone =(PageData) this.dao.findForObject("ServiceTelMapper.findBm","05");//查询产业园的客服电话
		    PageData xiangqi=new PageData();
		    xiangqi.put("cont", cont);
		    xiangqi.put("phone", phone);
			return JSONArray.toJSONString(xiangqi).toString(); 
		}
		return null;   
	} 
	
	/**
	 * 园区服务信息删除
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String delete(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			try{
				Object ob =dao.update("AppFwMapper.delete",data);//删除产业园区的信息
				if(Integer.parseInt(ob.toString())>=1){  
 					PageData res=new PageData();
					res.put("code","200"); 
					return JSONArray.toJSONString(res).toString();    
	 			}else{
	 				PageData res=new PageData();
					res.put("code","400"); 
					return JSONArray.toJSONString(res).toString();  
	 			}   
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		}
		return null;  
	}
}
