package org.yx.controller;

import io.rong.RongCloud;
import io.rong.models.TokenResult;

import java.util.Date;
import java.util.List; 

import javax.annotation.Resource; 

import org.apache.shiro.crypto.hash.SimpleHash; 
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.ChineseToEnglish;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.RandomNum;
import org.yx.common.utils.ReturnCode;
import org.yx.common.utils.UuidUtil;
import org.yx.common.utils.fileConfig;
import org.yx.services.common.AppUserLoginService;
import org.yx.services.common.AppUsertokenService;
import org.yx.services.common.ChangeCacheService;
import org.yx.util.CommonUtil;
import org.yx.util.Emoji; 
import org.yx.util.PushUtil;
import org.yx.util.ResultResetUtil;
import org.yx.util.ResultUtil;

import com.alibaba.fastjson.JSONArray;
import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.http.IGtPush;

 
/**
 *  
 * @Description 更新缓存
 */ 
@Controller
@RequestMapping({ "/appCache" })
public class AppCacheController extends BaseController{ 
	
	@Resource(name = "changeCacheService")
	private ChangeCacheService cacheService; 
	 
	
	/**
	 * 根据参数更新缓存数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/updateCacheData" })
	@ResponseBody
	public PageData updateCacheData(){ 
		PageData _result = new PageData(); 
		PageData pa = getPageData();  
		try{  
			logBefore(this.logger,"appCache","updateCacheData",JSONArray.toJSONString(pa).toString());  
			if(EmptyUtil.isNullOrEmpty(pa)){ 
				_result.put("code", "400");    
			}else{
				if(EmptyUtil.isNullOrEmpty(pa.get("name"))){
					this.cacheService.reload();
				}else{
					this.cacheService.reload(pa.getString("name"));
				} 
				_result.put("code", "200");   
			}  
		}catch(Exception e){
			e.printStackTrace();
			_result.put("code","400"); 
			logger.error("错误信息：",e); 
		}  
		return _result;
	}  
}
