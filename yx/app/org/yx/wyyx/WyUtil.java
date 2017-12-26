package org.yx.wyyx;

import net.sf.json.JSONArray;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.yx.common.entity.PageData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class WyUtil {
	public static String appKey = "47045ca57e2ca57f805cb24563e34160";
	public static String appSecret = "818942ff3fac";
	
	/**
	 * 注册网易云信账户
	 * id:用户ID
	 * name:用户REAL_NAME
	 * path:用户IMG
	 * */
    public static String createWyAccount(String id,String name,String path) throws Exception{
        String url = "https://api.netease.im/nimserver/user/create.action";
        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", id));
        nvps.add(new BasicNameValuePair("name", name));
        nvps.add(new BasicNameValuePair("icon", path));
        String result = POST(url,nvps);
        System.out.println("创建网易账号:"+result);
        return result;
    }
    
    /**
     * 更新并获取新token
	 * id:用户ID
	 * */
    public static String refreshToken(String id) throws Exception{
        String url = "https://api.netease.im/nimserver/user/refreshToken.action";
        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", id));
        String result = POST(url,nvps);
        System.out.println("更新并获取新TOKEN:"+result);
        return result;
    }
    
    /**
     *  发送通知消息
	 *  from	String	是	发送者accid，用户帐号，最大32字符，APP内唯一
		msgtype	int	是	0：点对点自定义通知，1：群消息自定义通知，其他返回414
		to	String	是	msgtype==0是表示accid即用户id，msgtype==1表示tid即群id
		attach	String	是	自定义通知内容，第三方组装的字符串，建议是JSON串，最大长度4096字符
	 * */
    public static String sendAttachMsg(PageData pd) throws Exception{
    	String from = "f1e3cfff9686475aaa8383e959bf8f9c";//默认超级管理员
    	String msgtype = "1";//默认群通知
    	String to = "204877827";//默认管理员群组
        String url = "https://api.netease.im/nimserver/msg/sendAttachMsg.action";
        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("from", from));
        nvps.add(new BasicNameValuePair("msgtype", msgtype));
        nvps.add(new BasicNameValuePair("to", to));
        nvps.add(new BasicNameValuePair("attach", pd.getString("attach")));
        String result = POST(url,nvps);
        System.out.println("发送通知消息:"+result);
        return result;
    }
    
    /**
     *  发送普通消息
	 *  from	String	是	发送者accid，用户帐号，最大32字符，
		必须保证一个APP内唯一
		ope	int	是	0：点对点个人消息，1：群消息（高级群），其他返回414
		to	String	是	ope==0是表示accid即用户id，ope==1表示tid即群id
		type	int	是	0 表示文本消息,
		1 表示图片，
		2 表示语音，
		3 表示视频，
		4 表示地理位置信息，
		6 表示文件，
		100 自定义消息类型（特别注意，对于未对接易盾反垃圾功能的应用，该类型的消息不会提交反垃圾系统检测）
		body	String	是	请参考下方消息示例说明中对应消息的body字段，
		最大长度5000字符，为一个JSON串
	 * */
    public static String sendMsg(PageData pd) throws Exception{
    	String from = "f1e3cfff9686475aaa8383e959bf8f9c";//默认超级管理员
    	String ope = "1";//默认群消息
    	String to = "204877827";//默认管理员群组
    	String type = "0";//默认文本消息
        String url = "https://api.netease.im/nimserver/msg/sendMsg.action";
        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("from", from));
        nvps.add(new BasicNameValuePair("ope", ope));
        nvps.add(new BasicNameValuePair("to", to));
        nvps.add(new BasicNameValuePair("type", type));
        nvps.add(new BasicNameValuePair("body", pd.getString("body")));
        String result = POST(url,nvps);
        System.out.println("发送消息:"+result);
        return result;
    }
    
    /**
     * POST请求
	 * url:网易云信接口url
	 * nvps：网易云信接口参数
	 * */
    public static String POST(String url,List<NameValuePair> nvps
    		) throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        //String url = "https://api.netease.im/nimserver/user/refreshToken.action";
        HttpPost httpPost = new HttpPost(url);

        String nonce =  UUID.randomUUID().toString();
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        //List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        //nvps.add(new BasicNameValuePair("accid", id));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        // 打印执行结果
        return EntityUtils.toString(response.getEntity(), "utf-8");
    }
    
    public static void main(String[] args) throws Exception {
    	String obj = null;
    	//obj = refreshToken("78f6ab1802b34bae9cd4095d1a95b3fb");
    	//JSONObject jo = new JSONObject(obj);
    	//System.out.println(jo.getString("code"));
    	//String id = "f1e3cfff9686475aaa8383e959bf8f9c";
    	//String name = "超级管理员";
    	//String path = "";
    	//obj = createWyAccount(id,name,path);
    	
    	//系统消息
//    	String token = "253f98697821f3ee292f5a334fc52562";
//    	PageData pd = new PageData();
//    	pd.put("attach", "{'type':'test'}");
//    	obj = sendAttachMsg(pd);
    	
    	//
    	PageData pd = new PageData();
    	pd.put("body", "{'msg':'超级管理员000'}");
    	obj = sendMsg(pd);
    	System.out.println(obj);
    }
}
