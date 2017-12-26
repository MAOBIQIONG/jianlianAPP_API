package org.yx.services.jlq.impl;
import java.util.List;

import javax.annotation.Resource; 

import org.springframework.stereotype.Service;  
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData; 
import org.yx.common.utils.ConfConfig;
import org.yx.common.utils.EmptyUtil; 
import org.yx.services.jlq.inter.AppMagazineServiceInter;
import org.yx.util.ReadFile;

import com.alibaba.fastjson.JSONArray;

@Service("appMagazineService")
public class AppMagazineService implements AppMagazineServiceInter{ 
	//期刊文件存放路径
    public static String qkBasePath = ConfConfig.getString("qkBasePath");

	@Resource(name = "daoSupport")
	private DaoSupport dao;  
	
	/**
	 * 查看期刊列表信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryQks(PageData pd) throws Exception{
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
		    List<PageData> qks =(List<PageData>) dao.findForList("AppQkMapper.queryQks",data);
			return JSONArray.toJSONString(qks).toString();
		}
		return null; 
	}
	
	/**
	 * 查询某期期刊的全部页数
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryQkxqById(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			String filepath = qkBasePath + data.getString("PATH");
			List<PageData> fileList = ReadFile.readfile(filepath);
		    //List<PageData> qkxqs =(List<PageData>) dao.findForList("AppQkxqMapper.queryByQkid",data);
			return JSONArray.toJSONString(fileList).toString();
		}
		return null; 
	}  
}

 

