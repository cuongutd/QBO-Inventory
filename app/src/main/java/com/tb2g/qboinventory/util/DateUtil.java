package com.tb2g.qboinventory.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private static final SimpleDateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("MM-dd-yy hh:mm a");

    private static final SimpleDateFormat ISO_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static final String getCurrentDateAsString(){
		return ISO_DATE_FORMAT.format(new Date());
	}

	public static final String getCurrentTimestampAsString(){
		return ISO_DATETIME_FORMAT.format(new Date());
	}

	public static final String formatDateToString(Date date){
		if (date != null)
			return ISO_DATE_FORMAT.format(date);
		else
			return null;
	}

	public static final String formatTimestampAsString(Date date){
		if (date != null)
			return ISO_DATETIME_FORMAT.format(date);
		else return null;
	}
    public static final Date formatStringToTimestamp(String sTimeStamp){
    	try {
            return ISO_DATETIME_FORMAT.parse(sTimeStamp);
        }catch(Exception e){
            return null;
        }
    }


	public static final String getReadableDate(Date d){
		return READABLE_DATE_FORMAT.format(d);
	}

    public static final Date formatStringToDate(String sDate) throws Exception{
    	return ISO_DATE_FORMAT.parse(sDate);
    }

}
