import java.util.ArrayList;


public class EditCommand extends AddCommand {
	
	Task editedTask;
	ArrayList<Task> state;
	
	public EditCommand(ArrayList<Task> current) {
		super();
		state = new ArrayList<Task>(allTasks);
		currentTaskList = current;
	}
	
	@Override
	public void execute(String userCommand) {
		userCommand = userCommand.trim();

		String userChoice = getFirstWord(userCommand);
		
		if (currentTaskList != null) {
			boolean isValidChoice = ValidationCheck.isValidChoice(userChoice, currentTaskList.size());

			if (isValidChoice) {
				int choice = convertToInteger(userChoice);
				currentTask = currentTaskList.get(choice-1);
				editedTask = new Task(currentTask);

				String userModifications = userCommand.substring(userChoice.length()).trim();

				systemFeedback = parseAndModifyTask(userModifications, editedTask, "edit");

				if (systemFeedback.equals("Successfully parsed")) {
					boolean success = addFirstTaskAndDeleteSecondTask(editedTask, currentTask);
					
					if (success) {
						systemFeedback = "Successfully made changes to \"" + editedTask.getName() +"\".";
					} else {
						systemFeedback = "Unable to edit";
					}
				} else {
					systemFeedback = "Unable to parse input";
				}
			} else {
				systemFeedback = "Invalid number";
			}
		}
		else {
			systemFeedback = "No list displayed at the moment!";
		}
	}
	
	@Override
	public void undo() {
		boolean success = addFirstTaskAndDeleteSecondTask(currentTask, editedTask);
		
		if (success) {
			systemFeedback = "Undo previous update: Successfully reverted \""+ currentTask.getName() + "\"";
		}
		else {
			systemFeedback = "Cannot undo!";
		}
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
