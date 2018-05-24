package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {


    public static String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        return date;
    }
}
