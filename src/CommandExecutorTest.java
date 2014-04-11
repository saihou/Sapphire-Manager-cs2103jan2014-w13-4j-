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

import org.junit.Test;

public class CommandExecutorTest {

	@Test
	public void test() {
		CommandExecutor exec = new CommandExecutor();
		
		unitTestForIndividualCommands();
		/*
		assertTaskListForCommands(exec);
		assertFeedbackForCommands(exec);
		assertUpdateHistory(exec, history);
		*/
	}
	
	public void unitTestForIndividualCommands() {
		testClearCommand();
		testAddCommand();
		//testEditCommand();
		//testDeleteCommand();
	}
	
	private void testClearCommand() {
		assertClearAll();
		assertClearDone();
		assertClearInvalidKeyword();
	}
	
	private void testAddCommand() {
		assertAddMemo();
		assertAddFullday();
		assertAddDeadline();
		assertAddDuration();
		assertAddInvalidKeyword();
	}
	
	private void assertAddMemo() {
		clearAll();
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
		add.execute("deadline /on 040414 /at 1000");
		r = add.getResult();
		assertEquals("add deadline", "Successfully added \"Deadline\".", r.getSystemFeedback());
		
		//use "by" keyword
		add.execute("deadline2 /on 100414 /by 1300");
		r = add.getResult();
		assertEquals("add deadline", "Successfully added \"Deadline2\".", r.getSystemFeedback());
	}
	
	private void assertAddDuration() {
		Command add = new AddCommand();
		add.execute("duration /on 040414 /from 1000 to 1200");
		Result r = add.getResult();
		assertEquals("add duration", "Successfully added \"Duration\".", r.getSystemFeedback());
	}
	
	private void assertAddInvalidKeyword() {
		Command add = new AddCommand();
		Result r;
		
		/*
		add.execute("invalid keyword /something");
		r = add.getResult();
		assertEquals("give invalid keyword", "ERROR: Invalid keyword(s) specified.", r.getSystemFeedback());
		
		add.execute("empty keyword /      ");
		r = add.getResult();
		assertEquals("give empty keyword", "Successfully added \"\".", r.getSystemFeedback());
		*/
		
		add.execute("invalid time /on 040414 /from abcd to 1200");
		r = add.getResult();
		assertEquals("user enters invalid duration", 
				"ERROR: Input duration is not valid.", r.getSystemFeedback());
		
		add.execute("invalid time /on 040414 /at -656");
		r = add.getResult();
		assertEquals("user enters invalid deadline", 
				"ERROR: Input deadline time is not valid.", r.getSystemFeedback());
	
		add.execute("invalid time /on 040414 /from 1009");
		r = add.getResult();
		assertEquals("user enters start time but no end time", 
				"ERROR: Input starting time without ending time.", r.getSystemFeedback());
		
		add.execute("invalid time /on 040414 /from 2359 to 2359");
		r = add.getResult();
		assertEquals("user enters same start time and end time", 
				"ERROR: Input duration is not valid.", r.getSystemFeedback());
		
	}
	
	private void assertClearAll() {
		clearAll();
	}

	private void clearAll() {
		Command clear = new ClearCommand();
		clear.execute("all");
		Result r = clear.getResult();
		assertEquals("clear all", 
				"Successfully cleared all tasks.", 
				r.getSystemFeedback());
	}
	private void assertClearDone() {
		Command clear = new ClearCommand();
		Result r;
		
		//clear done by specifying done explicitly
		clear.execute("done");
		r = clear.getResult();
		assertEquals("clear done", 
				"Successfully cleared all done tasks.", 
				r.getSystemFeedback());
		
		//clear done by giving empty string
		clear = new ClearCommand();
		clear.execute("   ");
		r = clear.getResult();
		assertEquals("clear    ", 
				"Successfully cleared all done tasks.", 
				r.getSystemFeedback());
	}
	
	private void assertClearInvalidKeyword() {
		Command clear = new ClearCommand();
		clear.execute("invalid stuff here");
		Result r = clear.getResult();
		assertEquals("user writes rubbish", 
				"ERROR: Invalid keyword(s) entered!", 
				r.getSystemFeedback());
	}
	
