package org.xdemo.app.xutils.j2se;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 日期工具类
 * @author Goofy 252878950@qq.com <a href="http://<a href="http://www.xdemo.org">www.xdemo.org</a>/">http://<a href="http://www.xdemo.org">www.xdemo.org</a>/</a>
 */
public class DateUtils {

	public static enum Type {
		Year, Month, Week, Day, Hour, Minutes, Seconds;
	}
	
	public static enum DateField {
		YEAR(Calendar.YEAR), MONTH(Calendar.MONTH), WEEK_OF_YEAR(Calendar.WEEK_OF_YEAR), WEEK_OF_MONTH(Calendar.WEEK_OF_YEAR), DAY_OF_MONTH(Calendar.DAY_OF_MONTH), DAY_OF_YEAR(Calendar.DAY_OF_YEAR), HOUR(Calendar.HOUR_OF_DAY), MINUTES(Calendar.MINUTE), SECONDS(Calendar.SECOND);

		private int index;

		private DateField(int index) {
			this.index = index;
		}
		
		public int getIndex(){
			return this.index;
		}

	}

	/**
	 * <b>获取当前时间</b><br>
	 * y 年 M 月 d 日 H 24小时制 h 12小时制 m 分 s 秒
	 * 
	 * @param format
	 *            日期格式
	 * @return String
	 */
	public static String now(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	/**
	 * 获取制定日期的格式化字符串
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return String
	 */
	public static String format(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 判断哪个日期在前 如果日期一在日期二之前，返回true,否则返回false
	 * 
	 * @param date1
	 *            日期一
	 * @param date2
	 *            日期二
	 * @return boolean
	 */
	public static boolean isBefore(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);

		if (c1.before(c2))
			return true;

		return false;
	}

	/**
	 * 将字符串转换成日期
	 * 
	 * @param date
	 *            日期字符串
	 * @param format
	 *            日期字符串格式
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String date, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);
	}

	/**
	 * 获取指定日期当月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date lastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	/**
	 * 获取指定日期当月的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date firstDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * 是否是闰年
	 * 
	 * @param year
	 *            年份
	 * @return boolean
	 */
	public static boolean isLeapYear(int year) {
		GregorianCalendar calendar = new GregorianCalendar();
		return calendar.isLeapYear(year);
	}

	/**
	 * 获取指定日期之前或者之后多少天的日期
	 * 
	 * @param day
	 *            指定的时间
	 * @param offset
	 *            日期偏移量，正数表示延后，负数表提前
	 * @return Date
	 */
	public static Date getDateByOffset(Date day, int offsetDays) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.add(Calendar.DAY_OF_MONTH, offsetDays);
		return c.getTime();
	}

