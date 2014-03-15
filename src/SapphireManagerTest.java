import static org.junit.Assert.*;

import org.junit.Test;


public class SapphireManagerTest {

	//@Test
	public void test() {
		Executor exec = new Executor(new Parser(), new UserInterface());
		
		assertAddingAllTypesOfTasks(exec);
	}
	
	private void assertAddingAllTypesOfTasks(Executor exec) {
		assertAddATaskWithNoSetTiming(exec);
		assertAddATaskWithDuration(exec);
		assertAddATaskWithDeadline(exec);
		assertAddAFullDayTask(exec);
		assertAddATaskWithKeywordsInItsTaskName(exec);
	}
	
	private void assertAddATaskWithNoSetTiming(Executor exec) {
		exec.executeAddCommand("nosettiming");
		assertEquals("test", "1. nosettiming\n" + 
						"To be completed during free-time.\n", 
						exec.jUnitTestAdd("no use string", "junit"));
		
		exec.executeClearCommand("no use");
	}
	private void assertAddATaskWithDuration(Executor exec) {
		exec.executeAddCommand("setduration /on 123456 /from 0000 to 0001");
		assertEquals("test", "1. setduration\n" + 
				"Date: 123456\n" +
				"Time: 0000 to 0001\n", 
				exec.jUnitTestAdd("no use string", "junit"));

		exec.executeClearCommand("no use");
	}
	private void assertAddATaskWithDeadline(Executor exec) {
		exec.executeAddCommand("deadline /on 123456 /at 0000");
		assertEquals("test", "1. deadline\n" + 
				"Date: 123456\n" +
				"Time: 0000\n",  
				exec.jUnitTestAdd("no use string", "junit"));

		exec.executeClearCommand("no use");
	}
	private void assertAddAFullDayTask(Executor exec) {
		exec.executeAddCommand("fullday /on 123456");
		assertEquals("test", "1. fullday\n" + 
						"Date: 123456\n", 
						exec.jUnitTestAdd("no use string", "junit"));
		
		exec.executeClearCommand("no use");
	}
	private void assertAddATaskWithKeywordsInItsTaskName(Executor exec) {
		exec.executeAddCommand("from to loc on at /on 123456");
		assertEquals("test", "1. from to loc on at\n" + 
						"Date: 123456\n", 
						exec.jUnitTestAdd("no use string", "junit"));
		
		exec.executeClearCommand("no use");
	}
}
