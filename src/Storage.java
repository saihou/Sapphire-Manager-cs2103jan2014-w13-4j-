import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JOptionPane;

/**
 * @author A0097706U
 * This class is the storage that that deals with the reading of file and writing to file
 */

public class Storage {
	//default file name - cannot change
	private final static String FILE_NAME = "mytextfile.txt";
	private final static String FILE_ERROR_BACKUP_NAME = "mytextfileWithError.txt";

	//error messages
	private final static String FILE_CLEARING_ERROR = "Error clearing file! Please re-start program.";
	private final static String FILE_INITIALIZING_ERROR = "Error initializing file! Please re-start program.";
	private final static String FILE_READING_ERROR = "Error reading file! Please re-start program.";
	private final static String FILE_SAVING_ERROR = "Error saving file! Please re-start program.";

	//file header
	private final static String FILE_HEADER = String.format("%-6s", "Date").replace(' ', ' ')+"|"+
			String.format("%-10s", "Start Time").replace(' ', ' ')+"|"+
			String.format("%-8s", "End Time").replace(' ', ' ')+"|"+
			String.format("%-60s", "Task Name").replace(' ', ' ')+"|"+
			String.format("%-30s", "Location").replace(' ', ' ')+"|"+
			String.format("%-12s", "Task Type").replace(' ', ' ')+"|"+
			String.format("%-7s", "Task Status").replace(' ', ' ');
	private final static String FILE_DIVIDER = String.format("%-145s", "-").replace(' ', '-');

	//declaration
	private ArrayList<Task> taskList, backUpTaskList;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private File file, errorFile;
	private Task task;

	//instance - Singleton
	private static Storage instance = null;

	//constructor to initialize arraylist and file
	private Storage() {
		taskList = new ArrayList<Task>();
		backUpTaskList = new ArrayList<Task>();
		readFile();
	}

	//return instance of Storage <Singleton>
	public static Storage getInstance() {
		if (instance == null) {
			instance = new Storage();
			return instance;
		}
		else {
			return instance;
		}
	}

	//gets tasklist
	public ArrayList<Task> getTaskList() {
		return taskList;
	}

	//add task to list
	public boolean setTaskList(Task task) {
		if(task != null) {
			taskList.add(task);
			updateBackUpTaskList();
			return true;
		}
		updateBackUpTaskList();
		return false;
	}

