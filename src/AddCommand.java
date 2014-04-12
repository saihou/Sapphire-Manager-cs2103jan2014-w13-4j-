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
		
		//if parsing is successful
		if (systemFeedback.equals("parsing success")) {
			add();
		} else {
			//do nothing; parsing error message is already contained in systemFeedback
		}
		
		result.setSystemFeedback(systemFeedback);
	}

	private void add() {
		boolean isAdditionOfNewTaskSuccessful = addThisTask(currentTask);
		if (isAdditionOfNewTaskSuccessful) {
			result.setSuccess(true);
			systemFeedback = "Successfully added \"" + currentTask.getName() + "\".";
		}
		else {
			systemFeedback = "ERROR: Unable to add \"" + currentTask.getName() + "\".";
		}
	}
	
	protected String parseAndModifyTask(String userCommand, Task taskToBeModified, String caller) {
		String systemFeedback;
		boolean isParsable = true;
		String [] taskDetails;
		
		taskDetails = parseUserCommand(userCommand, caller);
		isParsable = checkParsability(isParsable);
		
		if (!isParsable) {
			systemFeedback = parser.invalidFeedBack;
		} else {
			systemFeedback = proceedToTaskModification(taskToBeModified, taskDetails);
		}
		
		return systemFeedback;
	}

	private String proceedToTaskModification(Task taskToBeModified,
			String[] taskDetails) {
		String systemFeedback;
		boolean isModificationSuccessful;
		isModificationSuccessful = modifyTask(taskToBeModified, taskDetails);
		
		if (!isModificationSuccessful) {
			systemFeedback = "ERROR: Entering a timestamp without a date doesn't make sense!";
		}
		else {
			systemFeedback = "parsing success";
		}
		return systemFeedback;
	}

	private boolean modifyTask(Task taskToBeModified, String[] taskDetails) {
		boolean isSettingTaskTypeSuccessful;
		setTaskDetails(taskToBeModified, taskDetails);
		isSettingTaskTypeSuccessful = setTaskType(taskToBeModified);
		return isSettingTaskTypeSuccessful;
	}

	private boolean checkParsability(boolean isParsable) {
		if (parser.invalidFeedBack != null) {
			isParsable = false;
		}
		return isParsable;
	}

	private String[] parseUserCommand(String userCommand, String caller) {
		String[] taskDetails;
		
		if (caller.equals("add")) {
			parser.extractTaskDetailsForAdd(userCommand);
		} else {
			parser.extractTaskDetailsForEdit(userCommand);
		}
		
		taskDetails = parser.taskDetails;
		return taskDetails;
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

	protected boolean setTaskType(Task task) {
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
			systemFeedback = "ERROR: Cannot undo!";
		}
		result.setSystemFeedback(systemFeedback);
	}
}
