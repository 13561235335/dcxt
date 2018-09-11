package cn.wzgzs.springboot.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 文件相关工具类
 * @author purplebrick
 */
public final class FileUtilsForSeekHelp {
	private static Logger logger = Logger.getLogger(FileUtilsForSeekHelp.class);
	private static String realPath = "";
	/** 常见视频格式 */
	public static final String[] VIDEO_FORMAT = {"avi","wmv","mpeg","mp4","mov","mkv","flv","f4v","m4v","rmvb","rm","3gp","dat","ts","mts","vob"};

	/**
	 * 生成文件名,时间戳+UUID前8位
	 * @author purplebrick
	 */
	public static String getFileName(){
	    UUID uuid = UUID.randomUUID();
	    String[] arr = uuid.toString().split("-");
	    return System.currentTimeMillis() + "-" + arr[0];
	}
	
	/**
     * 保存文件的具体执行
     * @param file 要保存的文件
     * @param filePath 保存文件的路径
     * @param shortName 保存的新文件名(不含扩展名,默认原扩展名),如果传null,自动生成文件名(时间戳+UUID前8位)
     * @param maxSize 上传文件最大尺寸,单位KB,如果传null,不做判断
     * @param maxSizeMsg 超过上传文件尺寸的提示信息
     * @param format 格式,视频FileUtils.VIDEO_FORMAT,图片FileUtils.IMG_FORMAT,如果传null,不做判断
     * @param formatMsg 上传文件格式不正确的提示信息
     * @return 可用map.get("code")分别获取
     * code(1成功/0失败),
     * msg(提示信息),
     * shortName(不含扩展名的文件名), 
     * fullName(含扩展名的文件名)
     * @author purplebrick
     */
    public static Map<String,String> saveFileDetail(CommonsMultipartFile file, String filePath, String shortName, 
    		Integer maxSize, String maxSizeMsg, String[] format, String formatMsg) throws Exception {
		Map<String,String> map = new HashMap<>();
		float fileSize = 0;
		String extend = null;
		if ((file != null) && (!file.isEmpty())) {
			//原文件扩展名
	    	extend = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
	    	if (StringUtils.isBlank(shortName)) {
	    		//新文件名:时间戳+UUID前8位
	    		shortName = getFileName();	
			}
	    	
	    	String fullName = shortName + "." + extend;
	    	fileSize = (float) file.getSize();
			File path = new File(filePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			InputStream is = null;
			String outFilePath = filePath + File.separator + fullName;
			if (isVideoFormat(extend)) {
				DataOutputStream out = new DataOutputStream(new FileOutputStream(outFilePath));
				try {
					is = file.getInputStream();
					int size = (int) fileSize;
					byte[] buffer = new byte[size];
					while (is.read(buffer) > 0)
						out.write(buffer);
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(FileUtils.class, e);
				} finally {
					if (is != null) {
						is.close();
					}
					if (out != null) {
						out.close();
					}
				}
			} else {
				try {
					is = file.getInputStream();
					String iconPath = realPath + "shuiyin.png"; // 固定位置
					WaterMarkUtils.markImageByIcon(iconPath, is, outFilePath);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(FileUtils.class, e);
				} finally {
					if (is != null) {
						is.close();
					}
				}
			}
			map.put("shortName", shortName);		
			map.put("fullName", fullName);			
		}
		
		if(fileSize == 0) {
			map.put("code", "0");
			map.put("msg", "保存失败!");
			return map;
		}
		//判断大小
		if(maxSize != null) {
			if(fileSize > maxSize * 1024) {
				map.put("code", "0");
				map.put("msg", maxSizeMsg);
				return map;
			}
		}
		//判断格式
		if(format != null) {
			boolean formatFlag = false;
			for (String item : format) {
				 if(item.equals(extend)) {
					 formatFlag = true;
					 break;
				 }
			}
			if(!formatFlag) {
				map.put("code", "0");
				map.put("msg", formatMsg);
				return map;
			}
		}
		
		map.put("code", "1");
		map.put("msg", "保存成功!");
		return map;
    }
    
	/**
	 * 保存多个文件,以","分割
	 * @param request	HttpServletRequest request
	 * @param filePath	保存文件的路径
	 * @param shortName	保存的新文件名(不含扩展名,默认原扩展名),如果传null,自动生成文件名(时间戳+UUID前8位)
	 * @param maxSize	上传文件最大尺寸,单位KB,如果传null,不做判断
	 * @param format	格式,视频FileUtils.VIDEO_FORMAT,图片FileUtils.IMG_FORMAT,如果传null,不做判断
	 * @param serverPath	保存在数据库的图片url的前缀(不包含文件名)
	 * @return 可用map.get("code")分别获取 code(1成功/0失败), msg(提示信息),
	 *         imgUrls(图片url,多个图片url用","隔开)
	 * @author purplebrick
	 */
	public static Map<String, String> saveFiles(HttpServletRequest request, String filePath, String shortName,
			Integer maxSize, String[] format, String serverPath) throws Exception {
		
		realPath = request.getServletContext().getRealPath("/");
		
		MultipartHttpServletRequest multipartRequest = null;
		try {
			multipartRequest = (MultipartHttpServletRequest) request;
		} catch (Exception e) {
			throw new Exception("请求中没有任何文件，强转成文件类型请求失败");
		}

		Map<String, Map<String, MultipartFile>> allFileMap = new HashMap<>();

		Iterator<String> fileNames = multipartRequest.getFileNames();
		if (fileNames != null && fileNames.hasNext()) {
			while (fileNames.hasNext()) {
				List<MultipartFile> fileRows = multipartRequest.getFiles(fileNames.next().toString());
				if (fileRows != null && fileRows.size() != 0) {
					for (MultipartFile file : fileRows) {
						if (file != null && !file.isEmpty()) {

							String name = file.getName();
							if (allFileMap.containsKey(name)) {
								Map<String, MultipartFile> reqMap = allFileMap.get(name);
								String originalFilename = file.getOriginalFilename();
								reqMap.put(originalFilename, file);
							} else {
								Map<String, MultipartFile> reqMap = new HashMap<>();
								String originalFilename = file.getOriginalFilename();
								reqMap.put(originalFilename, file);
								allFileMap.put(name, reqMap);
							}
						}
					}
				}
			}
		}
		List<CommonsMultipartFile> fileList = new ArrayList<>();
		for (String key : allFileMap.keySet()) {
			Map<String, MultipartFile> map = allFileMap.get(key);

			for (String k2 : map.keySet()) {
				MultipartFile file = map.get(k2);
				if (file.getSize() > 0) {
					fileList.add((CommonsMultipartFile) file);
				}
			}
		}

		// 获取图片url,多个图片url用","隔开
		Map<String, String> respMap = null;
		for (int i = 0; i < fileList.size(); i++) {
			// 一个一个文件调用保存方法
			String maxSizeMsg = "上传失败！您新上传的第" + (i + 1) + "个文件太大,系统允许最大文件" + maxSize + "KB";
			String formatMsg = "上传失败！您新上传的" + (i + 1) + "文件格式不正确";
			Map<String, String> map = saveFileDetail(fileList.get(i), filePath, shortName, maxSize, maxSizeMsg, format, formatMsg);
			if ("0".equals(map.get("code"))) {
				return map;
			}
			String imgUrl = serverPath + map.get("fullName");

			String filedName = fileList.get(i).getName();
			if(respMap == null) {
				 respMap = new HashMap<>();
			}
			if (respMap.containsKey(filedName)) {
				String url = respMap.get(filedName);
				if (url.endsWith(",")) {
					url += imgUrl + ",";
				} else {
					url += "," + imgUrl;
				}
				respMap.put(filedName, url);
			} else {
				respMap.put(filedName, imgUrl);
			}
		}
		return respMap;
	}
	
	/**
	 * 以后缀判断是否是视频格式
	 * @author ChenWangWu
	 * @createTime 2018年5月21日下午4:38:37
	 * @param extend
	 * @return
	 */
	private static boolean isVideoFormat(String extend) {
		//判断格式
		boolean formatFlag = false;
		for (String item : VIDEO_FORMAT) {
			if (item.equals(extend)) {
				formatFlag = true;
				break;
			}
		}
		return formatFlag;
	}
	
	public static void main(String[] args) {
		
	}
}
