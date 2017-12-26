package org.yx.services.pay.impl;

import io.rong.RongCloud;
import io.rong.models.CodeSuccessResult;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date; 
import java.util.List; 

import javax.annotation.Resource; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData;
import org.yx.common.utils.AppUtil;
import org.yx.common.utils.DateUtil;
import org.yx.common.utils.EmptyUtil; 
import org.yx.common.utils.UuidUtil;
import org.yx.services.jy.inter.AppProjectServiceInter;

import cn.beecloud.BCCache;
import cn.beecloud.BeeCloud;

import com.alibaba.fastjson.JSONArray;  

@Service("appWebhookService")
public class AppWebhookService {
	private static String appID="34b9869a-3ba6-41c1-a6d3-aae1fb3c0049";
	private static String testSecret="eec0b8ba-ce5a-46a5-82e7-b192b0f396c4";
	private static String appSecret="a5982ad9-400f-46d1-981f-b27acb7fd6f5";
	private static String masterSecret="ad707adb-aa8a-44f4-8f16-4c9500bd3f9e"; 

	@Resource(name = "daoSupport")
	private DaoSupport dao;  
	 
	Logger log = Logger.getLogger(this.getClass());

    boolean verify(String sign, String text, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        log.info("mysign:" + mysign);

        long timeDifference = System.currentTimeMillis() - Long.valueOf(key);
        log.info("timeDifference:" + timeDifference);
        if (mysign.equals(sign) && timeDifference <= 300000) {
            return true;
        } else {
            return false;
        }
    }

    boolean verifySign(String sign, String timestamp) {
        log.info("sign:" + sign);
        log.info("timestamp:" + timestamp);

        return verify(sign, BCCache.getAppID() + BCCache.getAppSecret(),
                timestamp, "UTF-8");

    }

    byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    } 
    
    /**
	 * 支付成功回调地址（修改订单状态、修改会员等级或者修改活动信息）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping({ "/success" })
	@ResponseBody
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String success(HttpServletRequest request,HttpServletResponse response) throws Exception{
		BeeCloud.registerApp(appID,testSecret,appSecret,masterSecret); 
	    StringBuffer json = new StringBuffer();
	    String line = null;  
	   try {
	        request.setCharacterEncoding("utf-8"); 
	        BufferedReader reader = request.getReader();
	        while ((line = reader.readLine()) != null) {
	            json.append(line);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }  
	    if(json.toString()!=null&&!"".equals(json.toString())){
	    	JSONObject jsonObj = JSONObject.fromObject(json.toString());
	    	System.out.println(jsonObj);
		    String sign =jsonObj.getString("sign");
		    String timestamp =jsonObj.getString("timestamp");
		    boolean status = verifySign(sign, timestamp); 
		    if (status) { //验证成功  
		    	String transaction_id =jsonObj.getString("transaction_id"); 
		    	PageData pa =new PageData(); 
		    	pa.put("ID",transaction_id);
		    	System.out.println(pa.getString("ID"));
		    	PageData  orders=null;//appOrdersService.findById(pa);  
		    	Object rel=null;
		    	if(orders!=null){ 
		    		BigDecimal  price=(BigDecimal) orders.get("PRICE");//数据库中订单的金额   
		    		String transaction_fee =jsonObj.getString("transaction_fee"); //beecloud平台订单的金额
		    		BigDecimal result=price.multiply(new BigDecimal(100));//将price乘以100，变为分 
		   		    int amount=result.intValue(); 
			    	if(String.valueOf(amount).equals(transaction_fee)){//订单金额匹配
			    		orders.put("STATUS","03");
			    		orders.put("DATE",new Date());
			    		Object re=null;//appOrdersService.edit(orders);
			    		System.out.println("re="+Integer.valueOf(re.toString()));
			    		if(Integer.valueOf(re.toString()) >= 1){//修改订单状态成功！
			    			if("01".equals(orders.getString("TYPE"))){//支付会费
			    				pa.put("VIP_LEVEL","01");
					    		pa.put("IS_VIP","1");
					    		pa.put("UPGRADE_DATE",new Date());
					    		/*pa.put("DUE_DATE",DateUtil.getDate(DateUtil.getDay(new Date()),"12")); */
					    		pa.put("userid",orders.getString("USER_ID"));
					    		//rel=appusersService.edit(pa); //修改会员等级
					    		System.out.println("rel="+Integer.valueOf(rel.toString()));
			    			}else if("02".equals(orders.getString("TYPE"))){//支付活动费用
			    				//修改活动参与人数--新增活动参与人数 
			    				PageData actUser=new PageData(); 
			    				//actUser.put("ID",get32UUID());
			    				actUser.put("STATUS","01");
			    				actUser.put("APPLY_DATE", new Date()); 
		 						Object obj=null;//appActService.save(actUser);//保存活动参与人信息
		 						if(Integer.valueOf(obj.toString()) >= 1){
		 							PageData act=new PageData();
		 							act.put("ACTIVITY_ID",orders.getString("OBJECT_ID")); 
		 			 				//rel=appActService.updateCounts(act); //修改参与活动的人数
						    		System.out.println("rel="+Integer.valueOf(rel.toString()));
		 						}else{
		 							System.out.println("fail7");
				    				return "fail"; 
		 						}
			    			} 
			    			if(Integer.valueOf(rel.toString())>=1){//修改会员等级或者修改活动参与信息成功
				     			PageData data=new PageData(); 
				    			//data.put("ID",get32UUID());
				    			data.put("ORDER_ID",orders.getString("ID")); 
				    			data.put("PRICE",orders.get("PRICE")); 
				    			data.put("DESCRIPTION",orders.getString("EVENT"));
				    			data.put("PAY_TYPE",jsonObj.getString("sub_channel_type"));
				    			data.put("DATE",new Date()); 
				    			data.put("STATUS","03");//支付成功
				    			data.put("USER_ID",orders.getString("USER_ID")); 
				    			Object ob=null;//appPayService.save(data); 
				    			System.out.println("ob="+Integer.valueOf(ob.toString()));
				    			if(Integer.valueOf(ob.toString()) >= 1){ 
				    				return "success"; 
				    			}else{
				    				System.out.println("fail");
				    				return "fail"; 
				    			}
			    			}else{ 
			    				System.out.println("fail1");
				    			return "fail";
				    		}
			    		}else{
			    			System.out.println("fail2");
			    			return "fail";
			    		} 
			    	}else{
			    		System.out.println("fail3");
			    		return "fail";
			    	}
		    	}else{
		    		System.out.println("fail4");
		    		return "fail";
		    	} 
		    } else { //验证失败
		    	System.out.println("fail5");
		    	return "fail";
		    }
	    }else{
	    	System.out.println("fail6");
	    	return "fail";
	    } 
	}  
}
