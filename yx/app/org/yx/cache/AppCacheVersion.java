package org.yx.cache;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.common.entity.PageData;

class AppCacheVersion implements ICache<Map<String,PageData> ,String> {
	
	private int version;

	 public static Map<String,PageData> appCache=new ConcurrentHashMap<String,PageData>();
	 static boolean isInitFlag=false;
	    
	@Override
	public void init(Map<String, PageData> dataMap) {
		if(isInitFlag){
			return;
		}
		appCache=dataMap;
		isInitFlag=true; 
	}

	@Override
	public void reload(Map<String, PageData> dataMap) {
		appCache.clear();
		appCache=dataMap; 
	}

	@Override
	public PageData get(String id) { 
		return appCache.get(id);
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
