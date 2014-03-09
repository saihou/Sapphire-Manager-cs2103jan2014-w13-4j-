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
	private final static String FILENAME = "mytextfile.txt";
	
	private ArrayList<Task> taskList;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private File file, newFile;
	private String fileName = FILENAME;
	private Task task;
	private UserInterface userInterface;

	public Storage(String fileName, UserInterface userInterface) {
		this.userInterface = userInterface;
		taskList = new ArrayList<Task>();
		readFromFile();
	}

	public ArrayList<Task> getTaskList() {
		return taskList;
	}

	public boolean setTaskList(Task task) {
		if(task == null) {
			return false;
		} else {
			taskList.add(task);
			return true;
		}
	}
	
	/*
	public void printArrayList() {
		Task task1;
		for(int i=0; i<taskList.size(); i++) {
			task1 = taskList.get(i);
			System.out.println("No: "+i+
								"\nType: "+task1.getType()+
								"\nName: "+task1.getName()+
								"\nDate: "+task1.getDate()+
								"\nStart Time: "+task1.getStartTime()+
								"\nEnd Time: "+task1.getEndTime()+
								"\nDeadline: "+task1.getDeadline()+
								"\nLocation: "+task1.getLocation()+"\n");
		}
	}
	*/
	
	public boolean readFromFile() {		
		try {
			file = new File(fileName);
			if(file.exists()) {
				userInterface.displayMessage("[Test Message]: File exists."); 
				bufferedReader = new BufferedReader(new FileReader(fileName));
				String line = bufferedReader.readLine();
				String[] splitedTaskInfo;

				while(line != null) {
					splitedTaskInfo = line.split("\\|");
					taskList.add(convertStringToTask(splitedTaskInfo));
					line = bufferedReader.readLine();
				}
				bufferedReader.close();
				userInterface.displayMessage("[Test Message]: File initialized.");
				return true;
			} else {
				userInterface.displayMessage("[Test Message]: File did not exist."); 
				bufferedWriter = new BufferedWriter(new FileWriter(file, true));
				bufferedWriter.close();
				userInterface.displayMessage("[Test Message]: File created."); 
				return false;
			}
		} catch(Exception ex) {
			userInterface.displayMessage("Error reading file! Please re-start program."); 
			return false;
		}
	}

	//toAppend: 
	//true - append to existing file
	//false - rewrite the file
	public boolean writeATaskToFile(Task task, boolean toAppend) {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName), toAppend));
			bufferedWriter.write(convertTaskToString(task));
			bufferedWriter.newLine();
			bufferedWriter.close();
			return true;
		} catch(Exception ex) {
			userInterface.displayMessage("Error writing to file!");
			return false;
		}
	}

	public boolean writeTaskListToFile() {
		if(taskList.size() == 0) { 
			if(clearFile()) {
				return true;
			} else {
				return false;
			}
		} else if(taskList.size() > 0) { //write text file
			for(int i=0; i<taskList.size(); i++) {
				if(i == 0) {
					writeATaskToFile(taskList.get(i), false);
				} else {
					writeATaskToFile(taskList.get(i), true);
				}
			}
			return true;
		} else { 
			userInterface.displayMessage("Error writing to file!");
			return false;
		}
	}

	public boolean clearFile() {
		try {
			file = new File(fileName);
			if(file.delete()) {
				newFile = new File(fileName);
				if(newFile.createNewFile()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception ex) {
			userInterface.displayMessage("Error deleting file.");
			return false;
		}
	}
	
	private void pr(String msg) {
		System.out.println(msg);
	}

	private Task convertStringToTask(String[] splitedTaskInfo) {
		task = new Task();	
		pr(Integer.toString(splitedTaskInfo.length));
		
		if(!splitedTaskInfo[0].equals("[null]")) { //type
			pr(splitedTaskInfo[0]);
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