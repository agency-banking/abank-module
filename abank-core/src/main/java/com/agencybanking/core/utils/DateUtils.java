package com.agencybanking.core.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

//    public static LocalDate toLocalDate(Date date) {
//        return LocalDate.f(date.toInstant(), ZoneId.systemDefault());
//    }
//
//    public static Date localDateToDate(LocalDate localDate) {
//        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
//    }
    
    public static Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public static Date firstDayCurrentYear() {
        LocalDate date = LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
        LocalDate firstDayOfYear = date.with(TemporalAdjusters.firstDayOfYear());
        return Date.from(firstDayOfYear.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
