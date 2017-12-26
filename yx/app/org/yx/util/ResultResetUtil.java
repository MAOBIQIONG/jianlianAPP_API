package org.yx.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yx.common.entity.PageData;

public class ResultResetUtil { 
	
	public static void resetStr(Map a ,String[] keys){ 
		for(int i=0;i<keys.length;i++){
			if(a.get(keys[i])==null||"".equals(a.get(keys[i]))){
				 a.put(keys[i],"");
			}else{
				a.put(keys[i],a.get(keys[i]).toString());
			} 
		} 
	}
	
	public static void resetInt(Map a ,String[] keys){ 
		for(int i=0;i<keys.length;i++){
			if(a.get(keys[i])==null||"".equals(a.get(keys[i]))){
				 a.put(keys[i],0);
			}else{
				a.put(keys[i],a.get(keys[i]).toString());
			} 
		} 
	}
	
	public static void resetResStr(Map a ,Map b,String[] keys){ 
		for(int i=0;i<keys.length;i++){
			if(a.get(keys[i])==null||"".equals(a.get(keys[i]))){
				if(b.get(keys[i])==null||"".equals(b.get(keys[i]))){
					a.put(keys[i],"");
				}else{
					a.put(keys[i],b.get(keys[i]));
				} 
			} 
		} 
	}
	
	public static void resetResInt(Map a ,Map b,String[] keys){ 
		for(int i=0;i<keys.length;i++){
			if(a.get(keys[i])==null||"".equals(a.get(keys[i]))){
				if(b.get(keys[i])==null||"".equals(b.get(keys[i]))){
					a.put(keys[i],0);
				}else{
					a.put(keys[i],b.get(keys[i]));
				} 
			} 
		} 
	}
	
	public static void resetImgs(String value,String name,String returnName,PageData data){
		String[] strs=value.split(",");
		List<PageData> imgs=new ArrayList<PageData>();
		for(int i=0;i<strs.length;i++){
			PageData img=new PageData();
			img.put(name,strs[i]);
			imgs.add(img);
		}
		data.put(returnName,imgs);
	}
}
