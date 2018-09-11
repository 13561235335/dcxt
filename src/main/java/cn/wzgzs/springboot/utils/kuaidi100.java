package cn.wzgzs.springboot.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;


public class kuaidi100
{
	
	public static void main1(String[] agrs)
	{
		
		try
		{
			URL url= new URL("http://www.kuaidi100.com/query?type=shentong&postid=3368575246956&temp=0.8603669291038145");
			URLConnection con=url.openConnection();
			 con.setAllowUserInteraction(false);
			   InputStream urlStream = url.openStream();
			   String type = con.guessContentTypeFromStream(urlStream);
			   String charSet=null;
			   if (type == null)
			    type = con.getContentType();

			   if (type == null || type.trim().length() == 0 || type.trim().indexOf("text/html") < 0)
			    return ;

			   if(type.indexOf("charset=") > 0)
			    charSet = type.substring(type.indexOf("charset=") + 8);

			   byte b[] = new byte[10000];
			   int numRead = urlStream.read(b);
			  String content = new String(b, 0, numRead);
			   while (numRead != -1) {
			    numRead = urlStream.read(b);
			    if (numRead != -1) {
			     //String newContent = new String(b, 0, numRead);
			     String newContent = new String(b, 0, numRead, charSet);
			     content += newContent;
			    }
			   }
			   //System.out.println("content:" + content);
			   urlStream.close();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/*Map<String, Object> jsonObject = HttpUtils.doGetJson("http://www.kuaidi100.com/query?type=shentong&postid=3368575246956&temp=0.8603669291038145");
		
		System.out.println(jsonObject.get("data"));*/
		
		/*
		 * 
		 * 
		 * CREATE TABLE `help_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '助物记录id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `seek_help_id` int(11) NOT NULL COMMENT '求物id',
  `items_number` int(11) NOT NULL DEFAULT '0' COMMENT '助捐助数量',
  `items_valuation` decimal(10,2) NOT NULL COMMENT '物品估价',
  `courier_company` varchar(254) DEFAULT NULL COMMENT '快递公司ID',
  `courier_number` varchar(254) DEFAULT NULL COMMENT '快递单号',
  `anonymous_state` tinyint(1) DEFAULT NULL COMMENT '是否匿名',
  `get_love_value` int(11) DEFAULT NULL,
  `donations_time` datetime NOT NULL COMMENT '捐助时间',
  `donations_message` varchar(50) DEFAULT NULL COMMENT '捐助留言',
  `ask_leave_id` int(11) DEFAULT NULL COMMENT '求助留言表id',
  `order_number` varchar(50) NOT NULL COMMENT '订单号',
  `status` enum('CG','SB') DEFAULT NULL COMMENT '助物状态',
  `uploadtime` datetime DEFAULT NULL COMMENT '上传快递单号时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='助物表';

	 		order:620669359676
			id:shunfeng
			s:80799215ed06c97e2cbf960711a8586a
		 * */
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("order", "620669359676");
//		jsonObject.put("id", "shunfeng");
//		jsonObject.put("s", "80799215ed06c97e2cbf960711a8586a");
//		
//		JSONObject jsonObject2 = HttpUtils.doPostJson("https://www.aikuaidi.cn/query",jsonObject);
//		
//		System.out.println(jsonObject2);
		JSONObject jsonObject = HttpUtils.doGetJson("http://www.kuaidi100.com/query?type=shunfeng&postid=620669359676&temp=34534534");
		
		System.out.println(jsonObject);
	}
}
