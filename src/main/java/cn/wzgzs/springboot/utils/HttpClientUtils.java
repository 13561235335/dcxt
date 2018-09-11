package cn.wzgzs.springboot.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {

	public static String requestGET(String url, int timeOut) throws IOException {
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setContentCharset("UTF-8");
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeOut);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(timeOut);
		client.executeMethod(getMethod);
		return getMethod.getResponseBodyAsString();
	}

	public static String GET(String url, LinkedHashMap<String, Object> params, int timeOut) throws IOException {
		HttpClient client = new HttpClient();
		if (null != params) {
			url = jointUrl(url, params);
		}
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setContentCharset("UTF-8");
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeOut);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(timeOut);
		client.executeMethod(getMethod);
		return getMethod.getResponseBodyAsString();
	}

	public static String POST(String url, LinkedHashMap<String, Object> headers, LinkedHashMap<String, Object> params, int timeOut) throws IOException {
		HttpClient client = new HttpClient();
		PostMethod httpPost = new PostMethod(url);
		// header
		if (null != headers && headers.size() > 0) {
			for (String key : headers.keySet()) {
				if (null != headers.get(key)) {
					httpPost.addRequestHeader(key, headers.get(key).toString());
				}
			}
		}
		// param
		if (null != params && params.size() > 0) {
			for (String key : params.keySet()) {
				if (null != params.get(key)) {
					httpPost.addParameter(key, params.get(key).toString());
				}
			}
		}
		httpPost.getParams().setContentCharset("utf-8");
		httpPost.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeOut);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(timeOut);
		client.executeMethod(httpPost);
		return httpPost.getResponseBodyAsString();
	}

	public static String REST_JSON(String url, String jsonParam) throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-type", "application/json; charset=utf-8");
		httpPost.setHeader("Accept", "application/json");
		httpPost.setEntity(new StringEntity(jsonParam, Charset.forName("UTF-8")));
		HttpResponse response = httpClient.execute(httpPost);
		String body = EntityUtils.toString(response.getEntity());
		return body;
	}

	public static String jointUrl(String url, LinkedHashMap<String, Object> params) {
		String tem = "";
		for (String key : params.keySet()) {
			tem += "&" + key + "=" + params.get(key).toString();
		}
		if (!"".equals(tem)) {
			url += "?" + tem.substring(1);
		}
		return url;
	}
}
