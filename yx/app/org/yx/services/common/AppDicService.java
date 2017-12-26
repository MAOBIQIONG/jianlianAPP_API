 package org.yx.services.common;
 
 import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service; 
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.Page;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;

import com.alibaba.fastjson.JSONArray;
 
 @Service("appDicService")
 public class AppDicService{
 
   @Resource(name="daoSupport")
   private DaoSupport dao;   
   
   Logger log=Logger.getLogger(this.getClass());//日志信息
  
	   public Object edit(PageData pd) throws Exception
	   {
		   log.info("service:【appDicService】 function：【edit】");
		   return  this.dao.update("AppDicMapper.edit", pd);
	   } 
	   
	   public List<PageData> queryByPBM(String p_bm) throws Exception
	   { 
		 log.info("service:【appDicService】 function：【queryByPBM】");
	     return (List)this.dao.findForList("AppDicMapper.queryByPBM", p_bm);
	   }

	   public List<PageData> listAllParentDict() throws Exception
	   {
		 log.info("service:【appDicService】 function：【listAllParentDict】");
	     return (List)this.dao.findForList("AppDicMapper.listAllParentDict", null);
	   }
	   
	   public PageData findById(PageData pd) throws Exception
	   {
		 log.info("service:【appDicService】 function：【findById】");
	     return (PageData)this.dao.findForObject("AppDicMapper.findById", pd);
	   }
	 
	   public PageData findCount(PageData pd) throws Exception
	   {
		 log.info("service:【appDicService】 function：【findCount】");
	     return (PageData)this.dao.findForObject("AppDicMapper.findCount", pd);
	   }
	   
	   public PageData findByBianma(PageData pd) throws Exception
	   {
		 log.info("service:【appDicService】 function：【findByBianma】");
	     return (PageData)this.dao.findForObject("AppDicMapper.findByBianma", pd);
	   } 
	   
	   public PageData findCountByParam(Page page) throws Exception
	   {
		 log.info("service:【appDicService】 function：【findCountByParam】");
	     return (PageData)this.dao.findForObject("AppDicMapper.findCountByParam", page);
	   }
	   
	   public PageData findBmCount(PageData pd) throws Exception
	   {
		 log.info("service:【appDicService】 function：【findBmCount】");
	     return (PageData)this.dao.findForObject("AppDicMapper.findBmCount", pd);
	   }
	 
	   public List<PageData> dictlistPage(Page page) throws Exception
	   {
		 log.info("service:【appDicService】 function：【dictlistPage】");
	     return (List)this.dao.findForList("AppDicMapper.dictlistPage", page);
	   }
	 
	   public List<PageData> dictlist(String parent_id)
	     throws Exception
	   {
		 log.info("service:【appDicService】 function：【dictlist】");
	     PageData dictPd = new PageData();
	     dictPd.put("PARENT_ID", parent_id);
	     return (List)this.dao.findForList("AppDicMapper.dictlist", dictPd);
	   }
	 
	   public List<PageData> findByPbm(String p_bm) throws Exception
	   {
		 log.info("service:【appDicService】 function：【findByPbm】");
	     PageData dictPd = new PageData();
	     dictPd.put("P_BM", p_bm);
	     return (List)this.dao.findForList("AppDicMapper.findByPbm", dictPd);
	   } 
 }