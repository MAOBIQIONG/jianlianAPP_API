package org.yx.services.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yx.cache.CacheHandler;
import org.yx.common.entity.PageData;
import org.yx.services.ApiInterfaceMethodService;
import java.util.List;

/**
 * Created by zhangwei on 2017/7/31.
 */
@Service("cacheService")
public class CacheService implements ICacheService{
   @Autowired
   private ApiInterfaceMethodService apiInterfaceMethodService;
    @Autowired
   private IDictionaryService dictionaryService;
   @Autowired
   private IAppCacheService appCacheService;  

    @Override
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

    @Override
    public void reload() {

    }
}
