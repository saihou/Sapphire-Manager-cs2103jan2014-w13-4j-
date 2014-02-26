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
	
	//get first word of the command
	static String getFirstWord(String cmd){
		return cmd.trim().split("\\s+")[0];
	}
	
	//create
	private static void executeAddCommand(String cmd)throws Exception{
		Task myTask = new Task();
		parser(cmd, myTask);
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
		ArrayList<Task> matchedTasks = searchByName(cmd);
		
		//display matched tasks
		displayTasks(matchedTasks);
		
		//ask user to choose
		int choice = readUserChoice(new Scanner(System.in));
		
		//in case user entered the wrong number
		while(choice > matchedTasks.size()) {
			displayTasks(matchedTasks);
			choice = readUserChoice(new Scanner(System.in));
		}
		
		//display selected task to user
		displaySelectedTasksForDelete(matchedTasks, choice);
		
		//ask user to confirm
		boolean CfmChoice = displayConfirmationMsg();
		
		//delete
		if(CfmChoice) {
			taskList.remove(matchedTasks.get(choice-1));
			System.out.println("Successfully deleted "+choice+". "+matchedTasks.get(choice-1).getName());
		} else {
			print("Delete terminated. Please enter your next command.");
		}
	}
	private static boolean displayConfirmationMsg() {
		//return promptYesOrNo();
		Scanner scan = new Scanner(System.in);
		//print("Confirm delete (y/n)");
		String choice = null;//= scan.next();
		//System.out.println("Entered1: "+choice);
		while(true) {
			print("\nConfirm delete (y/n)");
			choice = scan.next();
			//System.out.println("Entered2: "+choice);
			if(choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("n")) {
				break;
			}
		}
		if(choice.equalsIgnoreCase("y")) {
			System.out.println("Chose Yes");
			return true;
		} else {
			System.out.println("Chose No");
			return false;
		}
	} /*
	private static boolean promptYesOrNo() {
		String choice = null;
		Scanner scan = new Scanner(System.in);
		while(!scan.next().equalsIgnoreCase("n") || !scan.next().equalsIgnoreCase("n")) {
			choice = scan.next();
		}
		if(choice.equalsIgnoreCase("y")) {
			return true;
		} else {
			return false;
		}
	}*/
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
	private static void displaySelectedTasksForDelete(ArrayList<Task> tasks, int choice) {
		Task selectedTask = tasks.get(choice-1);
		System.out.println(choice+". "+selectedTask.getName());
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
	//parser method if only for add and edit
	public static void parser(String cmd, Task myTask)throws Exception{

		//store the task name
		int indexOfFirstDash = cmd.indexOf("-");
		myTask.setTaskName(cmd.substring(0, indexOfFirstDash));
		
		//new cmd string without the task name
		String cmdWithoutTaskName = cmd.substring(indexOfFirstDash);
		
		int indexOfFrom = cmdWithoutTaskName.indexOf("from");
		int indexOfTo = cmdWithoutTaskName.indexOf("to");
		int indexOfDate = cmdWithoutTaskName.indexOf("on");
		int indexOfLocation = cmdWithoutTaskName.indexOf("loc");
		
		//store the starting time
		if (indexOfFrom != -1) 
			myTask.setStartTime(getFirstWord(cmdWithoutTaskName.substring(indexOfFrom+4)));
		
		//store the ending time
		if (indexOfTo != -1)
			myTask.setEndTime(getFirstWord(cmdWithoutTaskName.substring(indexOfTo+2)));
		
		//store the date
		if (indexOfDate != -1)
			myTask.setDate(getFirstWord(cmdWithoutTaskName.substring(indexOfDate+2)));
		
		//store the location
		if (indexOfLocation != -1)
			myTask.setLocation(getFirstWord(cmdWithoutTaskName.substring(indexOfLocation+3)));
		
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
		//Task taskToAdd = new Task(line);
		//taskList.add(taskToAdd);
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
		//String todaysDate = getTodaysDate();
		//ArrayList <Task> todaysTasks = searchByDate(todaysDate);
		//display(todaysTasks);
	}
	
	private static void executeCommandsUntilExit()throws Exception{
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
	private static void doUserOperation(String userInput)throws Exception {
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
	public static void main(String[] args)throws Exception {
		printWelcomeMessage();
		
		initialize();
		
		printTodaysTask();
		
		Task tt = new Task();
		tt.setTaskName("HELLOWORLD");
		tt.setDate("260214");
		
		Task tt2 = new Task();
		tt2.setTaskName("HELLO");
		tt2.setDate("260214");
		
		taskList.add(tt);
		taskList.add(tt2);
		
		executeCommandsUntilExit();

	}

}

