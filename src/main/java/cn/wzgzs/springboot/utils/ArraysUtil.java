package cn.wzgzs.springboot.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class ArraysUtil {

	/**
	 * 数组去重复
	 * @param ids
	 * @return
	 */
	public static Integer[] delSameData(Integer[] ids) {
		List<Integer> list = new ArrayList<Integer>();    
	    for (int i=0; i<ids.length; i++) {    
	        if(!list.contains(ids[i]) && ids[i] != -1) {    
	            list.add(ids[i]);    
	        }    
	    }  
	    Integer[] Ids = new Integer[list.size()];
	    for (int i = 0; i < Ids.length; i++) {
	    	Ids[i] = list.get(i);
		}
	    return Ids;
	}
	
	/**
	 * 清楚空数据
	 * @param arr
	 * @return
	 */
	public static String[] nullArrays(String[] arr) {
		if(arr != null) {
			int index = 0;
			String[] ss = new String[arr.length];
			for (String str : arr) {
				if(StringUtils.isNotBlank(str)) {
					ss[index] = str;
					index ++;
				}
			}
			String[] array = new String[index];
			for (int i = 0; i < index; i++) {
				array[i] = ss[i];
			}
			return array;
		}
		return null; 
	}
	
	/**
	 * 判斷是否有相同項
	 * @param arr
	 * @return
	 */
	public static boolean isSameArrays(String[] arr) {
		Set<String> set = new HashSet<>();
		if(arr != null) {
			for (int i = 0; i < arr.length; i++) {
				if(StringUtils.isNotBlank(arr[i])) {
					if(!set.contains(arr[i])) {
						set.add(arr[i]);
					} else {
						return true;
					}
				}
			}
		}
		return false; 
	}
}
