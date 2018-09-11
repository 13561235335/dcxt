package cn.wzgzs.springboot.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel 工具类
 * 
 * @author Dun
 */
public class ExcelOutUtil {

	/**
	 * 创建excel文档
	 * 
	 * @param list
	 *            数据
	 * @param keys
	 *            list中map的key数组集合
	 * @param columnNames
	 *            excel的列名
	 * @return Workbook
	 * @author: Dun
	 */
	public static Workbook createSummaryWorkBook(Map<String, List<LinkedHashMap<String, Object>>> map, String[] keys, String columnNames[]) {
		HSSFWorkbook wb = new HSSFWorkbook();
		for (String key : map.keySet()) {
			List<LinkedHashMap<String, Object>> list = map.get(key);
		
			// 创建excel工作簿
//			Workbook wb = new HSSFWorkbook();
			// 创建第一个sheet（页），并命名
			// Sheet sheet =
			// wb.createSheet(list.get(0).get("sheetName").toString());
			Sheet sheet = wb.createSheet(key);
			// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
			for (int i = 0; i < keys.length; i++) {
				sheet.setColumnWidth((short) i, (short) (35.7 * 150));
			}
	
			// 创建第一行
			Row row = sheet.createRow((short) 0);
	
			// 创建两种单元格格式
			CellStyle cs = wb.createCellStyle();
			CellStyle cs2 = wb.createCellStyle();
	
			// 创建两种字体
			Font f = wb.createFont();
			Font f2 = wb.createFont();
	
			// 创建第一种字体样式（用于列名）
			f.setFontHeightInPoints((short) 10);
			f.setColor(IndexedColors.BLACK.getIndex());
			f.setBoldweight(Font.BOLDWEIGHT_BOLD);
	
			// 创建第二种字体样式（用于值）
			f2.setFontHeightInPoints((short) 10);
			f2.setColor(IndexedColors.BLACK.getIndex());
	
			// 设置第一种单元格的样式（用于列名）
			cs.setFont(f);
			cs.setBorderLeft(CellStyle.BORDER_THIN);
			cs.setBorderRight(CellStyle.BORDER_THIN);
			cs.setBorderTop(CellStyle.BORDER_THIN);
			cs.setBorderBottom(CellStyle.BORDER_THIN);
			cs.setAlignment(CellStyle.ALIGN_CENTER);
	
			// 设置第二种单元格的样式（用于值）
			cs2.setFont(f2);
			cs2.setBorderLeft(CellStyle.BORDER_THIN);
			cs2.setBorderRight(CellStyle.BORDER_THIN);
			cs2.setBorderTop(CellStyle.BORDER_THIN);
			cs2.setBorderBottom(CellStyle.BORDER_THIN);
			cs2.setAlignment(CellStyle.ALIGN_CENTER);
			// 设置列名
			for (int i = 0; i < columnNames.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(columnNames[i]);
				cell.setCellStyle(cs);
			}
			// 设置每行每列的值
			for (short i = 0; i < list.size(); i++) {
				// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
				// 创建一行，在页sheet上
				Row row1 = sheet.createRow((short) i + 1);
				// 在row行上创建一个方格
				for (short j = 0; j < keys.length; j++) {
					Cell cell = row1.createCell(j);
					cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
					cell.setCellStyle(cs2);
				}
			}
		}
		return wb;
	}
	
