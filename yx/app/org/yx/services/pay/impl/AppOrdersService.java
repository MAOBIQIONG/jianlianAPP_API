package org.yx.services.pay.impl; 

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.DateUtil;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;
import org.yx.services.pay.inter.AppOrdersServiceInter;
import org.yx.util.ConstantUtil;
import org.yx.util.PriceUtil;
import org.yx.util.PushUtil;
import org.yx.util.ResultUtil;
import org.yx.util.noticeConfig;
import org.yx.util.noticePushutil;

import com.alibaba.fastjson.JSONArray;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

@Service("appPayOrdersService")
public class AppOrdersService implements AppOrdersServiceInter{   
	
	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	
	public static String noticeText8 = noticeConfig.getString("noticeText5");
	
	PriceUtil priceUtil=new PriceUtil();
	
	/**
	 * 查看订单列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryOrders(PageData pd) throws Exception{
		return null;
	}
	
	/**
	 * 查看订单详细信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryOrdersInfo(PageData pd) throws Exception{
		return null;
	}
	
	/**
	 * 申请退费
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String cancleOrders(PageData pd) throws Exception{ 
		PageData data = pd.getObject("params");
		if(EmptyUtil.isNullOrEmpty(data))
			return null;
		data.put("STATUS","04");
		boolean rest =dao.update_("AppOrdersMapper.editOrdersStaus",data);  
        if(!rest){
        	return ResultUtil.failMsg("申请退款失败！");
        }else{
        	boolean rest1 =dao.update_("AppUpgradeMapper.editUpStatus",data); 
        	if(!rest1){
             	return ResultUtil.failMsg("申请退款失败！");
            }else{
            	return ResultUtil.successRes();
            } 
        }  
	}
	
	/**
	 * 索取发票信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String applyInvoice(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(EmptyUtil.isNullOrEmpty(data))
			return null;
		data.put("STATUS","07");
		boolean rest =dao.update_("AppOrdersMapper.editOrdersStaus",data); 
        if(rest){
        	return ResultUtil.successRes();
        }else{
        	return ResultUtil.failMsg("申请发票失败！");
        }  
	}
	
	/**
	 * 取消订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String toCancle(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(EmptyUtil.isNullOrEmpty(data))
			return null;
		data.put("STATUS","20");
		boolean rest =dao.update_("AppOrdersMapper.editOrdersStaus",data); 
		PageData datatype=(PageData) dao.findForObject("AppOrdersMapper.querybyddtype", data);
        if(!rest){
        	return ResultUtil.failMsg("取消订单失败！");
        }else{
        	if(datatype.get("TYPE").equals("01")){
	        	data.put("STATUS","05");
	        	boolean rest1 =dao.update_("AppUpgradeMapper.editUpStatus",data); 
	        	if(!rest1){
	             	return ResultUtil.failMsg("取消订单失败！");
	            }else{
	            	return ResultUtil.successRes();
	            }
        	}
        	return ResultUtil.successRes();
        }  
	}
	
	/**
	 * 升级入会生成订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addOrders(PageData pd){
		try{  
		PageData data = pd.getObject("params");
		if(EmptyUtil.isNullOrEmpty(data))
			return null;  
		List<PageData> ur =(List<PageData>) dao.findForList("AppUpgradeMapper.checkIsApply",data);
		if(ur!=null&&ur.size()>0){
			return ResultUtil.failMsg("您之前已经申请过该等级！");
		}
		PageData grade =(PageData) dao.findForObject("AppzUserMapper.queryUpIsOut",data);  
	    if(grade==null||"".equals(grade)){
	    	return ResultUtil.failMsg("没有升级入会信息！");
	    }
		PageData price=	priceUtil.calculatePrice(data.getString("LEVEL"), grade.getString("VIP"),grade.getString("VIP_LEVEL"));
	    if(!(Boolean) price.get("flag")){
	    	return ResultUtil.failMsg(price.getString("msg"));
        }
		/**
         * 生成订单信息
         */
	    String ORDER_ID = UuidUtil.get32UUID().substring(1, 21);
        PageData pagedata = new PageData();
        pagedata.put("ID",ORDER_ID);
        pagedata.put("USER_ID", data.get("USER_ID")); 
        pagedata.put("PRICE",Double.parseDouble(price.get("price").toString()));
        if("01".equals(data.getString("LEVEL"))){
       	    pagedata.put("EVENT",ConstantUtil.LEVEL_01);
        }
        if("03".equals(data.getString("LEVEL"))){
       	    pagedata.put("EVENT",ConstantUtil.LEVEL_02);
        }
        if("04".equals(data.getString("LEVEL"))){
       	    pagedata.put("EVENT",ConstantUtil.LEVEL_03);
        }
        if("05".equals(data.getString("LEVEL"))){
       	    pagedata.put("EVENT",ConstantUtil.LEVEL_04);
        }
        if("200".equals(data.getString("LEVEL"))){
       	    pagedata.put("EVENT",ConstantUtil.LEVEL_05);
        }
       
