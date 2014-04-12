/**
 * @author Si Rui 
 * @author Sai Hou
 *
 *	This class contains the jUnit tests for CommandExecutor and ActionHistory Class. It checks if:
 *	1. The Add (all Types of Tasks), Display, Edit, Delete, Undo and Clear are working correctly.
 *	2. The action history has been updated correctly after each action (if applicable).
 * 	
 */

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Queue;
import java.util.ArrayList;







import org.junit.Test;

public class CommandExecutorTest {

	SimpleDateFormat inputDateFormat = new SimpleDateFormat("ddMMyy");
	SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd-MMM-yyyy <EEE>");
	
	private String FEEDBACK_CLEAR_ALL = "Successfully cleared all tasks.";
	private String FEEDBACK_CLEAR_DONE = "Successfully cleared all done tasks.";
	
	private String FEEDBACK_INVALID_KEYWORDS = "ERROR: Invalid keyword(s) entered!";
	
	private final static String TASK_NUMBER_SPACING = "   ";
	private final static String DETAILS_SPACING = "      ";
	
	@Test
	public void test() {
		CommandExecutor exec = new CommandExecutor();
		
		//unitTestForIndividualCommands();

		positiveTestForDoUserOperations(exec);
	}
	
	public void unitTestForIndividualCommands() {
		testClearCommand();
		testAddCommand();
		testEditCommand();
		//testDeleteCommand();
		//testDisplayCommand();
		//testSearchCommand();
	}
	
	private void testClearCommand() {
		assertClearAll();
		assertClearDone();
		assertClearInvalid();
	}
	
	private void testAddCommand() {
		assertAddMemo();
		assertAddFullday();
		assertAddDeadline();
		assertAddDuration();
		assertAddInvalid();
	}
	
	private void testEditCommand() {
		assertEditName();
		assertEditLoc();
		assertEditDate();
		assertEditDeadline();
		assertEditDuration();
		assertEditMark();
		assertEditRemoveTime();
		assertEditRemoveLoc();
		assertEditRemoveDate();
		assertEditCombination();
		assertEditInvalid();
	}

	private Command initEdit() {
		return new EditCommand();
	}
	
	private void assertEditName() {
		//Command edit = initEdit();
		Command edit = new EditCommand();
		edit.execute("1 a new name");
		String taskname = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals("Successfully made changes to \""+ taskname + "\".", r.getSystemFeedback());
	}
	
	private void assertEditLoc() {
		Command edit = initEdit();
		edit.execute("1 /loc new location");
		String taskname = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals("Successfully made changes to \""+ taskname + "\".", r.getSystemFeedback());
	}
	
	private void assertEditDate() {
		Command edit = initEdit();
		edit.execute("1 /on 100114");
		String taskname = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals("Successfully made changes to \""+ taskname + "\".", r.getSystemFeedback());
	}
	
	private void assertEditDeadline() {
		Command edit = initEdit();
		edit.execute("1 /on 100114 /at 1000");
		String taskname = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals("Successfully made changes to \""+ taskname + "\".", r.getSystemFeedback());
	}
	
	private void assertEditDuration() {
		Command edit = initEdit();
		edit.execute("1 /on 100114 /from 1000 to 1001");
		String taskname = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals("Successfully made changes to \""+ taskname + "\".", r.getSystemFeedback());
	}
	
	private void assertEditMark() {
		Command edit = initEdit();
		edit.execute("3 /mark done");
		String taskname = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals("Successfully made changes to \""+ taskname + "\".", r.getSystemFeedback());
	
		edit.execute("5 /mark undone");
		taskname = edit.getEditedTask().getName();
		r = edit.getResult();
		assertEquals("Successfully made changes to \""+ taskname + "\".", r.getSystemFeedback());
	}
	
	private void assertEditRemoveTime() {
		Command edit = initEdit();
		edit.execute("4 Fullday2 /rm time");
		String taskname = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals("Successfully made changes to \""+ taskname + "\".", r.getSystemFeedback());
	}
	
	private void assertEditRemoveLoc() {
		Command edit = initEdit();
		edit.execute("2 /rm loc");
		String taskname = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals("Successfully made changes to \""+ taskname + "\".", r.getSystemFeedback());
	}
	
	private void assertEditRemoveDate() {
		Command edit = initEdit();
		edit.execute("5 /rm date");
		String taskname = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals("Successfully made changes to \""+ taskname + "\".", r.getSystemFeedback());
	}
	
