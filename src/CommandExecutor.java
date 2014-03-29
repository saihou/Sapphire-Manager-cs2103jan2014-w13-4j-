import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
	
	private final static String MESSAGE_INVALID_COMMAND = "Invalid command entered. Please try again.";
	private final static String MESSAGE_NO_SEARCH_RESULTS = "No records found!";
	
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
		boolean isParsable = true;
		boolean isSettingTaskTypeSuccessful;
		
		//parser.parse(userCommand, taskToBeAdded);
		parser.extractTaskDetails(userCommand);
		
		String [] taskDetails = parser.taskDetails;
		if (parser.invalidFeedBack != null) {
			isParsable = false;
			systemFeedback = parser.invalidFeedBack;
		}
		
		isSettingTaskTypeSuccessful = setTaskType(taskDetails[1], taskDetails[2], 
											taskDetails[3], taskToBeAdded);
		
		if (isParsable && isSettingTaskTypeSuccessful) {
			
			taskToBeAdded.setName(taskDetails[0]);
			taskToBeAdded.setStartTime(taskDetails[1]);
			taskToBeAdded.setEndTime(taskDetails[2]);
			taskToBeAdded.setDate(taskDetails[3]);
			taskToBeAdded.setLocation(taskDetails[4]);
			taskToBeAdded.setIsDone(false);
			
			boolean isAdditionOfNewTaskSuccessful = addThisTask(taskToBeAdded);

			if (isAdditionOfNewTaskSuccessful) {
				systemFeedback = "Successfully added \"" + taskToBeAdded.getName() + "\".";
				updateHistory("add", taskToBeAdded, null);
			}
			else {
				systemFeedback = "Unable to add \"" + taskToBeAdded.getName() + "\".";
			}
		}
		return systemFeedback;
	}

	private boolean setTaskType(String startTime, 
			String endTime, String date, Task taskToBeAdded) {
		if (date == null && startTime == null && endTime == null) {
			taskToBeAdded.setType("noSetTiming");
			return true;
		} else if (date != null && startTime == null && endTime == null) {
			taskToBeAdded.setType("fullDay");
			return true;
		} else if (date != null && startTime != null && endTime == null) {
			taskToBeAdded.setType("targetTime");
			return true;
		} else if (date != null && startTime != null && endTime != null) {
			taskToBeAdded.setType("setDuration");
			return true;
		} else {
			//the rest of the cases, e.g. entered deadline/duration 
			//but never enter date 
			return false;
		}
	}

	private boolean addThisTask(Task taskToBeAdded) {
		allTasks.add(taskToBeAdded);

		boolean isFileWritingSuccessful = taskStorage.writeATaskToFile(taskToBeAdded, true);
		return isFileWritingSuccessful;
	}

	/**
	 * @author Si Rui
	 */
	public String executeDisplayCommand(String userCommand) {
		String systemFeedback = "";
		//ArrayList<Task> taskList = null;
		
		if (allTasks.isEmpty()) {
			 systemFeedback = "You have no tasks.\n";
		} else {
			String displayType = parser.parseDisplayType(userCommand);
			assert(displayType != null);
			
			try {
				currentTaskList = prepareTaskList(displayType, currentTaskList);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
			
			if (currentTaskList.isEmpty()) {
				systemFeedback = getFeedbackIfHaveNoTasks(displayType);
			}
			
			if (systemFeedback.equals("")){
				systemFeedback = formDisplayText(currentTaskList);
			}
		}
		return systemFeedback;
	}
	
	private ArrayList<Task> prepareTaskList(String displayType, ArrayList<Task> taskList) throws IllegalArgumentException {
		Collections.sort(allTasks);
		
		boolean isDone;
		
		switch (displayType) {
			case "all" : 
				taskList = new ArrayList<Task>(allTasks);
				break;
			case "past" :
				isDone = true;
				taskList = getTasksBasedOnCompletion(allTasks, isDone);
				break;
			case "today" :
				taskList = getTodaysTasks();
				break;
			case "future" :
				isDone = false;
				taskList = getTasksBasedOnCompletion(allTasks, isDone);
				break;
			default :
				throw new IllegalArgumentException(MESSAGE_INVALID_COMMAND);
		}
		return taskList;
	}
	
	private ArrayList<Task> getTasksBasedOnCompletion(ArrayList<Task> taskList, 
														boolean statusRequested) {
		ArrayList<Task> matchedTasks = new ArrayList<Task>();

		for (Task t : taskList) {
			if (t.getIsDone() == statusRequested) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
	}
	
	private String getFeedbackIfHaveNoTasks(String displayType) {
		String feedback = "";
		if (displayType.equals("past")) {
			feedback = "You have no completed tasks.";
		} else if (displayType.equals("today")) {
			feedback = "You have no tasks for today.";
		} else if (displayType.equals("future")) {
			feedback = "You have no uncompleted tasks.";
		}
		return feedback;
	}
	
	private String formDisplayText(ArrayList<Task> taskList) {
		String displayText = "";
		String currentDate = null;
		
		boolean printingMemos = false;
		int count = 1;
		
		for (Task t : taskList) {
			String taskDate = t.getDate();
			if ((taskDate != null && taskDate.equals(currentDate)) || printingMemos) {
				displayText += formDisplayTextOfOneTask(count, t);
			} else {
				String taskType = t.getType();
				if (taskType.equals("noSetTiming")) {
					//count = 1;
					printingMemos = true;
					displayText += "Memos: " + '\n';
					displayText += formDisplayTextOfOneMemo(count, t);
				} else {
					//count = 1;
					currentDate = taskDate;
					displayText += formatDate(currentDate) + '\n';
					displayText += formDisplayTextOfOneTask(count, t);
				}
			}
			count++;
		}
		return displayText;
	}
	
	private String formDisplayTextOfOneTask(int count, Task t) {
		System.out.println(t.getName() + '-' + t.getIsDone());
		return "\t" + count + ". " + t.getTaskDetailsWithoutDate() + '\n';
	}
	
	private String formDisplayTextOfOneMemo(int count, Task t) {
		System.out.println(t.getName() + '-' + t.getIsDone());
		return "\t" + count + ". " + t.getTaskDetailsWithoutDate();
	}
	private String formatDate(String date){
		//STUB
		return date;
	}

	/**
	 * @author Sai Hou
	 */
	public String executeEditCommand(String userCommand) {
		String systemFeedback = "";
		userCommand = userCommand.trim();
		
		String userChoice = getFirstWord(userCommand);
		
		boolean isValidChoice = ValidationCheck.isValidChoice(userChoice, currentTaskList.size());
		
		if (isValidChoice) {
			int choice = convertToInteger(userChoice);
			
			currentTask = currentTaskList.get(choice-1);
			
			Task duplicatedOldTask = new Task(currentTask);
			
			String userModifications = userCommand.substring(userChoice.length()).trim();
			
			System.out.println(userCommand);
			System.out.println(userModifications);
			
			boolean isEditSuccessful = editThisTask(currentTask, userModifications);

			if (isEditSuccessful) {
				systemFeedback = "Successfully made changes to \"" + currentTask.getName() +"\".";
				updateHistory("edit", currentTask, duplicatedOldTask);
			}
		} else {
			systemFeedback = "Invalid number";
		}
		return systemFeedback;
	}
	private boolean editThisTask(Task taskToBeEdited, String userModifications) {
		//modify array list
		parser.extractTaskDetails(userModifications);

		//write changes to file
		boolean isFileWritingSuccessful = taskStorage.writeTaskListToFile();
		return isFileWritingSuccessful;
	}

	public String executeDeleteCommand(String userChoice) {	
		String systemFeedback = "";
		userChoice = userChoice.trim();
		
		boolean isValidChoice = ValidationCheck.isValidChoice(userChoice, currentTaskList.size());
		if (isValidChoice) {
			int choice = convertToInteger(userChoice);
			//retrieve the task to be removed
			currentTask = currentTaskList.get(choice-1);
			
			boolean isDeletionSuccessful = deleteThisTask(currentTask);
			
			if (isDeletionSuccessful) {
				systemFeedback = "Successfully deleted "+choice+". "+currentTask.getName() + ".";
				updateHistory("delete", currentTask, null);
			}
		} else {
			systemFeedback = "Invalid number";
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

		try {
			if(isAbleToUndo(lastAction)){
				String actionTakenToUndo = executeActionRequiredToUndo(lastAction);
				updateHistory("undo", null, null);
				systemFeedback = "Successfully " + actionTakenToUndo + '.';
			} else {
				systemFeedback = getReasonForFailedUndo(lastAction);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return systemFeedback;
	}

	/**
	 * @author Cai Di
	 */
	private String executeSearchCommand(String taskName){
		String systemFeedback = "";
		boolean isSearchingByDate = false;
		
		taskName = taskName.trim();
		
		try {
			int date = Integer.parseInt(taskName);
			if (taskName.length() == 6){
				isSearchingByDate = true;
			}
			else {
				isSearchingByDate = false;
			}
		} catch(NumberFormatException e) {
			isSearchingByDate = false;
		}
		
		Collections.sort(allTasks);
		
		if(isSearchingByDate) {
			currentTaskList = searchByDate(taskName);
		}
		else {
			currentTaskList = searchByName(taskName);
		}
		
		if (currentTaskList.size() > 0) {
			systemFeedback = "Search results:\n" + formDisplayText(currentTaskList);
		}
		else {
			systemFeedback = MESSAGE_NO_SEARCH_RESULTS;
		}
		return systemFeedback;
	}
	/**
	 * @author Sai Hou
	 */
	public ArrayList<Task> searchByName(String name) {
		name = name.trim();
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

	public ArrayList<Task> getTodaysTasks(){
		String todaysDate = getTodaysDate();
		
		ArrayList<Task> matchedTasks = searchByDate(todaysDate);
		matchedTasks = getTasksBasedOnCompletion(matchedTasks, false);
		
		return matchedTasks;
	}
	
	private String getTodaysDate(){
		SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyy");
		Calendar date = Calendar.getInstance();
		return dateFormatter.format(date.getTime());
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
	
	private int convertToInteger(String userCommand) {
		return Integer.parseInt(userCommand);
	}
	
	private String getFirstWord(String str) {
		return str.trim().split("\\s+")[0];
	}
	
	public String doUserOperation(String userCommand) {
		String systemFeedback = "";
		
		String operation = getOperation(userCommand);
		
		boolean isValidOperation = ValidationCheck.isValidOperation(operation);
		if (!isValidOperation) {
			return "Wrong command entered! Enter F1 for a list of commands.";
		}
		
		if(operation.equalsIgnoreCase("exit") || operation.equalsIgnoreCase("quit")) {
			System.out.println("exit");
			System.exit(0);
		}
		
		systemFeedback = executeOperation(operation, userCommand);
		
		return systemFeedback;
	}
	
	private String executeOperation(String operation, String userCommand) {
		String systemFeedback = "";
		
		switch (operation) {
			case "create" :
				systemFeedback = executeAddCommand(userCommand.substring(6).trim());
				break;
			case "new" :
				//fallthrough
			case "add" :
				systemFeedback = executeAddCommand(userCommand.substring(3).trim());
				break;
			case "show" :
				systemFeedback = executeDisplayCommand(userCommand.substring(4).trim());
				break;
			case "display" :
				systemFeedback = executeDisplayCommand(userCommand.substring(7).trim());
				break;
			case "update" :
				systemFeedback = executeEditCommand(userCommand.substring(6).trim());
				break;
			case "edit" :
				systemFeedback = executeEditCommand(userCommand.substring(4).trim());
				break;
			case "remove" :
				//fallthrough
			case "delete" :
				systemFeedback = executeDeleteCommand(userCommand.substring(6).trim());
				break;
			case "clear" :
				systemFeedback = executeClearCommand();
				break;
			case "undo" :
				systemFeedback = executeUndoCommand();
				break;
			case "find" :
				systemFeedback = executeSearchCommand(userCommand.substring(4).trim());
				break;
			case "search" :
				systemFeedback = executeSearchCommand(userCommand.substring(6).trim());
				break;
			default : 
				//assert false here
				//if program reaches here, it should fail
				assert false;
				break;
		}
		return systemFeedback;
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
			sb.append(task.getAllTaskDetails());
		}
		return sb.toString();
	}
	
	
	/**
	 * @author Si Rui
	 */
	public ActionHistory getHistoryInstance() {
		return history;
	}


}
