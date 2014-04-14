//@author A0101252A
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/*
 * This class handles all date and time related formatting and 
 * any complicated comparisons between dates and time.
 */
public class DateTimeConfiguration {
	/* Format of dates */
	private static final String DISPLAY_DATE_FORMAT = "dd-MMM-yyyy <EEE>";
	private static final String SYSTEM_DATE_FORMAT = "ddMMyy";
	
	/* SimpleDateFormat instances used to format date */
	private SimpleDateFormat systemDateFormat;
	private SimpleDateFormat displayDateFormat;
	
	/* Constructor */
	public DateTimeConfiguration() {
		systemDateFormat = new SimpleDateFormat(SYSTEM_DATE_FORMAT);
		displayDateFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
	}
	
	/*
	 * @return	Today's date in DDMMYY format as stored in system
	 */
	public String getTodaysDate() {
		Calendar date = Calendar.getInstance();
		return systemDateFormat.format(date.getTime());
	}

	/*
	 * @param	time	Time in HHMM format as stored in system
	 * @return			time in HH:MM format
	 */
	public String getTimeForDisplay(String time) {
		return time.substring(0,2) + ':' + time.substring(2);
	}
	
	/*
	 * @param 	dateToFormat	Date in DDMMYY format as stored in system
	 * @return 					date in DD-MMM-YYYY format for display
	 */
	public String getDateForDisplay(String dateToFormat) {
		Date date = null;
		try {
			date = systemDateFormat.parse(dateToFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return displayDateFormat.format(date);
	}
	
	/*
	 * Checks if taskDate is in the past or today.
	 */
	public boolean isPastOrToday(String taskDate, String todaysDate) {
		long differenceInDays = getDifferenceInDays(taskDate, todaysDate);
		
		if (differenceInDays <= 0) {
			return true;
		}
		return false;
	}
	
	/*
	 * Checks if taskDate is within 7 days from today and not today.
	 * (exclusive of 7th day)
	 */
	public boolean isThisWeekButNotToday(String taskDate, String todaysDate) {
		long differenceInDays = getDifferenceInDays(taskDate, todaysDate);
		
		if (differenceInDays > 0 && differenceInDays < 7) {
			return true;
		}
		return false;
	}
	
	/*
	 * Checks if taskDate is 7 or more days after today's date.
	 */
	public boolean isAfterAWeek(String taskDate, String todaysDate) {
		long differenceInDays = getDifferenceInDays(taskDate, todaysDate);
		
		if (differenceInDays >= 7) {
			return true;
		}
		return false;
	}
	
	/*
	 * Gets the difference in days between taskD and todaysD.
	 */
	private long getDifferenceInDays(String taskD, String todaysD) {
		Date taskDate = null;
		Date todaysDate = null;
		try {
			taskDate = systemDateFormat.parse(taskD);
			todaysDate = systemDateFormat.parse(todaysD);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		long differenceInMilliseconds = taskDate.getTime() - todaysDate.getTime();
		long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds); 
		
		return differenceInDays;
	}

	/* 
	 * Reverse specified date String from DDMMYY to YYMMDD format to directly compare values  
	 * using pre-defined compareTo function in Java String class.
	 */
	public String reverseDate(String originalDate) {
		String reversedDate = "";
		for (int i = 6; i > 0; i -= 2){
			String toAppend = originalDate.substring(i-2, i);
			reversedDate += toAppend;
		}
		return reversedDate;
	}
	
}
