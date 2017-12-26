package org.yx.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import org.apache.log4j.Logger;
import org.yx.common.entity.PageData;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * 图片压缩工具类
 */
public class ImageCompressUtil {
   public static Logger logger= Logger.getLogger(ImageCompressUtil.class.getName());
    /**
     * 直接指定压缩后的宽高： 
     * (先保存原文件，再压缩、上传) 
     * 壹拍项目中用于二维码压缩 
     * @param oldFile 要进行压缩的文件全路径 
     * @param width 压缩后的宽度 
     * @param height 压缩后的高度 
     * @param quality 压缩质量 
     * @param smallIcon 文件名的小小后缀(注意，非文件后缀名称),入压缩文件名是yasuo.jpg,则压缩后文件名是yasuo(+smallIcon).jpg 
     * @return 返回压缩后的文件的全路径
     */
    public static String zipImageFile(String oldFile, int width, int height,float quality, String smallIcon) {
        if (oldFile == null) {
            return null;
        }
        String newImage = null;
        try {
            /**对服务器上的临时文件进行处理 */
            Image srcFile = ImageIO.read(new File(oldFile));
            /** 宽,高设定 */
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(srcFile, 0, 0, width, height, null);
            String filePrex = oldFile.substring(0, oldFile.indexOf('.'));
            /** 压缩后的文件名 */
            newImage = filePrex + smallIcon + oldFile.substring(filePrex.length());
            /** 压缩之后临时存放位置 */
            FileOutputStream out = new FileOutputStream(newImage);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
            /** 压缩质量 */
            jep.setQuality(quality, true);
            encoder.encode(tag, jep);
            out.close();
        } catch (FileNotFoundException e) {
            logger.error("压缩文件时出现异常，文件 ："+oldFile+"不存在，异常：",e);
        } catch (IOException e) {
            logger.error("写文件时间出现IO异常，文件名："+oldFile+"，异常：",e);
        }
        return newImage;
    }

    /**
     * 保存文件到服务器临时路径(用于文件上传) 
     * @param fileName
     * @param is
     * @return 文件全路径
     */
    public static String writeFile(String fileName, InputStream is) {
        if (fileName == null || fileName.trim().length() == 0) {
            return null;
        }
        try {
            /** 首先保存到临时文件 */
            FileOutputStream fos = new FileOutputStream(fileName);
            byte[] readBytes = new byte[512];// 缓冲大小  
            int readed = 0;
            while ((readed = is.read(readBytes)) > 0) {
                fos.write(readBytes, 0, readed);
            }
            fos.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("写文件时间出现IO异常，文件名："+fileName+"，异常：",e);
        }
        return fileName;
    }

