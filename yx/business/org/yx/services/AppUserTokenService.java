package org.yx.services;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.DateUtil;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;

/**
 * 
 * <b>类名：</b>AppUserTokenService.java<br>
 * <p><b>标题：</b>芝麻店</p>
 * <p><b>描述：</b>芝麻店</p>
 * <p><b>版权声明：</b>Copyright (c) 2017</p>
 * <p><b>公司：</b>上海诣新信息科技有限公司 </p>
 * @author <font color='blue'>陈鹏</font> 
 * @version 1.0.1
 * @date  2017年3月6日 上午11:48:28
 * @Description 用户token
 */
@Service("appUserTokenService")
public class AppUserTokenService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 
	 * <b>方法名：</b>：saveToken<br>
	 * <b>功能说明：</b>：新增token<br>
	 * @author <font color='blue'>陈鹏</font> 
	 * @date  2017年3月9日 下午4:59:43
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String saveToken(PageData pd)throws Exception{
		Calendar calendar=Calendar.getInstance();
		Date date=new Date();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		date=calendar.getTime();
		pd.put("id", UuidUtil.get32UUID());
		pd.put("last_active_date", date);
		
		PageData data=findTokenByUserId(pd);
		if(EmptyUtil.isNullOrEmpty(data)){
			//用户token不存在创建token
			dao.save("AppUserTokenMapper.saveToken", pd);
			return pd.getString("token");
		}else{
			String last_active_date=data.getString("last_active_date");
			Date aa= DateUtil.fomatDate(last_active_date);
			if(aa.getTime()<new Date().getTime()){
				//用户token过期，更新token
				updateToken(pd);
				return pd.getString("token");
			}else{
				return data.getString("token");
			}
		}
	}
	
	/**
	 * 
	 * <b>方法名：</b>：findToken<br>
	 * <b>功能说明：</b>：根据userId查询token过期时间<br>
	 * @author <font color='blue'>陈鹏</font> 
	 * @date  2017年3月9日 下午5:33:48
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findTokenByUserId(PageData pd)throws Exception {
		return (PageData) dao.findForObject("AppUserTokenMapper.findTokenByUserId", pd);
	}
	
	/**
	 * 
	 * <b>方法名：</b>：findToken<br>
	 * <b>功能说明：</b>：根据token查询token过期时间<br>
	 * @author <font color='blue'>陈鹏</font> 
	 * @date  2017年3月9日 下午5:33:48
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findToken(PageData pd)throws Exception {
		return (PageData) dao.findForObject("AppUserTokenMapper.findToken", pd);
	}
	
	/**
	 * 
	 * <b>方法名：</b>：updateToken<br>
	 * <b>功能说明：</b>：根据用户id修改token信息<br>
	 * @author <font color='blue'>陈鹏</font> 
	 * @date  2017年3月9日 下午5:50:23
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Object updateToken(PageData pd)throws Exception{
		return dao.update("AppUserTokenMapper.updateToken", pd);
	}
	
	
}
