package org.yx.services.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;


@Service("appUsertokenService")
public class AppUsertokenService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增用户token信息
	*/
	public Object saveToken(PageData pd)throws Exception{
		return dao.save("AppUsertokenMapper.saveToken", pd);
	}
	/*
	* 修改用户token信息
	*/
	public Object editToken(PageData pd)throws Exception{
		return dao.update("AppUsertokenMapper.editToken", pd);
	}
	
	/*
	* 获取用户token信息
	*/
	public PageData findToken(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppUsertokenMapper.findToken", pd);
	}
	
	/*
	* 获取用户token信息
	*/
	public PageData queryNewCode(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppUsertokenMapper.queryNewCode", pd);
	} 
	
	/*
	* 检查用户是否已经存在token
	*/
	public PageData checkToken(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppUsertokenMapper.checkToken", pd);
	}
	
	
	/*
	* 根据token查询userid
	*/
	public PageData queryToken(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppUsertokenMapper.queryToken", pd);
	}
	
	
	/**
	 * 绑定第三方账号
	 */
	public Object updateUsertoken(PageData pd)throws Exception{
		return dao.update("AppUsertokenMapper.updateUsertoken", pd);
	}
	
	/*
	* 根据第三方查询token
	*/
	public PageData checkThirdparty(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppUsertokenMapper.checkThirdparty", pd);
	}
}

