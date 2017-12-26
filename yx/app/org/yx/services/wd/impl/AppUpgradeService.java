package org.yx.services.wd.impl; 
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData; 
import org.yx.common.utils.EmptyUtil; 
import org.yx.services.wd.inter.AppUpgradeServiceInter;
import org.yx.util.ResultUtil;

import com.alibaba.fastjson.JSONArray;

@Service("appUpgradeService")
public class AppUpgradeService implements AppUpgradeServiceInter{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**
	 * 修改升级入会申请信息状态，返回查询是否完善信息--未开发
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String updateUpStatus(PageData pd) throws Exception {
		PageData data = pd.getObject("params");
		if(EmptyUtil.isNullOrEmpty(data))
			return null; 
		data.put("STATUS","01");
		boolean rest =  dao.update_("AppUpgradeMapper.editStatus",data);//修改升级入会信息的状态  
		if(!rest){ 
			return ResultUtil.failMsg("修改升级入会信息失败！");
		}else{
			PageData res=new PageData();
			PageData complete =(PageData) dao.findForObject("AppzUserMapper.queryusxx",data);
			if(complete.get("PHONE")!=null&&!"".equals(complete.get("PHONE"))&&complete.get("COMPANY_NAME")!=null&&!"".equals(complete.get("COMPANY_NAME"))&&complete.get("CLID")!=null&&!"".equals(complete.get("CLID"))&&complete.get("HYID")!=null&&!"".equals(complete.get("HYID"))&&complete.get("IMG_PATH")!=null&&!"".equals(complete.get("IMG_PATH"))){
				res.put("isComplete",1);//信息已完善
			}else{
				res.put("isComplete",0);//信息未完善
			} 
			res.put("code","200"); 
			return JSONArray.toJSONString(res).toString();   
		}  
	}

	/**
	 * 根据id查询升级入会信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public String queryById(PageData pd) throws Exception {
		PageData data = pd.getObject("params");
		if(EmptyUtil.isNullOrEmpty(data))
			return null;  
		PageData grade =(PageData) dao.findForObject("AppUpgradeMapper.queryById",data);
		grade.put("isInCancle","正在退款中，请耐心等待！"); 
		grade.put("isInCheck","正在审核中，请耐心等待！完善个人信息，可以提高审核进度及审核通过率。"); 
		return JSONArray.toJSONString(grade).toString();   
	} 
	
	/**
	 * 查询某个用户某个等级是否已经申请
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String checkIsApply(PageData pd) throws Exception{
		PageData data = pd.getObject("params");
		if(EmptyUtil.isNullOrEmpty(data))
			return null; 
		PageData res=new PageData();
		List<PageData> grades =(List<PageData>) dao.findForList("AppUpgradeMapper.checkIsApply",data);  
		if(grades!=null&&grades.size()>0){
			res.put("isApply",1);//已经申请
		}else{
			res.put("isApply",0);//未申请
		}
		return JSONArray.toJSONString(res).toString(); 
	}
	
	/**
	 * 修改订单支付状态和生成建联卡号
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String editStatusAndCardNo(PageData pd) throws Exception{ 
		return null;   
	}
}
