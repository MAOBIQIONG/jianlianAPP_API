package org.yx.services.common;

/**
 * Created by zhangwei on 2017/7/31.
 */
public interface ICacheService {
    //初始化缓存
    void init() throws Exception;
    //重新加载缓存
    void reload();
}