    /**
     * 等比例压缩算法：  
     * 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图 
     * @param srcURL 原图地址 
     * @param deskURL 缩略图地址 
     * @param comBase 压缩基数 
     * @param scale 压缩限制(宽/高)比例  一般用1： 
     * 当scale>=1,缩略图height=comBase,width按原图宽高比例;若scale<1,缩略图width=comBase,height按原图宽高比例 
     * @throws Exception
     * @author shenbin
     * @createTime 2014-12-16 
     * @lastModifyTime 2014-12-16 
     */
    public static void saveMinPhoto(String srcURL, String deskURL, double comBase,double scale) {
        try{
            File srcFile = new File(srcURL);
            Image src = ImageIO.read(srcFile);
            int srcHeight = src.getHeight(null);
            int srcWidth = src.getWidth(null);
            int deskHeight = 0;// 缩略图高
            int deskWidth = 0;// 缩略图宽
            double srcScale = (double) srcHeight / srcWidth;
            /**缩略图宽高算法*/
            if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
                if (srcScale >= scale || 1 / srcScale > scale) {
                    if (srcScale >= scale) {
                        deskHeight = (int) comBase;
                        deskWidth = srcWidth * deskHeight / srcHeight;
                    } else {
                        deskWidth = (int) comBase;
                        deskHeight = srcHeight * deskWidth / srcWidth;
                    }
                } else {
                    if ((double) srcHeight > comBase) {
                        deskHeight = (int) comBase;
                        deskWidth = srcWidth * deskHeight / srcHeight;
                    } else {
                        deskWidth = (int) comBase;
                        deskHeight = srcHeight * deskWidth / srcWidth;
                    }
                }
            } else {
                deskHeight = srcHeight;
                deskWidth = srcWidth;
            }
            BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
            tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); //绘制缩小后的图
            FileOutputStream deskImage = new FileOutputStream(deskURL); //输出到文件流
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
            encoder.encode(tag); //近JPEG编码
            deskImage.close();
        }catch( Exception e) {
            logger.error("生成缩略图异常，文件名："+srcURL+"，异常：",e);
        }

    }

    /**
     * 压缩为轮播缩略图
     * @param srcURL
     */
    public static String zipfile(String srcURL ) {
        String targetURL="";
        try{
            File file=new File(srcURL);
            Image srcFile = ImageIO.read(file);
            int srcHeight=srcFile.getHeight(null);
            int srcWidth = srcFile.getWidth(null);
            int height = srcHeight;
            int width = srcWidth;
            targetURL=getNewFileName(file,"_min2x");
            int size=288;
            int maxSize=150*1024;//150k
            float qualty=1f;
            //文件小于150k或者文件宽高均小于288，不做压缩
            if(srcHeight<=size||file.length()<maxSize){
                //拷贝文件
                FileUtil.copy(srcURL,targetURL);
                return targetURL;
            }else{
            
                height = size;
                width = (srcWidth*size/srcHeight);
            }
            ImageCompressUtil.zipImageFile(srcURL, width, height, qualty, "_min2x");
            return targetURL;
        }catch( Exception e) {
            logger.error("生成缩略图异常，文件名："+srcURL+"，异常：",e);
        }
        return targetURL;
    }
    
    /**
     * 压缩为轮播缩略图
     * @param srcURL
     */
    public static String zipMinfile(String srcURL ) {
        String targetURL="";
        try{
            File file=new File(srcURL);
            Image srcFile = ImageIO.read(file);
            int srcHeight=srcFile.getHeight(null);
            int srcWidth = srcFile.getWidth(null);
            int height = srcHeight;
            int width = srcWidth;
            targetURL=getNewFileName(file,"_min2x");
            int size=288;
            int maxSize=150*1024;//150k
            float qualty=1f;
            //文件小于150k或者文件宽高均小于80，不做压缩
            if(srcHeight<=size||file.length()<maxSize){
                //拷贝文件
                FileUtil.copy(srcURL,targetURL);
                return targetURL;
            }else{
            	 //文件小于300k不做压缩
                if(file.length()<300*1024){
                	height = 700;
                	width = (srcWidth*700/srcHeight);
                }else{
                	height = size;
                    width = (srcWidth*size/srcHeight);
                } 
            }
            ImageCompressUtil.zipImageFile(srcURL, width, height, qualty, "_min1x");
            return targetURL;
        }catch( Exception e) {
            logger.error("生成缩略图异常，文件名："+srcURL+"，异常：",e);
        }
        return targetURL;
    }

    /**
     * 缩略图
     * @throws Exception
     */
    public static String zipMinPhoto(String srcURL){
        String newf="";
        try{
            File f=new File(srcURL);
            String srcf = f.getAbsolutePath();
            Image srcFile = ImageIO.read(new File(srcf)); 
            newf=getNewFileName(f,"_min1x"); 
            if(null==srcFile){
                return "";
            } 
            int maxSize=100*1024;//100k
            float qualty=1f;
            //文件小于30k不做压缩
            if(f.length()<maxSize){
                //拷贝文件
                FileUtil.copy(srcURL,newf);
                return newf;
            }
            //文件小于30k不做压缩
            if(f.length()<300*1024){ 
            	ImageCompressUtil.saveMinPhoto(srcf, newf, 700, 1);
                return newf;
            }  
            ImageCompressUtil.saveMinPhoto(srcf, newf, 450, 1);
        }catch( Exception e) {
            logger.error("生成缩略图异常，文件名："+srcURL+"，异常：",e);
        }
        return newf; 
   }

    private static String getNewFileName(File file,String suffix){
        String fileName=file.getName();
        int index=fileName.indexOf(".");

        String  f1=fileName.substring(0,index);
        String  f2=fileName.substring(index,fileName.length());

        String dir=file.getParent();
        String targetURL=dir+File.separatorChar+f1+suffix+f2;
         
        return targetURL;
    }

   public static Map<String,String> compressFile(String srcURL){
       Map<String,String> fileNames=new HashMap<String,String>(); 
        File f=new File(srcURL);
       	String newFname=getNewFileName(f,"_min1x"); 
		File file=new File(newFname);
		if(!file.exists()){
			 String min1=zipMinPhoto(srcURL);
			 fileNames.put("min1",min1);
		}
		newFname=getNewFileName(f,"_min2x"); 
		file=new File(newFname);
		if(!file.exists()){ 
			 String min2=zipfile(srcURL); 
		     fileNames.put("min2",min2);
		} 
       return  fileNames;
   }


   //批量压缩，压缩已经存在的图片
    public static void compressFiles(String dir){ 
    	List<String> fileList=new ArrayList<String>();
    	fileList.add(dir);
    	while(fileList.size()>0){
	        File dirF=new File(fileList.get(0));
	        File[] ffs=dirF.listFiles();
	        fileList.remove(0);
	        if(ffs!=null&&ffs.length>0){  
		        for (File f :ffs) {
		        	//判断是否为文件夹
		        	if(f.isDirectory()){
		        		fileList.add(f.getAbsolutePath()); 
		        	}else{  
		        		if(!f.getAbsolutePath().contains("_min2x")&&!f.getAbsolutePath().contains("_min1x")){
		        			compressFile(f.getAbsolutePath());	
		        		} 
		        	} 
		        }
	        }
    	}
    }  

    public static void main(String args[]) throws Exception { 
    	  
    	  
        //String filepath="/Users/zhangwei/Desktop/pic/pp/de2747930a74473aa016b0ceccce1b19.png";
       
       
      /* String name=dir.substring(dir.lastIndexOf("/")+1);
       
       System.out.println("===name==="+name);
        */
        
        
       

//        Image srcFile = ImageIO.read(new File(filepath));
//
//        int height = srcFile.getHeight(null)/20;
//        int width = srcFile.getWidth(null)/20;
//
//
//        int percent=6;
//        int percent2=7;
//        int percent3=8;
//
//        int width_y1=width * percent ;
//        int height_y1=height * percent;
//        int width_y2=width * percent2 ;
//        int height_y2=height * percent2;
//        int width_y3=width * percent3 ;
//        int height_y3=height * percent3;
//        ImageCompressUtil.zipImageFile(filepath, width_y1, height_y1, 1f, "x");
//        ImageCompressUtil.zipImageFile(filepath, width_y2, height_y2, 1f, "x2");
//        ImageCompressUtil.zipImageFile(filepath, width_y3, height_y3, 1f, "x31");
//        ImageCompressUtil.zipImageFile(filepath, width_y3, height_y3, 0.95f, "x32");
//        ImageCompressUtil.zipImageFile(filepath, width_y3, height_y3, 0.9f, "x33");
//        ImageCompressUtil.saveMinPhoto("/Users/zhangwei/Desktop/pic/car1.jpg", "/Users/zhangwei/Desktop/pic/car50.jpg", 50, 1d);
//        ImageCompressUtil.saveMinPhoto("/Users/zhangwei/Desktop/pic/car1.jpg", "/Users/zhangwei/Desktop/pic/car100.jpg", 100, 1d);
//        ImageCompressUtil.saveMinPhoto("/Users/zhangwei/Desktop/pic/car1.jpg", "/Users/zhangwei/Desktop/pic/car200.jpg", 200, 1d);

//        testFileSize();

//        zipfiles();//压缩图片
//        zipMinPhotos();//缩略图
    	//String dir="C:/Users/ZYJ/Desktop/imgs/68bc4c82161b4eab8bb72fd043ca200b.png"; 
    	String dir="C:/Users/ZYJ/Desktop/imgs"; 
        System.out.println("------------start compressFile");
        //compressFile(dir); 
        compressFiles(dir);

    }

    /**
     * 缩略图
     * @throws Exception
     */
    static void zipMinPhotos() throws Exception{
        String dir="/Users/zhangwei/Desktop/pic/mp";
        File dirF=new File(dir);
        File[] ffs=dirF.listFiles();
        for (File f :ffs) {
            String srcf = f.getAbsolutePath();
            Image srcFile = ImageIO.read(new File(srcf));
            if(null==srcFile){
                continue;
            }
//          String newf=f.getParent()+File.separatorChar+"mini_"+f.getName();
//            double combase=110/80;
//            double combase=110/80;
            ImageCompressUtil.saveMinPhoto(srcf, srcf, 320, 1);
        }
    }




    static void zipfiles() throws Exception{
        String dir="/Users/zhangwei/Desktop/pic/pp";
        File dirF=new File(dir);
        File[] ffs=dirF.listFiles();
        for (File f :ffs){
            String newf=f.getAbsolutePath();
            File file=new File(newf);
            Image srcFile = ImageIO.read(file);
            if(null==srcFile){
              continue;
            }
            int srcHeight=srcFile.getHeight(null);
            int srcWidth = srcFile.getWidth(null);
            int height = srcHeight;
            int width = srcWidth;

            int size=288;
            System.out.println(" 文件 ："+newf +" 大小 "+file.length());
            float qualty=1f;
            if(srcHeight<=size||file.length()<150*1024){
                continue;
            }else{
                height = size;
                width = (srcWidth*size/srcHeight);
            }

//            String newimg=f.getParentFile().getPath()+File.separatorChar+"yy/"+f.getName();
//            ImageCompressUtil.zipImageFile(filepath, width*4, height*4, 1f, "x100");
              ImageCompressUtil.zipImageFile(newf, width, height, qualty, "");
//            ImageCompressUtil.zipImageFile(newf, width*9, height*9, 95f, "x2");
//            ImageCompressUtil.zipImageFile(newf, width*9, height*9, 90f, "x3");
//            ImageCompressUtil.zipImageFile(filepath, width*4, height*4, 0.9f, "x90");
        }

    }


}  