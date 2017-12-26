package org.yx.services.cyyq.inter;   
import java.sql.SQLException;

import org.yx.common.entity.PageData;  
import org.yx.common.utils.EmptyUtil;

import com.alibaba.fastjson.JSONArray;
public interface AppFwuServiceInter { 
	
	/**
	 * 查询服务信息详情用于修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryfwxqToEdit(PageData pd) throws Exception;
	/**
	 * 查询服务信息详情用于修改(新）
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryNewfwxqToEdit(PageData pd);
	
	/**
	 * 园区服务信息填写+修改
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String editFw(PageData pd) throws Exception;
	
	/**
	 * 查询我的服务信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String querymyfwlist(PageData pd) throws Exception;
	
	/**
	 * 服务信息列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryfwlists(PageData pd) throws Exception;
	
	/**
	 * 服务信息列表（分页）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryfwlxlists(PageData pd) throws Exception;
	
	/**
	 * 服务信息详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */ 
	public String queryfwxq(PageData pd) throws Exception;
	
	/**
	 * 园区服务信息删除
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String delete(PageData pd) throws Exception;
		 
}