	//writes String to file
	private boolean writeHeader(String header, boolean toAppend) {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(FILE_NAME), toAppend));
			bufferedWriter.write(header);
			bufferedWriter.newLine();
			bufferedWriter.close();
			return true;
		} catch(Exception ex) {
			displayPopUpMsg(FILE_SAVING_ERROR);
			return false;
		}
	}

	//write a task to file
	//toAppend: true - append to existing file; false - rewrite the file
	public boolean writeATaskToFile(Task task, boolean toAppend) {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(FILE_NAME), true));
			bufferedWriter.write(convertTaskToString(task));
			bufferedWriter.newLine();
			bufferedWriter.close();

			if(toAppend) {
				backUpTaskList.add(task);
			} 
			updateBackUpTaskList();
			return true;
		} catch(Exception ex) {
			displayPopUpMsg(FILE_SAVING_ERROR);
			return false;
		}
	}

	//write multiple task to file
	public boolean writeTaskListToFile() {
		if(taskList.size() > 0) {
			initializeFile();
			return writeFile();
		} else {
			backUpTaskList.clear();
			return clearFile();
		}
	}

	//clear all file
	public boolean clear() {
		if(!clearFile()) {
			writeBackUpFile();
		}

		if(clearTaskList()) {
			return true;
		} else {
			reAddTaskListFromBackUp();
			return false;
		}
	}

	//read file
	private boolean readFile() {		
		try {
			file = new File(FILE_NAME);
			if(file.exists()) {
				return readFileLine();
			} else {
				return initializeFile();
			}
		} catch(Exception ex) { 
			displayPopUpMsg(FILE_READING_ERROR);
			return false;
		}
	}

	//initialize file
	private boolean initializeFile() {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file, false));
			writeHeader(FILE_HEADER, false);
			writeHeader(FILE_DIVIDER, true);
			bufferedWriter.close();
			return true;
		} catch (Exception ex) {
			displayPopUpMsg(FILE_INITIALIZING_ERROR);
			return false;
		}
	}

	//read file line by line
	private boolean readFileLine() {
		try {
			bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
			String oneTaskInfo = bufferedReader.readLine(); //skip header line 1
			oneTaskInfo = bufferedReader.readLine(); //skip header line 2
			oneTaskInfo = bufferedReader.readLine(); //line 3
			String[] oneTaskSplited;
			while(oneTaskInfo != null) {
				oneTaskSplited = oneTaskInfo.split("\\|");
				if(oneTaskSplited.length > 1) {
					if(convertStringToTask(oneTaskSplited) != null) {
						taskList.add(convertStringToTask(oneTaskSplited));
					} else {
						settleInvalidFile();
					}
					oneTaskInfo = bufferedReader.readLine();
				}
			}
			updateBackUpTaskList();
			bufferedReader.close();
			return true;
		} catch (Exception ex) {
			displayPopUpMsg(FILE_READING_ERROR);
			return false;
		}
	}
	
	//copy the contents of invalid file under a separate file name
	//reinitializes the original file
	private boolean settleInvalidFile() {
		String ERROR_MESSAGE = "There is a problem reading the existing file, \n"
				+ "a copy of the file ('mytextfileWithError.txt') has been saved.\n"
				+ "You may continue to use the program.";
		displayPopUpMsg(ERROR_MESSAGE);
		try {  
			errorFile = new File(FILE_ERROR_BACKUP_NAME);

			bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
			bufferedWriter = new BufferedWriter(new FileWriter(errorFile, false));
			String copiedLine = bufferedReader.readLine();
			while(copiedLine != null) {
				bufferedWriter.write(copiedLine);
				bufferedWriter.newLine();
				copiedLine = bufferedReader.readLine();
			}

			bufferedWriter.close();
			bufferedReader.close();

			taskList.clear();
			backUpTaskList.clear();
			initializeFile();
		} catch (Exception e) {  
			displayPopUpMsg(e.toString());  
		}  
		return false;
	}

	//clear and re-initialize file
	private boolean clearFile() {
		try {
			file = new File(FILE_NAME);
			if(file.delete()) {	
				initializeFile();
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			displayPopUpMsg(FILE_CLEARING_ERROR);
			return false;
		}
	}

	//clear and initialize ArrayList
	private boolean clearTaskList() {
		try {
			taskList.clear();
			backUpTaskList.clear();
			return true;
		} catch (Exception ex) {
			displayPopUpMsg(FILE_CLEARING_ERROR);
			reAddTaskListFromBackUp();
			return false;
		}
	}

	//write file line by line
	private boolean writeFile() {
		initializeFile();
		for(int i=0; i<taskList.size(); i++) {
			if(i == 0) {
				writeATaskToFile(taskList.get(i), false);
			} else {
				writeATaskToFile(taskList.get(i), true);
			}
		}
		updateBackUpTaskList();
		return true;
	}

	//converts String to Task
	public Task convertStringToTask(String[] splitedTaskInfo) {
		task = new Task();	
		boolean isConversionOkay = true;		

		if(!splitedTaskInfo[0].trim().equals("-")) { //date
			if(ValidationCheck.isValidDate(splitedTaskInfo[0].trim())) {
				task.setDate(splitedTaskInfo[0].trim());
			} else {
				isConversionOkay = false;
			}
		} else {
			task.setDate(null);
		}

		if(!splitedTaskInfo[1].trim().equals("-")) { //start time
			if(ValidationCheck.isValidTime(splitedTaskInfo[1].trim())) {
				task.setStartTime(splitedTaskInfo[1].trim());
			} else {
				isConversionOkay = false;
			}
		} else {
			task.setStartTime(null);
		}

		if(!splitedTaskInfo[2].trim().equals("-")) { //end time
			if(ValidationCheck.isValidTime(splitedTaskInfo[2].trim())) {
				task.setEndTime(splitedTaskInfo[2].trim());
			} else {
				isConversionOkay = false;
			}
		} else {
			task.setEndTime(null);
		}

		if(task.getStartTime() != null && task.getEndTime() != null) {
			if(!ValidationCheck.isValidDuration(task.getStartTime(), task.getEndTime())) {
				isConversionOkay = false;
			}
		}

		if(!splitedTaskInfo[3].trim().equals("-")) { //task name
			task.setName(splitedTaskInfo[3].trim());
		} else {
			task.setName(splitedTaskInfo[3].trim());
		}

		if(!splitedTaskInfo[4].trim().equals("-")) { //location
			task.setLocation(splitedTaskInfo[4].trim());
		} else {
			task.setLocation(null);
		}

		if(!splitedTaskInfo[5].trim().equals("-")) { //type
			task.setType(splitedTaskInfo[5].trim());
		} else {
			task.setType(null);
		}

		if(splitedTaskInfo[6].trim().equals("done")) { //isDone
			if(ValidationCheck.isValidStatus(splitedTaskInfo[6].trim())) {
				task.setIsDone(true);
			} else {
				isConversionOkay = false;
			}
		} else {
			task.setIsDone(false);
		}

		//assert task != null;
		//assert false;

		if(isConversionOkay) {
			return task;
		} 

		return null;
	}

	//converts Task to String
	public String convertTaskToString(Task task) {
		String convertedTask = "";

		if(task.getDate() == null) { //date
			convertedTask += String.format("%-6s", "-").replace(' ', ' ');
		} else {
			convertedTask += String.format("%-6s", task.getDate()).replace(' ', ' ');
		}

		convertedTask += "|";

		if(task.getStartTime() == null) { //start time
			convertedTask += String.format("%-10s", "-").replace(' ', ' ');
		} else {
			convertedTask += String.format("%-10s", task.getStartTime()).replace(' ', ' ');
		}

		convertedTask += "|";

		if(task.getEndTime() == null) { //end time
			convertedTask += String.format("%-8s", "-").replace(' ', ' ');
		} else {
			convertedTask += String.format("%-8s", task.getEndTime()).replace(' ', ' ');
		}

		convertedTask += "|";

		if(task.getName() == null) { //name
			convertedTask += String.format("%-60s", "-").replace(' ', ' ');
		} else {
			convertedTask += String.format("%-60s", task.getName()).replace(' ', ' ');
		}

		convertedTask += "|";

		if(task.getLocation() == null) { //location
			convertedTask += String.format("%-30s", "-").replace(' ', ' ');
		} else {
			convertedTask += String.format("%-30s", task.getLocation()).replace(' ', ' ');
		}

		convertedTask += "|";

		if(task.getType() == null) { //type
			convertedTask += String.format("%-12s", "-").replace(' ', ' ');
		} else {
			convertedTask += String.format("%-12s", task.getType()).replace(' ', ' ');
		}

		convertedTask += "|";

		if(task.getIsDone() == false) { //isDone
			convertedTask += "undone";
		} else {
			convertedTask += "done";
		}

		return convertedTask;
	}
	
	private void displayPopUpMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

	//BACK UP PURPOSES - Save a copy of backup list from original list
	private void updateBackUpTaskList() {
		backUpTaskList.clear();
		for(int i=0; i<taskList.size(); i++) {
			backUpTaskList.add(taskList.get(i));
		}
	}

	//BACK UP PURPOSES - Re-add tasks to original list from backup list
	private void reAddTaskListFromBackUp() {
		taskList.clear();
		for(int i=0; i<backUpTaskList.size(); i++) {
			taskList.add(backUpTaskList.get(i));
		}
	}

	//BACK UP PURPOSES - Re-write file from backup list
	private boolean writeBackUpFile() {
		for(int i=0; i<backUpTaskList.size(); i++) {
			writeATaskToFile(backUpTaskList.get(i), true);
		}
		return true;
	}
}