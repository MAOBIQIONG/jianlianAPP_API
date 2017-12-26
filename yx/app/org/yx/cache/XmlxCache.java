package org.yx.cache;

import org.yx.common.entity.PageData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangwei on 2017/7/30.
 */
 class XmlxCache implements ICache< Map<String,List<PageData>> ,String> {
	 
	 private int version;

    public static Map<String,List<PageData>> xmlxCache= new ConcurrentHashMap<String,List<PageData>>();
    static boolean isInitFlag=false;

    @Override
    public   void init(Map<String, List<PageData>> stringPageDataMap) {
        //如果已经初始化过则跳过此操作
        if(isInitFlag){
            return;
        }
        xmlxCache=stringPageDataMap;
        isInitFlag=true;//修改初始化标志
    }

    @Override
    public void reload(Map<String, List<PageData>> stringPageDataMap) {
    	xmlxCache.clear();
    	xmlxCache=stringPageDataMap;
    }

    @Override
    public Map<String,List<PageData>> get(String id) {
        return xmlxCache;
    }

    @Override
    public boolean isInit() {
        return isInitFlag;
    }

	@Override
	public int getVersion() { 
		return version;
	}

	@Override
	public void setVerdion(int version) { 
		this.version=version;
	} 
}
