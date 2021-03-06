package com.example.yukicalendar.yukiparser.parser.handler.english;

import android.content.Context;

import com.example.yukicalendar.yukiparser.parser.SuggestionValue;
import com.example.yukicalendar.yukiparser.parser.handler.SuggestionHandler;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DOWSuggestionHandler_EN extends SuggestionHandler {

    private static final String DAY_OF_WEEK = "(next\\s{0,2})?(?:\\b(?:(?:(mon)|(fri)|(sun))(?:d(?:ay?)?)?)|\\b(tue(?:s(?:d(?:ay?)?)?)?)|\\b(wed(?:n(?:e(?:s(?:d(?:ay?)?)?)?)?)?)|\\b(thu(?:r(?:s(?:d(?:ay?)?)?)?)?)|\\b(sat(?:u(?:r(?:d(?:ay?)?)?)?)?))\\b";
    private Pattern pDow;

    public DOWSuggestionHandler_EN() {
        pDow = Pattern.compile(DAY_OF_WEEK, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public void handle(Context context, String input, SuggestionValue suggestionValue) {
        Matcher matcher = pDow.matcher(input);
        if (matcher.find()) {
            int value = -1;
            if (matcher.group(2) != null) {
                // Monday
                value = Calendar.MONDAY;
            } else if (matcher.group(3) != null) {
                // Friday
                value = Calendar.FRIDAY;
            } else if (matcher.group(4) != null) {
                // Sunday
                value = Calendar.SUNDAY;
            } else if (matcher.group(5) != null) {
                // Tuesday
                value = Calendar.TUESDAY;
            } else if (matcher.group(6) != null) {
                // Wednesday
                value = Calendar.WEDNESDAY;
            } else if (matcher.group(7) != null) {
                // Thursday
                value = Calendar.THURSDAY;
            } else if (matcher.group(8) != null) {
                // Saturday
                value = Calendar.SATURDAY;
            }
            if (value != -1) {
                if (matcher.group(1) != null) {
                    suggestionValue.appendSuggestion(SuggestionValue.DAY_OF_WEEK_NEXT, new SuggestionValue.LocalItemItem(value, matcher.start(), matcher.end()));
                } else {
                    suggestionValue.appendSuggestion(SuggestionValue.DAY_OF_WEEK, new SuggestionValue.LocalItemItem(value, matcher.start(), matcher.end()));
                }
            }
        }

        super.handle(context, input, suggestionValue);
    }

}
