package edu.uob.prototype;

import java.time.LocalDate;

public class LocalDateTools {

    // Is date1 larger than date2
    public static boolean isLarger(LocalDate date1, LocalDate date2) {
        return date1.compareTo(date2) > 0;
    }

}
