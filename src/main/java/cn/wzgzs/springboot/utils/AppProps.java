package cn.wzgzs.springboot.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

/**
 * 加载配置文件，获取参数属性值
 * @author tengyicheng
 * @version [1.0, 2013/10/22]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AppProps {

	private static Logger logger = LoggerFactory.getLogger(AppProps.class);
	
	public static final String CONF_CLASS_PATH = "/config.properties";

	private static Properties props = new Properties();

	private AppProps() {
	}

	public static boolean isEmpty() {
		return props.isEmpty();
	}

	public static void load() {
		loadFromClassPath(CONF_CLASS_PATH);
	}

	public static void load(InputStream in) {
		try {
			props.load(in);
		} catch (Exception ex) {
			logger.debug(ex.getMessage(), ex);
			ex.printStackTrace();
		}
	}

	public static void loadFromFilePath(String filePath) {
		try {
			load(new FileInputStream(filePath));
		} catch (Exception ex) {
			logger.debug(ex.getMessage(), ex);
			ex.printStackTrace();
		}
	}

	public static void loadFromClassPath(String classPath) {
		try {
			load(new AppProps().getClass().getResourceAsStream(classPath));
		} catch (Exception ex) {
			logger.debug(ex.getMessage(), ex);
			ex.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	public static void setProperty(String key, String value) {
		props.setProperty(key, value);
	}

	public static String getString(String key) {
		return props.getProperty(key);
	}

	public static void setString(String key, String value) {
		props.setProperty(key, value);
	}

	public static void put(Object key, Object value) {
		props.put(key, value);
	}

	public static void put(Map<String, String> map) {
		props.putAll(map);
	}

	public static Object get(Object key) {
		return props.get(key);
	}

	public static boolean getBoolean(String key) {
		return Boolean.valueOf(getProperty(key)).booleanValue();
	}

	public static int getInt(String key) {
		int ret = 0;
		try {
			ret = Integer.parseInt(getProperty(key));
		} catch (Exception ex) {
		}
		return ret;
	}

	public static long getLong(String key) {
		long ret = 0;
		try {
			ret = Long.parseLong(getProperty(key));
		} catch (Exception ex) {
			logger.debug(ex.getMessage(), ex);
		}
		return ret;
	}

	public static float getFloat(String key) {
		float ret = 0.0F;
		try {
			ret = Float.parseFloat(getProperty(key));
		} catch (Exception ex) {
			logger.debug(ex.getMessage(), ex);
		}
		return ret;
	}

	public static double getDouble(String key) {
		double ret = 0.0;
		try {
			ret = Double.parseDouble(getProperty(key));
		} catch (Exception ex) {
		}
		return ret;
	}

	public static void list() {
		props.list(System.out);
	}

	public static String getPropertiesString() {
		return props.toString();
	}

}
