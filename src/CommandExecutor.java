//@author A0097812X
import java.util.ArrayList;

/*
 * Façade pattern: From the perspective of the GUI, it throws whatever
 * the user enters to this class, and waits for a result. The GUI does 
 * not know about any back-end logic. Thus, this class acts as a 
 * façade and hides the logic from the GUI.
 * 
 * Command pattern: This class is the "invoker" class. It will create 
 * Commands and then execute them. After executing, it will then store
 * them in a history (if the command is undo-able). When user wants
 * to undo, it will retrieve from the history and execute its 
 * undo method.
 * 
 * Description: This class is the only class that the GUI interacts with.
 * It will take in the user input and decide which Command to execute.
 */

public class CommandExecutor {
	private static final String ERROR_NOTHING_TO_UNDO = "ERROR: Nothing to undo!";
	private static final String ERROR_INVALID_COMMAND = "ERROR: No such command! Press F1 for help.";
	
	private static final String CREATE = "create";
	private static final String NEW = "new";
	private static final String ADD = "add";
	private static final String SHOW = "show";
	private static final String DISPLAY = "display";
	private static final String UPDATE = "update";
	private static final String EDIT = "edit";
	private static final String DEL = "del";
	private static final String REMOVE = "remove";
	private static final String DELETE = "delete";
	private static final String CLEAR = "clear";
	private static final String FIND = "find";
	private static final String SEARCH = "search";
	private static final String UNDO = "undo";
	private static final String EXIT = "exit";
	private static final String QUIT = "quit";
	
	private static final String UNDONE = "undone";
	
	protected ArrayList<Task> currentTaskList;
	protected ActionHistory<Command> history;
	
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
			return new Result(ERROR_INVALID_COMMAND);
		}

		if(isExit(operation)) {
			System.exit(0);
		}
		
		result = executeOperation(operation, userCommand);
		assert result != null;
		return result;
	}

	private Result executeOperation(String operation, String userCommand) {
		Result result = null;
		
		userCommand = getSecondWordOnwards(userCommand);
		
		switch (operation) {
			case CREATE :
			case NEW :
				//same operations fall through
			case ADD :
				result = executeAddOperation(userCommand);
				break;
			case SHOW :
				//same operations fall through
			case DISPLAY :
				result = executeDisplayOperation(userCommand);
				break;
			case UPDATE :
				//same operations fall through
			case EDIT :
				result = executeEditOperation(userCommand);
				break;
	        case DEL :
			case REMOVE :
				//same operations fall through
			case DELETE :
				result = executeDeleteOperation(userCommand);
				break;
			case CLEAR :
				result = executeClearOperation(userCommand);
				break;
			case FIND :
				//same operations fall through
			case SEARCH :
				result = executeSearchOperation(userCommand);
				break;
			case UNDO :
				result = executeUndoOperation();
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

	private Result executeUndoOperation() {
		String systemFeedback;
		Result result;
		if (!history.isEmpty()) {
			Command lastCommand = history.pop();
			lastCommand.undo();
			result = lastCommand.getResult();
			systemFeedback = result.getSystemFeedback();
			result = displayTaskListWithHighlightAfterUndoCommand(systemFeedback, lastCommand);
		} else {
			result = displayTaskListAfterCommand(ERROR_NOTHING_TO_UNDO);
		}
		return result;
	}

	private Result executeSearchOperation(String userCommand) {
		Result result;
		Command search = new SearchCommand(currentTaskList);
		search.execute(userCommand);
		result = search.getResult();
		currentTaskList = search.getCurrentTaskList();
		return result;
	}

	private Result executeClearOperation(String userCommand) {
		String systemFeedback;
		Result result;
		Command clear = new ClearCommand();
		clear.execute(userCommand);
		result = clear.getResult();
		systemFeedback = clear.getSystemFeedback();
		
		result = displayTaskListAfterCommand(systemFeedback);
		return result;
	}

	private Result executeDeleteOperation(String userCommand) {
		String systemFeedback;
		Result result;
		Command delete = new DeleteCommand(currentTaskList);
		delete.execute(userCommand);
		result = delete.getResult();
		systemFeedback = delete.getSystemFeedback();
		System.out.println("DELETE SYSTEM FEEDBACK: " + systemFeedback);
		boolean success = result.isSuccessful();
		if (success) {
			history.push(delete);
		}
		
		result = displayTaskListAfterCommand(systemFeedback);
		return result;
	}

	private Result executeEditOperation(String userCommand) {
		String systemFeedback;
		Result result;
		Command edit = new EditCommand(currentTaskList);
		edit.execute(userCommand);
		systemFeedback = edit.getSystemFeedback();
		System.out.println("EDIT SYSTEM FEEDBACK: " + systemFeedback);
		result = edit.getResult();
		boolean success = result.isSuccessful();
		if (success) {
			history.push(edit);
		}
		
		result = displayTaskListWithHighlightAfterEditCommand(systemFeedback, edit);
		return result;
	}

	private Result executeDisplayOperation(String userCommand) {
		Result result;
		Command display = new DisplayCommand();
		display.execute(userCommand);
		result = display.getResult();
		currentTaskList = display.getCurrentTaskList();
		return result;
	}

	private Result executeAddOperation(String userCommand) {
		String systemFeedback;
		Result result;
		Command add = new AddCommand();
		add.execute(userCommand);
		systemFeedback = add.getSystemFeedback();
		result = add.getResult();
		boolean success = result.isSuccessful();
		if (success) {
			history.push(add);
		}
		result = displayTaskListWithHighlightAfterAddCommand(systemFeedback, add);
		return result;
	}

	private Result displayTaskListAfterCommand(String systemFeedback) {
		Result result;
		DisplayCommand disp = new DisplayCommand();
		disp.execute(UNDONE);
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
		disp.execute(UNDONE);
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
		disp.execute(UNDONE);
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
		disp.execute(UNDONE);
		result = disp.getResult();
		result.setSystemFeedback(systemFeedback);
		currentTaskList = disp.getCurrentTaskList();
		return result;
	}
	
	private boolean isExit(String operation) {
		return operation.equalsIgnoreCase(EXIT) || operation.equalsIgnoreCase(QUIT);
	}
	
	private String getOperation(String userCommand) {
		return getFirstWord(userCommand).toLowerCase();
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
}
