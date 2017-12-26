package org.yx.common.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * 自定义的指定Shiro验证用户登录的类
 * @author andy
 * @date 2016-10-08
 * 
 */
public class MyRelam extends AuthorizingRealm{
	
	/**
	 * 为当前登录的用户授予角色与权限
	 * 访问场景: ① 需要授权资源时访问,② 经测试:并且每次访问需授权资源时都会执行该方法中的逻辑,
	 * 这表明本例中默认并未启用AuthorizationCache 
	 * 运行场景: 进行权限判断时运行此方法
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();  
	    String currentUsername = (String)super.getAvailablePrincipal(principals);  	//当前登录名
	    if(null!=currentUsername && "admin".equals(currentUsername)){  
            //添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色    
            simpleAuthorInfo.addRole("admin");  
            //添加权限  
            simpleAuthorInfo.addStringPermission("admin:manage");  
            System.out.println("已为用户[andy]赋予了[admin]角色和[admin:manage]权限");  
            return simpleAuthorInfo;  
        }else if(null!=currentUsername && "andy".equals(currentUsername)){  
            System.out.println("当前用户[admin]无授权");  
            return simpleAuthorInfo;  
        }  
        //若该方法什么都不做直接返回null的话,就会导致任何用户访问/admin/listUser.jsp时都会自动跳转到unauthorizedUrl指定的地址  
        //详见applicationContext.xml中的<bean id="shiroFilter">的配置  
        return null;  
	}

	/**
	 * 验证当前登录的Subject
	 * 本例中该方法的调用时机为LoginController.login()方法中执行Subject.login()时 
	 * 运行场景:   该方法在subject.login()运行时会运行到这里
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken)authcToken;  
		 if("admin".equals(token.getUsername())){  
	            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo("admin", "admin", this.getName());  
	            setSession("currentUser", "admin");  
	            return authcInfo;  
	        }else if("andy".equals(token.getUsername())){  
	            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo("andy", "andy", this.getName());  
	            setSession("currentUser", "andy");  
	            return authcInfo;  
	        }  
	        //没有返回登录用户名对应的SimpleAuthenticationInfo对象时,就会在LoginController中抛出UnknownAccountException异常  
	        return null;  
	}
	
	 /** 
     * 将一些数据放到ShiroSession中,以便于其它地方使用 
     * @see 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到 
     */  
    private void setSession(Object key, Object value){  
        Subject currentUser = SecurityUtils.getSubject();  
        if(null != currentUser){  
            Session session = currentUser.getSession();  
            System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");  
            if(null != session){  
                session.setAttribute(key, value);  
            }  
        }  
    }  
}
