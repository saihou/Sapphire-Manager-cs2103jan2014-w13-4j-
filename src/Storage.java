//@author A0097706U
//This class is the storage that that deals with the reading of file and writing to file

//JAVA-UTIL LIBRARIES
import java.util.ArrayList;

//JAVA-IO LIBRARIES
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

//JAVAX-SWING LIBRARIES
import javax.swing.JOptionPane;

public class Storage {
	//@author A0097706U
	//default file name - cannot change
	private final static String FILE_NAME = "mytextfile.txt";
	private final static String FILE_ERROR_BACKUP_NAME = "mytextfileWithError.txt";

	//error messages
	private final static String FILE_CLEARING_ERROR = "Error clearing file!";
	private final static String FILE_INITIALIZING_ERROR = "Error initializing file!";
	private final static String FILE_READING_ERROR = "Error reading file!";
	private final static String FILE_SAVING_ERROR = "Error saving file!";

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

	//@author A0097706U
	//constructor to initialize arraylist and file
	private Storage() {
		taskList = new ArrayList<Task>();
		backUpTaskList = new ArrayList<Task>();
		readFile();
	}

	//@author A0097706U
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

	//@author A0097706U
	//gets tasklist
	public ArrayList<Task> getTaskList() {
		return taskList;
	}

	//@author A0097706U
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

