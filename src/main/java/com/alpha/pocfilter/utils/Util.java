package com.alpha.pocfilter.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static Long getIdOfUrl(String url){
        Pattern pattern = Pattern.compile("\\d+$");
        Matcher matcher = pattern.matcher(url);
        if(matcher.find()) {
            String numericValue = matcher.group();
            return Long.parseLong(numericValue);
        }
        return 0L;
    }
}
