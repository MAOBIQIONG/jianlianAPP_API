package org.yx.services.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yx.cache.CacheHandler;
import org.yx.common.entity.PageData;
import org.yx.services.ApiInterfaceMethodService;
import org.yx.util.ConstantUtil;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Created by zhangwei on 2017/7/31.
 */
@Service("changeCacheService")
public class ChangeCacheService{
	
	@Resource(name = "apiInterfaceMethodService")
	private ApiInterfaceMethodService apiInterfaceMethodService; 
	
	@Resource(name = "dictionaryService")
	private DictionaryService dictionaryService; 
	
	@Resource(name = "appCacheService")
	private AppCacheService appCacheService;  
     
    
   public void init() throws Exception { 
    	
    	 //加载接口缓存
        List<PageData> codes= appCacheService.queryAll();
        CacheHandler.initAppCache(codes);
    	
        //加载接口缓存
        List<PageData> apis= apiInterfaceMethodService.findAllMethods();
        CacheHandler.initApiMethods(apis);

        //加载区域、城市缓存
        List<PageData> areas=dictionaryService.findAllAreaCities();
        CacheHandler.initAreasCities(areas); 
        
        //加载所有字典表缓存
        List<PageData> dics= dictionaryService.queryAllDics();
        CacheHandler.initSysDic(dics);
        
        //加载接口缓存
        List<PageData> cates= dictionaryService.queryAllCates();
        CacheHandler.initCate(cates);
        
        //加载接口缓存
        List<PageData> clans= dictionaryService.queryAllClans();
        CacheHandler.initClan(clans);
        
        //加载接口缓存
        List<PageData> xmlxs= dictionaryService.queryAllXmxs();
        CacheHandler.initXmlxs(xmlxs); 
    }
    
    public void initData(String name) throws Exception {
    	if(ConstantUtil.VERSION_API.equals(name)){
    		 //加载接口缓存
            List<PageData> apis= apiInterfaceMethodService.findAllMethods();
            CacheHandler.initApiMethods(apis);
    	}else if(ConstantUtil.VERSION_DIC.equals(name)){
    		 //加载所有字典表缓存
            List<PageData> dics= dictionaryService.queryAllDics();
            CacheHandler.initSysDic(dics);
    	}else if(ConstantUtil.VERSION_AREA.equals(name)){
    		 //加载区域、城市缓存
            List<PageData> areas=dictionaryService.findAllAreaCities();
            CacheHandler.initAreasCities(areas); 
    		
    	}else if(ConstantUtil.VERSION_CATE.equals(name)){
    		 //加载接口缓存
            List<PageData> cates= dictionaryService.queryAllCates();
            CacheHandler.initCate(cates);
    	}else if(ConstantUtil.VERSION_CLAN.equals(name)){
    		//加载接口缓存
            List<PageData> clans= dictionaryService.queryAllClans();
            CacheHandler.initClan(clans);
    	}else if(ConstantUtil.VERSION_XMLX.equals(name)){
    		//加载接口缓存
            List<PageData> xmlxs= dictionaryService.queryAllXmxs();
            CacheHandler.initXmlxs(xmlxs); 
    	}
    	
    	//加载接口缓存
        List<PageData> codes= appCacheService.queryAll(); 
        CacheHandler.initAppCache(codes);  
    } 
    
    public void reload() throws Exception {
    	//加载接口缓存
        List<PageData> codes= appCacheService.findAll();
    	CacheHandler.reloadAppCache(codes); 
    	
        //加载接口缓存
        List<PageData> apis= apiInterfaceMethodService.queryAllMethods();
        CacheHandler.reloadApiMethods(apis);

        //加载区域、城市缓存
        List<PageData> areas=dictionaryService.findAllAreaCities();
        CacheHandler.reloadAreasCities(areas);
        
        //加载所有字典表缓存
        List<PageData> dics= dictionaryService.findAllDics();
        CacheHandler.reloadSysDic(dics);
        
        //加载接口缓存
        List<PageData> cates= dictionaryService.findAllCates();
        CacheHandler.reloadCate(cates);
        
        //加载接口缓存
        List<PageData> clans= dictionaryService.findAllClans();
        CacheHandler.reloadClan(clans);
        
        //加载接口缓存
        List<PageData> xmlxs= dictionaryService.findAllXmxs();
        CacheHandler.reloadXmlxs(xmlxs);  
    }
    
    public void reload(String name) throws Exception {
    	if(ConstantUtil.VERSION_API.equals(name)){
    		//加载接口缓存
            List<PageData> apis= apiInterfaceMethodService.queryAllMethods();
            CacheHandler.reloadApiMethods(apis);
	   	}else if(ConstantUtil.VERSION_DIC.equals(name)){
	   		//加载所有字典表缓存
	        List<PageData> dics= dictionaryService.findAllDics();
	        CacheHandler.reloadSysDic(dics);
	   	}else if(ConstantUtil.VERSION_AREA.equals(name)){
	   		//加载区域、城市缓存
	        List<PageData> areas=dictionaryService.findAllAreaCities();
	        CacheHandler.reloadAreasCities(areas);
	   	}else if(ConstantUtil.VERSION_CATE.equals(name)){
	   		//加载接口缓存
	        List<PageData> cates= dictionaryService.findAllCates();
	        CacheHandler.reloadCate(cates);
	   	}else if(ConstantUtil.VERSION_CLAN.equals(name)){
	   		//加载接口缓存
	        List<PageData> clans= dictionaryService.findAllClans();
	        CacheHandler.reloadClan(clans);
	   	}else if(ConstantUtil.VERSION_XMLX.equals(name)){
	   		//加载接口缓存
	        List<PageData> xmlxs= dictionaryService.findAllXmxs();
	        CacheHandler.reloadXmlxs(xmlxs);  
	   	} 
    	//加载接口缓存
    	List<PageData> codes= appCacheService.findAll();
     	CacheHandler.reloadAppCache(codes); 
    }
}
