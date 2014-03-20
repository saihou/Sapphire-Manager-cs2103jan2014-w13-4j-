import java.util.ArrayList;

/**
 *	This class is the main executor that will execute the commands
 *	ADD, DISPLAY, EDIT, DELETE, UNDO
 */

public class CommandExecutor {

	protected Storage taskStorage;
	protected CommandParser parser;
	protected SapphireManagerGUI gui = SapphireManagerGUI.getInstance();
	protected ActionHistory history;
	protected ArrayList<Task> allTasks, matchedTasks;

	/**
	 * @author Sai Hou
	 */
	public CommandExecutor(CommandParser parserFromManager) {
		parser = parserFromManager;
		history = new ActionHistory();
		taskStorage = new Storage();
		allTasks = taskStorage.getTaskList();
	}

	public void executeAddCommand(String userCommand) {
		Task taskToBeAdded = new Task();

		parser.parse(userCommand, taskToBeAdded);

		boolean isAdditionOfNewTaskSuccessful = addThisTask(taskToBeAdded);

		if (isAdditionOfNewTaskSuccessful) {
			gui.printToDisplay("Successfully added : " + taskToBeAdded.getName() + ".");
			//userInterface.displayMessage("Successfully added : " + taskToBeAdded.getName() + ".");
			updateHistory("add", taskToBeAdded, null);
		}
	}

	private boolean addThisTask(Task taskToBeAdded) {
		allTasks.add(taskToBeAdded);

		boolean isFileWritingSuccessful = taskStorage.writeATaskToFile(taskToBeAdded, true);
		return isFileWritingSuccessful;
	}

	public void executeDisplayCommand(String userCommand){
		gui.displayTasksGivenList(allTasks);
		//userInterface.displayTasksGivenList(allTasks);
	}

	public void executeEditCommand1(String userCommand){
		System.out.println("In edit command 1: "+userCommand);
		//search for the task
		matchedTasks = searchByName(userCommand);

		if (searchResultIsEmpty(userCommand, matchedTasks)) {
			return;
		}
		//display matched tasks
		gui.displayExistingTasksFound(matchedTasks);
	}

	public Task executeEditCommand2(){
		System.out.println("In edit command 2");
		//ask user to choose
		int choice = loopUntilUserEntersValidChoice(matchedTasks);

		Task taskToBeEdited = matchedTasks.get(choice-1);

		//display selected task to user
		gui.displayCurrentlyEditingSequence(taskToBeEdited);

		return taskToBeEdited; 
	}

	public void executeEditCommand3(Task taskToBeEdited){
		System.out.println("In edit command 3");
		//read input from user, parse input
		String userModifications = gui.readUserEdits();

		Task duplicatedOldTask = new Task(taskToBeEdited);

		boolean isEditSuccessful = editThisTask(taskToBeEdited, userModifications);

		if (isEditSuccessful) {
			gui.printToDisplay("Successfully made changes to " + taskToBeEdited.getName() +".");
			updateHistory("edit", taskToBeEdited, duplicatedOldTask);
		}
	}
	private boolean editThisTask(Task taskToBeEdited, String userModifications) {
		//modify array list
		parser.parse(userModifications, taskToBeEdited);

		//write changes to file
		boolean isFileWritingSuccessful = taskStorage.writeTaskListToFile();
		return isFileWritingSuccessful;
	}

	public void executeDeleteCommand1(String userCommand) {
		System.out.println("In delete command 1: "+userCommand);
		//search for the task
		matchedTasks = searchByName(userCommand);
		
		if (searchResultIsEmpty(userCommand, matchedTasks)) {
			return;
		}
		//display matched tasks
		gui.displayExistingTasksFound(matchedTasks);
	}

	public void executeDeleteCommand2() {	
		//ask user to choose
		int choice = loopUntilUserEntersValidChoice(matchedTasks);
		
		//retrieve the task to be removed
		Task taskToBeDeleted = matchedTasks.get(choice-1);
		
		boolean isDeletionSuccessful = deleteThisTask(taskToBeDeleted);
		
		if (isDeletionSuccessful) {
			gui.printToDisplay("Successfully deleted "+choice+". "+taskToBeDeleted.getName());
			updateHistory("delete", taskToBeDeleted, null);
		}
	}
	
