package org.yx.controller;

import io.rong.RongCloud;
import io.rong.models.CodeSuccessResult;
import io.rong.models.GroupInfo;
import io.rong.models.GroupUserQueryResult;

import java.util.List; 

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.entity.PageData;
import org.yx.common.utils.AppUtil;  

@Controller
@RequestMapping({ "/appRoog" })
public class AppRoogController extends AppController{
	String imAppKey = "vnroth0kvfo6o";//替换成您的appkey
	String appSecret = "fEPFfFSclDYo";//替换成匹配上面key的secret
	RongCloud rongCloud = RongCloud.getInstance(imAppKey, appSecret); 
	 
	/**
	 * 同步用户所属群组方法（当第一次连接融云服务器时，需要向融云服务器提交 userId 对应的用户当前所加入的所有群组，此接口主要为防止应用中用户群信息同融云已知的用户所属群信息不同步。）
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/groupSync" })
	@ResponseBody
	public PageData groupSync() throws Exception {
		PageData _restult = new PageData(); 
		PageData pa = getPageData().getObject("pa");  
		String userId=pa.getString("userId");  
		GroupInfo[] groupSyncGroupInfo = {};
		List<PageData> groupArr = pa.getList("groupArr"); 
		for( PageData group : groupArr ){
			
		}
		//new GroupInfo("groupId1","groupName1" ), new GroupInfo("groupId2","groupName2" ), new GroupInfo("groupId3","groupName3" )
		
		CodeSuccessResult groupSyncResult = rongCloud.group.sync(userId, groupSyncGroupInfo);
		System.out.println("sync:  " + groupSyncResult.toString());
		if( groupSyncResult.getCode() == 200 ){
			_restult = AppUtil.ss(null, "200", "成功", "true", null);
		} else {
			_restult = AppUtil.ss(null, "00", groupSyncResult.getErrorMessage(), "true", null);
		}
		return _restult;
	}

	/**
	 * 将用户加入指定群组，用户将可以收到该群的消息，同一用户最多可加入 500 个群，每个群最大至 3000 人。 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/groupJoin" })
	@ResponseBody
	public PageData groupJoin() throws Exception {
		PageData _restult = new PageData(); 
		PageData pa = getPageData().getObject("pa");  
		String groupId = pa.getString("groupId"); 
		String groupName = pa.getString("groupName"); 
		String joinUser = pa.getString("joinUser"); 
		String[] groupJoinUserId = joinUser.split(",");
		CodeSuccessResult groupJoinResult = rongCloud.group.join(groupJoinUserId, groupId, groupName);
		System.out.println("join:  " + groupJoinResult.toString());
		if( groupJoinResult.getCode() == 200 ){
			_restult = AppUtil.ss(null, "200", "成功", "true", null);
		} else {
			_restult = AppUtil.ss(null, "00", groupJoinResult.getErrorMessage(), "true", null);
		}
		return _restult;
	}
	
	/**
	 * 退出群组方法（将用户从群中移除，不再接收该群组的消息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/groupQuit" })
	@ResponseBody
	public PageData groupQuit() throws Exception {
		PageData _restult = new PageData(); 
		PageData pa = getPageData().getObject("pa");  
		String groupId=pa.getString("groupId"); 
		String quitUser = pa.getString("quitUser"); 
		String[] groupQuitUserId = quitUser.split(",");
		CodeSuccessResult groupQuitResult = rongCloud.group.quit(groupQuitUserId, groupId);
		System.out.println("quit:  " + groupQuitResult.toString());
		if( groupQuitResult.getCode() == 200 ){
			_restult = AppUtil.ss(null, "200", "成功", "true", null);
		} else {
			_restult = AppUtil.ss(null, "00", groupQuitResult.getErrorMessage(), "true", null);
		}
		return _restult;
	}
	
	/**
	 * 查询群成员方法 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/groupQueryUser" })
	@ResponseBody
	public PageData groupQueryUser() throws Exception {
		PageData _restult = new PageData(); 
		PageData pa = getPageData().getObject("pa");  
		String groupId=pa.getString("groupId"); 
		GroupUserQueryResult groupQueryUserResult = rongCloud.group.queryUser(groupId);
		System.out.println("queryUser:  " + groupQueryUserResult.toString());
		if( groupQueryUserResult.getCode() == 200 ){
			_restult = AppUtil.ss(groupQueryUserResult.getUsers(), "200", "成功", "true", null);
		} else {
			_restult = AppUtil.ss(null, "00","失败", "true", null);
		}
		return _restult;
	}
	
	public static void main(String[] args) {
		
	}
}