	private void assertEditCombination() {
		Command edit = new EditCommand();
		
		//add loc remove date
		edit.execute("4 /loc /rm date");
		String taskname = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals("ERROR: Location must start with a letter.", r.getSystemFeedback());
		
	}
	
	private void assertEditInvalid() {
		Command edit = initEdit();
		Result r;
		
		//empty string e.g. "     "
		edit.execute("     ");
		r = edit.getResult();
		assertEquals("ERROR: Invalid task number!", r.getSystemFeedback());
		
		//got choice but rest is empty string e.g. "   "
		edit.execute("1     ");
		r = edit.getResult();
		assertEquals("ERROR: Empty input!", r.getSystemFeedback());
		
		//empty keyword
		edit.execute("1   /  ");
		r = edit.getResult();
		assertEquals("ERROR: Command keyword is missing.", r.getSystemFeedback());
		
		//invalid keyword
		edit.execute("1   /abc");
		r = edit.getResult();
		assertEquals("ERROR: Input Command is not valid.", r.getSystemFeedback());
		
		//spam special characters
		edit.execute("1   //////");
		r = edit.getResult();
		assertEquals("ERROR: Command keyword is missing.", r.getSystemFeedback());
		
		//invalid task number
		edit.execute("0   ");
		r = edit.getResult();
		assertEquals("ERROR: Invalid task number!", r.getSystemFeedback());
		
		//invalid date
		edit.execute("1   /on abc");
		r = edit.getResult();
		assertEquals("ERROR: Input date is not valid.", r.getSystemFeedback());
		
		//invalid deadline
		edit.execute("1 /on 101015 /at com1");
		r = edit.getResult();
		assertEquals("ERROR: Input deadline time is not valid.", r.getSystemFeedback());
		
		//invalid duration
		edit.execute("1 /on 101015 /from 1000 to 2360");
		r = edit.getResult();
		assertEquals("ERROR: Input duration is not valid.", r.getSystemFeedback());
		
		//got start time but no end time
		edit.execute("1 /on 101015 /from 1000");
		r = edit.getResult();
		assertEquals("ERROR: Input starting time without ending time.", r.getSystemFeedback());
		
		//got end time but no start time
		edit.execute("1 /on 101015 to 1000");
		r = edit.getResult();
		assertEquals("ERROR: Input ending time without starting time.", r.getSystemFeedback());
		
		//same start and end time
		edit.execute("1 /on 101015 /from 1000 to 1000");
		r = edit.getResult();
		assertEquals("ERROR: Input duration is not valid.", r.getSystemFeedback());
		
		//5 digits for time
		edit.execute("1 /on 101015 /from 10000 to 00000");
		r = edit.getResult();
		assertEquals("ERROR: Input duration is not valid.", r.getSystemFeedback());
		
		//7 digits for date
		edit.execute("1 /on 1010151");
		r = edit.getResult();
		assertEquals("ERROR: Input date is not valid.", r.getSystemFeedback());
		
		//both /from and /at
		edit.execute("1 /from 1010 to 1511 /by 2359");
		r = edit.getResult();
		assertEquals("ERROR: Command /from and /at are mutually exclusive.", r.getSystemFeedback());
	
		//remove date from a memo
		edit.execute("1 /rm date");
		r = edit.getResult();
		assertEquals("ERROR: Cannot remove something not present!", r.getSystemFeedback());
		
		//remove time from a fullday task
		edit.execute("4 /rm time");
		r = edit.getResult();
		assertEquals("ERROR: Cannot remove something not present!", r.getSystemFeedback());
		
		//invalid removal keyword
		edit.execute("4 /rm asde");
		r = edit.getResult();
		assertEquals("ERROR: Invalid keyword(s)!", r.getSystemFeedback());
		
		//remove date from a deadline/duration task
		edit.execute("3 /rm date");
		r = edit.getResult();
		assertEquals("ERROR: Cannot remove date without removing time!", r.getSystemFeedback());
		
		//empty removal keyword
		edit.execute("3 /rm    ");
		r = edit.getResult();
		assertEquals("ERROR: Nothing to remove!", r.getSystemFeedback());
		
		//string 2 keywords together
		edit.execute("4 /loc /rm date");
		r = edit.getResult();
		assertEquals("ERROR: Location must start with a letter.", r.getSystemFeedback());
	}
	
