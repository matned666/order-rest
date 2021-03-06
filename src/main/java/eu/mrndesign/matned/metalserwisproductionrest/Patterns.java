package eu.mrndesign.matned.metalserwisproductionrest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Patterns {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static boolean isCorrectDate(String date, DateTimeFormatter formatter){
        if (date != null) {
            try {
                LocalDateTime.parse(date, formatter);
                return true;
            } catch (DateTimeParseException e1) {
                try {
                    LocalDate.parse(date, formatter);
                    return true;
                } catch (DateTimeParseException e2) {
                    return false;
                }
            }
        } return false;
    }

}
