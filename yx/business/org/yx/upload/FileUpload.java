 package org.yx.upload;
 
 import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.yx.common.utils.PathUtil;
import org.yx.common.utils.UuidUtil;

 
 public class FileUpload
 {
   public static String fileUp(MultipartFile file, String filePath, String fileName)
   {
     String extName = "";
     try {
       if (file.getOriginalFilename().lastIndexOf(".") >= 0) {
         extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
       }
       copyFile(file.getInputStream(), filePath, fileName + extName).replaceAll("-", "");
     } catch (IOException e) {
       //System.out.println(e);
     }
     return fileName + extName;
   }
 
   private static String copyFile(InputStream in, String dir, String realName)
     throws IOException
   {
     File file = new File(dir, realName);
     if (!file.exists()) {
       if (!file.getParentFile().exists()) {
         file.getParentFile().mkdirs();
       }
       file.createNewFile();
     }
     FileUtils.copyInputStreamToFile(in, file);
     return realName;
   }
   
   public static String getImgpath(MultipartHttpServletRequest request){ 
		//PageData _result=new PageData();
		/*PageData pa =this.appCommonService.findtokenDetail(getPageData());
		PageData data = new PageData();*/
	   	String img="";
		MultipartFile file=request.getFile("IMG");
		if ((file != null) && (!file.isEmpty())) {
			String filePath ="uploadFiles/image/"; 
			String basePath = PathUtil.getClasspath();
			String fileName = fileUp(file,basePath+filePath,UuidUtil.get32UUID());
			img=filePath+fileName;
			//data.put("IMG", filePath+fileName); 
		}  
		return img;
		/*data.put("ID", pa.getString("ID")); 
		Object obj= this.appusersService.upimg(data);*/
 }
 }
