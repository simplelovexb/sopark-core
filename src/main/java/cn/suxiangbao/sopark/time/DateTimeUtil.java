package cn.suxiangbao.sopark.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateTimeUtil {

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String COMPACT_DATETIME_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String COMPACT_DATE_FORMAT = "yyyyMMdd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String COMPACT_TIME_FORMAT = "HHmmss";
    public static final String COMPACT_MONTH_FORMAT = "yyyyMM";
    public static final String MONTH_FORMAT = "yyyy-MM";

    public static String formatDateTime(Date date) {
        return formatDate(date, DATETIME_FORMAT);
    }

    public static String formatCompactDateTime(Date date) {
        return formatDate(date, COMPACT_DATETIME_FORMAT);
    }

    public static String formatDate(Date date) {
        return formatDate(date, DATE_FORMAT);
    }

    public static String formatCompactDate(Date date) {
        return formatDate(date, COMPACT_DATE_FORMAT);
    }
    
    public static String formatMonth(Date date) {
        return formatDate(date, MONTH_FORMAT);
    }

    public static String formatCompactMonth(Date date) {
        return formatDate(date, COMPACT_MONTH_FORMAT);
    }

    public static String formatTime(Date date) {
        return formatDate(date, TIME_FORMAT);
    }

    public static String formatCompactTime(Date date) {
        return formatDate(date, COMPACT_TIME_FORMAT);
    }

    public static String formatDate(Date date, String pattern) {
        DateTime dt = new DateTime(date);

        return dt.toString(pattern);
    }

    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, DATE_FORMAT);
    }

    public static Date parseDate(String dateStr, String format) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
        DateTime dt = formatter.parseDateTime(dateStr);

        return dt.toDate();
    }

    public static DateTime toDateTime(Date date) {
        return new DateTime(date);
    }

    public static Date toDate(DateTime dateTime) {
        return dateTime.toDate();
    }

    public static Date addSeconds(Date date, int seconds) {
        DateTime dt = new DateTime(date);
        DateTime destDt = null;

        if (seconds == 0) {
            destDt = dt;
        } else if (seconds > 0) {
            destDt = dt.plusSeconds(seconds);
        } else {
            destDt = dt.minusSeconds(-seconds);
        }
        return destDt.toDate();
    }

    public static Date addMinutes(Date date, int minutes) {
        DateTime dt = new DateTime(date);
        DateTime destDt = null;

        if (minutes == 0) {
            destDt = dt;
        } else if (minutes > 0) {
            destDt = dt.plusMinutes(minutes);
        } else {
            destDt = dt.minusMinutes(-minutes);
        }
        return destDt.toDate();
    }

    public static Date addHours(Date date, int hours) {
        DateTime dt = new DateTime(date);
        DateTime destDt = null;

        if (hours == 0) {
            destDt = dt;
        } else if (hours > 0) {
            destDt = dt.plusHours(hours);
        } else {
            destDt = dt.minusHours(-hours);
        }
        return destDt.toDate();
    }

    public static Date addDays(Date date, int days) {
        DateTime dt = new DateTime(date);
        DateTime destDt = null;

        if (days == 0) {
            destDt = dt;
        } else if (days > 0) {
            destDt = dt.plusDays(days);
        } else {
            destDt = dt.minusDays(-days);
        }
        return destDt.toDate();
    }

    public static Date addMonths(Date date, int months) {
        DateTime dt = new DateTime(date);
        DateTime destDt = null;

        if (months == 0) {
            destDt = dt;
        } else if (months > 0) {
            destDt = dt.plusMonths(months);
        } else {
            destDt = dt.minusMonths(-months);
        }
        return destDt.toDate();
    }

    public static Date addYears(Date date, int years) {
        DateTime dt = new DateTime(date);
        DateTime destDt = null;

        if (years == 0) {
            destDt = dt;
        } else if (years > 0) {
            destDt = dt.plusYears(years);
        } else {
            destDt = dt.minusYears(-years);
        }
        return destDt.toDate();
    }

}
