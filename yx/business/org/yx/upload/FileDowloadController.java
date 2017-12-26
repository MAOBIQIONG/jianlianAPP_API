package org.yx.upload;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.ConfConfig;
import org.yx.common.utils.FileDownload;



/**
 * 图片下载
 * @author Administrator
 *
 */

@Controller
@RequestMapping({ "/fileDowload" })
public class FileDowloadController extends BaseController{
	
	/**
	 * 下载文件
	 */
	@RequestMapping({"/file"})
	public void dowloadImg(HttpServletResponse response) throws Exception{
		PageData pd = new PageData();
		pd = getPageData();
		String imgpath = new String(pd.getString("filepath").getBytes("iso8859-1"),"utf-8");
		String fileName = imgpath.substring(imgpath.lastIndexOf("/")+1,imgpath.length());
		//System.out.println("==========查询图片=========");
		FileDownload.fileDownload(response, ConfConfig.getString("UPLOAD_PATH") + imgpath, fileName);
	}
}
