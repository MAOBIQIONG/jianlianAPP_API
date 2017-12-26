package org.yx.services.jy.impl;

import io.rong.RongCloud;
import io.rong.models.CodeSuccessResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date; 
import java.util.List; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData;
import org.yx.common.utils.AppUtil;
import org.yx.common.utils.EmptyUtil; 
import org.yx.common.utils.UuidUtil;
import org.yx.services.jy.inter.AppProjectServiceInter;

import com.alibaba.fastjson.JSONArray;  

@Service("appRongService")
public class AppRongService {
	String imAppKey = "vnroth0kvfo6o";//替换成您的appkey
	String appSecret = "fEPFfFSclDYo";//替换成匹配上面key的secret
	RongCloud rongCloud = RongCloud.getInstance(imAppKey, appSecret);

	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	
	
	/**
	 * 加入/取消加入项目讨论小组 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editXz(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData isCy =(PageData) this.dao.findForObject("AppXzcyMapper.chenckIsIn",data);//判断是否已经加入
			PageData pro =(PageData) this.dao.findForObject("AppProjectMapper.queryByProId",data); 
			if(isCy==null){//没有加入
				String groupId = data.getString("PRO_ID"); 
				String groupName = data.getString("GROUP_NAME"); 
				if(groupName==null||"".equals(groupName)){
					String[] groupCreateUserId = {pro.getString("PROJECT_MANAGER_ID")};
	    	 		CodeSuccessResult groupCreateResult = rongCloud.group.create(groupCreateUserId, pro.getString("ID"), pro.getString("PROJECT_NAME"));
	    	 		System.out.println("***************创建项目讨论群****************");
	    	 		System.out.println("创建项目讨论群组:  " + groupCreateResult.toString());
	    	 		if(groupCreateResult.getCode()==200){
	    	 			PageData xz=new PageData(); 
	    	 			xz.put("ID",UuidUtil.get32UUID());
	    	 			xz.put("PRO_ID", pro.getString("ID"));
	    	 			xz.put("USER_ID",pro.getString("PROJECT_MANAGER_ID"));
	    	 			xz.put("DATE", new Date());
	    	 			xz.put("IS_ADMIN","1");
	    	 			xz.put("IS_APPUSER","0"); 
	    	 			Object obj=dao.save("AppXzcyMapper.addXz",xz); 
	    	 			if(Integer.valueOf(obj.toString()) >= 1){ 
	    	 				//修改项目群名称
	    	 				xz.put("GROUP_NAME",pro.getString("PROJECT_NAME"));
		    	 			dao.update("AppProjectMapper.updateGroupName",xz);
			 			}
	    	 		} 
				}
				String USER_ID = data.getString("USER_ID"); 
				String[] groupJoinUserId = {USER_ID};
				CodeSuccessResult groupJoinResult = rongCloud.group.join(groupJoinUserId, groupId, pro.getString("PROJECT_NAME"));
				System.out.println("**********************************");
				System.out.println("join:  " + groupJoinResult.toString());
				if( groupJoinResult.getCode() == 200 ){
					String id=UuidUtil.get32UUID();
					data.put("ID", id);
					data.put("DATE", new Date()); 
					Object obj=dao.save("AppXzcyMapper.addXz",data);
					if(Integer.valueOf(obj.toString()) >= 1){ 
	 					PageData res=new PageData();
						res.put("code","200");
						return JSONArray.toJSONString(res).toString();   
					}else{
						return null;
					} 
				}
			}else{//已经加入，退出讨论小组
				String groupId = data.getString("PRO_ID"); 
				String USER_ID = data.getString("USER_ID"); 
				String[] groupQuitUserId = {USER_ID};
				CodeSuccessResult groupQuitResult = rongCloud.group.quit(groupQuitUserId, groupId);
				System.out.println("**********************************");
				System.out.println("quit:  " + groupQuitResult.toString());
				if( groupQuitResult.getCode() == 200 ){
					Object obj =dao.delete("AppXzcyMapper.deleteXz",data);
		 		    if(Integer.valueOf(obj.toString()) >= 1){ 
		 		    	//修改项目群名称
		 		    	PageData xz=new PageData(); 
	    	 			xz.put("PRO_ID", pro.getString("ID"));
	    	 			xz.put("GROUP_NAME","");
		    	 		dao.update("AppProjectMapper.updateGroupName",xz);
	    	 			
	 					PageData res=new PageData();
						res.put("code","200");
						return JSONArray.toJSONString(res).toString();   
		 			}else{
		 				return null;  
		 			}
				}
			} 
		}
		return null;   
	} 
}
