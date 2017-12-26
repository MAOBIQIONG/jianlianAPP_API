package org.yx.services.cylj.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.ChineseToEnglish;
import org.yx.common.utils.EmptyUtil;
import org.yx.services.AppCommonService;
import org.yx.services.cylj.inter.AppCyljServiceInter;

import com.alibaba.fastjson.JSONArray;

@Service("appCyljService")
public class AppCyljService implements AppCyljServiceInter{


	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	
	@Resource(name="appCommonService")
	private AppCommonService appCommonService;
	
	ChineseToEnglish  cte=new ChineseToEnglish();
	
	/**
	 * .查询链接的信息（分页）
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryCyljBYindex(PageData pd) throws Exception {
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
			List<PageData> coms=null;
			try{
				coms =(List<PageData>) dao.findForList("AppIndustry_linksMapper.queryByIndex",data);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(coms).toString();
		}
		return null;  
	}

}
