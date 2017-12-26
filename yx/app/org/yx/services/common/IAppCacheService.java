package org.yx.services.common;

import java.util.List;

import org.yx.common.entity.PageData;

public interface IAppCacheService {
	
	 /**
     * 获取全部缓存表的信息
     * @return
     */
    List<PageData> queryAll() ;

	List<PageData> findAll(); 
}
