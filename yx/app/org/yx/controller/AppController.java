package org.yx.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.AppUtil;
import org.yx.common.utils.Logger;
import org.yx.exception.RefuseAccessException;
import org.yx.exception.TokenTimeout;
import org.yx.util.AppUrl;

/**
 * 所有app接口应继承的控制器,包含客户端请求合法性验证
 * 
 * @author Administrator
 * 
 */
public class AppController extends BaseController {
	
//	@Autowired
//	AppuserService appuserService;

	// 允许app直接访问的 链接
	@Resource(name = "appurl")
	AppUrl appurl = null;

	@ModelAttribute
	private void validate(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData pd = this.getPageData();

		for (Object key : pd.keySet()) {
			if (pd.get(key) instanceof String) {
				pd.put(key, pd.getString(key).trim());
			}
		}

		/**
		 * 所有请求必须包含 SIGNATURE,TIMESTAMP,APPTOKEN,USER_ID 四个参数 且必须符合
		 * MD5(SIGNATURE+TIMESTAMP+URL) == APPTOKEN
		 */
		String url = request.getRequestURL().toString()
				.substring(request.getRequestURL().toString().indexOf("app"));
		boolean permited = false;
		for (String _url : appurl.getPermitted()) {
			if (_url.equals(url))
				permited = true;
		}

//		if (!permited && !url.contains("appLogin")) {
//			
//			if(AppUtil.validateParams(pd,"APPTOKEN","USER_ID")){//验证参数
//				// 验证TOKEN
//				String token = getToken(pd.getString("USER_ID"));
//				if (!pd.get("APPTOKEN").equals(token)) {
//					throw new TokenTimeout();
//				}
//			}else{
//				throw new RefuseAccessException();
//			}
//			
//		}

		/*
		 * if (!permited) { //验证参数合法性 if (AppUtil.validateParams(pd,
		 * "SIGNATURE", "TIMESTAMP", "APPTOKEN")) {
		 * 
		 * String md5 = MD5.md5(pd.getString("APPTOKEN") +
		 * pd.getString("TIMESTAMP") + url);
		 * 
		 * if (!pd.getString("SIGNATURE").equals(md5)) { throw new
		 * RefuseAccessException(); } } else throw new RefuseAccessException();
		 * //验证token 是否过期
		 * if(!pd.getString("APPTOKEN").equals(appuserService.findAppToken
		 * (pd))){ throw new TokenTimeout(); } }
		 */
	}

	@ExceptionHandler
	@ResponseBody
	public PageData exp(HttpServletRequest request, Exception ex) {
		ex.printStackTrace();
		if (ex instanceof RefuseAccessException)
			return AppUtil.refuseError();
		else if (ex instanceof TokenTimeout) {
			return AppUtil.timeoutError();
		}
		return AppUtil.otherError();

	}

	public PageData saveSuccess(String id) {
		PageData success = AppUtil.success();
		PageData pd = new PageData();
		pd.put("id", id);
		success.put("data", pd);
		return success;
	}

	protected void limit(PageData pd) {
		int size = AppUtil.size;
		int cp = Integer.parseInt(pd.getString("CURRENT_PAGE"));
		pd.put("LIMIT", "limit " + ((cp - 1) * size) + "," + (size));
	}

	protected void limit(int size, PageData pd) {
		int cp = Integer.parseInt(pd.getString("CURRENT_PAGE"));
		pd.put("LIMIT", "limit " + ((cp - 1) * size) + "," + (size));
	}

	protected Object getToken(String sesseionId) {
		Cache cache = CacheManager.getInstance().getCache("apptoken");
		Element token = cache.get(sesseionId);
		if(token == null){
			return null;
		}
		return token.getValue();
	}

	public Logger getLogger() {
		return Logger.getLogger(this.getClass());
	}
}