        pagedata.put("OBJECT_ID", "");
        pagedata.put("STATUS", "01");
        if(pagedata.get("USER_ID")!=null){
        	List odersize=(List)dao.findForList("UmbsMapper.queryRhordd",pagedata);
        	if(odersize.size()> 1){
        		pagedata.put("TYPE", "03");
        	}else{
        		pagedata.put("TYPE", "01");
        	}
        }
        pagedata.put("DATE", DateUtil.getTime());
        boolean result1 =  dao.save_("UmbsMapper.saveOrder",pagedata); 
        if(!result1){
        	return ResultUtil.failMsg("生成订单信息失败！");
        }
        PageData pdd = new PageData(); 
		pdd.put("ID", UuidUtil.get32UUID());
		pdd.put("USER_ID", data.get("USER_ID"));
		pdd.put("ORDER_ID",ORDER_ID); 
		pdd.put("UPGRADE_LEVEL",data.getString("LEVEL"));
		pdd.put("CREATE_DATE", DateUtil.getTime());
		pdd.put("PRICE_TOPAY",pagedata.getString("price"));
		pdd.put("STATUS", "00");//未支付 
		pdd.put("LEVEL", grade.getString("VIP")); 
		boolean result2 =  dao.save_("AppUpgradeMapper.save",pdd); 
		if(!result2){
			return ResultUtil.failMsg("生成升级入会信息失败！");
		} 
        PageData user =(PageData) dao.findForObject("AppUsersMapper.queryByPhone","13636534637");
        String content="有用户正在申请会员，请注意去后台审核！";
        //向会员管理中心经理推送审核消息
        push(user,content); 
        
      //获取notice.properties noticeText5内容。
	       String vv3=new String(noticeText8.getBytes("ISO-8859-1"),"UTF-8");
	    String notice1=vv3;
        noticePushutil notutil=new noticePushutil();
        notutil.toNotice(notice1);
		
		//添加用户系统消息
		PageData nofi = new PageData();
		nofi.put("ID", UuidUtil.get32UUID());
		nofi.put("USER_ID", user.get("ID"));
		nofi.put("CONTENT",content);
		nofi.put("CREATE_DATE", DateUtil.getTime()); 
		nofi.put("STATUS", "01"); 
		Object object =  dao.save("AppzUserMapper.saveUserMsg",nofi);//通知会员中心经理
		
		//添加用户系统消息
		PageData no_user = new PageData();
		no_user.put("ID", UuidUtil.get32UUID());
		no_user.put("USER_ID", data.get("USER_ID"));
		no_user.put("CONTENT", "升级入会信息提交成功，如果信息未完善请尽快完善，以免影响审核进度。如果已经完善，我们的工作人员会在24小时之内审核，请耐心等待!");
		no_user.put("CREATE_DATE", DateUtil.getTime());
		no_user.put("TABLE_NAME", "jl_upgrade");
		no_user.put("STATUS", "01"); 
		object =  dao.save("AppzUserMapper.saveUserMsg",no_user);//通知升级的用户  
		if(Integer.valueOf(object.toString()) >= 1){ 
			PageData res=new PageData();
			PageData complete =(PageData) dao.findForObject("AppzUserMapper.queryusxx",data);
			if(complete!=null){
				if(complete.get("PHONE")!=null&&!"".equals(complete.get("PHONE"))&&complete.get("COMPANY_NAME")!=null&&!"".equals(complete.get("COMPANY_NAME"))&&complete.get("CLID")!=null&&!"".equals(complete.get("CLID"))&&complete.get("HYID")!=null&&!"".equals(complete.get("HYID"))&&complete.get("IMG_PATH")!=null&&!"".equals(complete.get("IMG_PATH"))){
					res.put("isComplete",1);//信息已完善
				}else{
					res.put("isComplete",0);//信息未完善
				} 
			}else{
				res.put("isComplete",0);//信息未完善
			} 
			res.put("code","200");
			res.put("ORDER_ID",ORDER_ID);
			return JSONArray.toJSONString(res).toString();  
		}else{
			return ResultUtil.failMsg("生成升级入会信息失败！");
		}  
		}catch(Exception e){
			e.printStackTrace();
			return ResultUtil.failMsg("生成升级入会信息失败！");
		}
	}
	
	public void push(PageData user,String content){  
		String jsonStr = "{'type':'notice','title':'上海建联','content':'"+content+"'}";//透传内容
		TransmissionTemplate tmTemplate = PushUtil.transmissionTemplateDemo("上海建联",content,jsonStr);
		PushUtil pushApp=new PushUtil();
		String alias = user.getString("ID");
		try {
			pushApp.pushToSingle(tmTemplate,alias);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
}

 

