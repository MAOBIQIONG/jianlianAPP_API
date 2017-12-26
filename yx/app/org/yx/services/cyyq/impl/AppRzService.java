package org.yx.services.cyyq.impl;

import java.sql.SQLException;
import java.util.Date; 
import java.util.List; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil; 
import org.yx.common.utils.UuidUtil;
import org.yx.services.cyyq.inter.AppRzServiceInter;

import com.alibaba.fastjson.JSONArray;   

@Service("appRzService")
public class AppRzService implements AppRzServiceInter{


	@Resource(name = "daoSupport")
	private DaoSupport dao;  
	
	/**
	 * 查询我的入驻信息
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public String queryRzhulist(PageData pd) throws Exception{
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
				products =(List<PageData>) dao.findForList("AppRzMapper.queryByRZlist",data);//查询政府招商项目列表
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
	 * 查询入驻信息列表
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryRzhulists(PageData pd) throws Exception{
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
				products =(List<PageData>) dao.findForList("AppRzMapper.queryByRZlists",data);//查询政府招商项目列表
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
	 * 入驻信息详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryRzxq(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData cont =(PageData) this.dao.findForObject("AppRzMapper.queryByRZbyid",data);  
		    PageData phone =(PageData) this.dao.findForObject("ServiceTelMapper.findBm","05");//查询产业园的客服电话
		    PageData xiangqi=new PageData();
		    xiangqi.put("cont", cont); 
		    xiangqi.put("phone", phone); 
			return JSONArray.toJSONString(xiangqi).toString(); 
		}
		return null;   
	} 
	
	/**
	 * 入驻园区信息填写+修改（企业、创业者）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editRz(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
	        data.put("STATUS","01");
	        if(data.getString("RZ_ID")!=null&&!"".equals(data.getString("RZ_ID"))){//修改   
	    	   data.put("MODIFY_DATE",new Date());
	    	   Object obj=dao.update("AppRzMapper.edit",data);
	    	   if(Integer.valueOf(obj.toString()) >= 1){ 
					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();   
	 			}else{
	 				return null;  
	 			}
	        }else{
	           String id=UuidUtil.get32UUID();
			   data.put("RZ_ID", id);   
	    	   data.put("CREATE_DATE",new Date());
	    	   Object obj=dao.update("AppRzMapper.save",data); 
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
	 * 入驻园区信息删除
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String delete(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			try{
				Object ob =dao.update("AppRzMapper.delete",data);//删除产业园区的信息
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
