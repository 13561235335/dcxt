package cn.wzgzs.springboot.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * BASE64的工具类
 * @author Dun
 */
@SuppressWarnings("restriction")
public class BASE64Utils
{
    public BASE64Utils()
    {
    }

    /**
     * 按系统默认编码encode该字符串
     * 
     * @param s
     * @return String
     */
    public static String encode(String s)
    {
        return new BASE64Encoder().encode(s.getBytes());
    }

    /**
     * 对字节数组进行encode
     * 
     * @param bytes
     * @return String
     */
    public static String encode(byte[] bytes)
    {
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * 对ByteBuffer进行encode
     * 
     * @param buf
     * @return String
     */
    public static String encode(ByteBuffer buf)
    {
        return new BASE64Encoder().encode(buf);
    }

    /**
     * 对BASE64的字符串进行decode，若decode失败，则返回null
     * 
     * @param str
     * @return byte[]
     */
    public static byte[] decode(String str)
    {
        try
        {
            return new BASE64Decoder().decodeBuffer(str);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    public static String GetImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	    byte[] data = null;
	     
	    // 读取图片字节数组
	    try {
	      InputStream in = new FileInputStream(imgFilePath);
	      data = new byte[in.available()];
	      in.read(data);
	      in.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	     
	    // 对字节数组Base64编码
	    BASE64Encoder encoder = new BASE64Encoder();
	    return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}
}
