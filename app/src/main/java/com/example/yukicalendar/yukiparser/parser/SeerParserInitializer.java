package com.example.yukicalendar.yukiparser.parser;

import android.content.Context;
import android.widget.Toast;

import com.example.yukicalendar.model.CalendarEvent;
import com.example.yukicalendar.yukiparser.Config;
import com.example.yukicalendar.yukiparser.SuggestionRow;
import com.example.yukicalendar.yukiparser.parser.handler.SuggestionHandler;
import com.example.yukicalendar.yukiparser.parser.handler.english.DOWSuggestionHandler_EN;
import com.example.yukicalendar.yukiparser.parser.handler.english.DateSuggestionHandler_EN;
import com.example.yukicalendar.yukiparser.parser.handler.english.LocationSuggestionHandler_EN;
import com.example.yukicalendar.yukiparser.parser.handler.english.NumberRelativeTimeSuggestionHandler_EN;
import com.example.yukicalendar.yukiparser.parser.handler.english.RelativeTimeSuggestionHandler_EN;
import com.example.yukicalendar.yukiparser.parser.handler.english.TODSuggestionHandler_EN;
import com.example.yukicalendar.yukiparser.parser.handler.english.TimeSuggestionHandler_EN;
import com.example.yukicalendar.yukiparser.parser.model.ParsedEvent;

import java.util.ArrayList;
import java.util.List;


public class SeerParserInitializer {

    private final TimeSuggestionBuilder timeSuggestionBuilder;
    private Context context;
    SuggestionHandler numberRelativeTimeSuggestionHandler;
    private LocationSuggestionHandler_EN locationSuggestionHandler;

    private void initializeHandlers(@Config.Language int language) {
        // handlers
        SuggestionHandler relativeTimeSuggestionHandler;
        SuggestionHandler dateSuggestionHandler;
        SuggestionHandler dowSuggestionHandler;
        SuggestionHandler timeSuggestionHandler;
        SuggestionHandler todSuggestionHandler;

        switch (language) {

            case Config.Language.ENGLISH:
                numberRelativeTimeSuggestionHandler = new NumberRelativeTimeSuggestionHandler_EN();
                relativeTimeSuggestionHandler = new RelativeTimeSuggestionHandler_EN();
                dateSuggestionHandler = new DateSuggestionHandler_EN(language);
                dowSuggestionHandler = new DOWSuggestionHandler_EN();
                timeSuggestionHandler = new TimeSuggestionHandler_EN();
                todSuggestionHandler = new TODSuggestionHandler_EN();
                locationSuggestionHandler = new LocationSuggestionHandler_EN();
                break;

            // TODO implement other languages here

            default:
                numberRelativeTimeSuggestionHandler = new NumberRelativeTimeSuggestionHandler_EN();
                relativeTimeSuggestionHandler = new RelativeTimeSuggestionHandler_EN();
                dateSuggestionHandler = new DateSuggestionHandler_EN(language);
                dowSuggestionHandler = new DOWSuggestionHandler_EN();
                timeSuggestionHandler = new TimeSuggestionHandler_EN();
                todSuggestionHandler = new TODSuggestionHandler_EN();

        }

        // build handler chain
        numberRelativeTimeSuggestionHandler.setNextHandler(relativeTimeSuggestionHandler);
        relativeTimeSuggestionHandler.setNextHandler(dateSuggestionHandler);
        dateSuggestionHandler.setNextHandler(dowSuggestionHandler);
        dowSuggestionHandler.setNextHandler(timeSuggestionHandler);
        timeSuggestionHandler.setNextHandler(todSuggestionHandler);
        todSuggestionHandler.setNextHandler(locationSuggestionHandler);
    }

    public SeerParserInitializer(Context context, Config config) {
        this.context = context;

        initializeHandlers(config.getLanguage());


        // builders
        timeSuggestionBuilder = new TimeSuggestionBuilder(config);
        NumberRelativeTimeSuggestionBuilder numberRelativeTimeSuggestionBuilder = new NumberRelativeTimeSuggestionBuilder(config);
        RelativeTimeSuggestionBuilder relativeTimeSuggestionBuilder = new RelativeTimeSuggestionBuilder(config);
        DateSuggestionBuilder dateSuggestionBuilder = new DateSuggestionBuilder(config);
        DOWSuggestionBuilder dowSuggestionBuilder = new DOWSuggestionBuilder(config);
        TODSuggestionBuilder todSuggestionBuilder = new TODSuggestionBuilder(config);

        // build builder chain
        timeSuggestionBuilder.setNextBuilder(todSuggestionBuilder);
        todSuggestionBuilder.setNextBuilder(numberRelativeTimeSuggestionBuilder);
        numberRelativeTimeSuggestionBuilder.setNextBuilder(relativeTimeSuggestionBuilder);
        relativeTimeSuggestionBuilder.setNextBuilder(dateSuggestionBuilder);
        dateSuggestionBuilder.setNextBuilder(dowSuggestionBuilder);
    }

    public CalendarEvent parseInput(String input) {

        // Stores information about the user input
        SuggestionValue suggestionValue = new SuggestionValue();

        // Interpret user input and store values in suggestion value
        numberRelativeTimeSuggestionHandler.handle(context, input, suggestionValue);

        suggestionValue.init();

        List<SuggestionRow> suggestionList = new ArrayList<>(3);
        // Build suggestion list base on the user input (i.e. the suggestion value)
        timeSuggestionBuilder.build(context, suggestionValue, suggestionList);


        if (suggestionList.size() > 0) {
            int timestamp = suggestionList.get(0).getValue();
            if (timestamp > 0) {
                Toast.makeText(context, locationSuggestionHandler.getLocationString(), Toast.LENGTH_SHORT).show();
                ParsedEvent event = new ParsedEvent();
                event.setTitle(input);
                event.setDtStart(timestamp * 1000L);
                long millisecondsInAnHour = 60 * 60 * 1000L;
                event.setDtEnd(timestamp * 1000L + millisecondsInAnHour);
                event.setLocation(locationSuggestionHandler.getLocationString());
                return event.toCalendarEvent();
            }
        }

        return null;
    }

}
