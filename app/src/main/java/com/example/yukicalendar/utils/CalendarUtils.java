package com.example.yukicalendar.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author p-v
 */

public class CalendarUtils {

    public static String getDisplayTime(Context context, Calendar cal, Locale locale) {
        String format;
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            format = "HH:mm";
        } else {
            if (cal.get(Calendar.MINUTE) == 0) {
                format = "ha";
            } else {
                format = "h:mma";
            }
        }
        DateFormat df = new SimpleDateFormat(format, locale);
        return df.format(cal.getTime()).toLowerCase();
    }


    public static int getDayOfMonth(Calendar cal) {
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static String getDayOfWeek(Calendar calendar) {
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
    }

    public static String getDisplayTime(Context context, long time, Locale locale) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String format;
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            format = "HH:mm";
        } else {
            if (cal.get(Calendar.MINUTE) == 0) {
                format = "ha";
            } else {
                format = "h:mma";
            }
        }
        DateFormat df = new SimpleDateFormat(format, locale);
        return df.format(cal.getTime()).toLowerCase();
    }
}
