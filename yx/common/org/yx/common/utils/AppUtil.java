package org.yx.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.yx.common.entity.PageData;

import com.alibaba.fastjson.JSONObject;

public class AppUtil {
	protected static Logger logger = Logger.getLogger(AppUtil.class);
	
	public static int size = 5;

	public static void main(String[] args) {
		PageData pd = new PageData();
		pd.put("username", "zhangsan");

		checkParam("registered", pd);
	}

	public static boolean checkParam(String method, PageData pd) {
		boolean result = false;

		int falseCount = 0;
		String[] paramArray = new String[20];
		String[] valueArray = new String[20];
		String[] tempArray = new String[20];

		if (method == "registered") {
			paramArray = Const.APP_REGISTERED_PARAM_ARRAY;
			valueArray = Const.APP_REGISTERED_VALUE_ARRAY;
		} else if (method == "getAppuserByUsernmae") {
			paramArray = Const.APP_GETAPPUSER_PARAM_ARRAY;
			valueArray = Const.APP_GETAPPUSER_VALUE_ARRAY;
		}

		int size = paramArray.length;
		for (int i = 0; i < size; i++) {
			String param = paramArray[i];
			if (!pd.containsKey(param)) {
				tempArray[falseCount] = (valueArray[i] + "--" + param);
				falseCount++;
			}
		}

		if (falseCount > 0) {
			logger.error(method + "接口，请求协议中缺少 " + falseCount + "个 参数");
			for (int j = 1; j <= falseCount; j++)
				logger.error("   第" + j + "个：" + tempArray[(j - 1)]);
		} else {
			result = true;
		}

		return result;
	}

	public static PageData setPageParam(PageData pd) {
		String page_now_str = pd.get("page_now").toString();
		int pageNowInt = Integer.parseInt(page_now_str) - 1;
		String page_size_str = pd.get("page_size").toString();
		int pageSizeInt = Integer.parseInt(page_size_str);

		String page_now = pageNowInt + "";
		String page_start = pageNowInt * pageSizeInt + "";

		pd.put("page_now", page_now);
		pd.put("page_start", page_start);

		return pd;
	}

	public static List<PageData> setListDistance(List<PageData> list, PageData pd) {
		List<PageData> listReturn = new ArrayList();
		String user_longitude = "";
		String user_latitude = "";
		try {
			user_longitude = pd.get("user_longitude").toString();
			user_latitude = pd.get("user_latitude").toString();
		} catch (Exception e) {
			logger.error("缺失参数--user_longitude和user_longitude");
			logger.error("lost param：user_longitude and user_longitude");
		}

		PageData pdTemp = new PageData();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			pdTemp = list.get(i);
			String longitude = pdTemp.get("longitude").toString();
			String latitude = pdTemp.get("latitude").toString();
			String distance = MapDistance.getDistance(user_longitude, user_latitude, longitude, latitude);
			pdTemp.put("distance", distance);
			pdTemp.put("size", Integer.valueOf(distance.length()));
			listReturn.add(pdTemp);
		}

		return listReturn;
	}

	public static Object returnObject(PageData pd, Map map) {
		return map;
	}

	public static Object filterNull(Object object, String string) {
		return object == null ? string : object;
	}
	
	public static boolean validateParams(PageData pd, String... params) {
		for (String string : params) {
			if (!pd.containsKey(string)) {
				logger.error("缺少参数:"+string);
				return false;
			}
			if (pd.get(string) == null || pd.get(string).equals("")){
				logger.error("参数为空:"+string);
				return false;
			}
		}
		return true;
	}
	

	public static PageData success() {
		PageData result = new PageData();
		result.put("result", "01");
		result.put("reason", "成功");
		return result;
	}

	
	public static PageData success(Object data) {
		PageData result = success();
		result.put("data", data);
		return result;
	}
	
	public static PageData ss(Object data, String code, String describe, String resu, Object token) {
		PageData result = new PageData();
		result.put("data", JSONObject.toJSON(data));
		result.put("errcode", code);
		result.put("errmsg", describe);
		result.put("result", resu);
		result.put("token", token);
		return result;
	}
	
	public static PageData noData() {
		PageData result = new PageData();
		result.put("result", "02");
		result.put("reason", "没有找到相关数据!");
		return result;
	}

	public static PageData paramError() {
		PageData result = new PageData();
		result.put("result", "03");
		result.put("reason", "参数错误");
		logger.error(result.get("result")+" : "+result.get("reason"));
		return result;
	}

	public static PageData operateError() {
		PageData result = new PageData();
		result.put("result", "04");
		result.put("reason", "操作失败");
		logger.error(result.get("result")+" : "+result.get("reason"));
		return result;
	}

	public static PageData operateError(String message) {
		PageData result = new PageData();
		result.put("result", "04");
		result.put("reason", message);
		logger.error(result.get("result")+" : "+result.get("reason"));
		return result;
	}

	public static PageData noUpdate() {
		PageData result = new PageData();
		result.put("result", "05");
		result.put("reason", "no update!");
		logger.error(result.get("result")+" : "+result.get("reason"));
		return result;
	}



	public static PageData otherError() {
		PageData result = new PageData();
		result.put("result", "09");
		result.put("reason", "未知错误");
		logger.error(result.get("result")+" : "+result.get("reason"));
		return result;
	}
	

	public static PageData refuseError() {
		PageData result = new PageData();
		result.put("result", "08");
		result.put("reason", "拒绝请求");
		logger.error(result.get("result")+" : "+result.get("reason"));
		return result;
	}
	
	public static PageData timeoutError() {
		PageData result = new PageData();
		result.put("result", "07");
		result.put("reason", "请求过期");
		logger.error(result.get("result")+" : "+result.get("reason"));
		return result;
	}

	public static PageData otherError(String string) {
		PageData result = timeoutError();
		result.put("reason", string);
		logger.error(result.get("result")+" : "+result.get("reason"));
		return result;
	}
	
	public static ModelAndView mv_error(){
		return new ModelAndView("front/error"); 
	}
}

