import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageCompressUtil {
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
    public static String zipImageFile(String oldFile, int width, int height,
                                      float quality, String smallIcon) {
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
    public static void saveMinPhoto(String srcURL, String deskURL, double comBase,
                                    double scale) throws Exception {
        File srcFile = new java.io.File(srcURL);
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
    }

    public static void main(String args[]) throws Exception {
        String filepath="/Users/zhangwei/Desktop/pic/car2.jpg";

        Image srcFile = ImageIO.read(new File(filepath));

        int height = srcFile.getHeight(null)/20;
        int width = srcFile.getWidth(null)/20;


        int percent=6;
        int percent2=7;
        int percent3=8;

        int width_y1=width * percent ;
        int height_y1=height * percent;
        int width_y2=width * percent2 ;
        int height_y2=height * percent2;
        int width_y3=width * percent3 ;
        int height_y3=height * percent3;
//        ImageCompressUtil.zipImageFile(filepath, width_y1, height_y1, 1f, "x");
//        ImageCompressUtil.zipImageFile(filepath, width_y2, height_y2, 1f, "x2");
//        ImageCompressUtil.zipImageFile(filepath, width_y3, height_y3, 1f, "x31");
//        ImageCompressUtil.zipImageFile(filepath, width_y3, height_y3, 0.95f, "x32");
//        ImageCompressUtil.zipImageFile(filepath, width_y3, height_y3, 0.9f, "x33");
//        ImageCompressUtil.saveMinPhoto("/Users/zhangwei/Desktop/pic/car1.jpg", "/Users/zhangwei/Desktop/pic/car50.jpg", 50, 1d);
//        ImageCompressUtil.saveMinPhoto("/Users/zhangwei/Desktop/pic/car1.jpg", "/Users/zhangwei/Desktop/pic/car100.jpg", 100, 1d);
//        ImageCompressUtil.saveMinPhoto("/Users/zhangwei/Desktop/pic/car1.jpg", "/Users/zhangwei/Desktop/pic/car200.jpg", 200, 1d);

//        testFileSize();

        zipfiles();//压缩图片
//        zipMinPhotos();//缩略图

    }

    public  static void zipMinPhotos() throws Exception{
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

    public  static void testFileSize() throws Exception {
        String dir = "/Users/zhangwei/Desktop/pic/pp";
        File dirF = new File(dir);
        File[] ffs = dirF.listFiles();
        for (File f : ffs) {
            String newf = f.getAbsolutePath();
            File file = new File(newf);
            System.out.println(" 文件 ：" + newf + " 大小 " + file.length() +" ..... "+150*1024);
        }
    }





    public  static void zipfiles() throws Exception{
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