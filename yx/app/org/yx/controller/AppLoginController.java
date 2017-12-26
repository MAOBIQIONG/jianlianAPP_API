package org.yx.controller;

import io.rong.RongCloud;
import io.rong.models.TokenResult;

import java.util.Date;
import java.util.List; 

import javax.annotation.Resource; 

import org.apache.shiro.crypto.hash.SimpleHash; 
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.ChineseToEnglish;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.RandomNum;
import org.yx.common.utils.ReturnCode;
import org.yx.common.utils.UuidUtil;
import org.yx.common.utils.fileConfig;
import org.yx.services.common.AppUserLoginService;
import org.yx.services.common.AppUsertokenService;
import org.yx.util.CommonUtil;
import org.yx.util.Emoji; 
import org.yx.util.PushUtil;
import org.yx.util.ResultResetUtil;
import org.yx.util.ResultUtil;
import org.yx.wyyx.WyUtil;

import com.alibaba.fastjson.JSONArray;
import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.http.IGtPush;

 
/**
 * 
 * <b>类名：</b>ApiLoginController.java<br>
 * <p><b>标题：</b>建联接口Api</p>
 * <p><b>描述：</b>建联接口Api</p>
 * <p><b>版权声明：</b>Copyright (c) 2017</p>
 * <p><b>公司：</b>上海诣新信息科技有限公司 </p>
 * @author <font color='blue'>朱玉洁</font> 
 * @version 1.0.1
 * @date  2017年5月16日 14:40:33
 * @Description 登录
 */ 
@Controller
@RequestMapping({ "/appLogin" })
public class AppLoginController extends BaseController{
	public static String serverImgPath = fileConfig.getString("serverImgPath");
	
//	String imAppKey = "vnroth0kvfo6o";//替换成您的appkey
//	String appSecret = "fEPFfFSclDYo";//替换成匹配上面key的secret
//	RongCloud rongCloud = RongCloud.getInstance(imAppKey, appSecret);
	WyUtil wy = new WyUtil();

	@Resource(name = "appUserLoginService")
	private AppUserLoginService userService; 
	
	@Resource(name = "appUsertokenService")
	private AppUsertokenService tokenService;
	
	ChineseToEnglish  cte=new ChineseToEnglish();
	
	//推送
	private static String appId = "f3VtMt3APs6DhHWZhk2if9";
	private static String appKey = "MV6xcaqkMX8HhGioGjZyS9";
	private static String masterSecret = "V7EkFMB3Ij5IucuZeDwMz8";
	private static String url = "http://sdk.open.api.igexin.com/apiex.htm";
	   
