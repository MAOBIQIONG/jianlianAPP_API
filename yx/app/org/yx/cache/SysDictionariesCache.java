package org.yx.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.common.entity.PageData;

 class SysDictionariesCache implements ICache<Map<String,List<PageData>> ,String> {
	
	private int version;

	public static Map<String,List<PageData>> sysDictionariesCache=new ConcurrentHashMap<String,List<PageData>>();
	static boolean isInitFlag=false;
	
	@Override
	public void init(Map<String, List<PageData>> dataMap) {
		//如果已经初始化，则不需要此操作。
		if(isInitFlag){
			return;
		}
		sysDictionariesCache=dataMap;
		isInitFlag=true; //修改初始化标志
	}
	
	@Override
	public void reload(Map<String, List<PageData>> dataMap) {
		sysDictionariesCache.clear();
		sysDictionariesCache=dataMap; 
	}
	
	@Override
	public Map<String, List<PageData>> get(String id) { 
		return sysDictionariesCache;
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