	/*
	private void assertTaskListForCommands(CommandExecutor exec) {
		assertTaskListForClear(exec);
		assertTaskListWhenAddingAllTypesOfTasks(exec);	
		
		//KIV: assertTaskListWhenEditing... etc.
		
		exec.executeClearCommand();
	}
	
	private void assertTaskListForClear(CommandExecutor exec) {
		exec.executeClearCommand();
		assertEquals("All tasks are cleared", 
				"", exec.jUnitAutomatedTest());
	}
	
	private void assertTaskListWhenAddingAllTypesOfTasks(CommandExecutor exec) {
		assertAddATaskWithNoSetTiming(exec);
		assertAddATaskWithDuration(exec);
		assertAddATaskWithDeadline(exec);
		assertAddAFullDayTask(exec);
		assertAddATaskWithKeywordsInItsTaskName(exec);
	}
	
	//These are boundary test cases for adding tasks to an empty list
	private void assertAddATaskWithNoSetTiming(CommandExecutor exec) {
		exec.executeAddCommand("nosettiming");
		assertEquals("Desc: Adding a task with no set timing", 
				"1. Nosettiming\n", exec.jUnitAutomatedTest());
		
		exec.executeClearCommand();
	}
	private void assertAddATaskWithDuration(CommandExecutor exec) {
		exec.executeAddCommand("setduration /on 121214 /from 0000 to 0001");
		assertEquals("Desc: Adding a task with set duration", 
				"1. Setduration\n" + 
				"      12-Dec-2014\n" +
				"      From 00:00 to 00:01\n", 
				exec.jUnitAutomatedTest());

		exec.executeClearCommand();
	}
	private void assertAddATaskWithDeadline(CommandExecutor exec) {
		exec.executeAddCommand("deadline /on 121214 /at 0000");
		assertEquals("Desc: Adding a task with deadline",
				"1. Deadline\n" + 
				"      12-Dec-2014\n" +
				"      At/By 00:00\n",  
				exec.jUnitAutomatedTest());

		exec.executeClearCommand();
	}
	private void assertAddAFullDayTask(CommandExecutor exec) {
		exec.executeAddCommand("fullday /on 121214");
		assertEquals("Desc: Adding a full day task", 
				"1. Fullday\n" + 
				"      12-Dec-2014\n", 
				exec.jUnitAutomatedTest());
		
		exec.executeClearCommand();
	}
	private void assertAddATaskWithKeywordsInItsTaskName(CommandExecutor exec) {
		exec.executeAddCommand("from to loc on at /on 121214");
		assertEquals("Desc: Adding a task with keywords in task name", 
				"1. From to loc on at\n" + 
				"      12-Dec-2014\n", 
				exec.jUnitAutomatedTest());
		
		exec.executeClearCommand();
	}
	
	private void assertFeedbackForCommands(CommandExecutor exec) {
		assertFeedbackForClear(exec);
		assertFeedbackForDisplayDefaultNoTasks(exec);
		
		assertFeedbackForAdd(exec);
		assertFeedbackForDisplayAll(exec);
		assertFeedbackForDisplayOthers(exec);
		assertFeedbackForUndoAfterAdd(exec);
		
		
		//assertFeedbackForEdit(exec);
		//assertFeedbackForUndoAfterEdit(exec);
		//assertFeedbackForDelete(exec);
		//assertFeedbackForUndoAfterDelete(exec);
		
		//assertFeedbackFor(exec);
		//assertFeedbackFor(exec);
		exec.executeClearCommand();
	}
	
	private void assertFeedbackForClear(CommandExecutor exec) {
		assertEquals("Clear command execution", 
				"Successfully cleared all tasks.", exec.executeClearCommand());
	}
	
	private void assertFeedbackForAdd(CommandExecutor exec) {
		assertEquals("Add command execution", 
				"Successfully added \"Eat peanut butter\".", 
				exec.executeAddCommand("Eat peanut butter /on 121214 /loc Home"));
	}
	
	//This is a boundary test case for one task in list
	private void assertFeedbackForDisplayAll(CommandExecutor exec) {
		assertEquals("Display all command execution", 
				"\nTasks Occurring/Due More Than A Week Later:\n" +
						"   1. Eat peanut butter\n" +
						"      12-Dec-2014\n" +
						"      Home\n",
				exec.executeDisplayCommand("all"));
		assertEquals("Display undone command execution",
				"\nTasks Occurring/Due More Than A Week Later:\n" +
						"   1. Eat peanut butter\n" +
						"      12-Dec-2014\n" +
						"      Home\n",
				exec.executeDisplayCommand("undone"));
		assertEquals("Display default command execution",
				"\nTasks Occurring/Due More Than A Week Later:\n" +
						"   1. Eat peanut butter\n" +
						"      12-Dec-2014\n" +
						"      Home\n",
				exec.executeDisplayCommand(""));
	}
	
	//This is a boundary test case for no done tasks, todays tasks, memos in list
	private void assertFeedbackForDisplayOthers(CommandExecutor exec) {
		assertEquals("Display done command execution", 
				"You have no completed tasks.\n",
				exec.executeDisplayCommand("done"));
		assertEquals("Display done command execution", 
				"You have no memos.\n",
				exec.executeDisplayCommand("memos"));
		assertEquals("Display overdue command execution",
				"You have no overdue tasks.\n",
				exec.executeDisplayCommand("overdue"));
		assertEquals("Display overdue command execution",
				"You have no tasks for today.\n",
				exec.executeDisplayCommand("today"));	
	}
	
	private void assertFeedbackForDisplayDefaultNoTasks(CommandExecutor exec) {
		assertEquals("Display default command execution",
				"You have no tasks.\n",
				exec.executeDisplayCommand(""));
	}
	
	private void assertFeedbackForUndoAfterAdd(CommandExecutor exec) {
		assertEquals("Undo command execution",
				"Successfully deleted \"Eat peanut butter\".", exec.executeUndoCommand());
	}
	*/
	//ROAR! 
	/*private void assertFeedbackForEdit(CommandExecutor exec) {
		exec.executeAddCommand("Eat peanut butter /on 121214 /from 1300 to 1400");
		
		assertEquals("Edit name only execution",
				"Successfully made changes to \"Eat Nutella\".", 
				exec.executeEditCommand("Eat Nutella"));
		
		assertEquals("Edit name and time execution",
				"Successfully made changes to \"Eat Nutella\".", 
				exec.executeEditCommand("Eat peanut butter /from 1500 to 1600"));
	}
	
	private void assertFeedbackForUndoAfterEdit(CommandExecutor exec) {
		assertEquals("Undo command execution",
				"Successfully reverted \"Eat peanut butter\".", exec.executeUndoCommand());
	}
	
	private void assertFeedbackForDelete(CommandExecutor exec) {
		assertEquals("Delete command execution",
				"Successfully deleted 1. Eat peanut butter.", exec.executeDeleteCommand(1));
	}
	
	private void assertFeedbackForUndoAfterDelete(CommandExecutor exec) {
		assertEquals("Undo command execution",
				"Successfully re-added \"Eat peanut butter\".", exec.executeUndoCommand());
	}*/
	/*
	private void assertUpdateHistory(CommandExecutor exec, ActionHistory history) {
		assertHistoryForAdd(exec, history);
		assertHistoryForEdit(exec, history);
		assertHistoryForDelete(exec, history);
		assertHistoryForUndo(exec, history);
		//assertUpdateHistoryForMark(history));
	}
	
	private void assertHistoryForAdd(CommandExecutor exec, ActionHistory history) {
		exec.executeAddCommand("nosettiming");
		assertEquals("History update for add", "add", history.getLastAction()); 
	}
	
	private void assertHistoryForEdit(CommandExecutor exec, ActionHistory history) {
		
	}
	private void assertHistoryForDelete(CommandExecutor exec, ActionHistory history) {
		
	}
	private void assertHistoryForUndo(CommandExecutor exec, ActionHistory history) {
		exec.executeUndoCommand();
		assertEquals("History update for undo - last action", 
				"undo", history.getLastAction());
		assertEquals("History update for undo - copy of last task", 
				null, history.getCopyOfLastTask());
		assertEquals("History update for undo - reference to last task", 
				null, history.getReferenceToLastTask());
	}
	*/
}
