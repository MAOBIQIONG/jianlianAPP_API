package org.yx.services.common;

import java.util.Date;
import java.util.List; 

import javax.annotation.Resource;  

import mail.SimpleMailSender;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;    
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData; 
import org.yx.common.utils.EmptyUtil;

import com.alibaba.fastjson.JSONArray;  

@Service("appSendMailService")
public class AppSendMailService { 
	 
	@Resource(name = "daoSupport")
	private DaoSupport dao;  
	
	Logger log=Logger.getLogger(this.getClass());//日志信息

	/**
	 * 发送邮件
	 * @return
	 * @throws Exception
	 */ 
	public String sendMail(PageData pd) throws Exception {
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){  
			try{
				String email=data.getString("EMAIL");
				List<PageData> lists=(List<PageData>) dao.findForList("AppUsersMapper.queryByEmail",data);
				if(lists!=null&&lists.size()>0){
					SimpleMailSender sms = new SimpleMailSender();
					long time=(new Date()).getTime(); 
					String link="<a href='http://jianlian.shanghai-cu.com/jianlian/appLogin/findPassword？times="+time+"'>重置密码</a>"; 
					String content="如果你想继续重置密码，请点击如下链接："+link+",此链接有效期为一天，过期无效！";
					sms.sendMail("上海建联",content,"2",email);
					PageData res=new PageData();
					res.put("code","200");
					res.put("msg","邮件发送成功，请注意查看！");
					return JSONArray.toJSONString(res).toString();  
				} else{//该邮箱没有绑定
					PageData res=new PageData();
					res.put("code","400");
					res.put("msg","此邮箱没有绑定，请绑定邮箱或者使用手机号修改密码！");
					return JSONArray.toJSONString(res).toString();  
				}  
			}catch(Exception e){
				e.printStackTrace();
				PageData res=new PageData();
				res.put("code","400");
				res.put("msg","邮件发送失败！");
				return JSONArray.toJSONString(res).toString();
			}
		}
		log.info("service:【appSendMailService】 function：【sendMail】 result:【data is isNullOrEmpty】");
        return null;		
	} 
	 
    /**
	 * 发送邮箱验证码 
	 * @return
	 * @throws Exception
	 */ 
	public String sendMailCode(PageData pd) throws Exception {
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){  
			try{
				String email=data.getString("EMAIL");
				if(email!=null&&!"".equals(email)){
					SimpleMailSender sms = new SimpleMailSender();
					int code=(int)((Math.random()*9+1)*1000);
					String content=code+"";  
					sms.sendMail("上海建联",content,"1",email);
					System.out.println("邮箱发送成功！验证码："+code);
					PageData res=new PageData();
					res.put("code","200");
					res.put("mailCode",code);
					return JSONArray.toJSONString(res).toString();  
				}else{
					return null;  
				}  
			}catch(Exception e){
				e.printStackTrace();
				return null;  
			}
		}
        return null;		
	} 
}
