package org.yx.cache;

import com.alibaba.fastjson.JSONArray;

import org.apache.log4j.Logger;
import org.yx.common.entity.PageData;
import org.yx.util.ConstantUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangwei on 2017/7/30.
 */
public class CacheHandler {
    static Logger logger=Logger.getLogger(CacheHandler.class);
    //缓存表信息
    public static AppCacheVersion appCache=new AppCacheVersion();
    //接口调用方法 api  cache
    public static ApiMethodsCache apiMethodsCache =new ApiMethodsCache();
    //区域、城市缓存
    public static AreasCitiesCache areasCitiesCache=new AreasCitiesCache(); 
    //字典表缓存
    public static SysDictionariesCache sysDicCache=new SysDictionariesCache(); 
    //行业表缓存
    public static CategoryCache cateCache=new CategoryCache(); 
    //城市建联缓存
    public static ClanCache clanCache=new ClanCache();
    //项目类型缓存
    public static XmlxCache xmlxCache=new XmlxCache();
    
    
    /**缓存表缓存***************************************************************/

    /**
     * 初始化缓存表
     * @param apis
     */
    public static void initAppCache(List<PageData> apps){
        //如果已经初始化过则跳过此操作
        if(appCache.isInit()){
            return;
        }
        Map<String,PageData> mapApps=appCacheData(apps);
        appCache.init(mapApps); 
    }
    
    public static Map<String,PageData> appCacheData(List<PageData> apps){
    	 Map<String,PageData> mapApps=new ConcurrentHashMap<String,PageData>();
         for (PageData app : apps) {
         	mapApps.put(app.getString("NAME"),app);
         } 
         PageData data=new PageData();
         data.put(ConstantUtil.VERSION_APP,mapApps);//将所有的数据都放入缓存中，方便获取全部的数据
         mapApps.put(ConstantUtil.VERSION_APP,data);
         return mapApps;
    } 

    /**
     * 根据缓存编号获取缓存信息
     * @param id 缓存编号
     * @return
     */
    public static PageData getAppCache(String id){ 
        return  appCache.get(id);
    }  

    /**
     * 重新加载接口缓存
     * @param mapApis
     */
    public static void reloadAppCache(List<PageData> apps){
    	Map<String,PageData> mapApps=appCacheData(apps);
    	appCache.reload(mapApps);
    } 
    
    /**
     * 获取所有的缓存数据版本号的接口
     * @return
     */
    public static List<PageData> getAllAppCache(){
    	PageData data=appCache.get(ConstantUtil.VERSION_APP);
    	if(data!=null){
    		 return data.getList(ConstantUtil.VERSION_APP);
    	}else{
    		return null;
    	} 
    }

    /**接口缓存***************************************************************/ 
    /**
     * 初始化接口缓存
     * @param apis
     */
    public static void initApiMethods(List<PageData> apis){
        //如果已经初始化过则跳过此操作 
        if(apiMethodsCache.isInit()){
            return;
        }
        Map<String,PageData> mapApis=apiMethodsData(apis);
        apiMethodsCache.init(mapApis); 
    }
    
    public static Map<String,PageData> apiMethodsData(List<PageData> apis){
    	 Map<String,PageData> mapApis=new ConcurrentHashMap<String,PageData>();
         for (PageData api : apis) {
             mapApis.put(api.getString("id"),api);
         }
         PageData data=new PageData();
         data.put(ConstantUtil.VERSION_API,apis);
         mapApis.put(ConstantUtil.VERSION_API,data); //将所有的数据存入mapApis中，这样方便取出所有的接口方法
         return mapApis;
    }


    /**
     * 根据接口的编号获取接口
     * @param id 接口编号
     * @return
     */
    public static PageData getApiMethod(String id){
        return  apiMethodsCache.get(id);
    } 

    /**
     * 根据接口的编号获取接口
     * @return
     */
    public static List<PageData> getAllApiMethod(){
    	PageData data=apiMethodsCache.get(ConstantUtil.VERSION_API);
    	if(data!=null){
    		 return data.getList(ConstantUtil.VERSION_API);
    	}else{
    		return null;
    	} 
    }

    /**
     * 重新加载接口缓存
     * @param mapApis
     */
    public static void reloadApiMethods(List<PageData> apis){
    	Map<String,PageData> mapApis=apiMethodsData(apis);
        apiMethodsCache.reload(mapApis); 
    }

    /**区域、城市缓存***************************************************************/ 
    /**
     * 城市、区域缓存
     * @param areasCities
     */
    public static void initAreasCities(List<PageData> areasCities){
        //如果已经初始化过则跳过此操作
        boolean isInit=areasCitiesCache.isInit();
        if(isInit){
            return;
        }
        Map<String,List<PageData>> acs=areasCitiesData(areasCities);
        areasCitiesCache.init(acs); 
    }
    
