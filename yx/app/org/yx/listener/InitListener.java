package org.yx.listener;

import org.yx.common.entity.PageData;
import org.yx.common.utils.Logger;
import org.yx.services.common.ICacheService;
import org.yx.util.ImageCompressUtil;

import spring.SpringContextHolder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by zhangwei on 2017/7/28.
 */

public class InitListener  implements ServletContextListener{
	
	Logger log=Logger.getLogger(this.getClass());

    public void contextInitialized(ServletContextEvent context) {
//        ServletContext context = arg0.getServletContext();
//        ApplicationContext ctx = WebApplicationContextUtils
//                .getWebApplicationContext(context);
//        ApiInterfaceMethodService apiInterfaceMethodService = (ApiInterfaceMethodService) ctx
//                .getBean("apiInterfaceMethodService");
//        articleinfoService.init();

        System.out.println("================>[InitListener]自动加载启动开始.");
        log.info("================>[InitListener]自动加载启动开始.");
        PageData pd=new PageData();
        ICacheService cacheService = SpringContextHolder.getBean("cacheService");

//        ApiInterfaceMethodService apiInterfaceMethodService = SpringContextHolder.getBean("apiInterfaceMethodService");
        System.out.println("================>[InitListener ] 加载 apiInterfaceMethodService .");
        try {
            //加载接口缓存
//            List<PageData> apis=apiInterfaceMethodService.findAllMethods();
//            ApiMethodsUtil.init(apis);
//            CacheHandler.initApiMethods(apis);
              cacheService.init();//初始化缓存
              
              //ImageCompressUtil.compressFiles("D:/UPLOAD/jianlian/uploadImg"); 


        } catch (Exception e) {
            System.out.println("InitListener  异常 ：start ");
            log.error("InitListener  异常 ：",e);
            e.printStackTrace();
            System.out.println("InitListener  异常 end ");
        }
        System.out.println("================>[InitListener]加载完成.");
        log.info("================>[InitListener]加载完成.");
//        this.initWebApplicationContext(event.getServletContext());
        // 上下文初始化执行
//        System.out.println("================>[ServletContextListener]自动加载启动开始.");
//        SpringUtil.getInstance().setContext(WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext()));
    }



    public void contextDestroyed(ServletContextEvent event) {
//        this.closeWebApplicationContext(event.getServletContext());
    }



}
