package org.yx.cache;

import org.yx.common.entity.PageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangwei on 2017/7/30.
 */
class ApiMethodsCache implements ICache< Map<String,PageData> ,String> {
	
	private int version;

    public static Map<String,PageData> apiMethodsCache=new ConcurrentHashMap<String,PageData>();
    static boolean isInitFlag=false;

    @Override
    public   void init(Map<String, PageData> stringPageDataMap) {
        //如果已经初始化过则跳过此操作
        if(isInitFlag){
            return;
        }
        apiMethodsCache=stringPageDataMap;
        isInitFlag=true;//修改初始化标志
    }

    @Override
    public void reload(Map<String, PageData> stringPageDataMap) {
        apiMethodsCache.clear();
        apiMethodsCache=stringPageDataMap;
    }

    @Override
    public PageData get(String id) {
        return apiMethodsCache.get(id);
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
