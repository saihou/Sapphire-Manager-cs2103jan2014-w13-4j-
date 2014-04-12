import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Cai Di
 *
 */
public class CommandParserTest {

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
		assertEquals("task name", "task", parser.taskDetails[0]);
		assertEquals("starting time", "1010", parser.taskDetails[1]);
		assertEquals("ending time", "2020", parser.taskDetails[2]);
		assertEquals("date", "120314", parser.taskDetails[3]);
		assertEquals("Error Message", null, parser.invalidFeedBack);
	}
	
	private void userInputWithDate(CommandParser parser){
		parser.extractTaskDetailsForAdd("task /on 120314");
		assertEquals("task name", "task", parser.taskDetails[0]);
		assertEquals("date", "120314", parser.taskDetails[3]);
		assertEquals("Error Message", null, parser.invalidFeedBack);
	}
	
	private void userInputWithDeadlineAndDate(CommandParser parser){
		parser.extractTaskDetailsForAdd("task /at 1010 /on 120314");
		assertEquals("task name", "task", parser.taskDetails[0]);
		assertEquals("starting time", "1010", parser.taskDetails[1]);
		assertEquals("date", "120314", parser.taskDetails[3]);
		assertEquals("Error Message", null, parser.invalidFeedBack);
	}
	
	private void userInputWithDateAndLocation(CommandParser parser){
		parser.extractTaskDetailsForAdd("task1 /on 120314 /loc soc");
		assertEquals("task name", "task1", parser.taskDetails[0]);
		assertEquals("date", "120314", parser.taskDetails[3]);
		assertEquals("location", "soc", parser.taskDetails[5]);
		assertEquals("Error Message", null, parser.invalidFeedBack);
	}
	
	private void userInputWithDurationAndDeadline(CommandParser parser){
		parser.extractTaskDetailsForAdd("task /from 1010 to 2020 /at 1010");
		assertEquals("Error Message", "ERROR: Command /from and /at are mutually exclusive.", parser.invalidFeedBack);
	}
	
	private void userInputWithInvalidCommand(CommandParser parser){
		//invalid duration
		parser.extractTaskDetailsForAdd("task /from 12345 to 123456 /on 120314");
		assertEquals("Error Message", "ERROR: Input duration is not valid.", parser.invalidFeedBack);
        //invalid date
		parser.extractTaskDetailsForAdd("task /on 1234567");
		assertEquals("Error Message", "ERROR: Input date is not valid.", parser.invalidFeedBack);
		
		//invalid status
		parser.extractTaskDetailsForAdd("task /mark lalala");
		assertEquals("Error Message", "ERROR: Input status is not valid.", parser.invalidFeedBack);

		//command missing
		parser.extractTaskDetailsForAdd("task ////");
		assertEquals("Error Message", "ERROR: Command keyword is missing.", parser.invalidFeedBack);
        
		//invalid command
		parser.extractTaskDetailsForAdd("task /abc");
		assertEquals("Error Message", "ERROR: Input Command is not valid.", parser.invalidFeedBack);
	}
	
	

}
