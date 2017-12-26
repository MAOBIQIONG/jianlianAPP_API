package org.yx.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.AppUtil;
import org.yx.common.utils.MobileMessageCheck;
import org.yx.common.utils.MobileMessageSend;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping(value="/appMessage")
public class AppMessageController extends BaseController {  

	    /**
	     * 发送短信验证码
	     * @return
	     */
		@RequestMapping(value="/sendMsg")
		@ResponseBody
		public PageData sendMsg(){
			PageData pd=new PageData();
			pd=getPageData();
			logBefore(this.logger,"appMessage","sendMsg",JSONArray.toJSONString(pd).toString());  
			PageData _result=AppUtil.success();
			try {
				String co = MobileMessageSend.sendMsg(pd.getString("PHONE"));
				if(co.equals("success")){
					logger.info("获取验证码成功！");
					_result.put("reason", "success");
				}else{
					logger.info("获取验证码失败！");
					_result.put("reason", "error");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("错误信息",e);
				_result = AppUtil.otherError();
			}
			
			return _result;
		}
		
		/**
	     * 验证短信验证码
	     * @return
	     */
		@RequestMapping(value="/checkMsg")
		@ResponseBody
		public PageData checkMsg(){
			PageData pd=new PageData();
			pd=getPageData();
			logBefore(this.logger,"appMessage","checkMsg",JSONArray.toJSONString(pd).toString());  
			PageData _result=AppUtil.success();
			try {
				String co = MobileMessageCheck.checkMsg(pd.getString("PHONE"),pd.getString("CODE"));
				if(co.equals("success")){
					logger.info("验证短信验证码成功！");
					_result.put("reason", "success");
				}else{
					logger.info("验证短信验证码成功失败！");
					_result.put("reason", "error");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("错误信息",e);
				_result = AppUtil.otherError();
			} 
			return _result;
		} 
		
		
		
		@RequestMapping({ "/sendmessage" })
		@ResponseBody
	    public String sendMessage() throws IOException {
			PageData pd=new PageData();
			pd=getPageData();
			String co=null; 
			try {
				co= MobileMessageSend.sendMassage(pd.getString("PHONE"));  
				if(co.equals("success")){
					logger.info("验证短信验证码成功！"); 
				}else{
					logger.info("验证短信验证码成功失败！"); 
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("错误信息",e); 
			}  
	        return co;
	    } 
	} 