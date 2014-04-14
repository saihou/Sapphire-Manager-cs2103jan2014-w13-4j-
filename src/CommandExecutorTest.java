//@author A0101252A 

/*
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

import org.junit.Test;

public class CommandExecutorTest {
	/* Date formatters */
	private static SimpleDateFormat inputDateFormat = new SimpleDateFormat("ddMMyy");
	private static SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd-MMM-yyyy <EEE>");
	
	/* Display headings */
	private static final String HEADING_MEMOS = "Memos:\n";
	private static final String HEADING_TASKS_OCCURRING_DUE_MORE_THAN_A_WEEK_LATER = "Tasks Occurring/Due More Than A Week Later:\n";
	private static final String HEADING_TASKS_OCCURRING_DUE_WITHIN_7_DAYS = "Tasks Occurring/Due Within 7 Days:\n";
	private static final String HEADING_TODAY_S_TASKS = "Today's Tasks:\n";
	private static final String HEADING_OVERDUE_TASKS = "Overdue Tasks:\n";
	
	/* Static Feedbacks */
	private static String FEEDBACK_CLEAR_ALL = "Successfully cleared all tasks.";
	
	/* Pre-defined spacing for formatting purposes */
	private static final String TASK_NUMBER_SPACING = "   ";
	private static final String DETAILS_SPACING = "      ";
	
	@Test
	public void test() {
		CommandExecutor exec = new CommandExecutor();
		
		positiveTestForDoUserOperations(exec);
	}
	
	/*
	 * A series of tests that test the CommandExecutor.doUserOperation method.
	 * They test the validity of the returned Result in particular.
	 */
	private void positiveTestForDoUserOperations(CommandExecutor exec) {
		//Chain of tests testing basic functions: clear, add, display, 
		// edit, delete, undo delete, undo edit and undo add
		assertDoClearOperation(exec);
		assertDoAddOverdueOperation(exec);
		assertDoDefaultDisplayOperation(exec);
		assertDoEditOperation(exec);
		assertDoDeleteOperation(exec);
		assertDoUndoDeleteOperation(exec);
		assertDoUndoEditOperation(exec);
		assertDoUndoAddOperation(exec);
		
		//Clear any tasks just in case
		assertDoClearOperation(exec);
		
		//Ensure Result is valid when more than 1 task exists, test includes
		//adding different tasks that fall under all display headings
		assertDoAddOperationsForAllHeadings(exec);
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
		headings.offer(HEADING_OVERDUE_TASKS);
		
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
		headings.offer(HEADING_OVERDUE_TASKS);
		
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
		headings.offer(HEADING_OVERDUE_TASKS);
		
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
		headings.offer(HEADING_OVERDUE_TASKS);
		
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
		headings.offer(HEADING_OVERDUE_TASKS);
		
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
		assertDoAddMemoOperation(exec);
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
	
	private void addMemosBodySegment(ArrayDeque<Queue<String>> bodyAll) {
		Queue<String> bodySegment = new ArrayDeque<String>();
		bodySegment.offer(TASK_NUMBER_SPACING + "1. A memo\n");
		bodySegment.offer(TASK_NUMBER_SPACING + "2. A second memo\n");
		bodyAll.offer(bodySegment);
	}
	
	private void assertDoAddTodayOperation(CommandExecutor exec) {	
		Calendar todaysDate = Calendar.getInstance();
		
		String feedback = "Successfully added \"A task for today\".";
		
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		headings.offer(HEADING_OVERDUE_TASKS);
		headings.offer(HEADING_TODAY_S_TASKS);
		
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
		headings.offer(HEADING_OVERDUE_TASKS);
		headings.offer(HEADING_TODAY_S_TASKS);
		headings.offer(HEADING_TASKS_OCCURRING_DUE_WITHIN_7_DAYS);
		
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
		headings.offer(HEADING_OVERDUE_TASKS);
		headings.offer(HEADING_TODAY_S_TASKS);
		headings.offer(HEADING_TASKS_OCCURRING_DUE_WITHIN_7_DAYS);
		headings.offer(HEADING_TASKS_OCCURRING_DUE_MORE_THAN_A_WEEK_LATER);
		
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
	
	private void assertDoAddMemoOperation(CommandExecutor exec) {
		Calendar todaysDate = Calendar.getInstance();
		
		String feedback = "Successfully added \"A second memo\".";
		
		ArrayDeque<String> headings = new ArrayDeque<String>(); 
		headings.offer(HEADING_OVERDUE_TASKS);
		headings.offer(HEADING_TODAY_S_TASKS);
		headings.offer(HEADING_TASKS_OCCURRING_DUE_WITHIN_7_DAYS);
		headings.offer(HEADING_TASKS_OCCURRING_DUE_MORE_THAN_A_WEEK_LATER);
		headings.offer(HEADING_MEMOS);
		
		ArrayDeque<Queue<String>> bodyAll = new ArrayDeque<Queue<String>>();
		addOverdueBodySegment(bodyAll);
		addTodayBodySegment(bodyAll, (Calendar)todaysDate.clone());
		addThisWeekBodySegment(bodyAll, (Calendar)todaysDate.clone());
		addNextWeekBodySegment(bodyAll, (Calendar)todaysDate.clone());
		addMemosBodySegment(bodyAll);
		
		Result expectedResults = new Result(4, 1, feedback, headings, bodyAll, true);
		exec.doUserOperation("add a memo");
		Result actualResults = exec.doUserOperation("add a second memo");
		
		assertEquals("Do Add A Second Memo Operation", expectedResults, actualResults);
	}
}
