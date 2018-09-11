package cn.wzgzs.springboot.utils;

import java.util.Random;

/** 
* @ClassName: GetOrderNum 
* @Description: 生成订单编号
* @author WL
* @date 2017年5月10日 下午4:16:24  
*/
public class GetOrderNum {
	
	private static Random ra =new Random();
	/**
	 * 序列值
	 */
	private static int trade_id = 100000;
	
	/** 
	* @Title: getSequenceNext 
	* @Description: 获取下一个序列号
	* @param @return 
	* @return Integer
	* @throws 
	*/
	private synchronized static Integer getSequenceNext(){
		if(++trade_id > 999999){
			trade_id = 100000;
		}
		return trade_id;
	}
	
	/** 
	* @Title: getOrderId 
	* @Description: 生成订单编号
	* @param @param userId
	* @param @return  
	* @return String 规则：流水号前两位  + 随机数两位 + 时间戳后八位 + 流水号中间两位 + 流水号后两位
	* @throws 
	*/
	public static String getOrderId(){
		StringBuilder builder = new StringBuilder();
		String time = String.valueOf(System.currentTimeMillis());
//		String _time = time.substring(time.length()-8);
		String number = getSequenceNext().toString();
		
		builder.append(ra.nextInt(90)+10);
		builder.append(number.substring(0,2));
		builder.append(time.substring(time.length()-8));
		builder.append(number.substring(2,4));
		builder.append(number.substring(4));
		return builder.toString();
	}
	
	public static void main(String[] args) {
		String str = null;
		String [] s = new String[1000];
		for (int i = 0; i < 1000; i++) {
			str = getOrderId();
			s[i] = str;
			//System.out.println(str);
		}
		int n = 0;
		for (int i = 0; i < s.length; i++) {
			
			for (int j = 0; j < s.length; j++) {
			if(s[i].equals(s[j])){
				n++;
				System.out.println(s[i]+"---"+s[j]);
			}
			
		}
		}
		System.out.println(n);
	}

}
