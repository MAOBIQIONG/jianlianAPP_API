package org.yx.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.AppUtil;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;

import com.alibaba.fastjson.JSONArray;

@Service("appUserService")
public class AppUserService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	PageData _result = AppUtil.success();
	Object object = null;
	//查询findList
	public PageData findList(PageData pd)throws Exception{
		return (PageData) dao.findForList("AppUserMapper.findList", pd);
	}
	
	//修改用户信息
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object editUser(PageData pd) throws Exception{
		return this.dao.update("AppUserMapper.editUser", pd);
	}
	
	//设置支付密码
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object savepwd(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			object = this.dao.update("AppUserMapper.editPass", data);
			if(Integer.parseInt(object.toString()) > 0){
			    return JSONObject.fromObject(object).toString();
		    }else{
			    return null;
		    }
		}else{
		    return null;
	    }
	}
	
	
	/**
	 * 新增用户信息
	 */
	public String saveUser(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData result= (PageData) dao.findForObject("AppUserMapper.findUserByCode", data);
			if(EmptyUtil.isNullOrEmpty(result)){
				//新增
				data.put("ID", UuidUtil.get32UUID());
				object=dao.save("AppUserMapper.saveUser", data);
			}else{
				//修改
				object=dao.save("AppUserMapper.updateUser", data);
			}
		    if(Integer.parseInt(object.toString()) > 0){
			    return JSONObject.fromObject(object).toString();
		    }else{
			    return null;
		    }
		}
		return null;
	}
	
	/**********************************************用户收货地址相关信息*****************************************************/
	/**
	 * 查询用户收货地址列表信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findUserAddressList(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			List<PageData> addressList = (List<PageData>) dao.findForList("AppUserMapper.findUserAddressList", data);
			
			return JSONArray.toJSONString(addressList);
		}
		return null;
		
	}
	
	/**
	 * 查询用户默认收货地址信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findUserDefaultAddress(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData defaultAddress = (PageData) dao.findForObject("AppUserMapper.findUserDefaultAddress", data);
			
			return JSONObject.fromObject(defaultAddress).toString();
		}
		return null;
	}
	
	/**
	 * 新增用户收货地址信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object saveAddress(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			//将以前的默认地址设为非默认
			object = dao.update("AppUserMapper.editAddressis", data);
			//新增(自动设改地址为默认)
			String id=UuidUtil.get32UUID();
			data.put("ID", id);
		    object = dao.save("AppUserMapper.saveAddress", data);
		    Map<String, Object> map=new HashMap<String, Object>();
		    map.put("ID", id);
		    if(Integer.parseInt(object.toString()) > 0){
			    return JSONObject.fromObject(map).toString();
		    }else{
			    return null;
		    }
		    
		}
		return null;
		
	}
	
	
	/**
	 * 编辑用户收货地址信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object editAddress(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){

		    object = dao.update("AppUserMapper.editAddress", data);
		    
		    if(Integer.parseInt(object.toString()) > 0){
			    return JSONObject.fromObject(object).toString();
		    }else{
			    return null;
		    }
		    
		}
		return null;
	    
	}
	
	/**
	 * 设置地址为默认
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object editIsdefault(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){

			//将以前的默认地址设为非默认
			object = dao.update("AppUserMapper.editAddressis", data);
			
			//设置改地址为默认
		    object = dao.update("AppUserMapper.editIsdefault", data);
		    
		    if(Integer.parseInt(object.toString()) > 0){
			    return JSONObject.fromObject(object).toString();
		    }else{
			    return null;
		    }
		    
		  
		}
		return null;
		
	}
	
	/**
	 * 删除地址
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object delAddress(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){

			object = dao.delete("AppUserMapper.delAddress", data);
		    
			if(Integer.parseInt(object.toString()) > 0){
			    return JSONObject.fromObject(object).toString();
		    }else{
			    return null;
		    }
		  
		}
		return null;
		
	}


}
