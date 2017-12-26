package org.yx.util;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yx.common.entity.PageData;
import org.yx.common.utils.ConfConfig;
import org.yx.common.utils.DateUtil;
import org.yx.common.utils.FileDownload;
import org.yx.controller.AppController;

@Controller
@RequestMapping({ "/appdownfile" })
public class AppDownFile extends AppController{
	//上传文件存放路径
	public static String imgBasePath = ConfConfig.getString("imgBasePath");
	
	//图片下载
	@RequestMapping({ "/downFile" })
	@ResponseBody
	public void downFile(HttpServletResponse response) throws Exception {	 
		PageData pa = getPageData(); 
		String filePath = imgBasePath + pa.getString("FILE_NAME");	
		FileDownload.fileDownload(response,filePath,pa.getString("FILE_NAME"));
	}
	
	
}
