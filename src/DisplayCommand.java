//@author A0101252A
import java.util.ArrayList;
import java.util.Collections;

/*
* Command pattern: This is one of the concrete commands.
* 
* Description: This class will handle the display operations.
* Display operations include display (default: undone tasks), all, overdue,
* today, done, undone, memos.
*/
public class DisplayCommand extends Command {

	/* Instances of other classes used: Parser for parsing, 
	 * dateTimeConfig for getting date/time information, and 
	 * taskToHighlight to process tasks. */
	protected CommandParser parser;
	protected DateTimeConfiguration dateTimeConfig;
	
	protected Task taskToHighlight;
	
	/* Error messages */ 
	private final static String MESSAGE_INVALID_COMMAND = "ERROR: Invalid command entered. Please try again.";
	
	/* Headings for displaying tasks */ 
	private final static String HEADING_OVERDUE = "Overdue Tasks:\n";
	private final static String HEADING_TODAY = "Today's Tasks:\n"; 
	private final static String HEADING_THIS_WEEK = "Tasks Occurring/Due Within 7 Days:\n";
	private final static String HEADING_AFTER_A_WEEK = "Tasks Occurring/Due More Than A Week Later:\n";
	private final static String HEADING_MEMO = "Memos:\n";
	private final static String HEADING_COMPLETED = "Completed Tasks:\n";
	
	/* Feedback when there are no tasks in manager */
	private final static String FEEDBACK_NO_OVERDUE = "You have no overdue tasks.";
	private final static String FEEDBACK_NO_TODAY = "You have no tasks for today.";
	private final static String FEEDBACK_NO_DONE = "You have no completed tasks.";
	private final static String FEEDBACK_NO_UNDONE = "You have no uncompleted tasks.";
	private final static String FEEDBACK_NO_MEMOS = "You have no memos.";
	private final static String FEEDBACK_NO_TASKS = "You have no tasks.";
	
	/* Feedback for various displays when there are existing tasks */ 
	private final static String FEEDBACK_DISPLAY_ALL = "Displaying all tasks.";
	private final static String FEEDBACK_DISPLAY_OVERDUE = "Displaying overdue tasks.";
	private final static String FEEDBACK_DISPLAY_TODAY = "Displaying tasks for today.";
	private final static String FEEDBACK_DISPLAY_DONE = "Displaying completed tasks.";
	private final static String FEEDBACK_DISPLAY_UNDONE = "Displaying uncompleted tasks.";
	private final static String FEEDBACK_DISPLAY_MEMOS = "Displaying memos.";

	/* Pre-defined spacing for formatting purposes */
	private final static String TASK_NUMBER_SPACING = "   ";
	
	/* For identifying display type */
	private final static String ALL = "all";
	private final static String OVERDUE = "overdue";
	private final static String TODAY = "today";
	private final static String DONE = "done";
	private final static String UNDONE = "undone";
	private final static String MEMOS = "memos";
	
	public DisplayCommand() {
		super();
		dateTimeConfig = new DateTimeConfiguration();
	}
	
	/*
	 * Calls execute display command.
	 * @see Command#execute(java.lang.String)
	 */
	@Override
	public void execute(String userCommand) {
		parser = new CommandParser();
		executeDisplayCommand(userCommand);
		result.setSystemFeedback(systemFeedback);
	}
	
	/*
	 * Executes display command by setting currentTaskList to the list of task
	 * that matches the display required, forming the display result and also
	 * setting the systemFeedback.
	 * 
	 * @param userCommand A valid command input from user
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

	/* 
	 * Sorts the entire task list and sets currentTaskList to a list of tasks that 
	 * contains only the tasks required for display, based on displayType.
	 * 
	 * @param	displayType		Type of display requested by user (e.g. all, overdue, today etc.)
	 * @return 					true if task list is successfully set
	 * @throws	IllegalArgumentException If displayType is not recognized
	 */
	private boolean prepareCurrentTaskList(String displayType) throws IllegalArgumentException {
		Collections.sort(allTasks);

		//Set currentTaskList to default: all undone tasks
		boolean isDone = false;
		currentTaskList = getTasksBasedOnCompletion(allTasks, isDone);

		switch (displayType) {
			case ALL : 
				//Append completed tasks at the end of list
				isDone = true;
				ArrayList<Task> completedTasks = getTasksBasedOnCompletion(allTasks, isDone);
				currentTaskList.addAll(completedTasks);
				break;
			case OVERDUE :
				currentTaskList = getOverdue(currentTaskList);
				break;
			case MEMOS :
				currentTaskList = getMemos(currentTaskList);
				break;
			case TODAY :
				currentTaskList = getTodaysTasks();
				break;
			case UNDONE :
				break;
			case DONE :
				isDone = true;
				currentTaskList = getTasksBasedOnCompletion(allTasks, isDone);
				break;
			default :
				throw new IllegalArgumentException(MESSAGE_INVALID_COMMAND);
		}
		return true;
	}
	
