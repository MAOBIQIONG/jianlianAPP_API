package org.yx.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomNum {
	 public static String getFourRandom(){
        Random random = new Random();
        String fourRandom = random.nextInt(10000) + "";
        int randLength = fourRandom.length();
        if(randLength<4){
          for(int i=1; i<=4-randLength; i++)
              fourRandom = "0" + fourRandom  ;
      }
        return fourRandom;
    }
	 
	public static String getFiveRadom(){ 
        List list = new ArrayList();
        for (char c = 'a'; c <= 'z'; c++) {
            list.add(c);
        }
        String str = "";
        for (int i = 0; i < 5; i++) {
            int num = (int) (Math.random() * 26);
            str = str + list.get(num);
        }
        return str;
    }
	 
	public static void main(String[] args){
		String ss=RandomNum.getFiveRadom();
		System.out.println(ss);
	} 


}
