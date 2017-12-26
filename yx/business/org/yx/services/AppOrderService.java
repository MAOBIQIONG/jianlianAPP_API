package org.yx.services;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.AppUtil;
import org.yx.common.utils.DateUtil;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.RandomNum;
import org.yx.common.utils.UuidUtil;

import com.alibaba.fastjson.JSONArray;

/**
 * 
 * @author zandezhang
 * 店铺信息
 */

@Service("appOrderService")
public class AppOrderService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
    
	PageData _result = AppUtil.success();
	Object object = null;
	
	/**
	 * 查看订单列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
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
			String day = data.getString("DAY");
			
			if(day != null && !"".equals(day)){
				data.put("CREATEDATE", AppOrderService.getDateBefore(Integer.valueOf(day)));
			}
			
		    List<PageData> de =(List<PageData>) dao.findForList("AppOrderMapper.findOrderList", data);
		    
			return JSONArray.toJSONString(de);
		}
		return null;
	}
	
	/**
	 * 查看订单总数
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findOrderCount(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			String day = data.getString("DAY");
			
			if(day != null && !"".equals(day)){
				data.put("CREATEDATE", AppOrderService.getDateBefore(Integer.valueOf(day)));
				data.put("ENDDATE", AppOrderService.getDateBefore(1));
			}
			
		    PageData de =(PageData) dao.findForObject("AppOrderMapper.findOrderCount", data);
		    
			return JSONArray.toJSONString(de);
		}
		return null;
	}
	
	/**
	 * 查看订单销售业绩
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findOrderTotalPrice(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			
		    PageData de =(PageData) dao.findForObject("AppOrderMapper.findOrderTotalPrice", data);
		    
			return JSONArray.toJSONString(de);
		}
		return null;
	}
	
	/**
	 * 查看订单销售佣金总数
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findCommCount(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			
		    PageData de =(PageData) dao.findForObject("AppOrderMapper.findCommCount", data);
		    
			return JSONArray.toJSONString(de);
		}
		return null;
	}
	
	/**
	 * 查看销售佣金订单列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findCommList(PageData pd)throws Exception{
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
		    List<PageData> de =(List<PageData>) dao.findForList("AppOrderMapper.findCommList", data);
		    
			return JSONArray.toJSONString(de);
		}
		return null;
	}
	
	
	
	
	/**
	 * 新增订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Object save(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		//新增订单
		String orderid = UuidUtil.get32UUID();
		
		data.put("ID",orderid);
		//新建的时候默认状态为00，即待付款状态
		data.put("ORDER_STATU", "00");
		
		//data.put("CREATE_DATE", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		data.put("EXPRESS_NAME", "");
		data.put("EXPRESS_NO", "");
		if(!EmptyUtil.isNullOrEmpty(data)){
			String ORDER_NO = DateUtil.getTimeStamp()+RandomNum.getFourRandom();
			ORDER_NO= ORDER_NO.substring(2);
			data.put("ORDER_NO",ORDER_NO);
			object = dao.save("AppOrderMapper.saveOrder", data);
			
			//新增订单商品信息
			List<PageData> pro = data.getList("PROD");
			for (int i = 0; i < pro.size(); i++) {
				pro.get(i).put("ID", UuidUtil.get32UUID());
				pro.get(i).put("ORDER_NO", ORDER_NO);
				object = dao.save("AppOrderMapper.saveOrderPro",pro.get(i));
				
				//修改商品已售数量和商品sku已售数量
				object = dao.update("AppProductMapper.editProSkuNumi",pro.get(i));
				object = dao.update("AppProductMapper.editProNumi",pro.get(i));
			}
			
			return JSONObject.fromObject(data).toString();
		}
		return null;
	}
	
	/**
	 * 修改订单状态
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)//事物
	public Object editOrderStatu(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			
			String ORDER_STATU = data.getString("ORDER_STATU");

			object = dao.update("AppOrderMapper.editOrderStatu", data);
			//取消订单时，还原商品已售数量与商品相关sku已售数量
			if(ORDER_STATU.equals("20")){
				List<PageData> or = (List<PageData>) dao.findForList("AppOrderMapper.findOrderProSku", data);
				for (int i = 0; i < or.size(); i++) {
					//修改商品已售数量和商品sku已售数量
					object = dao.update("AppProductMapper.editProSkuNumd",or.get(i));
					object = dao.update("AppProductMapper.editProNumd",or.get(i));
				}
			}
			
			//订单状态改成已完成时，往数据库中更新完成时间
			if(ORDER_STATU.equals("03")){
				Date now1 = new Date();  
		        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		        String format = sdf.format(now1);
				data.put("FINISH_DATE", format);
				object = dao.update("AppOrderMapper.editOrderStatu", data);
			}
			
		    if(Integer.parseInt(object.toString()) > 0){
				    return JSONObject.fromObject(object).toString();
			}else{
				    return null;
		    }
		}
		return null;
	}
	
	public Object editOrderPay(PageData pd)throws Exception{
		if(!EmptyUtil.isNullOrEmpty(pd)){
			object = dao.update("AppOrderMapper.editOrderStatu", pd);
		}
		return object;
	}
	
	
	/**
	 * 查看订单详情
	 */
	public Object findDetail(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
		    PageData odetail = (PageData) dao.findForObject("AppOrderMapper.findOrderDetail", data);
			return JSONObject.fromObject(odetail).toString();
		}
		return null;
	}
	
	/**
	 * 确认付款是查询订单信息
	 */
	public Object findOrderPay(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
		    PageData odetail = (PageData) dao.findForObject("AppOrderMapper.findOrderPay", data);
			return JSONObject.fromObject(odetail).toString();
		}
		return null;
	}
	
	/**
	 * 确认付款是查询订单信息
	 */
	public Object findOrderPaySuccess(PageData pd)throws Exception{
		if(!EmptyUtil.isNullOrEmpty(pd)){
		    PageData odetail = (PageData) dao.findForObject("AppOrderMapper.findOrderPay", pd);
			return odetail;
		}
		return null;
	}
	
	/**
	 * 查看七日订单统计
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String findQrTJ(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		PageData da = new PageData(); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData YJ =(PageData) dao.findForObject("AppOrderMapper.findOrderTotalPrice", data);
		    PageData YONGJ =(PageData) dao.findForObject("AppOrderMapper.findCommCount", data);
		    
//		    List<PageData> VIEWTJ=(List<PageData>) dao.findForList("AppOrderMapper.findViewtj", data);
		    

		   /* List<PageData> ORDERTJ = (List<PageData>) dao.findForList("AppOrderMapper.findOrdertj", data);
		    List<PageData> SHARETJ=(List<PageData>) dao.findForList("AppOrderMapper.findSharetj", data);*/
		    List<PageData> ORDERTJ = new ArrayList<PageData>();
		    List<PageData> SHARETJ= new ArrayList<PageData>();
		    List<PageData> VIEWTJ =new ArrayList<PageData>();

		    
			String [] Createdate = AppOrderService.getDateBefore1();
			String [] Createdate1 = AppOrderService.getDateBefore2();
		    for (int i = 0; i < Createdate1.length; i++) {
		    	 data.put("CREATE_DATE", Createdate[i]);
		    	 PageData Sharetj =(PageData) dao.findForObject("AppOrderMapper.findSharetj", data);
		    	 PageData Ordertj =(PageData) dao.findForObject("AppOrderMapper.findOrdertj", data);
		    	 PageData viewtj =(PageData) dao.findForObject("AppOrderMapper.findViewtj", data);
		    	 if(Sharetj != null && !"".equals(Sharetj)){
		    		 SHARETJ.add(Sharetj);
		    	 }else{
		    		 Sharetj = new PageData();
//		    		 Sharetj.put("ID", ""+i);
//		    		 Sharetj.put("USER_ID", data.get("USER_ID"));
		    		 Sharetj.put("TIMES", ""+0);
		    		 Sharetj.put("DATE", Createdate1[i]);
//		    		 Sharetj.put("SEVEN_TJ", ""+0);
		    		 SHARETJ.add(Sharetj);
		    	 }
		    	 if(Ordertj != null && !"".equals(Ordertj)){
		    		 ORDERTJ.add(Ordertj);
		    	 }else{
		    		 Ordertj = new PageData();
//		    		 Ordertj.put("ID", ""+i);
//		    		 Ordertj.put("USER_ID", data.get("USER_ID"));
		    		 Ordertj.put("TIMES", ""+0);
//		    		 Ordertj.put("DATE", Createdate1[i]);
//		    		 Ordertj.put("SEVEN_TJ", ""+0);
		    		 ORDERTJ.add(Ordertj);
		    	 }
		    	 
		    	 if(viewtj != null && !"".equals(VIEWTJ)){
		    		 VIEWTJ.add(viewtj);
		    	 }else{
		    		 viewtj = new PageData();
		    		 viewtj.put("ID", ""+i);
		    		 viewtj.put("USER_ID", data.get("USER_ID"));
		    		 viewtj.put("TIMES", ""+0);
		    		 viewtj.put("DATE", Createdate1[i]);
		    		 viewtj.put("SEVEN_TJ", ""+0);
		    		 VIEWTJ.add(viewtj);
		    	 }
			}
		    
