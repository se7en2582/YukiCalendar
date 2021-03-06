package com.example.yukicalendar.yukiparser.parser.model;


import com.example.yukicalendar.yukiparser.parser.SuggestionValue;

/**
 * @author p-v
 * Time item class.
 */
public class TimeItem extends SuggestionValue.LocalItemItem {

    public boolean isAmPmPresent;

    public TimeItem(int value, boolean amPmPresent, int startIdx, int endIdx) {
        super(value, startIdx, endIdx);
        this.isAmPmPresent = amPmPresent;
    }
}
