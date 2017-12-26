package org.yx.services.cyyq.inter;   
import java.sql.SQLException;

import org.yx.common.entity.PageData;  
import org.yx.common.utils.EmptyUtil;

import com.alibaba.fastjson.JSONArray;
public interface AppRzServiceInter { 
	
	/**
	 * 查询我的入驻信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryRzhulist(PageData pd) throws Exception;
	
	
	/**
	 * 查询入驻信息列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryRzhulists(PageData pd) throws Exception;
	
	
	/**
	 * 入驻信息详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryRzxq(PageData pd) throws Exception; 
	
	/**
	 * 入驻园区信息填写+修改（企业、创业者）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String editRz(PageData pd) throws Exception;
	
	/**
	 * 入驻园区信息删除
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String delete(PageData pd) throws Exception;
}