//		    da.put("DATE", Createdate1);
//		    da.put("DATA", aArray);
//		    da.put("TOTAL", TOTAL);*/
		    da.put("YJ", YJ);
		    da.put("YONGJ", YONGJ);
		    da.put("SHARETJ", SHARETJ);
		    da.put("ORDERTJ", ORDERTJ);
		    da.put("VIEWTJ", VIEWTJ);
			return JSONArray.toJSONString(da);
		}
		return null;
	}
	
	/**
	 * 查询订单中的商品是否下架
	 */
	public String findProdStatus(PageData pd)throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){
			List<String> list=Arrays.asList(data.getString("PROD_NO").split(","));
		    List<PageData> odetail = (List<PageData>)dao.findForList("AppOrderMapper.findProdStatus", list);
		    String status="";
		    int flag=0;
		    for(int i=0;i<odetail.size();i++){
		    	status = odetail.get(i).getString("STATUS");
		    	if(!status.equals("00")){
		    		flag=1;
		    		break;
		    	}
		    }
		    List<PageData> re=new ArrayList<PageData>();
		    re.add(new PageData());
		    re.get(0).put("STATUS", flag);
			return JSONArray.toJSONString(re);
		}
		return null;
	}
	
	
	public static String getDateBefore(int day) {  
		Date now1 = new Date(); 
        Calendar now = Calendar.getInstance();  
        now.setTime(now1);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);  
        
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        return sdf.format(now.getTime())+" 00:00:00";
    }   
	
	public static String[] getDateBefore1() {  
		Date now1 = new Date(); 
		String[] aArray = new String[7];  
		for (int i = 0; i < 7 ; i++) {
	        Calendar now = Calendar.getInstance();  
	        now.setTime(now1);  
	        now.set(Calendar.DATE, now.get(Calendar.DATE) - (i+1));  
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        	aArray[i] = sdf.format(now.getTime());
		}
		return aArray;
    }   
	
	public static String[] getDateBefore2() {  
		String[] aArray = new String[7];  
		for (int i = 6; i >=0 ; i--) {
			Date now1 = new Date(); 
	        Calendar now = Calendar.getInstance();  
	        now.setTime(now1);  
	        now.set(Calendar.DATE, now.get(Calendar.DATE) - (i+1));  
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("M月dd日");
	        aArray[i] = sdf.format(now.getTime());
		}
		return aArray;
    }
}