    public static Map<String,List<PageData>> areasCitiesData(List<PageData> areasCities){
    	 List<PageData> areas=new ArrayList<PageData>();
         Map<String,PageData> areaCodes=new ConcurrentHashMap<String,PageData>();
         for (PageData api : areasCities) {
             String areacode=api.getString("areacode");
             String areaname =api.getString("areaname");
             String citycode=api.getString("citycode");
             String cityname =api.getString("cityname");

             //设置区域
             PageData area =new PageData();
             setArea(area,areacode,areaname);
             if(!areaCodes.containsKey(areacode)){
                 areas.add(area);
                 areaCodes.put(areacode,area);
             }else{
                 area=areaCodes.get(areacode);
             }


             //设置城市
             List<PageData> cities=new ArrayList<PageData>();
             if(area.containsKey("children")){
                 cities=(List<PageData>)area.get("children");
             }else{ 
             	area.put("children",cities);
                 /*area.put("citys",cities);*/
             }

             if(null!=citycode && !"null".equalsIgnoreCase(citycode)){
                 PageData city= new PageData();
                 setArea(city,citycode,cityname);
                 cities.add(city);
             }

         }
         Map<String,List<PageData>> acs =new ConcurrentHashMap<String,List<PageData>>();
         acs.put(ConstantUtil.VERSION_AREA,areas);
         return acs;
    }


    public static List<PageData> getAllAreas(){
        return areasCitiesCache.get("").get(ConstantUtil.VERSION_AREA);//不需要参数
    }
    
    /**
     * 重新加载接口缓存
     * @param mapApis
     */
    public static void reloadAreasCities(List<PageData> areasCities){
    	Map<String,List<PageData>> mapApis=areasCitiesData(areasCities);
    	areasCitiesCache.reload(mapApis); 
    }


    private static  void setArea(PageData area ,String code,String name){
        area.put("value",code);
        area.put("text",name);
    } 

    /**字典表缓存***************************************************************/

    /**
     * 字典表缓存
     * @param sysDics
     */
    public static void initSysDic(List<PageData> sysDics){
        //如果已经初始化过则跳过此操作
        boolean isInit=sysDicCache.isInit();
        if(isInit){
            return;
        } 
        Map<String,List<PageData>> datas=sysDicData(sysDics);
        sysDicCache.init(datas);   
    }


    public static List<PageData> getDics(String bianma){
        return sysDicCache.get(null).get(bianma);
    }
    
    public static List<PageData> getAllDics(){
    	List<PageData> dataList=sysDicCache.get(null).get(ConstantUtil.VERSION_DIC);
    	if(dataList!=null&&dataList.size()>0){
    		return dataList;
    	}
        return null;
    }
    
    public static Map<String,List<PageData>> sysDicData(List<PageData> sysDics){
    	Map<String,List<PageData>> datas=new ConcurrentHashMap<String,List<PageData>>();
        for (PageData api : sysDics) {
            String pbm=api.getString("P_BIANMA"); 
            String bm =api.getString("BIANMA");
            String name=api.getString("NAME"); 

            //设置字典表信息
            PageData dic =new PageData();
            setDic(dic,bm,name);
            
            if(!datas.containsKey(pbm)){ 
            	List<PageData> dics=new ArrayList<PageData>();
            	dics.add(dic); 
            	datas.put(pbm,dics);
            }else{ 
            	datas.get(pbm).add(dic); 
            } 
        } 
        //将所有的字典表信息存入缓存 
        datas.put(ConstantUtil.VERSION_DIC,sysDics); 
        return datas;
    }
    
    public static void reloadSysDic(List<PageData> sysDics){
    	Map<String,List<PageData>> datas=sysDicData(sysDics);
        sysDicCache.reload(datas);  
    }


    private static  void setDic(PageData dic ,String bianma,String name){
    	dic.put("value",bianma);
    	dic.put("text",name);
    } 
    
    /**行业表缓存***************************************************************/

    /**
     * 行业表缓存
     * @param cates
     */
    public static void initCate(List<PageData> cates){
        //如果已经初始化过则跳过此操作
        boolean isInit=cateCache.isInit();
        if(isInit){
            return;
        }  
        cateCache.init(cates);   
    }
    
    public static void reloadCate(List<PageData> cates){
    	cateCache.reload(cates); 
    } 

    public static List<PageData> getAllCates(){
        return cateCache.get("");
    }  
    
    /**城市建联缓存***************************************************************/

