package org.yx.services;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;

/**
 * 
 * <b>类名：</b>ApiInterfaceValidateService.java<br>
 * <p><b>标题：</b>芝麻店接口Api</p>
 * <p><b>描述：</b>芝麻店接口Api</p>
 * <p><b>版权声明：</b>Copyright (c) 2017</p>
 * <p><b>公司：</b>上海诣新信息科技有限公司 </p>
 * @author <font color='blue'>陈鹏</font> 
 * @version 1.0.1
 * @date  2017年2月14日 下午1:33:32
 * @Description 接口方法参数验证service
 */
@Service("apiInterfaceValidateService")
public class ApiInterfaceValidateService {

	@Resource(name="daoSupport")
	private DaoSupport dao;
	
	
	//根据接口方法id查询接口验证集合
	public List<PageData> findValidateByMid(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("ApiInterfaceMethodMapper.findValidateByMid", pd);
	}
}
