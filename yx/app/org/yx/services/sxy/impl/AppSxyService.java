package org.yx.services.sxy.impl;

import java.sql.SQLException;
import java.util.Date; 
import java.util.List; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport; 
import org.yx.common.entity.PageData;
import org.yx.common.utils.ChineseToEnglish;
import org.yx.common.utils.EmptyUtil; 
import org.yx.common.utils.UuidUtil;
import org.yx.services.AppCommonService;
import org.yx.services.ppp.inter.AppPppServiceInter;
import org.yx.services.sxy.inter.AppSxyServiceInter;
import org.yx.util.ConstantUtil;
import org.yx.util.DataConvertUtil;
import org.yx.util.PushUtil;

import com.alibaba.fastjson.JSONArray;   
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

@Service("appSxyService")
public class AppSxyService implements AppSxyServiceInter{


	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	/**
	 * 商学院、推荐列表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	
	public String querySxy(PageData pd) throws Exception{
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
			try{
				List<PageData> pros =(List<PageData>) dao.findForList("AppSxyMapper.queryBylist",data);//第一页列表
			    return JSONArray.toJSONString(pros).toString();
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		}
		return null;  
	}
	
	/**
	 * 根据ID查询文章详情页
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String querySxyDetails(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData media =(PageData) this.dao.findForObject("AppSxyMapper.querySxyDetails",data); 
		    
		    data.put("OBJECT_ID",data.getString("ID"));
			data.put("TYPE","JL_JLB_SXY"); 
			data.put("VIEW_COUNT",1); 
			this.dao.update("AppSxyMapper.updateCounts",data);
			String uid=data.getString("USER_ID");
			if(uid!=null&&!"".equals(uid)&&uid!="undefined"){
				PageData page =(PageData) this.dao.findForObject("AppCollectionMapper.checkCollection",data);//判断是否收藏
				if(page!=null&&media!=null){
					media.put("IS_COLLECT",1);//已经收藏
				}else{
					media.put("IS_COLLECT",0);//未收藏
				} 
			}
			if(uid!=null&&!"".equals(uid)&&uid!="undefined"){
				PageData isDz =(PageData) this.dao.findForObject("AppSxyMapper.checkDz",data);//判断是否点赞
				if(isDz!=null&&media!=null){
					media.put("IS_LIKE",1);//已经点赞
				}else{
					media.put("IS_LIKE",0);//未点赞
				} 
			}
			
			data.put("start", 0);
			data.put("pageSize",10);
			List<PageData> PROFFESSORS=(List<PageData>) this.dao.findForList("AppSxyMapper.queryByjs",data);
			List<PageData> COMMENTS=(List<PageData>) this.dao.findForList("AppSxyMapper.queryBypl",data);
			media.put("appurls", "http://jianlian.shanghai-cu.com/jianlian/appSxy/queryfenxiangById.do?ID=");
			PageData details=new PageData();
			details.put("ARTICLE", media);
			details.put("PROFFESSORS", PROFFESSORS);
			details.put("COMMENTS", COMMENTS);
			return JSONArray.toJSONString(details).toString(); 
		}
		return null;  
	}
	
	
	/**
	 * 修改文章查看次数
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String addView(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){   
			 data.put("VIEW_COUNT",1); 
			 Object ob =dao.update("AppSxyMapper.updateCounts",data);
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
	 * 商学院/推荐添加评论
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addComment(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			data.put("SXY_ID", data.getString("ID"));
			data.put("ID", UuidUtil.get32UUID());
			data.put("COMMENT_DATE", new Date()); 
			Object obj=dao.save("AppSxyMapper.plsave",data);
			if(Integer.valueOf(obj.toString()) >= 1){ 
				PageData media=new PageData();
				media.put("COMMENT_COUNT",Integer.valueOf(obj.toString()));
				media.put("ID",data.getString("SXY_ID"));  
				Object ob =dao.update("AppSxyMapper.updateCounts",media);
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
	 * 获取评论列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryComments(PageData pd) throws Exception{
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
			try{
				List<PageData> pros =(List<PageData>) dao.findForList("AppSxyMapper.queryBypl",data);//第一页列表
			    return JSONArray.toJSONString(pros).toString();
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		}
		return null;  
	}
	
	
	/**
	 * 分享回调（修改分享数量）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String addShare(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){   
			 data.put("SHARE_COUNT",1); 
			 Object ob =dao.update("AppSxyMapper.updateCounts",data);
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
	 * 教授列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryProfessors(PageData pd) throws Exception{
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
			try{
				List<PageData> pros =(List<PageData>) dao.findForList("AppSxyMapper.queryProfessors",data);//第一页列表
			    return JSONArray.toJSONString(pros).toString();
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		}
		return null;  
	}
	
	/**
	 * 点赞/取消点赞
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String editLike(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData isDz =(PageData) this.dao.findForObject("AppSxyMapper.checkDz",data);//判断是否点赞
			PageData objdate=new PageData();
			objdate.put("ID", data.getString("ID"));
			if(isDz==null){//没有点赞
				String id=UuidUtil.get32UUID();
				data.put("SXY_ID", data.getString("ID"));
				data.put("ID", id);
				data.put("DATE", new Date()); 
				Object obj=dao.save("AppSxyMapper.dzsave",data);
				if(Integer.valueOf(obj.toString()) >= 1){
					objdate.put("LIKE_COUNT",1); 
	 				Object ob=this.dao.update("AppSxyMapper.updateCounts",objdate);
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
			}else{//已经点赞，取消点赞
				 Object obj =dao.delete("AppSxyMapper.del",data);
	 		     if(Integer.valueOf(obj.toString()) >= 1){ 
					objdate.put("LIKE_COUNT",1); 
	 				Object ob=dao.update("AppSxyMapper.dwdateCounts",objdate);
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
		}
		return null;  
	}  
	
	
	//返回成功参数
	public String result(String code){  
		PageData res=new PageData();
		res.put("code",code);
		return JSONArray.toJSONString(res).toString();  
	} 
}
