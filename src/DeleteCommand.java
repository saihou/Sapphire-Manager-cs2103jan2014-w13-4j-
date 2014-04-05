import java.util.ArrayList;


public class DeleteCommand extends Command {
	
	public CommandParser parser = null;
	
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
	      
	    boolean isValidChoice = ValidationCheck.isValidChoice(userChoice, currentTaskList.size());
	      if (isValidChoice) {
	         int choice = convertToInteger(userChoice);
	         //retrieve the task to be removed
	         currentTask = currentTaskList.get(choice-1);
	         boolean isDeletionSuccessful = deleteThisTask(currentTask);
	         
	         if (isDeletionSuccessful) {
	            systemFeedback = "Successfully deleted "+choice+". "+currentTask.getName() + ".";
	         }
	      } else {
	         systemFeedback = "Invalid number";
	      }
	}
	
	protected boolean deleteThisTask(Task taskToBeRemoved) {
		//delete from array list
		System.out.println(allTasks.remove(taskToBeRemoved));
		//delete from text file
		boolean isFileWritingSuccessful = taskStorage.writeTaskListToFile();
		return isFileWritingSuccessful;
	}
	
	@Override
	public void undo() {
		System.out.println("Add: "+ allTasks.add(currentTask));
		
		if (taskStorage.writeTaskListToFile()) {
			systemFeedback = "Undo previous deletion: Successfully added \""+ currentTask.getName() + "\"";
		}
		else {
			systemFeedback = "Cannot undo!";
		}
	}
	
	private int convertToInteger(String userCommand) {
		return Integer.parseInt(userCommand);
	}
	
	
}
