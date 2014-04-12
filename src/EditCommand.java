import java.util.ArrayList;


public class EditCommand extends AddCommand {
	
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
			systemFeedback = "ERROR: No list displayed at the moment!";
		}
		result.setSystemFeedback(systemFeedback);
	}

	private void proceedWithEditOnlyIfValidChoice(String userCommand, String userChoice) {
		boolean isValidChoice = ValidationCheck.isValidChoice(userChoice, currentTaskList.size());

		if (isValidChoice) {
			proceedWithEdit(userCommand, userChoice);
		} else {
			systemFeedback = "ERROR: Invalid task number!";
		}
	}

	private void proceedWithEdit(String userCommand, String userChoice) {
		String userModifications = prepareUserModifications(userCommand, userChoice);
		systemFeedback = parseAndModifyTask(userModifications, editedTask, "edit");
		
		//if parsing is successful
		if (systemFeedback.equals("parsing success")) {
			systemFeedback = parseRemovalKeywordsAndModifyTask(userCommand);
			
			if (systemFeedback.equals("parsing success")) {
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
			
			if (fieldsToRemove[0]) {
				feedback = "ERROR: Invalid keyword(s)!";
				return feedback;
			}
			
			if (isRemovingSomethingAlreadyNull(fieldsToRemove)) {
				feedback = "ERROR: Cannot remove something not present!";
				return feedback;
			}
			
			if (fieldsToRemove[1]) {
				countNumberOfEdits++;
				editedTask.setLocation(null);
			}
			
			if (fieldsToRemove[2]) {
				countNumberOfEdits++;
				editedTask.setStartTime(null);
				editedTask.setEndTime(null);
			}
			
			if (fieldsToRemove[3]) {
				countNumberOfEdits++;
				//can only remove date if there is no time
				if (editedTask.getStartTime() == null && editedTask.getEndTime() == null) {
					editedTask.setDate(null);
				} else {
					feedback = "ERROR: Cannot remove date without removing time!";
					return feedback;
				}
			}
			
			if (countNumberOfEdits == 0) {
				feedback = "ERROR: Nothing to remove!";
			} else {
				setTaskType(editedTask);
				feedback = "parsing success";
			}
		} else {
			feedback = "parsing success";
		}
		return feedback;
	}
	
	private boolean isRemovingSomethingAlreadyNull(boolean[] fieldsToRemove) {
		if ( (fieldsToRemove[1] && editedTask.getLocation() == null) ||
			(fieldsToRemove[2] && editedTask.getStartTime() == null) ||
			(fieldsToRemove[3] && editedTask.getDate() == null) ) {
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
			systemFeedback = "Successfully made changes to \"" + editedTask.getName() +"\".";
		} else {
			systemFeedback = "ERROR: Unable to edit.";
		}
	}
	
	@Override
	public void undo() {
		boolean success = addFirstTaskAndDeleteSecondTask(currentTask, editedTask);
		
		if (success) {
			systemFeedback = "Undo previous update: Successfully reverted \""+ currentTask.getName() + "\"";
		}
		else {
			systemFeedback = "ERROR: Cannot undo!";
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
}
