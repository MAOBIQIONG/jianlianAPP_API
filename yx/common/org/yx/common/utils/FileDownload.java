 package org.yx.common.utils;
 
 import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;
 
 public class FileDownload
 {
   public static void fileDownload(HttpServletResponse response, String filePath, String fileName)
     throws Exception
   {
     byte[] data = FileUtil.toByteArray2(filePath);
     fileName = URLEncoder.encode(fileName, "UTF-8");
     response.reset();
     response.addHeader("Content-Length", "" + data.length);
     response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
     System.out.println(data.length);
     
     response.setContentType("application/x-msdownload;charset=UTF-8");
     OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
     outputStream.write(data);
     outputStream.flush();
     outputStream.close();
     response.flushBuffer();
   }
 }
