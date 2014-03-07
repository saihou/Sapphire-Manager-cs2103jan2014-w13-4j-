/**
 * @author Cai Di
 */

public class SapphireManager {
	
	private static UserInterface myUI;
	private static Parser myParser;
	private static Executor myExecutor;
	
	private static void startApplication() {
		
		while(true){
			
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
			userInputWithoutOperation = userInput.substring(7);
			myExecutor.executeEditCommand(userInputWithoutOperation);
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initialiseEnvironment();
		
		myUI.displayWelcomeMessage();	
		
		startApplication();
	}

}

