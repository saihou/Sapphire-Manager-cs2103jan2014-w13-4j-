

public class SapphireManager {
	private static UserInterface myUI = new UserInterface();
	private static Parser myParser = new Parser();
	private static Executor myExecutor = new Executor(myParser,myUI);
	
	private static void processCommand(String command){
		while(true){
			doUserOperation(command);
			if(command.compareTo("Exit") == 0)
					break;
		}
	}

	private static void doUserOperation(String userInput) {

		String operation = userInput.split(" ")[0];
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
	public static void main(String[] args)throws Exception {
		
		myUI.displayWelcomeMessage();	
		
		processCommand(myUI.readCommandFromUser());
		

	}

}

