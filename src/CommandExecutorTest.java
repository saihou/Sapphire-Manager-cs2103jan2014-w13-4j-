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
		ActionHistory history = exec.getHistoryInstance();

		assertTaskListForCommands(exec);
		assertFeedbackForCommands(exec);
		assertUpdateHistory(exec, history);
	}
	
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
				"1. nosettiming\n", exec.jUnitAutomatedTest());
		
		exec.executeClearCommand();
	}
	private void assertAddATaskWithDuration(CommandExecutor exec) {
		exec.executeAddCommand("setduration /on 121214 /from 0000 to 0001");
		assertEquals("Desc: Adding a task with set duration", 
				"1. setduration\n" + 
				"\tDate: 121214\n" +
				"\tTime: 0000 to 0001\n", 
				exec.jUnitAutomatedTest());

		exec.executeClearCommand();
	}
	private void assertAddATaskWithDeadline(CommandExecutor exec) {
		exec.executeAddCommand("deadline /on 121214 /at 0000");
		assertEquals("Desc: Adding a task with deadline",
				"1. deadline\n" + 
				"\tDate: 121214\n" +
				"\tTime: 0000\n",  
				exec.jUnitAutomatedTest());

		exec.executeClearCommand();
	}
	private void assertAddAFullDayTask(CommandExecutor exec) {
		exec.executeAddCommand("fullday /on 121214");
		assertEquals("Desc: Adding a full day task", 
				"1. fullday\n" + 
				"\tDate: 121214\n", 
				exec.jUnitAutomatedTest());
		
		exec.executeClearCommand();
	}
	private void assertAddATaskWithKeywordsInItsTaskName(CommandExecutor exec) {
		exec.executeAddCommand("from to loc on at /on 121214");
		assertEquals("Desc: Adding a task with keywords in task name", 
				"1. from to loc on at\n" + 
				"\tDate: 121214\n", 
				exec.jUnitAutomatedTest());
		
		exec.executeClearCommand();
	}
	
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
				"121214\n" +
						"\t1. Eat peanut butter\n" +
						"\tLocation: Home\n\n",
				exec.executeDisplayCommand("/all"));
	}
	
	//This is a boundary test case for no tasks in list
	private void assertFeedbackForDisplayPast(CommandExecutor exec) {
		assertEquals("Display past command execution", 
				"You have no completed tasks.",
				exec.executeDisplayCommand("/past"));
	}
	
	private void assertFeedbackForUndoAfterAdd(CommandExecutor exec) {
		assertEquals("Undo command execution",
				"Successfully deleted \"Eat peanut butter\".", exec.executeUndoCommand());
	}
	
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
	
}
