package wangyi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils; 
import org.springframework.stereotype.Service; 
import org.yx.common.entity.PageData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject; 

/**
 * 发送模板短信
 *
 */
public class SendTemplate{
	private static final String SERVER_URL="https://api.netease.im/sms/sendcode.action";//发送验证码的请求路径URL
    private static final String APP_KEY="47045ca57e2ca57f805cb24563e34160";//网易云信分配的账号
    private static final String APP_SECRET="818942ff3fac";//网易云信分配的密钥
    private static final String NONCE="123456";//随机数 
	 
    public String sendMsg(PageData pd) throws IOException { 
		String mobile = pd.getString("mobile");                    // 目标手机号码
        String templateid = pd.getString("template");              // 短信模板
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(SERVER_URL);

        String curTime=String.valueOf((new Date().getTime()/1000L));
        String checkSum=CheckSumBuilder.getCheckSum(APP_SECRET,NONCE,curTime);//得到 发送验证码必须的参数checkSum

        //设置请求的header
        post.addHeader("AppKey",APP_KEY);
        post.addHeader("Nonce",NONCE);
        post.addHeader("CurTime",curTime);
        post.addHeader("CheckSum",checkSum);
        post.addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        //设置请求参数
        List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();  
        nameValuePairs.add(new BasicNameValuePair("mobile",mobile));
        nameValuePairs.add(new BasicNameValuePair("templateid", templateid)); 

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));

        //执行请求
        HttpResponse response=httpclient.execute(post);
        String responseEntity= EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println("验证码："+responseEntity);
        //判断是否发送成功，发送成功返回true
        JSONObject jsonStr= JSON.parseObject(responseEntity);
        return jsonStr.toString();
    }
}

