//@author A0097812X
import java.util.ArrayList;

/*
 * Command pattern: This is one of the concrete commands.
 * 
 * Description: This class will handle the clear operation.
 */
public class ClearCommand extends Command {
	private static final String SUCCESSFULLY_CLEARED_ALL = "Successfully cleared all tasks.";
	private static final String SUCCESSFULLY_CLEARED_DONE = "Successfully cleared all done tasks.";
	private static final String ERROR_COULDN_T_CLEAR_TASKS = "ERROR: Couldn't clear tasks.";
	
	private static final String DONE = "done";
	private static final String ALL = "all";
	
	public CommandParser parser = null;
	
	@Override
	public void execute(String userCommand) {
		parser = new CommandParser();
		
		userCommand = userCommand.trim();
		
		String clearType = parser.parseClearType(userCommand);

		if (clearType.equals(ALL)) {
			clearAll();
		} else if (clearType.equals(DONE)){
			clearDone();
		} else {
			//if parsing fails, error message will be stored in clearType
			systemFeedback = clearType;
		}
		result.setSystemFeedback(systemFeedback);
	}
	
	private void clearDone() {
		Search seeker = new Search(allTasks);
		
		ArrayList<Task> tasksToClear = seeker.searchByStatus(true);
		boolean isSuccessful = true;
		
		for (Task t : tasksToClear) {
			boolean isRemoved = allTasks.remove(t);
			if (!isRemoved) {
				isSuccessful = false;
			}
		}
		
		if (isSuccessful) {
			taskStorage.writeTaskListToFile();
			systemFeedback = SUCCESSFULLY_CLEARED_DONE;
		} else {
			systemFeedback = ERROR_COULDN_T_CLEAR_TASKS;
		}
		
	}
	
	private void clearAll() {
		boolean isSuccessful = taskStorage.clear();
		
		if (isSuccessful) {
			systemFeedback = SUCCESSFULLY_CLEARED_ALL;
		} else {
			systemFeedback = ERROR_COULDN_T_CLEAR_TASKS;
		}
	}
	
	
}
