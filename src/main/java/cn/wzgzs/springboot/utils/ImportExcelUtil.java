package cn.wzgzs.springboot.utils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ImportExcelUtil {
	private final static String excel2003L = ".xls"; // 2003- 版本的excel
	private final static String excel2007U = ".xlsx"; // 2007+ 版本的excel

	/**
	 * 描述：获取IO流中的数据，组装成List<List<Object>>对象
	 */
	public List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception {
		List<List<Object>> list = null;

		// 创建Excel工作薄
		Workbook work = this.getWorkbook(in, fileName);
		if (null == work) {
			throw new Exception("创建Excel工作薄为空！");
		}
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;

		list = new ArrayList<List<Object>>();
		// 遍历Excel中所有的sheet
		for (int i = 0; i < work.getNumberOfSheets(); i++) {
			sheet = work.getSheetAt(i);
			if (sheet == null) {
				continue;
			}

			// 遍历当前sheet中的所有行
			for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum() + 1; j++) {
				row = sheet.getRow(j);
				if (row == null || row.getFirstCellNum() == j) {
					continue;
				}

				// 遍历所有的列
				List<Object> li = new ArrayList<Object>();
				for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
					cell = row.getCell(y);
					li.add(this.getCellValue(cell));
					// li.add(new BigDecimal(cell.getNumericCellValue()).toPlainString());
				}
				list.add(li);
			}
		}
		// work.close();
		return list;
	}

	/**
	 * 描述：根据文件后缀，自适应上传文件的版本
	 */
	public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
		Workbook wb = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (excel2003L.equals(fileType)) {
			wb = new HSSFWorkbook(inStr); // 2003-
		} else if (excel2007U.equals(fileType)) {
			wb = new XSSFWorkbook(inStr); // 2007+
		} else {
			throw new Exception("解析的文件格式有误！");
		}
		return wb;
	}

	/**
	 * 描述：对表格中数值进行格式化
	 */
	public Object getCellValue(Cell cell) {
		Object value = null;
		// DecimalFormat df = new DecimalFormat("0"); //格式化number String字符
		// SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); //日期格式化
		// DecimalFormat df2 = new DecimalFormat("0.00"); //格式化数字

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_FORMULA:
			Double value2 = cell.getNumericCellValue();
			BigDecimal bd1 = new BigDecimal(Double.toString(value2));
			value = bd1.toPlainString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			Double value3 = cell.getNumericCellValue();
			BigDecimal bd2 = new BigDecimal(Double.toString(value3));
			if (bd2.toString().endsWith("0")) {
				value = bd2.toPlainString().substring(0, bd2.toPlainString().lastIndexOf("0"));
			} else {
				value = bd2.toPlainString();
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;
		default:
			break;
		}
		return value;
	}

	/**
	 * 鎻忚堪锛氳幏鍙朓O娴佷腑鐨勬暟鎹紝缁勮鎴怢ist<List<Object>>瀵硅薄
	 */
	public List<List<Object>> getBankListByExcel2(InputStream in, String fileName) throws Exception {
		List<List<Object>> list = null;

		// 鍒涘缓Excel宸ヤ綔钖�
		Workbook work = this.getWorkbook2(in, fileName);
		if (null == work) {
			throw new Exception("鍒涘缓Excel宸ヤ綔钖勪负绌猴紒");
		}
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;

		list = new ArrayList<List<Object>>();
		// 閬嶅巻Excel涓墍鏈夌殑sheet
		for (int i = 0; i < work.getNumberOfSheets(); i++) {
			sheet = work.getSheetAt(i);
			if (sheet == null) {
				continue;
			}

			// 閬嶅巻褰撳墠sheet涓殑鎵�鏈夎
			for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum() + 1; j++) {
				row = sheet.getRow(j);
				if (row == null || row.getFirstCellNum() == j) {
					continue;
				}

				// 閬嶅巻鎵�鏈夌殑鍒�
				List<Object> li = new ArrayList<Object>();
				for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
					cell = row.getCell(y);
					li.add(this.getCellValue2(cell));
				}
				list.add(li);
			}
		}
		// work.close();
		return list;
	}

	/**
	 * 鎻忚堪锛氭牴鎹枃浠跺悗缂�锛岃嚜閫傚簲涓婁紶鏂囦欢鐨勭増鏈�
	 */
	public Workbook getWorkbook2(InputStream inStr, String fileName) throws Exception {
		Workbook wb = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (excel2003L.equals(fileType)) {
			wb = new HSSFWorkbook(inStr); // 2003-
		} else if (excel2007U.equals(fileType)) {
			wb = new XSSFWorkbook(inStr); // 2007+
		} else {
			throw new Exception("瑙ｆ瀽鐨勬枃浠舵牸寮忔湁璇紒");
		}
		return wb;
	}

	/**
	 * 鎻忚堪锛氬琛ㄦ牸涓暟鍊艰繘琛屾牸寮忓寲
	 */
	public Object getCellValue2(Cell cell) {
		Object value = null;
		DecimalFormat df = new DecimalFormat("0"); // 鏍煎紡鍖杗umber String瀛楃
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); // 鏃ユ湡鏍煎紡鍖�
		DecimalFormat df2 = new DecimalFormat("0.00"); // 鏍煎紡鍖栨暟瀛�

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			String temp = cell.getCellStyle().getDataFormatString();
			// 鏃ユ湡杞崲,鏍规嵁temp鍊艰嚜瀹氫箟杩囨护瑙勫垯(鏂偣鏌ョ湅temp鍊�)
			if (temp.indexOf("yyyy") > -1 && temp.indexOf("dddd") > -1) {
				value = sdf.format(cell.getDateCellValue());
				break;
			}
			if (temp.indexOf("骞�") > -1 && temp.indexOf("鏈�") > -1) {
				value = sdf.format(cell.getDateCellValue());
				break;
			}
			if ("General".equals(cell.getCellStyle().getDataFormatString())) {
				value = df.format(cell.getNumericCellValue());
			} else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
				value = sdf.format(cell.getDateCellValue());
			} else {
				value = df2.format(cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;
		default:
			break;
		}
		return value;
	}

}
