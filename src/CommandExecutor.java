import java.util.ArrayList;
import java.util.Stack;

/**
 *   This class is the main executor that will execute the commands
 *   ADD, DISPLAY, EDIT, DELETE, UNDO
 */

public class CommandExecutor {

	protected Storage taskStorage;
	protected DateTimeConfiguration dateTimeConfig;
	protected ArrayList<Task> allTasks;
	protected ArrayList<Task> currentTaskList;

	protected Stack<Command> hist;
	
	private final String MESSAGE_INVALID_COMMAND = "No such command! Press F1 for help.";
	
	/**
	 * @author Sai Hou
	 */
	public CommandExecutor() {
		taskStorage = Storage.getInstance();
		dateTimeConfig = new DateTimeConfiguration();
		allTasks = taskStorage.getTaskList();
		currentTaskList = null;
		hist = new Stack<Command>();
	}
	
	public String doUserOperation(String userCommand) {
		String systemFeedback = "";
		String operation = "";
		
		operation = getOperation(userCommand);

		boolean isValidOperation = ValidationCheck.isValidOperation(operation);
		if (!isValidOperation) {
			return MESSAGE_INVALID_COMMAND;
			//return new Result(MESSAGE_INVALID_COMMAND);
		}

		if(isExit(operation)) {
			System.exit(0);
		}

		systemFeedback = executeOperation(operation, userCommand);

		return systemFeedback;
	}

	private String executeOperation(String operation, String userCommand) {
		String systemFeedback = "";
		Result result = null;
		
		userCommand = getSecondWordOnwards(userCommand);
		
		switch (operation) {
			case "create" :
				//fall through
			case "new" :
				//fall through
			case "add" :
				Command add = new AddCommand();
				add.execute(userCommand);
				systemFeedback = add.getSystemFeedback();
				result = add.getResult();
				hist.push(add);
				
				DisplayCommand disp = new DisplayCommand();
				disp.setTaskToHighlight(add.getCurrentTask());
				disp.execute("undone");
				break;
				
			case "show" :
				//fall through
			case "display" :
				Command display = new DisplayCommand();
				display.execute(userCommand);
				systemFeedback = display.getSystemFeedback();
				result = display.getResult();
				currentTaskList = display.getCurrentTaskList();
				break;
				
			case "update" :
				//fall through
			case "edit" :
				Command edit = new EditCommand(currentTaskList);
				edit.execute(userCommand);
				systemFeedback = edit.getSystemFeedback();
				result = edit.getResult();
				hist.push(edit);
				
				disp = new DisplayCommand();
				disp.setTaskToHighlight(edit.getEditedTask());
				disp.execute("undone");
				break;
				
	        case "del" :
	        	//fall through
			case "remove" :
				//fall through
			case "delete" :
	        	Command delete = new DeleteCommand(currentTaskList);
	        	delete.execute(userCommand);
	        	result = delete.getResult();
	        	systemFeedback = delete.getSystemFeedback();
	        	hist.push(delete);
				break;
				
			case "clear" :
				Command clear = new ClearCommand();
				clear.execute(userCommand);
				result = clear.getResult();
				systemFeedback = clear.getSystemFeedback();
				break;
				
			case "find" :
				//fall through
			case "search" :
				Command search = new SearchCommand(currentTaskList);
				search.execute(userCommand);
				result = search.getResult();
				systemFeedback = search.getSystemFeedback();
				currentTaskList = search.getCurrentTaskList();
				break;
				
			case "undo" :
				if (!hist.empty()) {
					Command lastCommand = hist.pop();
					lastCommand.undo();
					result = lastCommand.getResult();
					systemFeedback = lastCommand.getSystemFeedback();
				} else {
					result = new Result();
					systemFeedback = "Nothing to undo!";
				}
				break;
				
			default : 
				//assert false here
				//if program reaches here, it should fail because
				//previously, already checked for valid operation
				assert false;
				break;
			}
		return systemFeedback;
	}
	
	private boolean isExit(String operation) {
		return operation.equalsIgnoreCase("exit") || operation.equalsIgnoreCase("quit");
	}
	
	private String getOperation(String userCommand) {
		return getFirstWord(userCommand);
	}
	
	private String getFirstWord(String str) {
		return str.trim().split("\\s+")[0];
	}
	
	private String getSecondWordOnwards(String str) {
		String [] individualWordsInArray = str.split(" ");
		StringBuilder sb = new StringBuilder();
		boolean isFirstWord = true;
		
		for (String word: individualWordsInArray) {
			if (isFirstWord) {
				isFirstWord = false;
				continue;
			}
			sb.append(word + " ");
		}
		return sb.toString().trim();
	}
	
	//only used for JUnitTesting!
	public String jUnitAutomatedTest() {
		//basically combining all the println from various objects (UI, Task)
		//and appending them to a StringBuilder. At the end, the StringBuilder is
		//converted to a String and returned so that JUnit can assertEquals();
		
		StringBuilder sb = new StringBuilder();
		int count = 1;
		for(Task task : allTasks){
			sb.append(count+ ". ");
			sb.append(task.getAllTaskDetails());
		}
		
		return sb.toString();
		//return executeDisplayCommand("undone");
	}
}
