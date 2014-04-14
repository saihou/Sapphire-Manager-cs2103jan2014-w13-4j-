//@author A0097812X
import java.util.ArrayList;

/*
 * Command pattern: assumes role of "Command interface", except that
 * it is not an interface, but an abstract class. This is because we
 * realized instead of just specifying an interface, there are a LOT
 * more common attributes and methods between the concrete commands,
 * such as systemFeedback, and currentTask and their respective Get
 * methods.
 */
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
	
	//to be implemented by all subclasses
	public abstract void execute(String s);
	
	//undo-able commands = add, edit and delete
	//undo-able commands should override this method
	public void undo() {
		return;
	}
	
	//edit command should override this method
	public Task getEditedTask() {
		return new Task();
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
}
