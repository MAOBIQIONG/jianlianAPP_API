package org.yx.controller;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;
import org.yx.services.AppOrderService;
import org.yx.services.AppPayService; 

import com.alibaba.fastjson.JSONArray;

import cn.beecloud.BCCache;
import cn.beecloud.BeeCloud;

@Controller
@RequestMapping(value="/appWebhook")
public class AppWebhookController extends BaseController {  

	@Resource(name="appPayService")
	private AppPayService appPayService;

	
	@Resource(name="appOrderService")
	private AppOrderService appOrderService;  
	
	private static String appID="d79690c1-c717-47f8-b0be-10e7eebb11bf";
	private static String testSecret="2323606a-3fce-4736-bec3-e66a5f8ea4f5";
	private static String appSecret="ff093372-f9ea-45a2-b666-9159cd8c4ecc";
	private static String masterSecret="91b10d75-e377-44ea-aa8d-29aacb306281";   

	    boolean verify(String sign, String text, String key, String input_charset) {
	        text = text + key;
	        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset)); 
	        
	        logBefore(this.logger,"appWebhook","verify",mysign);  

	        long timeDifference = System.currentTimeMillis() - Long.valueOf(key);
	        logger.info("timeDifference:" + timeDifference);
	        if (mysign.equals(sign) && timeDifference <= 300000) {
	            return true;
	        } else {
	            return false;
	        }
	    }

	    boolean verifySign(String sign, String timestamp) {
	    	logBefore(this.logger,"appWebhook","verifySign","sign:" + sign+"timestamp:" + timestamp); 
	       
	        return verify(sign, BCCache.getAppID() + BCCache.getAppSecret(), timestamp, "UTF-8");

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
		 * 支付成功后回调地址
		 * @throws Exception 
		 */
		@RequestMapping({ "/success" })
		@ResponseBody
		public String success(HttpServletRequest request,HttpServletResponse response) throws Exception{
			BeeCloud.registerApp(appID,testSecret,appSecret,masterSecret); 
			//开启测试模式
			//BeeCloud.setSandbox(true);
		    StringBuffer json = new StringBuffer();
		    String line = null;  
		   try {
		        request.setCharacterEncoding("utf-8"); 
		        BufferedReader reader = request.getReader();
		        while ((line = reader.readLine()) != null) {
		            json.append(line);
		        }
		    } catch (Exception e) {
		    	 logger.error("支付回调接口错误：",e); 
		        e.printStackTrace();
		    }  
		   logBefore(this.logger,"appWebhook","success",json.toString()); 
		    if(json.toString()!=null&&!"".equals(json.toString())){
		    	JSONObject jsonObj = JSONObject.fromObject(json.toString());
		    	System.out.println(jsonObj);
		    	logger.info("订单id:" + jsonObj); 
			    String sign =jsonObj.getString("sign");
			    String timestamp =jsonObj.getString("timestamp");
			    boolean status = verifySign(sign, timestamp); 
			    if (status) { //验证成功  
			    	String transaction_id =jsonObj.getString("transaction_id"); 
			    	PageData pa =new PageData(); 
			    	pa.put("ORDER_NO",transaction_id);
			    	System.out.println(pa.getString("ORDER_NO"));
			    	logger.info("orderId:" + pa.getString("ORDER_NO")); 
			    	PageData  orders=(PageData) appOrderService.findOrderPaySuccess(pa);  
			    	if(!EmptyUtil.isNullOrEmpty(orders)){ 
			    		BigDecimal  price=(BigDecimal) orders.get("TOTAL_PRICE");//数据库中订单的金额   
			    		String transaction_fee =jsonObj.getString("transaction_fee"); //beecloud平台订单的金额
			    		BigDecimal result=price.multiply(new BigDecimal(100));//将price乘以100，变为分 
			   		    int amount=result.intValue(); 
				    	if(String.valueOf(amount).equals(transaction_fee)){//订单金额匹配
				    		orders.put("ORDER_STATU","01");//支付后订单状态变为待发货
				    		//修改订单状态
				    		Object re=appOrderService.editOrderPay(orders);
				    		logger.info("修改订单状态:service：【appOrderService】方法：【editOrderPay】,参数："+JSONArray.toJSONString(orders)); 
				    		System.out.println(Integer.valueOf(re.toString())+"+++++++++++++++++++++++++++++++++++++");
				    		//记录订单支付信息
				     	   if(Integer.valueOf(re.toString()) >0){
				     			PageData data=new PageData(); 
				    			data.put("ID",get32UUID());
				    			data.put("ORDER_NO",jsonObj.getString("transaction_id")); 
				    			data.put("PAY_TOTALPRICE",jsonObj.getString("transaction_fee")); 
				    			data.put("PAY_TYPE",jsonObj.getString("sub_channel_type"));
				    			data.put("PAY_STATE","00");//支付成功
				    			Object ob=appPayService.save(data);
				    			logger.info("添加支付信息:service：【appPayService】方法：【save】,参数："+JSONArray.toJSONString(data)); 
				    			if(Integer.valueOf(ob.toString()) >= 1){  
				    				logAfter(this.logger,"appWebhook","success","支付回调接口成功，success！"); 
				    				return "success"; 
				    			}else{
				    				logger.error("支付接口回调失败,添加支付信息失败:service：【appPayService】方法：【save】,参数："+JSONArray.toJSONString(data)); 
				    				System.out.println("fail1");
				    				return "fail"; 
				    			} 
				    		}else{
				    			logger.info("支付接口回调失败，修改订单状态失败:service：【appOrderService】方法：【editOrderPay】,参数："+JSONArray.toJSONString(orders)); 
				    			System.out.println(Integer.valueOf(re.toString())+"===========================");
				    			System.out.println("fail2");
				    			return "fail";
				    		}
				    	}else{
				    		logger.info("支付接口回调失败，订单金额不正确，请注意查看，订单id为："+transaction_id); 
				    		System.out.println("fail2");
				    		return "fail";
				    	}
			    	}else{
			    		logger.info("支付接口回调失败，订单信息不存在，请注意查看，订单id为："+transaction_id); 
			    		System.out.println("fail3");
			    		return "fail";
			    	} 
			    } else { //验证失败
			    	logger.error("支付接口回调失败,订单信息验证未通过，请注意查看，订单id为："+jsonObj.getString("transaction_id")); 
			    	System.out.println("fail4");
			    	return "fail";
			    }
		    }else{
		    	logger.error("支付接口回调失败,订单信息为空！"); 
		    	System.out.println("fail5");
		    	return "fail";
		    } 
		} 
	} 