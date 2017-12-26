package org.yx.services.jlq.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource; 

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;   
import org.yx.services.jlq.inter.AppMediaServiceInter; 

import com.alibaba.fastjson.JSONArray;

@Service("appMediaService")
public class AppMediaService implements AppMediaServiceInter{ 
	
	Logger log=Logger.getLogger(this.getClass());

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 查看精选首页
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryJxIndexInfo(PageData pd) throws Exception {
		long startTime=System.currentTimeMillis();
		PageData result = new PageData(); 
		if(!EmptyUtil.isNullOrEmpty(pd)){  
			log.info("service 【appMediaService】 接口 【queryJxIndexInfo】 参数："+JSONArray.toJSONString(pd).toString());
			 
			pd.put("LOCATION_NO","10");
		    List<PageData> firRots=(List<PageData>) this.dao.findForList("AppRotationMapper.queryByColno",pd);//获取资讯页面顶部的轮换大图
		   
		    pd.put("LOCATION_NO","11");
		    List<PageData> secRots=(List<PageData>) this.dao.findForList("AppRotationMapper.queryByColno",pd);//获取资讯页面中间的轮换大图
		    
		    pd.put("LOCATION_NO","02_00");
		    List<PageData> firMeds=(List<PageData>) this.dao.findForList("AppMediaMapper.queryByCategory", pd);//查询热点新闻
		    
		    pd.put("LOCATION_NO","02_02");
		    List<PageData> secMeds=(List<PageData>) this.dao.findForList("AppMediaMapper.queryByCategory", pd);//查询建筑新闻
		    
		    result.put("firRots",firRots);
		    result.put("secRots",secRots); 
		    result.put("firMeds",firMeds);
		    result.put("secMeds",secMeds);
		    long lastTime=System.currentTimeMillis();
		    lastTime=lastTime-startTime;
			log.info("queryJxIndexInfo接口总消耗时间为："+lastTime+"毫秒，共计："+lastTime/1000+"秒！！！！！");  
			return JSONArray.toJSONString(result).toString();
		}
		return null;  
	}
	
