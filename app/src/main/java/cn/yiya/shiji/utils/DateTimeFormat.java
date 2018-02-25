package cn.yiya.shiji.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeFormat {
	public static final long DayTime = 24 * 60 * 60 * 1000;
	public static final long MonthTime = 30*DayTime;
	public static final long YearTime = 365*DayTime;
	/**
	 * 格式化秒数
	 *
	 * @param seconds
	 * @return HH:mm:ss 格式的字符串
	 */
	public static String formatTime(int seconds) {
		if (seconds < 0) {
			return "invialdTime";
		}
		int hour = seconds / 3600;
		int minute = (seconds % 3600) / 60;
		int sec = seconds % 60;
		if (hour < 1) {
			return formatNumber(minute) + ":" + formatNumber(sec);
		}
		return formatNumber(hour) + ":" + formatNumber(minute) + ":" + formatNumber(sec);
	}

	/**
	 * 格式化数字显示  不满两位的添加0占位
	 *
	 * @param num
	 * @return
	 */
	public static String formatNumber(int num) {
		String str = num + "";
		if (str.length() < 2) {
			str = "0" + str;
		}
		return str;
	}

	public static boolean isSameDay(long time1, long time2) {
		Calendar cal1 = Calendar.getInstance(Locale.CHINA);
		cal1.setTimeInMillis(time1);
		Calendar cal2 = Calendar.getInstance(Locale.CHINA);
		cal2.setTimeInMillis(time2);
		if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)) {
			return true;
		}

		return false;
	}

	public static String getMonthString(long times) {
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setTimeInMillis(times);
		int month = cal.get(Calendar.MONTH);
		String monthStr = "";
		switch (month) {
			case 0:
				monthStr = "一月";
				break;
			case 1:
				monthStr = "二月";
				break;
			case 2:
				monthStr = "三月";
				break;
			case 3:
				monthStr = "四月";
				break;
			case 4:
				monthStr = "五月";
				break;
			case 5:
				monthStr = "六月";
				break;
			case 6:
				monthStr = "七月";
				break;
			case 7:
				monthStr = "八月";
				break;
			case 8:
				monthStr = "九月";
				break;
			case 9:
				monthStr = "十月";
				break;
			case 10:
				monthStr = "十一";
				break;
			case 11:
				monthStr = "十二";
				break;
			default:
				break;
		}

		return monthStr;
	}

	public static String getCountDays(String birthday) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		try {
			Date date = sdf.parse(birthday);
			long days = Math.abs(System.currentTimeMillis() - date.getTime()) / DayTime;
			return days + "天";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "--天";
		}
	}

	public static String getNearMonthDays(String birthday) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		try {
			Date date = sdf.parse(birthday);
			Calendar cal = Calendar.getInstance(Locale.CHINA);
			long days = Math.abs(System.currentTimeMillis() - date.getTime()) / DayTime;
			return days + "天";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "--天";
		}
	}

	/**
	 * 格式化日期
	 *
	 * @param times
	 * @return
	 */
	public static String format(long times) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date date = new Date(times);
		return sdf.format(date);
	}

	/**
	 * 解析日期
	 *
	 * @param dateStr
	 * @return
	 */
	public static long parserDateString(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return date.getTime();
	}

	/**
	 * 格式化日期时间字符串
	 *
	 * @param times
	 * @return
	 */
	public static String formatDateTime(long times) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		Date date = new Date(times);
		return sdf.format(date);
	}

	/**
	 * 格式化日期时间字符串
	 *
	 * @param times
	 * @return
	 */
	public static String formatTimeStr(long times) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
		Date date = new Date(times);
		return sdf.format(date);
	}

	/**
	 * 计算距今的时间
	 *
	 * @param time
	 * @return
	 */
	public static String formatRecentTime(String time) {
		if (null == time || "".equals(time))
			return "";
		Date commentTime = null;
		Date currentTime = null;
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			commentTime = dfs.parse(time);
			currentTime = Calendar.getInstance().getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long between = (currentTime.getTime() - commentTime.getTime()) / 1000;// 除以1000是为了转换成秒

		long year = between / (24 * 3600 * 30 * 12);
		long month = between / (24 * 3600 * 30);
		long week = between / (24 * 3600 * 7);
		long day = between / (24 * 3600);
		long hour = between % (24 * 3600) / 3600;
		long minute = between % 3600 / 60;
		long second = between % 60 / 60;

		StringBuffer sb = new StringBuffer();
		if (year != 0) {
			sb.append(year + "年");
			return sb.toString() + "前";
		}
		if (month != 0) {
			sb.append(month + "个月");
			return sb.toString() + "前";
		}
		if (week != 0) {
			sb.append(week + "周");
			return sb.toString() + "前";
		}
		if (day != 0) {
			sb.append(day + "天");
			return sb.toString() + "前";
		}
		if (hour != 0) {
			sb.append(hour + "小时");
			return sb.toString() + "前";
		}
		if (minute != 0) {
			sb.append(minute + "分钟");
			return sb.toString() + "前";
		}
		if (second != 0) {
			sb.append(second + "秒");
			return sb.toString() + "前";
		}

		return "";
	}

	/**
	 * 格式化为应用 常见显示格式 当前天显示时间，其他显示年月日
	 *
	 * @param strTime
	 * @return
	 */
	public static String formatLatelyTime(String strTime) {
		if (null == strTime || "".equals(strTime))
			return "";
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentTime = null;
		Date commentTime = null;
		String str = null;
		try {
			currentTime = Calendar.getInstance().getTime();
			commentTime = dfs.parse(strTime);
			if (currentTime.getYear() == commentTime.getYear()
					&& currentTime.getMonth() == commentTime.getMonth()
					&& currentTime.getDate() == commentTime.getDate()) {
				dfs = new SimpleDateFormat("HH:mm");
				str = dfs.format(commentTime);
			} else {
				dfs = new SimpleDateFormat("yyyy-MM-dd");
				str = dfs.format(commentTime);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 获取中文日期,当年只显示月日,其他显示年月日
	 * @param strTime
	 * @return
	 */
	public static String formatDateZh(String strTime) {
		if (null == strTime || "".equals(strTime))
			return "";
		Date currentTime = null;
		Date commentTime = null;
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
		String str = null;
		try {
			currentTime = Calendar.getInstance().getTime();
			commentTime = dfs.parse(strTime);
			if (currentTime.getYear() == commentTime.getYear()) {
				str = (commentTime.getMonth()+1)+"月"+commentTime.getDate()+"日";
			} else {
				str = commentTime.getYear()+1900+"年"+(commentTime.getMonth()+1)+"月"+commentTime.getDate()+"日";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 得到当前返回格式:yyyy-MM-dd
	 */
	public static String getCurrentDate() {
		Date nowDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(nowDate);
	}
	public static String calcCurrentAge(String birthday) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date =  sdf.parse(birthday);
			Calendar cal = Calendar.getInstance();
			long year = (System.currentTimeMillis() - date.getTime()) / YearTime;
			long month = (System.currentTimeMillis() - date.getTime()-year*YearTime) / MonthTime;
			System.out.println(""+year);
			System.out.println(""+month);
			if(year>0){
				if(month>0){
					return year+"岁"+month+"个月";
				}else{
					return year+"岁";
				}
			}else{
				if(month>0){
					return month+"个月";
				}else{
					return "未满月";
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return "--天";
		}
	}
	public static String calcCurrentAge1(String strTime) {
		if (null == strTime || "".equals(strTime))
			return "";
		Date birthday = null;
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		int year=0;
		int month=0;
		int day=0;
		try {
			birthday = dfs.parse(strTime);
			if (cal.before(birthday)) {
				throw new IllegalArgumentException(
						"The birthDay is before Now.It's unbelievable!");
			}

			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

			cal.setTime(birthday);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

			year = yearNow - yearBirth;
			if(year>0){
				if (monthNow <= monthBirth) {
					if (monthNow == monthBirth) {
						if (dayOfMonthNow < dayOfMonthBirth) {
							year--;
						}
					} else {
						year--;
						month=monthNow - monthBirth;

					}
				}else {
					month = monthNow - monthBirth;
					if (monthNow <= monthBirth) {
						if (dayOfMonthNow < dayOfMonthBirth) {
							month--;
						}
					}
				}
			}else{
				month = monthNow - monthBirth;
				if (dayOfMonthNow < dayOfMonthBirth) {
					month--;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return year+"周岁"+month+"个月";
	}

	public static String formatDayofweek(int dayOfWeek){
		switch (dayOfWeek){
			case 1:
				return "Mon";
			case 2:
				return "Tues";
			case 3:
				return "Wed";
			case 4:
				return "Thur";
			case 5:
				return "Fri";
			case 6:
				return "Sat";
			case 7:
				return "Sun";
		}
		return "";
	}

	/**
	 * 格式化为应用 常见显示格式 日期格式：今天以前：2016-08-12  14:26，今天：今天  20:30
	 *
	 * @param strTime
	 * @return
	 */
	public static String formatTime(String strTime) {
		if (null == strTime || "".equals(strTime))
			return "";
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentTime = null;
		Date commentTime = null;
		String str = null;
		try {
			currentTime = Calendar.getInstance().getTime();
			commentTime = dfs.parse(strTime);
			if (currentTime.getYear() == commentTime.getYear()
					&& currentTime.getMonth() == commentTime.getMonth()
					&& currentTime.getDate() == commentTime.getDate()) {
				dfs = new SimpleDateFormat("HH:mm");
				str = "今天" + dfs.format(commentTime);
			} else {
				dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				str = dfs.format(commentTime);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}
}