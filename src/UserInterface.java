
/**
 * @author Si Rui
 *	This class handles reading the user input and printing feedback from the application
 */

import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
	
	private Scanner scanner;
	private final String WELCOME_MESSAGE = "Welcome to Sapphire Manager!";
	private final String HELP_MESSAGE = "Enter '?' for a list of commands.";
	private final String TASKS_FOUND_MESSAGE = "Existing tasks found: ";
	
	private final String PROMPT_FOR_COMMAND = "Command: ";
	private final String PROMPT_FOR_NUMBER = "Enter a number: ";
	private final String PROMPT_FOR_EDITS = "Enter your edits: ";
	private final String CURRENTLY_EDITING_MESSAGE = "Currently Editing:";
	
	//private final String READY_MESSAGE = "Sapphire Manager is ready to use.";
	
	private void print(String message){
		System.out.println(message);
	}
	
	private void printInLine(String message){
		System.out.print(message);
	}
	
	private void printLineSeparator(){
		print("");
	}
	
	// UI Default Constructor
	public UserInterface(){
		this.scanner = new Scanner(System.in);
	}

	// SECTION 1 : Functions to print messages to user
	
	public void displayWelcomeMessage(){
		print(WELCOME_MESSAGE);
		//print(READY_MESSAGE); //to be removed in final version, this to check file is ready
		//print(HELP_MESSAGE); //not implemented yet
	}
	
	public void displayMessage(String message){
		print(message);
	}
	
	public void displaySingleTask(Task taskToDisplay) {
		taskToDisplay.printTaskDetails();
		printLineSeparator();
	}
	
	public void displayTasksGivenList(ArrayList<Task> taskList){
		int count = 1;
		for(Task task : taskList){
			printInLine(count++ + ". ");
			task.printTaskDetails();
			printLineSeparator(); 	
		}
	}
	
	public void displayExistingTasksFound(ArrayList<Task> taskList){
		print(TASKS_FOUND_MESSAGE);
		displayTasksGivenList(taskList);
		printInLine(PROMPT_FOR_NUMBER);
	}
	
	public void displayCurrentlyEditingSequence(Task taskBeingEdited){
		print(CURRENTLY_EDITING_MESSAGE);
		displaySingleTask(taskBeingEdited);
		printInLine(PROMPT_FOR_EDITS);
	}
	
	// SECTION 2 : Functions to read user input
	
	private String readUserInput(){
		String userInput = null;
		if (scanner.hasNext()){
			userInput = scanner.nextLine();
		}
		return userInput;
	}
	
	public String readCommandFromUser() {
		printInLine(PROMPT_FOR_COMMAND);
		return readUserInput();
	}
	
	public String readUserEdits(){
		return readUserInput();
	}
	
	public int readUserChoice(){
		String userInput = readUserInput();
		int userChoice = Integer.parseInt(userInput);
		return userChoice;
	}	

}

