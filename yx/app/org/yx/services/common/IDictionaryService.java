package org.yx.services.common;

import java.util.List;
import org.yx.common.entity.PageData;

/**
 * Created by zhangwei on 2017/7/31.
 */
public interface IDictionaryService {
    /**
     * 获取全部区域城市数据
     * @return
     */
    List<PageData> findAllAreaCities() ;


    /**
     * 根据区域编码获取城市数据
     * @param code
     * @return
     */
    List<PageData> findCitiesByAreaCode(String code) ;
    
    
    /**
     * 查询全部字典表信息
     * @param code
     * @return
     */
    List<PageData> queryAllDics() ;
    
    /**
     * 查询字典表中某个类别的信息
     * @param code
     * @return
     */
    List<PageData> queryDicByParam(String name) ;
    
    /**
     * 查询全部城市建联信息
     * @param code
     * @return
     */
    List<PageData> queryAllClans() ;
    
    /**
     * 查询全部行业信息
     * @param code
     * @return
     */
    List<PageData> queryAllCates() ;
    
    /**
     * 查询全部项目类型信息
     * @param code
     * @return
     */
    List<PageData> queryAllXmxs() ;
}