	/**
	 * 创建excel文档
	 * 
	 * @param list
	 *            数据
	 * @param keys
	 *            list中map的key数组集合
	 * @param columnNames
	 *            excel的列名
	 * @return Workbook
	 * @author: Dun
	 * @param fileName 
	 */
	public static Workbook createWorkBook(List<LinkedHashMap<String, Object>> list, String[] keys, String columnNames[], String fileName) {
		// 创建excel工作簿
		Workbook wb = new HSSFWorkbook();
		// 创建第一个sheet（页），并命名
		// Sheet sheet =
		// wb.createSheet(list.get(0).get("sheetName").toString());
		Sheet sheet = wb.createSheet("excel");
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
		for (int i = 0; i < keys.length; i++) {
			sheet.setColumnWidth((short) i, (short) (35.7 * 150));
		}
		
		// 创建第一行
		Row row = sheet.createRow((short) 0);
		//设置PL文件单元格
		if(fileName.contains("PL")) {
			sheet.setColumnWidth(0, 10*256);
			sheet.setColumnWidth(1, 8*256);
			sheet.setColumnWidth(2, 6*256);
			sheet.setColumnWidth(3, 8*256);
			sheet.setColumnWidth(4, 15*256);
			sheet.setColumnWidth(5, 5*256);
			sheet.setColumnWidth(6, 10*256);
			sheet.setColumnWidth(7, 10*256);
			sheet.setColumnWidth(8, 10*256);
			sheet.setColumnWidth(9, 8*256);
			sheet.setColumnWidth(10, 8*256);
			sheet.setColumnWidth(11, 5*256);
			sheet.setColumnWidth(12, 12*256);
			sheet.setColumnWidth(13, 10*256);
			sheet.setColumnWidth(14, 10*256);
			sheet.setColumnWidth(15, 10*256);
			sheet.setColumnWidth(16, 10*256);
		}
		
		// 创建两种单元格格式
		CellStyle cs = wb.createCellStyle();
		CellStyle cs2 = wb.createCellStyle();
		
		// 创建两种字体
		Font f = wb.createFont();
		Font f2 = wb.createFont();
		
		// 创建第一种字体样式（用于列名）
		f.setFontHeightInPoints((short) 10);
		f.setColor(IndexedColors.BLACK.getIndex());
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		// 创建第二种字体样式（用于值）
		f2.setFontHeightInPoints((short) 10);
		f2.setColor(IndexedColors.BLACK.getIndex());
		
		// 设置第一种单元格的样式（用于列名）
		cs.setFont(f);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		
		// 设置第二种单元格的样式（用于值）
		cs2.setFont(f2);
		cs2.setBorderLeft(CellStyle.BORDER_THIN);
		cs2.setBorderRight(CellStyle.BORDER_THIN);
		cs2.setBorderTop(CellStyle.BORDER_THIN);
		cs2.setBorderBottom(CellStyle.BORDER_THIN);
		cs2.setAlignment(CellStyle.ALIGN_CENTER);
		// 设置列名
		for (int i = 0; i < columnNames.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(columnNames[i]);
			cell.setCellStyle(cs);
		}
		// 设置每行每列的值
		for (short i = 0; i < list.size(); i++) {
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			Row row1 = sheet.createRow((short) i + 1);
			// 在row行上创建一个方格
			for (short j = 0; j < keys.length; j++) {
				Cell cell = row1.createCell(j);
				cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
				cell.setCellStyle(cs2);
			}
		}
		return wb;
	}

