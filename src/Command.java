import java.util.ArrayList;

public abstract class Command {
	
	protected String systemFeedback;
	protected ArrayList<Task> currentTaskList;
	protected ArrayList<Task> allTasks;
	protected ActionHistory history;
	protected Storage taskStorage;
	protected Task task;
	
	public Command() {
		systemFeedback = "";
		history = ActionHistory.getInstance();
		taskStorage = Storage.getInstance();
		allTasks = taskStorage.getTaskList();
		currentTaskList = allTasks;
		task = null;
	}
	
	public Command(ArrayList<Task> current) {
		this();
		currentTaskList = current;
	}
	
	public abstract void execute(String s);
	
	public String getSystemFeedback() {
		return systemFeedback;
	}
	
	public ArrayList<Task> getCurrentTaskList() {
		return currentTaskList;
	}
	
	public void updateHistory(String lastAction, Task reference, Task duplicatedTask) {
		history.setLastAction(lastAction);
		history.setReferenceToLastTask(reference);
		history.setCopyOfLastTask(duplicatedTask);
	}
}
