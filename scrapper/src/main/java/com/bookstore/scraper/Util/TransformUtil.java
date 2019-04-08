package com.bookstore.scraper.Util;

import com.bookstore.scraper.Enum.ResultEnum;
import com.bookstore.scraper.Exception.ScrapException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformUtil {
    public static Double extractDouble(String data) throws ScrapException{
        String regex = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
        Matcher matcher = Pattern.compile( regex ).matcher(data);
        if(matcher.find()){
            return Double.parseDouble(matcher.group());
        }else{
            throw new ScrapException(ResultEnum.Invalid_Double);
        }

    }

    public static Integer extractInteger(String data) throws ScrapException{
        String regex = "[0-9]+";
        Matcher matcher = Pattern.compile( regex ).matcher(data);
        if(matcher.find()){
            return Integer.parseInt(matcher.group());
        }else{
            throw new ScrapException(ResultEnum.Invalid_Double);
        }
    }

    public static String removeStr(String key, String data){
        data = data.replace(key, "");
        data = StringUtils.strip(data);
        return data;
    }

    public static String extractDate(String data) throws ScrapException{
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        Date date;
        try{
            date = formatter.parse(data);
        }catch(Exception e){
            e.printStackTrace();
            throw new ScrapException(ResultEnum.Date_Invalid);
        }

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String extractUrl(String rawUrl){

        JsonObject obj = new JsonParser().parse(rawUrl).getAsJsonObject();
        String url ="";
        int maxResolution = 0;
        for(Map.Entry<String, JsonElement> entry : obj.entrySet()){
            JsonArray arr = entry.getValue().getAsJsonArray();
            int resolution = arr.get(0).getAsInt() + arr.get(1).getAsInt();
            if(resolution > maxResolution){
                maxResolution = resolution;
                url = entry.getKey();
            }
        }
        return url;
    }

    public static String extractDateFromPublisher(String publisher) throws ScrapException{
        String regex = "\\(.*\\)";
        Matcher matcher = Pattern.compile( regex ).matcher(publisher);
        if(matcher.find()){
            String result = matcher.group();
            return extractDate(result.substring(1, result.length()-1));
        }else{
            throw new ScrapException(ResultEnum.Invalid_Double);
        }
    }
}