	/*
	 * @param	taskList	currentTaskList as prepared previously
	 * @return				An ArrayList that contains only overdue items from taskList
	 */
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
	
	/*
	 * @param	taskList	currentTaskList as prepared previously
	 * @return				An ArrayList that contains only memos from taskList
	 */
	private ArrayList<Task> getMemos(ArrayList<Task> taskList){
		ArrayList<Task> matchedTasks = new ArrayList<Task>();
		for (Task t : taskList) {
			if (isMemo(t)) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
	}

	/*
	 * Generates an ArrayList of tasks that contains tasks based on completion status, 
	 * as specified by statusRequested
	 * 
	 * @param	taskList		currentTaskList as prepared previously
	 * @param	statusRequested	Is done (true) or not done (false)
	 * @return					An ArrayList that contains only items with matching status from taskList
	 */
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
	
	/*
	 * @return 	An ArrayList that contains tasks dated today
	 */
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
	
	/*
	 * @return	Feedback when there are tasks to be displayed
	 */
	private String getFeedbackIfHaveTasks(String displayType) {
		String feedback = "";
		if (displayType.equals(DONE)) {
			feedback = FEEDBACK_DISPLAY_DONE;
		} else if (displayType.equals(TODAY)) {
			feedback = FEEDBACK_DISPLAY_TODAY;
		} else if (displayType.equals(UNDONE)) {
			feedback = FEEDBACK_DISPLAY_UNDONE;
		}  else if (displayType.equals(ALL)) {
			feedback = FEEDBACK_DISPLAY_ALL;
		} else if (displayType.equals(MEMOS)) {
			feedback = FEEDBACK_DISPLAY_MEMOS;
		} else if (displayType.equals(OVERDUE)) {
			feedback = FEEDBACK_DISPLAY_OVERDUE;
		}
		
		return feedback;
	}
	
	/*
	 * @return	Feedback when there are no tasks to be displayed
	 */
	private String getFeedbackIfHaveNoTasks(String displayType) {
		String feedback = "";
		if (displayType.equals(OVERDUE)) {
			feedback = FEEDBACK_NO_OVERDUE;
		} else if (displayType.equals(MEMOS)) {
			feedback = FEEDBACK_NO_MEMOS;
		} else if (displayType.equals(DONE)) {
			feedback = FEEDBACK_NO_DONE;
		} else if (displayType.equals(TODAY)) {
			feedback = FEEDBACK_NO_TODAY;
		} else if (displayType.equals(UNDONE)) {
			feedback = FEEDBACK_NO_UNDONE;
		}  else if (displayType.equals(ALL)) {
			feedback = "You have no tasks.";
		}
		
		return feedback;
	}

	/*
	 * @return	Text to be display on screen
	 */
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
		
		return displayText;
	}

	/*
	 * Generates text to show on display screen and saves it using the Result class.
	 * 
	 * @param	taskList	List of tasks to generate display text from
	 * @param 	result		Stores display text in separate components (headings/task details)
	 * @return				String of text to display
	 */
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
					
