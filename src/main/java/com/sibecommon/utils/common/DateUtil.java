
package com.sibecommon.utils.common;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;


/**
 * The type Date util.
 */
public class DateUtil {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DateUtil.class);


    /**
     * Gets date to local date time.
     *
     * @param date the date
     * @return the date to local date time
     */
    public static LocalDateTime getDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }


    public static java.time.LocalDate getDateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime= LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }


    public static void main(String[] args) {
       Date bb= DateUtil.cenvertStringToDate("20180921", "yyyyMMdd");
        LocalDateTime cc =  getDateToLocalDateTime(bb);

    }

    /**
     * 字体转日期
     * Gets yyyymmddhhmmto date.
     *
     * @param date the date
     * @return the yyyymmddhhmmto date
     */
    public static final Date getYYYYMMDDHHMMTODate(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMddHHmm");
        DateTime dateTime = dtf.parseDateTime(date);
        return dateTime.toDate();
    }

    /**
     * 字体转日期
     * Gets yyyymmdd todate.
     *
     * @param date the date
     * @return the yyyymmdd todate
     */
    public static final Date getYYYYMMDDTodate(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dateTime = dtf.parseDateTime(date);
        return dateTime.toDate();
    }


    /**
     * 日期转字符
     * Gets yyyymmd dto ddmmyy.
     *
     * @param date the date
     * @return the yyyymmd dto ddmmyy
     */
    public static String getYYYYMMDDtoDDMMYY(String date) {
        return getDateToDDMMYY(getYYYYMMDDTodate(date));
    }

    /**
     * 日期转字符
     *
     * @param date the date
     * @return the date to ddmmyy
     */
    public static String getDateToDDMMYY(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString("ddMMyy");
    }

    /**
     * 日期转字符
     *
     * @param date the date
     * @return the date to yyyymmdd
     */
    public static String getDateToYYYYMMDD(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString("yyyyMMdd");
    }

    /**
     * 日期转字符
     *
     * @param date the date
     * @return the date to ddmmyyyy
     */
    public static String getDateToDDMMYYYY(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString("ddMMyyyy");
    }


    /**
     * Gets date to ddmmyyyy.
     *
     * @param date the date
     * @return the date to ddmmyyyy
     */
    public static String getYYYYMMDDToDDMMYYYY(String date) {

        return getDateToDDMMYYYY(getYYYYMMDDTodate(date));
    }


    /**
     * Gets yyyymmddhhm ito ddmmyy.
     *
     * @param date the date
     * @return the yyyymmddhhm ito ddmmyy
     */
    public static String getYYYYMMDDHHMItoDDMMYY(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMddHHmm");
        DateTime dateTime = dtf.parseDateTime(date);
        return dateTime.toString("ddMMyy");
    }

    /**
     * Gets yyyymmddhhm ito hhmi.
     *
     * @param date the date
     * @return the yyyymmddhhm ito hhmi
     */
    public static String getYYYYMMDDHHMItoHHMI(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMddHHmm");
        DateTime dateTime = dtf.parseDateTime(date);
        return dateTime.toString("HHmm");
    }

    /**
     * Gets mm myyto yyyymmdd.
     *
     * @param date the date
     * @return the mm myyto yyyymmdd
     */
    public static String getddMMMyytoYYYYMMDD(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("ddMMMyy");
        DateTime dateTime = dtf.parseDateTime(date);
        return dateTime.toString("yyyyMMdd");

    }

    /**
     * Gets ddmmy yto yyyymmdd.
     *
     * @param date the date
     * @return the ddmmy yto yyyymmdd
     */
    public static String getDDMMYYtoYYYYMMDD(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("ddMMyy");
        DateTime dateTime = dtf.parseDateTime(date);
        return dateTime.toString("yyyyMMdd");

    }


    /**
     * Gets ddmmyyy yto yyyymmdd.
     *
     * @param date the date
     * @return the ddmmyyy yto yyyymmdd
     */
    public static String getDDMMYYYYtoYYYYMMDD(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("ddMMyyyy");
        DateTime dateTime = dtf.parseDateTime(date);
        return dateTime.toString("yyyyMMdd");
    }


    /**
     * 校验日期格式
     *
     * @param date      the date
     * @param formatter the formatter
     * @return boolean boolean
     */
    public static boolean isValidDate(String date, String formatter) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern(formatter).withZoneUTC();
        try {
            dtf.parseDateTime(date);
        } catch (IllegalArgumentException e) {
            LOGGER.error(date + ":" + formatter + ":" + e.getMessage());
            return false;
        }

        return true;
    }


    /**
     * 如果是今天之前的日期则返回True，否则返回False
     * Is past date boolean.
     *
     * @param date      the date
     * @param formatter the formatter
     * @return the boolean
     */
    public static boolean isPastDate(String date, String formatter) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern(formatter);
        DateTime start = dtf.parseDateTime(date);
        DateTime end = new DateTime();
        Period p = new Period(start, end, PeriodType.days());
        int days = p.getDays();
        return days > 0 ? true : false;
    }


    /**
     * 日期超过当前系统日期days天，则返回True，否则返回False
     * Is over days boolean.
     *
     * @param date      the date
     * @param formatter the formatter
     * @param days      the days
     * @return the boolean
     */
    public static boolean isOverDays(String date, String formatter, int days) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern(formatter);
        DateTime end = dtf.parseDateTime(date);
        DateTime today = new DateTime();
        DateTime start = today.plusDays(days);
        Period p = new Period(start, end, PeriodType.days());
        int overDays = p.getDays();
        return overDays > 0 ? true : false;
    }


    /**
     * 日期1大于日期2则返回True，否则返回False
     * Gets date substruction.
     *
     * @param afterDate  the after date
     * @param beforeDate the before date
     * @return the date substruction
     */
    public static int getDateSubstruction(Date afterDate, Date beforeDate) {
        DateTime start = new DateTime(beforeDate);
        DateTime end = new DateTime(afterDate);
        Period p = new Period(start, end, PeriodType.days());
        return p.getDays();
    }


    /**
     * 日期1大于日期2则返回True，否则返回False
     * Gets date substruction.
     *
     * @param endDate    the end date
     * @param formatter1 the formatter 1
     * @param startDate  the start date
     * @param formatter2 the formatter 2
     * @return the date substruction
     */
    public static boolean getDateSubstruction(String endDate, String formatter1, String startDate, String formatter2) {
        DateTimeFormatter dtf1 = DateTimeFormat.forPattern(formatter1);
        DateTime start = dtf1.parseDateTime(startDate);
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern(formatter2);
        DateTime end = dtf2.parseDateTime(endDate);
        Period p = new Period(start, end, PeriodType.days());
        int days = p.getDays();
        return days > 0 ? true : false;
    }

    /**
     * 根据生日得到年龄
     * Gets age from birth.
     *
     * @param birth     the birth
     * @param formatter the formatter
     * @return the age from birth
     */
    public static int getAgeFromBirth(String birth, String formatter) {
        DateTimeFormatter format = DateTimeFormat.forPattern(formatter);
        //时间解析
        LocalDate birthday = DateTime.parse(birth, format).toLocalDate();
        LocalDate now = new LocalDate();
        Period period = new Period(birthday, now, PeriodType.yearMonthDay());
        return period.getYears();
    }

    /**
     * 根据生日得到年龄
     * Gets age from birth.
     *
     * @param birth the birth
     * @return the age from birth
     */
    /***
     * 以compareDateString - birth得到年龄
     * @param birth 生日
     * @param birthFormatter 生日format字符串
     * @param compareDateString 比较日期
     * @param compareDateFormatter 比较日期format字符串
     * @return age from birth by assign date
     */
    public static int getAgeFromBirthByAssignDate(String birth, String birthFormatter, String compareDateString, String compareDateFormatter) {
        DateTimeFormatter birthFormat = DateTimeFormat.forPattern(birthFormatter);
        LocalDate birthday = DateTime.parse(birth, birthFormat).toLocalDate();

        DateTimeFormatter compareDateFormat = DateTimeFormat.forPattern(compareDateFormatter);
        LocalDate compareDate = DateTime.parse(compareDateString, compareDateFormat).toLocalDate();

        Period period = new Period(birthday, compareDate, PeriodType.yearMonthDay());
        return period.getYears();
    }

    /**
     * 日期转字符
     *
     * @param date the date
     * @return the date to yyyy-MM-dd
     */
    public static String getDateToYYYY_MM_DD(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString("yyyy-MM-dd");
    }


    /**
     * Gets yyyymmd dto yyyy mm dd.
     *
     * @param fromDate the from date
     * @return the yyyymmd dto yyyy mm dd
     */
    public static String getYYYYMMDDtoYYYY_MM_DD(String fromDate) {
        return getDateToYYYY_MM_DD(getYYYYMMDDTodate(fromDate));
    }

    /**
     * Gets yyyymmddh hssto yyyy mm dd.
     *
     * @param fromDate the from date
     * @return the yyyymmddh hssto yyyy mm dd
     */
    public static String getYYYYMMDDHHsstoYYYY_MM_DD(String fromDate) {
        return getDateToYYYY_MM_DD(getYYYYMMDDHHssTodate(fromDate));
    }

    /**
     * Gets yyyymmddh hss todate.
     *
     * @param date the date
     * @return the yyyymmddh hss todate
     */
    public static final Date getYYYYMMDDHHssTodate(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMddHHss");
        DateTime dateTime = dtf.parseDateTime(date);
        return dateTime.toDate();
    }

    /**
     * Gets galileo time string.
     *
     * @param date the date
     * @return the galileo time string
     */
    public static String getGalileoTimeString(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString("yyyy-MM-dd") + "T" + dateTime.toString("HH:MM:ss.SSS") + "+08:00";
    }

    /**
     * Get offset date to formal string string.
     *
     * @param date   the date
     * @param offset the offset
     * @return the string
     */
    public static String getOffsetDateToFormalString(Date date, int offset){
        DateTime dateTime = new DateTime(date);
        DateTime modifieDateTime = dateTime.plusDays(offset);
        return modifieDateTime.toString("yyyy-MM-dd") + "T" + modifieDateTime.toString("HH:mm:ss.SSS") + "+08:00";
    }

    /**
     * Get offset date to formal string string.
     *
     * @param dateTime the date time
     * @param offset   the offset
     * @return the string
     */
    public static String getOffsetDateToFormalString(DateTime dateTime, int offset){
        DateTime modifieDateTime = dateTime;
        String offsetString = "";
        if(offset < 0){
            offsetString += "-";
        }else {
            offsetString += "+";
        }
        if(Math.abs(offset) < 10){
            offsetString = offsetString +"0" + offset + ":00";
        }else {
            offsetString = offsetString + offset + ":00";
        }
        return modifieDateTime.toString("yyyy-MM-dd") + "T" + modifieDateTime.toString("HH:mm:ss.SSS") + offsetString;
    }

    /**
     * Gets yyyymmddh hmmss todate.
     *
     * @param date the date
     * @return the yyyymmddh hmmss todate
     */
    public static final Date getYYYYMMDDHHmmssTodate(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMddHHmmss");
        DateTime dateTime = dtf.parseDateTime(date);
        return dateTime.toDate();
    }

    /**
     * Cenvert string to date date.
     * 根据指定的字符格式转换为日期类型
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the date
     */
    public static final Date cenvertStringToDate(String date,String pattern) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
        DateTime dateTime = dtf.parseDateTime(date);
        return dateTime.toDate();
    }


    /**
     * 字体转日期
     * Gets mm myyto date.
     *
     * @param date the date
     * @return the mm myyto date
     */
    public static Date getddMMMyytoDate(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("ddMMMyy");
        DateTime dateTime = dtf.parseDateTime(date);
        return dateTime.toDate();
    }

    /**
     * Convert to timestamp timestamp.
     *
     * @param dateTime the date time
     * @return the timestamp
     */
    public static Timestamp convertToTimestamp(ZonedDateTime dateTime) {
        return dateTime == null ? null : Timestamp.from(dateTime.toInstant());
    }

    /**
     * Convert to zoned date time zoned date time.
     *
     * @param timestamp the timestamp
     * @return the zoned date time
     */
    public static ZonedDateTime convertToZonedDateTime(Timestamp timestamp) {
        return timestamp == null ? null : ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("Asia/Shanghai"));
    }

}
