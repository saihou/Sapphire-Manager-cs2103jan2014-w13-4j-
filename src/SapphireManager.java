/**
 * @author Cai Di
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SapphireManager {
	
	private static UserInterface myUI;
	private static Parser myParser;
	private static Executor myExecutor;
	
	private static void startApplication() {
		
		while(true) {
			String userCommand = myUI.readCommandFromUser();
			String operation = myParser.getFirstWord(userCommand);
			
			if(operation.equalsIgnoreCase("Exit")) {
				myUI.displayMessage("Sapphire Manager will now exit.");
				break;
			}
			
			doUserOperation(userCommand, operation);
		}
	}
	
	private static void initialiseEnvironment() {
		myUI = new UserInterface();
		myParser = new Parser();
		myExecutor = new Executor(myParser,myUI);
	}
	
	private static void doUserOperation(String userInput, String operation) {

		String userInputWithoutOperation = "";
		
		switch (operation) {
		case "add":
			userInputWithoutOperation = userInput.substring(4);
			myExecutor.executeAddCommand(userInputWithoutOperation);
			break;
		case "delete":
			userInputWithoutOperation = userInput.substring(7);
			myExecutor.executeDeleteCommand(userInputWithoutOperation);
			break;
		case "display":
			userInputWithoutOperation = userInput.substring(7);
			myExecutor.executeDisplayCommand(userInputWithoutOperation);
			break;
		case "edit":
			userInputWithoutOperation = userInput.substring(5);
			myExecutor.executeEditCommand(userInputWithoutOperation);
			break;
		case "undo":
			myExecutor.executeUndoCommand();
			break;
		default:
			break;
		}
	}
	
	/**
	 * @author Si Rui
	 * This function gets the current date and returns it in DDMMYY format.
	 */
	private static String getTodaysDate(){
		SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyy");
		Calendar date = Calendar.getInstance();
		return dateFormatter.format(date.getTime());
	}
	
	/**
	 * @author Si Rui
	 * This function processes the current date and prints tasks that fall on this date.
	 * This is part of the application start up sequence, an added function to increase 
	 * efficiency of the user.
	 */
	private static void printTodaysTasks(){
		String todaysDate = getTodaysDate();
		ArrayList<Task> todaysTasks = myExecutor.getTodaysTasks(todaysDate);
		
		if(todaysTasks.isEmpty()){
			myUI.displayMessage("You have no tasks today.");
		}else{
			myUI.displayMessage("Today's tasks: ");
			myUI.displayTasksGivenList(todaysTasks);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initialiseEnvironment();
		
		myUI.displayWelcomeMessage();	
		
		printTodaysTasks();
		
		startApplication();
	}

}

