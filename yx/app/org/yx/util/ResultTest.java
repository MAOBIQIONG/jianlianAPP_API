package org.yx.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.yx.common.entity.PageData;
import org.yx.common.utils.ReturnCode;

import com.alibaba.fastjson.JSONArray;

public class ResultTest {

	public static void main(String[] strs){
		/*7.查询是否完善信息--未开发
		 * 
		8.查询完善信息用于展示--未修改
		9.修改/新增完善信息--未修改
		10.根据id查询升级入会信息--未开发
		11.申请退费--未开发
		12.支付列表：查询是否有发票信息--未修改
		13.查询发票信息--已存在
		14.修改/新增发票信息--已存在
		15.索取发票信息---未开发*/
		
		PageData res=new PageData(); 
		res.put("code", ReturnCode.SUCCESS);
		res.put("result",queryPays());
		res.put("msg", "操作成功"); 
		//7.查询是否完善信息--未开发
		//System.out.print(JSONArray.toJSONString(res).toString()); 
		//8.查询完善信息用于展示--未修改
		System.out.print(JSONArray.toJSONString(res).toString()); 
	}  
	
	//7.查询是否完善信息--未开发
	public static String completeInfo(){
		PageData res=new PageData();
		res.put("code", ConstantUtil.RES_SUCCESS);
		res.put("isComplete", 0);
		return JSONArray.toJSONString(res).toString();
	}
	
	//10.根据id查询升级入会信息--未开发
	public static String queryUpgrade(){  
		PageData conn=new PageData();  
		conn.put("ID","23546");
		conn.put("USER_ID","12134235");
		conn.put("ORDER_ID","201933452");
		conn.put("UPGRADE_LEVEL","04");
		conn.put("PRICE_TOPAY",8000);
		conn.put("STATUS","02");
		conn.put("NOPASS_REASON","信息不全");
		conn.put("CREATE_DATE",new Date());
		conn.put("AUDIT_DATE",new Date()); 
		return JSONArray.toJSONString(conn).toString();
	}
	
	//12.支付列表：查询是否有发票信息--未修改
	public static String queryPays(){
		PageData res=new PageData();
		List<PageData> list=new ArrayList<PageData>();
		for(int i=0;i<2;i++){
			PageData conn=new PageData(); 
			conn.put("ID","12345");
			conn.put("TYPE","01");
			conn.put("EVENT","升级成为常务副会长的支付费用！");
			conn.put("STATUS","03");
			conn.put("PRICE",8000);
			conn.put("DATE","2017-07-07 10:10:00"); 
			list.add(conn);
		}  
		res.put("pays",list);
		res.put("isHas",1); 
		return JSONArray.toJSONString(res).toString();
	} 
	
	//8.查询完善信息用于展示--未修改
	public static String queryCompleteInfo(){
		PageData res=new PageData();
		List<PageData> list=new ArrayList<PageData>();
		for(int i=0;i<2;i++){
			PageData conn=new PageData(); 
			conn.put("USID","USID");
			conn.put("PHONE","PHONE");
			conn.put("VIP_LEVEL","VIP_LEVEL");
			conn.put("COMID","COMID");
			conn.put("COMPANY_NAME","COMPANY_NAME");
			conn.put("CLID","CLID");
			conn.put("CLNAME","CLNAME");
			conn.put("HYID","HYID");
			conn.put("HYNAME","HYNAME");
			conn.put("CERTIFICATE_NAME","CERTIFICATE_NAME");
			conn.put("MPID","MPID");
			conn.put("IMG_PATH","IMG_PATH"); 
			list.add(conn);
		} 
		List<PageData> list1=new ArrayList<PageData>();
		for(int i=0;i<2;i++){ 
			PageData conn=new PageData(); 
			conn.put("value","value");
			conn.put("text","text"); 
			list1.add(conn);
		} 
		List<PageData> list2=new ArrayList<PageData>();
		for(int i=0;i<2;i++){
			PageData conn=new PageData(); 
			conn.put("value","value");
			conn.put("text","text"); 
			list2.add(conn);
		} 
		res.put("conns",list);
		res.put("cates",list1);
		res.put("areas",list2);
		return JSONArray.toJSONString(res).toString();
	}
}