	/**
	 * 用户注册 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/register" })
	@ResponseBody
	public PageData register() throws Exception { 
		PageData param = new PageData();
		PageData _result = new PageData();
		param = getPageData().getObject("params");
		logBefore(this.logger,"appLogin","register",JSONArray.toJSONString(param).toString());  
		try { 
			// 验证用户手机是否已注册
			List<PageData> users = this.userService.queryUserByPhone(param); 
			if (users != null&&users.size()>0) {
				_result.put("code", ReturnCode.REGISTER_FAIL);
				_result.put("msg", "手机号已存在！"); 
				logger.info("注册失败：手机号已存在！");
			} else { 
				// 用户信息
				PageData pad = new PageData();   
				pad.put("ID",UuidUtil.get32UUID()); 
		        String REAL_NAME="上海建联"+RandomNum.getFiveRadom();
				pad.put("REAL_NAME",REAL_NAME); 
				pad.put("VALERIE", cte.getPingYinToHeaderChar(REAL_NAME)); 
				pad.put("REGISTER_DATE",new Date());
				pad.put("LAST_LOGIN_DATE",new Date());
				pad.put("PHONE",param.getString("PHONE"));   
				pad.put("USER_NAME",param.getString("PHONE")); 
				String password = new SimpleHash("SHA-1",param.getString("PHONE"),param.getString("PASSWORD")).toString();
				pad.put("PASSWORD",password);
				pad.put("TOTAL_POINTS",0);
				pad.put("STATUS","01"); 
				pad.put("IS_VIP",0);  
				pad.put("VIP_LEVEL","02"); 
				Object re = this.userService.register(pad);   
				if(Integer.valueOf(re.toString()) >= 1){
					_result.put("code", ReturnCode.SUCCESS);
					_result.put("msg", "注册成功!");	  
			    }else{
			    	_result.put("code", ReturnCode.FAIL);
					_result.put("msg", "注册失败！");	 
				}
				logger.info(_result.getString("msg")+"service：【userService】 方法：【register】 参数【"+JSONArray.toJSONString(pad).toString()+"】");
			}
			//logAfter(this.logger,"appLogin","register",JSONArray.toJSONString(_result).toString());  
		} catch (Exception e) {
			e.printStackTrace();
			_result.put("code", ReturnCode.SERVICE_FAIL);
			_result.put("msg", "服务器繁忙，请稍后操作");
			logger.error("用户注册失败！",e);
			return _result;
		} 
		return _result;
	} 
	
	/**
	 * 用户登录 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/login" })
	@ResponseBody
	public PageData login() throws Exception { 
		PageData _result = new PageData(); 
		PageData pa = getPageData().getObject("params");  
		logBefore(this.logger,"appLogin","login",JSONArray.toJSONString(pa).toString());  
		try{ 
			List<PageData> users = this.userService.doCheckLogin(pa);
			if(users!=null&&users.size()>0){
				if(users.size()==1){
					String PASSWORD=pa.getString("PASSWORD"); 
					String pwd=new SimpleHash("SHA-1",users.get(0).getString("USER_NAME"),PASSWORD).toString();
					if(pwd.equals(users.get(0).getString("PASSWORD"))){ 
						 //判断用户是否是第一次登录 是新增token信息，否则更新token信息 
						PageData utk = this.tokenService.checkToken(users.get(0)); 
						// 用户token信息
						PageData topd = CommonUtil.getToken(users.get(0).get("userid"));
						Object editToken=null;
						if (utk != null) {
							editToken=this.tokenService.editToken(topd);
							//logger.info("修改用户的token，service：【tokenService】方法：【editToken】 参数："+JSONArray.toJSONString(topd).toString()); 
						} else {
							editToken=this.tokenService.saveToken(topd);
							//logger.info("新增用户的token，service：【tokenService】方法：【saveToken】 参数："+JSONArray.toJSONString(topd).toString()); 
						}
						if(Integer.valueOf(editToken.toString()) >= 1){
							//设置推送别名
							String cid = pa.getString("CLIENT_ID");
							String alias = users.get(0).getString("ID");
							IGtPush push = new IGtPush(url, appKey, masterSecret);
					        IAliasResult queryAlias = push.queryAlias(appId, cid);
					        if( !alias.equals(queryAlias.getAlias()) ){
					      	    IAliasResult bindSCid = push.bindAlias(appId, alias, cid);
					            System.out.println("绑定结果：" + bindSCid.getResult() + "错误码:" + bindSCid.getErrorMsg());
					        }
							
							PageData client=new PageData();
							client.put("ID",users.get(0).getString("ID"));
							client.put("CLIENT_ID",pa.getString("CLIENT_ID"));
							client.put("PLATFORM",pa.getString("PLATFORM"));
							client.put("LAST_LOGIN_DATE",new Date());
							Object cent=this.userService.updateCidAndDate(client); 
							logger.info("修改用户的登录信息，service：【userService】方法：【updateCidAndDate】 参数："+JSONArray.toJSONString(client).toString()); 
							
							if(Integer.valueOf(cent.toString()) >= 1){
								//注册并保存网易token
								String token = saveWyToken(users.get(0));
								if( token != null ){
									users.get(0).put("WYTOKEN", token);
								}
								_result.put("code", ReturnCode.SUCCESS);
								_result.put("msg", "登录成功!");
								_result.put("data",users.get(0));
								_result.put("token",topd.get("token")); 
							}else{
								_result.put("code", ReturnCode.FAIL);
								_result.put("msg", "登录失败!");  
							}  
						}else{//编辑token信息失败
							_result.put("code", ReturnCode.FAIL);
							_result.put("msg", "登录失败!");  
						} 
					}else{
						_result.put("code", ReturnCode.PWD_FAIL);
						_result.put("msg", "密码错误!");  
					}
				}else{//多条记录
					_result.put("code", ReturnCode.USER_NAME_ISIN);
					_result.put("msg", "账号重复，请联系管理员！");  
				} 
			} else {
				_result.put("code", ReturnCode.USER_NAME_ISNULL);
				_result.put("msg", "用户不存在，请先注册！"); 
			}
			logAfter(this.logger,"appLogin","login",JSONArray.toJSONString(_result).toString());  
		}catch(Exception e){
			e.printStackTrace();
			_result.put("code", ReturnCode.SERVICE_FAIL);
			_result.put("msg", "服务器繁忙，请稍后操作");
			logger.error("用户登录 失败！",e);
			return _result;
		} 
		return _result;
	}
	
	 /**
	 * 第三方登录 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/thirdLogin" })
	@ResponseBody
	public PageData thirdLogin() throws Exception {

		PageData _result = new PageData(); 
		PageData thirdUser = new PageData(); 
		PageData data =getPageData().getObject("params"); 
		logBefore(this.logger,"appLogin","thirdLogin",JSONArray.toJSONString(data).toString());  
		String TYPE=data.getString("TYPE");   
		Emoji ee=new Emoji();
		String NICK_NAME=ee.filterEmoji(data.getString("NICK_NAME"),"");  
		if ("qq".equals(TYPE)||"QQ".equals(TYPE)) {  
			thirdUser.put("QQ",data.getString("openid"));  
		}else if ("WEIXIN".equals(TYPE)||"weixin".equals(TYPE)){
			thirdUser.put("WEBCHAT",data.getString("openid")); 
		}else if("WEBO".equals(TYPE)||"webo".equals(TYPE)){
			thirdUser.put("WEBO",data.getString("openid")); 
		}
		thirdUser.put("USER_NAME",data.getString("openid")); 
		if ((NICK_NAME != null) && (!"".equals(NICK_NAME))) {  
			thirdUser.put("NICK_NAME",NICK_NAME);
			thirdUser.put("REAL_NAME",NICK_NAME);
		    String chr=cte.getPingYinToHeaderChar(NICK_NAME);
		    thirdUser.put("VALERIE",chr);
		} 
		PageData usertoken = this.tokenService.checkThirdparty(thirdUser); 
		if (usertoken != null) {//判断用户是否存在 
			// 获取新的token信息
			PageData topd = CommonUtil.getToken(usertoken.get("userid")); 
			Object editToken=this.tokenService.editToken(topd);  
			if(Integer.valueOf(editToken.toString()) >= 1){
				//设置推送别名
				String cid = data.getString("CLIENT_ID");
				String alias = usertoken.getString("userid");
				IGtPush push = new IGtPush(url, appKey, masterSecret);
		        IAliasResult queryAlias = push.queryAlias(appId, cid);
		        if( !alias.equals(queryAlias.getAlias()) ){
		      	    IAliasResult bindSCid = push.bindAlias(appId, alias, cid);
		            System.out.println("第三方登录个推绑定推送别名结果：" + bindSCid.getResult() + "错误码:" + bindSCid.getErrorMsg());
		        }
		        
				thirdUser.put("ID",usertoken.get("userid"));
				thirdUser.put("CLIENT_ID",data.getString("CLIENT_ID"));
				thirdUser.put("LAST_LOGIN_DATE",new Date());
				thirdUser.put("IMG",data.getString("img"));
				thirdUser.put("PLATFORM",data.getString("PLATFORM")); 
				
				Object ob=this.userService.updateCidAndDate(thirdUser);
				if(Integer.valueOf(ob.toString()) >= 1){
					PageData user=userService.thirdLogin(thirdUser); 
					//注册并保存网易token
					String token = saveWyToken(user);
					if( token != null ){
						user.put("WYTOKEN", token);
					}
					_result.put("flag","false");//不是第一次
					_result.put("code", ReturnCode.SUCCESS);
					_result.put("msg", "第三方登录成功！");	  
					_result.put("data", user);	 
					_result.put("token",topd.get("token"));	 
				}else{
					_result.put("code", ReturnCode.FAIL);
					_result.put("msg", "第三方登录失败!");	
				} 
				logger.info(_result.getString("msg")+" service：【userService】 方法：【updateCidAndDate】，结果为"+JSONArray.toJSONString(_result).toString());
			}else{//修改token失败
				_result.put("code", ReturnCode.FAIL);
				_result.put("msg", "登录失败!");	
			}
			logger.info("用户已经存在，修改用户token信息。 service：【tokenService】 方法：【editToken】，结果为"+JSONArray.toJSONString(_result).toString());
		} else {  
			thirdUser.put("ID",this.get32UUID());
			thirdUser.put("REGISTER_DATE",new Date());
			thirdUser.put("LAST_LOGIN_DATE",new Date()); 
			thirdUser.put("TOTAL_POINTS",0);
			thirdUser.put("STATUS","01"); 
			thirdUser.put("IS_VIP",0);  
			thirdUser.put("VIP_LEVEL","02");
			thirdUser.put("CLIENT_ID",data.getString("CLIENT_ID"));
			thirdUser.put("IMG",data.getString("img"));
			
			Object re = this.userService.save(thirdUser); 
			logger.info("用户不存在，保存用户信息。 service：【userService】 方法：【save】，参数为"+JSONArray.toJSONString(thirdUser).toString());
			if(Integer.valueOf(re.toString()) >= 1){
				// 生成token信息
				PageData topd = CommonUtil.getToken(thirdUser.getString("ID"));
				Object ob=this.tokenService.saveToken(topd); 
				logger.info("保存用户token信息。 service：【tokenService】 方法：【saveToken】，参数为"+JSONArray.toJSONString(topd).toString());
				if(Integer.valueOf(ob.toString()) >= 1){
					//设置推送别名
					String cid = data.getString("CLIENT_ID");
					String alias = thirdUser.getString("ID");
					IGtPush push = new IGtPush(url, appKey, masterSecret);
			        IAliasResult queryAlias = push.queryAlias(appId, cid);
			        if( !alias.equals(queryAlias.getAlias()) ){
			      	    IAliasResult bindSCid = push.bindAlias(appId, alias, cid);
			            System.out.println("（首次）第三方登录个推绑定推送别名结果：" + bindSCid.getResult() + "错误码:" + bindSCid.getErrorMsg());
			        }
			        
					PageData user=userService.thirdLogin(thirdUser); 
					//注册并保存网易token
					String token = saveWyToken(user);
					if( token != null ){
						user.put("WYTOKEN", token);
					}
					_result.put("flag","true");//是第一次
					_result.put("code", ReturnCode.SUCCESS);
					_result.put("msg", "登录成功！");	  
					_result.put("data", user);	 
					_result.put("token",topd.get("token"));  
				}else{
					_result.put("code", ReturnCode.FAIL);
					_result.put("msg", "登录失败!");	
				} 
				logger.info("保存用户token信息,结果为"+JSONArray.toJSONString(_result).toString());
		    }else{
		    	_result.put("code", ReturnCode.FAIL);
				_result.put("msg", "登录失败!");	 
		    }
			logger.info("保存用户信息,结果为"+JSONArray.toJSONString(_result).toString());
		}
		logAfter(this.logger,"appLogin","thirdLogin",JSONArray.toJSONString(_result).toString());  
		return _result;
	}
	
	/* 输入账户，查询是否存在密码，若存在则账号密码登录，若不存在根据验证码登录
	 * 手机号验证码登录
	 */  
	@RequestMapping({ "/loginByPhone" })
	@ResponseBody
	public PageData loginByPhone(){  
		PageData _result = new PageData(); 
		try{ 
			PageData data = getPageData().getObject("params");
			logBefore(this.logger,"appLogin","loginByPhone",JSONArray.toJSONString(data).toString());  
			// 验证用户手机号，账号，卡号查找信息
			List<PageData> uc = this.userService.doCheckLogin(data);
			if(uc.size()==1){
				if (uc.get(0).getString("PASSWORD")!=null&&!"".equals(uc.get(0).getString("PASSWORD"))) {
					_result.put("code", ReturnCode.PWD_ISIN);
					_result.put("msg", "密码已存在！"); 
				} else {  
					_result.put("code", ReturnCode.PWD_ISNULL);
					_result.put("msg", "暂无密码！");	 
				} 
			}else{
				if(uc.size()==0){//说明手机号不存在 
					_result.put("code", ReturnCode.USER_NAME_ISNULL);
					_result.put("msg", "账号不存在，请注册！");	  
				}else{//存在多条记录 
					_result.put("code", ReturnCode.USER_NAME_ISIN);
					_result.put("msg", "账号重复，请联系客服！");	  
				}
			}
		}catch(Exception e){
			logger.error("错误信息：",e);
			e.printStackTrace();
		}
		logAfter(this.logger,"appLogin","loginByPhone",JSONArray.toJSONString(_result).toString());  
		return _result;
	} 
	