	public static void responseExcel(HttpServletResponse response, String fileName, List<LinkedHashMap<String, Object>> list, String[] columnNames,
			String[] keys) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ExcelOutUtil.createWorkBook(list, keys, columnNames, fileName).write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}
	
	/**
	 * 创建excel
	 * @param response
	 * @param fileName
	 * @param list
	 * @param columnNames
	 * @param keys
	 * @param path
	 * @throws IOException
	 */
	public static ByteArrayOutputStream ftpExcel(String fileName, Map<String, List<LinkedHashMap<String, Object>>> map, String[] columnNames, String[] keys) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ExcelOutUtil.createSummaryWorkBook(map, keys, columnNames).write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os;
	}

	/**
	 * 创建excel文档
	 * 
	 * @param list
	 *            数据
	 * @param keys
	 *            list中map的key数组集合
	 * @param columnNames
	 *            excel的列名
	 * @return Workbook
	 * @author: Dun
	 */
	public static Workbook createWorkBookJOIN(List<LinkedHashMap<String, Object>> list, String[] keys, String columnNames[]) {
		// 创建excel工作簿
		Workbook wb = new HSSFWorkbook();
		// 创建第一个sheet（页），并命名
		// Sheet sheet =
		// wb.createSheet(list.get(0).get("sheetName").toString());
		Sheet sheet = wb.createSheet("excel");
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
		for (int i = 0; i < keys.length; i++) {
			sheet.setColumnWidth((short) i, (short) (35.7 * 150));
		}

		// 创建第一行
		Row row = sheet.createRow((short) 0);

		// 创建两种单元格格式
		CellStyle cs = wb.createCellStyle();
		CellStyle cs2 = wb.createCellStyle();

		// 创建两种字体
		Font f = wb.createFont();
		Font f2 = wb.createFont();

		// 创建第一种字体样式（用于列名）
		f.setFontHeightInPoints((short) 10);
		f.setColor(IndexedColors.BLACK.getIndex());
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);

		// 创建第二种字体样式（用于值）
		f2.setFontHeightInPoints((short) 10);
		f2.setColor(IndexedColors.BLACK.getIndex());

		// 设置第一种单元格的样式（用于列名）
		cs.setFont(f);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		cs2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
		// 设置第二种单元格的样式（用于值）
		cs2.setFont(f2);
		cs2.setBorderLeft(CellStyle.BORDER_THIN);
		cs2.setBorderRight(CellStyle.BORDER_THIN);
		cs2.setBorderTop(CellStyle.BORDER_THIN);
		cs2.setBorderBottom(CellStyle.BORDER_THIN);
		cs2.setAlignment(CellStyle.ALIGN_CENTER);// 水平
		cs2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
		// 设置列名
		for (int i = 0; i < columnNames.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(columnNames[i]);
			cell.setCellStyle(cs);
		}
		// 设置每行每列的值
		for (short i = 0; i < list.size(); i++) {
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			Row row1 = sheet.createRow((short) i + 1);
			// 在row行上创建一个方格
			for (short j = 0; j < keys.length; j++) {
				if (i != 0 && j <= 17) {
					if (list.get(i - 1).get(keys[0]).equals(list.get(i).get(keys[0]))) {
						// 重点在这里动态合并
						CellRangeAddress cra = new CellRangeAddress(i, i + 1, j, j);
						// 在sheet里增加合并单元格
						sheet.addMergedRegion(cra);
						Cell cell = row1.createCell(j);
						cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
						cell.setCellStyle(cs2);
						continue;
					} else {
						Cell cell = row1.createCell(j);
						cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
						cell.setCellStyle(cs2);
						continue;
					}
				} else {
					Cell cell = row1.createCell(j);
					cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
					cell.setCellStyle(cs2);
					continue;
				}

			}

		}
		return wb;
	}

	private static int getCount(int i, int count, List<LinkedHashMap<String, Object>> list, String[] keys, String columnNames[]) {
		int count1 = count;
		if (i >= count1 + 1 && list.get(i - (count1 + 1)).get(keys[0]).equals(list.get(i).get(keys[0]))) {
			count1++;
			count1 = getCount(i, count1, list, keys, columnNames);

		}
		if (count1 >= 3) {
			System.out.println(count1);
		}
		return count1;
	}

	public static void responseExcelJOIN(HttpServletResponse response, String fileName, List<LinkedHashMap<String, Object>> list,
			String[] columnNames, String[] keys) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ExcelOutUtil.createWorkBookJOIN(list, keys, columnNames).write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}
	
	public static List<List<String>> analysisExcel(String url){
		List<List<String>> robotList = new LinkedList<>();
		File file = new File(url);
		InputStream is = null;
		try {
			is =new FileInputStream(file);
//			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
			//循环处理每一页
			for(int sheetNum =0;sheetNum<hssfWorkbook.getNumberOfSheets();sheetNum++){
				XSSFSheet hssfSheet = hssfWorkbook.getSheetAt(sheetNum);
				if(null == hssfSheet){
					continue;
				}
				for(int rowNum = 1;rowNum <= hssfSheet.getLastRowNum();rowNum++ ){
					XSSFRow hssfRow = hssfSheet.getRow(rowNum);
					int minCell = hssfRow.getFirstCellNum();
					int maxCell = hssfRow.getLastCellNum();
					List<String> rowList = new LinkedList<>();
					for(int cellNum = minCell;cellNum<maxCell;cellNum++){
						XSSFCell hssfCell = hssfRow.getCell(cellNum);
						if(null == hssfCell){
							continue;
						}
						rowList.add(ExcelOutUtil.getStringVal(hssfCell));
					}
					robotList.add(rowList);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return robotList;
	}
	
	public static List<List<String>> readExcel(InputStream is){
		try{
			XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
			List<List<String>> list = new LinkedList<>();
			//循环处理每一页
			for(int sheetNum =0;sheetNum<hssfWorkbook.getNumberOfSheets();sheetNum++){
				XSSFSheet hssfSheet = hssfWorkbook.getSheetAt(sheetNum);
				if(null == hssfSheet){
					continue;
				}
				for(int rowNum = 1;rowNum <= hssfSheet.getLastRowNum();rowNum++ ){
					XSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if(null == hssfRow){
						break;
					}
					int minCell = hssfRow.getFirstCellNum();
					int maxCell = hssfRow.getLastCellNum();
					List<String> rowList = new LinkedList<>();
					for(int cellNum = minCell;cellNum<maxCell;cellNum++){
						XSSFCell hssfCell = hssfRow.getCell(cellNum);
						if(null == hssfCell){
							continue;
						}
						String value = ExcelOutUtil.getStringVal(hssfCell);
						if(null != value && !"".equals(value)){
							rowList.add(value);
						}
					}
					list.add(rowList);
				}
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getStringVal(XSSFCell cell){
		switch (cell.getCellType()){
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue()?"true":"false";
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		default:
			return null;
		}
	}
}
