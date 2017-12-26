package org.yx.util;

import org.yx.common.entity.PageData;

import com.alibaba.fastjson.JSONArray;

public class ResultUtil {
	
	//返回成功参数
	public static String successRes(){  
		return sucessRes_("");
	} 
	
	//返回成功参数
	public static String successMsg(String msg){  
		return sucessRes_(msg);
	} 
	
	//返回成功参数
	public static String successMsg(String msg,String token,PageData resut){  
		return resStr_(ConstantUtil.RES_SUCCESS,msg,token,resut);
	}
	
	//返回成功参数
	public static String failRes(){  
		return failRes_("");
	} 
	
	//返回成功参数
	public static String failMsg(String msg){  
		return failRes_(msg);
	} 
	
	private static String sucessRes_(String msg) { 
		return resStr_(ConstantUtil.RES_SUCCESS,msg);
	}  
	
	private static String failRes_(String msg){
		//
		return resStr_(ConstantUtil.RES_FAIL,msg);
	}

	private static String resStr_(String code ,String msg) { 
		return resStr_(code,msg,null,null); 
	} 
	
	private static String resStr_(String code ,String msg,String token,PageData result) { 
		PageData res=new PageData();
		res.put("code",code);
		if(msg!=null){
			res.put("msg",msg);
		} 
		if(token!=null){
			res.put("token",token);
		}
		if(result!=null){
			res.put("result",result);
		} 
		return JSONArray.toJSONString(res).toString();  
	} 
	
	private static String resString_(String code ,String result) { 
		PageData res=new PageData();
		res.put("code",code); 
		if(result!=null){
			res.put("result",result);
		} 
		return JSONArray.toJSONString(res).toString();  
	} 
	
	public static void main(String[] strs){
		//System.out.println(ResultUtil.result(new String[]{"200","测试"}));
	}
}
