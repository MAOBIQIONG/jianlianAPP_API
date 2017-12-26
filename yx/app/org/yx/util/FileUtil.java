package org.yx.util;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


/**
 * 文件处理工具类
 * @author Administrator
 *
 */
public class FileUtil {
	private static final Logger logger = Logger.getLogger(FileUtil.class);
	
	public static boolean deleteDir(String path){
//		logger.debug("删除目录："+path);
        File tmp = new File(path);
        if (tmp.exists()) {
            if (tmp.isDirectory()) { 
                for (File file: tmp.listFiles()) {
                    if (file.isDirectory()) {
                        if (!deleteDir(file.getAbsolutePath())) return false;
                    } else {
                    	if(!file.delete()) return false;
                    }
                }
                return tmp.delete();
            } else return tmp.delete();
        }
        return true;
    }
	
	public static boolean deleteFiles(String path){
		if(deleteDir(path)){
			File tmpfile = new File(path);
			if(!(tmpfile.exists()&&tmpfile.isDirectory())){
				return tmpfile.mkdirs();
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	   
	  public static void createFile(String path, byte[] content) throws IOException { 
		  createFile(path,true);     
          FileOutputStream fos = new FileOutputStream(path);  
    
          fos.write(content);  
          fos.close();  
      }  

	
	public static boolean isValidFileName(String fileName) throws Exception{
		if (fileName == null || fileName.length() > 255)
			return false;
		else
			return fileName
					.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
	}
	
	public static String normalFileName(String fileName) throws Exception{
		try {
			if(isValidFileName(fileName))
				return fileName;
			String mat = "[\\s\\\\/:\\*\\?\\\"<>\\|]";
			fileName = fileName.replaceAll(mat, "_");
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	
	/**
     * 向指定文件中写入一个字符串
     * @param info        要写入的字符串
     * @param append      true续写，false重写
     * @return            成功返回true,失败返回false
     * @throws Exception
     */
    public static boolean printlnFile(File file,String info,boolean append) throws Exception{
        boolean result = false;
        if(file.exists()){
            if(file.canWrite()){
                PrintWriter output = null;
                try {
                    output = new PrintWriter(new FileOutputStream(file, append));
                    output.println(info);
                    output.flush();
                    output.close();
                    result = true;
                }
                catch (FileNotFoundException ex) {
                    throw ex;
                }finally{
                    if(output != null){
                        output.close();
                    }
                }
            }else{
                throw new IOException("文件为只读文件！");
            }
        }else{
            throw new FileNotFoundException("文件不存在！");
        }
        return result;
    }
	 
	
	/**
	 * 
	 * @param source 
	 * @param target
	 * @throws Exception
	 */
	public static boolean copy(String source, String target) {
		boolean bool = false;
		FileInputStream fis=null;
		BufferedInputStream bis = null;
		FileOutputStream fos =null;
		BufferedOutputStream bos=null;
		try{
			File sourceFile = new File(source);
			File targetFile = new File(target); 
			File directory = targetFile.getParentFile();
			//File directory = new File(target.substring(0, target.lastIndexOf("/")));
			if (sourceFile.exists()) {
				if (!targetFile.exists()) {
					if (targetFile.isDirectory()) {
						 bool= directory.mkdirs();
						if(!bool){
							logger.error("目录"+targetFile.getAbsolutePath()+"创建失败!");
							return bool;
						}
					}
					targetFile.createNewFile();
				}

			fis = new FileInputStream(sourceFile);
			bis = new BufferedInputStream(fis);
			fos = new FileOutputStream(targetFile);
			bos = new BufferedOutputStream(fos);
				int count=0;
				byte[] buffer = new byte[1024];
				while ((count=bis.read(buffer)) > 0) {
					bos.write(buffer,0,count);
				}
				bos.close();
				bos=null;
			bool=true;
			} else{ 
				logger.error("source file not exist!");
			} 
		}catch(IOException e){
			e.printStackTrace();
			logger.error("IOException",e);
		}finally{ 
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(bos!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bool;
	}
		

	/**
	 * 
	 * @param dir
	 * @param filepath
	 */
	public static boolean copyFilesToDir( String filepath,String dir) {
		boolean result = false;
		File file = new File(filepath);
		result=createFile(dir, false);
		if (file.isDirectory()) {
			result=copyFileToDir(dir, listFile(file));
		}else{
			result=false;
		}
		return result;
	}


	/**
	 * 
	 * @param targetDir
	 * @param filePath
	 */
	public static boolean copyFileToDir(String targetDir, String[] filePath) {
		boolean result=true;
		if (targetDir == null || "".equals(targetDir)) {
			return false;
		}
		File targetFile = new File(targetDir);
		if (!targetFile.exists()) {
			 result=targetFile.mkdir();
			if(!result){
				logger.error("目录"+targetFile.getAbsolutePath()+"创建失败!"); 
			}
		} else {
			result=targetFile.isDirectory();
//			if (!targetFile.isDirectory()) {
//				return false;
//			}
		}
		if(result){
			for (String path : filePath) {
				File file = new File(path);
				if (file.isDirectory()) {
					result=copyFileToDir(targetDir + "/" + file.getName(), listFile(file));
				} else {
					result=copyFileToDir(targetDir, file, "");
				}
			}
		}
		
		return result;
	}

	/**
	 * 
	 * @param targetDir
	 * @param file
	 * @param newName
	 */
	public static boolean copyFileToDir(String targetDir, File file, String newName) {
		boolean bool=false;
		String newFile = "";
		if (newName != null && !"".equals(newName)) {
			newFile = targetDir + "/" + newName;
		} else {
			newFile = targetDir + "/" + file.getName();
		}
		File tFile = new File(newFile);
		bool=copyFile(tFile, file);
		return bool;
	}

	/**
	 * 
	 * @param targetFile
	 * @param file
	 */
	public static boolean copyFile(File targetFile, File file) {
		boolean bool =true;
		if (!targetFile.exists()) { 
			bool=createFile(targetFile, true);
		}
		if(bool){
			InputStream is =null;
			FileOutputStream fos=null;
			int num=0;
			try {
				is = new FileInputStream(file);
				fos = new FileOutputStream(targetFile);
				byte[] buffer = new byte[1024];
				while ((num=is.read(buffer)) != -1) {
					fos.write(buffer,0,num);
				}
				fos.flush();
				fos.close();
				fos=null;
				is.close();
				is=null; 
			} catch (FileNotFoundException e) {
				bool=false;
				e.printStackTrace();
			} catch (IOException e) {
				bool=false;
				e.printStackTrace();
			}finally{
				if(null!=is){
					try {
						is.close();
					} catch (IOException e) {
						logger.error("关闭输入流异常：",e);
					}
				}
				if(null!=fos){
					try {
						fos.close();
					} catch (IOException e) {
						logger.error("关闭输出流异常：",e);
					}
				}
						
			}
		} 
		return bool;
	}

	public static String[] listFile(File dir) {
		String absolutPath = dir.getAbsolutePath();
		String[] paths = dir.list();
		String[] files = new String[paths.length];
		for (int i = 0; i < paths.length; i++) {
			files[i] = absolutPath + File.separator + paths[i];
		}
		return files;
	}

	public static boolean createFile(String path, boolean isFile) {
		boolean result=false;
		result=createFile(new File(path), isFile);
		return result;
	}

	public static boolean createFile(File file, boolean isFile) {
		boolean result=true;
		if (!file.exists()) { 
				if (isFile) {
					if (!file.getParentFile().exists()) {
						result=file.getParentFile().mkdirs();
					}
					if(result){
						try {
							result=file.createNewFile();
						} catch (IOException e) {
							result=false;
							logger.error("文件"+file.getAbsolutePath()+"创建失败!"+e);
							e.printStackTrace();
						}
					}
				} else {
					result=file.mkdirs();
					if(!result){
						logger.error("目录"+file.getAbsolutePath()+"创建失败!");
					}
				} 
		}
		return result;
	}
	/**
	 * 获取文件内容
	 * @param path
	 * @return
	 */
	public static String getFileContent(String path){
		 StringBuffer sb=new StringBuffer();
		 sb.append("");
		 BufferedReader buf = null;
		 InputStreamReader isr = null;
		 InputStream in=null;
		try {  
			in=FileUtil.class.getResourceAsStream(path); 
			isr = new InputStreamReader(in);
			buf = new BufferedReader(isr); 
			String line = null; 
			while((line = buf.readLine()) != null){
				sb.append(line+"\r\n");
			} 
		
		} catch (IOException e) {
			logger.error("获取文件内容异常：",e);
		}finally{
			if(null!=buf){
				try {
					buf.close();
				} catch (IOException e) { 
					logger.error("", e);
				}
				buf=null;
			}
			if(null!=isr){
				try {
					isr.close();
				} catch (IOException e) { 
					logger.error("", e);;
				}
				isr=null;
			}
			if(null!=in){
				try {
					in.close();
				} catch (IOException e) {
					logger.error("", e);
				}
				in=null;
			}
		}
			return sb.toString();
	 }
	/**
	 * 读取 .properties 文件返回Properties对象
	 * @param path
	 * @param classLoader
	 * @return
	 */
	public static Properties getPropertiesContent(String path,ClassLoader classLoader){
		Properties prop = new Properties(); 
		 InputStream in=null;
		 try { 
				if(classLoader!=null){ 
					in=classLoader.getResourceAsStream(path); 
					prop.load(in);
				}		
			} catch (IOException e) {
				logger.error("获取文件内容异常：",e);
			}finally{ 
				if(null!=in){
					try {
						in.close();
					} catch (IOException e) {
						logger.error("", e);
					}
					in=null;
				}
			}
		return prop;
	}
	/**
	 * 读取 .properties 文件返回Map对象
	 * @param path 相对路径
	 * @param classLoader
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String,String> getMapFromProperties(String path,ClassLoader classLoader){
		Map<String,String> map = new HashMap<String,String>();
		Properties prop = getPropertiesContent(path,classLoader);
		Iterator it=prop.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			String value=prop.getProperty(key);
			map.put(key, value);
		}
		return map;  
	}
	/**
	 * 读取 .properties 文件返回Map对象
	 * @param path 绝对路径
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<String,String> getMapFromProperties(String path){
		FileInputStream fis = null;
		HashMap<String,String> map = new HashMap<String,String>();
		try {
			Properties props = new Properties();
			fis = new FileInputStream(path);
			props.load(fis);
			Iterator it=props.keySet().iterator();
			while(it.hasNext()){
				String key = (String) it.next();
				String value=props.getProperty(key);
				map.put(key, value);
			}
			
		} catch (Exception e) {
			logger.error("项目config.properties文件加载异常", e);
		} finally {
			if(null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
		return map;
	}
	
	/**
	 * 读取 .properties 文件返回Map对象
	 * @param path 绝对路径
	 * @return
	 */
	public static HashMap<String,String> getMapFromPropertiesFile(String path,String charSet){
		HashMap<String,String> map = new HashMap<String,String>();
		InputStream is = null; 
		BufferedReader buf = null;
		InputStreamReader isr = null;
		String text;
		try {
			is=new FileInputStream(path); 
			if(charSet!=null){
				isr = new InputStreamReader(is,charSet);
			}else{
				isr = new InputStreamReader(is);
			}
			buf = new BufferedReader(isr);
			while ((text = buf.readLine()) != null) {
				if(!text.startsWith("#")){
					String temparr[]=text.split("=");
					map.put(temparr[0], temparr[1]);
				} 
			}
		} catch (IOException e) {
			logger.error("读取文件内容异常："+e);
		}
		return map;
	}
	/**
	 * 按指定编码读取文件内容
	 * @param path 文件路径
	 * @param charSet 编码
	 * @return
	 */
	public static String getFileContext(String path,String charSet){
		InputStream is = null;
		StringBuffer sbf = new StringBuffer(); 
		BufferedReader buf = null;
		InputStreamReader isr = null;
		String text;
		try {
			is=new FileInputStream(path); 
			if(charSet!=null){
				isr = new InputStreamReader(is,charSet);
			}else{
				isr = new InputStreamReader(is);
			}
			buf = new BufferedReader(isr);
			while ((text = buf.readLine()) != null) {
				sbf.append(text);
			}
		} catch (IOException e) {
			logger.error("读取文件内容异常："+e);
			return "";
		}
		return sbf.toString();
	}
	
	public static boolean writeFile(String fileName,String fileContent,String charSet){
		boolean bool=true;
		File file = new File(fileName);
		FileOutputStream fo=null;
		try{
			if(!(file.exists()&&file.isFile())){ 
				bool=FileUtil.createFile(fileName, true);
			} 
			if(bool){
				fo=new FileOutputStream(fileName);	
				byte [] bb;
				if(charSet!=null){
					bb=fileContent.getBytes(charSet);
				}else{
					bb=fileContent.getBytes();
				}
				fo.write(bb,0,bb.length);
				fo.close();
				fo=null;
			}
		}catch (IOException e){
			bool=false;
			e.printStackTrace();
		}finally{
			if(null!=fo){
				try {
					fo.close();
					fo=null;
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
		
		return bool;
	}
	  
	  
	  
	/**
	 * 使用apache.commons.fileupload创建文件对象
	 * 
	 * @author liugang
	 * @param is
	 * @param path
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static File create(InputStream is, String path, String fileName)
			throws Exception {
		File file = new File(path + fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] b = new byte[1024];
			while (is.read(b) != -1) {
				fos.write(b);
			}
			is.close();
			fos.close();
		} catch (FileNotFoundException fe) {
			new File(path).mkdirs();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] b = new byte[1024];
			while (is.read(b) != -1) {
				fos.write(b);
			}
			is.close();
			fos.close();
		} catch (Exception e) {
			file = null;
		}
		return file;
	}
	
	 public byte[] getContent(String filePath) throws IOException {  
	      File file = new File(filePath);  
	      long fileSize = file.length();  
	      if (fileSize > Integer.MAX_VALUE) {  
	          System.out.println("file too big...");  
	          return null;  
	      }  
	      FileInputStream fi = new FileInputStream(file);  
	      byte[] buffer = new byte[(int) fileSize];  
	      int offset = 0;  
	      int numRead = 0;  
	      while (offset < buffer.length  
	      && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {  
	          offset += numRead;  
	      }  
	      // 确保所有数据均被读取  
	      if (offset != buffer.length) {  
	      throw new IOException("Could not completely read file "  
	                  + file.getName());  
	      }  
	      fi.close();  
	      return buffer;  
	  }  


    /* the traditional io way 
    *  
    * @param filename 
    * @return 
    * @throws IOException 
    */  
   public static byte[] toByteArray(String filename) throws IOException {  
 
       File f = new File(filename);  
       if (!f.exists()) {  
           throw new FileNotFoundException(filename);  
       }  
 
       ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());  
       BufferedInputStream in = null;  
       try {  
           in = new BufferedInputStream(new FileInputStream(f));  
           int buf_size = 1024;  
           byte[] buffer = new byte[buf_size];  
           int len = 0;  
           while (-1 != (len = in.read(buffer, 0, buf_size))) {  
               bos.write(buffer, 0, len);  
           }  
           return bos.toByteArray();  
       } catch (IOException e) {  
           e.printStackTrace();  
           throw e;  
       } finally {  
           try {  
               in.close();  
           } catch (IOException e) {  
               e.printStackTrace();  
           }  
           bos.close();  
       }  
   }  
 

	
	 /**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
    public static void readTxtFile(String filePath){
        try {
                String encoding="GBK";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){            	
                    	String [] appdpiResults=lineTxt.split("	");
                    	 System.out.println("---"+appdpiResults[0]+"---");
                    	 System.out.println("---"+appdpiResults[1]+"---");
                    	 System.out.println("---"+appdpiResults[2]+"---");
                    	 System.out.println("---"+appdpiResults[3]+"---");
                    	 System.out.println("---"+appdpiResults[4]+"---");         	
                    	 System.out.println("============================华丽的分割线");
                    }
                    read.close();
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }
    
    /**
     * 根据返回的XML文件 获取流量使用情况
     * @param xmlDoc
     * @return
     */
	public static int getRatableUsed(String xmlDoc) {
		Document doc = null;
		int RatableUsed=0;//已使用总流量
		int RatableUsed_MY=0;//漫游流量
		int RatableUsed_JB=0;//加包流量
		try {
			String RatableUsed_MY_STR="";//漫游流量
			String RatableUsed_JB_STR="";//加包流量
			// 下面的是通过解析xml字符串的
			doc = DocumentHelper.parseText(xmlDoc); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			Iterator iter = rootElt.elementIterator("Body"); // 获取根节点下的子节点BODY
			// 遍历head节点
			while (iter.hasNext()) {
				Element recordEle = (Element) iter.next();
				Iterator SgwQueryAck = recordEle.elementIterator("SgwQueryAck"); //拿到BODY节点下的子节点SgwQueryAck
				while(SgwQueryAck.hasNext()){
					Element recordEle2 = (Element) SgwQueryAck.next();
					Iterator SgwQueryAck2 = recordEle2.elementIterator("DATA"); //拿到SgwQueryAck节点下的子节点DATA
					while(SgwQueryAck2.hasNext()){
						Element recordEle3 = (Element) SgwQueryAck2.next();
						Iterator ProductOffInfo = recordEle3.elementIterator("ProductOffInfo"); // 拿到DATA节点下的子节点ProductOffInfo
						while(ProductOffInfo.hasNext()){
							Element itemEle = (Element) ProductOffInfo.next();
							String RatableResourceName = itemEle.elementTextTrim("RatableResourceName"); // 拿到ProductOffInfo下的RatableResourceName的值
							//三种情形
							if(RatableResourceName.contains("本地及国内漫游上网")){
								 RatableUsed_MY_STR=itemEle.elementTextTrim("RatableUsed");
							}
							if(RatableResourceName.contains("本地上网（不含wifi）")){
								 RatableUsed_JB_STR=itemEle.elementTextTrim("RatableUsed");
							}						
						}					
					}				
				}
			}
			if(RatableUsed_MY_STR.length()!=0){
				RatableUsed_MY=Integer.parseInt(RatableUsed_MY_STR);
			}
			if(RatableUsed_JB_STR.length()!=0){
				RatableUsed_JB=Integer.parseInt(RatableUsed_JB_STR);			
			}
			//计算总流量
			RatableUsed=RatableUsed_JB+RatableUsed_MY;
			
		} catch (DocumentException e) {
			logger.error("解析IT部返回的流量接口XML异常", e);

		} catch (Exception e) {
			logger.error("根据IT部返回的流量接口XML获取手机流量信息异常", e);
		}
		return RatableUsed;
	}
	
	
	/**
	 * 解析流量返回信息(错误格式)
	 * @param xmlDoc
	 * @return
	 */
	public static String getErrorMes_RatableUsed(String xmlDoc) {
		String errorMes = "";
		Document doc = null;
		try {
			// 下面的是通过解析xml字符串的
			doc = DocumentHelper.parseText(xmlDoc); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			Iterator iter = rootElt.elementIterator("Body"); // 获取根节点下的子节点BODY
			// 遍历head节点
			while (iter.hasNext()) {
				Element recordEle = (Element) iter.next();
				Iterator SgwQueryAck = recordEle.elementIterator("SgwQueryAck"); // 拿到BODY节点下的子节点SgwQueryAck
				while (SgwQueryAck.hasNext()) {
					Element itemEle = (Element) SgwQueryAck.next();
					String rc = itemEle
							.elementTextTrim("RC"); // 拿到SgwQueryAck下的RC的值
					String ds = itemEle
							.elementTextTrim("DS"); // 拿到SgwQueryAck下的RC的值
					errorMes="错误编码:  "+rc+"  错误信息内容为："+ds;
				}

			}

		} catch (DocumentException e) {
			logger.error("解析IT部返回的流量接口XML(错误格式)异常", e);

		} catch (Exception e) {
			logger.error("根据IT部返回的流量接口XML获取手机流量信息(错误格式)异常", e);
		}
		return errorMes;
	}
	
	/**
	 * 根据返回的XML获取手机号
	 * @param xmlDoc
	 * @return
	 */
	public static String getPhoneNumber(String xmlDoc) {
		String phoneNumber = "";
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xmlDoc); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			Iterator iter = rootElt.elementIterator("Body"); // 获取根节点下的子节点
			while (iter.hasNext()) {
				Element recordEle = (Element) iter.next();
				Iterator CSBThroughCallResponse = recordEle
						.elementIterator("CSBThroughCallResponse"); // 获取Body节点下的CSBThroughCallResponse节点
				while (CSBThroughCallResponse.hasNext()) {
					Element recordEle2 = (Element) CSBThroughCallResponse
							.next();
					Iterator Data = recordEle2.elementIterator("Data"); // 获取CSBThroughCallResponse节点下的Data节点
					while (Data.hasNext()) {
						Element recordEle3 = (Element) Data.next();
						phoneNumber = recordEle3.elementTextTrim("DataValue"); // 拿到Data下的子节点script下的字节点DataValue的值
					}
				}
			}
		} catch (DocumentException e) {
			phoneNumber = "";
			logger.error("根据IMSI解析IT部返回XML来获取手机号X异常", e);

		} catch (Exception e) {
			phoneNumber = "";
			logger.error("根据IMSI解析IT部返回XML来获取手机号接口异常", e);
		}
		return phoneNumber;
	}
    
    
    
    public static void main(String args[]) throws Exception{  
		 String filePath= "E:/prod_result.txt";
		 FileUtil fu=new FileUtil();
		 fu.readTxtFile(filePath);
    }
}
