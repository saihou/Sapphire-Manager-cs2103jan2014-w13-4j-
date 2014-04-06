import java.util.ArrayList;
import java.util.Collections;


public class DisplayCommand extends Command {

	protected CommandParser parser;
	protected DateTimeConfiguration dateTimeConfig;
	
	protected Task taskToHighlight;
	
	private final static String MESSAGE_INVALID_COMMAND = "Invalid command entered. Please try again.";
	
	private final static String HEADING_OVERDUE = "Overdue Tasks:\n";
	private final static String HEADING_TODAY = "Today's Tasks:\n"; 
	private final static String HEADING_THIS_WEEK = "Tasks Occurring/Due Within This Week:\n";
	private final static String HEADING_AFTER_A_WEEK = "Tasks Occurring/Due More Than A Week Later:\n";
	private final static String HEADING_MEMO = "Memos:\n";
	private final static String HEADING_COMPLETED = "Completed Tasks:\n";
	
	private final static String FEEDBACK_NO_OVERDUE = "You have no overdue tasks.";
	private final static String FEEDBACK_NO_MEMOS = "You have no memos.";
	private final static String FEEDBACK_NO_DONE = "You have no completed tasks.";
	private final static String FEEDBACK_NO_TODAY = "You have no tasks for today.";
	private final static String FEEDBACK_NO_UNDONE = "You have no uncompleted tasks.";
	private final static String FEEDBACK_NO_TASKS = "You have no tasks.";
	
	public DisplayCommand() {
		super();
		dateTimeConfig = new DateTimeConfiguration();
	}
	
	@Override
	public void execute(String userCommand) {
		parser = new CommandParser();
		executeDisplayCommand(userCommand);
		
		result.setSystemFeedback(systemFeedback);
	}
	
