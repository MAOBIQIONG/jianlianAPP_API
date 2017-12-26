package org.yx.services.cyl.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.cache.CacheHandler;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;
import org.yx.common.utils.DateUtil;
import org.yx.common.utils.EmptyUtil;
import org.yx.common.utils.UuidUtil;
import org.yx.services.cyl.inter.AppCompanyServiceInter; 
import org.yx.util.ConstantUtil;
import org.yx.util.DataConvertUtil;
import org.yx.util.PushUtil;
import org.yx.util.ResultUtil;
import org.yx.util.noticeConfig;
import org.yx.util.noticePushutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.sun.istack.internal.logging.Logger;


@Service("appComService")
public class AppCompanyService implements AppCompanyServiceInter {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;   
	
	Logger log=Logger.getLogger(this.getClass());

	public static String noticeText15 = noticeConfig.getString("noticeText15");
	/**
	 * 查询所有的行业信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public String queryCates(PageData pd) throws Exception {
		if(!EmptyUtil.isNullOrEmpty(pd)){  
			log.info("查询所有的行业信息. service：【appComService】 方法：【queryCates】，参数为"+JSONArray.toJSONString(pd).toString());
			List<PageData> cates=(List<PageData>) this.dao.findForList("AppCategoryMapper.listByPId","0");//查询一级行业分类
			
			//客户要求：暂时不显示产业链内容；有需要时，删除代码即可。
			cates = new ArrayList<PageData>();
			
			for(PageData pagedata:cates){
		    	List<PageData> child=(List<PageData>) this.dao.findForList("AppCategoryMapper.listByPId",pagedata.getString("value"));//查询二级行业分类
		    	pagedata.put("children",child); 
		    }
			log.info("查询所有的行业信息. service：【appComService】 方法：【queryCates】，返回值数据量："+cates.size());
			return JSONArray.toJSONString(cates).toString(); 
		}
		return null;  
	}
	
	/**
	 * 查询所有的行业信息(新)
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public String queryNewCates(PageData pd) throws Exception {
		PageData data = pd.getObject("params");  
		log.info("查询所有的行业信息(新). service：【appComService】 方法：【queryNewCates】，参数为"+JSONArray.toJSONString(data).toString());
		PageData res=new PageData();
		
		boolean flag=CacheHandler.getCompareVersion(ConstantUtil.VERSION_CATE, data);//比较app端的缓存和服务器的数据是否一致
		DataConvertUtil.getRes(flag,ConstantUtil.VERSION_CATE,res); //根据比较结果返回数据
		log.info("查询所有的行业信息(新). service：【appComService】 方法：【queryNewCates】");
		return JSONArray.toJSONString(res).toString(); 
	}

	/**
	 * 根据行业查询公司列表信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public String queryCompanysByCate(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		log.info("根据行业查询公司列表信息. service：【appComService】 方法：【queryCompanysByCate】，参数为"+JSONArray.toJSONString(data).toString());
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
			List<PageData> coms=null;
			try{
				coms =(List<PageData>) dao.findForList("AppCompanyMapper.queryByParam",data);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(coms).toString();
		}
		return null;  
	}

	/**
	 * 查询公司详细信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public String queryCompanyInfo(PageData pd) throws Exception {
		PageData data = pd.getObject("params"); 
		log.info("查询公司详细信息. service：【appComService】 方法：【queryCompanyInfo】，参数为"+JSONArray.toJSONString(data).toString());
		if(!EmptyUtil.isNullOrEmpty(data)){  
		    PageData com =(PageData) this.dao.findForObject("AppCompanyMapper.queryById",data); 
		    if(com!=null){
		    	List<PageData> honors =(List<PageData>) this.dao.findForList("AppCompanyMapper.queryHonors",data); //查询公司的荣誉信息
			    PageData shop=(PageData) this.dao.findForObject("ShopMapper.queryByComid",data);//根据公司id查询店铺基本信息 
				if( shop != null ){
					PageData proCount=(PageData) this.dao.findForObject("ProductMapper.queryBySid",shop);//查询商品总量
					data.put("SHOP_ID",shop.getString("SHOP_ID"));
					data.put("start",0);
					data.put("pageSize",10); 
					List<PageData> prods=(List<PageData>) this.dao.findForList("ProductMapper.queryByParam",data);//查询店铺的商品信息 
					com.put("shop",shop);
					com.put("prod_count",proCount.get("counts"));
					com.put("prods",prods); 
				}
				com.put("honors",honors);//荣誉信息 
				return JSONArray.toJSONString(com).toString(); 
		    }else{
		    	return null; 
		    } 
		}
		return null;   
	} 
	
	/**
	 * 查询首页轮换图片
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryRotations(PageData pd) throws Exception{
		log.info("查询首页轮换图片. service：【appComService】 方法：【queryRotations】，参数为"+JSONArray.toJSONString(pd).toString());
		if(!EmptyUtil.isNullOrEmpty(pd)){ 
			PageData param =new PageData();
			param.put("LOCATION_NO","14");
			List<PageData> firRots=(List<PageData>) dao.findForList("AppRotationMapper.queryByColno",param);//查询首页轮换大图 
			List<PageData> cates=(List<PageData>) dao.findForList("AppCategoryMapper.queryShopCate",param);//查询首页的行业信息
			param.put("start", 0);
			param.put("pageSize",10); 
			List<PageData> shops =(List<PageData>) dao.findForList("ShopMapper.queryByParam",param);
			PageData res=new PageData();
			res.put("firRots", firRots);
			//log.info("查询首页轮换图片. service：【appComService】 方法：【queryRotations】，firRots值为："+JSONArray.toJSONString(firRots).toString());
			res.put("cates", cates);  
			res.put("shops", shops);    
			return JSONArray.toJSONString(res).toString(); 
		 }
		 return null;  
	}
	
	/**
	 * 根据条件查询店铺列表信息(店铺名称、行业),分页
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryShops(PageData pd) throws Exception{ 
		PageData data = pd.getObject("params");
		log.info("根据条件查询店铺列表信息(店铺名称、行业),分页. service：【appComService】 方法：【queryShops】，参数为"+JSONArray.toJSONString(data).toString());
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
			List<PageData> shops=null;
			try{
				shops =(List<PageData>) dao.findForList("ShopMapper.queryByParam",data);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(shops).toString();
		}
		return null;  
	}
	
	/**
	 * 查询所有行业信息，查出默认第一个行业的店铺信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCate(PageData pd) throws Exception{ 
		if(!EmptyUtil.isNullOrEmpty(pd)){
			log.info("查询所有行业信息，查出默认第一个行业的店铺信息. service：【appComService】 方法：【queryCate】，参数为"+JSONArray.toJSONString(pd).toString());
			List<PageData> cates=null;
			try{
				//cates=(List<PageData>) this.dao.findForList("AppCategoryMapper.listByPId","0");//查询一级行业分类
				Object pageNo = pd.get("currentPage");
				Object pageSiZe = pd.get("pageSize");
				if(EmptyUtil.isNullOrEmpty(pageNo) || EmptyUtil.isNullOrEmpty(pageSiZe)){
					pd.put("start", 0);
					pd.put("pageSize",10);
				}else{
					int currentPage =Integer.valueOf(pageNo.toString());
					int pageSize=Integer.valueOf(pageSiZe.toString());
					int start=(currentPage-1)*pageSize;
					pd.put("start",start);
					pd.put("pageSize",pageSize);
				} 
				cates=CacheHandler.getAllCates();
				List<PageData> shops =(List<PageData>) dao.findForList("ShopMapper.queryByParam",pd); 
				cates.get(0).put("shops",shops);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(cates).toString();
		}
		return null;  
	}
	
	/**
	 * 根据参数搜索商品（名称,分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProductByParam(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		log.info("根据参数搜索商品（名称,分页）. service：【appComService】 方法：【queryProductByParam】，参数为"+JSONArray.toJSONString(data).toString());
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
				products =(List<PageData>) dao.findForList("ProductMapper.queryByParam",data);
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
	 * 根据参数查询商品信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProducts(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		log.info("根据参数查询商品信息. service：【appComService】 方法：【queryProducts】，参数为"+JSONArray.toJSONString(data).toString());
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
				products =(List<PageData>) dao.findForList("ProductMapper.queryProdsByParam",data);
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
	 * 根据参数查询商品详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProductDetail(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		PageData res=new PageData();
		if(!EmptyUtil.isNullOrEmpty(data)){
			List<PageData> imgs=null;
			List<PageData> prods=null;
			try{
				imgs=(List<PageData>) this.dao.findForList("ProductImgMapper.queryDetailImg",data);//查询商品详情
				prods=(List<PageData>) this.dao.findForList("ProductMapper.querySameByCate",data);//查询推荐商品信息
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			res.put("imgs",imgs);
			res.put("prods",prods);
			return JSONArray.toJSONString(res).toString();
		}
		return null; 
	}
	
	/**
	 * 查询商品信息和店铺统计等信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProductAndShop(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		log.info("接口 queryProductAndShop的参数为："+JSON.toJSONString(data).toString());
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData prod=new PageData();
			try{
				List<PageData>imgs=(List<PageData>) this.dao.findForList("ProductImgMapper.queryImgs",data);//查询商品展示图片
				prod=(PageData) this.dao.findForObject("ProductMapper.queryById",data);//查询商品信息
				PageData prods=(PageData) this.dao.findForObject("ProductMapper.queryBySid",prod);//查询商品总量
				PageData shop=(PageData) this.dao.findForObject("ShopMapper.queryById",prod);//查询用户的店铺信息
				PageData comment=(PageData) this.dao.findForObject("ProductCommentMapper.queryNewComment",data);//查询最新评价信息
				PageData counts=(PageData) this.dao.findForObject("ProductCommentMapper.queryCount",data);//查询评价总数
				PageData tel =(PageData) this.dao.findForObject("ServiceTelMapper.findBm","03");//查询商城下单页面的客服电话
				log.info("shop的值为："+JSON.toJSONString(shop).toString());
				prod.put("shop",shop);
				prod.put("prod_count",prods.get("counts"));
				prod.put("imgs",imgs);
				prod.put("comment",comment);
				prod.put("comment_count",counts.get("counts"));
				prod.put("tel",tel);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(prod).toString();
		}
		return null;  
	}
	
	/**
	 * 查询店铺信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryShopInfo(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			PageData shop=new PageData();
			try{
				shop=(PageData) this.dao.findForObject("ShopMapper.queryById",data);//店铺基本信息
				if(data.getString("USER_ID")!=null&&!"".equals(data.getString("USER_ID"))){
					PageData collect=(PageData) this.dao.findForObject("ShopMapper.queryIsCollect",data);//查询某个用户是否收藏店铺
					if(collect!=null){
						shop.put("isCollect","1");//已经收藏店铺 
					}else{
						shop.put("isCollect","0");//没有收藏店铺
					}
				}else{
					shop.put("msg","请先登录！"); 
				}   
				data.put("start",0);
				data.put("pageSize",10); 
				List<PageData> products=(List<PageData>) dao.findForList("ProductMapper.queryByParam",data);
				shop.put("products",products);
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(shop).toString();
		}
		return null;    
	}  
	
	/**
	 *查询商品规格信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryGuige(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		PageData res=new PageData();
		if(!EmptyUtil.isNullOrEmpty(data)){
			List<PageData> props=null;
			List<PageData> skus=null;
			PageData prod=new PageData();
			try{
				props=(List<PageData>) dao.findForList("ProductSpecMapper.queryByPid",data); 
				skus=(List<PageData>) dao.findForList("ProductSpecMapper.querySku",data); 
				prod=(PageData) dao.findForObject("ProductMapper.queryById",data);  
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
			res.put("props",props);
			res.put("skus",skus);
			res.put("prod",prod);
			return JSONArray.toJSONString(res).toString();  
		}
		return null;   
	}
	
	/**
	 *查询商属性信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryProperty(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){
			List<PageData> props=null;
			try{
				props=(List<PageData>) dao.findForList("ProductPropMapper.queryByPid",data); 
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}  
			return JSONArray.toJSONString(props).toString();  
		}
		return null;   
	}
	
	/**
	 * 下单
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addOrders(PageData pd) throws Exception{ 
		PageData data = pd.getObject("params");  
		log.info("下单.方法：【addOrders】，参数为"+JSONArray.toJSONString(data).toString());
		if(EmptyUtil.isNullOrEmpty(data)){
			return null;  
		} 
		PageData shop=null;
		if(EmptyUtil.isNullOrEmpty(data.get("SHOP_ID"))){
			if(EmptyUtil.isNullOrEmpty(data.get("PROD_SKUINFO"))){
				log.info("参数不正确！参数为："+JSON.toJSONString(data).toString());
				return ResultUtil.failMsg("下单失败!");
			}else{
				String[] infos=data.getString("PROD_SKUINFO").split(",");
				String prodid=infos[0]; 
				data.put("PROD_ID",prodid); 
				shop=(PageData) dao.findForObject("ProductMapper.queryShopByPid",data); 
				data.put("SHOP_ID",shop.get("SHOP_ID"));
			} 
		}else{
			shop=(PageData) dao.findForObject("ShopMapper.queryById",data); 
		}  
		if(EmptyUtil.isNullOrEmpty(shop)){
			log.info("店铺信息没有查找到！参数为："+JSON.toJSONString(data).toString());
			return ResultUtil.failMsg("店铺信息没有查找到!");
		} 
		log.info("店铺信息为："+JSON.toJSONString(shop).toString());
		data.put("SHOP_PHONE",shop.get("PHONE"));
		data.put("SHOP_USERNAME",shop.get("SHOP_NAME"));
		data.put("SHOP_ADDRESS",shop.get("ADDR")); 
		data.put("EXPRESS_PRICE",shop.get("EXPRESS_PRICE"));
        data.put("ORDER_STATU","00");//已下单
        data.put("CREATE_DATE",new Date()); 
        if(data.get("EXPRESS_PRICE")==null||"".equals(data.get("EXPRESS_PRICE"))){
        	data.put("EXPRESS_PRICE",0);
        }
        if(data.get("ORDER_ID")!=null&&!"".equals(data.get("ORDER_ID"))){//修改 
    	   boolean obj=dao.update_("DpOrderMapper.editOrder",data);
    	   if(!obj){
    		    PageData res=new PageData();
				res.put("code","400"); 
				return JSONArray.toJSONString(res).toString(); 
    	   } 
    	   boolean re=dao.update_("DpOrderMapper.editDetail",data); 
		   if(!re){
			   	PageData res=new PageData();
				res.put("code","400"); 
				return JSONArray.toJSONString(res).toString(); 
		   }
		   	PageData type=new PageData();
  		    type.put("SERVICE_TYPE","40");
  		    List<PageData> users =(List<PageData>) dao.findForList("AppUsersMapper.queryByType",type);
  		    if( users != null && users.size()>=1){
				String content="有用户在商城下单了，快去后台查看吧！";
				PageData cont=new PageData(); 
				cont.put("CONTENT",content);
				cont.put("USER_ID",users.get(0).getString("ID"));
				cont.put("TABLE_NAME","JL_DP_SHOP"); 
				cont.put("DESCRIPTION",content);  
				Object o=push(users.get(0),cont);
				if(Integer.valueOf(o.toString()) >= 1){
					//获取notice.properties noticeText15内容。
	 			    String vv3=new String(noticeText15.getBytes("ISO-8859-1"),"UTF-8");
			    	String notice1=vv3;
	    	        //实时更新后台管理消息
	    	 		noticePushutil notutil=new noticePushutil();
	    	        notutil.toNotice(notice1);
					PageData res=new PageData();
					res.put("code","200"); 
					return JSONArray.toJSONString(res).toString(); 
				}else{
					PageData res=new PageData();
					res.put("code","400"); 
					return JSONArray.toJSONString(res).toString(); 
				}
			}else{
				PageData res=new PageData();
				res.put("code","200"); 
				return JSONArray.toJSONString(res).toString();
			}  
        }else{ 
           String id=DateUtil.getDays()+DateUtil.getFixLenthString(11);   
		   data.put("ORDER_ID",UuidUtil.get32UUID());  
    	   data.put("ORDER_NO",id);
    	   data.put("TYPE","01");   
    	   boolean obj=dao.update_("DpOrderMapper.saveOrder",data);  
    	   if(!obj){
    		   return null;   
    	   }  
		   PageData detail=new PageData();
    	   detail.put("DETAIL_ID", UuidUtil.get32UUID());
    	   detail.put("ORDER_NO", id); 
    	   detail.put("PROD_NO",data.get("PROD_NO")); 
    	   detail.put("PROD_NUM", data.get("PROD_NUM")); 
    	   detail.put("PROD_PRICE",data.get("PROD_PRICE")); 
    	   detail.put("PROD_GUIGE", data.get("PROD_GUIGE")); 
    	   detail.put("PROD_SKUINFO",  data.get("PROD_SKUINFO")); 
    	   detail.put("CREATE_DATE", new Date());   
		   //保存项目反馈信息  
      	   boolean re=dao.save_("DpOrderMapper.saveDetail",detail); 
      	   if(!re){ 
      		   	PageData res=new PageData();
				res.put("code","400"); 
				return JSONArray.toJSONString(res).toString(); 
      	   }
  			PageData type=new PageData();
  		    type.put("SERVICE_TYPE","40");
  		    List<PageData> users =(List<PageData>) dao.findForList("AppUsersMapper.queryByType",type);
  		    if( users != null && users.size()>=1){
				String content="有用户在商城下单了，快去后台查看吧！";
				PageData cont=new PageData(); 
				cont.put("CONTENT",content);
				cont.put("USER_ID",users.get(0).getString("ID"));
				cont.put("TABLE_NAME","JL_DP_SHOP"); 
				cont.put("DESCRIPTION",content);  
				Object o=push(users.get(0),cont);
				if(Integer.valueOf(o.toString()) >= 1){
					//获取notice.properties noticeText15内容。
	 			    String vv3=new String(noticeText15.getBytes("ISO-8859-1"),"UTF-8");
			    	String notice1=vv3;
	    	        //实时更新后台管理消息
	    	 		noticePushutil notutil=new noticePushutil();
	    	        notutil.toNotice(notice1);
					PageData res=new PageData();
					res.put("code","200"); 
					return JSONArray.toJSONString(res).toString(); 
				}else{
					PageData res=new PageData();
					res.put("code","400"); 
					return JSONArray.toJSONString(res).toString(); 
				}
			}else{
				PageData res=new PageData();
				res.put("code","200"); 
				return JSONArray.toJSONString(res).toString();
			}  
        } 
	}
	
	/**
	 * 查询我的订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryOrdersByUid(PageData pd) throws Exception{
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
			List<PageData> orders=null;
			try{
				orders =(List<PageData>) dao.findForList("DpOrderMapper.queryByParam",data); 
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(orders).toString();
		}
		return null;      
	}
	
	/**
	 * 添加评论
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String AddComments(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){   
			data.put("ID",UuidUtil.get32UUID()); 
			data.put("CREATE_DATE",new Date()); 
			Object ob=dao.save("ProductCommentMapper.save",data); 
			if(Integer.valueOf(ob.toString())>0){ 
				int mun=0;
				List<PageData> imgs=data.getList("imgs");  
				for(PageData img:imgs){ 
					img.put("ID",UuidUtil.get32UUID());
					img.put("COMMENT_ID",data.getString("ID"));  
					img.put("CREATE_DATE",new Date()); 
					if(img.getString("IMG_PATH").contains("{")){
						PageData path=(PageData) img.get("IMG_PATH");
						img.put("IMG_PATH",path.getString("result")); 
					} 
					Object obj=dao.save("ProductCommentMapper.saveImgs",img); 
					mun+=Integer.valueOf(obj.toString()); 
				} 
				if(mun==imgs.size()){ 
					PageData order=new PageData();
					order.put("ORDER_ID",data.getString("ORDER_ID"));
					order.put("STATUS","05");
					Object obj=dao.update("DpOrderMapper.editStatus",order); 
					if(Integer.valueOf(obj.toString())>0){ 
						PageData res=new PageData();
						res.put("code","200");
						return JSONArray.toJSONString(res).toString(); 
					}else{
						PageData res=new PageData();
						res.put("code","400");
						return JSONArray.toJSONString(res).toString(); 
					} 
				}else{
					PageData res=new PageData();
					res.put("code","400");
					return JSONArray.toJSONString(res).toString(); 
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
	 * 查看评论,分页
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
			List<PageData> comments=null;
			try{
				comments =(List<PageData>) dao.findForList("ProductCommentMapper.queryByParam",data);
				for(PageData comm:comments){
					if("01".equals(comm.getString("HAS_IMG"))){
						List<PageData> tps =(List<PageData>) dao.findForList("ProductCommentMapper.queryCommImgs",comm);
						comm.put("tps",tps);
					} 
				}
			}catch(SQLException sql){
				sql.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(comments).toString();
		}
		return null;    
	}
	
	/**
	 * 查询评论总数
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryCommentsCounts(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData res=new PageData();
			try{
				PageData counts=(PageData) this.dao.findForObject("ProductCommentMapper.queryCount",data);//查询某个商品的评论总数
				PageData imgCounts=(PageData) this.dao.findForObject("ProductCommentMapper.queryImgCount",data);//查询某个商品的带有图片的评论总数
				data.put("start",0);
				data.put("pageSize",10);
				List<PageData> comments =(List<PageData>) dao.findForList("ProductCommentMapper.queryByParam",data);
				for(PageData comm:comments){
					if("01".equals(comm.getString("HAS_IMG"))){
						List<PageData> tps =(List<PageData>) dao.findForList("ProductCommentMapper.queryCommImgs",comm);
						comm.put("tps",tps);
					} 
				}
				res.put("comments",comments);
				long count = (Long) counts.get("counts");
				long imgCount = (Long) imgCounts.get("counts");
				res.put("counts",count);
				res.put("imgCounts",imgCount);
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
	 * 查询是否收藏店铺
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryIsCollect(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			PageData res=new PageData();
			try{
				if(data.getString("USER_ID")!=null&&!"".equals(data.getString("USER_ID"))){
					PageData collect=(PageData) this.dao.findForObject("ShopMapper.queryIsCollect",data);//查询某个用户是否收藏店铺
					if(collect!=null){
						res.put("isCollect","1");//已经收藏店铺
					}else{
						res.put("isCollect","0");//没有收藏店铺
					}
				}else{
					res.put("msg","请先登录！"); 
				} 
			}catch(Exception e){
				e.printStackTrace();
			} 
			return JSONArray.toJSONString(res).toString();
		}
		return null; 
	}
	
	public Object push(PageData user,PageData message){  
		Object ob=0;
		String content = message.getString("CONTENT");
		String jsonStr = "{'type':'notice','title':'上海建联','content':'"+content+"'}";//透传内容
		TransmissionTemplate tmTemplate = PushUtil.transmissionTemplateDemo("上海建联",content,jsonStr);
		PushUtil pushApp=new PushUtil();
		String alias = user.getString("ID");
		try {
			message.put("ID",UuidUtil.get32UUID());
			message.put("CREATE_DATE",new Date());  
			ob=dao.save("AppNotiMapper.save",message);
			pushApp.pushToSingle(tmTemplate,alias);  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return ob;
	} 
	
	/**
	 * 删除订单/取消订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editOrderStatus(PageData pd) throws Exception{
		PageData data = pd.getObject("params"); 
		if(!EmptyUtil.isNullOrEmpty(data)){ 
			Object ob=dao.update("DpOrderMapper.editStatus",data); 
			if(Integer.valueOf(ob.toString())>0){ 
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
	
	/**
	 * 下单成功的客服电话
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String ServiceTel(PageData pd) throws Exception{
		PageData tel =(PageData) this.dao.findForObject("ServiceTelMapper.findBm","03");//查询商城下单页面的客服电话
		return JSONArray.toJSONString(tel).toString(); 
	}
}
