package org.yx.util;

import java.util.ArrayList;
import java.util.List;

import org.yx.cache.CacheHandler;
import org.yx.common.entity.PageData;

public class DataConvertUtil {
	
	public static void getDicRes(boolean flag,String[] datas,PageData result){
		if(flag){
			result.put("code","100"); //本地缓存数据和服务器缓存数据相同，可直接取本地缓存数据。 
		}else{
			PageData dataMap=new PageData();
			for(int i=0;i<datas.length;i++){
				List<PageData> data =CacheHandler.getDics(datas[i]);//查询活动类型   
				dataMap.put(datas[i],data);
			}
			result.put("code","200");  
			result.put("data",dataMap);  
			result.put("version",CacheHandler.getLocalVersion(ConstantUtil.VERSION_DIC));
		}   
	}
	
	public static void getRes(boolean flag,String version,PageData result){
		if(flag){
			result.put("code","100"); //本地缓存数据和服务器缓存数据相同，可直接取本地缓存数据。 
		}else{ 
			result.put("code","200");  
			result.put("data",getDataList(version));  
			result.put("version",CacheHandler.getLocalVersion(version));
		}  
	} 
	
	public static List<PageData> getDataList(String version){
		
		List<PageData> dataList=new ArrayList<PageData>(); 
		
		if(ConstantUtil.VERSION_AREA.equals(version)){
			dataList=CacheHandler.getAllAreas(); 
		}
		if(ConstantUtil.VERSION_CATE.equals(version)){
			dataList=CacheHandler.getAllCates(); 
		}
		if(ConstantUtil.VERSION_CLAN.equals(version)){
			dataList=CacheHandler.getAllClans();
		}
		if(ConstantUtil.VERSION_XMLX.equals(version)){
			dataList=CacheHandler.getXmlx();
		} 
		return dataList;
	}
}
