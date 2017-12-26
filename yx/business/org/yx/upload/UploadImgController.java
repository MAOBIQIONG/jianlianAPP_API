 package org.yx.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.Base64;
import org.yx.common.utils.ConfConfig;
import org.yx.common.utils.ReturnCode;

@Controller
@RequestMapping({ "/appUpload"})
public class UploadImgController extends BaseController {  
	
	//上传文件存放路径
	public static String clientBasePath = ConfConfig.getString("UPLOAD_PATH");
	 
	 @RequestMapping(value={"/imgBase64"}) 
	 @ResponseBody
	 public PageData imgBase64() throws Exception{ 
		PageData _result=new PageData();
		PageData pa=getPageData(); 
		String img=pa.getString("img"); 
        img=img.substring(img.lastIndexOf(",")+1);
    	if (img == null){//图像数据为空  
    	    _result.put("code", ReturnCode.FAIL);
    	    _result.put("msg", "失败");
		}else{
			 
			//Base64解码  
            byte[] b = Base64.decodeBase64(img);  
            for(int i=0;i<b.length;++i){  
                if(b[i]<0){//调整异常数据
                    b[i]+=256;  
                }  
            }  
            //生成png图片   
            String imgName=get32UUID()+".png"; 
            String imgFilePath =clientBasePath+"image/"+imgName; //新生成的图片 
            File file=new File(imgFilePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                  file.getParentFile().mkdirs();
                }
                  file.createNewFile();
              }
            OutputStream out = new FileOutputStream(file);  
            out.write(b);  
            out.flush();  
            out.close();
            _result.put("code", ReturnCode.SUCCESS);
            _result.put("result", "image/"+imgName);
            _result.put("msg", "成功");
		 } 
         return _result;
	}  
}
