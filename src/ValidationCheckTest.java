import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * @author Sai Hou
 */

public class ValidationCheckTest {

	@Test
	public void test() {
		assertDate();
		assertTime();
		assertChoice();
		assertOperation();
		assertDuration();
		assertStatus();
		assertLocation();
	}
	
	private void assertLocation() {
		assertTrue("valid location", ValidationCheck.isValidLocation("Holland V"));
		assertTrue("valid location", ValidationCheck.isValidLocation("Z123"));
		assertTrue("valid location", ValidationCheck.isValidLocation("a#$#$"));
		assertFalse("invalid location", ValidationCheck.isValidLocation("/rmma"));
		assertFalse("invalid location", ValidationCheck.isValidLocation("*#@%"));
	}
	
	private void assertDuration() {
		assertTrue("random valid duration", ValidationCheck.isValidDuration("1000", "1001"));
		
		//boundary cases: duration
		assertFalse("invalid duration", ValidationCheck.isValidDuration("2300", "2200"));
		assertFalse("invalid duration", ValidationCheck.isValidDuration("2200", "2200"));
	}
	private void assertStatus() {
		assertTrue("valid status", ValidationCheck.isValidStatus("done"));
		assertTrue("valid status", ValidationCheck.isValidStatus("undone"));
		
		assertFalse("valid status", ValidationCheck.isValidStatus("HELLO"));
	}
	private void assertDate() {
		assertTrue("random date", ValidationCheck.isValidDate("260314"));
		assertTrue("random date", ValidationCheck.isValidDate("261012"));
		
		//boundary cases: leap year
		assertTrue("non-leap year, feb 28", ValidationCheck.isValidDate("280214"));
		assertFalse("non-leap year, feb 29", ValidationCheck.isValidDate("290214"));
		assertTrue("leap year, feb 28", ValidationCheck.isValidDate("280212"));
		assertTrue("leap year, feb 29", ValidationCheck.isValidDate("290212"));
		
		//boundary cases: last day of month
		assertTrue("check for 31st", ValidationCheck.isValidDate("310113"));
		assertFalse("check for 31st", ValidationCheck.isValidDate("310213"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("310313"));
		assertFalse("check for 31st", ValidationCheck.isValidDate("310413"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("310513"));
		assertFalse("check for 31st", ValidationCheck.isValidDate("310613"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("310713"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("310813"));
		assertFalse("check for 31st", ValidationCheck.isValidDate("310913"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("311013"));
		assertFalse("check for 31st", ValidationCheck.isValidDate("311113"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("311213"));
	}
	private void assertTime() {
		assertTrue("random time", ValidationCheck.isValidTime("1234"));
		
		//boundary cases: first and last value in "valid" partition
		assertTrue("max time", ValidationCheck.isValidTime("2359"));
		assertTrue("min time", ValidationCheck.isValidTime("0000"));
		
		//boundary cases: first and last value in "invalid" partition
		assertFalse("exceed max hours", ValidationCheck.isValidTime("2459"));
		assertFalse("exceed max min", ValidationCheck.isValidTime("2360"));
		assertFalse("negative", ValidationCheck.isValidTime("-120"));
		assertFalse("alphabets", ValidationCheck.isValidTime("abcd"));
	}
	private void assertChoice() {
		assertTrue("random choice", ValidationCheck.isValidChoice("1", 2));
		
		assertTrue("max possible value", ValidationCheck.isValidChoice("1", 1));
		
		//boundary cases
		assertFalse("exceed max possible value", ValidationCheck.isValidChoice("2", 1));
		assertFalse("0", ValidationCheck.isValidChoice("0", 1));
		assertFalse("negative value", ValidationCheck.isValidChoice("-2", 1));
	}
	private void assertOperation() {
		assertTrue("add", ValidationCheck.isValidOperation("add"));
		assertTrue("add", ValidationCheck.isValidOperation("new"));
		assertTrue("add", ValidationCheck.isValidOperation("create"));
		assertTrue("delete", ValidationCheck.isValidOperation("delete"));
		assertTrue("delete", ValidationCheck.isValidOperation("remove"));
		assertTrue("delete", ValidationCheck.isValidOperation("del"));
		assertTrue("display", ValidationCheck.isValidOperation("display"));
		assertTrue("delete", ValidationCheck.isValidOperation("show"));
		assertTrue("edit", ValidationCheck.isValidOperation("edit"));
		assertTrue("update", ValidationCheck.isValidOperation("update"));
		assertTrue("undo", ValidationCheck.isValidOperation("undo"));
		assertTrue("clear", ValidationCheck.isValidOperation("clear"));
		assertTrue("search", ValidationCheck.isValidOperation("search"));
		assertTrue("delete", ValidationCheck.isValidOperation("find"));
		assertTrue("delete", ValidationCheck.isValidOperation("exit"));
		assertTrue("delete", ValidationCheck.isValidOperation("quit"));
		
		assertFalse("invalid", ValidationCheck.isValidOperation("101"));
		assertFalse("invalid", ValidationCheck.isValidOperation("abc"));
	}
}
