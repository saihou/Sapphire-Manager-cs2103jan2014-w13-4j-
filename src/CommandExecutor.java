import java.util.ArrayList;

/**
 *   This class is the main executor that will execute the commands
 *   ADD, DISPLAY, EDIT, DELETE, UNDO
 */

public class CommandExecutor {

	protected ArrayList<Task> currentTaskList;
	protected ActionHistory<Command> history;
	
	private final String MESSAGE_INVALID_COMMAND = "No such command! Press F1 for help.";
	
	/**
	 * @author Sai Hou
	 */
	public CommandExecutor() {
		currentTaskList = null;
		history = new ActionHistory<Command>();
	}
	
	public Result doUserOperation(String userCommand) {
		Result result = null;
		String operation = "";
		
		operation = getOperation(userCommand);

		boolean isValidOperation = ValidationCheck.isValidOperation(operation);
		if (!isValidOperation) {
			//return MESSAGE_INVALID_COMMAND;
			return new Result(MESSAGE_INVALID_COMMAND);
		}

		if(isExit(operation)) {
			System.exit(0);
		}
		
		result = executeOperation(operation, userCommand);
		return result;
	}

	private Result executeOperation(String operation, String userCommand) {
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
				boolean success = result.isSuccessful();
				if (success) {
					history.push(add);
				}
				result = displayTaskListWithHighlightAfterAddCommand(systemFeedback, add);
				break;
				
			case "show" :
				//fall through
			case "display" :
				Command display = new DisplayCommand();
				display.execute(userCommand);
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
				success = result.isSuccessful();
				if (success) {
					history.push(edit);
				}
				
				result = displayTaskListWithHighlightAfterEditCommand(systemFeedback, edit);
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
				success = result.isSuccessful();
				if (success) {
					history.push(delete);
				}
				
				result = displayTaskListAfterCommand(systemFeedback);
				break;
				
			case "clear" :
				Command clear = new ClearCommand();
				clear.execute(userCommand);
				result = clear.getResult();
				systemFeedback = clear.getSystemFeedback();
				
				result = displayTaskListAfterCommand(systemFeedback);
				break;
				
			case "find" :
				//fall through
			case "search" :
				Command search = new SearchCommand(currentTaskList);
				search.execute(userCommand);
				result = search.getResult();
				currentTaskList = search.getCurrentTaskList();
				break;
				
			case "undo" :
				if (!history.isEmpty()) {
					Command lastCommand = history.pop();
					lastCommand.undo();
					result = lastCommand.getResult();
					systemFeedback = result.getSystemFeedback();
					result = displayTaskListWithHighlightAfterUndoCommand(systemFeedback, lastCommand);
				} else {
					result = displayTaskListAfterCommand("Nothing to undo!");
				}
				break;
				
			default : 
				//assert false here
				//if program reaches here, it should fail because
				//previously, already checked for valid operation
				assert false;
				break;
			}
		return result;
	}

	private Result displayTaskListAfterCommand(String systemFeedback) {
		Result result;
		DisplayCommand disp = new DisplayCommand();
		disp.execute("undone");
		result = disp.getResult();
		result.setSystemFeedback(systemFeedback);
		currentTaskList = disp.getCurrentTaskList();
		return result;
	}
	
	private Result displayTaskListWithHighlightAfterUndoCommand(
			String systemFeedback, Command undo) {
		Result result;
		DisplayCommand disp = new DisplayCommand();
		disp.setTaskToHighlight(undo.getCurrentTask());
		disp.execute("undone");
		result = disp.getResult();
		result.setSystemFeedback(systemFeedback);
		currentTaskList = disp.getCurrentTaskList();
		return result;
	}
	
	private Result displayTaskListWithHighlightAfterEditCommand(
			String systemFeedback, Command edit) {
		Result result;
		DisplayCommand disp = new DisplayCommand();
		disp.setTaskToHighlight(edit.getEditedTask());
		disp.execute("undone");
		result = disp.getResult();
		result.setSystemFeedback(systemFeedback);
		currentTaskList = disp.getCurrentTaskList();
		return result;
	}

	private Result displayTaskListWithHighlightAfterAddCommand(String systemFeedback,
			Command add) {
		Result result;
		DisplayCommand disp = new DisplayCommand();
		disp.setTaskToHighlight(add.getCurrentTask());
		disp.execute("undone");
		result = disp.getResult();
		result.setSystemFeedback(systemFeedback);
		currentTaskList = disp.getCurrentTaskList();
		return result;
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
		/*
		StringBuilder sb = new StringBuilder();
		int count = 1;
		for(Task task : allTasks){
			sb.append(count+ ". ");
			sb.append(task.getAllTaskDetails());
		}
		
		return sb.toString();
		*/
		//return executeDisplayCommand("undone");
		return "";
	}
}
