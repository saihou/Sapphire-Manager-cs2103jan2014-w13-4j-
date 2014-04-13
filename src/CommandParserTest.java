//@author Cai Di
import static org.junit.Assert.*;

import org.junit.Test;

public class CommandParserTest {
	private static final int INDICATOR_TASK_NAME = 0;
	private static final int INDICATOR_START_TIME = 1;
	private static final int INDICATOR_END_TIME = 2;
	private static final int INDICATOR_DATE = 3;
	private static final int INDICATOR_LOCATION = 5;
	
	private static final String ERROR_COMMAND_FROM_AND_AT_ARE_MUTUALLY_EXCLUSIVE = "ERROR: Command /from and /at are mutually exclusive.";
	private static final String ERROR_INPUT_DURATION_IS_NOT_VALID = "ERROR: Input duration is not valid.";
	private static final String ERROR_INPUT_DATE_IS_NOT_VALID = "ERROR: Input date is not valid.";
	private static final String ERROR_INPUT_STATUS_IS_NOT_VALID = "ERROR: Input status is not valid.";
	private static final String ERROR_COMMAND_KEYWORD_IS_MISSING = "ERROR: Command keyword is missing.";
	private static final String ERROR_INPUT_COMMAND_IS_NOT_VALID = "ERROR: Input Command is not valid.";
	
	@Test
	public void test() {
		CommandParser parser = new CommandParser();
		userInputWithDurationAndDate(parser);
		userInputWithDate(parser);
		userInputWithDeadlineAndDate(parser);
		userInputWithDateAndLocation(parser);
		userInputWithDurationAndDeadline(parser);
		userInputWithInvalidCommand(parser);
	}
	
	private void userInputWithDurationAndDate(CommandParser parser){
		parser.extractTaskDetailsForAdd("task /from 1010 to 2020 /on 120314");
		assertEquals("task name", "task", parser.taskDetails[INDICATOR_TASK_NAME]);
		assertEquals("starting time", "1010", parser.taskDetails[INDICATOR_START_TIME]);
		assertEquals("ending time", "2020", parser.taskDetails[INDICATOR_END_TIME]);
		assertEquals("date", "120314", parser.taskDetails[INDICATOR_DATE]);
		assertEquals("Error Message", null, parser.invalidFeedBack);
	}
	
	private void userInputWithDate(CommandParser parser){
		parser.extractTaskDetailsForAdd("task /on 120314");
		assertEquals("task name", "task", parser.taskDetails[INDICATOR_TASK_NAME]);
		assertEquals("date", "120314", parser.taskDetails[INDICATOR_DATE]);
		assertEquals("Error Message", null, parser.invalidFeedBack);
	}
	
	private void userInputWithDeadlineAndDate(CommandParser parser){
		parser.extractTaskDetailsForAdd("task /at 1010 /on 120314");
		assertEquals("task name", "task", parser.taskDetails[INDICATOR_TASK_NAME]);
		assertEquals("starting time", "1010", parser.taskDetails[INDICATOR_START_TIME]);
		assertEquals("date", "120314", parser.taskDetails[INDICATOR_DATE]);
		assertEquals("Error Message", null, parser.invalidFeedBack);
	}
	
	private void userInputWithDateAndLocation(CommandParser parser){
		parser.extractTaskDetailsForAdd("task1 /on 120314 /loc soc");
		assertEquals("task name", "task1", parser.taskDetails[INDICATOR_TASK_NAME]);
		assertEquals("date", "120314", parser.taskDetails[INDICATOR_DATE]);
		assertEquals("location", "soc", parser.taskDetails[INDICATOR_LOCATION]);
		assertEquals("Error Message", null, parser.invalidFeedBack);
	}
	
	private void userInputWithDurationAndDeadline(CommandParser parser){
		parser.extractTaskDetailsForAdd("task /from 1010 to 2020 /at 1010");
		assertEquals("Error Message", ERROR_COMMAND_FROM_AND_AT_ARE_MUTUALLY_EXCLUSIVE, parser.invalidFeedBack);
	}
	
	private void userInputWithInvalidCommand(CommandParser parser){
		//invalid duration
		parser.extractTaskDetailsForAdd("task /from 12345 to 123456 /on 120314");
		assertEquals("Error Message", ERROR_INPUT_DURATION_IS_NOT_VALID, parser.invalidFeedBack);
        //invalid date
		parser.extractTaskDetailsForAdd("task /on 1234567");
		assertEquals("Error Message", ERROR_INPUT_DATE_IS_NOT_VALID, parser.invalidFeedBack);
		
		//invalid status
		parser.extractTaskDetailsForAdd("task /mark lalala");
		assertEquals("Error Message", ERROR_INPUT_STATUS_IS_NOT_VALID, parser.invalidFeedBack);

		//command missing
		parser.extractTaskDetailsForAdd("task ////");
		assertEquals("Error Message", ERROR_COMMAND_KEYWORD_IS_MISSING, parser.invalidFeedBack);
        
		//invalid command
		parser.extractTaskDetailsForAdd("task /abc");
		assertEquals("Error Message", ERROR_INPUT_COMMAND_IS_NOT_VALID, parser.invalidFeedBack);
	}
	
	

}
