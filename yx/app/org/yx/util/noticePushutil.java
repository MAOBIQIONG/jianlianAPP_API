package org.yx.util;

import org.json.JSONObject;
import org.yx.common.entity.PageData;
import org.yx.wyyx.WyUtil;

public class noticePushutil {
 
	
	public boolean toNotice(String noticeText) throws Exception{   
	//获取notice.properties noticeText2内容。
    //String vv3=new String(noticeText.getBytes("ISO-8859-1"),"UTF-8");
		String notice1=noticeText;
        //实时更新后台管理消息
       WyUtil wy = new WyUtil();
       PageData npd = new PageData();
       npd.put("body", "{'msg':'"+notice1+"'}");
         String result = wy.sendMsg(npd);
         JSONObject jo = new JSONObject(result);
         if( "200".equals(jo.getString("code")) ){
           System.out.println("实时更新成功！");  
           return true;
         }else{
           System.out.println("实时更新失败！");  
           return false;
         }
	}

}