	/**
	 * @author Si Rui
	 */
	public void executeDisplayCommand(String userCommand) {
		systemFeedback = "";
		if (allTasks.isEmpty()) {
			systemFeedback = FEEDBACK_NO_TASKS;
		} else {
			String displayType = parser.parseDisplayType(userCommand);
			assert(displayType != null);
			
			try {
				prepareCurrentTaskList(displayType);
				
				if (currentTaskList.isEmpty()) {
					systemFeedback = getFeedbackIfHaveNoTasks(displayType);
				}

				if (systemFeedback.equals("")) {
					formDisplayText(result);
					systemFeedback = getFeedbackIfHaveTasks(displayType);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				systemFeedback = ex.getMessage();
			} 
		}
	}

	private boolean prepareCurrentTaskList(String displayType) throws IllegalArgumentException {
		Collections.sort(allTasks);

		//Set currentTaskList to default: all undone tasks
		boolean isDone = false;
		currentTaskList = getTasksBasedOnCompletion(allTasks, isDone);

		switch (displayType) {
		case "all" : 
			//Append completed tasks at the end of list
			isDone = true;
			ArrayList<Task> completedTasks = getTasksBasedOnCompletion(allTasks, isDone);
			currentTaskList.addAll(completedTasks);
			break;
		case "overdue" :
			currentTaskList = getOverdue(currentTaskList);
			break;
		case "memos" :
			currentTaskList = getMemos(currentTaskList);
			break;
		case "today" :
			currentTaskList = getTodaysTasks();
			break;
		case "undone" :
			break;
		case "done" :
			isDone = true;
			currentTaskList = getTasksBasedOnCompletion(allTasks, isDone);
			break;
		default :
			throw new IllegalArgumentException(MESSAGE_INVALID_COMMAND);
		}
		return true;
	}
	
	private ArrayList<Task> getOverdue(ArrayList<Task> taskList){
		ArrayList<Task> matchedTasks = new ArrayList<Task>();
		String todaysDate = dateTimeConfig.getTodaysDate();
		for (Task t : taskList) {
			String taskDate = t.getDate();
			if (taskDate != null && isOverdueTask(taskDate, todaysDate)) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
	}
	
	private ArrayList<Task> getMemos(ArrayList<Task> taskList){
		ArrayList<Task> matchedTasks = new ArrayList<Task>();
		for (Task t : taskList) {
			if (isMemo(t)) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
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
	
	private String getFeedbackIfHaveTasks(String displayType) {
		String feedback = "";
		if (displayType.equals("done")) {
			feedback = "Displaying completed tasks.";
		} else if (displayType.equals("today")) {
			feedback = "Displaying tasks for today.";
		} else if (displayType.equals("undone")) {
			feedback = "Displaying undone tasks.";
		}  else if (displayType.equals("all")) {
			feedback = "Displaying all tasks.";
		} else if (displayType.equals("memos")) {
			feedback = "Displaying memos.";
		} else if (displayType.equals("overdue")) {
			feedback = "Displaying overdue tasks.";
		}
		
		return feedback;
	}
	
	private String getFeedbackIfHaveNoTasks(String displayType) {
		String feedback = "";
		if (displayType.equals("overdue")) {
			feedback = FEEDBACK_NO_OVERDUE;
		} else if (displayType.equals("memos")) {
			feedback = FEEDBACK_NO_MEMOS;
		} else if (displayType.equals("done")) {
			feedback = FEEDBACK_NO_DONE;
		} else if (displayType.equals("today")) {
			feedback = FEEDBACK_NO_TODAY;
		} else if (displayType.equals("undone")) {
			feedback = FEEDBACK_NO_UNDONE;
		}  else if (displayType.equals("all")) {
			feedback = "You have no tasks.";
		} else if (displayType.equals("memos")) {
			feedback = "You have no memos.";
		} else if (displayType.equals("overdue")) {
			feedback = "You have no overdue tasks.";
		}
		
		return feedback;
	}

	protected String formDisplayText(Result result) {
		
		ArrayList<Task> uncompletedTasks = getTasksBasedOnCompletion(currentTaskList, false);
		ArrayList<Task> completedTasks = getTasksBasedOnCompletion(currentTaskList, true);
		
		String displayText = "";
		int numberOfUncompletedTasks = 0;
		if (!uncompletedTasks.isEmpty()) {
			displayText += formDisplayTextUncompletedTasks(uncompletedTasks, result);
			numberOfUncompletedTasks = uncompletedTasks.size();
		}
		if (!completedTasks.isEmpty()) {
			displayText += formDisplayTextCompletedTasks(completedTasks, numberOfUncompletedTasks, result);
		}
		
		//result.printResult();
		
		return displayText;
	}

	private String formDisplayTextUncompletedTasks(ArrayList<Task> taskList, Result result) {
		String displayText = "";
		String todaysDate = dateTimeConfig.getTodaysDate();

		boolean isPrintingOverdue = false;
		boolean isPrintingToday = false;
		boolean isPrintingWeek = false;
		boolean isPrintingAfterAWeek = false;
		boolean isPrintingMemos = false;

		int count = 1;
		
		for (Task t : taskList) {
			String taskDate = t.getDate();
			if (isMemo(t)) {
				if (!isPrintingMemos) {
					displayText += '\n' + HEADING_MEMO;
					isPrintingMemos = true;
					
					result.savePreviousHeading();
					result.pushNewHeadingText(HEADING_MEMO);
				}
			} else if (isOverdueTask(taskDate, todaysDate) && !isPrintingOverdue) {
				displayText += HEADING_OVERDUE;
				isPrintingOverdue = true;
				
				result.savePreviousHeading();
				result.pushNewHeadingText(HEADING_OVERDUE);
				
			} else if (isTodaysTask(taskDate, todaysDate) && !isPrintingToday) {
				displayText += '\n' + HEADING_TODAY;
				isPrintingToday = true;
				
				result.savePreviousHeading();
				result.pushNewHeadingText(HEADING_TODAY);
				
			} else if (isThisWeeksButNotTodaysTask(taskDate, todaysDate) && !isPrintingWeek) {
				displayText += '\n' + HEADING_THIS_WEEK;
				isPrintingWeek = true;
				
				result.savePreviousHeading();
				result.pushNewHeadingText(HEADING_THIS_WEEK);
			} else if (isAfterThisWeeksTask(taskDate, todaysDate) && !isPrintingAfterAWeek) {
				displayText += '\n' + HEADING_AFTER_A_WEEK;
				isPrintingAfterAWeek = true;
				
				result.savePreviousHeading();
				result.pushNewHeadingText(HEADING_AFTER_A_WEEK);
			}
			
			
			
			displayText += formDisplayTextOfOneTask(count, t);
			result.pushTaskToCurrentHeading(formDisplayTextOfOneTask(count, t));
			count++;
			
			if (t == taskToHighlight) {
				result.saveHighlightIndex();
				result.printHighlightIndex();
			}
		}
		
		result.savePreviousHeading();
		return displayText;
	}
	
	private String formDisplayTextCompletedTasks(ArrayList<Task> taskList, int continueNumbering, Result result) {
		String displayText = "";
		
		if (continueNumbering == 0) {
			displayText += HEADING_COMPLETED;
			
			result.savePreviousHeading();
			result.pushNewHeadingText(HEADING_COMPLETED);
		} else {
			displayText += '\n' + HEADING_COMPLETED;
			
			result.savePreviousHeading();
			result.pushNewHeadingText('\n' + HEADING_COMPLETED);
		}
		
		for (int i = 0; i < taskList.size(); i++) {
			displayText += formDisplayTextOfOneTask((i+1)+continueNumbering, taskList.get(i));
			result.pushTaskToCurrentHeading(formDisplayTextOfOneTask((i+1)+continueNumbering, taskList.get(i)));
		}
		
		result.savePreviousHeading();
		return displayText;
	}
	
	private String formDisplayTextOfOneTask(int count, Task t) {
		return "   " + count + ". " + t.getAllTaskDetails();
	}
	
	private boolean isMemo(Task t) {
		String taskType = t.getType();
		return (taskType.equals("noSetTiming")) ? true : false;
	}

	private boolean isOverdueTask(String taskD, String todaysD) {
		String taskDate = dateTimeConfig.reverseDate(taskD);
		String todaysDate = dateTimeConfig.reverseDate(todaysD);
		return (taskDate.compareTo(todaysDate) < 0) ? true : false;
	}

	private boolean isTodaysTask(String taskDate, String todaysDate) {
		return (taskDate.equals(todaysDate)) ? true : false;
	}

	private boolean isThisWeeksButNotTodaysTask(String taskDate, String todaysDate) {
		return (dateTimeConfig.isThisWeekButNotToday(taskDate, todaysDate)) ? true : false;
	}

	private boolean isAfterThisWeeksTask(String taskDate, String todaysDate) {
		return (dateTimeConfig.isAfterAWeek(taskDate, todaysDate)) ? true : false;
	}

	public ArrayList<Task> getTodaysTasks(){
		ArrayList<Task> matchedTasks = new ArrayList<Task>();
		String todaysDate = dateTimeConfig.getTodaysDate();

		for (Task t : allTasks) {
			String taskDate = t.getDate();
			if (taskDate != null && dateTimeConfig.isPastOrToday(taskDate, todaysDate)) {
				matchedTasks.add(t);
			}
		}
		matchedTasks = getTasksBasedOnCompletion(matchedTasks, false);

		return matchedTasks;
	}
	public void setTaskToHighlight(Task task) {
		taskToHighlight = task;
	}
}