	private void assertAddMemo() {
		Command add = new AddCommand();
		add.execute("memo");
		Result r = add.getResult();
		assertEquals("add memo", "Successfully added \"Memo\".", r.getSystemFeedback());
	}
	
	private void assertAddFullday() {
		Command add = new AddCommand();
		add.execute("fullday /on 040414");
		Result r = add.getResult();
		assertEquals("add fullday", "Successfully added \"Fullday\".", r.getSystemFeedback());
	}
	
	private void assertAddDeadline() {
		Command add = new AddCommand();
		Result r;
		
		//use "at" keyword
		add.execute("deadline /on 110414 /at 1000");
		r = add.getResult();
		assertEquals("add deadline", "Successfully added \"Deadline\".", r.getSystemFeedback());
		
		//use "by" keyword
		add.execute("deadline2 /on 160414 /by 1300");
		r = add.getResult();
		assertEquals("add deadline", "Successfully added \"Deadline2\".", r.getSystemFeedback());
	}
	
	private void assertAddDuration() {
		Command add = new AddCommand();
		add.execute("duration /on 040614 /from 1000 to 1200");
		Result r = add.getResult();
		assertEquals("add duration", "Successfully added \"Duration\".", r.getSystemFeedback());
	}
	
	private void assertAddInvalid() {
		Command add = new AddCommand();
		Result r;
		
		//empty string e.g. "     "
		add.execute("   ");
		r = add.getResult();
		assertEquals("user never enter task name", 
				"ERROR: Empty input!", r.getSystemFeedback());
		
		//empty keyword
		add.execute("empty keyword /      ");
		r = add.getResult();
		assertEquals("user gives empty keyword", "ERROR: Command keyword is missing.", r.getSystemFeedback());
		
		//empty task name
		add.execute("/");
		r = add.getResult();
		assertEquals("user gives empty taskname", "ERROR: No task name.", r.getSystemFeedback());
		
		//invalid keyword
		add.execute("invalid keyword /something");
		r = add.getResult();
		assertEquals("user gives invalid keyword", "ERROR: Input Command is not valid.", r.getSystemFeedback());
		
		//spam special characters
		add.execute("spam special characters keyword ///////");
		r = add.getResult();
		assertEquals("user gives empty keyword", "ERROR: Command keyword is missing.", r.getSystemFeedback());

		//deadine without date
		add.execute("time without date /at 0010");
		r = add.getResult();
		assertEquals("user enters deadline without date", 
				"ERROR: Entering a timestamp without a date doesn't make sense!", r.getSystemFeedback());
		
		//duration without date
		add.execute("time without date /from 0010 to 0011");
		r = add.getResult();
		assertEquals("user enters duration without date", 
				"ERROR: Entering a timestamp without a date doesn't make sense!", r.getSystemFeedback());
	
		//invalid duration
		add.execute("invalid time /on 040414 /from abcd to -7889");
		r = add.getResult();
		assertEquals("user enters invalid duration", 
				"ERROR: Input duration is not valid.", r.getSystemFeedback());
		
		//invalid deadine
		add.execute("invalid time /on 040414 /at -656");
		r = add.getResult();
		assertEquals("user enters invalid deadline", 
				"ERROR: Input deadline time is not valid.", r.getSystemFeedback());
	
		//got start time but no end time
		add.execute("invalid time /on 040414 /from 1009");
		r = add.getResult();
		assertEquals("user enters start time but no end time", 
				"ERROR: Input starting time without ending time.", r.getSystemFeedback());
		
		//got end time but no start time
		add.execute("invalid time /on 040414 to 2359");
		r = add.getResult();
		assertEquals("user enters end time but no start time", 
				"ERROR: Input ending time without starting time.", r.getSystemFeedback());
		
		//same start and end time
		add.execute("invalid time /on 040414 /from 2359 to 2359");
		r = add.getResult();
		assertEquals("user enters same start time and end time", 
				"ERROR: Input duration is not valid.", r.getSystemFeedback());
		
		//5 digits for time
		add.execute("5 digits time /on 040414 /by 00000");
		r = add.getResult();
		assertEquals("user enters 5 digits for time", 
				"ERROR: Input deadline time is not valid.", r.getSystemFeedback());
		
		//7 digits for date
		add.execute("7 digits date /on 0104151 /by 0000");
		r = add.getResult();
		assertEquals("user enters 7 digits for date", 
				"ERROR: Input date is not valid.", r.getSystemFeedback());
		
		//extra spaces between date
		add.execute("space btw date /on 10 1 1 1 1 /by 0000");
		r = add.getResult();
		assertEquals("user enters spaces between date", 
				"ERROR: Input date is not valid.", r.getSystemFeedback());
		
		//extra spaces between time
		add.execute("space btw time /on 101111 /by 0 0 0 0");
		r = add.getResult();
		assertEquals("user enters spaces between time", 
				"ERROR: Input deadline time is not valid.", r.getSystemFeedback());
		
		//both /from and /at
		add.execute("both from and at /on 101111 /from 1000 to 1100 /at 0000");
		r = add.getResult();
		assertEquals("user enters spaces between time", 
				"ERROR: Command /from and /at are mutually exclusive.", r.getSystemFeedback());
		
		//string 2 keywords together
		add.execute("2 keywords together /loc /rm date");
		r = add.getResult();
		assertEquals("ERROR: Location must start with a letter.", r.getSystemFeedback());
	}
	
