 package org.yx.common.interceptor;
 
 import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.yx.common.entity.User;
import org.yx.common.utils.Const;
import org.yx.common.utils.Jurisdiction;

 
 public class LoginHandlerInterceptor extends HandlerInterceptorAdapter{
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
	   
     String path = request.getServletPath();
     if (path.matches(".*/((login)|(logout)|(code)|(app)|(quartz)|(weixin)|(static)|(district)|(sell)|(store)|(position)|(HUI)|(images)|(websocket)|(main)|(websocket)).*")) {
       return true;
     }
     
     Subject currentUser = SecurityUtils.getSubject();
     Session session = currentUser.getSession();
     User user = (User)session.getAttribute("sessionUser");
//     if (user != null) {
//       path = path.substring(1, path.length());
//       boolean b = Jurisdiction.hasJurisdiction(path);
//       if (!b) {
//         response.sendRedirect(request.getContextPath() + Const.NOACESS);
//       }
//       return b;
//     } 
     response.sendRedirect(request.getContextPath() + Const.LOGIN);
     return false;
   }
 }