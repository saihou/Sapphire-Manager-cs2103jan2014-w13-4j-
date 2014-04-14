//@author A0101252A
import static org.junit.Assert.*;
import org.junit.Test;

/*
 * This class contains unit tests for DateTimeConfiguration class.
 */
public class DateTimeConfigurationTest {

	@Test
	public void test() {
		DateTimeConfiguration dateTimeConfig = new DateTimeConfiguration();
		
		assertDateForDisplay(dateTimeConfig);
		assertTimeForDisplay(dateTimeConfig);
		assertIsPastOrToday(dateTimeConfig);
		assertIsThisWeekButNotToday(dateTimeConfig);
		assertIsAfterAWeek(dateTimeConfig);
		assertReverseDate(dateTimeConfig);
	}
	
	private void assertDateForDisplay(DateTimeConfiguration dateTimeConfig) {
		assertEquals("Date for Display should be dd-mmm-yyyy", 
				"23-May-2014 <Fri>", dateTimeConfig.getDateForDisplay("230514"));
		assertNotEquals("False test: Date for Display different dates test", 
				"23-May-2014 <Fri>", dateTimeConfig.getDateForDisplay("121212"));
	}
	
	private void assertTimeForDisplay(DateTimeConfiguration dateTimeConfig) {
		assertEquals("Time for Display should be hh:mm", 
				"00:00", dateTimeConfig.getTimeForDisplay("0000"));
		assertNotEquals("False test: Time for Display different times test",
				"00:00", dateTimeConfig.getTimeForDisplay("1111"));
	}
	
	private void assertIsPastOrToday(DateTimeConfiguration dateTimeConfig) {
		assertEquals("Date is in the past", 
				true, dateTimeConfig.isPastOrToday("010114", "020114"));
		assertEquals("Date is same as today",
				true, dateTimeConfig.isPastOrToday("010114", "010114"));
		assertEquals("Past/Today test: Date is not in the past/today", 
				false, dateTimeConfig.isPastOrToday("020114", "010114"));
	}
	
	private void assertIsThisWeekButNotToday(DateTimeConfiguration dateTimeConfig) {
		assertEquals("Date is this week but not today", 
				true, dateTimeConfig.isThisWeekButNotToday("180414", "120414"));
		assertEquals("This week/not today test: Date is in the future",
				false, dateTimeConfig.isThisWeekButNotToday("190414", "120414"));
		assertEquals("This week/not today test: Date is in the past",
				false, dateTimeConfig.isThisWeekButNotToday("010414", "120414"));
	}
	
	private void assertIsAfterAWeek(DateTimeConfiguration dateTimeConfig) {
		assertEquals("Date is immediately after a week", 
				true, dateTimeConfig.isAfterAWeek("080114", "010114"));
		assertEquals("Is after a week test: Date is on the 7th day after today", 
				false, dateTimeConfig.isAfterAWeek("070114", "010114"));
		assertEquals("Is after a week test: Date is in the past", 
				false, dateTimeConfig.isAfterAWeek("010114", "050414"));
	}
	
	private void assertReverseDate(DateTimeConfiguration dateTimeConfig) {
		assertEquals("Reverse Date: symmetrical test", 
				"140514", dateTimeConfig.reverseDate("140514"));
		assertEquals("Reverse Date: normal test", 
				"140311", dateTimeConfig.reverseDate("110314"));
	}

}
