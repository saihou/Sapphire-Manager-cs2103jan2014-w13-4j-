//@author A0097812X

/*
 * Command pattern: This is one of the concrete commands.
 * 
 * Description: This class will handle the add operation.
 */
public class AddCommand extends Command {
	
	private static final int INDICATOR_TASK_NAME = 0;
	private static final int INDICATOR_START_TIME = 1;
	private static final int INDICATOR_END_TIME = 2;
	private static final int INDICATOR_DATE = 3;
	private static final int INDICATOR_STATUS = 4;
	private static final int INDICATOR_LOCATION = 5;
	
	private static final String TASK_TYPE_NO_SET_TIMING = "noSetTiming";
	private static final String TASK_TYPE_FULL_DAY = "fullDay";
	private static final String TASK_TYPE_TARGETED_TIME = "targetedTime";
	private static final String TASK_TYPE_SET_DURATION = "setDuration";
	
	private static final String ERROR_CANNOT_UNDO = "ERROR: Cannot undo!";
	private static final String ERROR_ENTERING_TIMESTAMP_WITHOUT_DATE = "ERROR: Entering a timestamp without a date doesn't make sense!";
	private static final String ERROR_UNABLE_TO_ADD = "ERROR: Unable to add \"%s\".";
	
	private static final String SUCCESS_ADD = "Successfully added \"%s\".";
	private static final String SUCCESS_UNDO = "Undo previous addition: Successfully deleted \"%s\".";
	
	private static final String PLACEHOLDER_PARSE_SUCCESS = "parsing success";
	
	private static final String ADD = "add";
	
	public CommandParser parser = null;
	
	public AddCommand() {
		super();
		parser = new CommandParser();
	}
	
	@Override
	public void execute(String userCommand) {
		userCommand = userCommand.trim();
		currentTask = new Task();
		
		systemFeedback = parseAndModifyTask(userCommand, currentTask, ADD);
		
		//if parsing is successful
		if (systemFeedback.equals(PLACEHOLDER_PARSE_SUCCESS)) {
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
			systemFeedback = formatString(SUCCESS_ADD, currentTask.getName());
		}
		else {
			systemFeedback = formatString(ERROR_UNABLE_TO_ADD, currentTask.getName());
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
			systemFeedback = ERROR_ENTERING_TIMESTAMP_WITHOUT_DATE;
		}
		else {
			systemFeedback = PLACEHOLDER_PARSE_SUCCESS;
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
		
		if (caller.equals(ADD)) {
			parser.extractTaskDetailsForAdd(userCommand);
		} else {
			parser.extractTaskDetailsForEdit(userCommand);
		}
		
		taskDetails = parser.taskDetails;
		return taskDetails;
	}

	//@author Cai Di
	private void setTaskDetails(Task task, String[] taskDetails) {
		if (taskDetails[INDICATOR_TASK_NAME] != null) {
			task.setName(taskDetails[INDICATOR_TASK_NAME]);
		}
		if (taskDetails[INDICATOR_START_TIME] != null) {
			task.setStartTime(taskDetails[INDICATOR_START_TIME]);
			task.setEndTime(taskDetails[INDICATOR_END_TIME]);
		}
		if (taskDetails[INDICATOR_DATE] != null) {
			task.setDate(taskDetails[INDICATOR_DATE]);
		}
		if (taskDetails[INDICATOR_STATUS] != null) {
			if (taskDetails[INDICATOR_STATUS].equalsIgnoreCase("done")) {
				task.setIsDone(true);
			}
			else {
				task.setIsDone(false);
			}
		}
		if (taskDetails[INDICATOR_LOCATION] != null) {
			task.setLocation(taskDetails[INDICATOR_LOCATION]);
		}
	}

	//@author A0097812X
	protected boolean setTaskType(Task task) {
		if (task.getDate() == null && task.getStartTime() == null && task.getEndTime() == null) {
			task.setType(TASK_TYPE_NO_SET_TIMING);
			return true;
		} else if (task.getDate() != null && task.getStartTime() == null && task.getEndTime() == null) {
			task.setType(TASK_TYPE_FULL_DAY);
			return true;
		} else if (task.getDate() != null && task.getStartTime() != null && task.getEndTime() == null) {
			task.setType(TASK_TYPE_TARGETED_TIME);
			return true;
		} else if (task.getDate() != null && task.getStartTime() != null && task.getEndTime() != null) {
			task.setType(TASK_TYPE_SET_DURATION);
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
			systemFeedback = formatString(SUCCESS_UNDO, currentTask.getName());
		}
		else {
			systemFeedback = ERROR_CANNOT_UNDO;
		}
		result.setSystemFeedback(systemFeedback);
	}
	
	private String formatString(String message, String arg) {
		return String.format(message, arg);
	}
}
