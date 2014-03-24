import java.util.ArrayList;

/**
 *	This class is the main executor that will execute the commands
 *	ADD, DISPLAY, EDIT, DELETE, UNDO
 */

public class CommandExecutor {

	protected Storage taskStorage;
	protected CommandParser parser;
	//protected SapphireManagerGUI gui = SapphireManagerGUI.getInstance();
	protected ActionHistory history;
	protected ArrayList<Task> allTasks;
	
	protected String phase;
	
	protected Task currentTask;
	protected ArrayList<Task> currentTaskList;
	
	/**
	 * @author Sai Hou
	 */
	public CommandExecutor() {
		parser = new CommandParser();
		history = new ActionHistory();
		taskStorage = new Storage();
		allTasks = taskStorage.getTaskList();
		phase = "normal";
		currentTask = null;
		currentTaskList = null;
	}

	public String executeAddCommand(String userCommand) {
		String systemFeedback = "";
		Task taskToBeAdded = new Task();

		parser.parse(userCommand, taskToBeAdded);

		boolean isAdditionOfNewTaskSuccessful = addThisTask(taskToBeAdded);

		if (isAdditionOfNewTaskSuccessful) {
			systemFeedback = "Successfully added : " + taskToBeAdded.getName() + ".";
			updateHistory("add", taskToBeAdded, null);
		}
		return systemFeedback;
	}

	private boolean addThisTask(Task taskToBeAdded) {
		allTasks.add(taskToBeAdded);

		boolean isFileWritingSuccessful = taskStorage.writeATaskToFile(taskToBeAdded, true);
		return isFileWritingSuccessful;
	}

	public void executeDisplayCommand(String userCommand){
		gui.displayTasksGivenList(allTasks);
	}

	public String executeEditCommand(String userModifications){
		String systemFeedback = "";

		Task duplicatedOldTask = new Task(currentTask);

		boolean isEditSuccessful = editThisTask(currentTask, userModifications);

		if (isEditSuccessful) {
			systemFeedback = "Successfully made changes to " + currentTask.getName() +".";
			updateHistory("edit", currentTask, duplicatedOldTask);
		}
		return systemFeedback;
	}
	private boolean editThisTask(Task taskToBeEdited, String userModifications) {
		//modify array list
		parser.parse(userModifications, taskToBeEdited);

		//write changes to file
		boolean isFileWritingSuccessful = taskStorage.writeTaskListToFile();
		return isFileWritingSuccessful;
	}

	public String executeDeleteCommand(int choice) {	
		String systemFeedback = "";
		//retrieve the task to be removed
		currentTask = currentTaskList.get(choice-1);
		
		boolean isDeletionSuccessful = deleteThisTask(currentTask);
		
		if (isDeletionSuccessful) {
			systemFeedback = "Successfully deleted "+choice+". "+currentTask.getName() + ".";
			updateHistory("delete", currentTask, null);
		}
		return systemFeedback;
	}
	
	private boolean deleteThisTask(Task taskToBeRemoved) {
		//delete from array list
		allTasks.remove(taskToBeRemoved);

		//delete from text file
		boolean isFileWritingSuccessful = taskStorage.writeTaskListToFile();
		return isFileWritingSuccessful;
	}

	public String executeClearCommand() {
		String systemFeedback = "";
		taskStorage.clear();
		systemFeedback = "Successfully cleared all tasks.";
		return systemFeedback;
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
	public ArrayList<Task> searchByName(String name) {
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
	
	public String getOperation(String userCommand) {
		return getFirstWord(userCommand);
	}
	
	public String getPhase() {
		return phase;
	}
	public ArrayList<Task> getCurrentTaskList() {
		return currentTaskList;
	}
	public Task getCurrentTask() {
		return currentTask;
	}
	public String doUserOperation(String userCommand) {
		String systemFeedback = "";
		
		String operation = getOperation(userCommand);
		
		if(operation.equalsIgnoreCase("exit") || operation.equalsIgnoreCase("quit")) {
			System.out.println("exit");
			System.exit(0);
		}
		
		switch (phase) {
			case "normal" :
				systemFeedback = executeOperation(operation, userCommand);
				break;
			case "editPhase1" :
				int choice = convertToInteger(userCommand);
				currentTask = currentTaskList.get(choice-1);
				phase = "editPhase2";
				break;
			case "editPhase2" :
				systemFeedback = executeEditCommand(userCommand);
				phase = "normal";
				break;
			case "deletePhase1" :
				choice = convertToInteger(userCommand);
				systemFeedback = executeDeleteCommand(choice);
				phase = "normal";
				break;
			default :
				break;
		}
		
		if (phase.equals("normal")) {
			currentTask = null;
			currentTaskList = null;
		}
		
		return systemFeedback;
	}
	
	private boolean checkIfOperationIsValid(String operation) {
		boolean isValidOperation = false;
		
		if (operation.equalsIgnoreCase("add") || 
			operation.equalsIgnoreCase("delete") ||
			operation.equalsIgnoreCase("display") ||
			operation.equalsIgnoreCase("edit") ||
			operation.equalsIgnoreCase("undo") ||
			operation.equalsIgnoreCase("clear")) {
			isValidOperation = true;
		}
		return isValidOperation;
	}
	
	private int convertToInteger(String userCommand) {
		return Integer.parseInt(userCommand);
	}
	
	private String executeOperation(String operation, String userCommand) {
		String systemFeedback = "";
		
		boolean isValidOperation = checkIfOperationIsValid(operation);
		
		if (!isValidOperation) {
			return "Wrong command entered! Enter F1 for a list of commands.";
		}
		
		switch (operation) {
			case "add" :
				systemFeedback = executeAddCommand(userCommand.substring(4));
				break;
			case "display" :
				executeDisplayCommand(userCommand.substring(7));
				break;
			case "edit" :
				currentTaskList = searchByName(userCommand.substring(5));
				phase = "editPhase1";
				break;
			case "delete" :
				currentTaskList = searchByName(userCommand.substring(7));
				phase = "deletePhase1";
				break;
			case "clear" :
				systemFeedback = executeClearCommand();
				break;
			case "undo" :
				executeUndoCommand();
				break;
			default : 
				break;
		}
		return systemFeedback;
	}
	
	private String getFirstWord(String str) {
		return str.trim().split("\\s+")[0];
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
				//sb.append("Time: " + task.getDeadline()+"\n");
			} else {
				sb.append("To be completed during free-time.\n");
			}

			if(task.getLocation() != null){
				sb.append("Location: " + task.getLocation()+"\n");
			}

			//task.printTaskDetails(count, gui);
		}
		return sb.toString();
	}

}
