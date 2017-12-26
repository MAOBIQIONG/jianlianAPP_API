 package org.yx.common.utils;
 
 import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.yx.common.entity.Menu;
 
 public class Jurisdiction
 {
   public static boolean hasJurisdiction(String menuUrl)
   {
     Subject currentUser = SecurityUtils.getSubject();
     Session session = currentUser.getSession();
     @SuppressWarnings("unused")
	Boolean b = Boolean.valueOf(true);
     List<?> menuList = (List<?>)session.getAttribute("allmenuList");
 
     for (int i = 0; i < menuList.size(); i++) {
       for (int j = 0; j < ((Menu)menuList.get(i)).getSubMenu().size(); j++) {
         if (((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).getMENU_URL().split(".do")[0].equals(menuUrl.split(".do")[0])) {
           if (!((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).isHasMenu()) {
             return false;
           }
           @SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>)session.getAttribute("QX");
           map.remove("add");
           map.remove("del");
           map.remove("edit");
           map.remove("cha");
           map.remove("sh");
           String MENU_ID = ((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).getMENU_ID();
           String USERNAME = session.getAttribute("USERNAME").toString();
           Boolean isAdmin = Boolean.valueOf("admin".equals(USERNAME));
           map.put("add", (RightsHelper.testRights((String)map.get("adds"), MENU_ID)) || (isAdmin.booleanValue()) ? "1" : "0");
           map.put("del", (RightsHelper.testRights((String)map.get("dels"), MENU_ID)) || (isAdmin.booleanValue()) ? "1" : "0");
           map.put("edit", (RightsHelper.testRights((String)map.get("edits"), MENU_ID)) || (isAdmin.booleanValue()) ? "1" : "0");
           map.put("cha", (RightsHelper.testRights((String)map.get("chas"), MENU_ID)) || (isAdmin.booleanValue()) ? "1" : "0");
           map.put("sh", (RightsHelper.testRights((String)map.get("shs"), MENU_ID)) || (isAdmin.booleanValue()) ? "1" : "0");
           session.removeAttribute("QX");
           session.setAttribute("QX", map);
         }
       }
     }
 
     return true;
   }
 
   public static boolean buttonJurisdiction(String menuUrl, String type)
   {
     Subject currentUser = SecurityUtils.getSubject();
     Session session = currentUser.getSession();
     Boolean b = Boolean.valueOf(true);
     List menuList = (List)session.getAttribute("allmenuList");
 
     for (int i = 0; i < menuList.size(); i++) {
       for (int j = 0; j < ((Menu)menuList.get(i)).getSubMenu().size(); j++) {
         if (((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).getMENU_URL().split(".do")[0].equals(menuUrl.split(".do")[0])) {
           if (!((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).isHasMenu()) {
             return false;
           }
           Map map = (Map)session.getAttribute("QX");
           String MENU_ID = ((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).getMENU_ID();
           String USERNAME = session.getAttribute("USERNAME").toString();
           Boolean isAdmin = Boolean.valueOf("admin".equals(USERNAME));
           if ("add".equals(type))
             return (RightsHelper.testRights((String)map.get("adds"), MENU_ID)) || (isAdmin.booleanValue());
           if ("del".equals(type))
             return (RightsHelper.testRights((String)map.get("dels"), MENU_ID)) || (isAdmin.booleanValue());
           if ("edit".equals(type))
             return (RightsHelper.testRights((String)map.get("edits"), MENU_ID)) || (isAdmin.booleanValue());
           if ("cha".equals(type)) {
             return (RightsHelper.testRights((String)map.get("chas"), MENU_ID)) || (isAdmin.booleanValue());
           }
         }
       }
     }
 
     return true;
   }
 }

/* Location:           F:\掌上幼儿园\源码\yzy_web\WEB-INF\classes\
 * Qualified Name:     com.fh.util.Jurisdiction
 * JD-Core Version:    0.6.2
 */