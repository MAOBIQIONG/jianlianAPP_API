package org.yx.services.common;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;


@Service("cateService")
public class CategoryService implements ICategoryService{
     
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	Logger log=Logger.getLogger(this.getClass());//日志信息  

	@Override
	public List<PageData> listByPId() { 
		List<PageData> list=null;
        try {
        	list=CacheHandler.getAllCates();
        	if(null==list||list.size()==0){
        		list= (List<PageData>) dao.findForList("AppCategoryMapper.listByPId");
        		CacheHandler.initCate(list);
        	}  
        } catch (Exception e) {
        	log.error("获取区域列表信息异常：", e);
        }
        return list;
	} 
}
