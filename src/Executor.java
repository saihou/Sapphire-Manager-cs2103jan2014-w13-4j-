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
	
	/**
	 * @author executeUndoCommand and related functions: Si Rui 
	 * Undo the last entered event. This can only be entered after an “add”, “delete”, or “edit” command.
	 * 		
	 * 		Last user action | Action taken by undo function
	 * 		Added a task 	-> Remove the task
	 * 		Deleted a task 	-> Restore the original task
	 * 		Edited a task	-> Revert all changes done to the original task
	 */
	
	private boolean noLastAction(String lastAction, boolean undoWasCalled){
		if(lastAction == null && !undoWasCalled){	//undo was not called
			return true;
		}else{
			return false;
		}
	}
	
	private boolean ableToUndo(String lastAction){
		boolean undoWasCalled = history.getUndoWasCalled();
		
		if(noLastAction(lastAction, undoWasCalled)){
			userInterface.displayMessage("There is nothing to undo.");
			return false;
		}else if(undoWasCalled){
			userInterface.displayMessage("Sorry, only able to undo once.");
			return false;
		}else{
			return true;
		}
	}
	
	private String executeActionRequiredToUndo(String lastAction){
		Task pointerToLastTask = history.getPointerToLastTask();
		String taskName = pointerToLastTask.getName();
		String actionTakenToUndo = "";
		
		switch(lastAction){
		case "add": //execute delete(pointerToLastTask)
			actionTakenToUndo = "deleted \"" + taskName + '"';
			break;
		case "delete":	//execute add(pointerToLastTask)
			actionTakenToUndo = "re-added \"" + taskName + '"';
			break;
		case "edit": 	//revert original copy of task
			Task taskToRestore = history.getCopyOfLastTask();
			//execute delete(pointerToLastTask)
			//execute add(taskToRestore)
			actionTakenToUndo = "reverted \"" + taskName + '"';
			break;
		}
		
		return actionTakenToUndo;
	}
	
	public void executeUndoCommand(){
		String lastAction = history.getLastAction();
		
		if(ableToUndo(lastAction)){
			String actionTakenToUndo = executeActionRequiredToUndo(lastAction);
			userInterface.displayMessage("Successfully " + actionTakenToUndo + '.');
		}
		
	}
	
	/**
	 * @author SH/Dex
	 */
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
	 * @author Si Rui (&SH/Dex?? Whoever wrote the one above)
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
		history.setUndoIsCalled(false);
	}
}
