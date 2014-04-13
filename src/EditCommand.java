//@author A0097812X
import java.util.ArrayList;

/*
 *Command pattern: This is one of the Concrete Commands.
 * 
 *Description: This class will handle the edit operation.
 */
public class EditCommand extends AddCommand {
	
	private static final int INDICATOR_INVALID = 0;
	private static final int INDICATOR_LOCATION = 1;
	private static final int INDICATOR_TIME = 2;
	private static final int INDICATOR_DATE = 3;
	
	private static final String ERROR_CANNOT_UNDO = "ERROR: Cannot undo!";
	private static final String ERROR_UNABLE_TO_EDIT = "ERROR: Unable to edit.";
	private static final String ERROR_KEYWORD = "ERROR: Invalid keyword(s)!";
	private static final String ERROR_NO_LIST = "ERROR: No list displayed at the moment!";
	private static final String ERROR_TASK_NUMBER = "ERROR: Invalid task number!";
	private static final String ERROR_REMOVE_NOT_PRESENT = "ERROR: Cannot remove something not present!";
	private static final String ERROR_REMOVE_DATE_WO_TIME = "ERROR: Cannot remove date without removing time!";
	private static final String ERROR_NOTHING_TO_REMOVE = "ERROR: Nothing to remove!";
	
	private static final String SUCCESS_UNDO = "Undo previous update: Successfully reverted \"%s\".";
	private static final String SUCCESS_EDIT = "Successfully made changes to \"%s\".";
	
	private static final String PLACEHOLDER_PARSE_SUCCESS = "parsing success";
	
	Task editedTask;
	
	public EditCommand(ArrayList<Task> current) {
		super();
		currentTaskList = current;
	}
	
	//constructor used by junit test for initialisation
	public EditCommand() {
		super();
	}
	
	@Override
	public void execute(String userCommand) {
		userCommand = userCommand.trim();

		String userChoice = getFirstWord(userCommand);
		
		if (currentTaskList != null) {
			proceedWithEditOnlyIfValidChoice(userCommand, userChoice);
		} else {
			systemFeedback = ERROR_NO_LIST;
		}
		result.setSystemFeedback(systemFeedback);
	}

	private void proceedWithEditOnlyIfValidChoice(String userCommand, String userChoice) {
		boolean isValidChoice = ValidationCheck.isValidChoice(userChoice, currentTaskList.size());

		if (isValidChoice) {
			proceedWithEdit(userCommand, userChoice);
		} else {
			systemFeedback = ERROR_TASK_NUMBER;
		}
	}

	private void proceedWithEdit(String userCommand, String userChoice) {
		String userModifications = prepareUserModifications(userCommand, userChoice);
		systemFeedback = parseAndModifyTask(userModifications, editedTask, "edit");
		
		//if parsing is successful
		if (systemFeedback.equals(PLACEHOLDER_PARSE_SUCCESS)) {
			systemFeedback = parseRemovalKeywordsAndModifyTask(userCommand);
			
			//if parsing is successful
			if (systemFeedback.equals(PLACEHOLDER_PARSE_SUCCESS)) {
				edit();
			}
			else {
				//do nothing; parsing error message is already contained in systemFeedback
			}
		} else {
			//do nothing; parsing error message is already contained in systemFeedback
		}
	}
	
	private String parseRemovalKeywordsAndModifyTask(String userCommand) {
		String feedback = "";
		if (userCommand.contains("/rm")) {
			int countNumberOfEdits = 0;
			boolean [] fieldsToRemove = parser.extractFieldsToRemove(userCommand);
			
			if (fieldsToRemove[INDICATOR_INVALID]) {
				feedback = ERROR_KEYWORD;
				return feedback;
			}
			
			if (isRemovingSomethingAlreadyNull(fieldsToRemove)) {
				feedback = ERROR_REMOVE_NOT_PRESENT;
				return feedback;
			}
			
			if (fieldsToRemove[INDICATOR_LOCATION]) {
				countNumberOfEdits++;
				editedTask.setLocation(null);
			}
			
			if (fieldsToRemove[INDICATOR_TIME]) {
				countNumberOfEdits++;
				editedTask.setStartTime(null);
				editedTask.setEndTime(null);
			}
			
			if (fieldsToRemove[INDICATOR_DATE]) {
				countNumberOfEdits++;
				//can only remove date if there is no time
				if (editedTask.getStartTime() == null && editedTask.getEndTime() == null) {
					editedTask.setDate(null);
				} else {
					feedback = ERROR_REMOVE_DATE_WO_TIME;
					return feedback;
				}
			}
			
			if (countNumberOfEdits == 0) {
				feedback = ERROR_NOTHING_TO_REMOVE;
			} else {
				setTaskType(editedTask);
				feedback = PLACEHOLDER_PARSE_SUCCESS;
			}
		} else {
			feedback = PLACEHOLDER_PARSE_SUCCESS;
		}
		return feedback;
	}
	
	private boolean isRemovingSomethingAlreadyNull(boolean[] fieldsToRemove) {
		if ( (fieldsToRemove[INDICATOR_LOCATION] && editedTask.getLocation() == null) ||
			(fieldsToRemove[INDICATOR_TIME] && editedTask.getStartTime() == null) ||
			(fieldsToRemove[INDICATOR_DATE] && editedTask.getDate() == null) ) {
			return true;
		}
		return false;
	}
	
	private String prepareUserModifications(String userCommand,
			String userChoice) {
		int choice = convertToInteger(userChoice);
		currentTask = currentTaskList.get(choice-1);
		editedTask = new Task(currentTask);

		String userModifications = userCommand.substring(userChoice.length()).trim();
		return userModifications;
	}

	private void edit() {
		boolean success = addFirstTaskAndDeleteSecondTask(editedTask, currentTask);
		
		if (success) {
			result.setSuccess(true);
			systemFeedback = formatString(SUCCESS_EDIT, editedTask.getName());
		} else {
			systemFeedback = ERROR_UNABLE_TO_EDIT;
		}
	}
	
	@Override
	public void undo() {
		boolean success = addFirstTaskAndDeleteSecondTask(currentTask, editedTask);
		
		if (success) {
			systemFeedback = formatString(SUCCESS_UNDO, currentTask.getName());
		}
		else {
			systemFeedback = ERROR_CANNOT_UNDO;
		}
		result.setSystemFeedback(systemFeedback);
	}
	
	private boolean addFirstTaskAndDeleteSecondTask(Task task1, Task task2) {
		//parent method
		boolean addSuccess = addThisTask(task1);
		
		DeleteCommand del = new DeleteCommand();
		boolean delSuccess = del.deleteThisTask(task2);
		
		if (delSuccess && addSuccess) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private String getFirstWord(String str) {
		return str.trim().split("\\s+")[0];
	}
	
	private int convertToInteger(String userCommand) {
		return Integer.parseInt(userCommand);
	}
	
	public Task getEditedTask() {
		return editedTask;
	}
	
	private String formatString(String message, String arg) {
		return String.format(message, arg);
	}
}
