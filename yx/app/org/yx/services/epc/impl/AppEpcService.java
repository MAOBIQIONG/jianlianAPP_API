package org.yx.services.epc.impl;

import java.sql.SQLException;
import java.util.List; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service;  
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData;
import org.yx.common.utils.ChineseToEnglish;
import org.yx.common.utils.EmptyUtil; 
import org.yx.services.epc.inter.AppEpcServiceInter;

import com.alibaba.fastjson.JSONArray;   

@Service("appEpcService")
public class AppEpcService implements AppEpcServiceInter{


	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	
	ChineseToEnglish  cte=new ChineseToEnglish();
	
	/**
	 * 查询交易列表信息(可以根据条件搜索：地区、阶段、类型、项目名称模糊查询)
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryIndexByParam(PageData pd) throws Exception { 
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
			List<PageData> pros=null;
			try{
				pros =(List<PageData>) dao.findForList("AppProjectMapper.queryProIndexByParam",data);
				for(PageData pro:pros){
					if("1".equals(pro.getString("IS_RESOURCE"))){
						 List<PageData> imgs =(List<PageData>) this.dao.findForList("AppXmtpMapper.queryImgsByPid",pro); //查询项目的图片
						 pro.put("imgs",imgs);
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

}
