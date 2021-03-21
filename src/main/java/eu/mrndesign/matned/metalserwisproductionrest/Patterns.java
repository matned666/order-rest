package eu.mrndesign.matned.metalserwisproductionrest;

import com.sun.el.parser.ParseException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Patterns {

    private static List<String> formatStrings = Arrays.asList("yyyy-MM-dd", "dd-MM-yyyy", "ddMMyyyy","yyyyMMdd", "dd.MM.yyyy");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;



    public static Date getDateFromString(String dateString)
    {
        for (String formatString : formatStrings)
        {
            try
            {
                return Date.from(LocalDate.parse(dateString, DateTimeFormatter.ofPattern(formatString)).atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            catch (Exception ignored) {}
        }

        return null;
    }

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
