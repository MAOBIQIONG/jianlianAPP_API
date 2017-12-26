 package mail;
 
import java.util.Date;
import java.util.Properties;  

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.yx.common.base.BaseController;
import org.yx.common.utils.Logger;

import com.alibaba.fastjson.JSONArray;
 
 public class SimpleMailSender extends BaseController{
	public Logger log = Logger.getLogger(getClass());
	 
   public boolean sendTextMail(MailSenderInfo mailInfo) throws Exception {
	 logBefore(this.logger, "SimpleMailSender","sendTextMail",JSONArray.toJSONString(mailInfo).toString());  
     MyAuthenticator authenticator = null;
     Properties pro = mailInfo.getProperties();
     
     if (mailInfo.isValidate()){
       authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
     }
     
     Session sendMailSession = Session.getInstance(pro, authenticator);  
     
     log.info("构造一个发送邮件的session");
     Message mailMessage = new MimeMessage(sendMailSession);
 
     Address from = new InternetAddress(mailInfo.getFromAddress());
 
     mailMessage.setFrom(from);
 
     Address to = new InternetAddress(mailInfo.getToAddress());
     mailMessage.setRecipient(Message.RecipientType.TO, to);
 
     mailMessage.setSubject(mailInfo.getSubject());
 
     mailMessage.setSentDate(new Date());
 
     String mailContent = mailInfo.getContent();
     mailMessage.setText(mailContent);
 
     Transport.send(mailMessage);//
     logAfter(this.logger,"SimpleMailSender","sendTextMail", "发送成功！");
     return true;
   }
 
   public boolean sendHtmlMail(MailSenderInfo mailInfo) throws Exception {
     MyAuthenticator authenticator = null;
     Properties pro = mailInfo.getProperties();
 
     if (mailInfo.isValidate()) {
       authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
     }  
     //Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
     Session sendMailSession = Session.getInstance(pro, authenticator); 
     log.info("构造一个发送邮件的session");
     Message mailMessage = new MimeMessage(sendMailSession);
 
     Address from = new InternetAddress(mailInfo.getFromAddress());
 
     mailMessage.setFrom(from);
 
     Address to = new InternetAddress(mailInfo.getToAddress());
 
     mailMessage.setRecipient(Message.RecipientType.TO, to);
 
     mailMessage.setSubject(mailInfo.getSubject());
 
     mailMessage.setSentDate(new Date());
 
     Multipart mainPart = new MimeMultipart();
 
     BodyPart html = new MimeBodyPart();
 
     html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
     mainPart.addBodyPart(html);
 
     mailMessage.setContent(mainPart);
 
     Transport.send(mailMessage);
     logAfter(this.logger,"SimpleMailSender","sendTextMail", "发送成功！");
     return true;  
   }
 
   public boolean sendMail(String title, String content, String type, String tomail)  throws Exception  {
     MailSenderInfo mailInfo = new MailSenderInfo();
     mailInfo.setMailServerHost("smtp.163.com");
     //mailInfo.setMailServerHost("smtp.qq.com");
     mailInfo.setMailServerPort("25");
     mailInfo.setValidate(true);
    /* mailInfo.setUserName("18827526631@163.com");
     mailInfo.setPassword("123456hzh");
     mailInfo.setFromAddress("18827526631@163.com"); */
     mailInfo.setUserName("shanghaijianlian@163.com");
     mailInfo.setPassword("shanghaicu2016");//tuqdmszccmsudege
     mailInfo.setFromAddress("shanghaijianlian@163.com"); 
     mailInfo.setToAddress(tomail);
     mailInfo.setSubject(title);
     mailInfo.setContent(content);  
 
     SimpleMailSender sms = new SimpleMailSender();
 
     if ("1".equals(type))
       return sms.sendTextMail(mailInfo);
     if ("2".equals(type)) {
       return sms.sendHtmlMail(mailInfo);
     }
     return false;
   }
 
   public static void sendEmail(String SMTP, String PORT, String EMAIL, String PAW, String toEMAIL, String TITLE, String CONTENT, String TYPE) throws Exception{
     MailSenderInfo mailInfo = new MailSenderInfo();
 
     mailInfo.setMailServerHost(SMTP);
     mailInfo.setMailServerPort(PORT);
     mailInfo.setValidate(true);
     mailInfo.setUserName(EMAIL);
     mailInfo.setPassword(PAW);
     mailInfo.setFromAddress(EMAIL);
     mailInfo.setToAddress(toEMAIL);
     mailInfo.setSubject(TITLE);
     mailInfo.setContent(CONTENT);
 
     SimpleMailSender sms = new SimpleMailSender();
 
     if ("1".equals(TYPE))
       sms.sendTextMail(mailInfo);
     else
       sms.sendHtmlMail(mailInfo);
   }
 
   public static void main(String[] args) {
    /* MailSenderInfo mailInfo = new MailSenderInfo();
     mailInfo.setMailServerHost("smtp.qq.com");
     mailInfo.setMailServerPort("25");
     mailInfo.setValidate(true);
     mailInfo.setUserName("864850150@qq.com");
     mailInfo.setPassword("comeon951118516");
     mailInfo.setFromAddress("864850150@qq.com");
     mailInfo.setToAddress("3170438191@qq.com");
     mailInfo.setSubject("设置邮箱标题");
     mailInfo.setContent("设置邮箱内容");*/
 
     SimpleMailSender sms = new SimpleMailSender(); 
     Logger logg = Logger.getLogger(sms.getClass());
     try {
    	 String str=null;
    	 str.substring(0,10);
    	/* String link="<a href='http://192.168.10.80:8080/jianlian/appLogin/findPassword'>重置密码</a>";
    	 String content="如果你想继续重置密码，请点击如下链接："+link;
    	 //msg.setContent("<a href=\"http://www.google.cn\">谷歌</a>","text/html;charset=gb2312"); 
		sms.sendMail("上海建联",content,"2","864850150@qq.com"); */
	} catch (Exception e) {
		logg.error("错误：",e);
		System.out.println("error:"+e);
		e.printStackTrace();
	}
   }
 }