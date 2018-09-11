package cn.wzgzs.springboot.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 编码生成器
 * 
 * @author Dun
 */
public class CodeUtils {

	// 三十六进制Model
	private static char[] model_36 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'R', 'E', 'J', 'K', 'M', 'L', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z' };

	// 十进制Model
	private static char[] model_10 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * 获取当前时间戳(精确到秒) + 指定长度整形字符串 (十进制)
	 * 
	 * @author Dun
	 * @param length
	 * @return
	 */
	public static String getTimeCodeByLength(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		while (sb.length() < length) {
			sb.append(model_10[random.nextInt(10)]);
		}
		String res = System.currentTimeMillis() + sb.toString();
		return res;
	}

	/**
	 * 创建订单编号(当前系统时间 + 4位随机)
	 * 
	 * @author Dun
	 * @return
	 */
	public static String createOrderCode() {
		return DateUtils.formatTimeYYYYMMDDHHMMSS(new Date()) + getCodeByLength(4);
	}

	/**
	 * 获取指定长度随机整形字符串 (十进制)
	 * 
	 * @author Dun
	 * @param length
	 * @return
	 */
	public static String getCodeByLength(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		while (sb.length() < length) {
			sb.append(model_10[random.nextInt(10)]);
//			sb.append(model_10[Math.abs(random.nextInt()) % 10]);
		}
		return sb.toString();
	}

	/**
	 * 获取指定长度随机整形字符串 (三十六进制)
	 * 
	 * @author Dun
	 * @param length
	 * @return
	 */
	public static String getCodeByLength_36(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		while (sb.length() < length) {
			sb.append(model_36[random.nextInt(36)]);
		}
		return sb.toString();
	}

	/**
	 * 生成随机6位码 (三十六进制)
	 * 
	 * @return String
	 * @author: zhaodun
	 * @date: 2015年10月30日 下午7:19:14
	 */
	public static String createCode() {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		while (sb.length() < 6) {
			sb.append(model_36[random.nextInt(36)]);
		}
		return sb.toString();
	}

	/**
	 * 生成指定长度随机码(36进制)
	 * 
	 * @param length
	 * @return String
	 * @author vito
	 */
	public static String createCode(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		while (sb.length() < length) {
			sb.append(model_36[random.nextInt(36)]);
		}
		return sb.toString();
	}

	/**
	 * 获取不重复Code,需要传入比较对象Set (三十六进制)
	 * 
	 * @param set
	 * @return String
	 * @author: zhaodun
	 * @date: 2015年10月30日 下午4:25:24
	 */
	public static String createUniquenessCode(Set<String> set) {
		String code = createCode();
		if (!set.contains(code)) {
			return code;
		} else {
			return createUniquenessCode(set);
		}
	}

	/**
	 * 获取指定数量不重复Code Set集 (三十六进制)
	 * 
	 * @param set
	 * @param number
	 * @return Set<String>
	 * @author: zhaodun
	 * @date: 2015年10月30日 下午7:18:18
	 */
	public static List<String> createUniquenessCodeSet(Set<String> set, int number) {
		Set<String> temSet = new HashSet<String>();
		while (temSet.size() < number) {
			temSet.add(createUniquenessCode(set));
		}
		List<String> list = new ArrayList<String>();
		for (String s : temSet) {
			list.add(s);
		}
		return list;
	}

	/**
	 * 创建流水号
	 * 
	 * @author Dun
	 * @return
	 */
	public static String createUserCashFlowSn() {
		return "UCF" + createOrderCode();
	}

	/**
	 * 创建任务订单号
	 * 
	 * @return String
	 * @author Dun
	 */
	public static String createTaskOrderSn() {
		return "TASK" + createOrderCode();
	}

	/**
	 * 获取时间戳加随机字符串 (32位)
	 * 
	 * @author Dun
	 * @return
	 */
	public static String get32TimeCode() {
		return System.currentTimeMillis() / 1000 + getCodeByLength_36(19);
	}
	
	/**
	 * 获取直播间格间号
	 * 
	 * @param code
	 * @return Long
	 * @author Dun
	 */
	public static String getProgramRoomNo(String code) {
		Long codeNum = Long.parseLong(code);
		if (codeNum < 10100) {
			codeNum = 10100L;
		}
		codeNum = codeNum + 1;
		if (calculateRoomNo(codeNum)) {
			return (codeNum.toString());
		} else {
			return getProgramRoomNo(codeNum.toString());
		}
	}

	/**
	 * 排除以下数字组合 全五位数：AAAAA 四位数相同：AAAAB,BAAAA 三位相同：ABBBA,AABBB,AAABB,
	 * 连续五位相连：12345 10001 -10100 保留
	 */
	public static boolean calculateRoomNo(Long code) {
		// 预留号码包含
		// if (obligateSet.contains(code.toString())) {
		// return false;
		// }
		char[] charArr = code.toString().toCharArray();
		int num = 0;
		// 判断顺子
		for (int i = 0; i < charArr.length; i++) {
			char temi = charArr[i];
			if (i + 1 < charArr.length) {
				char temj = charArr[i + 1];
				if (temj - 1 == temi) {
					num += 1;
				} else {
					break;
				}
			}
		}
		// 连续五位相连
		if (num >= 4) {
			return false;
		}
		num = 0;
		// 判断三位相同
		for (int i = 0; i < charArr.length; i++) {
			char temi = charArr[i];
			if (i + 1 < charArr.length) {
				char temj = charArr[i + 1];
				if (temj == temi) {
					num += 1;
				}
			}
		}
		// 三位相同
		if (num >= 2) {
			return false;
		}
		return true;
	}
}