    /**
     * 城市建联缓存
     * @param clans
     */
    public static void initClan(List<PageData> clans){
        //如果已经初始化过则跳过此操作
        boolean isInit=clanCache.isInit();
        if(isInit){
            return;
        } 
        Map<String,List<PageData>> datas=clanData(clans);
        clanCache.init(datas);  
    } 
    
    public static Map<String,List<PageData>> clanData(List<PageData> clans){
    	List<PageData> clanList=new ArrayList<PageData>();
        Map<String,PageData> clanMap=new ConcurrentHashMap<String,PageData>();
        for (PageData data : clans) {
            String pid=data.getString("AREACODE");
            String pname =data.getString("AREANAME");
            String id=data.getString("ID");
            String name =data.getString("NAME");

            //设置一级项目类型
            PageData clan =new PageData();
            setXmlx(clan,pid,pname);
            if(!clanMap.containsKey(pid)){
            	clanList.add(clan);
            	clanMap.put(pid,clan);
            }else{
            	clan=clanMap.get(pid);
            } 
            //设置二级分类
            List<PageData> secClans=new ArrayList<PageData>();
            if(clan.containsKey("children")){
            	secClans=(List<PageData>)clan.get("children");
            }else{ 
            	clan.put("children",secClans); 
            } 
            if(null!=id && !"null".equalsIgnoreCase(id)){
                PageData c= new PageData();
                setXmlx(c,id,name);
                secClans.add(c);
            } 
        }
        Map<String,List<PageData>> datas =new ConcurrentHashMap<String,List<PageData>>();
        datas.put(ConstantUtil.VERSION_CLAN,clanList);
        return datas;
    }

    public static List<PageData> getAllClans(){
        return clanCache.get("").get(ConstantUtil.VERSION_CLAN);
    } 
    
    public static void reloadClan(List<PageData> clans){
    	Map<String,List<PageData>> datas=clanData(clans);
    	clanCache.reload(datas);
    }
    
    /**项目类型表缓存***************************************************************/

    /**
     * 项目类型表缓存
     * @param xmlxs
     */
    public static void initXmlxs(List<PageData> xmlxs){
        //如果已经初始化过则跳过此操作
        boolean isInit=xmlxCache.isInit();
        if(isInit){
            return;
        } 
        Map<String,List<PageData>> datas=xmlxsData(xmlxs);
        xmlxCache.init(datas);  
    }
    
    public static Map<String,List<PageData>> xmlxsData(List<PageData> xmlxs){
    	List<PageData> firLx=new ArrayList<PageData>();
        Map<String,PageData> lxs=new ConcurrentHashMap<String,PageData>();
        for (PageData api : xmlxs) {
            String pid=api.getString("P_ID");
            String pname =api.getString("P_NAME");
            String id=api.getString("ID");
            String name =api.getString("NAME");

            //设置一级项目类型
            PageData lx =new PageData();
            setXmlx(lx,pid,pname);
            if(!lxs.containsKey(pid)){
            	firLx.add(lx);
            	lxs.put(pid,lx);
            }else{
            	lx=lxs.get(pid);
            } 
            //设置二级分类
            List<PageData> secLx=new ArrayList<PageData>();
            if(lx.containsKey("children")){
            	secLx=(List<PageData>)lx.get("children");
            }else{ 
            	lx.put("children",secLx); 
            } 
            if(null!=id && !"null".equalsIgnoreCase(id)){
                PageData xmlx= new PageData();
                setXmlx(xmlx,id,name);
                secLx.add(xmlx);
            } 
        }
        Map<String,List<PageData>> datas =new ConcurrentHashMap<String,List<PageData>>();
        datas.put(ConstantUtil.VERSION_XMLX,firLx);
        return datas;
    }
    
    public static void reloadXmlxs(List<PageData> xmlxs){
    	Map<String,List<PageData>> datas=xmlxsData(xmlxs);
    	xmlxCache.reload(datas);
    }


    public static List<PageData> getXmlx(){
        return xmlxCache.get("").get(ConstantUtil.VERSION_XMLX);
    }


    private static  void setXmlx(PageData lx ,String value,String text){
    	lx.put("value",value);
    	lx.put("text",text);
    } 
   
    public static int getLocalVersion(String key){
    	PageData rescode=appCache.get(key);
    	int code=1;
    	if(rescode!=null){
    		code=Integer.parseInt(rescode.get("VERSION").toString());
    	}
    	return code;
    } 
    
    public static boolean getCompareVersion(String key,PageData data){
    	if(data==null){
    		return getLocalVersion(key)==0?true:false;
    	} 
    	Object code=data.get(key);
    	if(code==null||"".equals(code)){
    		return getLocalVersion(key)==0?true:false;
    	}
    	return (getLocalVersion(key)-Integer.parseInt(code.toString()))==0?true:false; 
    } 
}
