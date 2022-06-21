package edu.uob.prototype;

import java.time.LocalDate;

public class LocalDateTools {

    // Is date1 larger than date2
    public static boolean isLarger(LocalDate date1, LocalDate date2) {
        return date1.compareTo(date2) > 0;
    }

    // Is date a weekend
    public static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek().toString().equals("SATURDAY") ||
                date.getDayOfWeek().toString().equals("SUNDAY");
    }

}
