package org.yx.services.zfzs.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.Logger;
import org.yx.common.utils.UuidUtil;
import org.yx.services.zfzs.inter.AppEntrustServiceInter;
import org.yx.util.ConstantUtil;
import org.yx.util.DataConvertUtil;
import org.yx.util.PushUtil;

import com.alibaba.fastjson.JSONArray;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;


@Service("appEntrustService")
public class AppEntrustService implements AppEntrustServiceInter {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	Logger log=Logger.getLogger(getClass());

	/**
	 * 基本信息提交
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String AddEntrust(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){   
			try{ 
				data.put("ID",UuidUtil.get32UUID());
				Object ob=dao.update("AppEntrustMapper.saveEntrust",data); 
 				if(Integer.valueOf(ob.toString()) >= 1){
 					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();  
 				}else{
 					return null;  
 				}
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		} 
		return null;
	} 
	
	
	/**
	 * 首页广告图+第一次分页数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	
	public String queryzfzsIndexInfo(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			data.put("LOCATION_NO", "15");
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
			List<PageData> firRots=null;
			PageData tel=null;
			List<PageData> types=null;
			try{
				firRots=(List<PageData>) this.dao.findForList("AppRotationMapper.queryByColno",data);//获取资讯页面顶部的轮换大图
				products =(List<PageData>) dao.findForList("AppEntrustMapper.queryByEntrustfy",data);//查询政府招商项目列表
				tel =(PageData) this.dao.findForObject("ServiceTelMapper.findBm","04");//查询政府招商详情页面的客服电话
				types =(List<PageData>) this.dao.findForList("AppEntrustMapper.queryType",data); //查询项目的一级类型
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			PageData result = new PageData(); 
			result.put("firRots",firRots);
			result.put("tel",tel);
		    result.put("products",products);
		    result.put("types",types);
		   // log.info("类型为："+JSONArray.toJSONString(types).toString());
			return JSONArray.toJSONString(result).toString();
		}
		return null;  
	}
	
	/**
	 * 查询搜索条件+第一次分页数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querySearchParam(PageData pd) throws Exception{
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
			try{  
				data.put("pid","0"); 
			    List<PageData> areas =(List<PageData>) this.dao.findForList("SysAreaMapper.querybyPid",data); //查询全部的省级地区
			    for(PageData pagedata:areas){
			    	data.put("pid",pagedata.getString("areacode"));
			    	List<PageData> citys =(List<PageData>) this.dao.findForList("SysAreaMapper.querybyPid",data); //查询全部的省级地区
			    	pagedata.put("citys",citys);
			    }  
			    List<PageData> types =(List<PageData>) this.dao.findForList("AppEntrustMapper.queryType",data); //查询项目的一级类型
			    for(PageData pagedata:types){ 
			    	List<PageData> lxs =(List<PageData>) this.dao.findForList("AppEntrustMapper.querySubClass",pagedata); //查询二级类型
			    	pagedata.put("lxs",lxs);
			    } 
			    List<PageData> pros =(List<PageData>) dao.findForList("AppEntrustMapper.queryByEntrustfy",data);//查询政府招商项目列表
				PageData res = new PageData();  
			    res.put("pros",pros);
			    res.put("types",types);
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
	 * 查询搜索条件+第一次分页数据（新）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewSearchParam(PageData pd) {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			try{
				PageData res = new PageData();  
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
				
				PageData areas = new PageData();  
				//比较版本号
				boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_AREA, data);
				//获取返回数据
				DataConvertUtil.getRes(version, ConstantUtil.VERSION_AREA, areas);  
				
				List<PageData> types =(List<PageData>) this.dao.findForList("AppEntrustMapper.queryType",data); //查询项目的一级类型
			    for(PageData pagedata:types){ 
			    	List<PageData> lxs =(List<PageData>) this.dao.findForList("AppEntrustMapper.querySubClass",pagedata); //查询二级类型
			    	pagedata.put("lxs",lxs);
			    } 
			    List<PageData> pros =(List<PageData>) dao.findForList("AppEntrustMapper.queryByEntrustfy",data);//查询政府招商项目列表
			    
			    res.put("areas",areas);
			    res.put("pros",pros);
			    res.put("types",types); 
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
	 * 列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public String querxmlist(PageData pd) throws Exception{
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
				products =(List<PageData>) dao.findForList("AppEntrustMapper.queryByEntrustfy",data);//查询政府招商项目列表
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
	 * 根据项目id查询项目详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryxmxq(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData cont =(PageData) this.dao.findForObject("AppEntrustMapper.queryByEntrustXQ",data); 
		    List<PageData> imgs =(List<PageData>) this.dao.findForList("AppEntrustMapper.queryByEntrustXQIMG",data); 
		    PageData tel =(PageData) this.dao.findForObject("ServiceTelMapper.findBm","04");//查询政府招商详情页面的客服电话
		    PageData xiangqi=new PageData();
		    xiangqi.put("tel", tel);
		    xiangqi.put("cont", cont);
		    xiangqi.put("imgs", imgs);
			return JSONArray.toJSONString(xiangqi).toString(); 
		}
		return null;   
	} 
	
}
