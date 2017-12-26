package org.yx.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.common.entity.PageData;

class CategoryCache implements ICache<List<PageData> ,String> {
	
	private int version;

	public static List<PageData> categoryCache=new ArrayList<PageData>();
	static boolean isInitFlag=false;
	
	@Override
	public void init(List<PageData> dataList) {
		if(isInitFlag){
			return; 
		}
		categoryCache=dataList;
		isInitFlag=true;
	}
	
	@Override
	public void reload(List<PageData> dataList) { 
		categoryCache.clear();
		categoryCache=dataList;
	}
	
	@Override
	public List<PageData> get(String id) { 
		return categoryCache;
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
