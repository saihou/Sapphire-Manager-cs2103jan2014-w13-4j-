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
		Command command = null;

		assertTaskListForCommands(command);
		//assertFeedbackForCommands(command);
	}
	
	private void assertTaskListForCommands(Command command) {
		
		assertTaskListForClear(command);
		assertTaskListWhenAddingAllTypesOfTasks(command);	
		
		//KIV: assertTaskListWhenEditing... etc.
		
		clearAll(command);
	}
	
	private void clearAll(Command command) {
		assertTaskListForClear(command);
	}
	
	private void assertTaskListForClear(Command command) {
		command = new ClearCommand();
		command.execute("all");
		assertEquals("Successfully cleared all tasks.", 
				"", command.getSystemFeedback());
	}
	
	private void assertTaskListWhenAddingAllTypesOfTasks(Command command) {
		assertAddATaskWithNoSetTiming(command);
	//	assertAddATaskWithDuration(command);
	//	assertAddATaskWithDeadline(command);
	//	assertAddAFullDayTask(command);
	//	assertAddATaskWithKeywordsInItsTaskName(command);
	}
	
	//These are boundary test cases for adding tasks to an empty list
	private void assertAddATaskWithNoSetTiming(Command command) {
		command = new AddCommand();
		command.execute("nosettiming");
		assertEquals("Desc: Adding a task with no set timing", 
				"1. Nosettiming\n", command.getSystemFeedback());
		
		clearAll(command);
	}
	/*
	private void assertAddATaskWithDuration(Command command) {
	//	exec.executeAddCommand("setduration /on 121214 /from 0000 to 0001");
		assertEquals("Desc: Adding a task with set duration", 
				"1. Setduration\n" + 
				"      12-Dec-2014\n" +
				"      From 00:00 to 00:01\n", 
	//			exec.jUnitAutomatedTest());

		clearAll(command);
	}
	private void assertAddATaskWithDeadline(Command command) {
		exec.executeAddCommand("deadline /on 121214 /at 0000");
		assertEquals("Desc: Adding a task with deadline",
				"1. Deadline\n" + 
				"      12-Dec-2014\n" +
				"      At/By 00:00\n",  
				exec.jUnitAutomatedTest());

		clearAll(command);
	}
	private void assertAddAFullDayTask(CommandExecutor exec) {
		exec.executeAddCommand("fullday /on 121214");
		assertEquals("Desc: Adding a full day task", 
				"1. Fullday\n" + 
				"      12-Dec-2014\n", 
				exec.jUnitAutomatedTest());
		
		clearAll(command);
	}
	private void assertAddATaskWithKeywordsInItsTaskName(CommandExecutor exec) {
		exec.executeAddCommand("from to loc on at /on 121214");
		assertEquals("Desc: Adding a task with keywords in task name", 
				"1. From to loc on at\n" + 
				"      12-Dec-2014\n", 
				exec.jUnitAutomatedTest());
		
		clearAll(command);
	}*/
	/*
	private void assertFeedbackForCommands(CommandExecutor exec) {
		assertFeedbackForClear(exec);
		assertFeedbackForAdd(exec);
		assertFeedbackForDisplayAll(exec);
		assertFeedbackForDisplayPast(exec);
		assertFeedbackForUndoAfterAdd(exec);
		
		//assertFeedbackForEdit(exec);
		//assertFeedbackForUndoAfterEdit(exec);
		//assertFeedbackForDelete(exec);
		//assertFeedbackForUndoAfterDelete(exec);
		
		//assertFeedbackFor(exec);
		//assertFeedbackFor(exec);
		clearAll(command);
	}
	*/
	/*
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
				exec.executeDisplayCommand("/all"));
	}
	*/
	//This is a boundary test case for no tasks in list
	
	/*private void assertFeedbackForDisplayPast(CommandExecutor exec) {
		assertEquals("Display past command execution", 
				"\nTasks Occurring/Due More Than A Week Later:\n" +
						"   1. Eat peanut butter\n" +
						"      12-Dec-2014\n" +
						"      Home\n",
				exec.executeDisplayCommand("/done"));
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
