package cn.wzgzs.springboot.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public final class FileUtils {
	private static Logger logger = Logger.getLogger(FileUtils.class);
	/** 常见视频格式 */
	public static final String[] VIDEO_FORMAT = {"avi","wmv","mpeg","mp4","mov","mkv","flv","f4v","m4v","rmvb","rm","3gp","dat","ts","mts","vob"};
	/** 常见图片格式 */
	public static final String[] IMG_FORMAT = {"jpg","jpeg","bmp","png","gif","tif"};
	/** 常见Excel格式 */
	public static final String[] EXCEL_FORMAT = {"xls","xlsx"};
	/** 通用文件格式 */
	public static final String[] COMMON_FORMAT = {"pdf","txt","doc","docx","xls","xlsx","avi","wmv","mpeg","mp4","mov","mkv","flv","f4v","m4v","rmvb","rm","3gp","dat","ts","mts","vob","jpg","jpeg","bmp","png","gif","tif"};

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
     * 保存单个文件
     * @param request HttpServletRequest request
     * @param uploadKey form-data格式上传文件的key值
     * @param filePath 保存文件的路径
     * @param shortName 保存的新文件名(不含扩展名,默认原扩展名),如果传null,自动生成文件名(时间戳+UUID前8位)
     * @param maxSize 上传文件最大尺寸,单位KB,如果传null,不做判断
     * @param format 格式,视频FileUtils.VIDEO_FORMAT,图片FileUtils.IMG_FORMAT,如果传null,不做判断
     * @return 可用map.get("code")分别获取
     * code(1成功/0失败),
     * msg(提示信息),
     * shortName(不含扩展名的文件名), 
     * fullName(含扩展名的文件名)
     * @author purplebrick
     */
    public static Map<String,String> saveFile(HttpServletRequest request, String uploadKey, String filePath, String shortName,
    		Integer maxSize, String[] format) throws Exception {
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(uploadKey);
		String maxSizeMsg = "上传失败！您上传的文件太大,系统允许最大文件"+ maxSize +"KB";
		String formatMsg = "上传失败！您上传的文件格式不正确";
		return saveFileDetail(file, filePath, shortName, maxSize, maxSizeMsg, format, formatMsg);
    }
	
	/**
     * 保存多个文件,以","分割
     * @param request HttpServletRequest request
     * @param filePath 保存文件的路径
     * @param shortName 保存的新文件名(不含扩展名,默认原扩展名),如果传null,自动生成文件名(时间戳+UUID前8位)
     * @param maxSize 上传文件最大尺寸,单位KB,如果传null,不做判断
     * @param format 格式,视频FileUtils.VIDEO_FORMAT,图片FileUtils.IMG_FORMAT,如果传null,不做判断
     * @param serverPath 保存在数据库的图片url的前缀(不包含文件名)
     * @return 可用map.get("code")分别获取
     * code(1成功/0失败),
     * msg(提示信息),
     * imgUrls(图片url,多个图片url用","隔开)
     * @author purplebrick
     */
	public static Map<String, String> saveFiles(HttpServletRequest request, String filePath, String shortName,
    		Integer maxSize, String[] format, String serverPath) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> reqMap = multipartRequest.getFileMap();
		Map<String, String> respMap = new HashMap<>();
		//获取所有图片的file对象
		List<CommonsMultipartFile> fileList = new ArrayList<>();
		for (String key : reqMap.keySet()) {
			CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(key);
			if(file.getSize() > 0) {
				fileList.add(file);
			}
		}
		//获取图片url,多个图片url用","隔开
		String imgUrls = "";
		for (int i = 0; i < fileList.size(); i++) {
			//一个一个文件调用保存方法
			String maxSizeMsg = "上传失败！您新上传的第"+ (i+1) +"个文件太大,系统允许最大文件"+ maxSize +"KB";
			String formatMsg = "上传失败！您新上传的"+ (i+1) +"文件格式不正确";
			String originalFilename = fileList.get(i).getOriginalFilename();
			String name = fileList.get(i).getName();
			System.out.println("originalFilename = "+ originalFilename);
			System.out.println("name = "+ name);
			Map<String, String> map = saveFileDetail(fileList.get(i), filePath, shortName, maxSize, maxSizeMsg, format, formatMsg);
			if("0".equals(map.get("code"))) {
				return map;
			}
			String imgUrl = serverPath + map.get("fullName"); 
			if (i != fileList.size() - 1) {
				imgUrls += imgUrl + ",";
			} else {
				imgUrls += imgUrl;
			}
		}
		respMap.put("imgUrls", imgUrls);
		respMap.put("code", "1");
		respMap.put("msg", "保存成功!");
		return respMap;
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
			DataOutputStream out = new DataOutputStream(new FileOutputStream(filePath + File.separator + fullName));
			InputStream is = null;
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
     * 视频转码(除mp4外格式,如flv,最小最适合页面播放)
     * @param ffmpegPath 转码工具的存放路径
     * @param oldFilePath 视频源文件
     * @param newFilePath 转换后的的文件保存路径,后缀名变更转换格式,如newFilePath = test.flv,转换成flv格式
     * @param newImgPath 视频截图保存路径
     * @author purplebrick
     */
    public static boolean videoCodecs(String ffmpegPath, String oldFilePath, String newFilePath, String newImgPath) throws Exception {
    	List<String> videoCommand = videoCommand(ffmpegPath, oldFilePath, newFilePath);
        List<String> imgCommand = imgCommand(ffmpegPath, oldFilePath, newImgPath);

        boolean flag = true;
        ProcessBuilder builder = new ProcessBuilder();
        try {
    		builder.command(videoCommand);
            builder.redirectErrorStream(true);
            builder.start();
            
            builder.command(imgCommand);
            //如果此属性为 true，则任何由通过此对象的 start()方法启动的后续子进程生成的错误输出都将与标准输出合并, 
            //因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
            builder.redirectErrorStream(true);
            builder.start();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 视频转码(转成MP4格式,直接在页面播放)
     * @param ffmpegPath 转码工具的存放路径
     * @param oldFilePath 视频源文件
     * @param newFilePath 转换后的的文件保存路径
     * @param newImgPath 视频截图保存路径
     * @author purplebrick
     */
    public static boolean videoToMp4Codecs(String ffmpegPath, String oldFilePath, String newFilePath, String newImgPath) throws Exception {
    	//扩展名
    	String extend = oldFilePath.substring(oldFilePath.lastIndexOf(".") + 1).toLowerCase();
    	//组装视频格式转换命令
    	List<String> videoCommand = null;
    	String os = System.getProperty("os.name");  
    	//window系统
    	if(os.toLowerCase().startsWith("win")){  
    		videoCommand = videoToMp4Command(ffmpegPath, oldFilePath, newFilePath);
    	//非window系统
    	} else {
    		//源文件是MP4格式,直接复制
        	if("mp4".equals(extend)) {
        		videoCommand = copyCommand(oldFilePath, newFilePath);
        	//源文件不是MP4格式,转换成MP4格式
        	}else {
        		videoCommand = videoToMp4Command(ffmpegPath, oldFilePath, newFilePath);
        	}
    	}
        List<String> imgCommand = imgCommand(ffmpegPath, oldFilePath, newImgPath);

        boolean flag = true;
        ProcessBuilder builder = new ProcessBuilder();
        try {
    		builder.command(videoCommand);
    		builder.redirectErrorStream(true);
    		builder.start();
            
            builder.command(imgCommand);
            //如果此属性为 true，则任何由通过此对象的 start()方法启动的后续子进程生成的错误输出都将与标准输出合并, 
            //因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
            builder.redirectErrorStream(true);
            builder.start();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 组装视频格式转换命令(除mp4外格式)
     * @param ffmpegPath 转码工具的存放路径
     * @param oldFilePath 视频源文件
     * @param newFilePath 转换后的文件保存路径,后缀名变更转换格式,如newFilePath = test.flv,转换成flv格式
     * @return 视频格式转换命令集
     * @author purplebrick
     */
    private static List<String> videoCommand(String ffmpegPath, String oldFilePath, String newFilePath){
    	List<String> videoCommand = new ArrayList<String>();
    	videoCommand.add(ffmpegPath); // 添加转换工具路径
    	videoCommand.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
    	videoCommand.add(oldFilePath); // 添加要转换格式的视频文件的路径
    	videoCommand.add("-qscale");     //指定转换的质量
    	videoCommand.add("6");
    	videoCommand.add("-ab");        //设置音频码率
    	videoCommand.add("64");
    	videoCommand.add("-ac");        //设置声道数
    	videoCommand.add("2");
    	videoCommand.add("-ar");        //设置声音的采样频率
    	videoCommand.add("22050");
    	videoCommand.add("-r");        //设置帧频
    	videoCommand.add("24");
    	videoCommand.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
    	videoCommand.add(newFilePath);
    	return videoCommand;
    }
    
    /**
     * 组装视频格式转换命令(mp4)
     * @param ffmpegPath 转码工具的存放路径
     * @param oldFilePath 视频源文件
     * @param newFilePath 转换后的文件保存路径
     * @return 视频格式转换命令集
     * @author purplebrick
     */
    private static List<String> videoToMp4Command(String ffmpegPath, String oldFilePath, String newFilePath){
    	// 创建一个List集合来保存视频格式转换命令
        List<String> videoCommand = new ArrayList<String>();
        //转换成mp4格式
        videoCommand.add(ffmpegPath); 
        videoCommand.add("-i");  
        videoCommand.add(oldFilePath); 
        videoCommand.add("-vcodec");  
        videoCommand.add("libx264");  
        videoCommand.add("-preset");  
        videoCommand.add("ultrafast");  
        videoCommand.add("-profile:v");  
        videoCommand.add("baseline");  
        videoCommand.add("-acodec");  
        videoCommand.add("aac");  
        videoCommand.add("-strict");  
        videoCommand.add("experimental");  
        //------压缩------,这4行注释,不压缩
        videoCommand.add("-s");  
        videoCommand.add("640*480");  
        videoCommand.add("-b");
        videoCommand.add("568k");  
        //------压缩------
        videoCommand.add("-qscale");//视频品质  
        videoCommand.add("6");//视频品质参数  
        videoCommand.add("-ab");  
        videoCommand.add("128k");  
        videoCommand.add("-y");//文件存在选择重写  
        videoCommand.add(newFilePath);
    	return videoCommand;
    }
    
    /**
     * 组装视频截图命令
     * @param ffmpegPath 转码工具的存放路径
     * @param oldFilePath 视频源文件
     * @param newImgPath 视频截图保存路径
     * @return 视频截图命令集
     * @author purplebrick
     */
    private static List<String> imgCommand(String ffmpegPath, String oldFilePath, String newImgPath){
    	// 创建一个List集合来保存从视频中截图的命令
        List<String> imgCommand = new ArrayList<String>();
        imgCommand.add(ffmpegPath);
        imgCommand.add("-i");
        imgCommand.add(oldFilePath);// 转换之前的文件
        imgCommand.add("-y");		
        imgCommand.add("-f");		
        imgCommand.add("image2");	
        imgCommand.add("-ss"); 		// 添加参数＂-ss＂，该参数指定截取的起始时间
        imgCommand.add("0.1"); 		// 添加起始时间为第0.1秒
        imgCommand.add("-t"); 		// 添加参数＂-t＂，该参数指定持续时间
        imgCommand.add("0.1"); 		// 添加持续时间为0.1秒
        imgCommand.add("-s"); 		// 添加参数＂-s＂，该参数指定截取的图片大小
        imgCommand.add("350*240"); 	// 添加截取的图片大小为350*240
        imgCommand.add(newImgPath); // 添加截取的图片的保存路径
        return imgCommand;
    }
    
    /**
     * 复制文件命令
     * @param oldFilePath 视频源文件
     * @param newFilePath 复制后的文件保存路径
     * @return 复制文件命令集
     * @author purplebrick
     * @version 2017年12月27日
     */
    private static List<String> copyCommand(String oldFilePath, String newFilePath){
    	List<String> copyCommand = new ArrayList<String>();
        copyCommand.add("cp");
        copyCommand.add("-rf");
        copyCommand.add(oldFilePath);
        copyCommand.add(newFilePath);
        return copyCommand;
    }
    
    public static String createFile() {
    	Date date = new Date(); 
    	//格式化并转换String类型 
    	String path=new SimpleDateFormat("yyyyMMdd").format(date); 
    	return path;
	}
    
    public static void main(String[] args) throws Exception {  
    /*	String ffmpegPath = "F:/new/tools/ffmpeg.exe";
    	String oldFilePath = "F:/new/tools/videoTest/ios.mov";
    	String newFilePath = "F:/new/tools/videoTest/test.mp4";
    	String newImgPath = "F:/new/tools/videoTest/test.jpg";
    	//测试转换mp4文件
    	boolean flag = FileUtils.videoToMp4Codecs(ffmpegPath, oldFilePath, newFilePath, newImgPath);
    	System.out.println(flag);*/
    	createFile();
    }
}
