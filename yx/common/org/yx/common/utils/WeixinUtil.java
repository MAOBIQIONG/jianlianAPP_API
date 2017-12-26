package org.yx.common.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import weixin.popular.api.SnsAPI;
import weixin.popular.api.TicketAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.bean.ticket.Ticket;
import weixin.popular.bean.token.Token;

public class WeixinUtil {

	public static String accessToken;
	public static String jsapiTicket;
//	public static String APPID = ConfConfig.getString("WXAPPID");
//	public static String APPSECRET = ConfConfig.getString("APPSECRET");
	
	public static WeixinUtil weixinUtil;
	
	public static WeixinUtil getInstance(){
		if(weixinUtil == null){
			weixinUtil = new WeixinUtil();
		}
		return weixinUtil;
	}
	
	public static String getAccessToken(){
		Cache cache = CacheManager.getInstance().getCache("accessToken");
		Element element = cache.get("accessToken");
		if(element == null || element.getValue() == null){
			Token token = TokenAPI.token(ConfConfig.getString("WXAPPID"), ConfConfig.getString("WXAPPSECRET"));
			element = new Element("accessToken", token.getAccess_token());
			cache.put(element);
			accessToken = token.getAccess_token();
		}else{
			accessToken = element.getValue() + "";
			if(accessToken == null || accessToken.equals("")){
				Token token = TokenAPI.token(ConfConfig.getString("WXAPPID"), ConfConfig.getString("WXAPPSECRET"));
				element = new Element("accessToken", token.getAccess_token());
				cache.put(element);
				accessToken = token.getAccess_token();
			}
		}
//		String data = HttpUtil.getInstanse().httpGET("http://115.28.238.109/ruilong/appRuilong/getWxAccessToken");
//		accessToken = JSON.parseObject(data).getString("data");
		return accessToken;
	}
	
	public static void main(String[] args) {
		System.out.println(WeixinUtil.getAccessToken());
//		WeixinUtil.getAccessToken();
	}
	
	public static String getJsapiTicket(){
		Cache cache = CacheManager.getInstance().getCache("jsapiTicket");
		Element element = cache.get("jsapiTicket");
		if(element == null || element.getValue() == null){
			Ticket ticket = TicketAPI.ticketGetticket(getAccessToken());
			element = new Element("jsapiTicket", ticket.getTicket());
			cache.put(element);
			jsapiTicket = ticket.getTicket();
		}else{
			jsapiTicket = element.getValue() + "";
			if(jsapiTicket == null || jsapiTicket.equals("")){
				Ticket ticket = TicketAPI.ticketGetticket(getAccessToken());
				element = new Element("jsapiTicket", ticket.getTicket());
				cache.put(element);
				jsapiTicket = ticket.getTicket();
			}
		}
		
		return jsapiTicket;
	}
	
	public static SnsToken getSnsToken(String code){
		SnsToken snsToken = SnsAPI.oauth2AccessToken(ConfConfig.getString("WXAPPID"), ConfConfig.getString("WXAPPSECRET"), code);
		return snsToken;
	}
	
}
