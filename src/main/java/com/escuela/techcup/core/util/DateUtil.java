package com.escuela.techcup.core.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtil {

	private DateUtil(){
	}
    
    public static LocalDate today() {
		return LocalDate.now();
	}

	public static boolean isPast(LocalDate date) {
		ValidationUtil.requireNotNull(date, "fecha");
		return date.isBefore(today());
	}

	public static boolean isTodayOrFuture(LocalDate date) {
		ValidationUtil.requireNotNull(date, "fecha");
		return !date.isBefore(today());
	}

    public static long minutesUntil(LocalDateTime dateTime) {
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), dateTime);
    }

    public static boolean isInThePast(LocalDateTime dateTime) {
        return dateTime.isBefore(LocalDateTime.now());
    }

    public static boolean isWithinOneHour(LocalDateTime dateTime) {
        return minutesUntil(dateTime) < 60;
    }
}
