package org.yx.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageCutUtils {
    public static void cutJPG(InputStream input, OutputStream out, int x,
                              int y, int width, int height) throws IOException {
        ImageInputStream imageStream = null;
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");
            ImageReader reader = readers.next();
            imageStream = ImageIO.createImageInputStream(input);
            reader.setInput(imageStream, true);
            ImageReadParam param = reader.getDefaultReadParam();

            System.out.println(reader.getWidth(0));
            System.out.println(reader.getHeight(0));
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, "jpg", out);
        } finally {
            imageStream.close();
        }
    }


    public static void cutPNG(InputStream input, OutputStream out, int x,
                              int y, int width, int height) throws IOException {
        ImageInputStream imageStream = null;
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("png");
            ImageReader reader = readers.next();
            imageStream = ImageIO.createImageInputStream(input);
            reader.setInput(imageStream, true);
            ImageReadParam param = reader.getDefaultReadParam();

            System.out.println(reader.getWidth(0));
            System.out.println(reader.getHeight(0));

            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, "png", out);
        } finally {
            imageStream.close();
        }
    }

    public static void cutImage(InputStream input, OutputStream out, String type,int x,
                                int y, int width, int height) throws IOException {
        ImageInputStream imageStream = null;
        try {
            String imageType=(null==type||"".equals(type))?"jpg":type;
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageType);
            ImageReader reader = readers.next();
            imageStream = ImageIO.createImageInputStream(input);
            reader.setInput(imageStream, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, imageType, out);
        } finally {
            imageStream.close();
        }
    }


    public static void cutMinImageByCenter(String srcImg, String targetImg, String type, int width, int height) throws IOException {

        ImageInputStream imageStream = null;
        FileInputStream  input =new FileInputStream(srcImg);
        FileOutputStream out=new FileOutputStream(targetImg);

        File file=new File(srcImg);
        Image srcFile = ImageIO.read(file);
        if(null==srcFile){
            return;
        }
        int srcHeight=srcFile.getHeight(null);
        int srcWidth = srcFile.getWidth(null);
        int x=0;
        int y=0;
        if(srcWidth>width){
            x=(srcWidth-width)/2;
        }
        if(srcHeight>height){
            y=(srcHeight-height)/2;
        }
        try {
            String imageType=(null==type||"".equals(type))?"jpg":type;
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageType);
            ImageReader reader = readers.next();
            imageStream = ImageIO.createImageInputStream(input);
            reader.setInput(imageStream, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, imageType, out);
        } finally {
            imageStream.close();
            input.close();
            out.close();
        }
    }


    public static void main(String[] args) throws Exception {
        String filename="d788d43f8794a4c28c10040c04f41bd5ad6e39e2_min1x";
        String path="/Users/zhangwei/Desktop/pic/pp/"+filename+".jpg";
        String path2="/Users/zhangwei/Desktop/pic/pp/"+filename+"_x.jpg";
        String path3="/Users/zhangwei/Desktop/pic/pp/"+filename+"_xx.jpg";
        String path4="/Users/zhangwei/Desktop/pic/pp/"+filename+"_xxx.jpg";

//        ImageUtils.cutImage(new FileInputStream(path),
//                new FileOutputStream(path2), "jpg",0,0,100,100);

        ImageCutUtils.cutMinImageByCenter(path,path2,"jpg",120,120);
        ImageCutUtils.cutMinImageByCenter(path,path3,"jpg",150,150);
        ImageCutUtils.cutMinImageByCenter(path,path4,"jpg",180,180);

//        ImageUtils.cutJPG(new FileInputStream(path),
//                new FileOutputStream(path2), 0,0,100,100);

//        ImageUtils.cutPNG(new FileInputStream(path),
//                new FileOutputStream(path2), 0,0,50,40);
    }
}
