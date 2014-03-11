
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
	//private final String READY_MESSAGE = "Sapphire Manager is ready to use.";
	
	private void print(String message){
		System.out.println(message);
	}
	
	private void printInLine(String message){
		System.out.print(message);
	}
	
	// UI Default Constructor
	public UserInterface(){
		this.scanner = new Scanner(System.in);
	}

	// SECTION 1 : Functions to print messages to user
	
	public void displayWelcomeMessage(){
		print(WELCOME_MESSAGE);
		//print(READY_MESSAGE); //to be removed in final version, this to check file is ready
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
			printInLine(count++ + ". ");
			task.printTaskDetails();
			print(""); 	//line separator between tasks
		}
	}
	
	public void displayExistingTasksFound(ArrayList<Task> taskList){
		print("Existing tasks found:");
		displayTasksGivenList(taskList);
		printInLine("Enter a number: ");
	}
	
	public void displayCurrentlyEditingSequence(Task taskToEdit){
		print("Currently Editing:");
		displaySingleTask(taskToEdit);
		printInLine("Enter your edits: ");
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
		printInLine("Command: ");
		return readUserInput();
	}
	
	public String readUserUpdates(){
		return readUserInput();
	}
	
	public int readUserChoice(){
		String userInput = readUserInput();
		int userChoice = Integer.parseInt(userInput);
		return userChoice;
	}	

}

