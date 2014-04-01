import java.util.ArrayList;


public class DeleteCommand extends Command {
	
	public CommandParser parser = null;

	public DeleteCommand(ArrayList<Task> current) {
		super(current);
		System.out.println("In delete all: " + allTasks);
		System.out.println("In delete curr: " + currentTaskList);
	}
	
	@Override
	public void execute(String userChoice) {
		parser = new CommandParser();
		
	    userChoice = userChoice.trim();
	      
	    boolean isValidChoice = ValidationCheck.isValidChoice(userChoice, currentTaskList.size());
	      if (isValidChoice) {
	         int choice = convertToInteger(userChoice);
	         //retrieve the task to be removed
	         Task currentTask = currentTaskList.get(choice-1);
	         boolean isDeletionSuccessful = deleteThisTask(currentTask);
	         
	         if (isDeletionSuccessful) {
	            systemFeedback = "Successfully deleted "+choice+". "+currentTask.getName() + ".";
	            updateHistory("delete", currentTask, null);
	         }
	      } else {
	         systemFeedback = "Invalid number";
	      }
	}
	
	private boolean deleteThisTask(Task taskToBeRemoved) {
		//delete from array list
		System.out.println(allTasks.remove(taskToBeRemoved));
		System.out.println("In delete all: " + allTasks);
		System.out.println("In delete get: " + taskStorage.getTaskList());
		//delete from text file
		boolean isFileWritingSuccessful = taskStorage.writeTaskListToFile();
		return isFileWritingSuccessful;
	}
	
	private int convertToInteger(String userCommand) {
		return Integer.parseInt(userCommand);
	}
}
