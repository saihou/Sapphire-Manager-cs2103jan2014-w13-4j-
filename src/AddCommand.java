public class AddCommand extends Command {
	
	public CommandParser parser = null;
	
	public AddCommand() {
		super();
		parser = new CommandParser();
	}
	
	@Override
	public void execute(String userCommand) {
		userCommand = userCommand.trim();
		currentTask = new Task();
		
		systemFeedback = parseAndModifyTask(userCommand, currentTask, "add");
		
		if (systemFeedback.equals("Successfully parsed")) {
			boolean isAdditionOfNewTaskSuccessful = addThisTask(currentTask);
			if (isAdditionOfNewTaskSuccessful) {
				systemFeedback = "Successfully added \"" + currentTask.getName() + "\".";
			}
			else {
				systemFeedback = "Unable to add \"" + currentTask.getName() + "\".";
			}
		}
	}
	
	protected String parseAndModifyTask(String userCommand, Task taskToBeModified, String operation) {
		String systemFeedback;
		boolean isParsable = true;
		boolean isSettingTaskTypeSuccessful;
		
		parser.extractTaskDetails(userCommand);
		
		String [] taskDetails = parser.taskDetails;
		if (parser.invalidFeedBack != null) {
			isParsable = false;
		}
		
		if (!isParsable) {
			systemFeedback = parser.invalidFeedBack;
		}
		else {
			setTaskDetails(taskToBeModified, taskDetails);
			isSettingTaskTypeSuccessful = setTaskType(taskToBeModified);
			
			if (!isSettingTaskTypeSuccessful) {
				systemFeedback = "Entering a timestamp without a date doesn't make sense!";
			}
			else {
				systemFeedback = "Successfully parsed";
			}
		}
		return systemFeedback;
	}

	private void setTaskDetails(Task task, String[] taskDetails) {
		if (taskDetails[0] != null) {
			task.setName(taskDetails[0]);
		}
		if (taskDetails[1] != null) {
			task.setStartTime(taskDetails[1]);
			task.setEndTime(taskDetails[2]);
		}
		if (taskDetails[3] != null) {
			task.setDate(taskDetails[3]);
		}
		if (taskDetails[4] != null) {
			if (taskDetails[4].equalsIgnoreCase("done")) {
				task.setIsDone(true);
			}
			else {
				task.setIsDone(false);
			}
		}
		if (taskDetails[5] != null) {
			task.setLocation(taskDetails[5]);
		}
	}

	private boolean setTaskType(Task task) {
		if (task.getDate() == null && task.getStartTime() == null && task.getEndTime() == null) {
			task.setType("noSetTiming");
			return true;
		} else if (task.getDate() != null && task.getStartTime() == null && task.getEndTime() == null) {
			task.setType("fullDay");
			return true;
		} else if (task.getDate() != null && task.getStartTime() != null && task.getEndTime() == null) {
			task.setType("targetedTime");
			return true;
		} else if (task.getDate() != null && task.getStartTime() != null && task.getEndTime() != null) {
			task.setType("setDuration");
			return true;
		} else {
			//the rest of the cases, e.g. entered deadline/duration
			//but never enter date
			return false;
		}
	}

	protected boolean addThisTask(Task taskToBeAdded) {
		allTasks.add(taskToBeAdded);
		
		boolean isFileWritingSuccessful = taskStorage.writeATaskToFile(taskToBeAdded, true);
		return isFileWritingSuccessful;
	}
	
	@Override
	public void undo() {
		allTasks.remove(currentTask);
		
		if (taskStorage.writeTaskListToFile()) {
			systemFeedback = "Undo previous addition: Successfully deleted \""+ currentTask.getName() + "\"";
		}
		else {
			systemFeedback = "Cannot undo!";
		}
	}
}
