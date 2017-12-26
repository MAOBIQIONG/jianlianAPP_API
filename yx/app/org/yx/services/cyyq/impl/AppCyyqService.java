package org.yx.services.cyyq.impl;

import java.sql.SQLException;
import java.util.Date; 
import java.util.List; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil; 
import org.yx.common.utils.UuidUtil;
import org.yx.services.cyyq.inter.AppCyyqServiceInter; 
import org.yx.util.ConstantUtil;
import org.yx.util.DataConvertUtil;
import org.yx.util.noticeConfig;
import org.yx.util.noticePushutil;

import com.alibaba.fastjson.JSONArray;   

@Service("appCyyqService")
public class AppCyyqService implements AppCyyqServiceInter{


	@Resource(name = "daoSupport")
	private DaoSupport dao;  
	
	public static String noticeText18 = noticeConfig.getString("noticeText18");
	/**
	 * 查询招商信息（分页）
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public String queryZslist(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("pageSize",pageSize);
			} 
			List<PageData> products=null;
			PageData conet=new PageData();
			try{
				products =(List<PageData>) dao.findForList("AppCyyqMapper.queryzslist",data);//查询政府招商项目列表		 
				conet.put("products",products); 
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(conet).toString();
		}
		return null;  
	} 
	
	/**
	 * 根据项目id查询园区详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryyqxq(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData cont =(PageData) this.dao.findForObject("AppCyyqMapper.queryyqxq",data); 
		    List<PageData> imgs =(List<PageData>) this.dao.findForList("AppCyyqMapper.queryMyZSFBimg",data); 
		    PageData phone =(PageData) this.dao.findForObject("ServiceTelMapper.findBm","05");//查询产业园的客服电话
		    PageData xiangqi=new PageData();
		    xiangqi.put("cont", cont);
		    xiangqi.put("imgs", imgs);
		    xiangqi.put("phone", phone);
			return JSONArray.toJSONString(xiangqi).toString(); 
		}
		return null;   
	}
	
	/**
	 * 查询园区信息用于修改
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryYqInfo(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			try{
				PageData park=new PageData();
				if(data.getString("IN_ID")!=null&&!"".equals(data.getString("IN_ID"))){
					park =(PageData) dao.findForObject("AppCyyqMapper.queryYqInfo",data);//查询我是园区的详细信息用于修改
					List<PageData> imgs =(List<PageData>) this.dao.findForList("AppCyyqMapper.queryMyZSFBimg",data); //查询园区的图片
					park.put("imgs",imgs);
				} 
				List<PageData> types =(List<PageData>) this.dao.findForList("AppDicMapper.queryByBM","park_type"); //查询园区类型
				List<PageData> levels =(List<PageData>) this.dao.findForList("AppDicMapper.queryByBM","Industrial_Park_level"); //查询园区级别
				park.put("types",types);
				park.put("levels",levels);
				return JSONArray.toJSONString(park).toString();
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		}
		return null;  
	}
	
	
	/**
	 * 查询园区信息用于修改（信息）
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewYqInfo(PageData pd){
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			try{
				PageData park=new PageData();
				if(data.getString("IN_ID")!=null&&!"".equals(data.getString("IN_ID"))){
					park =(PageData) dao.findForObject("AppCyyqMapper.queryYqInfo",data);//查询我是园区的详细信息用于修改
					List<PageData> imgs =(List<PageData>) this.dao.findForList("AppCyyqMapper.queryMyZSFBimg",data); //查询园区的图片
					park.put("imgs",imgs);
				} 
				
				//比较app端和服务器的缓存
				boolean version=CacheHandler.getCompareVersion(ConstantUtil.VERSION_DIC, data);
				
				//查询园区类型和查询园区级别
				DataConvertUtil.getDicRes(version,new String[]{"park_type","Industrial_Park_level"},park);
				
				return JSONArray.toJSONString(park).toString();
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		}
		return null;  
	}
	
	/**
	 * 我是园区信息发布+修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editZsfb(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
	        data.put("STATUS","01");
	        if(data.getString("IN_ID")!=null&&!"".equals(data.getString("IN_ID"))){//修改  
	    	   data.put("MODIFY_DATE",new Date());
	    	   Object obj=dao.update("AppCyyqMapper.editWsyq",data);
	    	   if(Integer.parseInt(obj.toString())>=1){ 
	    		    int mun=0;
					List<PageData> imgs=data.getList("imgs");
					PageData delImg=new PageData();
					delImg.put("IN_ID",data.getString("IN_ID"));
					List<PageData> is =(List<PageData>) this.dao.findForList("AppCyyqMapper.queryMyZSFBimg",delImg);//判断是否存在图片 
					Object object=0;
					if(is!=null&&is.size()>0){
					    object = this.dao.delete("AppCyyqMapper.deleteYqImgs",delImg); //删除项目的图片
					}else{
						object=1;
					} 
					if(Integer.valueOf(object.toString()) >= 1){
						for(PageData img:imgs){ 
							img.put("ID",UuidUtil.get32UUID());
							img.put("PRO_ID",data.getString("IN_ID"));
							img.put("CREATE_DATE",new Date());
							if(img.getString("IMG_PATH").contains("{")){
								PageData path=(PageData) img.get("IMG_PATH");
								img.put("IMG_PATH",path.getString("result")); 
							} 
							Object ob=dao.save("AppCyyqMapper.saveZsimg",img); 
							mun+=Integer.valueOf(ob.toString()); 
						 } 
						 if(mun==imgs.size()){  
		 					 PageData res=new PageData();
							 res.put("code","200");
							 res.put("IN_ID",data.getString("IN_ID"));
							 return JSONArray.toJSONString(res).toString();    
			 			  }else{
			 				 return null;  
			 			  }   
						}
	    	   		}
	    	   }else{
		           String id=UuidUtil.get32UUID();
				   data.put("IN_ID", id);   
		    	   data.put("CREATE_DATE",new Date());
		    	   Object obj=dao.update("AppCyyqMapper.saveZsFb",data);
		    	   if(Integer.parseInt(obj.toString())>=1){ 
		    		    int mun=0;
						List<PageData> imgs=data.getList("imgs");  
						for(PageData img:imgs){ 
							img.put("ID",UuidUtil.get32UUID());
							img.put("PRO_ID",data.getString("IN_ID"));
							img.put("CREATE_DATE",new Date());
							if(img.getString("IMG_PATH").contains("{")){
								PageData path=(PageData) img.get("IMG_PATH");
								img.put("IMG_PATH",path.getString("result")); 
							} 
							Object ob=dao.save("AppCyyqMapper.saveZsimg",img); 
							mun+=Integer.valueOf(ob.toString()); 
						} 
						if(mun==imgs.size()){  
							//获取notice.properties noticeText18内容。
			 			    String vv3=new String(noticeText18.getBytes("ISO-8859-1"),"UTF-8");
					    	String notice1=vv3;
			    	        //实时更新后台管理消息
			    	 		noticePushutil notutil=new noticePushutil();
			    	        notutil.toNotice(notice1);
							
		 					PageData res=new PageData();
							res.put("code","200");
							res.put("IN_ID",data.getString("IN_ID"));
							return JSONArray.toJSONString(res).toString();    
			 			}else{
			 				return null;  
			 			}   
					}else{
		 				return null;  
		 			}
		        }
	        } 
	    return null;  
	}  
	
	/**
	 * 我发布的园区信息（分页）
	 * 分页显示
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querymyfblist(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
				data.put("start", 0);
				data.put("pageSize",10);
			}else{
				int currentPage =Integer.valueOf(pageNo.toString());
				int pageSize=Integer.valueOf(pageSiZe.toString());
				int start=(currentPage-1)*pageSize;
				data.put("start",start);
				data.put("pageSize",pageSize);
			} 
			List<PageData> products=null;
			try{
				products =(List<PageData>) dao.findForList("AppCyyqMapper.queryMyZSFB",data);//查询产业园区项目列表
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(products).toString();
		}
		return null;  
	} 
	
	/**
	 * 园区信息删除
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String delete(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			try{
				Object ob =dao.update("AppCyyqMapper.delete",data);//删除产业园区的信息
				if(Integer.parseInt(ob.toString())>=1){  
 					PageData res=new PageData();
					res.put("code","200"); 
					return JSONArray.toJSONString(res).toString();    
	 			}else{
	 				PageData res=new PageData();
					res.put("code","400"); 
					return JSONArray.toJSONString(res).toString();  
	 			}   
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		}
		return null;  
	}
}
