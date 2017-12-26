package org.yx.upload;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.yx.common.base.BaseController;
import org.yx.common.entity.PageData;
import org.yx.common.utils.AppUtil;
import org.yx.common.utils.DateUtil;
import org.yx.common.utils.FileUpload;

@Controller
@RequestMapping(value = "/upload")
public class ImageUpload extends BaseController{
	/**
	 * 上传
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/upload")
	@ResponseBody
	public PageData handleFormUpload(MultipartHttpServletRequest request) {
		PageData pd = AppUtil.success();
		try {
			List<MultipartFile> file = request.getFiles("file");
			for (MultipartFile multipartFile : file) {
				String ffile = DateUtil.getDays();
				String fileName = "";
				PageData ufile = new PageData();
				String filePath = null;
				if ((multipartFile != null) && (!multipartFile.isEmpty())) {
					filePath =request.getSession().getServletContext().getRealPath("/") + "upload/img/" + ffile;
					fileName = FileUpload.fileUp(multipartFile, filePath, get32UUID());
					ufile.put("FILEPATH", "upload/img/" + ffile + "/" + fileName);
					ufile.put("FILENAME", fileName);
					pd.put("FILE", ufile);
				} else {
					//System.out.println("上传失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pd;
	}

}
