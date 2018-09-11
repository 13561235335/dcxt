package cn.wzgzs.springboot.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Counter {
	private File m_File;
	
	private static Boolean increase;
	
	private static int count;

	// |构造函数
	public Counter(String filename) {
		m_File = null;
		// |
		this.InitFile(filename);
		count = this.ReaderCounter();
	}

	// |创建文件（对象）
	private void InitFile(String filename) {
		this.m_File = new File(filename);

		// |判断文件是否存在
		if (!this.m_File.exists()) {
			try {
				this.m_File.createNewFile();
				// |字符流
				try {
					FileWriter writer = new FileWriter(this.m_File);
					writer.write("0," + new SimpleDateFormat("yyyyMMdd").format(new Date()));
					writer.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	// |读取次数并且（递增）打印出来
	private Integer ReaderCounter() {
		// |读取
		try {
			FileReader in = new FileReader(this.m_File);
			/***************
			 * 判断文件的大小来申请相应的 char 数组
			 ******************/
			FileInputStream inputstream = new FileInputStream(this.m_File);
			int allBytes = inputstream.available();
			/********************/
			char[] buffer = new char[allBytes];
			in.read(buffer);
			// |char[]转化成String
			String str = String.valueOf(buffer);
			String[] split = str.split(",");
			int count = Integer.parseInt(split[0]);
			// |关闭输入字符流
			in.close();
			// |叠加
			if(new SimpleDateFormat("yyyyMMdd").format(new Date()).equals(split[1])) {
				if (increase) {
					++count;
				}
				
			} else {
				count = 1;
			}
			// |输出字符流
			FileWriter out = new FileWriter(this.m_File);
			// |int转化成 String 输出
			out.write(String.valueOf(count) + "," + DateUtils.formatDate(new Date(), "yyyyMMdd"));
			out.close();
			return count;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}
	
	/**
	 * 从文件读取今日的次数，从1开始
	 * @param path
	 * @param increase,默认为true，true则自增，false则不增
	 * @return
	 */
	public static int getCounter(String path,Boolean increase) {
		Counter.increase=increase;
		new Counter(path);  
		return count;
	}
	
	/**
	 * 从文件读取今日的次数，从1开始，count自增
	 * @param path
	 * @return
	 */
	public static int getCounter(String path) {
		//默认为true，自增
		Counter.increase=true;
		new Counter(path);  
		return count;
	}
	
		  
    public static void main(String[] args) {  
    	
        // TODO Auto-generated method stub  
        System.out.println(getCounter("D:/counter1.txt"));
    }  

}