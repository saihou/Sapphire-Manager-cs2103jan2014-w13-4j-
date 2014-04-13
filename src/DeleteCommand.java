//@author A0097812X
import java.util.ArrayList;

/*
* Command pattern: This is one of the concrete commands.
* 
* Description: This class will handle the delete operation.
*/

public class DeleteCommand extends Command {
	
	public CommandParser parser = null;
	
	private static final String ERROR_NO_LIST = "ERROR: No list displayed at the moment!";
	private static final String ERROR_TASK_NUMBER = "ERROR: Invalid task number!";
	private static final String ERROR_CANNOT_UNDO = "ERROR: Cannot undo!";
	private static final String ERROR_UNABLE_TO_DEL = "ERROR: Unable to delete.";
	
	private static final String SUCCESS_UNDO = "Undo previous deletion: Successfully added \"%s\".";
	private static final String SUCCESS_DEL = "Successfully deleted %s. %s.";
	
	//constructor used by junit test for initialisation
	public DeleteCommand() {
		super();
	}
	
	public DeleteCommand(ArrayList<Task> current) {
		super(current);
	}
	
	@Override
	public void execute(String userChoice) {
		parser = new CommandParser();
	    userChoice = userChoice.trim();
	    
	    if (currentTaskList != null) {
		    proceedWithDeleteOnlyIfValidChoice(userChoice);
	    } else {
	    	systemFeedback = ERROR_NO_LIST;
	    }
	    result.setSystemFeedback(systemFeedback);
	}

	private void proceedWithDeleteOnlyIfValidChoice(String userChoice) {
		boolean isValidChoice = ValidationCheck.isValidChoice(userChoice, currentTaskList.size());
		if (isValidChoice) {
			delete(userChoice);
		} else {
			systemFeedback = ERROR_TASK_NUMBER;
		}
	}

	private void delete(String userChoice) {
		int choice = convertToInteger(userChoice);
		//retrieve the task to be removed
		currentTask = currentTaskList.get(choice-1);
		boolean isDeletionSuccessful = deleteThisTask(currentTask);

		if (isDeletionSuccessful) {
			result.setSuccess(true);
			systemFeedback = formatString(SUCCESS_DEL, 
					Integer.toString(choice), currentTask.getName());
		} else {
			systemFeedback = ERROR_UNABLE_TO_DEL;
		}
	}
	
	protected boolean deleteThisTask(Task taskToBeRemoved) {
		//delete from array list
		allTasks.remove(taskToBeRemoved);
		//delete from text file
		boolean isFileWritingSuccessful = taskStorage.writeTaskListToFile();
		return isFileWritingSuccessful;
	}
	
	@Override
	public void undo() {
		System.out.println("Add: "+ allTasks.add(currentTask));
		
		if (taskStorage.writeTaskListToFile()) {
			systemFeedback = formatString(SUCCESS_UNDO, currentTask.getName());
		}
		else {
			systemFeedback = ERROR_CANNOT_UNDO;
		}
		result.setSystemFeedback(systemFeedback);
	}
	
	private int convertToInteger(String userCommand) {
		return Integer.parseInt(userCommand);
	}
	
	private String formatString(String message, String arg1, String arg2) {
		return String.format(message, arg1, arg2);
	}
	
	private String formatString(String message, String arg) {
		return String.format(message, arg);
	}
}
