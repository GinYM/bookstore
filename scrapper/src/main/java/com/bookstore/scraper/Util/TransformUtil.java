package com.bookstore.scraper.Util;

import com.bookstore.scraper.Enum.ResultEnum;
import com.bookstore.scraper.Exception.ScrapException;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
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
}
