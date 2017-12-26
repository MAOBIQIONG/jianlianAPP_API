package org.yx.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.ReturnCode;
import org.yx.services.common.ImgUploadService;
import org.yx.util.ConstantUtil;
import org.yx.util.ImgUploadUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
 
@Controller
@RequestMapping({ "/multImgsUpload"})
public class MuilImgsUploadController extends BaseController {  
	
	ImgUploadUtil imgUpload=new ImgUploadUtil();
	 
	@Resource(name = "imgUploadService")
	private ImgUploadService imgUpService; 
	
	 @RequestMapping(value={"/addPyq"}) 
	 @ResponseBody
	 public PageData addPyq(){ 
		 PageData result=new PageData();
		 long startTime=System.currentTimeMillis();
		 logBefore(this.logger,"pyq","addPyq","开始时间："+startTime);  
		 try{  
			 PageData pa=new PageData(getRequest()); 
			 //PageData param=pa.getObject("param"); 
			 //List<PageData> imgs=(List<PageData>)data.get("imgs"); 
			 PageData data=imgUpload.imgBase64(pa); 
			 result=imgUpService.editPyq(data); 
			 
			 long third=System.currentTimeMillis();
			 long thirdtime=third-startTime;
			 logAfter(this.logger,"pyq","addPyq","耗费时间："+thirdtime+" 毫秒 ，合计"+(thirdtime/1000.00) +"秒！");
			
			 return  result; 
		 }catch(Exception e){
			e.printStackTrace();
			logger.error("图片上传错误：",e); 
			 result.put("code",ReturnCode.SERVICE_FAIL);
			return  result;
		 }  
	}  
}
