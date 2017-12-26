package org.yx.util;

import org.yx.common.entity.PageData;

public class PriceUtil {

	/***************计算升级入会价格******************/
	/**
	 * upLevel：升级目标等级；
	 * isOut：如果已经入会，是否超过三个月，00：超过三个月，01：未超过三个月
	 * level：用户目前等级
	 * **/
	public PageData calculatePrice(String upLevel,String isOut,String level) {
		int price = -1;//默认价格
		String msg = "成功！";
		boolean flag=true;
		String[] levels =new String[]{"02","01","03","04","05","200"};//等级
		int[] prices = new int[]{0,1000,8000,10000,20000,30000};//对应等级的价格
		int m=-1;
		for(int i=0;i<levels.length;i++){
			if(levels[i].equals(upLevel)){
				m=i;
				if("01".equals(isOut)){
					int n=-1;
					for(int j=0;j<levels.length;j++){
						if(levels[j].equals(level)){
							n=j;
							price = prices[i] - prices[j];
						}
					}
					if(n<0){
						msg = "用户等级错误！";
						flag=false;
					}
				}else if("00".equals(isOut)){
					int n=-1;
					for(int j=0;j<levels.length;j++){
						if(levels[j].equals(level)){
							n=j;
							price = prices[i];
						}
					}
					if(n<0){
						msg = "用户等级错误！";
						flag=false;
					}
				}
			}
		}
		if(m==-1){
			msg = "升级等级不存在！";
			flag=false;
		}
		PageData res=new PageData();
		res.put("price",price);
		res.put("msg",msg);
		res.put("flag",flag);
		return  res;
	} 
	 
	public static void main(String[] strs){
		//System.out.println(calculatePrice("05","00","01"));
	}
}
