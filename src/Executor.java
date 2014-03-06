import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Sai Hou
 *	This class is the main executor that will execute the commands
 *	ADD, DISPLAY, EDIT, DELETE
 */
public class Executor {
	
	Storage taskStorage;
	Parser parser;
	UserInterface userInterface;
	History history;
	
	public Executor(Parser parserFromManager, UserInterface ui) {
		String fileName = "";
		parser = parserFromManager;
		userInterface = ui;
		history = new History();
		taskStorage = new Storage(fileName, ui);
	}
	
	public void executeAddCommand(String userCommand) {
		ArrayList<Task> allTasks = taskStorage.getTaskList();
		
		Task myTask = new Task();
		
		parser.parse(userCommand, myTask);
		
		allTasks.add(myTask);
		
		taskStorage.writeATaskToFile(myTask);
		
		updateHistory("add", myTask, null);
	}
	
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
		
		updateHistory("edit", taskToBeEdited, duplicatedOldTask);
	}
	
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
		
		userInterface.displayMessage("Successfully deleted "+choice+". "+taskToBeRemoved.getName());
		
		taskStorage.writeTaskListToFile();
		
		updateHistory("delete", taskToBeRemoved, null);
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
	
	private void updateHistory(String lastAction, Task pointer, Task duplicatedTask) {
		history.setLastAction(lastAction);
		history.setPointerToLastTask(pointer);
		history.setCopyOfLastTask(duplicatedTask);
	}
}
