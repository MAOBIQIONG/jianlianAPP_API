package org.yx.services;

import javax.annotation.Resource;

import org.springframework.stereotype.Service; 
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.exception.TokenTimeout;

import java.util.HashMap;


@Service("appCommonService")
public class AppCommonService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 根据token，返回userid
	 */
	public PageData findtokenDetail(PageData pd)throws Exception,TokenTimeout{
		
		PageData pa = new PageData();//pd.getObject("pa");
		PageData param = (PageData) dao.findForObject("AppCommonMapper.findtokenDetail", pd);
		if(param!=null){
			Object userid=param.get("userid");
			if(userid!=null){ 
				/*if(pd.containsKey("pa")){
					pa.put("userid", userid);
				}else{
					pa = new PageData();
					pa.put("userid", userid);
				}*/ 
				pa.put("userid", userid);
			}else{
				throw new TokenTimeout();//token过期或者无效 
			}
		}else{
			throw new TokenTimeout();//token过期或者无效
		} 
		return pa;
	}
}

