package org.yx.cache;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * Created by zhangwei on 2017/7/30.
 */
public interface ICache<T,PK extends Serializable>  {
	 
         void init( T t);
         void reload(T t);
         T get(PK id);
         boolean isInit();
         int getVersion();
         void setVerdion(int version);
//       void clear();
}
