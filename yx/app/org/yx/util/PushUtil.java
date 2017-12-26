package org.yx.util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;  

import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate; 
import com.gexin.rp.sdk.template.TransmissionTemplate; 

public class PushUtil { 
	//定义常量, appId、appKey、masterSecret 采用本文档 "第二步 获取访问凭证 "中获得的应用配置
    private static String appId = "f3VtMt3APs6DhHWZhk2if9";
    private static String appKey = "MV6xcaqkMX8HhGioGjZyS9";
    private static String masterSecret = "V7EkFMB3Ij5IucuZeDwMz8";
    private static String url = "http://sdk.open.api.igexin.com/apiex.htm"; 
    
    //对所有用户进行推送
    public String pushToAll(TransmissionTemplate tmtemplate) { 
    	IGtPush push = new IGtPush(url, appKey, masterSecret); 
    	
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);
        
        //透传模板
        SingleMessage tmmessage = SingleMessageDemo();
        tmmessage.setData(tmtemplate);
        
        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
        AppMessage message = new AppMessage();
        message.setData(tmtemplate);
        message.setAppIdList(appIds);
        message.setOffline(true);//设置消息离线储存
        message.setOfflineExpireTime(1000 * 600);
        
        IPushResult ret = push.pushMessageToApp(message);
        System.out.println(ret.getResponse().toString());
        String result=ret.getResponse().get("result").toString();
		return result;
	} 

    //对单个用户进行推送
    public String pushToSingle(TransmissionTemplate tmtemplate,String alias) throws Exception {
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        //透传模板
        SingleMessage tmmessage = SingleMessageDemo();
        tmmessage.setData(tmtemplate);
        
        IPushResult ret = null; 
    	Target target = new Target();
        target.setAppId(appId);
        target.setAlias(alias);
        try {
        	ret = push.pushMessageToSingle(tmmessage, target);
        } catch (RequestException e) {
            e.printStackTrace();
        }
        
        String result="";
        if (ret != null) {
           System.out.println(ret.getResponse().toString());
           result=ret.getResponse().get("result").toString();
        } else {
            System.out.println("服务器响应异常");
        }
        return result;
    }  
    
    //对少量用户进行推送
    public String pushToMany(TransmissionTemplate tmtemplate,String[] cids) throws Exception {
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        //点击消息打开应用
        SingleMessage message = SingleMessageDemo();
        message.setData(tmtemplate);
        
        IPushResult ret = null; 
        for(int i=0;i<cids.length;i++){
        	 Target target = new Target();
             target.setAppId(appId); 
             target.setAlias(cids[i]);
             try {
            	 ret = push.pushMessageToSingle(message, target);
             } catch (RequestException e) {
                 e.printStackTrace();
             }
        }  
        String result="";
        if (ret != null) {
           System.out.println(ret.getResponse().toString());
           result=ret.getResponse().get("result").toString();
        } else {
            System.out.println("服务器响应异常");
        }
        return result;
    }  
    
    //对少量用户进行推送
    public String pushToIosMany(TransmissionTemplate tmtemplate,String[] cids) throws Exception {
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        SingleMessage tmmessage = SingleMessageDemo();
        tmmessage.setData(tmtemplate);
        
        IPushResult ret = null; 
        for(int i=0;i<cids.length;i++){
        	 Target target = new Target();
             target.setAppId(appId);
             target.setAlias(cids[i]);
             try {
            	 ret = push.pushMessageToSingle(tmmessage, target);
             } catch (RequestException e) {
                 e.printStackTrace();
             }
        }  
        String result="";
        if (ret != null) {
           System.out.println(ret.getResponse().toString());
           result=ret.getResponse().get("result").toString();
        } else {
            System.out.println("服务器响应异常");
        }
        return result;
    }  
    
    //批量推送 
    public String pushToMore(TransmissionTemplate tmtemplate,String[] cids) throws Exception {
    	IGtPush push = new IGtPush(url, appKey, masterSecret);
        
        //透传消息
        ListMessage tmmessage = ListMessageDemo();
        tmmessage.setData(tmtemplate);
        // 配置推送目标
        List<Target> targets = new ArrayList<Target>();
        for(int i=0;i<cids.length;i++){
        	Target target = new Target();
        	target.setAppId(appId);
        	//target.setClientId(cids[i]);
            target.setAlias(cids[i]);
            targets.add(target);
        }  
        String tmtaskId = push.getContentId(tmmessage);
        IPushResult tmret = push.pushMessageToList(tmtaskId, targets);
        System.out.println("-------------pushToMore华丽的分割线--------------");
        System.out.println(tmret.getResponse().toString());
        String result = tmret.getResponse().get("result").toString();
        return result;
    }
    
    //批量推送 
    public String pushToIosMore(TransmissionTemplate tmtemplate,String[] cids) throws Exception {
    	IGtPush push = new IGtPush(url, appKey, masterSecret);
    	
        //透传消息
        ListMessage tmmessage = ListMessageDemo();
        tmmessage.setData(tmtemplate);
        // 配置推送目标
        List<Target> targets = new ArrayList<Target>();
        for(int i=0;i<cids.length;i++){
        	Target target = new Target();
        	target.setAppId(appId);
            target.setAlias(cids[i]);
            targets.add(target);
        }  
        String tmtaskId = push.getContentId(tmmessage);
        IPushResult tmret = push.pushMessageToList(tmtaskId, targets);
        System.out.println("-------------pushToIosMore华丽的分割线--------------");
        System.out.println(tmret.getResponse().toString());
        String result = tmret.getResponse().get("result").toString();
        return result;
    }
    
    //消息体：SingleMessage
    public static SingleMessage SingleMessageDemo() {
    	SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        return message;
    }
    
    //消息体：SingleMessage
    public static ListMessage ListMessageDemo() {
    	ListMessage message = new ListMessage();
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        return message;
    }

    //消息模板：点击通知打开网页
    public static LinkTemplate linkTemplateDemo() {
        LinkTemplate template = new LinkTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 设置通知栏标题与内容
        template.setTitle("请输入通知栏标题");
        template.setText("请输入通知栏内容");
        // 配置通知栏图标
        template.setLogo("icon.png");
        // 配置通知栏网络图标，填写图标URL地址
        template.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        template.setIsRing(true);
        template.setIsVibrate(true);
        template.setIsClearable(true);
        // 设置打开的网址地址
        template.setUrl("http://www.baidu.com");
        return template;
    }

    //消息模板：点击通知启动应用
    public static NotificationTemplate notificationTemplateDemo() {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 设置通知栏标题与内容
        template.setTitle("请输入通知栏标题");
        template.setText("请输入通知栏内容");
        // 配置通知栏图标
        template.setLogo("icon.png");
        // 配置通知栏网络图标
        template.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        template.setIsRing(true);
        template.setIsVibrate(true);
        template.setIsClearable(true);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(1);
        template.setTransmissionContent("");
        return template;
    }
    
  //消息模板：透传消息
    public static TransmissionTemplate transmissionTemplateDemo(String title,String text,String jsonStr) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent(jsonStr);//payload、content内容
        
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setBadge(0);
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory("$由客户端定义");
        payload.addCustomMsg("payloadJson", jsonStr);
        //字典模式使用下者
        payload.setAlertMsg(getDictionaryAlertMsg(title,text));
        template.setAPNInfo(payload);
        return template;
    }
    
    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(String title,String text){
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(text);
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.setLocKey("setLocKey");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image");
        // iOS8.2以上版本支持
        alertMsg.setTitle(title);
        alertMsg.setTitleLocKey("TitleLocKey");
        alertMsg.addTitleLocArg("TitleLocArg");
        return alertMsg;
    }

    
    //解除别名
    public boolean cleanAlias(String alias,String cid) throws Exception { 
        IGtPush push = new IGtPush(url, appKey, masterSecret); 
        IAliasResult queryAlias = push.unBindAlias(appId, alias, cid);
        System.out.println("解绑结果："+queryAlias.getResult()+";解绑错误信息："+queryAlias.getErrorMsg());
        return queryAlias.getResult();
    }

    //ios推送  
    public static void pushAPN(APNPayload.DictionaryAlertMsg alertMsg  
            , final List<String> deviceTokens, String content){  
                IGtPush push = new IGtPush(url, appKey, masterSecret);  
  
                APNTemplate t = new APNTemplate();  
                APNPayload apnpayload = new APNPayload();  
                apnpayload.setSound("");  
                apnpayload.setAlertMsg(alertMsg);  
              //传送的附加信息，用于解析  
                apnpayload.addCustomMsg("info",content);  
                t.setAPNInfo(apnpayload);  
                ListMessage message = new ListMessage();  
                message.setData(t);  
                
                String contentId = push.getAPNContentId(appId, message);
                IPushResult ret = push.pushAPNMessageToList(appId, contentId, deviceTokens);  
                System.out.println(ret.getResponse());  
    }  
    
    public static void pushAPNToSimple(APNPayload.DictionaryAlertMsg alertMsg  
            , String deviceToken){  
                IGtPush push = new IGtPush(url, appKey, masterSecret);  
  
                APNTemplate t = new APNTemplate();  
                APNPayload apnpayload = new APNPayload();  
                apnpayload.setSound("");  
                apnpayload.setAlertMsg(alertMsg);  
              //传送的附加信息，用于解析  
                apnpayload.addCustomMsg("info","123");  
                t.setAPNInfo(apnpayload);  
                SingleMessage message = new SingleMessage(); 
                message.setData(t);  
                
                IPushResult ret = push.pushAPNMessageToSingle(appId, deviceToken, message);
                System.out.println(ret.getResponse());  
    }
    
    public static void main(String[] args) throws IOException {
    	APNPayload.DictionaryAlertMsg alertMsg = getDictionaryAlertMsg("haha","textcontnt");
    	
    	String cid = "7c1688608405a4482a3ac0664fcfa8bd";
    	String token = "1AF9220B9F935D0D4138541C23AF3B73A6C6FCF5D951BF581FFDC37CFB19A42C";
//    	List<String> deviceTokens = new ArrayList<String>();
//    	deviceTokens.add(token);
//    	pushAPN(alertMsg, deviceTokens, "测试哦");
    	
    	pushAPNToSimple(alertMsg, token);
//    	restoreCidList(cid);
    }
}