	 /** 
	  * 第一次登陆修改密码
	 */
	@RequestMapping({ "/setPass" })
	@ResponseBody
	public PageData setPass() {  
		PageData _result = new PageData(); 
		try{ 
			PageData data = getPageData().getObject("params");
			logBefore(this.logger,"appLogin","setPass",JSONArray.toJSONString(data).toString());  
			List<PageData> user = this.userService.doCheckLogin(data);
			String password = new SimpleHash("SHA-1",data.getString("USER_NAME"),data.getString("PASSWORD")).toString();
			data.put("PASSWORD",password); 
			 //判断用户是否是第一次登录 是新增token信息，否则更新token信息 
			PageData utk = this.tokenService.checkToken(user.get(0)); 
			// 用户token信息
			PageData topd = CommonUtil.getToken(user.get(0).get("userid"));
			if (utk != null) {
				this.tokenService.editToken(topd);
			} else {
				this.tokenService.saveToken(topd);
			}
			logger.info("保存用户token信息。 service：【tokenService】 方法：【saveToken  editToken】，参数为"+JSONArray.toJSONString(topd).toString());
			//设置推送别名
			String cid = data.getString("CLIENT_ID");
			String alias = user.get(0).getString("ID");
			IGtPush push = new IGtPush(url, appKey, masterSecret);
			IAliasResult queryAlias = push.queryAlias(appId, cid);
			if( !alias.equals(queryAlias.getAlias()) ){
	     	    IAliasResult bindSCid = push.bindAlias(appId, alias, cid);
	           System.out.println("绑定结果：" + bindSCid.getResult() + "错误码:" + bindSCid.getErrorMsg());
			}
	        PageData client=new PageData();
			client.put("ID",user.get(0).getString("ID"));
			client.put("CLIENT_ID",data.getString("CLIENT_ID"));
			client.put("PLATFORM",data.getString("PLATFORM"));
			client.put("LAST_LOGIN_DATE",new Date());
			this.userService.updateCidAndDate(client);
			logger.info("修改用户信息。 service：【userService】 方法：【updateCidAndDate】，参数为"+JSONArray.toJSONString(client).toString());
			// 修改密码
			Object ob = this.userService.editPassWord(data); 
			logger.info("修改用户密码。 service：【userService】 方法：【editPassWord】，参数为"+JSONArray.toJSONString(data).toString());
			if(Integer.valueOf(ob.toString()) >= 1){
				_result.put("code", ReturnCode.SUCCESS);
				_result.put("msg", "密码修改成功！");	 
				_result.put("data", user.get(0));	 
				_result.put("token",topd.get("token"));	 
			}else{
		    	_result.put("code", ReturnCode.FAIL);
				_result.put("msg", "密码修改失败!");	
				_result.put("data", user.get(0));	 
				_result.put("token",topd.get("token"));	 
			}
		}catch(Exception e){
			logger.error("错误列表：",e);
			e.printStackTrace();
		}
		logAfter(this.logger,"appLogin","setPass",JSONArray.toJSONString(_result).toString());  
		return _result;
	} 
	 
