 package org.yx.common.utils;
 
 import java.net.InetAddress;
import java.net.UnknownHostException;
 
 public class PublicUtil
 {
   public static void main(String[] args)
   {
     //System.out.println("本机的ip=" + getIp());
   }
 
   public static String getPorjectPath() {
     String nowpath = "";
     nowpath = System.getProperty("user.dir") + "/";
 
     return nowpath;
   }
 
   public static String getIp()
   {
     String ip = "";
     try {
       InetAddress inet = InetAddress.getLocalHost();
       ip = inet.getHostAddress();
     }
     catch (UnknownHostException e) {
       e.printStackTrace();
     }
 
     return ip;
   }
 }

/* Location:           F:\掌上幼儿园\源码\yzy_web\WEB-INF\classes\
 * Qualified Name:     com.fh.util.PublicUtil
 * JD-Core Version:    0.6.2
 */