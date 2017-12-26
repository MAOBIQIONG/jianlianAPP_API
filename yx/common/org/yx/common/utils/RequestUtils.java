package org.yx.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * httpRequest请求工具类
 * 
 * @author andy
 * date: 2016-10-08
 * descrption: 请求工具类,主要封装http请求常用的一些方法,例如: 请求超时时间常量等。
 */
public class RequestUtils {

	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
    	System.out.println("请求参数："+url);
    	System.out.println("请求参数："+param);
    	
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
        System.out.println("响应数据："+result);
        return result;
    } 
    
    public static void main(String[] args) throws Exception {
    	String url = "http://ad.tnbchina.com:1080/asg/mobile/login/user/loginMsg.action";
    	
    	String data = "username=18977733148&password=123456";
    	
    	System.out.println(data);
    	
    	//发送 POST 请求
    	String sr=RequestUtils.sendPost(url, data);
        System.out.println(sr);
        
    	//String sr =  "{ee1d2r}";  null({"content":{"username":"18977733148","sex":1,"IdCard":"452802197210140022","nikeName":"王可宁","accountTotalAmount":"0.00","email":"1@q.com","age":10,"detailAddress":"锦溪路100号","Company":"Always","RegionName":"滨湖区","ProvinceName":"江苏省","CityName":"无锡市","StoreCode":"0777-0515","DutyName":"PMG","PositionName":"FM","EmployeeCode":"010771078367","EntryDateStr":"2013/03/09","LeaveDateStr":"","Status":"在职","FMCode":"","ASCode":"017710030027","AECode":"017710015904","RAMCode":"RAM005"},"status":0,"msg":"成功"})
	
    	sr = sr.substring(sr.indexOf("{"), sr.lastIndexOf("}")+1);
    	System.out.println(sr);
    	
    	JSONObject userJson = JSON.parseObject(sr);
    	String username = userJson.getJSONObject("content").getString("username");
    	System.out.println(username);
    }
}