	private void assertClearAll() {
		clearAll();
	}

	private void clearAll() {
		Command clear = new ClearCommand();
		clear.execute("all");
		Result r = clear.getResult();
		assertEquals("Successfully cleared all tasks.", r.getSystemFeedback());
	}
	
	private void assertClearDone() {
		Command clear = new ClearCommand();
		Result r;
		
		//clear done by specifying done explicitly
		clear.execute("done");
		r = clear.getResult();
		assertEquals("clear done", 
				FEEDBACK_CLEAR_DONE, 
				r.getSystemFeedback());
		
		//clear done by giving empty string
		clear = new ClearCommand();
		clear.execute("   ");
		r = clear.getResult();
		assertEquals("clear    ", 
				FEEDBACK_CLEAR_DONE, 
				r.getSystemFeedback());
	}
	
	private void assertClearInvalid() {
		Command clear = new ClearCommand();
		clear.execute("invalid stuff here");
		Result r = clear.getResult();
		assertEquals("user writes rubbish", 
				FEEDBACK_INVALID_KEYWORDS, 
				r.getSystemFeedback());
	}
	
	
	private void positiveTestForDoUserOperations(CommandExecutor exec) {
		assertDoClearOperation(exec);
		assertDoAddOverdueOperation(exec);
		assertDoDefaultDisplayOperation(exec);
		assertDoEditOperation(exec);
		assertDoDeleteOperation(exec);
		assertDoUndoDeleteOperation(exec);
		assertDoUndoEditOperation(exec);
		assertDoUndoAddOperation(exec);
		
		//assertDoClearOperation(exec);
		//assertDoAddOperationsForAllHeadings(exec);
	}
	
	private void assertDoClearOperation(CommandExecutor exec) {
		int indexI = -1;
		int indexJ = -1;
		String feedback = FEEDBACK_CLEAR_ALL;
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		
		Result expectedResults = new Result(indexI, indexJ, feedback, headings, bodyAll, true);
		Result actualResults = exec.doUserOperation("clear all");
		
		assertEquals("Do Clear Operation", expectedResults, actualResults);
	}	
	
	private Result assertDoAddOverdueOperation(CommandExecutor exec) {
		int indexI = 0;
		int indexJ = 0;
		String feedback = "Successfully added \"An overdue task\".";
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		headings.offer("Overdue Tasks:\n");
		
		Queue<String> bodySegment = new ArrayDeque<String>();
		bodySegment.offer(TASK_NUMBER_SPACING + "1. An overdue task\n" + 
						DETAILS_SPACING + "01-Jan-2014 <Wed>\n" +
						DETAILS_SPACING + "From 12:00 to 13:00\n");
		
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		bodyAll.offer(bodySegment);
		
		Result expectedResults = new Result(indexI, indexJ, feedback, headings, bodyAll, true);
		Result actualResults = exec.doUserOperation("add an overdue task /on 010114 /from 1200 to 1300");
		
		assertEquals("Do Add Overdue Operation", expectedResults, actualResults);
		
		return expectedResults;
	}
	
	private void assertDoEditOperation(CommandExecutor exec) {
		int indexI = 0;
		int indexJ = 0;
		String feedback = "Successfully made changes to \"An overdue task\".";
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		headings.offer("Overdue Tasks:\n");
		
		Queue<String> bodySegment = new ArrayDeque<String>();
		bodySegment.offer(TASK_NUMBER_SPACING + "1. An overdue task\n" + 
						DETAILS_SPACING + "02-Jan-2014 <Thu>\n" +
						DETAILS_SPACING + "From 12:00 to 13:00\n");
		
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		bodyAll.offer(bodySegment);
		
		Result expectedResults = new Result(indexI, indexJ, feedback, headings, bodyAll, true);
		Result actualResults = exec.doUserOperation("edit 1 /on 020114 /from 1200 to 1300");
		
		assertEquals("Do Edit Operation", expectedResults, actualResults);
	}
	
