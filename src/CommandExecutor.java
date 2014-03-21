import java.util.ArrayList;
import java.util.Collections;

/**
 *	This class is the main executor that will execute the commands
 *	ADD, DISPLAY, EDIT, DELETE, UNDO
 */

public class CommandExecutor {

	protected Storage taskStorage;
	protected CommandParser parser;
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

	/**
	 * @author Si Rui
	 */
	public String executeDisplayCommand(String userCommand){
		String systemFeedback;
		
		if (allTasks.isEmpty()) {
			 systemFeedback = "You have no tasks.\n";
		} else {
			Collections.sort(allTasks);
			
			//String displayType = parseDisplayType();
			ArrayList<Task> taskList = new ArrayList<Task>(allTasks);
			
			/*if (displayType.equals("all")) {
				taskList = allTasks; //INCOMPLETE!!!!
			}*/
			systemFeedback = formDisplayText(taskList);
			System.out.print(systemFeedback);
		}
		return systemFeedback;
	}
	
	private String formDisplayTextOfOneTask(int count, Task t) {
		return "\t" + count + ". " + t.getTaskDetails() + '\n';
	}
	
	private String formDisplayText(ArrayList<Task> taskList) {
		Task firstTask = taskList.get(0);
		String currentDate = firstTask.getDate();
		String displayText = currentDate + '\n';
		boolean printingMemos = false;
		int count = 1;
		
		for (Task t : taskList) {
			String taskDate = t.getDate();
			if ((taskDate != null && taskDate.equals(currentDate)) || printingMemos) {
				displayText += formDisplayTextOfOneTask(count, t);
			} else {
				String taskType = t.getType();
				if (taskType.equals("noSetTiming")) {
					count = 1;
					printingMemos = true;
					displayText += "Memos: " + '\n';
					displayText += formDisplayTextOfOneTask(count, t);
				} else {
					count = 1;
					currentDate = taskDate;
					displayText += currentDate + '\n';
					displayText += formDisplayTextOfOneTask(count, t);
				}
			}
			count++;
		}
		return displayText;
	}

	/**
	 * @author Sai Hou
	 */
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
	 * @author Si Rui
	 * This class extends Command class and is used to undo the last entered event. 
	 * Undo can only be used after an “add”, “delete”, or “edit” command by user.
	 * Expected Behavior:
	 * 		Last user action | Action taken by undo function
	 * ---------------------->-----------------------------------------------
	 * 		Added a task 	-> Remove the task
	 * 		Deleted a task 	-> Restore the original task
	 * 		Edited a task	-> Revert all changes done to the original task
	 */

	private boolean isAbleToUndo(String lastAction) {
		if (lastAction.equals("add") || lastAction.equals("delete") ||
				lastAction.equals("edit")) {
			return true;
		} else {
			return false;
		}
	}
	
	private String getReasonForFailedUndo(String lastAction) throws IllegalStateException {
		if (lastAction == null) {
			return "There is nothing to undo.";
		} else if (lastAction.equals("undo")) {
			return "Sorry, only able to undo once.";
		} else {
			throw new IllegalStateException("Error: Undo process has failed.");
		}
	}

	private String executeActionRequiredToUndo(String lastAction) throws IllegalStateException {
		Task pointerToLastTask = history.getReferenceToLastTask();
		assert pointerToLastTask != null;
		
		String taskName = pointerToLastTask.getName();
		String actionTakenToUndo = "";

		switch(lastAction) {
			case "add": 
				deleteThisTask(pointerToLastTask);
				actionTakenToUndo = "deleted \"" + taskName + '"';
				break;
			case "delete":	
				addThisTask(pointerToLastTask);
				actionTakenToUndo = "re-added \"" + taskName + '"';
				break;
			case "edit": 	//revert original copy of task
				Task taskToRestore = history.getCopyOfLastTask();
				deleteThisTask(pointerToLastTask);
				addThisTask(taskToRestore);
				actionTakenToUndo = "reverted \"" + taskName + '"';
				break;
			default :
				throw new IllegalStateException("Error: Undo process has failed.");
		}

		return actionTakenToUndo;
	}

	public String executeUndoCommand() {
		String systemFeedback = "";
		String lastAction = history.getLastAction();

		if(isAbleToUndo(lastAction)){
			String actionTakenToUndo = executeActionRequiredToUndo(lastAction);
			updateHistory("undo", null, null);
			systemFeedback = "Successfully " + actionTakenToUndo + '.';
		} else {
			systemFeedback = getReasonForFailedUndo(lastAction);
		}
		return systemFeedback;
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
				if (taskDate.equals(date)) {
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
				systemFeedback = executeDisplayCommand(userCommand.substring(7));
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
	
	private void search(String taskName){
		boolean searchDate = false;
		taskName = taskName.trim();
		
		try{
			int date = Integer.parseInt(taskName);
			if(taskName.length()==6){
				searchDate = true;
			}
			else{
				searchDate = false;
			}
		}catch(NumberFormatException e){
			searchDate = false;
		}
		
		if(searchDate){
			ArrayList<Task> matchedTasks = searchByDate(taskName);
			//pls display matched task
		}
		else{
			//search for the task
			ArrayList<Task> matchedTasks = searchByName(taskName);
			//pls display matched task
			


		}
	}





}
