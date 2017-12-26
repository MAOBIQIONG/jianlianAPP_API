package org.yx.controller;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.ReturnCode;
import org.yx.common.utils.fileConfig;
import org.yx.util.ImageCompressUtil;

import com.alibaba.fastjson.JSONArray;

import Decoder.BASE64Decoder;
 
@Controller
@RequestMapping({ "/appUploadImg"})
public class AppUploadImgController extends BaseController {  
	
	//上传文件存放路径
	public static String clientBasePath = fileConfig.getString("clientBasePath");
	
	//上传文件存放路径
	public static String imgBasePath = fileConfig.getString("imgBasePath"); 
	
	 @RequestMapping(value={"/imgBase64"}) 
	 @ResponseBody
	 public PageData imgBase64() throws Exception{  
		long startTime=System.currentTimeMillis();
		PageData _result=new PageData();
		PageData pa=getPageData(); 
		try{
			String img=pa.getString("img");  
			logBefore(this.logger,"appUploadImg","imgBase64","上传图片");  
	    	if (img == null){//图像数据为空  
	    		_result.put("code", ReturnCode.IMG_FAIL);
				_result.put("msg", "上传图片失败！");	 
				logger.error("错误:图像数据为空,上传图片失败！");
			}else{ 
				long stage=System.currentTimeMillis();
				BASE64Decoder decoder = new BASE64Decoder();  
				//Base64解码  
	            byte[] b = decoder.decodeBuffer(img);  
	            for(int i=0;i<b.length;++i){  
	                if(b[i]<0){//调整异常数据
	                    b[i]+=256;  
	                }  
	            } 
	            //生成png图片   
	            String imgName=get32UUID()+".png"; 
	            String imgFilePath =clientBasePath+imgName; //新生成的图片 
	            OutputStream out = new FileOutputStream(imgFilePath);      
	            out.write(b);  
	            out.flush();  
	            out.close();
	            ImageCompressUtil.compressFile(imgFilePath); 
	            _result.put("result",imgName);
	            _result.put("code", ReturnCode.SUCCESS);
				_result.put("msg", "上传图片成功！");	
				logger.info(_result.getString("msg"));
			 } 
		}catch(Exception e){
			e.printStackTrace();
			logger.error("图片上传错误：",e);
			_result.put("code", ReturnCode.SERVICE_FAIL);
			_result.put("msg", "服务器繁忙，请稍后操作");
			return _result;
		}
		long endTime=System.currentTimeMillis();
		long time=endTime-startTime;
		logger.info("imgBase64消耗时间为："+time+"毫秒，共计："+time/1000+"秒！！！！！");   
        return _result;
	}  
	 
	 @RequestMapping(value={"/imgBase64_2"}) 
	 @ResponseBody
	 public PageData imgBase64_2() throws Exception{ 
		long startTime=System.currentTimeMillis();
		PageData _result=new PageData();
		PageData pa=getPageData();
		try{
			logBefore(this.logger,"appUploadImg","imgBase64_2","上传图片");  
			String img=pa.getString("img");  
	    	if (img == null){//图像数据为空  
	    		_result.put("code", ReturnCode.IMG_FAIL);
				_result.put("msg", "上传图片失败！");	
				logger.error("错误:图像数据为空,上传图片失败！");
			}else{ 
				long stage=System.currentTimeMillis();
				BASE64Decoder decoder = new BASE64Decoder();  
				//Base64解码  
	            byte[] b = decoder.decodeBuffer(img);  
	            for(int i=0;i<b.length;++i){  
	                if(b[i]<0){//调整异常数据
	                    b[i]+=256;  
	                }  
	            } 
	            //生成png图片   
	            String imgName=get32UUID()+".png"; 
	            String imgFilePath =imgBasePath+imgName; //新生成的图片 
	            OutputStream out = new FileOutputStream(imgFilePath);      
	            out.write(b);  
	            out.flush();  
	            out.close();
	            ImageCompressUtil.compressFile(imgFilePath);  
	            _result.put("result",imgName);
	            _result.put("code", ReturnCode.SUCCESS);
				_result.put("msg", "上传图片成功！");
				logger.info(_result.getString("msg"));
			 } 
		}catch(Exception e){
			e.printStackTrace();
			logger.error("图片上传错误：",e);
			_result.put("code", ReturnCode.SERVICE_FAIL);
			_result.put("code", ReturnCode.SERVICE_FAIL);
			_result.put("msg", "服务器繁忙，请稍后操作");
			return _result;
		} 
		long endTime=System.currentTimeMillis();
		long time=endTime-startTime;
		logger.info("imgBase64_2消耗时间为："+time+"毫秒，共计："+time/1000+"秒！！！！！");   
        return _result;
	} 
	 
	 @RequestMapping(value={"/audioBase64"}) 
	 @ResponseBody
	 public PageData audioBase64() throws Exception{ 
		 long startTime=System.currentTimeMillis();
		PageData _result=new PageData();
		PageData pa=getPageData();
		try{
			logBefore(this.logger,"appUploadImg","audioBase64","上传图片");  
			String img=pa.getString("img");  
	    	if (img == null){//图像数据为空  
	    		_result.put("code", ReturnCode.IMG_FAIL);
				_result.put("msg", "上传图片失败！");	
				logger.error("错误:图像数据为空,上传图片失败！");
			}else{
				long stage=System.currentTimeMillis();
				BASE64Decoder decoder = new BASE64Decoder();  
				//Base64解码  
	            byte[] b = decoder.decodeBuffer(img);  
	            for(int i=0;i<b.length;++i){  
	                if(b[i]<0){//调整异常数据
	                    b[i]+=256;  
	                }  
	            }  
	            //生成png图片   
	            String imgName=get32UUID()+".amr"; 
	            String imgFilePath =imgBasePath+imgName; //新生成的图片 
	            OutputStream out = new FileOutputStream(imgFilePath);      
	            out.write(b);  
	            out.flush();  
	            out.close();
	            ImageCompressUtil.compressFile(imgFilePath);  
	            _result.put("result",imgName);
	            _result.put("code", ReturnCode.SUCCESS);
				_result.put("msg", "上传图片成功！");
				logger.info(_result.getString("msg"));
			 } 
		}catch(Exception e){
			e.printStackTrace();
			logger.error("图片上传错误：",e);
			_result.put("code", ReturnCode.SERVICE_FAIL);
			_result.put("code", ReturnCode.SERVICE_FAIL);
			_result.put("msg", "服务器繁忙，请稍后操作");
			return _result;
		} 
		long endTime=System.currentTimeMillis();
		long time=endTime-startTime;
		logger.info("audioBase64消耗时间为："+time+"毫秒，共计："+time/1000+"秒！！！！！");   
        return _result; 
	}
}