	private void assertDoDeleteOperation(CommandExecutor exec) {
		int indexI = -1;
		int indexJ = -1;
		String feedback = "Successfully deleted 1. An overdue task.";

		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		
		Result expectedResults = new Result(indexI, indexJ, feedback, headings, bodyAll, true);
		Result actualResults = exec.doUserOperation("del 1");

		assertEquals("Do Delete Operation", expectedResults, actualResults);
	}
	
	private void assertDoUndoDeleteOperation(CommandExecutor exec) {
		int indexI = 0;
		int indexJ = 0;
		String feedback = "Undo previous deletion: Successfully added \"An overdue task\".";
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		headings.offer("Overdue Tasks:\n");
		
		Queue<String> bodySegment = new ArrayDeque<String>();
		bodySegment.offer(TASK_NUMBER_SPACING + "1. An overdue task\n" + 
						DETAILS_SPACING + "02-Jan-2014 <Thu>\n" +
						DETAILS_SPACING + "From 12:00 to 13:00\n");
		
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		bodyAll.offer(bodySegment);
		
		Result expectedResults = new Result(indexI, indexJ, feedback, headings, bodyAll, true);
		Result actualResults = exec.doUserOperation("undo");
		assertEquals("Do Undo Delete Operation", expectedResults, actualResults);
	}
	
	private void assertDoUndoEditOperation(CommandExecutor exec) {
		int indexI = 0;
		int indexJ = 0;
		String feedback = "Undo previous update: Successfully reverted \"An overdue task\".";
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		headings.offer("Overdue Tasks:\n");
		
		Queue<String> bodySegment = new ArrayDeque<String>();
		bodySegment.offer(TASK_NUMBER_SPACING + "1. An overdue task\n" + 
						DETAILS_SPACING + "01-Jan-2014 <Wed>\n" +
						DETAILS_SPACING + "From 12:00 to 13:00\n");
		
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		bodyAll.offer(bodySegment);
		
		Result expectedResults = new Result(indexI, indexJ, feedback, headings, bodyAll, true);
		Result actualResults = exec.doUserOperation("undo");
		assertEquals("Do Undo Edit Operation", expectedResults, actualResults);
	}
	
	private void assertDoUndoAddOperation(CommandExecutor exec) {
		int indexI = -1;
		int indexJ = -1;
		String feedback = "Undo previous addition: Successfully deleted \"An overdue task\".";
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		
		Result expectedResults = new Result(indexI, indexJ, feedback, headings, bodyAll, true);
		Result actualResults = exec.doUserOperation("undo");
		
		assertEquals("Do Undo Add Operation", expectedResults, actualResults);
	}
	
	private void assertDoDefaultDisplayOperation(CommandExecutor exec) {
		int indexI = -1;
		int indexJ = -1;
		String feedback = "Displaying uncompleted tasks.";
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		headings.offer("Overdue Tasks:\n");
		
		Queue<String> bodySegment = new ArrayDeque<String>();
		bodySegment.offer(TASK_NUMBER_SPACING + "1. An overdue task\n" + 
						DETAILS_SPACING + "01-Jan-2014 <Wed>\n" +
						DETAILS_SPACING + "From 12:00 to 13:00\n");
		
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		bodyAll.offer(bodySegment);
		
		Result expectedResults = new Result(indexI, indexJ, feedback, headings, bodyAll, true);
		Result actualResults = exec.doUserOperation("display");
		assertEquals("Do Display Operation", expectedResults, actualResults);
	}
	
	
	
	private void assertDoAddOperationsForAllHeadings(CommandExecutor exec) {
		assertDoAddOverdueOperation(exec);
		assertDoAddTodayOperation(exec);
		assertDoAddThisWeekOperation(exec);
		assertDoAddNextWeekOperation(exec);
	}
	
	private void addOverdueBodySegment(ArrayDeque<Queue<String>> bodyAll){
		Queue<String> bodySegment = new ArrayDeque<String>();
		bodySegment.offer(TASK_NUMBER_SPACING + "1. An overdue task\n" + 
						DETAILS_SPACING + "01-Jan-2014 <Wed>\n" +
						DETAILS_SPACING + "From 12:00 to 13:00\n");
		bodyAll.offer(bodySegment);
	}
	
