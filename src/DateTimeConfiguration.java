import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Si Rui
 *
 */
public class DateTimeConfiguration {
	
	private SimpleDateFormat systemDateFormat;
	private SimpleDateFormat displayDateFormat;
	
	/**
	 * 
	 */
	public DateTimeConfiguration() {
		systemDateFormat = new SimpleDateFormat("ddMMyy");
		displayDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	}
	
	public String getTodaysDate() {
		Calendar date = Calendar.getInstance();
		return systemDateFormat.format(date.getTime());
	}

	public String getDateForDisplay(String dateToFormat) {
		Date date = null;
		try {
			date = systemDateFormat.parse(dateToFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return displayDateFormat.format(date);
	}
	
	public boolean isAfterAWeek(String taskDate, String todaysDate) {
		long differenceInDays = getDifferenceInDays(taskDate, todaysDate);
		
		if (differenceInDays >= 7) {
			return true;
		}
		return false;
	}

	public boolean isThisWeekButNotToday(String taskDate, String todaysDate) {
		long differenceInDays = getDifferenceInDays(taskDate, todaysDate);
		
		if (differenceInDays > 0 && differenceInDays < 7) {
			return true;
		}
		return false;
	}
	
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
	 * @author Si Rui
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

	public String getTimeForDisplay(String time) {
		return time.substring(0,2) + ':' + time.substring(2);
	}
}
