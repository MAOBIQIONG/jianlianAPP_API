package org.yx.services.common; 

import java.util.List;

import javax.annotation.Resource; 

import org.springframework.stereotype.Service;
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData;

@Service("appUserLoginService")
public class AppUserLoginService{  
	
	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	 
	//根据手机号查找用户是否被注册
	public List<PageData> queryUserByPhone(PageData pd) throws Exception {
		return (List<PageData>) this.dao.findForList("AppUsersMapper.queryUserByPhone", pd);
	}
	
	//根据用户id查询用户信息
	public PageData findById(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("AppUsersMapper.findById", pd);
	}
	
	//会员注册
	public Object register(PageData pd) throws Exception {
		return this.dao.save("AppUsersMapper.register", pd);
	}
	
	//修改密码
	public Object editPassWord(PageData pd) throws Exception {
		return this.dao.update("AppUsersMapper.editPassWord", pd);
	}
	
	//绑定邮箱
	public Object bindEmail(PageData pd) throws Exception {
		return this.dao.update("AppUsersMapper.bindEmail", pd);
	} 
	
	/*
	* 通过用户名或者卡号获取数据
	*/
	public List<PageData> doCheckLogin(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AppUsersMapper.doCheckLogin", pd);
	} 
	
	/*
	* 第三方登录成功之后返回信息
	*/
	public PageData thirdLogin(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppUsersMapper.thirdLogin", pd);
	}  
	
	/*
	*根据用户id查询用户升级入会修改的信息
	*/
	public PageData queryUpgradeInfo(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppUsersMapper.queryUpgradeInfo", pd);
	}  
	
	/*
	* 登录成功后修改登录时间和客户id
	*/
	public Object updateCidAndDate(PageData pd)throws Exception{
		return dao.update("AppUsersMapper.updateCidAndDate", pd);
	} 
	
	/*
	* 保存第三方登录信息
	*/
	public Object save(PageData pd)throws Exception{
		return dao.save("AppUsersMapper.adduser", pd);
	} 
	
	/*
	* 修改保存融云的信息
	*/
	public Object editImToken(PageData pd)throws Exception{
		return dao.update("AppUsersMapper.editImToken", pd);
	} 
	
	/*
	* 设置网易云信token
	*/
	public Object editWyToken(PageData pd)throws Exception{
		return dao.update("AppUsersMapper.editWyToken", pd);
	} 
	
	/*
	* 修改用户信息
	*/
	public boolean edit(PageData pd)throws Exception{ 
		Object ob=dao.update("AppUsersMapper.edit", pd);
		if(ob==null){
			return false;
		}
		if(Integer.parseInt(ob.toString())>=1){
			return true;
		}else{
			return false;
		} 
	}  
	
	/*
	* 删除用户信息
	*/
	public boolean deleteUser(PageData pd)throws Exception{ 
		Object ob=dao.update("AppUsersMapper.deleteUser", pd);
		if(ob==null){
			return false;
		}
		if(Integer.parseInt(ob.toString())>=1){
			return true;
		}else{
			return false;
		} 
	}  
}

 

