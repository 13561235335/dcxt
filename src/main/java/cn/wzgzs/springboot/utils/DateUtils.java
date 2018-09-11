package cn.wzgzs.springboot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期时间工具类
 * 
 * @author Dun
 */
public class DateUtils {
	/**
	 * 常用时间格式
	 */
	public static final String Format_Date = "yyyy-MM-dd";
	public static final String Format_Time = "HH:mm:ss";
	public static final String FORMAT_DATE_TIME = Format_Date + " " + Format_Time;
	public static final String FORMAT_DATE_TIME2 = Format_Date + " " + "HH:mm";

	/**
	 * 线程本地变量，确保线程间的数据安全
	 */
	public static final ThreadLocal<SimpleDateFormat> SHORT_DAY = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> MMDDHH = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm MM-dd");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> MDHM = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("M月d日 HH:mm");
		}
	};

	public static final ThreadLocal<SimpleDateFormat> MD = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("M月d日");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> YMD = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy年M月d日");
		}
	};

	public static final ThreadLocal<SimpleDateFormat> YMDHM = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy年M月d日 HH:mm");
		}
	};

	/** 默认的日期格式化器，格式为yyyy-MM-dd */
	public static final ThreadLocal<SimpleDateFormat> DEFAULT_DATE_FORMATER = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	/** 默认的时间格式化器，格式为yyyy-MM-dd HH:mm:ss */
	public static final ThreadLocal<SimpleDateFormat> DEFAULT_DATETIME_FORMATER = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	public static final ThreadLocal<SimpleDateFormat> YYYYMMDDHHMMSS = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMddHHmmss");
		}
	};

	/**
	 * 判断日期是否为同一天
	 * 
	 * @param dateA
	 * @param dateB
	 * @return
	 */
	public static boolean isSameDay(Date dateA, Date dateB) {
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTime(dateA);

		Calendar calDateB = Calendar.getInstance();
		calDateB.setTime(dateB);

		return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR) && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH) && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 日期加分钟,可以向前加，向后加
	 * 
	 * @param date
	 *            日期
	 * @param min
	 *            分钟
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int min) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + (min * 60 * 1000L));

		return c.getTime();
	}

	/**
	 * 获取当前时间的日期-时 数据，即将当前的分钟、秒置为0后的数值, *
	 * 
	 * @param hour
	 *            要加减的小时数，如果为0表示当前小时
	 * @return
	 */
	public static Date getDayHourDate(int hour) {
		Calendar today = Calendar.getInstance();
		today.setTimeInMillis(System.currentTimeMillis());
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.HOUR_OF_DAY, hour);
		return today.getTime();
	}

	/**
	 * 获得某天0时0分0秒的秒数
	 * 
	 * @param day
	 *            今天就是0，昨天就是-1，明天就是1，由此类推
	 * @return
	 */
	public static Date getSomeDayDate(int day) {
		// return (System.currentTimeMillis()/86400000*86400000L-(23-
		// Calendar.ZONE_OFFSET)*3600000L +
		// day*24*3600000L)/1000;
		// 上面这种方法在服务器执行发现跟系统时间并不一致，早上8点获取到的是新的一天的数据，屏蔽掉。用系统提供的方法以免出错
		Calendar today = Calendar.getInstance();
		today.setTimeInMillis(System.currentTimeMillis());
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.DAY_OF_YEAR, day);
		return today.getTime();
	}

	/**
	 * 返回日期代表的毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * 取得当前日期（只有日期，没有时间，或者可以说是时间为0点0分0秒）
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Date getCurrentDate() throws ParseException {
		Date date = new Date();
		date = DateUtils.DEFAULT_DATE_FORMATER.get().parse(DateUtils.DEFAULT_DATE_FORMATER.get().format(date));//
		return date;
	}

	/**
	 * 取得当前时间（包括日期和时间）
	 * 
	 * @return 当前时间
	 */
	public static Date getCurrentDateTime() {
		return longTimeToDateTime(System.currentTimeMillis());
	}

	/**
	 * 获取指定格式的当前系统日期时间
	 * 
	 * @param format
	 *            自定义日期格式器
	 * @return 前系统日期时间
	 */
	public static String getCurrentDateTime(String format) {
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(new Date());
	}

	/**
	 * 用默认的日期格式，格式化一个Date对象
	 * 
	 * @param date
	 *            待被格式化日期
	 * @return “yyyy-MM-dd”格式的日期字符串
	 */
	public static String formatDate(Date date) {
		return date == null ? "" : DateUtils.DEFAULT_DATE_FORMATER.get().format(date);
	}
	
	/**
	 * 转换时间 (Date)
	 * @author Dun 
	 * @param Date
	 * @return yyyy-MM-dd HH:mm:ss格式的日期时间字符串
	 */
	public static String formatDateTime(Date date){
		return date == null ? "" : longTimeToDateTimeString(date.getTime());
	}
	
	/**
	 * 转换时间 (long)
	 * @author Dun 
	 * @param long
	 * @return yyyy-MM-dd HH:mm:ss格式的日期时间字符串
	 */
	public static String formatDateTime(long time){
		return longTimeToDateTimeString(time);
	}

	/**
	 * 根据传入的格式，将日期对象格式化为日期字符串
	 * 
	 * @param date
	 *            待被格式化日期
	 * @param format
	 *            自定义日期格式器
	 * @return 格式后的日期字符串
	 */
	public static String formatDate(Date date, String format) {
		String s = "";

		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			s = sdf.format(date);
		}

		return s;
	}

	/**
	 * 用默认的日期时间格式，格式化一个Date对象
	 * 
	 * @param date
	 *            待被格式化日期
	 * @return “yyyy-MM-dd HH:mm:ss”格式的日期时间字符串
	 */
	public static String formatTime(Date date) {
		return date == null ? "" : DateUtils.DEFAULT_DATETIME_FORMATER.get().format(date);
	}

	/**
	 * 根据传入的格式，将日期对象格式化为时间字符串
	 * 
	 * @param date
	 *            待被格式化日期
	 * @param format
	 *            自定义日期格式器
	 * @return 格式后的日期时间字符串
	 */
	public static String formatTime(Date date, String format) {
		String s = "";
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			s = sdf.format(date);
		}

		return s;
	}

	/**
	 * 获取指定天数后的日期
	 * 
	 * @param baseDate
	 *            基准日期
	 * @param day
	 *            后推天数
	 * @return 后推后的天数
	 */
	public static Date getDateAfter(Date baseDate, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(baseDate);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 利用默认的格式（yyyy-MM-dd）将一个表示日期的字符串解析为日期对象
	 * 
	 * @param dateStr
	 *            待格式化日期字符串
	 * @return 格式化后日期对象
	 * @throws RuntimeException
	 */
	public static Date parseDate(String dateStr) {
		Date date = null;
		try {
			date = DateUtils.DEFAULT_DATE_FORMATER.get().parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		return date;
	}

	/**
	 * 利用默认的格式（yyyy-MM-dd HH:mm:ss）将一个表示时间的字符串解析为日期对象
	 * 
	 * @param timeStr
	 *            时间字符串
	 * @return 格式化后的日期对象
	 * @throws ParseException
	 */
	public static Date parseTime(String timeStr) {
		try {
			return DateUtils.DEFAULT_DATETIME_FORMATER.get().parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * 将一个字符串，按照特定格式，解析为日期对象
	 * 
	 * @param datetimeStr
	 *            日期、时间、日期时间字符串
	 * @param format
	 *            自定义日期格式器
	 * @return 格式化后的日期对象
	 * @throws ParseException
	 */
	public static Date parseDateTime(String datetimeStr, String format) throws ParseException {
		Date date = null;
		try {
			date = (new SimpleDateFormat(format)).parse(datetimeStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		return date;
	}

	/**
	 * 得到当前年份
	 * 
	 * @return 当前年份
	 */
	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 得到当前月份（1至12）
	 * 
	 * @return 当前月份（1至12）
	 */
	public static int getCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取yyyy-MM-dd格式的当前系统日期
	 * 
	 * @return 当前系统日期
	 */
	public static String getCurrentDateAsString() {
		return new SimpleDateFormat(DateUtils.Format_Date).format(new Date());
	}

	/**
	 * 获取指定格式的当前系统日期
	 * 
	 * @param format
	 *            自定义日期格式器
	 * @return 当前系统日期
	 */
	public static String getCurrentDateAsString(String format) {
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(new Date());
	}

	/**
	 * 获取HH:mm:ss格式的当前系统时间
	 * 
	 * @return 当前系统时间
	 */
	public static String getCurrentTimeAsString() {
		return new SimpleDateFormat(DateUtils.Format_Time).format(new Date());
	}

	/**
	 * 获取指定格式的当前系统时间
	 * 
	 * @param format
	 *            自定义日期格式器
	 * @return 当前系统时间
	 */
	public static String getCurrentTimeAsString(String format) {
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(new Date());
	}

	/**
	 * 获取格式为yyyy-MM-dd HH:mm:ss的当前系统日期时间
	 * 
	 * @return 当前系统日期时间
	 */
	public static String getCurrentDateTimeAsString() {
		return DateUtils.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取当前为星期几,从星期日~星期六对应的值是1~7
	 * 
	 * @return 星期几
	 */
	public static int getDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取指定日期为星期几,从星期日~星期六对应的值是1~7
	 * 
	 * @param date
	 *            指定日期
	 * @return 星期几
	 */
	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取星期几的中文名称
	 * 
	 * @param date
	 *            指定日期
	 * @return 星期几
	 */
	public static String getChineseDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		return DateUtils.getChineseDayOfWeek(cal.getTime());
	}

	/**
	 * 获取星期几的中文名称
	 * 
	 * @param date
	 *            指定日期
	 * @return 星期几
	 */
	public static String getChineseDayOfWeek(String date) {
		return DateUtils.getChineseDayOfWeek(DateUtils.parseDate(date));
	}

	/**
	 * 获取星期几的中文名称
	 * 
	 * @param date
	 *            指定日期
	 * @return 星期几
	 */
	public static String getChineseDayOfWeek(Date date) {
		int dateOfWeek = DateUtils.getDayOfWeek(date);
		if (dateOfWeek == Calendar.MONDAY) {
			return "星期一";
		} else if (dateOfWeek == Calendar.TUESDAY) {
			return "星期二";
		} else if (dateOfWeek == Calendar.WEDNESDAY) {
			return "星期三";
		} else if (dateOfWeek == Calendar.THURSDAY) {
			return "星期四";
		} else if (dateOfWeek == Calendar.FRIDAY) {
			return "星期五";
		} else if (dateOfWeek == Calendar.SATURDAY) {
			return "星期六";
		} else if (dateOfWeek == Calendar.SUNDAY) {
			return "星期日";
		}
		return null;
	}

	/**
	 * 获取当天为几号
	 * 
	 * @return 几号
	 */
	public static int getDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取指定日期为几号
	 * 
	 * @param date
	 *            指定的日期
	 * @return 几号
	 */
	public static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取指定日期所在月份的最后一天是几号
	 * 
	 * @param date
	 *            指定日期
	 * @return 指定日期所在月份的最后一天是几号
	 */
	public static int getMaxDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取指定日期所在月份的第一天
	 * 
	 * @param date
	 *            指定日期
	 * @return 指定日期所在月份的第一天
	 */
	public static String getFirstDayOfMonth(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.parseDate(date));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return new SimpleDateFormat(DateUtils.Format_Date).format(cal.getTime());
	}

	/**
	 * 获取当天为一年中第几天
	 * 
	 * @return 一年中第几天
	 */
	public static int getDayOfYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取指定日期为一年中第几天
	 * 
	 * @param date
	 *            指定日期
	 * @return 一年中第几天
	 */
	public static int getDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取指定日期为星期几,从星期日~星期六对应的值是1~7
	 * 
	 * @param date
	 *            指定日期
	 * @return 星期几
	 */
	public static int getDayOfWeek(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.parseDate(date));
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取指定日期为几号
	 * 
	 * @param date
	 *            指定的日期
	 * @return 几号
	 */
	public static int getDayOfMonth(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.parseDate(date));
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取指定日期为一年中第几天
	 * 
	 * @param date
	 *            指定日期
	 * @return 一年中第几天
	 */
	public static int getDayOfYear(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.parseDate(date));
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 时间戳转换:把距离GMT时间的毫秒数转为日期，中国处在东八区，所以是：GMT时间+8小时
	 * 
	 * @param time
	 *            距离GTM时刻的毫秒数
	 * @return 获取到的北京时区日期时间字符串
	 */
	public static String longTimeToDateTimeString(Long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = format.format(time);
		return d;
	}

	/**
	 * 时间戳转换:把距离GMT时间的毫秒数转为日期，中国处在东八区，所以是：GMT时间+8小时
	 * 
	 * @param time
	 *            距离GTM时刻的毫秒数
	 * @param strFmt
	 *            日期格式化字符串
	 * @return 获取到的北京时区日期时间字符串
	 */
	public static String longTimeToDateTimeString(Long time, String strFmt) {
		SimpleDateFormat format = new SimpleDateFormat(strFmt);
		String strDate = format.format(time);
		return strDate;
	}

	/**
	 * 时间戳转换:把距离GMT时间的毫秒数转为日期，中国处在东八区，所以是：GMT时间+8小时
	 * 
	 * @param time
	 *            距离GTM时刻的毫秒数
	 * @return 获取到的北京时区日期时间对象
	 */
	public static Date longTimeToDateTime(long time) {
		String d = DateUtils.DEFAULT_DATETIME_FORMATER.get().format(time);
		return DateUtils.parseTime(d);
	}

	// timeStamp,linux时间戳
	public static long getNow() {
		return System.currentTimeMillis() / 1000L;
	}

	public static long getTimestampForDB(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = format.format(time);
		Date date;
		try {
			date = format.parse(d);
			return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static Date formatterDateTime(String date) throws ParseException{
		if(null == date || "".equals(date)){
			return null;
		}
		if(date.length() == FORMAT_DATE_TIME.length()){
			return parseDateTime(date,FORMAT_DATE_TIME);
		}
		if(date.length() == FORMAT_DATE_TIME2.length()){
			return parseDateTime(date,FORMAT_DATE_TIME2);
		}
		return null;
	}

	/**
	 * 用默认的日期时间格式，格式化一个Date对象
	 * 
	 * @param date
	 *            待被格式化日期
	 */
	public static String formatTimeYYYYMMDDHHMMSS(Date date) {
		return date == null ? "" : DateUtils.YYYYMMDDHHMMSS.get().format(date);
	}

	public static void main(String[] args) throws ParseException {
		Date today = new Date();
		String todayStr = formatDate(today, "yyyy-MM-dd");
		Date todayNoTime = parseDateTime(todayStr, "yyyy-MM-dd");
		String[] todaySplit = todayStr.split("-");
		
		String testDayStr = "2017-12-28 09:02:00";
		Date testDay = parseDateTime(testDayStr, "yyyy-MM-dd HH:mm:ss");
		
		long temp = (today.getTime() - testDay.getTime())/1000/60/60;
		System.out.println(temp);
	}
	
	// 获得当前日期与本周日相差的天数  
	private static int getMondayPlus(Date gmtCreate) {  
	    Calendar cd = Calendar.getInstance();  
	    cd.setTime(gmtCreate);  
	    // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......  
	    int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1  
	    if (dayOfWeek == 1) {  
	        return 0;  
	    } else {  
	        return 1 - dayOfWeek;  
	    }  
	}  
	  
	// 获得下周星期一的日期  
	public static Date getNextMonday(Date gmtCreate) {  
	    int mondayPlus = getMondayPlus(gmtCreate);  
	    GregorianCalendar currentDate = new GregorianCalendar();  
	    currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);  
	    Date monday = currentDate.getTime();  
	    return monday;  
	}
	public static Date getNextMonday2(Date gmtCreate) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(gmtCreate);  

        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
        if(1 == dayWeek) {  
           cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  

       cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  

       int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
       cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day + 7);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值   
       
		return cal.getTime();  
	}
}
