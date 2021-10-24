package br.com.sousa.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    private DateUtil(){}

    public static final Long DEFAULT_TIME_LENGTH = 1L;

    public static Date getExpirationDate(LocalDateTime dateStart, Long minutes) {
        return convertDate(dateStart.plusMinutes(minutes));
    }

    public static Date convertDate(LocalDateTime dateTime) {
        return java.util.Date
                .from(dateTime.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static boolean isExpired(Date expirationDate) {
        return new Date().after(expirationDate);
    }
}
