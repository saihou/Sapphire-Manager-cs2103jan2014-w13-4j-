import java.util.ArrayList;


public class EditCommand extends AddCommand {
	
	public EditCommand(ArrayList<Task> current) {
		super();
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
				Task currentTask = currentTaskList.get(choice-1);
				task = new Task(currentTask);

				String userModifications = userCommand.substring(userChoice.length()).trim();

				systemFeedback = parseAndModifyTask(userModifications, currentTask, "edit");

				if (systemFeedback.equals("Successfully parsed")) {
					boolean isEditSuccessful = taskStorage.writeTaskListToFile();

					if (isEditSuccessful) {
						systemFeedback = "Successfully made changes to \"" + currentTask.getName() +"\".";
						updateHistory("edit", currentTask, task);
					}
				}
			} else {
				systemFeedback = "Invalid number";
			}
		}
		else {
			systemFeedback = "No list displayed at the moment!";
		}
		
	}
	
	private String getFirstWord(String str) {
		return str.trim().split("\\s+")[0];
	}
	
	private int convertToInteger(String userCommand) {
		return Integer.parseInt(userCommand);
	}
}
