/**
 * 
 */

/**
 * @author Si Rui
 *	This class handles reading the user input and printing feedback from the application
 */

import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
	private final String WELCOME_MESSAGE = "Welcome to Sapphire Manager!";
	private final String HELP_MESSAGE = "Enter '?' for a list of commands.";
	private final String READY_MESSAGE = "Sapphire Manager is ready to use.";
	
	private void print(String message){
		System.out.println(message);
	}
	
	private void printInLine(String message){
		System.out.print(message);
	}
	
	// UI Default Constructor
	public UserInterface(){
		
	}

	// SECTION 1 : Functions to print messages to user
	
	public void displayWelcomeMessage(){
		print(WELCOME_MESSAGE);
		print(READY_MESSAGE);
		print(HELP_MESSAGE);
	}
	
	public void displayMessage(String message){
		print(message);
	}
	
	public void displaySingleTask(Task taskToDisplay) {
		taskToDisplay.printTaskDetails();
	}
	
	public void displayTasksGivenList(ArrayList<Task> list){
		int count = 1;
		for(Task task : list){
			print(count++ + ". ");
			task.printTaskDetails();
			print(""); 	//line separator between tasks
		}
	}
	
	// SECTION 2 : Functions to read user input
	
	private String readUserInput(Scanner scanner){
		String userInput = null;
		if (scanner.hasNext()){
			userInput = scanner.nextLine();
		}
		return userInput;
	}
	
	public String readCommandFromUser(Scanner scanner) {
		printInLine("command: ");
		return readUserInput(scanner);
	}
	
	public String readUserUpdates(Scanner scanner){
		return readUserInput(scanner);
	}
	
	public int readUserChoice(Scanner scanner){
		String userInput = readUserInput(scanner);
		int userChoice = Integer.parseInt(userInput);
		return userChoice;
	}	

}

