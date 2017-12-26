package org.yx.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.common.entity.PageData;

class ClanCache implements ICache< Map<String,List<PageData>> ,String> {
	
	private int version;

	public static Map<String,List<PageData>> clanCache=new ConcurrentHashMap<String,List<PageData>>();
	static boolean isInitFlag=false;
	
	@Override
	public void init(Map<String, List<PageData>> dataMap) {
		if(isInitFlag){
			return ;
		}
		clanCache=dataMap;
		isInitFlag=true; 
	}
	
	@Override
	public void reload(Map<String, List<PageData>> dataMap) { 
		clanCache.clear();
		clanCache=dataMap;
	}
	
	@Override
	public Map<String, List<PageData>> get(String id) { 
		return clanCache;
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
