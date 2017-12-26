package org.yx.services.jlq.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;  
import org.yx.services.common.AppDicService;
import org.yx.services.jlq.inter.AppWdServiceInter;

import com.alibaba.fastjson.JSONArray;  

@Service("appWdService")
public class AppWdService implements AppWdServiceInter{ 

	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	
	@Resource(name = "appDicService")
	private AppDicService appDicService; 
	
	/**
	 * 查看问答首页列表信息/搜索列表信息(参数：TITLE)/我的提问（参数：USER_ID）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryWdIndexInfo(PageData pd) throws Exception { 
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
		    List<PageData> hds =(List<PageData>) dao.findForList("AppWenTiMapper.queryPageByParam",data);
			return JSONArray.toJSONString(hds).toString();
		}
		return null; 
	}
	
	
	/**
	 * 添加问题
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addWenTi(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			String id=UuidUtil.get32UUID();
			data.put("ID", id); 
			data.put("AGREE_COUNT", 0); 
			data.put("COMMENT_COUNT", 0); 
			data.put("FOCUS_COUNT", 0); 
			data.put("CREATE_DATE", new Date());  
			Object obj=dao.save("AppWenTiMapper.save",data); 
			if(Integer.valueOf(obj.toString()) >= 1){ 
				int mun=0;
				List<PageData> bqs=data.getList("bqs");
				for(PageData bq:bqs){ 
					bq.put("ID",UuidUtil.get32UUID());
					bq.put("WT_ID", id);
					bq.put("DATE", new Date()); 
					Object ob=dao.update("AppBqMapper.saveHtBq",bq);
					mun+=Integer.valueOf(ob.toString());
				}  
				if(mun==bqs.size()){
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
	 * 根据ID查询问题详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryById(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData wt =(PageData) this.dao.findForObject("AppWenTiMapper.queryById",data); 
			PageData gz =(PageData) this.dao.findForObject("AppGzMapper.checkGz",data);//判断是否关注
			PageData dz =(PageData) this.dao.findForObject("AppWdDzMapper.checkDz",data);//判断是否已经点赞
			List<PageData> bqs =(List<PageData>) this.dao.findForList("AppBqMapper.queryByParam",data);//查询某个问题的标签
			if(gz!=null){
				wt.put("isGz",1);//已经关注
			}else{
				wt.put("isGz",0);//未关注
			} 
			if(dz!=null){
				wt.put("isDz",1);//已经关注
			}else{
				wt.put("isDz",0);//未关注
			} 
			wt.put("bqs",bqs);//问题的标签
			return JSONArray.toJSONString(wt).toString();
		}
		return null;  
	}
	
	/**
	 * 根据问题ID查询问题回答列表（参数：WT_ID）/查询某个回答的列表信息(参数：USER_ID)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryHuiDa(PageData pd) throws Exception {
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
		    List<PageData> hds =(List<PageData>) dao.findForList("AppHuiDaMapper.queryPageByParam",data);
		    PageData wtdz=(PageData) this.dao.findForObject("AppWenTiMapper.queryCounts",data);//查询点赞数量
			PageData res=new PageData();
	        res.put("count",wtdz);
	        res.put("hds",hds);
		    return JSONArray.toJSONString(res).toString(); 
		}
		return null; 
	}
	
	/**
	 * 根据ID查询回答详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryByHdId(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData hd =(PageData) this.dao.findForObject("AppHuiDaMapper.queryById",data); 
			return JSONArray.toJSONString(hd).toString();
		}
		return null;  
	}
	
	/**
	 * 问题添加回答
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addHuiDa(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			String id=UuidUtil.get32UUID();
			data.put("ID", id);
			data.put("DATE", new Date()); 
			/*String CONTENT=data.getString("CONTENT");
			if ((CONTENT != null) && (!"".equals(CONTENT))) { 
				CONTENT=new String(CONTENT.getBytes("ISO-8859-1"),"UTF-8").trim();
				data.put("CONTENT",CONTENT);
			}*/
			Object obj=dao.save("AppHuiDaMapper.save",data);
			if(Integer.valueOf(obj.toString()) >= 1){ 
				PageData wt=new PageData();
				wt.put("COMMENT_COUNT",Integer.valueOf(obj.toString()));
				wt.put("WT_ID",data.getString("WT_ID"));  
				Object ob =dao.update("AppWenTiMapper.updateCounts",wt);
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
	 * 点赞问题/取消赞同
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editWtDz(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData dz =(PageData) this.dao.findForObject("AppWdDzMapper.checkDz",data);//判断是否已经点赞
			if(dz==null){//没有点赞
				String id=UuidUtil.get32UUID();
				data.put("ID", id);
				data.put("DATE", new Date());
				Object obj=dao.save("AppWdDzMapper.save",data);
				if(Integer.valueOf(obj.toString()) >= 1){
					PageData wtdz=(PageData) this.dao.findForObject("AppWenTiMapper.queryCounts",data);//查询点赞数量
	 				int  count=Integer.parseInt(wtdz.get("AGREE_COUNT")+"");  
 					data.put("AGREE_COUNT",count+1);   
	 				Object ob=dao.update("AppWenTiMapper.updateCounts",data);
	 				if(Integer.valueOf(ob.toString()) >= 1){
	 					PageData res=new PageData();
						res.put("code","200");
						res.put("count",data.get("AGREE_COUNT").toString());
						return JSONArray.toJSONString(res).toString(); 
	 				}else{
	 					return null;  
	 				} 
				}else{
					return null;
				} 
			}else{//已经点赞，取消点赞
				 Object obj =dao.delete("AppWdDzMapper.del",data);
	 		     if(Integer.valueOf(obj.toString()) >= 1){ 
	 		    	PageData wtdz=(PageData) this.dao.findForObject("AppWenTiMapper.queryCounts",data);//查询点赞数量
	 		    	int  count=Integer.parseInt(wtdz.get("AGREE_COUNT")+""); 
	 				if(count>1){ 
 						data.put("AGREE_COUNT",count-1);  
 					}else{ 
 						data.put("AGREE_COUNT",0);  
 					} 
	 				Object ob=dao.update("AppWenTiMapper.updateCounts",data);
	 				if(Integer.valueOf(ob.toString()) >= 1){
	 					PageData res=new PageData();
						res.put("code","200");
						res.put("count",data.get("AGREE_COUNT").toString());
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
	 * 关注问题/取消关注
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editWtGz(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData gz =(PageData) this.dao.findForObject("AppGzMapper.checkGz",data);//判断是否已经关注
			if(gz==null){//没有关注
				String id=UuidUtil.get32UUID();
				data.put("ID", id);
				data.put("DATE", new Date());
				Object obj=dao.save("AppGzMapper.save",data);
				if(Integer.valueOf(obj.toString()) >= 1){
					PageData wtdz=(PageData) this.dao.findForObject("AppWenTiMapper.queryCounts",data);//查询点赞数量
	 				int  count=Integer.parseInt(wtdz.get("FOCUS_COUNT")+"");  
 					data.put("FOCUS_COUNT",count+1);   
	 				Object ob=dao.update("AppWenTiMapper.updateCounts",data);
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
				 Object obj =dao.delete("AppGzMapper.del",data);
	 		     if(Integer.valueOf(obj.toString()) >= 1){ 
	 		    	PageData wtdz=(PageData) this.dao.findForObject("AppWenTiMapper.queryCounts",data);//查询点赞数量
	 		    	int  count=Integer.parseInt(wtdz.get("FOCUS_COUNT")+""); 
	 				if(count>1){ 
 						data.put("FOCUS_COUNT",count-1);  
 					}else{ 
 						data.put("FOCUS_COUNT",0);  
 					} 
	 				Object ob=dao.update("AppWenTiMapper.updateCounts",data);
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
	
	/**
	 * 查询某个人的问题关注列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryGz(PageData pd) throws Exception {
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
		    List<PageData> gzs =(List<PageData>) dao.findForList("AppGzMapper.queryPageByParam",data);
			return JSONArray.toJSONString(gzs).toString();
		}
		return null; 
	}  
	
	/**
	 * 查询某个人回答的问题列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryHdList(PageData pd) throws Exception {
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
		    List<PageData> wts =(List<PageData>) dao.findForList("AppWenTiMapper.queryHdByUid",data);
			return JSONArray.toJSONString(wts).toString();
		}
		return null; 
	}   
	
	/**
	 * 查询问答全部标签
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryWdBq(PageData pd) throws Exception { 
		if(!EmptyUtil.isNullOrEmpty(pd)){
			List<PageData> bqs=this.appDicService.queryByPBM("topic");  
			return JSONArray.toJSONString(bqs).toString();
		}
		return null; 
	}  
}

 

