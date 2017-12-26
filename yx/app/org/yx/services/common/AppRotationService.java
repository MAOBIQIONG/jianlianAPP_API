package org.yx.services.common;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.yx.common.dao.DaoSupport;
import org.yx.common.entity.PageData;

@Service("appRotationService")
public class AppRotationService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	Logger log=Logger.getLogger(this.getClass());//日志信息
	
	//根据位置编号查找要显示的轮换大图
	@Cacheable(value="imgCache",key="#pd.getString(\"LOCATION_NO\")")
	public List<PageData> queryByColno(PageData pd)throws Exception{ 
		log.info("service:【appRotationService】 function：【queryByColno】");
		return (List<PageData>) this.dao.findForList("AppRotationMapper.queryByColno", pd);
	} 
}
