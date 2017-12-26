package org.yx.services;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.BarcodeFactory;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;


/**
 * 
 * @author zandezhang
 * 店铺信息
 */

@Service("appShopService")
public class AppShopService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
    
	Object object = null;
	
	//根据用户code查询用户店铺
	public PageData findShopByUserId(PageData pd)throws Exception{
		return (PageData) dao.findForObject("AppShopMapper.findShopByUserId", pd);
	}
	
	//查询店铺详情
	public String findDetail(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
		    PageData de =(PageData) dao.findForObject("AppShopMapper.findDetail", data);
		    //获取商品二维码
		    String QRcode = BarcodeFactory.createShopQrcode(de.getString("ID"));
		    de.put("QRCODE", QRcode);
			return JSONObject.fromObject(de).toString();
		}
		return null;
	}
	
	//验证店铺名称是否存在
	public String checkShopName(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData de=(PageData) dao.findForObject("AppShopMapper.checkShopName", data);
			if(!EmptyUtil.isNullOrEmpty(de)){
				return JSONObject.fromObject(de).toString();
			}
			return null;
		}
		return null;
	} 
	
	//查询开店指南列表
	public String findShopGuideList(PageData pd) throws Exception{
		List<PageData> list=(List<PageData>) dao.findForList("AppShopMapper.findShopGuideList", null);
		List<PageData> rsultlist=new ArrayList<PageData>();
		
		if(!EmptyUtil.isNullOrEmpty(list)){
			for (PageData p1 : list) {
				List<PageData> newlist=new ArrayList<PageData>();
				for (PageData p2 : list) {
					if(p1.getString("ID").equals(p2.getString("PARENT_ID"))){
						newlist.add(p2);
					}
				}
				if(!EmptyUtil.isNullOrEmpty(newlist)){
					p1.put("child", newlist);
					rsultlist.add(p1);
				}
			}
			return JSONArray.fromObject(rsultlist).toString();
		}else{
			return null;
		}
	}
	
	//查询店铺指南
	public String findShopGuide(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
		    PageData de =(PageData) dao.findForObject("AppShopMapper.findShopGuide", data);
			return JSONObject.fromObject(de).toString();
		}
		return null;
	}
	
	//查询店铺名称
		public String findShopName(PageData pd)throws Exception{
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
			    PageData de =(PageData) dao.findForObject("AppShopMapper.findShopName", data);
				return JSONObject.fromObject(de).toString();
			}
			return null;
		}
		
		//查询店铺简介
		public String findShopDesc(PageData pd)throws Exception{
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
			    PageData de =(PageData) dao.findForObject("AppShopMapper.findShopDesc", data);
				return JSONObject.fromObject(de).toString();
			}
			return null;
		}
		
		//查询店铺主题
		public String findShopTheme(PageData pd)throws Exception{
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
			    PageData de =(PageData) dao.findForObject("AppShopMapper.findShopTheme", data);
				return JSONObject.fromObject(de).toString();
			}
			return null;
		}
		
		//查询店铺头像
		public String findShopPortralt(PageData pd)throws Exception{
			PageData data = pd.getObject("params");
			if(!EmptyUtil.isNullOrEmpty(data)){
			    PageData de =(PageData) dao.findForObject("AppShopMapper.findShopPortralt", data);
				return JSONObject.fromObject(de).toString();
			}
			return null;
		}
	
	/**
	 * 新增店铺信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String save(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			data.put("ID", UuidUtil.get32UUID());
		    object = dao.save("AppShopMapper.save", data);
		    
		    if(Integer.parseInt(object.toString()) > 0){
		    	//查询店铺信息
		    	PageData userIdPd=new PageData();
		    	userIdPd.put("userId", data.getString("USER_ID"));
		    	PageData shopDetail=(PageData) dao.findForObject("AppShopMapper.findShopByUserId", userIdPd);
		    	String userId=shopDetail.getString("USER_ID");
		    	shopDetail.remove("USER_ID");
		    	shopDetail.remove("CREATE_DATE");
		    	shopDetail.put("userId", userId);
			    return JSONObject.fromObject(shopDetail).toString();
		    }else{
			    return null;
		    }
		}
		return null;
	}
	
	/**
	 * 修改店铺名称
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object editShopName(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		object = dao.update("AppShopMapper.editShopName", data);
		
		if(Integer.parseInt(object.toString()) > 0){
		    return JSONObject.fromObject(object).toString();
	    }else{
		    return null;
	    }
	}
	
	/**
	 * 修改店铺简介
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object editShopDesc(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		object = dao.update("AppShopMapper.editShopDesc", data);
		
		if(Integer.parseInt(object.toString()) > 0){
		    return JSONObject.fromObject(object).toString();
	    }else{
		    return null;
	    }
	}
	
	/**
	 * 修改店铺主题
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object editShopTheme(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		object = dao.update("AppShopMapper.editShopTheme", data);
	    
		if(Integer.parseInt(object.toString()) > 0){
		    return JSONObject.fromObject(object).toString();
	    }else{
		    return null;
	    }
	}
	
	
	/**
	 * 修改店铺头像
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object editShopPortral(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		object = dao.update("AppShopMapper.editShopPortral", data);
		
		if(Integer.parseInt(object.toString()) > 0){
		    return JSONObject.fromObject(object).toString();
	    }else{
		    return null;
	    }
	}
	
	/**
	 * 查询店铺分享次数
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findShareCount(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			String day = data.getString("DAY");
			
			if(day != null && !"".equals(day)){
				data.put("CREATEDATE", AppOrderService.getDateBefore(Integer.valueOf(day)));
				data.put("ENDDATE", AppOrderService.getDateBefore(1));
			}
			
		    List<PageData> de =(List<PageData>) dao.findForList("AppShopMapper.findShareCount", data);
		    
			return JSONArray.fromObject(de).toString();
		}
		return null;
	}
	
	/**
	 * 查询浏览次数
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findViewCount(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			Date now1 = new Date(); 
	        Calendar now = Calendar.getInstance();  
	        now.setTime(now1);  
	        now.set(Calendar.DATE, now.get(Calendar.DATE) - 1);  
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
	        String format = sdf.format(now.getTime());
	        data.put("DATE", format);
		    PageData de = (PageData) dao.findForObject("AppShopMapper.findViewCount", data);
		    
			return JSONObject.fromObject(de).toString();
		}
		return null;
	}
	
	
	//添加分享次数
	public String addShare(PageData pd) throws Exception{
		PageData data=pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			Date now1 = new Date();  
	        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
	        String format = sdf.format(now1);
	        data.put("ID", UuidUtil.get32UUID());
	        data.put("DATE", format);
			PageData de =(PageData) dao.save("AppShopMapper.addShare",data);
			return JSONObject.fromObject(de).toString();
		}
		return null;
	}
	
	
	
}