	private void addTodayBodySegment(ArrayDeque<Queue<String>> bodyAll, Calendar date) {
		Queue<String> bodySegment = new ArrayDeque<String>();
		bodySegment.offer(TASK_NUMBER_SPACING + "2. A task for today\n" + 
						DETAILS_SPACING + displayDateFormat.format(date.getTime()) + "\n");
		bodyAll.offer(bodySegment);
	}
	
	private void addThisWeekBodySegment(ArrayDeque<Queue<String>> bodyAll, Calendar date) {
		date.add(Calendar.DATE, 6);  
		Queue<String> bodySegment = new ArrayDeque<String>();
		bodySegment.offer(TASK_NUMBER_SPACING + "3. A task for the week\n" + 
						DETAILS_SPACING + displayDateFormat.format(date.getTime()) + "\n");
		bodyAll.offer(bodySegment);
	}
	
	private void addNextWeekBodySegment(ArrayDeque<Queue<String>> bodyAll, Calendar date) {
		date.add(Calendar.DATE, 7);
		Queue<String> bodySegment = new ArrayDeque<String>();
		bodySegment.offer(TASK_NUMBER_SPACING + "4. A task for the next week\n" + 
						DETAILS_SPACING + displayDateFormat.format(date.getTime()) + "\n");
		bodyAll.offer(bodySegment);
	}
	
	private void assertDoAddTodayOperation(CommandExecutor exec) {	
		Calendar todaysDate = Calendar.getInstance();
		
		String feedback = "Successfully added \"A task for today\".";
		
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		headings.offer("Overdue Tasks:\n");
		headings.offer("Today's Tasks:\n");
		
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		addOverdueBodySegment(bodyAll);
		addTodayBodySegment(bodyAll, todaysDate);
		
		Result expectedResults = new Result(1, 0, feedback, headings, bodyAll, true);
		Result actualResults = exec.doUserOperation("add a task for today /on " + 
								inputDateFormat.format(todaysDate.getTime()));
		
		assertEquals("Do Add Today Operation", expectedResults, actualResults);
	}
	
	private void assertDoAddThisWeekOperation(CommandExecutor exec) {
		Calendar todaysDate = Calendar.getInstance();
		
		String feedback = "Successfully added \"A task for the week\".";
		
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		headings.offer("Overdue Tasks:\n");
		headings.offer("Today's Tasks:\n");
		headings.offer("Tasks Occurring/Due Within 7 Days:\n");
		
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		addOverdueBodySegment(bodyAll);
		addTodayBodySegment(bodyAll, (Calendar)todaysDate.clone());
		addThisWeekBodySegment(bodyAll, (Calendar)todaysDate.clone());
		
		Result expectedResults = new Result(2, 0, feedback, headings, bodyAll, true);
		
		todaysDate.add(Calendar.DATE, 6);  
		Result actualResults = exec.doUserOperation("add a task for the week /on " + 
								inputDateFormat.format(todaysDate.getTime()));
		
		assertEquals("Do Add Task In This Week Operation", expectedResults, actualResults);
	}
	
	private void assertDoAddNextWeekOperation(CommandExecutor exec) {		
		Calendar todaysDate = Calendar.getInstance();
		
		String feedback = "Successfully added \"A task for the next week\".";
		
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		headings.offer("Overdue Tasks:\n");
		headings.offer("Today's Tasks:\n");
		headings.offer("Tasks Occurring/Due Within 7 Days:\n");
		headings.offer("Tasks Occurring/Due More Than A Week Later:\n");
		
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		addOverdueBodySegment(bodyAll);
		addTodayBodySegment(bodyAll, (Calendar)todaysDate.clone());
		addThisWeekBodySegment(bodyAll, (Calendar)todaysDate.clone());
		addNextWeekBodySegment(bodyAll, (Calendar)todaysDate.clone());
		
		Result expectedResults = new Result(3, 0, feedback, headings, bodyAll, true);
		
		todaysDate.add(Calendar.DATE, 7); 
		Result actualResults = exec.doUserOperation("add a task for the next week /on " + 
								inputDateFormat.format(todaysDate.getTime()));
		
		assertEquals("Do Add Task In The Next Week Operation", expectedResults, actualResults);
	}
	
	
}
