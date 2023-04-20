package date_util_java_8;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;


public class DateUtils implements MondayCheckers{
    public static void main(String[] args) {


        new DateUtils().run();
    }

    LocalDate localDate1 = LocalDate.of(1980, 6, 13);
    Instant instant = Instant.parse("2023-05-19T12:00:00Z");
    String language=  "ru";
    LocalDate localDate2 = LocalDate.of(1928, 10, 03);
    ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());



    private void run() {

        System.out.println(formatFullJava8(zonedDateTime,language));
        System.out.println(betweenDates(localDate1, localDate2));
        System.out.println(between(localDate1, localDate2));
        System.out.println(Arrays.toString(mondays(instant)));
        System.out.println(Arrays.toString(mondays0(instant)));
        System.out.println(Arrays.toString(mondays1(instant)));
        System.out.println(Arrays.toString(mondays2(instant)));
        System.out.println(Arrays.toString(mondays3(instant)));
        System.out.println(Arrays.toString(mondays4(instant)));
        System.out.println(localDate1 + " is on Friday? " + isFridays13(localDate1));
    }
    // beállítja a a locale-nak megfeleő a dátumformázást 2 és 3 betűst

    private String formatFullJava8(ZonedDateTime zonedDate, String language) {
        Locale locale = null;
        if (language.length() == 2) {
            locale = new Locale(language.toLowerCase());
        } else if (language.length() == 3) {
            for (Locale availableLocale : Locale.getAvailableLocales()) {
                if (availableLocale.getISO3Language().equals(language.toLowerCase())) {
                    locale = availableLocale;
                    break;
                }
            }
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale);
        return zonedDate.format(formatter);
    }

    public boolean isFridays13(LocalDate localDate1) {
        return localDate1.getDayOfMonth() == 13 && localDate1.getDayOfWeek() == DayOfWeek.FRIDAY;
    }


    private String betweenDates(LocalDate date1, LocalDate date2) {
        LocalDate localDate1 = date1.isBefore(date2) ? date1 : date2;
        LocalDate localDate2 = date2.isAfter(date1) ? date2 : date1;
        Period timeDifference = Period.between(localDate1, localDate2);
        int[] diffs = {timeDifference.getYears(), timeDifference.getMonths(), timeDifference.getDays()};
        String[] timeUnit = {"year", "month", "day"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < timeUnit.length; i++) {
            sb.append(diffs[i] > 0 ? diffs[i] + " " + timeUnit[i] : "").append(diffs[i] > 1 ? "s " : " ");
        }
        return sb.toString().trim();
    }

    private String between(LocalDate date1, LocalDate date2) {
        LocalDate localDate1 = date1.isBefore(date2) ? date1 : date2;
        LocalDate localDate2 = date2.isAfter(date1) ? date2 : date1;
        Period timeDifference = Period.between(localDate1, localDate2);
        Map<String, Integer> diffs = new HashMap<>();
        diffs.put("year", timeDifference.getYears());
        diffs.put("month", timeDifference.getMonths());
        diffs.put("day", timeDifference.getDays());
        StringBuilder sb = new StringBuilder();
        for (String unit : diffs.keySet()) {
            int diff = diffs.get(unit);
            if (diff > 0) {
                String unitString = unit + (diff > 1 ? "s" : "");
                sb.append(String.format("%d %s ", diff, unitString));
            }
        }
        return sb.toString().trim();
    }

}
