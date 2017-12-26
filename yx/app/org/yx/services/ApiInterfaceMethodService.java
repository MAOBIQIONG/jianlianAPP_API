package org.yx.services;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 
 * <b>类名：</b>ApiInterfaceMethodService.java<br>
 * <p><b>标题：</b>芝麻店接口Api</p>
 * <p><b>描述：</b>芝麻店接口Api</p>
 * <p><b>版权声明：</b>Copyright (c) 2017</p>
 * <p><b>公司：</b>上海诣新信息科技有限公司 </p>
 * @author <font color='blue'>陈鹏</font> 
 * @version 1.0.1
 * @date  2017年2月14日 下午1:23:27
 * @Description API接口方法service
 */
@Service("apiInterfaceMethodService")
public class ApiInterfaceMethodService {
	
	@Autowired
	private DaoSupport dosSupport;

	/**
	 * 
	 * <b>方法名：</b>：findMethodList<br>
	 * <b>功能说明：</b>：根据接口code查询对象<br>
	 * @date  2017年2月14日 下午2:33:00
	 * @return
	 * @throws Exception
	 */
	public PageData findMethod(PageData pd)throws Exception{
		return (PageData) dosSupport.findForObject("ApiInterfaceMethodMapper.findMethod", pd);
	}



	/**
	 * 
	 * <b>方法名：</b>：findMethodCeshi<br>
	 * <b>功能说明：</b>：测试<br> 
	 * @date  2017年2月17日 下午1:39:53
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findMethodCeshi(PageData pd)throws Exception{
		PageData data=new PageData();
		data.put("name", "123");
		return JSONObject.fromObject(data).toString();
	}

	/**
	 *
	 * @return
	 */
	public List<PageData> findAllMethods() throws Exception{
		List<PageData> list= CacheHandler.getAllApiMethod();
			if(null==list||list.size()==0){
				list=(List<PageData>)dosSupport.findForList("ApiInterfaceMethodMapper.findAllMethods");
				CacheHandler.initApiMethods(list);
			} 
		return list;
	}
	
	/**
	 *
	 * @return
	 */
	public List<PageData> queryAllMethods() throws Exception{
		List<PageData> list=(List<PageData>)dosSupport.findForList("ApiInterfaceMethodMapper.findAllMethods");
		CacheHandler.initApiMethods(list); 
		return list;
	}

	/**
	 *
	 * @param apiID
	 * @return
	 * @throws Exception
	 */
	public PageData getMethod(String apiID)throws Exception{  
		PageData apiMethod=CacheHandler.getApiMethod(apiID);
		return apiMethod;
	} 
}
