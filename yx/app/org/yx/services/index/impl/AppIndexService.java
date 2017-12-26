package org.yx.services.index.impl; 
import java.util.List;  

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil; 
import org.yx.services.index.inter.AppIndexServiceInter; 

import com.alibaba.fastjson.JSONArray;  

@Service("appIndexService")
public class AppIndexService implements AppIndexServiceInter{ 
	
	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	
	/**
	 * 根据项目名称、活动名称、资讯名称搜索
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProAndActAndMed(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			List<PageData> prolist=(List<PageData>) this.dao.findForList("AppProjectMapper.queryProByTitle", data);
			for(int i=0;i<prolist.size();i++){
				String param=prolist.get(i).getString("PROJECT_NAME");
				param = param.replace(data.getString("TITLE"),"<font color=\"red\">"+data.getString("TITLE")+"</font>");//关键字样式   
				prolist.get(i).put("PROJECT_NAME",param);
			}
			List<PageData> madlist=(List<PageData>) this.dao.findForList("AppMediaMapper.queryMedByTitle", data);
			for(int i=0;i<madlist.size();i++){
				String param=madlist.get(i).getString("TITLE");
				param = param.replace(data.getString("TITLE"),"<font color=\"red\">"+data.getString("TITLE")+"</font>");//关键字样式   
				madlist.get(i).put("TITLE",param);
			}
//			List<PageData> actList=(List<PageData>) this.dao.findForList("AppActivityMapper.queryActByName", data);
//			for(int i=0;i<actList.size();i++){
//				String param=actList.get(i).getString("ACT_NAME");
//				param = param.replace(data.getString("TITLE"),"<font color=\"red\">"+data.getString("TITLE")+"</font>");//关键字样式   
//				actList.get(i).put("ACT_NAME",param);
//			}
			PageData res=new PageData(); 
			res.put("prolist",prolist);
			res.put("madlist",madlist);
//			res.put("actList",actList);
			res.put("keyWords",data.getString("TITLE")); 
			return JSONArray.toJSONString(res).toString(); 
		} 
		return null; 
	} 
	
	/**
	 * 查询首页所有信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryIndexInfo(PageData pd) throws Exception{ 
		 if(!EmptyUtil.isNullOrEmpty(pd)){ 
			PageData pa =new PageData();
			pa.put("LOCATION_NO","01");
			List<PageData> firRots=(List<PageData>) dao.findForList("AppRotationMapper.queryByColno",pa);//查询首页轮换大图
			pa.put("LOCATION_NO","08");
			List<PageData> secRots=(List<PageData>) dao.findForList("AppRotationMapper.queryByColno",pa);//查询首页中部轮换大图
			List<PageData> hotMadia=(List<PageData>) this.dao.findForList("AppMediaMapper.queryHot",null);
			List<PageData> hotList=(List<PageData>) this.dao.findForList("AppProjectMapper.queryHotProject",null);//查询热门项目
			PageData bidder=(PageData) this.dao.findForObject("AppProjectMapper.queryNewBidder", null);//查询最新成交项目 
			pa.put("start", 0);
			pa.put("startPage", 0);
			pa.put("pageSize",8);
			pa.put("DISTINGUISH", 1);
			List<PageData> pros =(List<PageData>) dao.findForList("AppProjectMapper.queryProIndexByParam",pa);
			for(PageData pro:pros){
				if("1".equals(pro.getString("IS_RESOURCE"))){
					 List<PageData> imgs =(List<PageData>) this.dao.findForList("AppXmtpMapper.queryImgsByPid",pro); //查询项目的图片
					 pro.put("imgs",imgs);
				}
			} 
			PageData res=new PageData(); 
		    res.put("firRots",firRots);
		    res.put("hotMadia",hotMadia);
		    res.put("secRots",secRots);
		    res.put("hotList",hotList);
		    res.put("bidder",bidder);
		    res.put("pros",pros);
		    return JSONArray.toJSONString(res).toString(); 
		 }
		 return null;  
	} 
	
	/**
	 * 查询是否有消息通知信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryHasMessage(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			List<PageData> mess=null;
			if(data.getString("USER_ID")!=null&&!"".equals(data.getString("USER_ID"))){
				mess=(List<PageData>) dao.findForList("SysNotificationMapper.queryNoRead",data);//查询是否有消息通知信息
			}else{
				mess=(List<PageData>) dao.findForList("SysNotificationMapper.queryNoReadCount",data);//查询是否有消息通知信息
			} 
			PageData res=new PageData();  
			if(mess!=null&&mess.size()>0){
				res.put("hasMessage","1");//有消息通知
			}else{
				res.put("hasMessage","0");//没有消息通知
			}
			return JSONArray.toJSONString(res).toString(); 
		} 
		return null; 
	} 
} 
	
	
 

