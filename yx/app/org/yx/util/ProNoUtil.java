package org.yx.util;
import org.yx.common.utils.DateUtil;

public class ProNoUtil {

	public static String getPartProNo(String str){
	   String lx = ChineseInital.getAllFirstLetter(str);
	   lx="PRO_"+lx+DateUtil.getTimes();
	   return lx;
	}
	
	public static String getPartResNo(){ 
	   String lx="PRO_XMXQ"+DateUtil.getTimes();
	   return lx;
	}
	
	public static String getProNo(String max){  
	   String ma=max.substring(max.length()-4,max.length());
	   Integer code=Integer.parseInt(ma);
	   code++;  
	   String len="0";
	   if(code.toString().length()==1){
		  len="000"+code.toString(); 
	   }else if(code.toString().length()==2){
		   len="00"+code.toString(); 
	   }else if(code.toString().length()==3){
		   len="0"+code.toString(); 
	   }else{
		   len=code.toString(); 
	   }
       return len;
	} 
	 
	   public static void main(String[] args) {  
			String no=ProNoUtil.getPartProNo("GRC装饰混凝土");
			String gg=no+ProNoUtil.getProNo(no+"0001"); 
	        System.out.println(no);
	        System.out.println(gg); 
	   }
	
}
