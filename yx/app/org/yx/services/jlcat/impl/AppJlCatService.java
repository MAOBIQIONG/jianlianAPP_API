package org.yx.services.jlcat.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;
import org.yx.services.jlcat.inter.AppJlCatServiceInter;
import org.yx.util.PushUtil;

import com.alibaba.fastjson.JSONArray;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.sun.istack.internal.logging.Logger;

@Service("appjlcatService")
public class AppJlCatService implements AppJlCatServiceInter{
	
	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	Logger log=Logger.getLogger(this.getClass());
	
	
	/**
	 * 查询会话
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryConversation(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		log.info("查询会话列表. service：【appjlcatService】 方法：【queryConversation】，参数为"+JSONArray.toJSONString(data).toString());
		if(!EmptyUtil.isNullOrEmpty(data)){
			/*查询当前会话*/
			List<PageData> pros =(List<PageData>) dao.findForList("ConversationMapper.findBycon",data);
			if(pros.size()> 0){
				//会话存在   
			}else{
				//会话不存在
				data.put("ID",UuidUtil.get32UUID());
				data.put("CREATE_DATE",new Date()); 
				Object res=dao.save("ConversationMapper.save",data);
				if(Integer.valueOf(res.toString()) >= 1){
					System.out.println("创建会话成功");  
				}else{
					System.out.println("创建会话失败");
					return null;
				}
			}
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("pageSize",pageSize);
			} 
			//查询消息
			List<PageData> messpros =(List<PageData>) dao.findForList("ConversationMapper.findBymess",data);
			PageData res=new PageData(); 
		    res.put("messpros",messpros);
			return JSONArray.toJSONString(messpros).toString(); 
		} 
		return null; 
	} 
	
	
	/**
	 * 创建会话
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addConversation(PageData pd) throws Exception{
		if(!EmptyUtil.isNullOrEmpty(pd)){ 
			PageData data = pd.getObject("params");  
			log.info("创建会话. service：【appjlcatService】 方法：【addConversation】，参数为"+JSONArray.toJSONString(data).toString());
			data.put("ID",UuidUtil.get32UUID());
			data.put("CREATE_DATE",new Date()); 
			Object res=dao.save("ConversationMapper.save",data);
			if(Integer.valueOf(res.toString()) >= 1){
				pd.put("code","200");
				return JSONArray.toJSONString(pd).toString();
			}else{
				return null;
			} 
		} 
		return null;  
	}
	
	/**
	 * 保存消息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addmessage(PageData pd) throws Exception{
		if(!EmptyUtil.isNullOrEmpty(pd)){ 
			String result = null;
			PageData data = pd.getObject("params"); 
			/*查询当前会话*/
			PageData pros =(PageData) dao.findForObject("ConversationMapper.findBycon",data);
			if( pros != null ){
				int scene = (Integer) pros.get("SCENE");
				if( scene == 1 ){
					result = addGroupMsg(data,pros);
				}else{
					result = addMsg(data,pros);
				}
			}else{
				result = addMsg(data,pros);
			}
			return result;
		} 
		return null;  
	}
	
	
	/**
	 * 保存消息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addMsg(PageData data,PageData pros) throws Exception{
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData pd =  new PageData(); 
			/*查询当前会话*/
			String CON_ID = null;
			String USER_ID = null;
			String JSID = null;
			if(pros != null){//会话存在
				//初始化全家参数
				CON_ID = pros.getString("ID");
				USER_ID = pros.getString("USER_ID");
				JSID = pros.getString("JSID");
				//修改会话
				PageData updata=new PageData();
				updata.put("ID", pros.getString("ID"));
				updata.put("USER_DEL", "01");
				updata.put("JS_DEL", "01"); 
				updata.put("time", data.getString("time"));
				updata.put("CONTENT", data.getString("CONTENT"));
				data.put("ID", pros.getString("ID"));
				//判断项目编号是否一样
				String PRO_ID = data.getString("PRO_ID");
				if(PRO_ID!=null && PRO_ID !="null" && !(PRO_ID).equals(pros.getString("PRO_ID"))){ 
					//项目编号不一样，获取最新的项目编号，修改会话项目编号
				    updata.put("PRO_ID", PRO_ID);
				}
				Object uphh=dao.update("ConversationMapper.upcon",updata);
				if(Integer.valueOf(uphh.toString()) >= 1){
					System.out.println("会话状态修改成功");  
				}else{
					System.out.println("会话状态修改失败"); 
				}  
			}else{//会话不存在
				//初始化全家参数
				CON_ID = UuidUtil.get32UUID();
				USER_ID = data.getString("USER_ID");
				JSID = data.getString("JSID");
				//新增会话
				data.put("ID",CON_ID);
				data.put("CREATE_DATE",data.getString("time")); 
				Object res=dao.save("ConversationMapper.save",data);
				if(Integer.valueOf(res.toString()) >= 1){
					System.out.println("创建会话成功");  
				}else{
					System.out.println("创建会话失败"); 
				}  
			}
			
			//修改会话接收人未读数量
			PageData upAcorjc=new PageData();
			upAcorjc.put("ID", CON_ID);
			if((USER_ID).equals(data.getString("FLAG_ID"))){
			    upAcorjc.put("JS_COUNT","JS_COUNT"); 
			}else if ((JSID).equals(data.getString("FLAG_ID"))) {
				upAcorjc.put("USER_COUNT", "USER_COUNT"); 
			}
			Object uphhu=dao.update("ConversationMapper.upcon",upAcorjc);
			if(Integer.valueOf(uphhu.toString()) >= 1){
				System.out.println("会话接收人未读数成功");  
			}else{
				System.out.println("会话接收人未读数失败"); 
			} 
			
			//修改用户总未读消息数量
			upAcorjc.put("UID",data.getString("JSID"));
			upAcorjc.put("UNREAD","UNREAD");
			Object upucon=dao.update("ConversationMapper.upAppucon",upAcorjc); 
			if(Integer.valueOf(upucon.toString()) >= 1){
				System.out.println("用户未读数量成功");  
			}else{
				System.out.println("用户未读数量失败"); 
			}
			
			//保存聊天记录
			data.put("ID",UuidUtil.get32UUID());
			data.put("CON_ID", CON_ID);
			data.put("CREATE_DATE",data.getString("time")); 
			log.info("保存消息. service：【appjlcatService】 方法：【addmessage】，参数为"+JSONArray.toJSONString(data).toString());
			Object messres=dao.save("ConversationMapper.savemess",data);
			if(Integer.valueOf(messres.toString()) >= 1){
				String content=data.getString("USER_NAME")+":"+data.getString("CONTENT");
		        //向会员管理中心经理推送审核消息
		        String jsonStr = "{'type':'chat','title':'上海建联','content':'"+content+"','ID':'"+data.getString("USER_ID")+"'}";//透传内容
				TransmissionTemplate tmTemplate = PushUtil.transmissionTemplateDemo("上海建联",content,jsonStr);
				PushUtil pushApp=new PushUtil();
				pushApp.pushToSingle(tmTemplate,data.getString("JSID")); 
				
				pd.put("code","200");
				return JSONArray.toJSONString(pd).toString();
			}else{
				pd.put("code","400");
				return JSONArray.toJSONString(pd).toString();
			}  
		} 
		return null;  
	}
	
	/**
	 * 保存群消息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String addGroupMsg(PageData data,PageData pros) throws Exception{
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData result = new PageData();
			/*根据JSID(群TID)查询所有会话id*/
			List<String> cons = (List<String>) dao.findForList("ConversationMapper.getConIds",data);
			for(int i=0;i<cons.size();i++){
			  if(cons.get(i).equals(pros.getString("ID")))
				 cons.remove(i);
			}
			
			//修改群用户未读记录及最新聊天内容
			PageData updata=new PageData();
			updata.put("ids", cons);
			updata.put("time", data.getString("time"));
			updata.put("CONTENT", data.getString("CONTENT"));
			Object uphh=dao.update("ConversationMapper.updConByIds",updata);
			log.info("保存消息. service：【appjlcatService】 方法：【updConByIds】，参数为"+uphh.toString());
			if(Integer.valueOf(uphh.toString()) >= 1){
				System.out.println("会话状态修改成功");
				
				//修改发送者聊天内容
				updata.put("ID", pros.getString("ID"));
				Object uphhu=dao.update("ConversationMapper.upcon",updata);
				log.info("保存消息. service：【appjlcatService】 方法：【upcon】，参数为"+uphhu.toString());
				
				/*查询当前会话对应群里所有用户ID*/
				List<String> users = (List<String>) dao.findForList("ConversationMapper.getAllFromGroup",data);
				for(int i=0;i<users.size();i++){
				  if(users.get(i).equals(data.getString("USER_ID")))
					 users.remove(i);
				}
				
				//修改接受人总未读数
				PageData apdata=new PageData();
				updata.put("ids", users);
				Object upucon=dao.update("ConversationMapper.updUnreadByIds",updata); 
				log.info("保存消息. service：【appjlcatService】 方法：【updUnreadByIds】，参数为"+upucon.toString());
				if(Integer.valueOf(upucon.toString()) >= 1){
					System.out.println("用户未读数量成功");  
				}else{
					System.out.println("用户未读数量失败"); 
				}
			}else{
				System.out.println("会话状态修改失败"); 
			}  
			
			//保存聊体记录
			log.info("保存消息. service：【appjlcatService】 方法：【addmessage】，参数为"+JSONArray.toJSONString(data).toString());
			data.put("ID",UuidUtil.get32UUID());
			data.put("CON_ID", pros.getString("ID"));
			data.put("CREATE_DATE",data.getString("time")); 
			Object messres=dao.save("ConversationMapper.savemess",data);
			if(Integer.valueOf(messres.toString()) >= 1){
				//发送消息后，推送通知栏消息
				String content=data.getString("USER_NAME")+":"+data.getString("CONTENT");
		        //向会员管理中心经理推送审核消息
		        String jsonStr = "{'type':'chat','title':'上海建联','content':'"+content+"','ID':'"+data.getString("USER_ID")+"','scene':1}";//透传内容
				TransmissionTemplate tmTemplate = PushUtil.transmissionTemplateDemo("上海建联",content,jsonStr);
				PushUtil pushApp=new PushUtil();
				//查询群内所有用户
				List<PageData> users = (List<PageData>) dao.findForList("ConversationMapper.getOtherUser",data);
				if( users != null ){
					String[] ids = new String[users.size()];
					for(int i=0,j=users.size();i<j;i++){
						if( !(data.getString("USER_ID")).equals(users.get(i).getString("USER_ID")) ){
							ids[i]=users.get(i).getString("USER_ID");
						}
					}
					//users.toArray(ids);
					if( ids.length > 9 ){
						pushApp.pushToMore(tmTemplate,ids); 
					}else if( ids.length > 0 ){
						pushApp.pushToMany(tmTemplate,ids); 
					}
				}
				
				result.put("code","200");
				return JSONArray.toJSONString(result).toString();
			}else{
				result.put("code","400");
				return JSONArray.toJSONString(result).toString();
			}  
		} 
		return null;  
	}
	
	
	/**
	 * 查询会话列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public String queryConverlist(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		log.info("查询会话列表. service：【appjlcatService】 方法：【queryConverlist】，参数为"+JSONArray.toJSONString(data).toString());
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("pageSize",pageSize);
			} 
			List<PageData> coms=null;
			try{
				coms =(List<PageData>) dao.findForList("ConversationMapper.queryConverlist",data);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(coms).toString();
		}
		return null;  
	}
	
	/**
	 * 查询消息列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public String querymesslist(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		log.info("查询消息列表. service：【appjlcatService】 方法：【querymesslist】，参数为"+JSONArray.toJSONString(data).toString());
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("pageSize",pageSize);
			} 
			List<PageData> coms=null;
			try{
				PageData hhdata=(PageData) dao.findForObject("ConversationMapper.findBycon", data);
				if( hhdata != null ){
					int scene = (Integer) hhdata.get("SCENE");
					if( scene == 1 ){
						data.put("JSID", hhdata.getString("JSID"));
						coms =(List<PageData>) dao.findForList("ConversationMapper.queryGroupMesslist",data);
					}else{
						data.put("CON_ID", hhdata.getString("ID"));
						coms =(List<PageData>) dao.findForList("ConversationMapper.queryMesslist",data);
					}
				}
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(coms).toString();
		}
		return null;  
	}
	
	/**
	 * 修改接收人状态
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String upJScount(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		PageData upnum=new PageData();
		log.info("修改接收人状态. service：【appjlcatService】 方法：【upJScount】，参数为"+JSONArray.toJSONString(data).toString());
		//查询会话
		PageData pros =(PageData) dao.findForObject("ConversationMapper.findBycon",data);
		//根据USERE_ID/jsid匹配更改那个用户的未读数
		if(!EmptyUtil.isNullOrEmpty(pros)){
			int ucount = 0;
			String USER_ID = data.getString("USER_ID");
			if((pros.getString("USER_ID")).equals(USER_ID)){
			   ucount = (Integer) pros.get("USER_UNREAD_COUNT");
			   data.put("USER_UNREAD_COUNT", 0); 
			}else{
			   ucount = (Integer) pros.get("JS_UNREAD_COUNT");
			   data.put("JS_UNREAD_COUNT", 0); 
			}
			data.put("ID", pros.getString("ID")); 
			//查询当前用户的未读数量
			PageData aucon =(PageData) dao.findForObject("ConversationMapper.queryAunum",data); 
			if(!EmptyUtil.isNullOrEmpty(aucon)){
				int aunum=(Integer) aucon.get("UNREAD");
				int AuSize=aunum - ucount;
				if(AuSize < 0){
					upnum.put("UNREAD", 0);
				}else {
					upnum.put("UNREAD", AuSize);
				}
				upnum.put("UID", USER_ID);
				Object aucons=this.dao.update("ConversationMapper.upAppucons", upnum);
				if(Integer.valueOf(aucons.toString()) >= 1){ 
					pd.put("unread", AuSize); 
				}else{
					pd.put("unread", -1);
				}
			} 
			Object messres=this.dao.update("ConversationMapper.upjscon", data);  
			if(Integer.valueOf(messres.toString()) >= 1){
				int scene = (Integer) pros.get("SCENE");
				PageData jsr = null;
				if( scene == 1 ){//群聊
					jsr =(PageData) dao.findForObject("ConversationMapper.queryGroupInfo",data);
					if( jsr != null ){
						pd.put("JSIMG", jsr.getString("ICON"));
						pd.put("REAL_NAME", jsr.getString("TNAME"));
					}
				}else{//单聊
					jsr =(PageData) dao.findForObject("ConversationMapper.queryJsInfo",data);
					if( jsr != null ){
						pd.put("JSIMG", jsr.getString("IMG"));
						String name = jsr.getString("REAL_NAME");
						int flag = (Integer) jsr.get("FLAG");
						if( flag == 1 ){
							name = name+"(上海建联)";
						}
						pd.put("REAL_NAME", name);
					}
				}
				pd.put("code","200");
				return JSONArray.toJSONString(pd).toString();
			}else{
				pd.put("code","400");
				return JSONArray.toJSONString(pd).toString();
			}  
		} 
		
		return null;  
	}
	
	
	
	/**
	 * 查询会话信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querybyconver(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		log.info("查询会话信息. service：【appjlcatService】 方法：【querybyconver】，参数为"+JSONArray.toJSONString(data).toString());
		if(!EmptyUtil.isNullOrEmpty(data)){
			/*查询当前会话*/ 
			List<PageData> pros =(List<PageData>) dao.findForList("ConversationMapper.findBycon",data); 
			return JSONArray.toJSONString(pros).toString(); 
		} 
		return null; 
	}
	
	/**
	 * 查询根据用户ID、项目ID查询用户是否加入群组
	 * @param pd｛"PRO_ID":""｝
	 * @return
	 * @throws Exception
	 */ 
	public String getGroupAndUserByPid(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		log.info("查询群信息. service：【appjlcatService】 方法：【getGroupAndUserByPid】，参数为"+JSONArray.toJSONString(data).toString());
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData result = new PageData();
			/*查询当前会话*/ 
			List<PageData> groups =(List<PageData>) dao.findForList("ConversationMapper.getGroupAndUserByPid",data); 
			if( groups.size() == 1 ){
				result.put("code", "200");
				result.put("msg", "查询成功！");
				result.put("group", groups.get(0));
			}else if( groups.size() == 0 ){
				result.put("code", "400");
				result.put("msg", "未加入该群组！");
			}else if( groups.size() == 0 ){
				result.put("code", "401");
				result.put("msg", "该群组中存在多个相同用户，请联系管理员！");
			}
			return JSONArray.toJSONString(result).toString(); 
		} 
		return null; 
	}
	/**
	 * 查询身份标识
	        普通用户：查秘书，查出会员经理
	        秘书：只查会员经理
	        会员经理：查所有的秘书
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryMissorjl(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		log.info("查询普通用户：查秘书，查出会员经理. service：【appjlcatService】 方法：【queryMissorjl】，参数为"+JSONArray.toJSONString(data).toString());
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("pageSize",pageSize);
			} 
			List<PageData> coms=null;
			List<PageData> msdata=null;
			try {
				//根据当前USER_ID判断身份(查出分配的秘书编号MANAGER_ID)
				PageData SFdata=(PageData) dao.findForObject("ConversationMapper.getUserBySF", data);
				//根据秘书编号查出秘书在app上的ID、REAL_NAME、等级、公司名称、
				if(SFdata!=null){ 
					//FLAG（0普通用户，1官方用户）
					int flag = (Integer) SFdata.get("FLAG");
					if(flag==1){
						//服务类型（00：无，10：项目经理，20：会员经理 30：城市合伙人经理,21：秘书）
						if("20".equals(SFdata.getString("SERVICE_TYPE"))){
							//查出所有的秘书
							data.put("SERVICE_TYPE", 21);
							coms=(List<PageData>) dao.findForList("ConversationMapper.getUserBySYU",data);
						}else if ("21".equals(SFdata.getString("SERVICE_TYPE"))) {
							//查所有的会员经理
							data.put("SERVICE_TYPE", 20);
							coms=(List<PageData>) dao.findForList("ConversationMapper.getUserBySYU",data);
						}
					}else{
						//普通用户：查分配的秘书，查出会员经理
						PageData jsdata=new PageData();
						jsdata.put("MANAGER_ID", SFdata.getString("MANAGER_ID"));
						//根据分配的秘书编号查出秘书信息
						msdata=(List<PageData>) dao.findForList("ConversationMapper.getUserByMS", jsdata);
						data.put("SERVICE_TYPE", 20);
						coms=(List<PageData>) dao.findForList("ConversationMapper.getUserBySYU",data);
					}
				}else{
					return null;
				}
				 PageData res=new PageData();
				 res.put("msdata", msdata);
				 res.put("coms", coms); 
				return JSONArray.toJSONString(res).toString();
			} catch (Exception e) {
				return null; 
			} 
		}
		return null;  
	}
	
	
	/**
	 * 如果是秘书：查所分配的用户
		如果会员经理，查询所有用户
	 * 
	 */
	public String queryUseorjl(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		log.info("如果是秘书：查所分配的用户,如果项目经理，查询所有用户. service：【appjlcatService】 方法：【queryUseorjl】，参数为"+JSONArray.toJSONString(data).toString());
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("pageSize",pageSize);
			} 
			List<PageData> coms=null;
			try {
				//根据当前USER_ID判断身份(查出分配的秘书编号MANAGER_ID)
				PageData SFdata=(PageData) dao.findForObject("ConversationMapper.getUserBySF", data);
				if(SFdata!=null){
					//FLAG（0普通用户，1官方用户）
					int flag = (Integer) SFdata.get("FLAG");
					if(flag==1){
						//服务类型（00：无，10：项目经理，20：会员经理 30：城市合伙人经理,21：秘书）
						if("21".equals(SFdata.getString("SERVICE_TYPE"))){
							//查所分配的用户 
							data.put("MANAGER_ID",SFdata.get("MANAGER_ID"));
							coms=(List<PageData>) dao.findForList("ConversationMapper.getMsbyUser",data);
						}else if ("20".equals(SFdata.getString("SERVICE_TYPE"))) {
							//查询所有用户
							coms=(List<PageData>) dao.findForList("ConversationMapper.getMsbyUser",data);
						}
					}
					
				}else{
					System.out.println("未查到分配的秘书信息");
				}  
				return JSONArray.toJSONString(coms).toString(); 
			} catch (Exception e) {
				return null;
			} 
		}
		return null;  
	}
	
	
	/**
	 * 会员经理：搜任何人都可以
	 * 秘书：搜索分配的用户
	 * 
	 */
	public String queryCatSearch(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		log.info("会员经理：搜任何人都可以,秘书：搜索分配的用户. service：【appjlcatService】 方法：【queryCatSearch】，参数为"+JSONArray.toJSONString(data).toString());
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("pageSize",pageSize);
			} 
			List<PageData> coms=null;
			try {
				//根据当前USER_ID判断身份(查出分配的秘书编号MANAGER_ID)
				PageData SFdata=(PageData) dao.findForObject("ConversationMapper.getUserBySF", data);
				if(SFdata!=null){ 
					//FLAG（0普通用户，1官方用户）
					int flag=(Integer) SFdata.get("FLAG");
					if(flag==1){
						//服务类型（00：无，10：项目经理，20：会员经理 30：城市合伙人经理,21：秘书）
						if("20".equals(SFdata.getString("SERVICE_TYPE"))){
							//搜任何人都可以 
							coms=(List<PageData>) dao.findForList("ConversationMapper.getMsbyUserName",data); 
						}else if ("21".equals(SFdata.getString("SERVICE_TYPE"))) {
							//搜索分配的用户 
							data.put("FLAG", 1);
							data.put("MANAGER_ID", data.getString("USER_ID"));
							coms=(List<PageData>) dao.findForList("ConversationMapper.getMsbyUserName",data); 
						}
					}
				}else{
					System.out.println("未查到当前的身份信息");
				}  
				return JSONArray.toJSONString(coms).toString();
			}catch (Exception e){
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 会员经理推送所有人消息
	 * 
	 */
	public String addPushs(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		log.info("会员经理推送所有人消息. service：【appjlcatService】 方法：【addPushs】，参数为"+JSONArray.toJSONString(data).toString());
		if(!EmptyUtil.isNullOrEmpty(data)){
			try {
				String title = data.getString("Title");
				String text = data.getString("Text");
				//消息推送
				String jsonStr = "{'type':'notice','title':'"+title+"','content':'"+text+"'}";//透传内容
				TransmissionTemplate tmTemplates = PushUtil.transmissionTemplateDemo(title,text,jsonStr);
				PushUtil pushApp=new PushUtil(); 
				String result=pushApp.pushToAll(tmTemplates);
				System.out.println("向全部用户推送："+result.toString());
				//存储消息内容
				PageData notif=new PageData();
				notif.put("ID", UuidUtil.get32UUID());
			    notif.put("USER_ID", "jianlian");//默认为系统通知消息
		 		notif.put("CREATE_DATE", new Date());
		 		notif.put("TABLE_NAME","");
		 		notif.put("OBJECT_ID","");
		 		notif.put("STATUS","01"); 
		 		notif.put("CONTENT",text);
			    Object val = dao.findForObject("ConversationMapper.Pushsave",notif);
			    if(Integer.valueOf(val.toString()) >= 1){
			    	PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();
			    }else{
			    	return null;
			    }
			} catch (Exception e) { 
			} 
		}
		return null;
	}
}
