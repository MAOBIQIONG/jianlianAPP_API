package org.yx.services.common;

import java.util.List;

import org.yx.common.entity.PageData;

public interface ICategoryService {
	
	 /**
     * 获取全部行业的信息
     * @return
     */
    List<PageData> listByPId() ; 
}
