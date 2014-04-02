import static org.junit.Assert.*;

//import org.junit.Test;


public class SapphireManagerTest {

	//@Test
	public void test() {
		CommandExecutor exec = new CommandExecutor();
		
		assertClearAll(exec);
		
		assertAddingAllTypesOfTasks(exec);
	}
	
	private void assertClearAll(CommandExecutor exec) {
		exec.executeClearCommand();
		assertEquals("Desc: Clear everything", "", exec.jUnitAutomatedTest());
	}
	
	private void assertAddingAllTypesOfTasks(CommandExecutor exec) {
		assertAddATaskWithNoSetTiming(exec);
		assertAddATaskWithDuration(exec);
		assertAddATaskWithDeadline(exec);
		assertAddAFullDayTask(exec);
		assertAddATaskWithKeywordsInItsTaskName(exec);
	}
	
	private void assertAddATaskWithNoSetTiming(CommandExecutor exec) {
		exec.executeAddCommand("nosettiming");
		assertEquals("Desc: Adding a task with no set timing", 
				"1. nosettiming\n" + 
				"To be completed during free-time.\n", 
				exec.jUnitAutomatedTest());
		
		exec.executeClearCommand();
	}
	private void assertAddATaskWithDuration(CommandExecutor exec) {
		exec.executeAddCommand("setduration /on 123456 /from 0000 to 0001");
		assertEquals("Desc: Adding a task with set duration", 
				"1. setduration\n" + 
				"Date: 123456\n" +
				"Time: 0000 to 0001\n", 
				exec.jUnitAutomatedTest());

		exec.executeClearCommand();
	}
	private void assertAddATaskWithDeadline(CommandExecutor exec) {
		exec.executeAddCommand("deadline /on 123456 /at 0000");
		assertEquals("Desc: Adding a task with deadline",
				"1. deadline\n" + 
				"Date: 123456\n" +
				"Time: 0000\n",  
				exec.jUnitAutomatedTest());

		exec.executeClearCommand();
	}
	private void assertAddAFullDayTask(CommandExecutor exec) {
		exec.executeAddCommand("fullday /on 123456");
		assertEquals("Desc: Adding a full day task", 
				"1. fullday\n" + 
				"Date: 123456\n", 
				exec.jUnitAutomatedTest());
		
		exec.executeClearCommand();
	}
	private void assertAddATaskWithKeywordsInItsTaskName(CommandExecutor exec) {
		exec.executeAddCommand("from to loc on at /on 123456");
		assertEquals("Desc: Adding a task with keywords in task name", 
				"1. from to loc on at\n" + 
				"Date: 123456\n", 
				exec.jUnitAutomatedTest());
		
		exec.executeClearCommand();
	}
}
