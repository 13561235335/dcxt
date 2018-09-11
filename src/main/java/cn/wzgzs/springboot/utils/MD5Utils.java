package cn.wzgzs.springboot.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Utils {
	
	private static Logger logger = LoggerFactory.getLogger(MD5Utils.class);

	/**
	 * 用MD5算法加密字节数组
	 * 
	 * @param bytes
	 *            要加密的字节
	 * @return byte[] 加密后的字节数组，若加密失败，则返回null
	 */
	public static byte[] encode(byte[] bytes) {
		try {
			java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(bytes);
			byte[] digesta = digest.digest();
			return digesta;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 用MD5算法加密后再转换成hex String
	 * 
	 * @param bytes
	 * @return String
	 */
	public static String encode2HexStr(byte[] bytes) {
		return HexUtils.bytes2HexStr(MD5Utils.encode(bytes));
	}

	/**
	 * 用MD5算法加密后再转换成BASE64编码的字符串
	 * 
	 * @param bytes
	 * @return String
	 */
	public static String encode2Base64(byte[] bytes) {
		return BASE64Utils.encode(MD5Utils.encode(bytes));
	}

	/**
	 * 计算文件的md5
	 * 
	 * @param filePath
	 *            文件路径
	 * @return md5结果，若加密失败，则返回null
	 */
	public static byte[] encodeFile(String filePath) {
		try {
			java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(filePath);
			byte[] buffer = new byte[1024];
			byte[] digesta = null;
			int readed = -1;
			try {
				while ((readed = fis.read(buffer)) != -1) {
					digest.update(buffer, 0, readed);
				}
				digesta = digest.digest();
			} catch (IOException e) {
				MD5Utils.logger.error("IOException:" + filePath, e);
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					MD5Utils.logger.error("IOException:" + filePath, e);
				}
			}
			return digesta;
		} catch (FileNotFoundException e) {
			MD5Utils.logger.error("file not found:" + filePath, e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			MD5Utils.logger.error("NoSuchAlgorithmException:MD5", e);
			return null;
		}
	}

	/**
	 * 计算文件的md5,转换成hex String
	 * @author Dun 
	 * @param filePath
	 * @return
	 */
	public static String encodeFile2HexStr(String filePath) {
		return HexUtils.bytes2HexStr(MD5Utils.encodeFile(filePath));
	}

	/**
	 * 计算文件的md5,转换成Base64 string
	 * @author Dun 
	 * @param filePath
	 * @return
	 */
	public static String encodeFile2Base64(String filePath) {
		byte[] bytes = MD5Utils.encodeFile(filePath);
		if (bytes == null) {
			return null;
		}
		return BASE64Utils.encode(bytes);
	}

	/**
	 * 仅为微信支付相关使用
	 * @author Dun
	 * @param s
	 * @return
	 */
	public final static String encodeForWx(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 用于加密会员密码
	 * @author Dun
	 * @param password
	 * @return
	 */
	public final static String encodeMemberPassword(String password) {
		if (password == null || "".equals(password.trim())) {
			return null;
		}
		return encode2HexStr(encode(password.getBytes()));
	}

	/**
	 * 用于微信MD5加密，支持中文
	 * @author Dun 
	 * @param key
	 * @return
	 */
	public static String getMD5AsHex(byte[] key) {
		return getMD5AsHex(key, 0, key.length);
	}

	/**
	 * MD5加密
	 * @author Dun 
	 * @param key
	 * @param offset
	 * @param length
	 * @return
	 */
	public static String getMD5AsHex(byte[] key, int offset, int length) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(key, offset, length);
			byte[] digest = md.digest();
			return new String(Hex.encodeHex(digest));
		} catch (NoSuchAlgorithmException e) {
		}
		return "";
	}

	public static void main(String[] args) {
		// 与前端MD5加密规则一致
		System.out.println("MD5加密:"+encodeForWx("123456"));
		System.out.println("F59BD65F7EDAFB087A81D4DCA06C4910");
	}
}
