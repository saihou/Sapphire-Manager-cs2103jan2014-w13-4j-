import java.util.ArrayList;

/**
 *	This class is the main executor that will execute the commands
 *	ADD, DISPLAY, EDIT, DELETE
 */
public class Executor {
	
	Storage taskStorage;
	Parser parser;
	UserInterface userInterface;
	History history;
	
	/**
	 * @author Sai Hou
	 */
	public Executor(Parser parserFromManager, UserInterface ui) {
		String fileName = "mytextfile";
		parser = parserFromManager;
		userInterface = ui;
		history = new History();
		taskStorage = new Storage(fileName, ui);
	}
	/**
	 * @author Cai Di
	 */
	public void executeAddCommand(String userCommand) {
		ArrayList<Task> allTasks = taskStorage.getTaskList();
		
		Task myTask = new Task();
		
		parser.parse(userCommand, myTask);
		
		allTasks.add(myTask);
		
		boolean isFileWritingSuccessful = taskStorage.writeATaskToFile(myTask, true);
		
		if (isFileWritingSuccessful) {
			userInterface.displayMessage("Successfully added : " + myTask.getName() + ".");
			updateHistory("add", myTask, null);
		}
	}
	/**
	 * @author Sai Hou
	 */
	public void executeDisplayCommand(String userCommand){
		ArrayList<Task> allTasks = taskStorage.getTaskList();
		
		userInterface.displayTasksGivenList(allTasks);
	}
	
	public void executeEditCommand(String userCommand){
		ArrayList<Task> allTasks = taskStorage.getTaskList();
		
		//search for the task
		ArrayList<Task> matchedTasks = searchByName(userCommand, allTasks);
		
		//display matched tasks
		userInterface.displayMessage("Existing tasks found:");
		userInterface.displayTasksGivenList(matchedTasks);
		userInterface.displayMessage("Enter a number: ");
		
		//ask user to choose
		int choice = userInterface.readUserChoice();
		Task taskToBeEdited = matchedTasks.get(choice-1);
		
		//display selected task to user
		userInterface.displayMessage("Currently editing: ");
		userInterface.displaySingleTask(taskToBeEdited);
		
		//read input from user, parse input
		String modificationsFromUser = userInterface.readUserUpdates();
		
		Task duplicatedOldTask = new Task(taskToBeEdited);
		
		parser.parse(modificationsFromUser, taskToBeEdited);
		
		userInterface.displayMessage("Successfully made changes to " + taskToBeEdited.getName() +".");
		
		updateHistory("edit", taskToBeEdited, duplicatedOldTask);
	}
	/**
	 * @author Dex
	 */
	public void executeDeleteCommand(String userCommand) {
		
		ArrayList<Task> allTasks = taskStorage.getTaskList();
		
		//search for the task
		ArrayList<Task> matchedTasks = searchByName(userCommand, allTasks);
		
		//display matched tasks
		userInterface.displayMessage("Existing tasks found:");
		userInterface.displayTasksGivenList(matchedTasks);
		userInterface.displayMessage("Enter a number: ");
		
		//ask user to choose
		int choice = userInterface.readUserChoice();
		
		//in case user entered the wrong number
		while (choice > matchedTasks.size()) {
			userInterface.displayTasksGivenList(matchedTasks);
			choice = userInterface.readUserChoice();
		}
		
		Task taskToBeRemoved = matchedTasks.get(choice-1);
		
		//display selected task to user
		userInterface.displaySingleTask(taskToBeRemoved);
		
		//delete
		allTasks.remove(taskToBeRemoved);
		
		
		boolean isFileWritingSuccessful = taskStorage.writeTaskListToFile();
		
		if (isFileWritingSuccessful) {
			userInterface.displayMessage("Successfully deleted "+choice+". "+taskToBeRemoved.getName());
			updateHistory("delete", taskToBeRemoved, null);
		}
	}
	
	private static ArrayList<Task> searchByName(String name, ArrayList<Task> taskList) {
		ArrayList<Task> matchedTasks = new ArrayList<Task>();
		
		for (Task t : taskList) {
			if (t.getName().contains(name)) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
	}
	
	/**
	 * @author Si Rui (&Dex/SH?? Whoever wrote the one above)
	 */
	private static ArrayList<Task> searchByDate(String date, ArrayList<Task> taskList) {
		ArrayList<Task> matchedTasks = new ArrayList<Task>();
		
		for (Task t : taskList) {
			if (t.getDate().compareTo(date)==0) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
	}
	
	public ArrayList<Task> getTodaysTasks(String date){
		ArrayList<Task> allTasks = taskStorage.getTaskList();
		
		ArrayList<Task> matchedTasks = searchByDate(date, allTasks);
		
		return matchedTasks;
	}
	
	private void updateHistory(String lastAction, Task pointer, Task duplicatedTask) {
		history.setLastAction(lastAction);
		history.setPointerToLastTask(pointer);
		history.setCopyOfLastTask(duplicatedTask);
	}
}
