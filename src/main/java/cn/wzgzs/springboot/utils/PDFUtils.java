package cn.wzgzs.springboot.utils;

import java.io.FileOutputStream;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * PDF工具类
 * @author purplebrick
 */
public final class PDFUtils {
	private static Logger logger = Logger.getLogger(PDFUtils.class);
	
	/**
	 * 生成pdf文件
	 * @param filePath pdf文件保存路径,如"D:/test.pdf"
	 * @param content pdf文件内容
	 * @author purplebrick
	 * @version 2017年12月27日
	 */
	public static boolean createPdf(String filePath, String content) {
		boolean flag = false;
		// 新建document对象
		Document document = new Document();
		PdfWriter writer = null;
		try {
			//中文字体,解决中文不能显示问题
	        BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
	        //黑色字体
	        Font blackFont = new Font(bfChinese);
	        blackFont.setColor(BaseColor.BLACK);
	        
	        // 建立一个书写器
			writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
	        // 打开文档
	        document.open();
	        // 添加内容
	        flag = document.add(new Paragraph(content, blackFont));
		} catch (Exception e) {
			flag = false;
			logger.error(PDFUtils.class,e);
		}finally {
			try {
				// 关闭文档
		        document.close();
		        // 关闭书写器
		        if(writer != null) {
		        	writer.close();
		        }
			} catch (Exception e2) {
				flag = false;
				logger.error(PDFUtils.class,e2);
			}
			
		}
        return flag;
	}
}