	/**
	 * 获取一天开始时间 如 2014-12-12 00:00:00
	 * 
	 * @return
	 */
	public static Date dayStart() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取一天结束时间 如 2014-12-12 2:59:59
	 * 
	 * @return
	 */
	public static Date dayEnd() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 2);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	/**
	 * 时间分段 比如：2014-12-12 10:00:00 ～ 2014-12-12 14:00:00 分成两段就是 2014-12-12
	 * 10：00：00 ～ 2014-12-12 12：00：00 和2014-12-12 12：00：00 ～ 2014-12-12 14：00：00
	 * 
	 * @param start
	 *            起始日期
	 * @param end
	 *            结束日期
	 * @param pieces
	 *            分成几段
	 */
	public static Date[] pieces(Date start, Date end, int pieces) {

		Long sl = start.getTime();
		Long el = end.getTime();

		Long diff = el - sl;

		Long segment = diff / pieces;

		Date[] dateArray = new Date[pieces + 1];

		for (int i = 1; i <= pieces + 1; i++) {
			dateArray[i - 1] = new Date(sl + (i - 1) * segment);
		}

		// 校正最后结束日期的误差，可能会出现偏差，比如14:00:00 ,会变成1:59:59之类的
		dateArray[pieces] = end;

		return dateArray;
	}

	/**
	 * 获取某个日期的当月第一天
	 * 
	 * @return
	 */
	public static Date firstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * 获取某个日期的当月最后一天
	 * 
	 * @return
	 */
	public static Date lastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		return cal.getTime();
	}

	/**
	 * 获取指定日期所在周，周几的日期，1：周日，2：周一，以此类推......
	 * 
	 * @param date
	 * @param dayOfWeek
	 * @return
	 */
	public static Date dateOfWeek(Date date, int dayOfWeek) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		return cal.getTime();
	}

	/**
	 * 获取两个日期的时间差，可以指定年，月，周，日，时，分，秒
	 * 
	 * @param date1
	 *            第一个日期
	 * @param date2
	 *            第二个日期<font color="red">此日期必须在date1之后</font>
	 * @param type
	 *            DateUtils.Type.X的枚举类型
	 * @return long值
	 * @throws Exception
	 */
	public static long diff(Date date1, Date date2, Type type) throws Exception {

		if (!isBefore(date1, date2))
			throw new Exception("第二个日期必须在第一个日期之后");

		switch (type) {
		case Year: {
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();

			cal1.setTime(date1);
			int year1 = cal1.get(Calendar.YEAR);
			int month1 = cal1.get(Calendar.MONTH);
			int day1 = cal1.get(Calendar.DAY_OF_MONTH);
			int hour1 = cal1.get(Calendar.HOUR_OF_DAY);
			int minute1 = cal1.get(Calendar.MINUTE);
			int second1 = cal1.get(Calendar.SECOND);

			cal2.setTime(date2);
			int year2 = cal2.get(Calendar.YEAR);
			int month2 = cal2.get(Calendar.MONTH);
			int day2 = cal2.get(Calendar.DAY_OF_MONTH);
			int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
			int minute2 = cal2.get(Calendar.MINUTE);
			int second2 = cal2.get(Calendar.SECOND);

			int yd = year2 - year1;

			if (month1 > month2) {
				yd -= 1;
			} else {
				if (day1 > day2) {
					yd -= 1;
				} else {
					if (hour1 > hour2) {
						yd -= 1;
					} else {
						if (minute1 > minute2) {
							yd -= 1;
						} else {
							if (second1 > second2) {
								yd -= 1;
							}
						}
					}
				}
			}
			return (long) yd;
		}
		case Month: {
			// 获取年份差
			long year = diff(date1, date2, Type.Year);

			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();

			cal1.setTime(date1);
			int month1 = cal1.get(Calendar.MONTH);
			int day1 = cal1.get(Calendar.DAY_OF_MONTH);
			int hour1 = cal1.get(Calendar.HOUR_OF_DAY);
			int minute1 = cal1.get(Calendar.MINUTE);
			int second1 = cal1.get(Calendar.SECOND);

			cal2.setTime(date2);
			int month2 = cal2.get(Calendar.MONTH);
			int day2 = cal2.get(Calendar.DAY_OF_MONTH);
			int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
			int minute2 = cal2.get(Calendar.MINUTE);
			int second2 = cal2.get(Calendar.SECOND);

			int md = (month2 + 12) - month1;

			if (day1 > day2) {
				md -= 1;
			} else {
				if (hour1 > hour2) {
					md -= 1;
				} else {
					if (minute1 > minute2) {
						md -= 1;
					} else {
						if (second1 > second2) {
							md -= 1;
						}
					}
				}
			}
			return (long) md + year * 12;
		}
		case Week: {
			return diff(date1, date2, Type.Day) / 7;
		}
		case Day: {
			long d1 = date1.getTime();
			long d2 = date2.getTime();
			return (int) ((d2 - d1) / (24 * 60 * 60 * 1000));
		}
		case Hour: {
			long d1 = date1.getTime();
			long d2 = date2.getTime();
			return (int) ((d2 - d1) / (60 * 60 * 1000));
		}
		case Minutes: {
			long d1 = date1.getTime();
			long d2 = date2.getTime();
			return (int) ((d2 - d1) / (60 * 1000));
		}
		case Seconds: {
			long d1 = date1.getTime();
			long d2 = date2.getTime();
			return (int) ((d2 - d1) / 1000);
		}
		default:
			throw new Exception("请指定要获取的时间差的类型：年，月，天，周，时，分，秒");
		}
	}

	/**
	 * 根据日期，获取当前是第几周
	 * 
	 * @param date
	 * @return
	 */
	public static int weekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 返回指定日期的某个字段值，field参考{@link Calendar}
	 * 
	 * @param date
	 * @param field
	 *            如年Calendar.YEAR，月Calendar.MONTH，日，周，等等，参考{@link Calendar}
	 * @return
	 */
	public static int fieldValue(Date date, DateField field) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(field==DateField.MONTH)
			return cal.get(field.getIndex())+1;
		return cal.get(field.getIndex());
	}

	/**
	 * 获取时间段的每一天
	 * 
	 * @param 开始日期
	 * @param 结算日期
	 * @return 日期列表
	 */
	public static List<Date> everyDay(Date start, Date end) {

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(start);
		c2.setTime(end);

		List<Date> list = new ArrayList<Date>();

		while (c1.before(c2)) {
			list.add(c1.getTime());
			c1.add(Calendar.DAY_OF_MONTH, 1);
		}

		if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
			list.add(end);
		}

		return list;
	}

	/**
	 * 获取时间段的每一天
	 * 
	 * @param start
	 * @param end
	 * @param format
	 *            时间格式
	 * @return
	 */
	public static List<String> everyDay(Date start, Date end, String format) {

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(start);
		c2.setTime(end);

		List<String> list = new ArrayList<String>();

		while (c1.before(c2)) {
			list.add(format(c1.getTime(), format));
			c1.add(Calendar.DAY_OF_MONTH, 1);
		}

		if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
			list.add(format(end, format));
		}

		return list;
	}
	
	/**
	 * 获取时间段的每一天
	 * 
	 * @param start
	 * @param end
	 * @param format
	 *            时间格式
	 * @return
	 */
	public static List<Integer> everyMonth(Date start, Date end) {

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(start);
		c2.setTime(end);

		List<Integer> list = new ArrayList<Integer>();

		while (c1.before(c2)) {
			list.add(c1.get(Calendar.MONTH)+1);
			c1.add(Calendar.MONTH, 1);
		}

		if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
			list.add(c2.get(Calendar.MONTH)+1);
		}

		return list;
	}

}
