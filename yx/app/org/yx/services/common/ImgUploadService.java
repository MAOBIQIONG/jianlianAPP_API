package org.yx.services.common;

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
import org.yx.util.ConstantUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Service("imgUploadService")
public class ImgUploadService { 
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	Logger log=Logger.getLogger(this.getClass());//日志信息
	
	/**
	 * 发布朋友圈信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public PageData editPyq(PageData data) throws Exception { 
		PageData res=new PageData(); 
		if(EmptyUtil.isNullOrEmpty(data)){
			log.error("【editPyq】 参数为空！");
			res.put("code",ConstantUtil.RES_FAIL);
			res.put("msg","发布失败！");
			return res ; 
		}  
		String id=UuidUtil.get32UUID();
		data.put("ID", id);
		data.put("PUBLISH_DATE", new Date()); 
		data.put("COMMENT_COUNT", 0); 
		data.put("LIKE_COUNT", 0); 
		data.put("SHARE_COUNT", 0); 
		data.put("STATUS","01"); //提交 
		boolean bl=dao.save_("AppPyqMapper.save",data); 
		if(!bl){
			res.put("code",ConstantUtil.RES_FAIL);
			res.put("msg","发布失败！");
			return res ; 
		}
		int mun=0; 
		JSONArray arrlist = JSON.parseArray(data.get("imgs") + "");
		if(arrlist!=null&&arrlist.size()>0){
			for(int i=0;i<arrlist.size();i++){ 
				PageData img = new PageData(arrlist.getJSONObject(i));
				img.put("ID",UuidUtil.get32UUID());
				img.put("RELATED_ID", id);
				Object ob=dao.update("AppPyqTpMapper.save",img);
				mun+=Integer.valueOf(ob.toString());
			}  
			if(mun==arrlist.size()){  
				res.put("msg","发布成功！"); 
			}else{
				res.put("msg","图片上传失败！"); 
			}
		}else{
			if("200".equals(data.getString("code"))){
				
			}else{
				
			}
		} 
		res.put("code",ConstantUtil.RES_SUCCESS);
		return  res; 
	}   
}
