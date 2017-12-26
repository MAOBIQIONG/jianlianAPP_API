package org.yx.util;

import org.yx.common.entity.PageData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
/**
 * Created by zhangwei on 2017/7/28.
 */
public  class   ApiMethodsUtil {

   public static Map<String,PageData> mapApis=new HashMap<String,PageData>();


   public static void init(List <PageData> apis){
      for (PageData api : apis) {
         mapApis.put(api.getString("id"),api);
      }
      System.out.println("+++++++++++++++++++++++++++++ 共有  ："+mapApis.size()+" 个接口 ******");
   }
   public static PageData getMethod(String id){
     return  mapApis.get(id);
   }


}
