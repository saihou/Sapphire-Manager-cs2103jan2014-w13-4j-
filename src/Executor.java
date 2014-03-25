import java.util.ArrayList;

/**
 *	This class is the main executor that will execute the commands
 *	ADD, DISPLAY, EDIT, DELETE, UNDO
 */

public class Executor {
	
	protected Storage taskStorage;
	protected Parser parser;
	protected UserInterface userInterface;
	protected ActionHistory history;
	protected ArrayList<Task> allTasks;
	
	/**
	 * @author Sai Hou
	 */
	public Executor(Parser parserFromManager, UserInterface ui) {
		parser = parserFromManager;
		userInterface = ui;
		history = new ActionHistory();
		taskStorage = new Storage();
		allTasks = taskStorage.getTaskList();
	}
	
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

	public void executeDisplayCommand(String userCommand){
		userInterface.displayTasksGivenList(allTasks);
	}
	
	public void executeEditCommand(String taskName){
		//search for the task
		ArrayList<Task> matchedTasks = searchByName(taskName);
		
		if (searchResultIsEmpty(taskName, matchedTasks)) {
			return;
		}
		//display matched tasks
		userInterface.displayExistingTasksFound(matchedTasks);
		
		//ask user to choose
		int choice = loopUntilUserEntersValidChoice(matchedTasks);
		
		Task taskToBeEdited = matchedTasks.get(choice-1);
		
		//display selected task to user
		userInterface.displayCurrentlyEditingSequence(taskToBeEdited);
		
		//read changes from user
		String userModifications = userInterface.readUserEdits();
		
		Task duplicatedOldTask = new Task(taskToBeEdited);
		
		boolean isEditSuccessful = editThisTask(taskToBeEdited, userModifications);
		
		if (isEditSuccessful) {
			userInterface.displayMessage("Successfully made changes to " + taskToBeEdited.getName() +".");
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
	
	public void executeDeleteCommand(String taskName) {
		//search for the task
		ArrayList<Task> matchedTasks = searchByName(taskName);
		
		if (searchResultIsEmpty(taskName, matchedTasks)) {
			return;
		}
		//display matched tasks
		userInterface.displayExistingTasksFound(matchedTasks);
		
		//ask user to choose
		int choice = loopUntilUserEntersValidChoice(matchedTasks);
		
		//retrieve the task to be removed
		Task taskToBeDeleted = matchedTasks.get(choice-1);
		
		boolean isDeletionSuccessful = deleteThisTask(taskToBeDeleted);
		
		if (isDeletionSuccessful) {
			userInterface.displayMessage("Successfully deleted "+choice+". "+taskToBeDeleted.getName());
			updateHistory("delete", taskToBeDeleted, null);
		}
	}

	private boolean searchResultIsEmpty(String userCommand, ArrayList<Task> matchedTasks) {
		if (matchedTasks.size() == 0) {
			userInterface.displayMessage("Cannot find " + userCommand + "!");
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
		int choice = userInterface.readUserChoice();
		boolean isChoiceValid = parser.checkIfUserChoiceIsValid(matchedTasks);
		
		while (!isChoiceValid) {
			userInterface.displayTasksGivenList(matchedTasks);
			choice = userInterface.readUserChoice();
			isChoiceValid = parser.checkIfUserChoiceIsValid(matchedTasks);
		}
		return choice;
	}
	
	public void executeClearCommand() {
		taskStorage.clear();
	}
	
	/**
	 * @author executeUndoCommand and related functions: Si Rui 
	 * Undo the last entered event. This can only be entered after an delete or edit
	 * 		
	 * 		Last user action | Action taken by undo function
	 * 		Added a task 	-> Remove the task
	 * 		Deleted a task 	-> Restore the original task
	 * 		Edited a task	-> Revert all changes done to the original task
	 */
	
	private boolean ableToUndo(String lastAction) {
		if(lastAction == null){
			userInterface.displayMessage("There is nothing to undo.");
			return false;
		} else if(lastAction.compareTo("undo") == 0) {
			userInterface.displayMessage("Sorry, only able to undo once.");
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
			userInterface.displayMessage("Successfully " + actionTakenToUndo + '.');
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
//				sb.append("Time: " + task.getDeadline()+"\n");
			} else {
				sb.append("To be completed during free-time.\n");
			}
			
			if(task.getLocation() != null){
				sb.append("Location: " + task.getLocation()+"\n");
			}
			
//			task.printTaskDetails(count, null);
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
			userInterface.displayExistingTasksFound(matchedTasks);
		}
		else{
			//search for the task
			ArrayList<Task> matchedTasks = searchByName(taskName);
			
			if (searchResultIsEmpty(taskName, matchedTasks)) {
				return;
			}
			//display matched tasks
			userInterface.displayExistingTasksFound(matchedTasks);
		}
	}




	

	
}
