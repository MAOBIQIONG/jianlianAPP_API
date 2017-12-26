package org.yx.services.common;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;

import javax.annotation.Resource;

import java.util.List;

/**
 * Created by zhangwei on 2017/7/31.
 */
@Service("dictionaryService")
public class DictionaryService implements IDictionaryService {
	Logger log=Logger.getLogger(this.getClass());//日志信息

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**
     * 获取全部区域城市数据
     *
     * @return
     */
    @Override
    public List<PageData> findAllAreaCities() {
        List<PageData> list=null;
        try {
            list= (List<PageData>) dao.findForList("DictionaryMapper.findAllAreasCities");
        } catch (Exception e) {
        	log.error("获取区域列表信息异常：", e);
        }
        return list;
    } 

    /**
     * 根据区域编码获取城市数据
     *
     * @param code
     * @return
     */
    @Override
    public List<PageData> findCitiesByAreaCode(String code) {
        return null;
    }
    
    /**
     * 查询全部字典表信息
     * @param code
     * @return
     */
    @Override
    public  List<PageData> queryAllDics(){
    	 List<PageData> list=null;
         try {
        	 list=CacheHandler.getAllDics();
	    	 if(null==list||list.size()==0){
	    		list= (List<PageData>) dao.findForList("AppDicMapper.queryAll");
        		CacheHandler.initSysDic(list);
        	 } 
         } catch (Exception e) {
         	log.error("获取区域列表信息异常：", e);
         }
         return list;
    }
    
    public  List<PageData> findAllDics(){
   	 List<PageData> list=null;
        try { 
	    	list= (List<PageData>) dao.findForList("AppDicMapper.queryAll");
       		CacheHandler.initSysDic(list); 
        } catch (Exception e) {
        	log.error("获取区域列表信息异常：", e);
        }
        return list;
   }
    
    /**
     * 查询字典表中某个类别的信息
     * @param code
     * @return
     */
    public List<PageData> queryDicByParam(String name) {
	   	 List<PageData> list=null;
	     try {
	    	 list=CacheHandler.getDics(name); 
	     } catch (Exception e) {
	     	log.error("获取区域列表信息异常：", e);
	     }
	     return list;
	}
    
    /**
     * 查询全部城市建联信息
     * @param code
     * @return
     */
    public List<PageData> queryAllClans() {
	   	 List<PageData> list=null;
	     try {
	    	 list=CacheHandler.getAllClans();
	    	 if(null==list||list.size()==0){
	    		list= (List<PageData>) dao.findForList("AppClanMapper.queryAll");
        		CacheHandler.initClan(list);
        	 }   
	     } catch (Exception e) {
	     	log.error("获取区域列表信息异常：", e);
	     }
	     return list;
	}  
    
    public List<PageData> findAllClans() {
	   	 List<PageData> list=null;
	     try { 
	    	 list= (List<PageData>) dao.findForList("AppClanMapper.queryAll");
       		 CacheHandler.initClan(list); 
	     } catch (Exception e) {
	     	log.error("获取区域列表信息异常：", e);
	     }
	     return list;
	}  
    
    /**
     * 查询全部行业信息
     * @param code
     * @return
     */
    public List<PageData> queryAllCates() {
	   	 List<PageData> list=null;
	     try {
	    	 list=CacheHandler.getAllCates();
        	 if(null==list||list.size()==0){
        		list= (List<PageData>) dao.findForList("AppCategoryMapper.queryCates"); 
        		CacheHandler.initCate(list);
        	 }  
	     } catch (Exception e) {
	     	log.error("获取区域列表信息异常：", e);
	     }
	     return list;
	}  
    
    public List<PageData> findAllCates() {
	   	 List<PageData> list=null;
	     try { 
       		list= (List<PageData>) dao.findForList("AppCategoryMapper.queryCates"); 
       		CacheHandler.initCate(list); 
	     } catch (Exception e) {
	     	log.error("获取区域列表信息异常：", e);
	     }
	     return list;
	}  
    
    /**
     * 查询全部项目类型信息
     * @param code
     * @return
     */
    public List<PageData> queryAllXmxs() {
	   	 List<PageData> list=null;
	     try {
	    	 list=CacheHandler.getXmlx();
	    	 if(null==list||list.size()==0){
	    		 list= (List<PageData>) dao.findForList("AppXmlxMapper.queryAll");
	    		 CacheHandler.initXmlxs(list);
	    	 } 
	     } catch (Exception e) {
	     	log.error("获取区域列表信息异常：", e);
	     }
	     return list;
	}  
    
    public List<PageData> findAllXmxs() {
	   	 List<PageData> list=null;
	     try { 
    		 list= (List<PageData>) dao.findForList("AppXmlxMapper.queryAll");
    		 CacheHandler.initXmlxs(list); 
	     } catch (Exception e) {
	     	log.error("获取区域列表信息异常：", e);
	     }
	     return list;
	}  
}
