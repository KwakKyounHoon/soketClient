package com.obigo.tmutestapp;

/**
 * Created by obigo on 10/11/16.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class Utils {

    static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
    static HashMap<String, Double> mCelsiusTable;

    public static String convertTimeToString(Date date) {
        return sdf.format(date);
    }

    public static Date convertStringToTime(String text) throws ParseException {
        return sdf.parse(text);
    }

    public static String getCodeToTemp(double temp){
        Iterator<String> iterator = mCelsiusTable.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if(mCelsiusTable.get(key) == temp){
                return key;
            }
        }
        return "0";
    }

    public static double getTempToCode(String code) {
        if(code.equals("0")){
            return 0.0;
        }else{
            return mCelsiusTable.get(code);
        }
    }

    public static void makeCelsiusTable() {
        mCelsiusTable = new HashMap<>();
        mCelsiusTable.put("01H", new Double(16.5));
        mCelsiusTable.put("02H", new Double(17));
        mCelsiusTable.put("03H", new Double(17.5));
        mCelsiusTable.put("04H", new Double(18));
        mCelsiusTable.put("05H", new Double(18.5));
        mCelsiusTable.put("06H", new Double(19));
        mCelsiusTable.put("07H", new Double(19.5));
        mCelsiusTable.put("08H", new Double(20));
        mCelsiusTable.put("09H", new Double(20.5));
        mCelsiusTable.put("0AH", new Double(21));
        mCelsiusTable.put("0BH", new Double(21.5));
        mCelsiusTable.put("0CH", new Double(22));
        mCelsiusTable.put("0DH", new Double(22.5));
        mCelsiusTable.put("0EH", new Double(23));
        mCelsiusTable.put("0FH", new Double(23.5));
        mCelsiusTable.put("10H", new Double(24));
        mCelsiusTable.put("11H", new Double(24.5));
        mCelsiusTable.put("12H", new Double(25));
        mCelsiusTable.put("13H", new Double(25.5));
        mCelsiusTable.put("14H", new Double(26));
        mCelsiusTable.put("15H", new Double(26.5));
        mCelsiusTable.put("16H", new Double(27));
        mCelsiusTable.put("17H", new Double(27.5));
        mCelsiusTable.put("18H", new Double(28));
        mCelsiusTable.put("19H", new Double(28.5));
        mCelsiusTable.put("1AH", new Double(29));
        mCelsiusTable.put("1BH", new Double(29.5));
        mCelsiusTable.put("1CH", new Double(30));
        mCelsiusTable.put("1DH", new Double(30.5));
        mCelsiusTable.put("1EH", new Double(31));
        mCelsiusTable.put("1FH", new Double(31.5));
        mCelsiusTable.put("20H", new Double(32));
    }

}
