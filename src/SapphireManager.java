import java.lang.Object.*;
import java.util.*;
import java.io.*;
import java.text.*;

/**
 * @author 
 *
 */
public class SapphireManager {
	private static final String WELCOME_MESSAGE = "Welcome to Sapphire Manager!";
	private static final String HELP_MESSAGE = "Enter '?' for a list of commands.";
	private static final String READY_MESSAGE = "Sapphire Manager is ready to use.";
	private static final String textFileName = "mytextfile.txt";
	
	private static ArrayList < Task > taskList;
	
	//create
	private static void executeAddCommand(String cmd){
		Task myTask = new Task();
		parseText(cmd, myTask);
		writeSingleTaskToFile(myTask);
	}
	
	private static void executeDisplayCommand(String cmd){
		//read everything from text file
		//sort????
		//print
	}
	private static void executeEditCommand(String cmd){
		//search for the task
		ArrayList<Task> matchedTasks = searchByName(cmd);
		
		//display matched tasks
		displayTasks(matchedTasks);
		
		//ask user to choose
		int choice = readUserChoice(new Scanner(System.in));
		
		//display selected task to user
		displaySelectedTasks(matchedTasks, choice);
		//read input from user, parse input
		
		//String input = readUserUpdates(new Scanner(System.in));
		
		//edit
	}
	private static void executeDeleteCommand(String cmd){
		//search for the task
		
		
		//ask user to choose
		
		
		//display selected task to user
		
		
		//ask user to confirm
		
		
		//delete
		
		
	}
	private static ArrayList<Task> searchByName(String name) {
		ArrayList<Task> matchedTasks = new ArrayList<Task>();
		
		for (Task t : taskList) {
			if (t.getName().contains(name)) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
	}
	private static void displayTasks(ArrayList<Task> tasks) {
		int i = 1;
		System.out.println("Existing tasks found: ");
		
		for (Task t: tasks) {
			//t.printTaskDetails();
			System.out.print(i+++ ". ");
			System.out.println(t.getName());
			System.out.println("Date: " + t.getDate());
		}
		System.out.println("Enter a number: ");
	}
	private static int readUserChoice(Scanner scan) {
		Integer userInput=0;
		if (scan.hasNext()) {
			userInput = scan.nextInt();
		}
		return userInput;
	}
	private static void displaySelectedTasks(ArrayList<Task> tasks, int choice) {
		Task selectedTask = tasks.get(choice-1);
		System.out.println("Currently editing: ");
		System.out.println(selectedTask.getName());
		System.out.println("Date: " + selectedTask.getDate());
		//selectedTask.printTaskDetails();
	}
	private static String readUserUpdates(Scanner scan) {
		String userInput=null;
		if (scan.hasNext()) {
			userInput = scan.nextLine();
		}
		return userInput;
	}
	private static void executeSearchCommand(String cmd) {
		
	}
	
	//parser method is only for create and update
	private static void parseText(String cmd, Task myTask){
		
		//store the task name 
		int indexOfFirstDash = cmd.indexOf("/");
		myTask.setTaskName(cmd.substring(0, indexOfFirstDash));
		
		//new cmd string without the task name
		String cmdWithoutTaskName = cmd.substring(indexOfFirstDash);
		
		int indexOfFrom = cmd.indexOf("From");
		int indexOfTo = cmd.indexOf("To");
		int indexOfDate = cmd.indexOf("on");
		int indexOfLocation = cmd.indexOf("loc");
		
		//store the starting time
		if (indexOfFrom != -1) 
			myTask.setStartTime(Integer.valueOf(cmdWithoutTaskName.substring(indexOfFrom + 2,
					indexOfTo + 6)));
		
		//store the ending time
		if (indexOfTo != -1)
			myTask.setEndTime(Integer.valueOf(cmdWithoutTaskName.substring(indexOfTo + 2,
					indexOfTo + 6)));
		
		//store the date 
		if (indexOfDate != -1)
			myTask.setDate(Integer.valueOf(cmdWithoutTaskName.substring(indexOfDate + 2,
					indexOfDate + 8)));
		
		//store the location
		if (indexOfLocation != -1)
			myTask.setLocation(cmdWithoutTaskName.substring(indexOfLocation + 2));
		//
	}
	private static void writeSingleTaskToFile(Task taskToBeWritten) {
		//this method writes a single task to file
	}
	
	private static void print(String message){
		System.out.println(message);
	}
	
	private static void printWelcomeMessage(){
		print(WELCOME_MESSAGE);
		print(HELP_MESSAGE);
	}
	
	private static void writeLineToArrayList(String line){
		Task taskToAdd = new Task(line);
		taskList.add(taskToAdd);
	}
	
	private static void processLineByLine(BufferedReader reader){
		try {
			String line;
			while ((line = reader.readLine()) != null) {
		        writeLineToArrayList(line);
			}
		} catch (IOException e) {
		    System.out.println("File cannot be created/opened.");
		}
	}
	
	private static void processTasksFromFile(){
		File file = new File(textFileName);
		try {
		    BufferedReader reader = new BufferedReader(new FileReader(file));
		    processLineByLine(reader);

		} catch (FileNotFoundException e) {
		    System.out.println("User's first use.");
		} 
	}
	
	private static void initialize(){
		taskList = new ArrayList <Task> ();
		processTasksFromFile();
		print(READY_MESSAGE);
	}
	
	private static String getTodaysDate(){
		SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyy");
		Calendar date = Calendar.getInstance();
		return dateFormatter.format(date.getTime());
	}
	
	private static void printTodaysTask(){
		String todaysDate = getTodaysDate();
		ArrayList <Task> todaysTasks = searchByDate(todaysDate);
		display(todaysTasks);
	}
	
	private static void executeCommandsUntilExit(){
		boolean isDone = false;
		Scanner scanUserInput = new Scanner(System.in);
		
		while (!isDone) {
			String userInput = captureInputFromUser(scanUserInput);

			isDone = checkIfUserIsGoingToExit(userInput);

			if (!isDone) {
				doUserOperation(userInput);
			}
		}
	}
	private static String captureInputFromUser(Scanner scanUserInput) {
		System.out.print("command: ");
		if (scanUserInput.hasNext()) {
			String userInput = scanUserInput.nextLine();
			return userInput;
		} else
			return null;
	}

	public static boolean checkIfUserIsGoingToExit(String input) {
		if (input == null || input.startsWith("exit")) {
			return true;
		} else {
			return false;
		}
	}
	private static void doUserOperation(String userInput) {
		String operation = userInput.split(" ")[0];
		String userInputWithoutOperation = "";
		
		switch (operation) {
		case "add":
			userInputWithoutOperation = userInput.substring(4);
			executeAddCommand(userInputWithoutOperation);
			break;
		case "delete":
			userInputWithoutOperation = userInput.substring(7);
			executeDeleteCommand(userInputWithoutOperation);
			break;
		case "display":
			userInputWithoutOperation = userInput.substring(7);
			executeDisplayCommand(userInputWithoutOperation);
			break;
		case "edit":
			userInputWithoutOperation = userInput.substring(7);
			executeEditCommand(userInputWithoutOperation);
			break;
		/*case "clear":
			break;
		case "sort":
			break;
		case "search":
			break;*/
		default:
			break;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		printWelcomeMessage();
		
		initialize();
		
		printTodaysTask();
		
		executeCommandsUntilExit();

	}

}

