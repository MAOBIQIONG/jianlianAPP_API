package org.yx.services.wd.impl; 

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;
import org.yx.common.utils.fileConfig;
import org.yx.services.wd.inter.AppConnectServiceInter;
import org.yx.util.noticePushutil;

import com.alibaba.fastjson.JSONArray; 

@Service("appConnectService")
public class AppConnectService implements AppConnectServiceInter{ 
	
	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	public static String noticeText11 = fileConfig.getString("noticeText11");
	/**
	 * 我的所有人脉列表（可以根据条件查询）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryMyConnects(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			PageData result = new PageData();
			try{
				List<PageData> conns =(List<PageData>) dao.findForList("AppConnectsMapper.queryAllConnUsers",data);
				Map<String, Object> map = new HashMap<String, Object>(); 
				List<PageData> lists = null;
				for (PageData pageData : conns) {
					if (pageData.getString("VALERIE").equals(map.get(pageData.getString("VALERIE")))) {
						lists.add(pageData);
					} else {
						lists = new ArrayList<PageData>();
						map.put(pageData.getString("VALERIE"),pageData.getString("VALERIE"));
						lists.add(pageData);
					}
					result.put(map.get(pageData.getString("VALERIE")), lists);
				}
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(result).toString();
		}
		return null; 
	}
	
	/**
	 * 人脉首页--我的人脉列表（5条，无条件查询）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryIndexMyConnects(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			List<PageData> conns=new ArrayList<PageData>();
			try{
				if(data.get("USER_ID")!=null&&!"".equals(data.get("USER_ID"))){
					conns =(List<PageData>) dao.findForList("AppConnectsMapper.queryConnUsers",data);
				} 
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(conns).toString();
		}
		return null; 
	}
	
	/**
	 * 推荐人脉查询（根据行业推荐）,如果没有推荐人脉，
	 * 任意查询5条除了我的人脉和自己以外的其余会员信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryRecommendConnects(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			try{
				PageData com =(PageData) dao.findForObject("AppUsersMapper.queryCompany",data);
				if(com!=null&&com.get("CATEGORY_ID")!=null&&!"".equals(com.get("CATEGORY_ID"))){
					data.put("CATEGORY_ID",com.getString("CATEGORY_ID"));
					List<PageData> conns =(List<PageData>) dao.findForList("AppConnectsMapper.queryRecommend",data);
					if(conns==null||conns.size()<=0){//没有推荐人脉
						conns =(List<PageData>) dao.findForList("AppConnectsMapper.queryRandUsers",data);//任意查询五条我的人脉以外的会员信息
					}
					if( conns!=null&&conns.size() > 0 ){
						for( PageData pa : conns ){
							String name = pa.getString("REAL_NAME");
							if( name.length() > 4 ){
								name = name.substring(0, 4)+"...";
								pa.put("REAL_NAME",name);
							}
						}
					}
					return JSONArray.toJSONString(conns).toString();
				}else{
					return null; 
				} 
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		}
		return null; 
	}
	
	/**
	 * 关注/取消关注人脉
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editConnect(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			if(data.getString("CONTRACT_ID")!=null&&!"".equals(data.getString("CONTRACT_ID"))){
				List<PageData> isGz =(List<PageData>) this.dao.findForList("AppConnectsMapper.checkIsGz",data);//判断是否关注
				if(isGz!=null&&isGz.size()>0){//已经关注，取消关注
					Object obj =dao.delete("AppConnectsMapper.delete",data);
		 		    if(Integer.valueOf(obj.toString()) >= 1){  
	 					PageData res=new PageData();
						res.put("code","200");
						return JSONArray.toJSONString(res).toString();  
		 			}else{
		 				return null;  
		 			}  
				}else{//没有关注，添加关注
					String id=UuidUtil.get32UUID();
					data.put("ID", id);
					data.put("DATE", new Date()); 
					Object obj=dao.save("AppConnectsMapper.save",data);
					if(Integer.valueOf(obj.toString()) >= 1){ 
	 					PageData res=new PageData();
						res.put("code","200");
						return JSONArray.toJSONString(res).toString();   
					}else{
						return null;
					} 
				}
			}else{
				PageData res=new PageData();
				res.put("code","400");
				return JSONArray.toJSONString(res).toString();  
			}
		}
		return null;  
	} 
	
	/**
	 * 查看他人个人中心信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryConnectInfo(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			try{
				PageData pa=new PageData();
				pa.put("USER_ID",data.getString("CONTRACT_ID"));
				PageData user =(PageData) dao.findForObject("AppzUserMapper.findUserByidDetail",pa);
				if(user!=null){
					List<PageData> isGz =(List<PageData>) this.dao.findForList("AppConnectsMapper.checkIsGz",data);//判断是否关注
					if(isGz!=null&&isGz.size()>0){
						user.put("isGz",1);//已经关注
					}else{
						user.put("isGz",0);//未关注
					}
					return JSONArray.toJSONString(user).toString();
				}else{
					return null;
				} 
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			
		} 
		return null;
	}
	
	/**
	 * 添加朋友印象
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String editConnectInfo(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(!EmptyUtil.isNullOrEmpty(data)){   
			try{ 
				data.put("USER_ID",data.getString("CONTRACT_ID"));
				Object ob=dao.update("AppUsersMapper.editPwdAndEmailAndPhone",data); 
 				if(Integer.valueOf(ob.toString()) >= 1){
 					PageData res=new PageData();
					res.put("code","200");
					return JSONArray.toJSONString(res).toString();  
 				}else{
 					return null;  
 				}
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
		} 
		return null;
	} 
	
	/**
	 * 查询人脉搜索条件
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryConnectParams(PageData pd) throws Exception{ 
		if(!EmptyUtil.isNullOrEmpty(pd)){ 
			List<PageData> cates =(List<PageData>) dao.findForList("AppCategoryMapper.listByPId","0");//查询一级行业分类
			List<PageData> viplevels =(List<PageData>) this.dao.findForList("AppDicMapper.queryByPBM","viptype"); //查询会员等级信息
		    PageData res=new PageData(); 
		    res.put("cates",cates);
		    res.put("viplevels",viplevels);
			return JSONArray.toJSONString(res).toString(); 
		}
		return null; 
	}
	
	/**
	 * 根据条件搜索扩展人脉（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryConnects(PageData pd) throws Exception{
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
			List<PageData> conns=null;
			try{
				conns =(List<PageData>) dao.findForList("AppConnectsMapper.queryExtendsByParam",data);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(conns).toString();
		}
		return null; 
	}
	
	/**
	 * 查询城市建联（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryClans(PageData pd) throws Exception{
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
			List<PageData> clans=null;
			try{
				clans =(List<PageData>) dao.findForList("AppClanMapper.queryByParam",data);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clans).toString();
		}
		return null; 
	}
	
	/**
	 * 查询用户关注的城市建联列表(分页)
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryClanByUid(PageData pd) throws Exception{
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
			List<PageData> clans=null;
			try{
				clans =(List<PageData>) dao.findForList("AppClanMapper.queryByUId",data);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clans).toString();
		}
		return null; 
	}
	
	/**
	 * 查询城市建联详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryClanInfo(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData res=new PageData();
			PageData clan=new PageData(); 
			try{
				if("100".equals(data.getString("CLAN_ID"))){//查询上海建联详情
					clan =(PageData) dao.findForObject("AppClanMapper.queryShClan",null);//查询上海建联详情
					PageData users =(PageData) dao.findForObject("AppUsersMapper.queryBySh",null);//查询上海建联成员数量
					PageData numbers =(PageData) dao.findForObject("AppClanUserMapper.querySh",null);//查询上海建联关注数量
					clan.put("member",users.get("counts"));//成员数量
					clan.put("number",numbers.get("counts"));//成员数量
				}else if("200".equals(data.getString("CLAN_ID"))){//查询会员所属建联详情
					clan =(PageData) dao.findForObject("AppClanMapper.queryByUserId",data);//查询所属城市建联详情
					PageData users =(PageData) dao.findForObject("AppUsersMapper.queryByCid",data);//查询成员数量
					PageData numbers =(PageData) dao.findForObject("AppClanUserMapper.queryCounts",data);//查询关注数量
					clan.put("member",users.get("counts"));//成员数量
					clan.put("number",numbers.get("counts"));//成员数量
				}else{//根据id查询建联详情
					clan =(PageData) dao.findForObject("AppClanMapper.queryById",data);//查询城市建联详情
					PageData users =(PageData) dao.findForObject("AppUsersMapper.queryByCid",data);//查询成员数量
					PageData numbers =(PageData) dao.findForObject("AppClanUserMapper.queryCounts",data);//查询关注数量
					clan.put("member",users.get("counts"));//成员数量
					clan.put("number",numbers.get("counts"));//成员数量
				}
				data.put("CLAN_ID",clan.getString("ID"));
				List<PageData> gzs =(List<PageData>) dao.findForList("AppClanUserMapper.checkIsGz",data);//查询用户是否关注了城市建联
			    if(gzs!=null&&gzs.size()>0){
			    	clan.put("isGz",1);//已经关注
			    }else{
			    	clan.put("isGz",0);//未关注
			    } 
				data.put("start", 0);
				data.put("pageSize",10); 
				List<PageData> members=(List<PageData>) dao.findForList("AppClanMapper.queryClanMembers",data);
				res.put("clan", clan);
				res.put("members",members);
				
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(res).toString();
		}
		return null; 
	} 
	
	/**
	 *查询城市建联关注人列表(分页)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryClanUsers(PageData pd) throws Exception{
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
			List<PageData> clans=null;
			try{
				clans =(List<PageData>) dao.findForList("AppClanMapper.queryGzUsers",data);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clans).toString();
		}
		return null; 
	}
	
	/**
	 *查询城市建联成员列表(分页)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryClanMembers(PageData pd) throws Exception{
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
			List<PageData> clans=null;
			try{
				if(data.getString("CLAN_ID")!=null&&!"".equals(data.getString("CLAN_ID"))){
					clans =(List<PageData>) dao.findForList("AppClanMapper.queryClanMembers",data);
				} 
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clans).toString();
		}
		return null; 
	}
	
	/**
	 * 关注/取消关注城市建联
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editClan(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			List<PageData> isGz =(List<PageData>) this.dao.findForList("AppClanUserMapper.checkIsGz",data);//判断是否关注
			if(isGz!=null&&isGz.size()>0){//已经关注，取消关注
				Object obj =dao.delete("AppClanUserMapper.delete",data);
	 		    if(Integer.valueOf(obj.toString()) >= 1){ 
	 		    	PageData clan=(PageData) this.dao.findForObject("AppClanMapper.queryById",data);//查询关注数量
	 				if(clan!=null){
	 					int  count=Integer.parseInt(clan.get("NUMBER_COUNTS")+"");  
		 				if(count>1){ 
							data.put("NUMBER_COUNTS",count-1);  
						}else{ 
							data.put("NUMBER_COUNTS",0);  
						} 
		 				Object ob=dao.update("AppClanMapper.updateCounts",data); 
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
	 			}else{
	 				return null;  
	 			}  
			}else{//没有关注，添加关注
				String id=UuidUtil.get32UUID();
				data.put("ID", id);
				data.put("DATE", new Date()); 
				Object obj=dao.save("AppClanUserMapper.save",data);
				if(Integer.valueOf(obj.toString()) >= 1){
					PageData clan=(PageData) this.dao.findForObject("AppClanMapper.queryById",data);//查询关注数量
	 				if(clan!=null){
	 					int  count=Integer.parseInt(clan.get("NUMBER_COUNTS")+"");   
							data.put("NUMBER_COUNTS",count+1);   
							Object ob=dao.update("AppClanMapper.updateCounts",data); 
		 				if(Integer.valueOf(ob.toString()) >= 1){
		 					//获取notice.properties noticeText11内容。
 					       String vv3=new String(noticeText11.getBytes("ISO-8859-1"),"UTF-8");
	 					    String notice1=clan.getString("NAME")+vv3;
	 				        noticePushutil notutil=new noticePushutil();
	 				        notutil.toNotice(notice1); 
		 					PageData res=new PageData();
							res.put("code","200");
							return JSONArray.toJSONString(res).toString();  
		 				}else{
		 					return null;  
		 				} 
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
	 * 查询上海建联详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryShClan(PageData pd) throws Exception{ 
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData clan=new PageData();
			try{
				clan =(PageData) dao.findForObject("AppClanMapper.queryShClan",null);//查询上海建联详情
			    data.put("CLAN_ID",clan.getString("ID"));
				List<PageData> gzs =(List<PageData>) dao.findForList("AppClanUserMapper.checkIsGz",data);//查询用户是否关注了城市建联
			    if(gzs!=null&&gzs.size()>0){
			    	clan.put("isGz",1);//已经关注
			    }else{
			    	clan.put("isGz",0);//未关注
			    }
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clan).toString();
		}
		return null; 
	} 
	
	/**
	 * 查询会员所属建联详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */  
	public String queryUserClan(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData clan=null;
			try{
				
				data.put("CLAN_ID",clan.getString("ID"));
				List<PageData> gzs =(List<PageData>) dao.findForList("AppClanUserMapper.checkIsGz",data);//查询用户是否关注了城市建联
			    if(gzs!=null&&gzs.size()>0){
			    	clan.put("isGz",1);//已经关注
			    }else{
			    	clan.put("isGz",0);//未关注
			    }
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(clan).toString();
		}
		return null; 
	} 
	
	/**
	 * 查询一级城市分类
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryFirstCity(PageData pd) throws Exception{  
	    //查询省份
	    List<PageData> areas =(List<PageData>) this.dao.findForList("SysAreaMapper.listByPId","0"); //查询全部的省级地区
		PageData res=new PageData(); 
		res.put("areas",areas);
		return JSONArray.toJSONString(res).toString();   
	}
	
	/**
	 * 查询某个人的城市合伙人申请记录
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryClanApply(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){  
			List<PageData> applys =(List<PageData>) dao.findForList("AppClanApplicantMapper.queryByUid",data);//查询用户的城市合伙人申请记录
			return JSONArray.toJSONString(applys).toString();
		}
		return null;  
	}
} 
	
	
 

