package cn.wzgzs.springboot.utils;

import java.util.UUID;

public class UUIDUtils {

	public static String getUUID(){
		 UUID uuid = UUID.randomUUID();
		 return uuid.toString();
	}
	
	public static String getUpperCaseCode(){
		return getUUID().replace("-", "").toUpperCase();
	}
	
	public static String getLowerCaseCode(){
		return getUUID().replace("-", "").toLowerCase();
	}
	
	/**
	 * 获得8个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String get8UUID(){
	    UUID id=UUID.randomUUID();
	    String[] idd=id.toString().split("-");
	    return idd[0];
	}
}
