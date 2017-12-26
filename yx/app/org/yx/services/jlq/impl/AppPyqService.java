package org.yx.services.jlq.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;  
import org.yx.services.jlq.inter.AppPyqServiceInter;
import org.yx.util.ConstantUtil;

import com.alibaba.fastjson.JSONArray;  

@Service("appPyqService")
public class AppPyqService implements AppPyqServiceInter{ 

	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	 
	/**
	 * 查询朋友圈列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryIndexByParam(PageData pd) throws Exception {
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
		    List<PageData> pyqs =(List<PageData>) dao.findForList("AppPyqMapper.queryPageByParam",data);
		    for(PageData pyq:pyqs){
		    	data.put("PYQ_ID",pyq.getString("ID"));
		    	List<PageData> imgs=(List<PageData>) dao.findForList("AppPyqTpMapper.queryByParam",pyq);
		    	List<PageData> pls =(List<PageData>) dao.findForList("AppPyqPlMapper.queryPageByParam",data);
		    	List<PageData> dzs =(List<PageData>) dao.findForList("AppPyqDzMapper.queryDzUsers",data);
		    	PageData dz =(PageData) this.dao.findForObject("AppPyqDzMapper.checkDz",data);//判断是否点赞
				if(dz!=null){
					pyq.put("isDz",1);//已经点赞
				}else{
					pyq.put("isDz",0);//未点赞
				} 
		    	pyq.put("imgs",imgs);//图片
		    	pyq.put("pls",pls);//评论人
		    	pyq.put("dzs",dzs);//点赞人
		    	pyq.put("dz_count",dzs.size());//点赞数量
		    }
			return JSONArray.toJSONString(pyqs).toString();
		}
		return null; 
	} 
	
	/**
	 * 查询朋友圈列表评论Or回复
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryIndexByParamor(PageData pd) throws Exception {
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
		    List<PageData> pyqs =(List<PageData>) dao.findForList("AppPyqMapper.queryPageByParam",data);
		    for(PageData pyq:pyqs){
		    	data.put("PYQ_ID",pyq.getString("ID"));
		    	List<PageData> imgs=(List<PageData>) dao.findForList("AppPyqTpMapper.queryByParam",pyq);
		    	List<PageData> pls =(List<PageData>) dao.findForList("AppPyqPlMapper.queryPageByParamor",data);
		    	List<PageData> dzs =(List<PageData>) dao.findForList("AppPyqDzMapper.queryDzUsers",data);
		    	PageData dz =(PageData) this.dao.findForObject("AppPyqDzMapper.checkDz",data);//判断是否点赞
				if(dz!=null){
					pyq.put("isDz",1);//已经点赞
				}else{
					pyq.put("isDz",0);//未点赞
				} 
		    	pyq.put("imgs",imgs);//图片
		    	pyq.put("pls",pls);//评论人
		    	pyq.put("dzs",dzs);//点赞人
		    	pyq.put("dz_count",dzs.size());//点赞数量
		    }
			return JSONArray.toJSONString(pyqs).toString();
		}
		return null; 
	} 
	/**
	 * 查询附近朋友圈列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryNearByParam(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			Object pageNo = data.get("currentPage");
			Object pageSiZe = data.get("pageSize");
			
			//设置附近默认距离：1000米
			Integer distance = (Integer) data.get("DISTANCE");
			if( distance == null ){
				data.put("DISTANCE", 1000);
			}else{
				data.put("DISTANCE", distance);
			}
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
		    List<PageData> pyqs =(List<PageData>) dao.findForList("AppPyqMapper.queryNearByParam",data);
		    for(PageData pyq:pyqs){
		    	data.put("PYQ_ID",pyq.getString("ID"));
		    	List<PageData> imgs=(List<PageData>) dao.findForList("AppPyqTpMapper.queryByParam",pyq);
		    	PageData dz =(PageData) this.dao.findForObject("AppPyqDzMapper.checkDz",data);//判断是否点赞
				if(dz!=null){
					pyq.put("isDz",1);//已经点赞
				}else{
					pyq.put("isDz",0);//未点赞
				} 
		    	pyq.put("imgs",imgs);
		    }
			return JSONArray.toJSONString(pyqs).toString();
		}
		return null; 
	}
	
	/**
	 * 发布朋友圈信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addPyq(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			String id=UuidUtil.get32UUID();
			data.put("ID", id);
			data.put("PUBLISH_DATE", new Date()); 
			data.put("COMMENT_COUNT", 0); 
			data.put("LIKE_COUNT", 0); 
			data.put("SHARE_COUNT", 0); 
			data.put("STATUS","01"); //提交 
			Object obj=dao.save("AppPyqMapper.save",data); 
			if(Integer.valueOf(obj.toString()) >= 1){ 
				int mun=0;
				List<PageData> imgs=data.getList("imgs");
				for(PageData img:imgs){ 
					img.put("ID",UuidUtil.get32UUID());
					img.put("RELATED_ID", id);
					Object ob=dao.update("AppPyqTpMapper.save",img);
					mun+=Integer.valueOf(ob.toString());
				}  
				if(mun==imgs.size()){
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
	 * 根据ID查询朋友圈详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryById(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData pyq =(PageData) this.dao.findForObject("AppPyqMapper.queryById",data);  
		    if(pyq!=null){
		    	List<PageData> imgs=(List<PageData>) dao.findForList("AppPyqTpMapper.queryByParam",pyq);
			    PageData dz =(PageData) this.dao.findForObject("AppPyqDzMapper.checkDz",data);//判断是否点赞
				if(dz!=null){
					pyq.put("isDz",1);//已经点赞
				}else{
					pyq.put("isDz",0);//未点赞
				} 
			    pyq.put("imgs",imgs); 
				return JSONArray.toJSONString(pyq).toString();
		    }else{
		    	return null;  
		    } 
		}
		return null;  
	}
	
	/**
	 * 根据朋友圈ID查询评论
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryPyqPl(PageData pd) throws Exception {
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
		    List<PageData> pls =(List<PageData>) dao.findForList("AppPyqPlMapper.queryPageByParam",data);
			return JSONArray.toJSONString(pls).toString();
		}
		return null; 
	}
	
	/**
	 * 朋友圈添加评论
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addPyqPl(PageData pd) throws Exception {
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
			PageData pl=(PageData) dao.findForObject("AppPyqPlMapper.queryCounts",data); 
			if(pl!=null){
				int count= Integer.parseInt(pl.get("counts").toString());
				data.put("FLOOR",count+1);
			}else{
				data.put("FLOOR",1);
			}  
			Object obj=dao.save("AppPyqPlMapper.save",data);
			if(Integer.valueOf(obj.toString()) >= 1){ 
				PageData pyq=new PageData();
				pyq.put("COMMENT_COUNT",Integer.valueOf(obj.toString()));
				pyq.put("PYQ_ID",data.getString("PYQ_ID"));  
				Object ob =dao.update("AppPyqMapper.updateCounts",pyq);
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
	 * 朋友圈添加评论OR回复
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addPyqPlorhf(PageData pd) throws Exception {
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
			PageData pl=(PageData) dao.findForObject("AppPyqPlMapper.queryCounts",data); 
			if(pl!=null){
				int count= Integer.parseInt(pl.get("counts").toString());
				data.put("FLOOR",count+1);
			}else{
				data.put("FLOOR",1);
			}  
			if(data.getString("ANSWER_ID")!=null && !"".equals(data.getString("ANSWER_ID"))){
				data.put("FLOOR",null);
				data.put("TYPE", 1);
			}else {
				data.put("TYPE", 0);
			}
			Object obj=dao.save("AppPyqPlMapper.save",data);
			if(Integer.valueOf(obj.toString()) >= 1){ 
				PageData pyq=new PageData();
				pyq.put("COMMENT_COUNT",Integer.valueOf(obj.toString()));
				pyq.put("PYQ_ID",data.getString("PYQ_ID"));  
				Object ob =dao.update("AppPyqMapper.updateCounts",pyq);
				if(Integer.valueOf(ob.toString()) >= 1){
					PageData res=new PageData();
					res.put("code","200");
					res.put("ID", id);
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
	 * 删除评论
	 */
	public String delPyqpl(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData res=new PageData();
			Object obj =dao.delete("AppPyqPlMapper.pldel",data);
		    if(Integer.valueOf(obj.toString()) >= 1){  
				res.put("code","200");
			}else{
				res.put("code","400");
			} 
		    return JSONArray.toJSONString(res).toString();  
		}
		return null;  
	}
	
	
	
	/**
	 * 点赞/取消点赞
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editPyqDz(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData isDz =(PageData) this.dao.findForObject("AppPyqDzMapper.checkDz",data);//判断是否点赞
			if(isDz==null){//没有点赞
				String id=UuidUtil.get32UUID();
				data.put("ID", id);
				data.put("DATE", new Date()); 
				Object obj=dao.save("AppPyqDzMapper.save",data);
				if(Integer.valueOf(obj.toString()) >= 1){
					PageData pyq=(PageData) this.dao.findForObject("AppPyqMapper.queryByPyqId",data);//查询点赞数
	 				int  count=Integer.parseInt(pyq.get("LIKE_COUNT")+"");  
 					data.put("LIKE_COUNT",count+1);   
	 				Object ob=dao.update("AppPyqMapper.updateCounts",data);
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
				 Object obj =dao.delete("AppPyqDzMapper.del",data);
	 		     if(Integer.valueOf(obj.toString()) >= 1){ 
	 		    	PageData pyq=(PageData) this.dao.findForObject("AppPyqMapper.queryByPyqId",data);//查询点赞数
	 		    	int  count=Integer.parseInt(pyq.get("LIKE_COUNT")+"");   
	 				if(count>1){ 
 						data.put("LIKE_COUNT",count-1);  
 					}else{ 
 						data.put("LIKE_COUNT",0);  
 					} 
	 				Object ob=dao.update("AppPyqMapper.updateCounts",data);
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
	 * 删除朋友圈信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String delPyq(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			Object obj =dao.delete("AppPyqMapper.del",data);
			Object ob =dao.delete("AppPyqTpMapper.del",data); 
		    if(Integer.valueOf(obj.toString()) >= 1){  
				PageData res=new PageData();
				res.put("code","200");
				return JSONArray.toJSONString(res).toString();  
			}else{
				PageData res=new PageData();
				res.put("code","400");
				return JSONArray.toJSONString(res).toString();  
			} 
		}
		return null;  
	}
}

