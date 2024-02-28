package edu.qc.seclass.rlm.roomLocalDB;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Converters {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    @TypeConverter
    public static String fromUUIDToString(UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }

    @TypeConverter
    public static UUID toUUID(String uuid) {
        return uuid == null ? null : UUID.fromString(uuid);
    }

    @TypeConverter
    public static String fromLocalDateToString(LocalDate date) {
        return date == null ? null : date.format(DATE_FORMATTER);
    }

    @TypeConverter
    public static LocalDate toLocalDate(String dateString) {
        return dateString == null ? null : LocalDate.parse(dateString, DATE_FORMATTER);
    }

    @TypeConverter
    public static String fromLocalTimeToString(LocalTime time) {
        return time == null ? null : time.format(TIME_FORMATTER);
    }

    @TypeConverter
    public static LocalTime toLocalTime(String timeString) {
        return timeString == null ? null : LocalTime.parse(timeString, TIME_FORMATTER);
    }
}