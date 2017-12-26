 package org.yx.common.dao;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.yx.common.entity.PageData;
 
 @Repository("daoSupport")
 public class DaoSupport implements DAO{
 
   @Resource(name="sqlSessionTemplate")
   private SqlSessionTemplate sqlSessionTemplate;
   
   Logger log=Logger.getLogger(this.getClass());
 
   public boolean save_(String str, Object obj){
	   boolean flag=false;
	   try{
		   int i=Integer.valueOf(this.sqlSessionTemplate.insert(str, obj));
		   if(i>=1){
			   flag=true;    
		   }  
	   }catch(Exception e){
		  log.error("方法：【"+str+"】 参数：【"+obj+"】");
		  log.error("错误信息：",e);
	   } 
	   return flag;  
   }
   
   @Override
   public Object save(String str, Object obj){
	   int i=0;
	   try{
		   i=Integer.valueOf(this.sqlSessionTemplate.insert(str, obj));   
	   }catch(Exception e){
		  log.error("方法：【"+str+"】 参数：【"+obj+"】");
		  log.error("错误信息：",e);
	   }
	   return i;
   }
 
   public Object batchSave(String str, List objs){ 
	   int i=0;
	   try{
		   i=Integer.valueOf(this.sqlSessionTemplate.insert(str, objs));  
	   }catch(Exception e){
		  log.error("方法：【"+str+"】 参数：【"+objs+"】");
		  log.error("错误信息：",e);
	   }
	   return i; 
   } 
   
   public boolean update_(String str, Object obj){
	   boolean flag=false;
	   try{
		   int i=Integer.valueOf(this.sqlSessionTemplate.update(str, obj));
		   if(i>=1){
			   flag=true;  
		   }   
	   }catch(Exception e){
		  log.error("方法：【"+str+"】 参数：【"+obj+"】");
		  log.error("错误信息：",e);
	   } 
       return flag;  
   } 
   
   @Override
   public Object update(String str, Object obj)  throws Exception { 
	   int i=0;
	   try{
		   i=Integer.valueOf(this.sqlSessionTemplate.update(str, obj)); 
	   }catch(Exception e){
		  log.error("方法：【"+str+"】 参数：【"+obj+"】");
		  log.error("错误信息：",e);
	   }
	   return i;  
   }
 
   public void batchUpdate(String str, List objs) throws Exception {
     SqlSessionFactory sqlSessionFactory = this.sqlSessionTemplate.getSqlSessionFactory();
 
     SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
       if (objs != null) {
         int i = 0; for (int size = objs.size(); i < size; i++) {
           sqlSession.update(str, objs.get(i));
         }
         sqlSession.flushStatements();
         sqlSession.commit();
         sqlSession.clearCache();
       } 
   }
 
   public Object batchDelete(String str, List objs) throws Exception  { 
	   return Integer.valueOf(this.sqlSessionTemplate.delete(str, objs)); 
   } 
    
   public boolean delete_(String str, Object obj){  
	   boolean flag=false;
	   try{
		   int i=Integer.valueOf(this.sqlSessionTemplate.delete(str, obj));
		   if(i>=1){
			   flag=true;  
		   }   
	   }catch(Exception e){
		   log.error("方法：【"+str+"】 参数：【"+obj+"】");
		   log.error("错误信息：",e);
	   } 
       return flag;  
   }
   
   @Override
   public Object delete(String str, Object obj){
	   int i=0;
	   try{
		   i=Integer.valueOf(this.sqlSessionTemplate.delete(str, obj));
	   }catch(Exception e){
		  log.error("方法：【"+str+"】 参数：【"+obj+"】");
		  log.error("错误信息：",e);
	   }
	   return i;   
   }
 
   @Override
   public Object findForObject(String str, Object obj){ 
	   try{
		   List list=this.sqlSessionTemplate.selectList(str, obj); 
		   if(list==null||list.size()<=0){ 
			   return null;
		   }else{
			   if(list!=null&&list.size()>1){
		    	   log.error("查询到多条信息，方法：【"+str+"】 参数：【"+obj+"】"); 
		    	   return  null;
		       }else{
		    	   return list.get(0);
		       }  
		   } 
	   }catch(Exception e){
		   log.error("方法：【"+str+"】 参数：【"+obj+"】");
		   log.error("错误信息：",e);
		   return  null;
	   } 
   }
 
   @Override
   public Object findForList(String str, Object obj) throws Exception { 
		return this.sqlSessionTemplate.selectList(str, obj); 
   }
 
   @Override
   public Object findForMap(String str, Object obj, String key, String value) throws Exception { 
    	return this.sqlSessionTemplate.selectMap(str, obj, key);
   }

   @Override
   public Object findForList(String paramString) throws Exception {
       return this.sqlSessionTemplate.selectList(paramString);
   } 
 }