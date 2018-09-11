package cn.wzgzs.springboot.utils;

/**
 * Created by Seven on 16/2/23.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpRequestUtils {
	private static Log logger = LogFactory.getLog(HttpRequestUtils.class);

	public HttpRequestUtils() {
	}

	public static String getParameter(HttpServletRequest request, String name) {
		return request.getParameter(name);
	}

	public static Integer getParameterAsInteger(HttpServletRequest request, String name) {
		return request.getParameter(name) == null ? null : Integer.valueOf(Integer.parseInt(request.getParameter(name)));
	}

	public static Map<String, String> parameterNoRepeatToMap(HttpServletRequest request) {
		HashMap map = new HashMap();
		Enumeration enumratrion = request.getParameterNames();

		String serviceString;
		while (enumratrion.hasMoreElements()) {
			serviceString = (String) enumratrion.nextElement();
			String values = request.getParameter(serviceString);
			map.put(serviceString, values);
		}

		serviceString = (String) map.get("service");
		if (("platform_dragonactivity_user_register".equals(serviceString) || "platform_dragonactivity_user_login".equals(serviceString))
				&& map.containsKey("password")) {
			map.put("password", ((String) map.get("password")).toLowerCase());
		}

		return map;
	}

	public static void printContent(String content, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = null;

		try {
			writer = response.getWriter();
			if (StringUtils.isEmpty(content)) {
				writer.write("{}");
			} else {
				writer.write(content);
			}
		} catch (IOException var5) {
			logger.error("invalid request", var5);
		}finally {
			if(null != writer){
				writer.close();
			}
		}

		try {
			response.flushBuffer();
		} catch (IOException var4) {
			logger.error("flush response error", var4);
		}

	}

	public static void printJsonContent(String content, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter writer = null;

		try {
			writer = response.getWriter();
			if (StringUtils.isEmpty(content)) {
				writer.write("{}");
			} else {
				writer.write(content);
			}
		} catch (IOException var5) {
			logger.error("invalid request", var5);
		}finally {
			if(null != writer){
				writer.close();
			}
		}
		try {
			response.flushBuffer();
		} catch (IOException var4) {
			logger.error("flush response error", var4);
		}

	}

	public static boolean checkNotEmptyParam(String paramName, String paramValue, HttpServletResponse response) {
		boolean isValid = true;
		if (StringUtils.isEmpty(paramValue)) {
			isValid = false;
			String errorReason = paramName + "参数不合法,必须有值!";
			printContent(errorReason, response);
		}

		return isValid;
	}

	public static boolean isGetRequest(HttpServletRequest request) {
		return "GET".equals(request.getMethod());
	}
}
