package org.yx.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

import javax.imageio.ImageIO;

import org.apache.shiro.crypto.hash.SimpleHash;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class ChineseToEnglish {

	public static String getPingYinToHeaderChar(String str){
		//String src="";
	    char[] t1 = null;  
	    t1 = str.toCharArray();  
	    String[] t2 = new String[t1.length];  
	    HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();  
	      
	    t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
	    t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
	    t3.setVCharType(HanyuPinyinVCharType.WITH_V);  
	    String t4 = "";  
	    int t0 = t1.length;  
	    try {  
	        for (int i = 0; i < t0; i++) {  
	            // 判断是否为汉字字符  
	            if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {  
	                t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);  
	                t4 += t2[0];  
	            } else {
	                t4 += java.lang.Character.toString(t1[i]);  
	            }  
	        }
	        if(t4!=null&&!"".equals(t4)){
	        	t4=t4.toUpperCase().substring(0,1); 
	        }
	    } catch (BadHanyuPinyinOutputFormatCombination e1) {  
	        e1.printStackTrace();  
	    }  
	    return t4;
	}  
	
	public  static void main(String args[]){
		
		/*BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new File("http://wx.shanghai-cu.com/jianlian/uploadImg/server/d08892d95abf4e8ca1a3161561afd965.jpg"));
			int width = bufferedImage.getWidth();   
	    	int height = bufferedImage.getHeight();  
	    	System.out.println("width:"+width+"====="+"height:"+height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   */
    	
		String password=new SimpleHash("SHA-1","50160800013","888888").toString();
		 //String img="1214,1213";
		 //String[] imgs=img.split(",");
		 System.out.println(password);
		 
	    
	   /* BigDecimal price=new BigDecimal("0.01");
	    //BigDecimal beishu=new BigDecimal(100);
	    BigDecimal result=price.multiply(new BigDecimal(100));
	    int d=result.intValue();
	    boolean isTrue=String.valueOf(d).equals("1"); 
	    System.out.println(isTrue);
	    System.out.println(result);
	    System.out.println(d);*/
	     
	}
}
