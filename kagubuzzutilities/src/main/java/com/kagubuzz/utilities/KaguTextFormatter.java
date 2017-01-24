package com.kagubuzz.utilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.atteo.evo.inflector.English;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.web.util.HtmlUtils;

public class KaguTextFormatter {

    static final String DATE_LONG_FORM = "EEEEEEEEE, MMMMMMMMMM d h:mm aaa";
    static final String DATE_SHORT_FORM = "MMM d";

    static final String DATE = "MM/dd/yyyy";
    static final String TIME = "hh:mm a";

    public static String squashNull(String string) {
        return (string == null) ? "" : string;
    }

    public <T> T fromJSON(String json, TypeReference<Set<String>> typeReference) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, typeReference);
        }
        catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public String escapeHtmlString(String string) {
        return HtmlUtils.htmlEscape(string);
    }

    public static boolean isNumeric(String str) {
        if (str == null)
            return false;

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    public Date gmtDateToTzDate(Date date, int timeZoneOffsetInMinutes) {

        if (date == null)
            return null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, timeZoneOffsetInMinutes - 60);
        return cal.getTime();
    }

    public String isChecked(Boolean boolValue) {
        return (boolValue) ? "checked=\"checked\"" : "";
    }

    public String toJSON(Object object) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(object);
        }
        catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public String capitalizeFirstLetter(String formatterText) {
        return WordUtils.capitalize(formatterText);
    }

    public String getSummary(String formatterText) {
        return getSummary(formatterText, 400);
    }

    public String getSummary(String formatterText, int charCutOff) {

        if ((formatterText == null) || (formatterText.isEmpty()))
            return null;

        String newString = formatterText;
        newString = newString.replaceAll("\\<.*?\\>", "");
        newString = newString.replaceAll("[\r\n]+", " ");
        newString = (newString.length() >= charCutOff) ? newString.substring(0, charCutOff) + "..." : newString;

        return newString;
    }

    public static String formatForJavaScriptArray(List<String> list) {
        StringBuilder sb = new StringBuilder();

        for (String string : list) {
            sb.append("\"" + string + "\"" + ",");
        }

        return sb.substring(0, sb.length() - 1);
    }

    public String timeSinceCreated(Date formatterDate) {
        return this.timeSinceCreated(formatterDate, DATE_LONG_FORM);
    }

    public String timeSinceCreatedShortForm(Date formatterDate) {
        return this.timeSinceCreated(formatterDate, DATE_SHORT_FORM);
    }

    public String timeSinceCreated(Date formatterDate, String dateFormat) {
        Calendar createdCalendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        long millisecondsSincePost;

        if (formatterDate == null)
            return null;

        now.setTime(new Date());
        createdCalendar.setTime(formatterDate);
        millisecondsSincePost = (now.getTimeInMillis() - createdCalendar.getTimeInMillis());

        // See if the posting is older than 2 days and return Date if so

        if (millisecondsSincePost > TimeUnit.DAYS.toMillis(1L)) {
            int timePast = (int) TimeUnit.MILLISECONDS.toDays(millisecondsSincePost);

            return String.format("Around %d %s Ago", timePast, English.plural("Day", timePast));
        }
        // TODO:Add return of "3 days" or less
        // See if posting is older than 90 minutes and return hours if so

        if (millisecondsSincePost > TimeUnit.MINUTES.toMillis(60L)) {
            int timePast = (int) TimeUnit.MILLISECONDS.toHours(millisecondsSincePost);
            return String.format("Around %d %s Ago", timePast, English.plural("Hour", timePast));
        }

        // See if posting is older than 2 minute and send minutes if so

        if (millisecondsSincePost > TimeUnit.MINUTES.toMillis(1L)) {
            int timePast = (int) TimeUnit.MILLISECONDS.toMinutes(millisecondsSincePost);

            return String.format("%d %s Ago", timePast, English.plural("Minute", timePast));
        }

        // Send seconds

        return String.format("%s Seconds Ago", TimeUnit.MILLISECONDS.toSeconds(millisecondsSincePost));

    }

    public String getDate(Date formatterDate) {
        if (formatterDate == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat(DATE);
        return sdf.format(formatterDate);
    }

    public String getTime(Date formatterDate) {
        if (formatterDate == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat(TIME);
        return sdf.format(formatterDate);
    }

    public String getFormattedDateLongForm(Date formatterDate) {
        if (formatterDate == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_LONG_FORM);
        return sdf.format(formatterDate);
    }

    public String getFormattedDateLongForm(Date formatterDate, TimeZone tz) {
        if (formatterDate == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_LONG_FORM);
        sdf.setTimeZone(tz);
        return sdf.format(formatterDate);
    }

    public String getFormattedDateShortForm(Date formatterDate, TimeZone tz) {
        if (formatterDate == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_SHORT_FORM);
        sdf.setTimeZone(tz);
        return sdf.format(formatterDate);
    }

    public String getFormattedDateShortForm(Date formatterDate) {
        if (formatterDate == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_SHORT_FORM);
        return sdf.format(formatterDate);
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_LONG_FORM);
        return sdf.format(date);
    }

    public String stripQuotesAndLineFeeds(String text) {

        return text.replaceAll("\"", "&quot;")
                .replaceAll("'", "&#39;")
                .replaceAll("[\\r\\n]", "");
    }

    public String stripLineFeeds(String text) {

        return text.replaceAll("[\\r\\n]", "");
    }

    public String getBasePhoneNumber(String phone) {       
        if(StringUtils.isBlank(phone)) {
            return null;        
        }
        return phone.substring(1);
    }
    
    public static String getPhoneFormatted(String phoneNumber) {
        
        String prefix = (String) phoneNumber.subSequence(0, 1);
        String areaCode = (String) phoneNumber.subSequence(1, 4);
        String exchange = (String) phoneNumber.subSequence(4, 7);
        String lastfour = (String) phoneNumber.subSequence(7, 11);
        
        return "(" + areaCode + ") " + exchange + "-" + lastfour;
    }
}
