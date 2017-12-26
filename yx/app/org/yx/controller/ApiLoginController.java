package org.yx.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.MD5;
import org.yx.common.utils.ReturnCode;
import org.yx.services.AppShopService;
import org.yx.services.AppUserService;
import org.yx.services.AppUserTokenService;

/**
 * 
 * <b>类名：</b>ApiLoginController.java<br>
 * <p><b>标题：</b>建联接口Api</p>
 * <p><b>描述：</b>建联接口Api</p>
 * <p><b>版权声明：</b>Copyright (c) 2017</p>
 * <p><b>公司：</b>上海诣新信息科技有限公司 </p>
 * @author <font color='blue'>朱玉洁</font> 
 * @version 1.0.1
 * @date  2017年5月16日 14:40:33
 * @Description 登录
 */
@Controller
@RequestMapping(value="/apiLogin")
public class ApiLoginController extends BaseController{
	
	@Resource(name="appUserService")
	private AppUserService appUserService;
	
	@Resource(name="appShopService")
	private AppShopService appShopService;
	
	@Resource(name="appUserTokenService")
	private AppUserTokenService appUserTokenService;
	
	@RequestMapping(value="/login")
	@ResponseBody
	public PageData saveOrUpdateUser(){
		PageData pd=new PageData();
		pd=getPageData();
		PageData _result=new PageData();
		try {
			String obj=appUserService.saveUser(pd);
			if(!EmptyUtil.isNullOrEmpty(obj)){
				//判断是否创建店铺
				PageData userPd=new PageData();
				String userId=pd.getObject("params").getString("EmployeeCode");
				userPd.put("userId",userId );
				PageData data=new PageData();
				if(!EmptyUtil.isNullOrEmpty(appShopService.findShopByUserId(userPd))){
					data=appShopService.findShopByUserId(userPd);
				}
				data.put("userId",  userId);
				
				//生成token
				PageData tokenPd=new PageData();
				tokenPd.put("s_app_users_id", userId);
				tokenPd.put("token", MD5.md5(new Date().toString()));
				String token=appUserTokenService.saveToken(tokenPd);
				data.put("token", token);
				
				//添加token信息
				_result.put("code", ReturnCode.SUCCESS);
				_result.put("result",data);
				_result.put("msg", "获取用户信息成功！");
			}else{
				_result.put("code", ReturnCode.FAIL);
				_result.put("msg", "获取用户信息失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			_result.put("code", ReturnCode.SERVICE_FAIL);
			_result.put("msg", "服务器繁忙，请稍后操作");
			return _result;
		}
		return _result;
	}

}