					saveResultHeadings(result, HEADING_MEMO);
				}
			} else if (isOverdueTask(taskDate, todaysDate) && !isPrintingOverdue) {
				displayText += HEADING_OVERDUE;
				isPrintingOverdue = true;
				
				saveResultHeadings(result, HEADING_OVERDUE);
				
			} else if (isTodaysTask(taskDate, todaysDate) && !isPrintingToday) {
				displayText += '\n' + HEADING_TODAY;
				isPrintingToday = true;
				
				saveResultHeadings(result, HEADING_TODAY);
				
			} else if (isThisWeeksButNotTodaysTask(taskDate, todaysDate) && !isPrintingWeek) {
				displayText += '\n' + HEADING_THIS_WEEK;
				isPrintingWeek = true;
				
				saveResultHeadings(result, HEADING_THIS_WEEK);

			} else if (isAfterThisWeeksTask(taskDate, todaysDate) && !isPrintingAfterAWeek) {
				displayText += '\n' + HEADING_AFTER_A_WEEK;
				isPrintingAfterAWeek = true;
				
				saveResultHeadings(result, HEADING_AFTER_A_WEEK);
			}
			
			displayText += formDisplayTextOfOneTask(count, t);
			result.pushTaskToCurrentHeading(formDisplayTextOfOneTask(count, t));
			count++;
			
			if (t == taskToHighlight) {
				result.saveHighlightIndex();
			}
		}
		
		result.savePreviousHeading();
		return displayText;
	}
	
	/*
	 * Sub-method that generates text to show on display screen for completed tasks only. Completed tasks 
	 * either appear alone on the screen or are appended at the bottom of the list on screen.
	 * 
	 * @param	taskList			List of tasks to generate display text from
	 * @param	continueNumbering	0 = completed tasks appear alone, otherwise at the bottom of list
	 * @param 	result				Stores display text in separate components (headings/task details)
	 * @return						String of text to display
	 */
	private String formDisplayTextCompletedTasks(ArrayList<Task> taskList, int continueNumbering, Result result) {
		String displayText = "";
		
		if (continueNumbering == 0) {
			displayText += HEADING_COMPLETED;
			
			saveResultHeadings(result, HEADING_COMPLETED);
		} else {
			displayText += '\n' + HEADING_COMPLETED;
			
			saveResultHeadings(result, HEADING_COMPLETED);
		}
		
		for (int i = 0; i < taskList.size(); i++) {
			displayText += formDisplayTextOfOneTask((i+1)+continueNumbering, taskList.get(i));
			result.pushTaskToCurrentHeading(formDisplayTextOfOneTask((i+1)+continueNumbering, taskList.get(i)));
		}
		
		result.savePreviousHeading();
		return displayText;
	}
	
	private String formDisplayTextOfOneTask(int count, Task t) {
		return TASK_NUMBER_SPACING + count + ". " + t.getAllTaskDetails();
	}
	
	/*
	 * Called method in Result class that saves existing contents before starting a new heading.
	 * 
	 * @param 	result	Stores display text in separate components (headings/task details)
	 * @param	heading	New heading
	 */
	private void saveResultHeadings(Result result, String heading) {
		result.savePreviousHeading();
		result.pushNewHeadingText(heading);
	}
	
	/*
	 * Checks if a task t is a memo.
	 * 
	 * @param t		Task to be checked
	 */
	private boolean isMemo(Task t) {
		String taskType = t.getType();
		return (taskType.equals("noSetTiming")) ? true : false;
	}

	/*
	 * Checks if a task is overdue given the task's date and today's date.
	 * 
	 * @param	taskD 	Tasks's date
	 * @param	todaysD	Today's date
	 */
	private boolean isOverdueTask(String taskD, String todaysD) {
		String taskDate = dateTimeConfig.reverseDate(taskD);
		String todaysDate = dateTimeConfig.reverseDate(todaysD);
		return (taskDate.compareTo(todaysDate) < 0) ? true : false;
	}

	/*
	 * Checks if a task occurs on today given the task's date and today's date.
	 * 
	 * @param	taskD 	Tasks's date
	 * @param	todaysD	Today's date
	 */
	private boolean isTodaysTask(String taskDate, String todaysDate) {
		return (taskDate.equals(todaysDate)) ? true : false;
	}

	/*
	 * Checks if a task is within the week (up to 6 days from today), but also does not fall on today,
	 * given the task's date and today's date.
	 * 
	 * @param	taskD 	Tasks's date
	 * @param	todaysD	Today's date
	 */
	private boolean isThisWeeksButNotTodaysTask(String taskDate, String todaysDate) {
		return (dateTimeConfig.isThisWeekButNotToday(taskDate, todaysDate)) ? true : false;
	}

	/*
	 * Checks if a task occurs more than 7 days from today, given the task's date and today's date.
	 * 
	 * @param	taskD 	Tasks's date
	 * @param	todaysD	Today's date
	 */
	private boolean isAfterThisWeeksTask(String taskDate, String todaysDate) {
		return (dateTimeConfig.isAfterAWeek(taskDate, todaysDate)) ? true : false;
	}
	
	public void setTaskToHighlight(Task task) {
		taskToHighlight = task;
	}
}
