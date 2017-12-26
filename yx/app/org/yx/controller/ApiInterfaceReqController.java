package org.yx.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.yx.cache.CacheHandler;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.AESOperator;
import org.yx.common.utils.Const; 
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.MD5;
import org.yx.common.utils.ReturnCode;
import org.yx.common.utils.XmlExercise;
import org.yx.services.ApiInterfaceMethodService;
import org.yx.services.ApiInterfaceValidateService;
import org.yx.services.AppCommonService;
import org.yx.services.AppUserTokenService;
import org.yx.util.ApiMethodsUtil;

import com.alibaba.fastjson.JSONArray;

/**
 * 
 * <b>类名：</b>ApiInterfaceReqController.java<br>
 * <p><b>标题：</b>建联接口Api</p>
 * <p><b>描述：</b>建联接口Api</p>
 * <p><b>版权声明：</b>Copyright (c) 2017</p>
 * <p><b>公司：</b>上海诣新信息科技有限公司 </p>
 * @author <font color='blue'></font> 
 * @version 1.0.1
 * @date  2017年2月14日 下午1:54:40
 * @Description api接口请求controller层
 */
@Controller
@RequestMapping(value="/apiReq")
@SuppressWarnings("unchecked")
public class ApiInterfaceReqController extends BaseController{ 
	
	//接口方法service
	@Resource(name="apiInterfaceMethodService")
	private ApiInterfaceMethodService apiInterfaceMethodService;
	
	//接口验证service
	@Resource(name="apiInterfaceValidateService")
	private ApiInterfaceValidateService apiInterfaceValidateService;
	
	@Resource(name="appUserTokenService")
	private AppUserTokenService appUserTokenService;
	