	 /**
	 * 根据手机号重置密码 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/setPwdByPhone" })
	@ResponseBody
	public PageData setPwdByPhone() throws Exception { 
		PageData _result=new PageData();  
		PageData data = getPageData().getObject("params");
		logBefore(this.logger,"appLogin","setPwdByPhone",JSONArray.toJSONString(data).toString());  
		// 验证用户手机号 
		List<PageData> uc = this.userService.queryUserByPhone(data);
		if (uc != null && uc.size()>0) { 
			String password=new SimpleHash("SHA-1",uc.get(0).getString("USER_NAME"),data.getString("PASSWORD")).toString();
			data.put("PASSWORD", password);
			data.put("USER_NAME",uc.get(0).getString("USER_NAME")); 
			Object re = this.userService.editPassWord(data); 
			if(Integer.valueOf(re.toString()) >= 1){
				_result.put("code", ReturnCode.SUCCESS);
				_result.put("msg", "密码修改成功！");	
				logger.info("密码修改成功。 service：【userService】 方法：【editPassWord】，参数为"+JSONArray.toJSONString(data).toString());
		    }else{
		    	_result.put("code", ReturnCode.FAIL);
				_result.put("msg", "密码修改失败!");	
				logger.info("密码修改失败! service：【userService】 方法：【editPassWord】，参数为"+JSONArray.toJSONString(data).toString());
		    } 
		} else { 
			_result.put("code", ReturnCode.FAIL);
			_result.put("msg", "此用户不存在，请注册！");	   
		}
		logAfter(this.logger,"appLogin","setPwdByPhone",JSONArray.toJSONString(_result).toString());  
		return _result;
	} 

	/**
	 * 根据手机号绑定邮箱
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/bindEmail" })
	@ResponseBody
	public PageData bindEmail() { 
		PageData _result = new PageData();
		try{ 
			PageData data =getPageData().getObject("pa");  
			logBefore(this.logger,"appLogin","bindEmail",JSONArray.toJSONString(data).toString());  
			Object ob=this.userService.bindEmail(data);
			if(Integer.valueOf(ob.toString()) >= 1){
				_result.put("code", "true");
				_result.put("errcode", ReturnCode.SUCCESS);
				_result.put("msg", "成功！");	  
				logger.info("根据手机号绑定邮箱成功！ service：【userService】 方法：【bindEmail】，参数为"+JSONArray.toJSONString(data).toString());
		    }else{
		    	_result.put("errcode", ReturnCode.FAIL);
				_result.put("msg", "失败!");	
				logger.info("根据手机号绑定邮箱失败! service：【userService】 方法：【bindEmail】，参数为"+JSONArray.toJSONString(data).toString());
		    }  
		}catch(Exception e){
			e.printStackTrace();
			logger.error("根据手机号绑定邮箱失败！错误信息：",e);
			_result.put("errcode", ReturnCode.FAIL);
			_result.put("msg", "失败!");	 
			logger.error("根据手机号绑定邮箱失败！",e);
		} 
		logAfter(this.logger,"appLogin","bindEmail",JSONArray.toJSONString(_result).toString());  
		return _result; 
	}
	
	@RequestMapping({ "/appCleanPush" })
	@ResponseBody
	public String appCleanPush() throws Exception {
		PageData pa=new PageData();
		pa = getPageData();
		PageData p = pa.getObject("params");
		logBefore(this.logger,"appLogin","appCleanPush",JSONArray.toJSONString(p).toString());  
		String userid = p.getString("userid");
		String cid = p.getString("cid");
		
		PushUtil jk=new PushUtil();
	    boolean result = jk.cleanAlias(userid, cid); 
	    logAfter(this.logger,"appLogin","appCleanPush",JSONArray.toJSONString(result).toString());  
		return result+"";
	}
	
	@RequestMapping({ "/queryNewCode" })
	@ResponseBody
	public PageData queryNewCode() {
		PageData _result = new PageData();
		try{
			logBefore(this.logger,"appLogin","queryNewCode","null");  
			PageData code=this.tokenService.queryNewCode(null); 
			_result.put("data",code);
			_result.put("code", ReturnCode.SUCCESS);
			_result.put("msg", "成功！");	 
		}catch(Exception e){
			e.printStackTrace();
			_result.put("code", ReturnCode.FAIL);
			_result.put("msg", "失败!");	
			logger.error("查询最新app版本号失败！",e); 
		}
		logAfter(this.logger,"appLogin","queryNewCode",JSONArray.toJSONString(_result).toString());  
		return _result; 
	} 
	
	/**
	 * 用户登录 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/loginByMobile" })
	@ResponseBody
	public PageData loginByMobile() throws Exception { 
		PageData _result = new PageData(); 
		PageData pa = getPageData().getObject("params");  
		try{ 
			logBefore(this.logger,"appLogin","loginByMobile",JSONArray.toJSONString(pa).toString());  
			List<PageData> users = this.userService.doCheckLogin(pa);
			if(users!=null&&users.size()>0){
				if(users.size()==1){
					//判断用户是否是第一次登录 是新增token信息，否则更新token信息 
					PageData utk = this.tokenService.checkToken(users.get(0)); 
					// 用户token信息
					PageData topd = CommonUtil.getToken(users.get(0).get("userid"));
					Object editToken=null;
					if (utk != null) {
						editToken=this.tokenService.editToken(topd);
					} else {
						editToken=this.tokenService.saveToken(topd);
					}
					if(Integer.valueOf(editToken.toString()) >= 1){
						//设置推送别名
						String cid = pa.getString("CLIENT_ID");
						String alias = users.get(0).getString("ID");
						IGtPush push = new IGtPush(url, appKey, masterSecret);
				        IAliasResult queryAlias = push.queryAlias(appId, cid);
				        if( !alias.equals(queryAlias.getAlias()) ){
				      	    IAliasResult bindSCid = push.bindAlias(appId, alias, cid);
				            System.out.println("绑定结果：" + bindSCid.getResult() + "错误码:" + bindSCid.getErrorMsg());
				        }
						
						PageData client=new PageData();
						client.put("ID",users.get(0).getString("ID"));
						client.put("CLIENT_ID",pa.getString("CLIENT_ID"));
						client.put("PLATFORM",pa.getString("PLATFORM"));
						client.put("LAST_LOGIN_DATE",new Date());
						Object cent=this.userService.updateCidAndDate(client); 
						if(Integer.valueOf(cent.toString()) >= 1){
							//注册并保存网易token
							String token = saveWyToken(users.get(0));
							if( token != null ){
								users.get(0).put("WYTOKEN", token);
							}
							_result.put("code", ReturnCode.SUCCESS);
							_result.put("msg", "登录成功!");
							_result.put("data",users.get(0));
							_result.put("token",topd.get("token")); 
						}else{
							_result.put("code", ReturnCode.FAIL);
							_result.put("msg", "登录失败!");  
						}  
					}else{//编辑token信息失败
						_result.put("code", ReturnCode.FAIL);
						_result.put("msg", "编辑token信息失败!");  
					} 
				}else{//多条记录
					_result.put("code", ReturnCode.USER_NAME_ISIN);
					_result.put("msg", "账号重复，请联系管理员！"); 
				} 
			} else {
				_result.put("code", ReturnCode.USER_NAME_ISNULL);
				_result.put("msg", "用户不存在，请先注册！");
			}
		}catch(Exception e){
			e.printStackTrace();
			_result.put("code", ReturnCode.SERVICE_FAIL);
			_result.put("msg", "服务器繁忙，请稍后操作");
			logger.error("根据手机号登录失败！",e);
			return _result;
		} 
		logAfter(this.logger,"appLogin","loginByMobile",JSONArray.toJSONString(_result).toString());  
		return _result;
	}
	
	/**
	 * 第三方登录绑定已有账号首次绑定账号功能
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/bindPhone" })
	@ResponseBody
	public PageData bindPhone(){ 
		PageData _result = new PageData(); 
		PageData pa = getPageData().getObject("params");  
		try{  
			logBefore(this.logger,"appLogin","bindPhone",JSONArray.toJSONString(pa).toString());  
			if(EmptyUtil.isNullOrEmpty(pa)){
				_result.put("code", ReturnCode.USER_NAME_ISNULL);
				_result.put("msg", "用户不存在，请先注册！");
				logger.info("用户不存在，请先注册！");
				return _result; 
			}  
			List<PageData> users = this.userService.queryUserByPhone(pa);  
			//手机号没有注册过
			if(users==null||users.size()==0){
				_result.put("code", ReturnCode.USER_NAME_ISNULL);
				_result.put("msg", "手机号不存在！");
				logger.info("手机号不存在！ service：【userService】 方法：【queryUserByPhone】，参数为"+JSONArray.toJSONString(users).toString());
			}
			//手机号已经注册且不止一条记录
			if(users.size()>1){
				_result.put("code", ReturnCode.USER_NAME_ISIN);
				_result.put("msg", "手机号重复，请联系管理员！");
				logger.info("手机号重复，请联系管理员！");
				return _result; 
			}
			//手机号已注册且只有一条记录
			PageData uu=users.get(0);
			PageData user =this.userService.findById(pa);
			ResultResetUtil.resetResStr(uu,user,new String[]{"POSITION","COMPANY_ID","PHONE","IMG","CARD_NO","SUMMARY","WEBCHAT","QQ","CLAN_ID"});
			uu.put("NICK_NAME",uu.getString("REAL_NAME"));
			uu.put("USER_ID",uu.get("ID")); 
			boolean result1=this.userService.edit(uu);
			if(!result1){
				_result.put("code", "400");
				_result.put("msg", "绑定手机号失败！"); 
				logger.info("绑定手机号失败！");
				return _result;  
			}
			boolean result2 =this.userService.deleteUser(uu);
			if(!result2){
				_result.put("code", "400");
				_result.put("msg", "绑定手机号失败！"); 
				logger.info("绑定手机号失败！");
				return _result;   
			} 
			//判断用户是否是第一次登录 是新增token信息，否则更新token信息 
			PageData utk = this.tokenService.checkToken(uu); 
			// 用户token信息
			PageData topd = CommonUtil.getToken(uu.get("userid"));
			Object editToken=null;
			if (utk != null) {
				editToken=this.tokenService.editToken(topd);
			} else {
				editToken=this.tokenService.saveToken(topd);
			} 
			if(editToken!=null&&Integer.parseInt(editToken.toString())>=1){
				PageData userinfo= this.userService.thirdLogin(uu);
				_result.put("code", "200");
				_result.put("msg", "绑定手机号成功！"); 
				_result.put("token",topd.get("token").toString()); 
				_result.put("result",userinfo); 
				logger.info("绑定手机号成功！");
				return _result;    
			}else{
				_result.put("code", "400");
				_result.put("msg", "绑定手机号失败！"); 
				logger.info("绑定手机号失败！");
				return _result;   
			} 
		}catch(Exception e){
			e.printStackTrace();
			_result.put("code", ReturnCode.SERVICE_FAIL);
			_result.put("msg", "服务器繁忙，请稍后操作");
			logger.error("根据手机号登录失败！",e);
			return _result;
		}  
	}  
	
	/**
	 * 根据用户id查询用户信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/queryUserInfo" })
	@ResponseBody
	public PageData queryUserInfo(){ 
		PageData _result = new PageData(); 
		PageData pa = getPageData();  
		try{  
			logBefore(this.logger,"appLogin","queryUserInfo",JSONArray.toJSONString(pa).toString());  
			if(EmptyUtil.isNullOrEmpty(pa)){
				_result.put("code","400"); 
				logger.info("用户id不存在！");
				return _result; 
			} 
			PageData user=this.userService.queryUpgradeInfo(pa);  
			_result.put("code", "200");  
			_result.put("result",user);  
			return _result;  
		}catch(Exception e){
			e.printStackTrace();
			_result.put("code","400"); 
			logger.error("根据手机号登录失败！",e);
			return _result;
		}  
	}  
	
	//修改获取网易云信TOKEN保存
	public String saveWyToken(PageData user) throws Exception {
		System.out.println("******************网易云信注册******************");
		if( user != null ){
			String wyToken = user.getString("WYTOKEN");
			if( "".equals(wyToken) || wyToken == null ){
				String userImg = user.getString("IMG");
				if( !"".equals(userImg) && userImg != null ){
					if( userImg.indexOf("http://") < 0 ){
						userImg = serverImgPath + userImg;
					}
				}
				/**
				 * 返回参数：
				 * 新建网易账号:
				 * {
				 *     "code":200,
				 *     "info":
				 *     {
				 *         "token":"6d3bd0f635024889da1811bdc5933aa9",
				 *         "accid":"cfbd8d6f1eeb47f59e3a5f74c79360a8",
				 *         "name":"何尔萌"
				 *      }
				 * }
				 * */
				String str = wy.createWyAccount(user.getString("ID"), user.getString("REAL_NAME"), userImg);
				JSONObject obj = new JSONObject(str);
				
				String code = obj.getString("code");
				if( "200".equals(code) ){
					PageData im = new PageData();
					im.put("ID",user.getString("ID"));
					im.put("WYTOKEN",obj.getJSONObject("info").getString("token"));
					int rs = (Integer) this.userService.editWyToken(im);
					if( rs > 0 ){
						return im.getString("WYTOKEN");
					}
					logger.info("修改用户消息推送的token，service：【userService】方法：【editWyToken】 参数："+JSONArray.toJSONString(im).toString()); 
				}else if( "414".equals(code) ){
					System.out.println("网易云信：已注册！");
				}
		    }
		}
		return null;
	}
}
