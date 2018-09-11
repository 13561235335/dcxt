package cn.wzgzs.springboot.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public abstract class HttpUtils {
    public static final String HEADER_SEPARATOR = ";";

    public HttpUtils() {
    }

    /**
     * 解决跨域
     * @param url
     * @return
     * @throws IOException
     */
    public static JSONObject doGetJson(String url) throws IOException {
        JSONObject jsonObject=null;
        DefaultHttpClient defaultHttpClient=new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
        HttpEntity httpEntity=httpResponse.getEntity();
        if(httpEntity!=null){
            String result= EntityUtils.toString(httpEntity,"UTF-8");
            jsonObject=JSONObject.parseObject(result);
        }
        httpGet.releaseConnection();
        return jsonObject;
    }
    
    /**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @param noNeedResponse    不需要返回结果
     * @return
     */ 
    public static JSONObject doPostJson(String url, JSONObject jsonParam){
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    JSONObject json = new JSONObject();
                    json = JSONObject.parseObject(str);
                	return json;
                } catch (Exception e) {
                	System.err.println("post请求提交失败:" + url);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
        	System.err.println("post请求提交失败:" + url);
        	e.printStackTrace();
        }
        return null;
    }
    
    /***************************请求方式***************************/
    
    /**
	 * 简单调用URL,并将返回结果
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String sendGet(String url) throws Exception {
		URL U = new URL(url);
		URLConnection connection = U.openConnection();
		connection.connect();

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}
		in.close();
		
		return sb.toString();
	}
	
	/**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求參数，请求參数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        Logger.getLogger(HttpRequest.class).info("流量充值開始:"+url+"&"+param);
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置例如以下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象相应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求參数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            Logger.getLogger(HttpRequest.class).info("流量充值结束:"+result);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常。" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
