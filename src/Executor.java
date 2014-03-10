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
	ArrayList<Task> allTasks;
	
	/**
	 * @author Sai Hou
	 */
	public Executor(Parser parserFromManager, UserInterface ui) {
		parser = parserFromManager;
		userInterface = ui;
		history = new History();
		taskStorage = new Storage(ui);
		allTasks = taskStorage.getTaskList();
	}
	/**
	 * @author Cai Di
	 */
	public void executeAddCommand(String userCommand) {
		Task taskToBeAdded = new Task();
		
		parser.parse(userCommand, taskToBeAdded);
		
		boolean isAdditionOfNewTaskSuccessful = addThisTask(taskToBeAdded);
		
		if (isAdditionOfNewTaskSuccessful) {
			userInterface.displayMessage("Successfully added : " + taskToBeAdded.getName() + ".");
			updateHistory("add", taskToBeAdded, null);
		}
	}
	private boolean addThisTask(Task taskToBeAdded) {
		allTasks.add(taskToBeAdded);
		
		boolean isFileWritingSuccessful = taskStorage.writeATaskToFile(taskToBeAdded, true);
		return isFileWritingSuccessful;
	}
	/**
	 * @author Sai Hou
	 */
	public void executeDisplayCommand(String userCommand){
		userInterface.displayTasksGivenList(allTasks);
	}
	
	public void executeEditCommand(String userCommand){
		//search for the task
		ArrayList<Task> matchedTasks = searchByName(userCommand);
		
		//display matched tasks
		userInterface.displayExistingTasksFound(matchedTasks);
		
		//ask user to choose
		int choice = userInterface.readUserChoice();
		Task taskToBeEdited = matchedTasks.get(choice-1);
		
		//display selected task to user
		userInterface.displayCurrentlyEditingSequence(taskToBeEdited);
		
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
		//search for the task
		ArrayList<Task> matchedTasks = searchByName(userCommand);
		
		//display matched tasks
		userInterface.displayExistingTasksFound(matchedTasks);
		
		//ask user to choose
		int choice = loopUntilUserEntersValidChoice(matchedTasks);
		
		//retrieve the task to be removed
		Task taskToBeDeleted = matchedTasks.get(choice-1);
		
		//display it to user
		userInterface.displaySingleTask(taskToBeDeleted);
		
		boolean isDeletionSuccessful = deleteThisTask(taskToBeDeleted);
		
		if (isDeletionSuccessful) {
			userInterface.displayMessage("Successfully deleted "+choice+". "+taskToBeDeleted.getName());
			updateHistory("delete", taskToBeDeleted, null);
		}
	}
	
	private boolean deleteThisTask(Task taskToBeRemoved) {
		//delete from array list
		allTasks.remove(taskToBeRemoved);
		
		//delete from text file
		boolean isFileWritingSuccessful = taskStorage.writeTaskListToFile();
		return isFileWritingSuccessful;
	}
	private int loopUntilUserEntersValidChoice(ArrayList<Task> matchedTasks) {
		int choice = userInterface.readUserChoice();
		
		while (choice > matchedTasks.size()) {
			userInterface.displayTasksGivenList(matchedTasks);
			choice = userInterface.readUserChoice();
		}
		return choice;
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
			case "add": 
				//execute delete(pointerToLastTask)
				deleteThisTask(pointerToLastTask);
				
				actionTakenToUndo = "deleted \"" + taskName + '"';
				break;
			case "delete":	
				//execute add(pointerToLastTask)
				addThisTask(pointerToLastTask);
				
				actionTakenToUndo = "re-added \"" + taskName + '"';
				break;
			case "edit": 	//revert original copy of task
				Task taskToRestore = history.getCopyOfLastTask();
				//execute delete(pointerToLastTask)
				deleteThisTask(pointerToLastTask);
				//execute add(taskToRestore)
				addThisTask(taskToRestore);
				
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
	private ArrayList<Task> searchByName(String name) {
		ArrayList<Task> matchedTasks = new ArrayList<Task>();
		
		for (Task t : allTasks) {
			if (t.getName().contains(name)) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
	}
	
	/**
	 * @author Si Rui (&SH/Dex?? Whoever wrote the one above)
	 */
	private ArrayList<Task> searchByDate(String date) {
		ArrayList<Task> matchedTasks = new ArrayList<Task>();
		
		for (Task t : allTasks) {
			
			if (t.getDate().compareTo(date)==0) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
	}
	
	public ArrayList<Task> getTodaysTasks(String date){
		
		ArrayList<Task> matchedTasks = searchByDate(date);
		
		return matchedTasks;
	}
	
	private void updateHistory(String lastAction, Task pointer, Task duplicatedTask) {
		history.setLastAction(lastAction);
		history.setPointerToLastTask(pointer);
		history.setCopyOfLastTask(duplicatedTask);
		history.setUndoIsCalled(false);
	}
}
