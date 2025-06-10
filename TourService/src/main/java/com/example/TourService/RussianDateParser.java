package com.example.TourService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RussianDateParser {


    private static final Map<String, String> MONTH_MAP = new HashMap<>();
    static {
        MONTH_MAP.put("янв", "Jan");
        MONTH_MAP.put("фев", "Feb");
        MONTH_MAP.put("мар", "Mar");
        MONTH_MAP.put("апр", "Apr");
        MONTH_MAP.put("май", "May");
        MONTH_MAP.put("июн", "Jun");
        MONTH_MAP.put("июл", "Jul");
        MONTH_MAP.put("авг", "Aug");
        MONTH_MAP.put("сен", "Sep");
        MONTH_MAP.put("окт", "Oct");
        MONTH_MAP.put("ноя", "Nov");
        MONTH_MAP.put("дек", "Dec");
    }

    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{1,2})\\s+(\\p{L}{3})");

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy");

    /**
     * Преобразует русскую дату в формате "13 июн" в LocalDate
     * @param russianDate дата в формате "день месяц" (например, "13 июн")
     * @param year год (если не указан, используется текущий)
     * @return LocalDate объект
     */
    public static LocalDate parseRussianDate(String russianDate, int year) {
        if (russianDate == null || russianDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Дата не может быть пустой");
        }

        Matcher matcher = DATE_PATTERN.matcher(russianDate.trim());
        if (!matcher.find()) {
            throw new IllegalArgumentException("Неверный формат даты: " + russianDate);
        }

        String day = matcher.group(1);
        String russianMonth = matcher.group(2).toLowerCase();

        String englishMonth = MONTH_MAP.get(russianMonth);
        if (englishMonth == null) {
            throw new IllegalArgumentException("Неизвестный месяц: " + russianMonth);
        }

        try {
            String englishDateString = day + " " + englishMonth + " " + year;
            return LocalDate.parse(englishDateString, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ошибка парсинга даты: " + russianDate, e);
        }
    }

    public static LocalDate parseRussianDate(String russianDate) {
        return parseRussianDate(russianDate, LocalDate.now().getYear());
    }

    public static boolean isValidRussianDate(String dateString) {
        try {
            parseRussianDate(dateString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}