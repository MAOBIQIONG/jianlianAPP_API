package org.yx.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yx.common.entity.PageData;
import org.yx.common.utils.UuidUtil;
import org.yx.common.utils.fileConfig;

import com.alibaba.fastjson.JSON;

import Decoder.BASE64Decoder;

public class ImgUploadUtil {
	
	Logger log=Logger.getLogger(this.getClass());
	
	//上传文件存放路径
	public static String clientBasePath = fileConfig.getString("clientBasePath");
		
	//上传文件存放路径
	public static String imgBasePath = fileConfig.getString("imgBasePath");  
 
	public PageData imgBase64(PageData pa){  
		try{  
			com.alibaba.fastjson.JSONArray arrlist = JSON.parseArray(pa.get("imgs") + "");
	    	if (arrlist == null||arrlist.size()==0){//图像数据为空   
	    		pa.put("code",ConstantUtil.RES_SUCCESS); 
				log.info("没有上传图片！");
			}else{
				for(int j=0;j<arrlist.size();j++){
					PageData img = new PageData(arrlist.getJSONObject(j));
					if(img!=null&&img.getString("base64Data")!=null&&!"".equals(img.getString("base64Data"))){
						BASE64Decoder decoder = new BASE64Decoder(); 
						//Base64解码  
			            byte[] b = decoder.decodeBuffer(img.getString("base64Data"));  
			            for(int i=0;i<b.length;++i){  
			                if(b[i]<0){//调整异常数据
			                    b[i]+=256;  
			                }  
			            }  
			            //生成png图片   String name=dir.substring(dir.lastIndexOf("/")+1);
			            String name=img.getString("filePath");
			            String imgName=name.substring(name.lastIndexOf("/")+1);;//UuidUtil.get32UUID()+".png"; 
			            String imgFilePath =clientBasePath+imgName; //新生成的图片 
			            OutputStream out = new FileOutputStream(imgFilePath);  
			            img.put("IMG_PATH",imgName);
			            img.put("ORDER_BY",j); 
			            img.put("filePath", null);
			            img.put("base64Data", null); 
			            out.write(b);   
			            out.flush();
			            out.close();   
			            ImageCompressUtil.compressFile(imgFilePath);
					} 
				} 
				pa.put("imgs",arrlist);
				pa.put("code", ConstantUtil.RES_SUCCESS); 
				log.info("上传图片成功！");
			 } 
		}catch(Exception e){
			e.printStackTrace();
			log.error("图片上传错误：",e);
			pa.put("code", ConstantUtil.RES_FAIL); 
			return pa;
		} 
        return pa;
	}  
	 
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest(); 
		return request;
	}
}
