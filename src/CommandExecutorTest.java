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

import java.util.ArrayList;

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
	
	private void assertClearInvalid() {
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
