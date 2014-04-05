import java.util.ArrayList;

public abstract class Command {
	
	protected String systemFeedback;
	protected Task currentTask;
	protected ArrayList<Task> currentTaskList;
	protected ArrayList<Task> allTasks;
	protected Storage taskStorage;
	protected Result result;
	
	public Command() {
		systemFeedback = "";
		taskStorage = Storage.getInstance();
		allTasks = taskStorage.getTaskList();
		currentTaskList = allTasks;
		currentTask = null;
		result = new Result();
	}
	
	public Command(ArrayList<Task> current) {
		this();
		currentTaskList = current;
	}
	
	//to be implemented
	public abstract void execute(String s);
	
	//undo-able commands = add, edit and delete
	//undo-able commands should override this method
	public void undo() {
		return;
	}
	
	public String getSystemFeedback() {
		return systemFeedback;
	}
	
	public Result getResult() {
		return result;
	}
	
	public ArrayList<Task> getCurrentTaskList() {
		return currentTaskList;
	}
	
	public Task getCurrentTask() {
		return currentTask;
	}
	
	//edit command should override this method
	public Task getEditedTask() {
		return new Task();
	}
}
