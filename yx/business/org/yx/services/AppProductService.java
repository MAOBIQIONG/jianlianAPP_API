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
import org.yx.common.utils.BarcodeFactory;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;

import com.alibaba.fastjson.JSONArray;

/**
 * 
 * @author zandezhang
 * 店铺商品信息
 */

@Service("appProductService")
public class AppProductService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
    
	Object object = null;
	//查询店铺商品列表
	public String findList(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("PAGENO");
			Object pageSiZe = data.get("PAGESIZE");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("PAGENO", 0);
				data.put("PAGESIZE", 5);
			}else{
				data.put("PAGENO", Integer.valueOf(data.getString("PAGENO")));
				data.put("PAGESIZE", Integer.valueOf(data.getString("PAGESIZE")));
				
			}
			
		    List<PageData> de =(List<PageData>) dao.findForList("AppProductMapper.findList", data);
		    return JSONArray.toJSONString(de);
		}
		return null;
	}
	
	//查询店铺商品详情
	public String findDetail(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
		    PageData de =(PageData) dao.findForObject("AppProductMapper.findDetail", data);
		    //获取商品二维码
		    String QRcode = BarcodeFactory.createProdQrcode(de.getString("PROD_NO"),de.getString("ID"),de.getString("SHOP_ID"));
		    de.put("QRCODE", QRcode);
			return JSONObject.fromObject(de).toString();
		}
		return null;
	}
	
	//查询店铺商品型号详情
	public String findXDetail(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
		    PageData de =(PageData) dao.findForObject("AppProductMapper.findXDetail", data);
			return JSONObject.fromObject(de).toString();
		}
		return null;
	}
	
	//查询用户店铺商品对象sku信息
	public String findShopBySku(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
		    PageData de =(PageData) dao.findForObject("AppProductMapper.findShopBySku", data);
			return JSONObject.fromObject(de).toString();
		}
		return null;
	}
	
	
	
	
	
}
