package date_util_java_8;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface MondayCheckers {
    default LocalDate[] mondays(Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        LocalDate firstDayOfMonth = localDateTime.toLocalDate().withDayOfMonth(1);
        List<LocalDate> mondays = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LocalDate monday = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
                    .with(TemporalAdjusters.dayOfWeekInMonth(i + 1, DayOfWeek.MONDAY));
            if (monday.getMonth() != firstDayOfMonth.getMonth()) {
                break;
            }
            mondays.add(monday);
        }
        return mondays.toArray(new LocalDate[0]);
    }

    default LocalDate[] mondays1(Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        LocalDate firstDayOfMonth = localDateTime.toLocalDate().withDayOfMonth(1);
        return IntStream.rangeClosed(1, firstDayOfMonth.getMonth().length(firstDayOfMonth.isLeapYear()))
                .mapToObj(dayOfMonth -> LocalDate.of(firstDayOfMonth.getYear(), firstDayOfMonth.getMonth(), dayOfMonth))
                .filter(date -> date.getDayOfWeek() == DayOfWeek.MONDAY)
                .toArray(LocalDate[]::new);
    }

    default LocalDate[] mondays0(Instant instant) {
        return IntStream.rangeClosed(1, YearMonth.from(instant.atZone(ZoneOffset.UTC)).lengthOfMonth())
                .mapToObj(day -> LocalDate.ofInstant(instant, ZoneOffset.UTC).withDayOfMonth(day))
                .filter(date -> date.getDayOfWeek() == DayOfWeek.MONDAY)
                .toArray(LocalDate[]::new);
    }


    default LocalDate[] mondays2(Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        LocalDate firstDayOfMonth = localDateTime.toLocalDate().withDayOfMonth(1);
        LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
        List<LocalDate> mondays = new ArrayList<>();
        LocalDate monday = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        while (monday.isBefore(lastDayOfMonth) || monday.isEqual(lastDayOfMonth)) {
            mondays.add(monday);
            monday = monday.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }
        return mondays.toArray(new LocalDate[0]);
    }

    default LocalDate[] mondays3(Instant instant) {
        LocalDate firstDayOfMonth = LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toLocalDate().withDayOfMonth(1);
        List<LocalDate> mondays = new ArrayList<>();
        LocalDate monday = firstDayOfMonth.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        while (monday.getMonth() == firstDayOfMonth.getMonth()) {
            mondays.add(monday);
            monday = monday.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }
        return mondays.toArray(new LocalDate[0]);
    }

    default LocalDate[] mondays4(Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        LocalDate firstDayOfMonth = localDateTime.toLocalDate().withDayOfMonth(1);
        LocalDate firstMonday = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        LocalDate lastMonday = firstDayOfMonth.with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY));
        int numberOfMondays = (int) ChronoUnit.WEEKS.between(firstMonday, lastMonday.plusDays(1));
        return Stream.iterate(firstMonday, date -> date.plusWeeks(1))
                .limit(numberOfMondays + 1)
                .toArray(LocalDate[]::new);
    }
}
