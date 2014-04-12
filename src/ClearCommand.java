import java.util.ArrayList;

public class ClearCommand extends Command {
	
	public CommandParser parser = null;
	
	@Override
	public void execute(String userCommand) {
		parser = new CommandParser();
		
		userCommand = userCommand.trim();
		
		String clearType = parser.parseClearType(userCommand);
		
		System.out.println("clearing : "+ clearType);

		if (clearType.equals("all")) {
			clearAll();
		} else if (clearType.equals("done")){
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
			systemFeedback = "Successfully cleared all done tasks.";
		} else {
			systemFeedback = "ERROR: Couldn't clear tasks.";
		}
		
	}
	
	private void clearAll() {
		boolean isSuccessful = taskStorage.clear();
		//boolean isSuccessful = true;
		
		if (isSuccessful) {
			systemFeedback = "Successfully cleared all tasks.";
		} else {
			systemFeedback = "ERROR: Couldn't clear tasks.";
		}
	}
	
	
}
