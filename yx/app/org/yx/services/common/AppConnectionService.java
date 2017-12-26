package org.yx.services.common;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;

import com.alibaba.fastjson.JSONArray;

@Service("appConnService")
public class AppConnectionService {


	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	Logger log=Logger.getLogger(this.getClass());//日志信息
	
	/**
	 * 关注人脉/取消关注
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addConnect(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			List<PageData> isConn =(List<PageData>) this.dao.findForList("AppConnectionMapper.checkConnected",data);//判断是否已经关注
			if(isConn.size()==0){//未关注
				String id=UuidUtil.get32UUID();
				data.put("ID", id);
				data.put("DATE", new Date());   
				Object obj=dao.save("AppConnectionMapper.addconcern",data);
				if(Integer.valueOf(obj.toString()) >= 1){ 
					log.info("function  addConnect is success!");
 					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();  
				}else{
					log.info("function  addConnect is fail!");
					return null;
				} 
			}else{//已经关注，取消关注
				 Object obj =dao.delete("AppConnectionMapper.delconcern",data);
	 		     if(Integer.valueOf(obj.toString()) >= 1){ 
	 		    	log.info("function  addConnect is success!");
 					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();  
	 			 }else{
	 				log.info("function  addConnect is fail!");
	 				return null;  
	 			 } 
			} 
		}
		log.info("function  addConnect is fail!");
		return null;  
	} 
}