	//@author A0097706U
	//writes String to file
	private boolean writeHeader(String header, boolean toAppend) {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(FILE_NAME), toAppend));
			bufferedWriter.write(header);
			bufferedWriter.newLine();
			bufferedWriter.close();
			return true;
		} catch(Exception ex) {
			//displayPopUpMsg(FILE_SAVING_ERROR);
			System.out.println(FILE_SAVING_ERROR);
			return false;
		}
	}

	//@author A0097706U
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
			//displayPopUpMsg(FILE_SAVING_ERROR);
			System.out.println(FILE_SAVING_ERROR);
			return false;
		}
	}

	//@author A0097706U
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

	//@author A0097706U
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

	//@author A0097706U
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
			//displayPopUpMsg(FILE_READING_ERROR);
			System.out.println(FILE_READING_ERROR);
			return false;
		}
	}

	//@author A0097706U
	//initialize file
	private boolean initializeFile() {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file, false));
			writeHeader(FILE_HEADER, false);
			writeHeader(FILE_DIVIDER, true);
			bufferedWriter.close();
			return true;
		} catch (Exception ex) {
			//displayPopUpMsg(FILE_INITIALIZING_ERROR);
			System.out.println(FILE_INITIALIZING_ERROR);
			return false;
		}
	}

	//@author A0097706U
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
			//displayPopUpMsg(FILE_READING_ERROR);
			System.out.println(FILE_READING_ERROR);
			return false;
		}
	}

	//@author A0097706U
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

	//@author A0097706U
	//clear and re-initialize file
	private boolean clearFile() {
		try {
			file.delete();
			file = new File(FILE_NAME);
			initializeFile();
			return true;
		} catch (Exception ex) {
			//displayPopUpMsg(FILE_CLEARING_ERROR);
			System.out.println(FILE_CLEARING_ERROR);
			return false;
		}
	}

	//@author A0097706U
	//clear ArrayList
	private boolean clearTaskList() {
		try {
			taskList.clear();
			backUpTaskList.clear();
			return true;
		} catch (Exception ex) {
			//displayPopUpMsg(FILE_CLEARING_ERROR);
			System.out.println(FILE_CLEARING_ERROR);
			reAddTaskListFromBackUp();
			return false;
		}
	}

	//@author A0097706U
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

	//@author A0097706U
	//converts String to Task
	public Task convertStringToTask(String[] splitedTaskInfo) {
		task = new Task();	

		if(checkStringTaskEntries(splitedTaskInfo)) {
			if(!splitedTaskInfo[0].trim().equals("-")) { //date
				task.setDate(splitedTaskInfo[0].trim());
			} else {
				task.setDate(null);
			}

			if(!splitedTaskInfo[1].trim().equals("-")) { //start time
				task.setStartTime(splitedTaskInfo[1].trim());
			} else {
				task.setStartTime(null);
			}

			if(!splitedTaskInfo[2].trim().equals("-")) { //end time
				task.setEndTime(splitedTaskInfo[2].trim());
			} else {
				task.setEndTime(null);
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
				task.setIsDone(true);
			} else {
				task.setIsDone(false);
			}

			return task;
		} else {
			return null;
		}
	}

	//@author A0097706U
	//converts Task to String
	public String convertTaskToString(Task task) {
		String convertedTask = "";
		if(checkTaskEntries(task)) {
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
		} else {
			return null;
		}
	}

	//@author A0097706U
	//checks Task's entries to ensure they are valid
	private boolean checkTaskEntries(Task task) {
		boolean isValid = true;

		if(task.getName() == null || task.getType() == null) {
			isValid = false;
		}

		if(task.getDate() != null && !ValidationCheck.isValidDate(task.getDate())) {
			isValid = false;
		}

		if(task.getStartTime() != null && !ValidationCheck.isValidTime(task.getStartTime())) {
			isValid = false;
		}

		if(task.getEndTime() != null && !ValidationCheck.isValidTime(task.getEndTime())) {
			isValid = false;
		}

		if((task.getStartTime() != null && task.getEndTime() != null) && !ValidationCheck.isValidDuration(task.getStartTime(), task.getEndTime())) {
			isValid = false;
		}

		if(task.getLocation() != null && !ValidationCheck.isValidLocation(task.getLocation())) {
			isValid = false;
		}

		if(task.getType() != null && !ValidationCheck.isValidType(task.getType())) {
			isValid = false;
		}

		if(isValid) {
			return true;
		} else {
			return false;
		}
	}

	//@author A0097706U
	//checks String's entries are valid before placing them in Task
	private boolean checkStringTaskEntries(String[] splitedTaskInfo) {
		boolean isValid = true;

		if(!splitedTaskInfo[0].trim().equals("-") && !ValidationCheck.isValidDate(splitedTaskInfo[0].trim())) {
			isValid = false;
		}

		if(!splitedTaskInfo[1].trim().equals("-") && !ValidationCheck.isValidTime(splitedTaskInfo[1].trim())) {
			isValid = false;
		}

		if(!splitedTaskInfo[2].trim().equals("-") && !ValidationCheck.isValidTime(splitedTaskInfo[2].trim())) {
			isValid = false;
		}
		
		if((!splitedTaskInfo[1].trim().equals("-") && !splitedTaskInfo[2].trim().equals("-")) && !ValidationCheck.isValidDuration(splitedTaskInfo[1].trim(), splitedTaskInfo[2].trim())) {
			isValid = false;
		}

		if(!splitedTaskInfo[4].trim().equals("-") && !ValidationCheck.isValidLocation(splitedTaskInfo[4].trim())) {
			isValid = false;
		}

		if(!splitedTaskInfo[5].trim().equals("-") && !ValidationCheck.isValidType(splitedTaskInfo[5].trim())) {
			isValid = false;
		}

		if(!splitedTaskInfo[6].trim().equals("-") && !ValidationCheck.isValidStatus(splitedTaskInfo[6].trim())) {
			isValid = false;
		}

		if(isValid) {
			return true;
		} else {
			return false;
		}
	}
	
	//@author A0097706U
	//display pop up message for errors
	private void displayPopUpMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

	//@author A0097706U
	//BACK UP PURPOSES - Save a copy of backup list from original list
	private void updateBackUpTaskList() {
		backUpTaskList.clear();
		for(int i=0; i<taskList.size(); i++) {
			backUpTaskList.add(taskList.get(i));
		}
	}

	//@author A0097706U
	//BACK UP PURPOSES - Re-add tasks to original list from backup list
	private void reAddTaskListFromBackUp() {
		taskList.clear();
		for(int i=0; i<backUpTaskList.size(); i++) {
			taskList.add(backUpTaskList.get(i));
		}
	}

	//@author A0097706U
	//BACK UP PURPOSES - Re-write file from backup list
	private boolean writeBackUpFile() {
		for(int i=0; i<backUpTaskList.size(); i++) {
			writeATaskToFile(backUpTaskList.get(i), true);
		}
		return true;
	}
}