	private boolean searchResultIsEmpty(String userCommand, ArrayList<Task> matchedTasks) {
		if (matchedTasks.size() == 0) {
			gui.printToDisplay("Cannot find " + userCommand + "!");
			return true;
		}
		else {
			return false;
		}
	}

	private boolean deleteThisTask(Task taskToBeRemoved) {
		//delete from array list
		allTasks.remove(taskToBeRemoved);

		//delete from text file
		boolean isFileWritingSuccessful = taskStorage.writeTaskListToFile();
		return isFileWritingSuccessful;
	}

	//stub
	//always true for now
	private int loopUntilUserEntersValidChoice(ArrayList<Task> matchedTasks) {
		int choice = gui.readUserChoice();
		boolean isChoiceValid = parser.checkIfUserChoiceIsValid(matchedTasks);

		while (!isChoiceValid) {
			gui.displayTasksGivenList(matchedTasks);
			choice = gui.readUserChoice();
			isChoiceValid = parser.checkIfUserChoiceIsValid(matchedTasks);
		}
		return choice;
	}

	public void executeClearCommand() {
		taskStorage.clear();
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

	private boolean ableToUndo(String lastAction) {
		if(lastAction == null){
			gui.printToDisplay("There is nothing to undo.");
			return false;
		} else if(lastAction.compareTo("undo") == 0) {
			gui.printToDisplay("Sorry, only able to undo once.");
			return false;
		} else{
			return true;
		}
	}

	private String executeActionRequiredToUndo(String lastAction){
		Task pointerToLastTask = history.getReferenceToLastTask();
		String taskName = pointerToLastTask.getName();
		String actionTakenToUndo = "";

		switch(lastAction) {
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

	public void executeUndoCommand() {
		String lastAction = history.getLastAction();

		if(ableToUndo(lastAction)){
			String actionTakenToUndo = executeActionRequiredToUndo(lastAction);
			gui.printToDisplay("Successfully " + actionTakenToUndo + '.');
			updateHistory("undo", null, null);
		}

	}

	/**
	 * @author Sai Hou
	 */
	private ArrayList<Task> searchByName(String name) {
		ArrayList<Task> matchedTasks = new ArrayList<Task>();

		for (Task t : allTasks) {
			String taskNameInLowerCase = t.getName().toLowerCase();
			name = name.toLowerCase();

			if (taskNameInLowerCase.contains(name)) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
	}

	/**
	 * @author Si Rui
	 */
	private ArrayList<Task> searchByDate(String date) {
		ArrayList<Task> matchedTasks = new ArrayList<Task>();

		for (Task t : allTasks) {
			String taskDate = t.getDate();

			if (taskDate != null) {
				if (taskDate.compareTo(date) == 0) {
					matchedTasks.add(t);
				}
			}
		}
		return matchedTasks;
	}

	public ArrayList<Task> getTodaysTasks(String date){

		ArrayList<Task> matchedTasks = searchByDate(date);

		return matchedTasks;
	}

	/**
	 * @author Sai Hou
	 */
	private void updateHistory(String lastAction, Task reference, Task duplicatedTask) {
		history.setLastAction(lastAction);
		history.setReferenceToLastTask(reference);
		history.setCopyOfLastTask(duplicatedTask);
	}

	//only used for JUnitTesting!
	public String jUnitAutomatedTest() {
		//basically combining all the println from various objects (UI, Task)
		//and appending them to a StringBuilder. At the end, the StringBuilder is
		//converted to a String and returned so that JUnit can assertEquals();

		StringBuilder sb = new StringBuilder();
		int count = 1;
		for(Task task : allTasks){
			sb.append(count+ ". ");

			sb.append(task.getName()+"\n");

			if(task.getType().equals("fullDay")){
				sb.append("Date: " + task.getDate()+"\n");
			} else if(task.getType().equals("setDuration")){
				sb.append("Date: " + task.getDate()+"\n");
				sb.append("Time: " + task.getStartTime() + " to " + task.getEndTime()+"\n");
			} else if(task.getType().equals("targetedTime")){
				sb.append("Date: " + task.getDate()+"\n");
				sb.append("Time: " + task.getDeadline()+"\n");
			} else {
				sb.append("To be completed during free-time.\n");
			}

			if(task.getLocation() != null){
				sb.append("Location: " + task.getLocation()+"\n");
			}

			task.printTaskDetails(count, gui);
		}
		return sb.toString();
	}

}