	/**
	 * 查看资讯首页头部轮换大图
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryMediaRotations(PageData pd)throws Exception{  
		if(!EmptyUtil.isNullOrEmpty(pd)){   
			pd.put("LOCATION_NO","03");
		    List<PageData> rotations=(List<PageData>) this.dao.findForList("AppRotationMapper.queryByColno", pd);//获取资讯页面顶部的轮换大图
			return JSONArray.toJSONString(rotations).toString();
		}
		return null;
	}  
	 
	/**
	 * 查询资讯首页的新闻列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryMediaIndexByParam(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		log.info("queryMediaIndexByParam 方法的参数为："+JSONArray.toJSONString(data).toString());
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
			Object mors=data.get("MORS");
			if(mors == null || mors==""){
				data.put("MORS",2); 
			}
		    List<PageData> medias =(List<PageData>) dao.findForList("AppMediaMapper.queryPageByParam",data);
			for(PageData media:medias){
				 List<PageData> imgs =(List<PageData>) dao.findForList("MediaPictureMapper.queryByParam",media);
				 media.put("imgs",imgs);
			}
			log.info("queryMediaIndexByParam 方法的返回值数量为："+JSONArray.toJSONString(medias.size()).toString());
		    return JSONArray.toJSONString(medias).toString();
		}
		return null; 
	} 
	
	/**
	 * 根据ID查询新闻详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryById(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData media =(PageData) this.dao.findForObject("AppMediaMapper.queryById",data); 
		    data.put("OBJECT_ID",data.getString("MEDIA_ID"));
			data.put("TYPE","JL_APP_MEDIA"); 
			PageData page =(PageData) this.dao.findForObject("AppCollectionMapper.checkCollection",data);//判断是否收藏
			if(page!=null&&media!=null){
				media.put("isCollect",1);//已经收藏
			}else{
				media.put("isCollect",0);//未收藏
			} 
			media.put("appurls", "http://jianlian.shanghai-cu.com/jianlian");
			return JSONArray.toJSONString(media).toString(); 
		}
		return null;  
	}
	
	/**
	 * 根据新闻ID查询新闻评论
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryMediaComments(PageData pd) throws Exception {
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
		    List<PageData> comments =(List<PageData>) dao.findForList("AppCommentsMapper.queryPageByParam",data);
		    PageData comment=(PageData) dao.findForObject("AppCommentsMapper.queryCounts",data); 
		    PageData res=new PageData();
		    if(comment!=null){
            	res.put("count",comment.get("counts").toString());
            }else{
            	res.put("count","0");
            } 
            res.put("comments",comments);
			return JSONArray.toJSONString(res).toString();
		}
		return null; 
	}
	
	/**
	 * 新闻添加评论
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addComments(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			String id=UuidUtil.get32UUID();
			data.put("ID", id);
			data.put("COMMENT_DATE", new Date()); 
			/*String CONTENT=data.getString("CONTENT");
			if ((CONTENT != null) && (!"".equals(CONTENT))) { 
				CONTENT=new String(CONTENT.getBytes("ISO-8859-1"),"UTF-8").trim();
				data.put("CONTENT",CONTENT);
			}*/
			PageData comment=(PageData) dao.findForObject("AppCommentsMapper.queryCounts",data); 
			if(comment!=null){
				int count= Integer.parseInt(comment.get("counts").toString());
				data.put("IDENTIFICATION",count+1);
			}else{
				data.put("IDENTIFICATION",1);
			} 
			Object obj=dao.save("AppCommentsMapper.save",data);
			if(Integer.valueOf(obj.toString()) >= 1){ 
				PageData media=new PageData();
				media.put("COMMENT_COUNT",Integer.valueOf(obj.toString()));
				media.put("MEDIA_ID",data.getString("MEDIA_ID"));  
				Object ob =dao.update("AppMediaMapper.updateCounts",media);
				if(Integer.valueOf(ob.toString()) >= 1){
					PageData res=new PageData();
					 res.put("code","200");
					 return JSONArray.toJSONString(res).toString(); 
				}else{
					return null;  
				} 
			}else{
				return null;
			} 
		}   
		return null;  
	}  
	 
	/**
	 * 新闻添加分享
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String addShare(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){   
			 data.put("COMMENT_COUNT",1); 
			 Object ob =dao.update("AppMediaMapper.updateCounts",data);
			 if(Integer.valueOf(ob.toString()) >= 1){
				 PageData res=new PageData();
				 res.put("code","200");
				 return JSONArray.toJSONString(res).toString(); 
			 }else{
				return null;  
			 }  
		}   
		return null;  
	}  
	
	/**
	 * 修改新闻查看次数
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String addView(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){   
			 data.put("VIEW_COUNT",1); 
			 Object ob =dao.update("AppMediaMapper.updateCounts",data);
			 if(Integer.valueOf(ob.toString()) >= 1){
				 PageData res=new PageData();
				 res.put("code","200");
				 return JSONArray.toJSONString(res).toString(); 
			 }else{
				return null;  
			 }  
		}   
		return null;  
	} 
	
	/**
	 * 查看视频列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryVedios(PageData pd) throws Exception{
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
			List<PageData> vedios=null; 
			try{
				vedios =(List<PageData>) dao.findForList("AppVedioMapper.queryList",data);//查询视频列表	 
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(vedios).toString();
		}
		return null;  
	}
	
	
		 
	//媒体首页新闻 
	public List<PageData> queryHot() throws Exception {
		return (List<PageData>) this.dao.findForList("AppMediaMapper.queryHot",null);
	} 
		
	//根据标题模糊查询
	public List<PageData> findMediaByTitle(PageData pd)throws Exception {
		return (List<PageData>) this.dao.findForList("AppMediaMapper.findMediaByTitle", pd);
	}
	
	//根据ID查询新闻详情
	public PageData queryShareById(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("AppMediaMapper.queryById", pd);
	}
}

