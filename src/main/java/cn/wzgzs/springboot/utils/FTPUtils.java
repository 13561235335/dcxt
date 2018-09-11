package cn.wzgzs.springboot.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtils {
	/**
	 * 上传文件（可供Action/Controller层使用）
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器保存目录
	 * @param fileName
	 *            上传到FTP服务器后的文件名称
	 * @param inputStream
	 *            输入文件流
	 * @return
	 */
	public static boolean uploadFile(String hostname, int port, 
			String username, String password, String pathname, String fileName,
			InputStream inputStream) {
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		try {
			// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 是否成功登录FTP服务器
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				return flag;
			}

			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.makeDirectory(pathname);
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.storeFile(fileName, inputStream);
			inputStream.close();
			ftpClient.logout();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 上传文件（可对文件进行重命名）
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器后的文件名称
	 * @param originfilename
	 *            待上传文件的名称（绝对地址）
	 * @return
	 */
	public static boolean uploadFileFromProduction(String hostname, int port,
			String username, String password, String pathname, String filename,
			String originfilename) {
		boolean flag = false;
		try {
			InputStream inputStream = new FileInputStream(new File(
					originfilename));
			flag = uploadFile(hostname, port, username, password, pathname,
					filename, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 上传文件（不可以进行文件的重命名操作）
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器保存目录
	 * @param originfilename
	 *            待上传文件的名称（绝对地址）
	 * @return
	  */
	public static boolean uploadFileFromProduction(String hostname, int port,
			String username, String password, String pathname,
			String originfilename) {
		boolean flag = false;
		try {
			createDir(hostname, port, username, password, pathname);
			String fileName = new File(originfilename).getName();
			InputStream inputStream = new FileInputStream(new File(
					originfilename));
			flag = uploadFile(hostname, port, username, password, pathname,
					fileName, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 删除文件
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器保存目录
	 * @param filename
	 *            要删除的文件名称
	 * @return
	  */
	public static boolean deleteFile(String hostname, int port,
			String username, String password, String pathname, String filename) {
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
		try {
			// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 验证FTP服务器是否登录成功
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				return flag;
			}
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.dele(filename);
			ftpClient.logout();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
				} catch (IOException e) {

				}
			}
		}
		return flag;
	}

	/**
	 * 下载文件
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器文件目录
	 * @param filename
	 *            文件名称
	 * @param localpath
	 *            下载后的文件路径
	 * @return
	  */
	public static boolean downloadFile(String hostname, int port,
			String username, String password, String pathname, String filename,
			String localpath) {
		FTPClient ftpClient = new FTPClient();
		try {
			// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 验证FTP服务器是否登录成功
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				return false;
			}
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(pathname);
			FTPFile[] ftpFiles = ftpClient.listFiles();
			for (FTPFile file : ftpFiles) {
				if(file.getName().contains(filename)) {
					File localFile = new File(localpath + "/" + file.getName());
					OutputStream os = new FileOutputStream(localFile);
					ftpClient.retrieveFile(file.getName(), os);
					os.close();
				}
			}
			ftpClient.logout();
			File file = new File(localpath + filename);
			if(file.isFile()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
				} catch (IOException e) {

				}
			}
		}
		return false;
	}
	/**
	 * 创建文件夹
	 * @param dirname  /test/tets/test
	 * @return 
	 */
	public static boolean createDir(String hostname, int port,
			String username, String password, String dirname) {
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
	    try{
	    	// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 验证FTP服务器是否登录成功
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				return flag;
			}
			if(StringUtils.isNotBlank(dirname)) {
				String[] split = dirname.split("/");
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < split.length; i++) {
					sb.append(split[i]).append("/");
					ftpClient.makeDirectory(sb.toString());
				}
			}
	        flag = true;
	    }catch(Exception ex){
	        System.out.println(ex.getMessage());
	    }
	    return flag;
	}
	
	/**
	*  检验指定路径的文件是否存在ftp服务器中
	* @param filePath--指定绝对路径的文件
	* @param user--ftp服务器登陆用户名
	* @param passward--ftp服务器登陆密码
	* @param ip--ftp的IP地址
	* @param port--ftp的端口号
	* @return
	*/

	public static boolean isFTPFileExist(String filePath,String user,String passward,String ip,int port){
	        FTPClient ftp = new FTPClient();
	        try {
	        	// 连接ftp服务器
	            ftp.connect(ip, port);
	            // 登陆    
	            ftp.login(user, passward);
	           // 检验登陆操作的返回码是否正确
	            if(!FTPReply.isPositiveCompletion(ftp.getReplyCode())){
	                ftp.disconnect();
	                return false;
	            }
	            
	            ftp.enterLocalActiveMode();
	            // 设置文件类型为二进制，与ASCII有区别
	            ftp.setFileType(FTP.BINARY_FILE_TYPE);
	            // 设置编码格式
	            ftp.setControlEncoding("GBK");
	            
	            // 提取绝对地址的目录以及文件名
	            filePath = filePath.replace("ftp://"+ip+":"+port+"/", "");
	            String dir = filePath.substring(0, filePath.lastIndexOf("/"));
	            String file = filePath.substring(filePath.lastIndexOf("/")+1);
	            
	            // 进入文件所在目录，注意编码格式，以能够正确识别中文目录
	            ftp.changeWorkingDirectory(new String(dir.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING));

	            // 检验文件是否存在
	            InputStream is = ftp.retrieveFileStream(new String(file.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING));
	            if(is == null || ftp.getReplyCode() == FTPReply.FILE_UNAVAILABLE){
	                return false;
	            }
	            
	            if(is != null){
	                is.close();
	                ftp.completePendingCommand();
	            }
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	            if(ftp != null){
	                try {
	                    ftp.disconnect();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return false;
	}

	public static void main(String[] args) {
		String hostname = "192.168.1.174";
		int port = 21;
		String username = "Administrator";
		String password = "yangjie";
		String pathname = "/";
		String filename = "big.rar";
		String originfilename = "C:\\Users\\Administrator\\Desktop\\qr.rar";
		uploadFileFromProduction(hostname, port, username, password, pathname,
				filename, originfilename);
		// String localpath = "D:/";

		// FavFTPUtil.downloadFile(hostname, port, username, password, pathname,
		// filename, localpath);
	}

}
