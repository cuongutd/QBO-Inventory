package com.tb2g.qboinventory.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
    private static final SimpleDateFormat ISO_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
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
    public static final Date formatStringToTimestamp(String sTimeStamp) throws Exception{
    	return ISO_DATETIME_FORMAT.parse(sTimeStamp);
    }

    public static final Date formatStringToDate(String sDate) throws Exception{
    	return ISO_DATE_FORMAT.parse(sDate);
    }

}
