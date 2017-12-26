package org.yx.services;

import java.util.List;
import java.util.UUID;

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

/**
 * 
 * @author zandezhang
 * 店铺信息
 */

@Service("appCartService")
public class AppCartService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
    
	PageData _result = AppUtil.success();
	Object object = null;
	
	/**
	 * 查看购物车
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findCartList(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
		    List<PageData> de =(List<PageData>) dao.findForList("AppCartMapper.findCartList", data);
			return JSONArray.toJSONString(de);
		}
		return null;
	}
	
	/**
	 * 查询购物车商品总数
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findCount(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
		    PageData de =(PageData) dao.findForObject("AppCartMapper.findCount", data);
			return JSONObject.fromObject(de).toString();
		}
		return null;
	}
	
	/**
	 * 空购物车最热销三款商品查询
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findHotProduct(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			List<PageData> de =(List<PageData>) dao.findForList("AppProductMapper.findHotProduct", data);
			return JSONArray.toJSONString(de);
		}
		return null;
	}
	
	
	
	/**
	 * 新增购物车
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object save(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData pro= (PageData) dao.findForObject("AppCartMapper.findIsProd", data);
			if(!EmptyUtil.isNullOrEmpty(pro)){
				data.put("ID", pro.get("ID"));
				data.put("PROD_NUM", (Integer.valueOf(pro.get("PROD_NUM").toString())+ Integer.valueOf(data.get("PROD_NUM").toString())));
				object = dao.update("AppCartMapper.editCart", data);
				if(Integer.parseInt(object.toString()) > 0){
				    return JSONObject.fromObject(object).toString();
			    }else{
				    return null;
			    }
			}else{
				data.put("ID", UuidUtil.get32UUID());
				object = dao.save("AppCartMapper.saveCart", data);
				if(Integer.parseInt(object.toString()) > 0){
				    return JSONObject.fromObject(object).toString();
			    }else{
				    return null;
			    }
			}
		}
		return null;
		
	}
	
	/**
	 * 修改购物车
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object edit(PageData pd)throws Exception{
	    List<PageData> list = (List<PageData>) pd.getList("params");
		if(list.size()>0){
		    for (int i = 0; i < list.size(); i++) {
		    	PageData data = list.get(i);
		    	
		    	//查询该商品是否已存在该用户的购物车中
				PageData PO= (PageData) dao.findForObject("AppCartMapper.findIsProd", data);
				
				
				if(!EmptyUtil.isNullOrEmpty(PO)){
					if(PO.getString("ID").equals(data.getString("ID")))
					{
						//修改
						object = dao.update("AppCartMapper.editCart", data);
					}else{
						PO.put("PROD_NUM", (Integer.valueOf(PO.get("PROD_NUM").toString())+ Integer.valueOf(data.get("PROD_NUM").toString())));
						//删除当前购物车商品信息
						object = dao.delete("AppCartMapper.deleteCart", data);
						//修改 商品数量累加
						object = dao.update("AppCartMapper.editCart", PO);
					}
					
					/*PO.put("PROD_NUM", (Integer.valueOf(PO.get("PROD_NUM").toString())+ Integer.valueOf(data.get("PROD_NUM").toString())));
					//删除当前购物车商品信息
					object = dao.delete("AppCartMapper.deleteCart", data);
					//修改 商品数量累加
					object = dao.update("AppCartMapper.editCart", PO);*/
				}else{
					object = dao.update("AppCartMapper.editCart", data);
				}
			}
		    if(Integer.parseInt(object.toString()) > 0){
			    return JSONObject.fromObject(object).toString();
		    }else{
			    return null;
		    }
		}
		return null;
	}
	
	
	/**
	 * 删除购物车
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object delete(PageData pd)throws Exception{
	    PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
		    object = dao.delete("AppCartMapper.deleteCart", data);
		    if(Integer.parseInt(object.toString()) > 0){
			    return JSONObject.fromObject(object).toString();
		    }else{
			    return null;
		    }
		}
		return null;
	}
	
	
}
