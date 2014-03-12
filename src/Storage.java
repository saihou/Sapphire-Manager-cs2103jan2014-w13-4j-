import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author Teck Sheng (Dex)
 * This class is the storage that that deals with the reading of file and writing to file
 */

public class Storage {
	//default file name - cannot change
	private final static String FILE_NAME = "mytextfile.txt";

	//error messages
	private final static String FILE_CLEARING_ERROR = "Error clearing file! Please re-start program.";
	private final static String FILE_INITIALIZING_ERROR = "Error initializing file! Please re-start program.";
	private final static String FILE_READING_ERROR = "Error reading file! Please re-start program.";
	private final static String FILE_SAVING_ERROR = "Error saving file! Please re-start program.";

	//declaration
	private ArrayList<Task> taskList;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private File file, newFile;
	protected Task task;
	protected UserInterface userInterface;

	//constructor
	public Storage(UserInterface userInterface) {
		this.userInterface = userInterface;
		taskList = new ArrayList<Task>();
		readFile();
	}

	//get-set tasklist
	public ArrayList<Task> getTaskList() {
		return taskList;
	}

	public boolean setTaskList(Task task) {
		if(task != null) {
			taskList.add(task);
			return true;
		}
		return false;
	}

	//write a task to file
	//toAppend: true - append to existing file; false - rewrite the file
	public boolean writeATaskToFile(Task task, boolean toAppend) {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(FILE_NAME), toAppend));
			bufferedWriter.write(convertTaskToString(task));
			bufferedWriter.newLine();
			bufferedWriter.close();
			return true;
		} catch(Exception ex) {
			userInterface.displayMessage(FILE_SAVING_ERROR);
			return false;
		}
	}

	//write multiple task to file
	public boolean writeTaskListToFile() {
		if(taskList.size() > 0) {
			return writeFile();
		} else {
			return clearFile();
		}
	}

	//readFile
	private boolean readFile() {		
		try {
			file = new File(FILE_NAME);
			if(file.exists()) {
				return readFileLine();
			} else {
				return initializeFile();
			}
		} catch(Exception ex) {
			userInterface.displayMessage(FILE_READING_ERROR); 
			return false;
		}
	}

	//initialize file
	private boolean initializeFile() {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file, true));
			bufferedWriter.close();
			return true;
		} catch (Exception ex) {
			userInterface.displayMessage(FILE_INITIALIZING_ERROR); 
			return false;
		}
	}

	//read file line by line
	private boolean readFileLine() {
		try {
			bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
			String oneTaskInfo = bufferedReader.readLine();
			String[] oneTaskSplited;
			while(oneTaskInfo != null) {
				oneTaskSplited = oneTaskInfo.split("\\|");
				taskList.add(convertStringToTask(oneTaskSplited));
				oneTaskInfo = bufferedReader.readLine();
			}
			bufferedReader.close();
			return true;
		} catch (Exception ex) {
			userInterface.displayMessage(FILE_READING_ERROR); 
			return false;
		}
	}

	//clear and re-initialize file
	public boolean clearFile() {
		try {
			file = new File(FILE_NAME);
			if(file.delete()) {
				newFile = new File(FILE_NAME);
				return newFile.createNewFile();
			} else {
				return false;
			}
		} catch (Exception ex) {
			userInterface.displayMessage(FILE_CLEARING_ERROR);
			return false;
		}
	}
	
	//clear and initialize ArrayList
	public boolean clearTaskList() {
		taskList.clear();
		return true;
	}

	//write file line by line
	private boolean writeFile() {
		for(int i=0; i<taskList.size(); i++) {
			if(i == 0) {
				writeATaskToFile(taskList.get(i), false);
			} else {
				writeATaskToFile(taskList.get(i), true);
			}
		}
		return true;
	}

	//converts String to Task
	private Task convertStringToTask(String[] splitedTaskInfo) {
		task = new Task();	

		if(!splitedTaskInfo[0].equals("[null]")) { //type
			task.setType(splitedTaskInfo[0]); 
		} else {
			task.setType(null);
		}

		if(!splitedTaskInfo[1].equals("[null]")) { //task name
			task.setName(splitedTaskInfo[1]);
		} else {
			task.setName(null);
		}

		if(!splitedTaskInfo[2].equals("[null]")) { //date
			task.setDate(splitedTaskInfo[2]);
		} else {
			task.setDate(null);
		}

		if(!splitedTaskInfo[3].equals("[null]")) { //start time
			task.setStartTime(splitedTaskInfo[3]);
		} else {
			task.setStartTime(null);
		}

		if(!splitedTaskInfo[4].equals("[null]")) { //end time
			task.setEndTime(splitedTaskInfo[4]);
		} else {
			task.setEndTime(null);
		}

		if(!splitedTaskInfo[5].equals("[null]")) { //deadline
			task.setDeadline(splitedTaskInfo[5]);
		} else {
			task.setDeadline(null);
		}

		if(!splitedTaskInfo[6].equals("[null]")) { //location
			task.setLocation(splitedTaskInfo[6]);
		} else {
			task.setLocation(null);
		}

		return task;
	}

	//converts Task to String
	private String convertTaskToString(Task task) {
		String convertedTask = "";

		if(task.getType() == null) { //type
			convertedTask += "[null]";
		} else {
			convertedTask += task.getType();
		}

		convertedTask += "|";

		if(task.getName() == null) { //name
			convertedTask += "[null]";
		} else {
			convertedTask += task.getName();
		}

		convertedTask += "|";

		if(task.getDate() == null) { //date
			convertedTask += "[null]";
		} else {
			convertedTask += task.getDate();
		}

		convertedTask += "|";

		if(task.getStartTime() == null) { //start time
			convertedTask += "[null]";
		} else {
			convertedTask += task.getStartTime();
		}

		convertedTask += "|";

		if(task.getEndTime() == null) { //end time
			convertedTask += "[null]";
		} else {
			convertedTask += task.getEndTime();
		}

		convertedTask += "|";

		if(task.getDeadline() == null) { //deadline
			convertedTask += "[null]";
		} else {
			convertedTask += task.getDeadline();
		}

		convertedTask += "|";

		if(task.getLocation() == null) { //location
			convertedTask += "[null]";
		} else {
			convertedTask += task.getLocation();
		}

		return convertedTask;
	}
}