	@Resource(name="appCommonService")
	private AppCommonService appCommonService;
	
	
	/**
	 * 
	 * <b>方法名：</b>：apiRequest<br>
	 * <b>功能说明：</b>：TODO<br>
	 * @author <font color='blue'>朱玉洁</font> 
	 * @date  2017年2月14日 下午2:25:25
	 * @return 接口请求
	 */
	@RequestMapping(method=RequestMethod.POST,value="/apiRequest")
	@ResponseBody
	public PageData apiRequest(){ 
		long startTime=System.currentTimeMillis();
		PageData pd=new PageData();
		pd=getPageData();
		
		PageData _result=new PageData();
		_result.put("result", null);
		_result.put("md5_str", null);
		String apiName="";
		try { 
			//查询接口对象
//			PageData data=apiInterfaceMethodService.findMethod(pd);//--需要放缓存中

			//PageData data= ApiMethodsUtil.getMethod(pd.getString("InterfaceId")); 

//			PageData data= ApiMethodsUtil.getMethod(pd.getString("InterfaceId"));
			String methodId=pd.getString("InterfaceId");
//			PageData data= CacheHandler.getApiMethod(methodId);//从缓存中获取ApiMethod
			PageData data= apiInterfaceMethodService.getMethod(methodId);
			long secTime=System.currentTimeMillis();
			long secdMills=secTime-startTime; 
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>接口 ："+ apiName+"第一次耗费时间"+ secdMills+" 毫秒 ，合计"+(secdMills/1000.00) +"秒！！！！！！！");
//			System.out.println(" start apis************************************************************************");
//			System.out.println(data.size());
//			System.out.println(" end apis************************************************************************");
			String paramDecode=null;
			if(!EmptyUtil.isNullOrEmpty(pd.getString("params"))){
				paramDecode=AESOperator.getInstance().decrypt(pd.getString("params"));//解密
			}
			PageData paramKey=new PageData();
			//String expendParam=paramDecode.substring(0,paramDecode.length()-1)+",\"USER_ID\":\""+USER_ID+"\"}";
			paramKey.put("params", paramDecode); 
			paramKey.put("Token", pd.getString("Token"));
			logBefore(this.logger,data.getString("class_path"),data.getString("function_name"),JSONArray.toJSONString(paramKey).toString());  
			
			
			if(!EmptyUtil.isNullOrEmpty(data)){ 
				if(Const.YES.equals(data.getString("is_login"))){
					PageData user = (PageData) this.appCommonService.findtokenDetail(pd); 
					if(user==null){ 
						//token过期
						_result.put("code", ReturnCode.TOKEN_TIMEOUT);
						_result.put("result","token过期！");
						_result.put("msg", "token过期！");
						logAfter(this.logger,data.getString("class_path"),data.getString("function_name"),null);  
						return _result;  
					}
				}
				if(Const.YES.equals(data.getString("is_validate"))){
					if(isValidate(pd, data)){
						//参数验证通过，调用具体业务类
						String result=this.invoke(data.getString("class_path"), data.getString("function_name"), paramKey);
						_result.put("code", ReturnCode.SUCCESS);
						_result.put("result",result );
						_result.put("msg", "操作成功");
					}else{
						//参数验证失败，返回错误编码
						_result.put("code", ReturnCode.FAIL);
						_result.put("result","参数错误！");
						_result.put("msg", "参数错误！");
					}
				}else{
					//不用验证参数，调用具体业务类
					String result=this.invoke(data.getString("class_path"), data.getString("function_name"), paramKey);
					_result.put("code", ReturnCode.SUCCESS);
					_result.put("result",result );
					_result.put("msg", "操作成功");
				}
			} 
			logAfter(this.logger,data.getString("class_path"),data.getString("function_name"),_result.getString("result"));  
		} catch (Exception e) {
			e.printStackTrace();
			_result.put("code", ReturnCode.SERVICE_FAIL);
			_result.put("msg", "服务器繁忙，请稍后操作");
			logger.error("错误：",e); 
			return _result;
		} 
		long entTime=System.currentTimeMillis();
		long spendMills=entTime-startTime; 
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>接口 ："+ apiName+"耗费时间"+ spendMills+" 毫秒 ，合计"+(spendMills/1000.00) +"秒！！！！！！！");
		return _result;
	}
	
	
	/**
	 * 
	 * <b>方法名：</b>：isValidate<br>
	 * <b>功能说明：</b>：验证参数<br>
	 * @author <font color='blue'>朱玉洁</font> 
	 * @date  2017年2月16日 上午9:53:04
	 * @param pd 客户端传递参数
	 * @param data 接口方法pagedata
	 * @return
	 */
	public boolean isValidate(PageData pd,PageData data){
		try { 
			String paramEncrypt=pd.getString("params").trim().replace("%2B", "+");
			//判断验证参数是否为空，如果不为空进行解密对比
			if(!EmptyUtil.isNullOrEmpty(paramEncrypt)){
				logger.info("===================开始加密解密验证===========================");
				//对参数进行解密验证
				String paramDecode=AESOperator.getInstance().decrypt(paramEncrypt);
				//对解密后的参数进行MD5加密，跟APP端进行对比
				String paramMD5=MD5.md5(paramDecode);
				String md5_str=pd.getString("md5_str");
				//判断两次md5加密是否相同
				if(paramMD5.equals(md5_str)){
					//验证参数
					logger.info("=====================加密解密验证通过，开始验证参数============================");
					//将json转为xml
					String xml=XmlExercise.jsontoXml(paramDecode);
			        Element root = XmlExercise.getRootElement(xml);
			        //解析xml
			        
			        XmlExercise.list.clear();
			        List<String> list = new ArrayList<String>();
			        list= XmlExercise.getElementList(root);
			        
			        //封装key
			        List<String> keyList=new ArrayList<String>();
			        for (String string : list) {
						keyList.add(string.substring(0, string.lastIndexOf("=")));
					}
			        
			        //将list<string>转换为list<map>
			        List<Map<String, String>> valueMap=new ArrayList<Map<String, String>>();
			        for (String string : list) {
						Map<String, String> map=new HashMap<String, String>();
						map.put(string.substring(0, string.lastIndexOf("=")), string.substring(string.lastIndexOf("=")+1));
						valueMap.add(map);
					}
			        
			        PageData midData=new PageData();
			        midData.put("api_interface_method_id", data.getString("id"));
			        //查询接口验证集合
			        List<PageData> validateList=apiInterfaceValidateService.findValidateByMid(midData);
			        //判断参数列表
			        if(EmptyUtil.isNullOrEmpty(validateList)){
			        	logger.error("=======================参数列表为空===========================");
			        	return false;
			        }
			        logger.info("========================参数数量验证通过，开始验证参数==========================");
	        		for (PageData pageData : validateList) {
        				String key=pageData.getString("key").trim();
        				
        				for (Map<String, String> vmap : valueMap) {
	        				//根据key获取value,判断value是否为空
	        				if(keyList.contains(key)){
	        					//1.验证是否为空
	        					String is_null=pageData.getString("is_null");
	        					if(is_null.equals(Const.NO)){
	        						//值不为空，判断是否需要验证
	        						if(!EmptyUtil.isNullOrEmpty(vmap.get(key))){
	        							//2.判断是否需要验证
	        							String is_validate=pageData.getString("is_validate");
	        							//需要验证
	        							if(is_validate.equals(Const.YES)){
	        								
	        								Integer min_length=0;
	        								if(!EmptyUtil.isNullOrEmpty(pageData.getString("min_length").trim())){
	        									//最小长度
	        									min_length=Integer.parseInt(pageData.getString("min_length").trim().toString());
	        								}
	        								Integer max_length=0;
	        								if(!EmptyUtil.isNullOrEmpty(pageData.getString("max_length").trim())){
	        									//最大长度
	        									max_length=Integer.parseInt(pageData.getString("max_length").trim().toString());
	        								}
	        								List<String> regList=null;
	        								//正则表达式
	        								if(!EmptyUtil.isNullOrEmpty(pageData.getString("reg"))){
	        									regList=Arrays.asList(pageData.getString("reg").split(";"));
	        								}
	        								
	        								if(vmap.get(key).trim().length()!=0){
	        									//验证最小长度
		        								if(min_length>0){
		        									if(vmap.get(key).length()<min_length){
		        										logger.error("====================参数最小长度验证失败======================");
		        										return false;
		        									}
		        								}
		        								
		        								//验证最大长度
		        								if(max_length>0){
	        										if(vmap.get(key).length()>max_length){
	        											logger.error("====================参数最大长度验证失败==================");
	        											return false;
	        										}
        										}
		        								
		        								//验证正则表达式
    											if(!EmptyUtil.isNullOrEmpty(regList)){
    												for (String string : regList) {
														//编译正则表达式
    													Pattern pattern=Pattern.compile(string);
    													Matcher matcher=pattern.matcher(vmap.get(key));
    													if(!matcher.matches()){
    														logger.error("======================正则验证失败===================");
    														return false;
    													}
													}
    											}
	        								}else{
	        									logger.error("==================验证失败，参数值为空=======================");
												return false;
	        								}
	        							}
	        						}
	        					}
	        				}else{
	        					logger.error("=================参数key不存在,验证失败=======================");
	        					return false;
	        				}
		        		}
		        	}
				}else{
					//md5加密对比失败，返回错误参数
					logger.error("=====================加密解密验证失败==================================");
					return false;
				}
			}else{
				//参数为空，返回参数列表错误
				logger.error("=========================参数为空=====================");
				return false;
			} 
		} catch (Exception e) {
			logger.error("===错误：",e);
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	
	/**
	 * 
	 * <b>方法名：</b>：invoke<br>
	 * <b>功能说明：</b>：内部方法反射<br>
	 * @author <font color='blue'>朱玉洁</font> 
	 * @date  2017年2月16日 下午4:49:48
	 * @param className
	 * @param functionName
	 * @param param
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public String invoke(String className,String functionName,PageData pd) { 
		String result=null;
			try {
				WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext(); 
				Class<?>  cls = wac.getBean(className).getClass(); 
				Method m =cls.getDeclaredMethod(functionName,new Class[]{PageData.class});
				 result=(String)m.invoke(wac.getBean(className),pd); 
			} catch (SecurityException e) {
				logger.error("===错误：",e);
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				logger.error("===错误：",e);
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				logger.error("===错误：",e);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				logger.error("===错误：",e);
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				logger.error("===错误：",e);
				e.printStackTrace();
			} catch (Exception e) {
				logger.error("===错误：",e);
				e.printStackTrace();
			}
		return result;
	}
	
	
	
	 public static void main(String args[]) throws DocumentException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	    	String json="{\"USER_ID\":\"1\", \"TYPE\":\"00\",\"EXPRESS_PRICE\":\"20\",\"TOTAL_PRICE\":\"202\",\"SHOP_USERNAME\":\"张三\",\"SHOP_ADDRESS\":\"上海市浦东新区东明路20号\",\"SHOP_PHONE\":\"15000900903\",\"PROD\" :[{\"PROD_NO\":\"p0001\",\"PROD_PRICE\":\"45.50\",\"PROD_NUM\":\"2\",\"PROD_GUIGE\":\"白色500ml\"},{\"PROD_NO\":\"p0002\",\"PROD_PRICE\":\"50.50\",\"PROD_NUM\":\"7\",\"PROD_GUIGE\":\"黑色500ml\"}]}";
	    	String xml=XmlExercise.jsontoXml(json);
	        Element root = XmlExercise.getRootElement(xml);
//	        List<Map<String, String>> elemList= new ArrayList<Map<String,String>>();
//	        elemList= XmlExercise.getElementList(root);
	        System.out.println("-----------原xml内容------------");
	        System.out.println(xml);
	        System.out.println("-----------解析结果------------");
	       // System.out.println(elemList);
	        
	        //验证正则
	        String str = "service@xsoftlab.net";
	        String regEx = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
	        Pattern pattern = Pattern.compile(regEx);
	        Matcher matcher = pattern.matcher(str);
	        boolean rs = matcher.matches();
	        System.out.println(rs);
	        
	        String aa="";
	        
	        System.err.println(aa.length()); 
	    }
}
