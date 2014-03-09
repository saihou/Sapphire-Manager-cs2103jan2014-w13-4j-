import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author Teck Sheng (Dex)
 *	This class is the storage that that deals with the reading of file and writing to file
 */

public class Storage {
	private ArrayList<Task> taskList;
	private UserInterface userInterface;
	private String fileName = "mytextfile.txt";
	
	public Storage(String fileName, UserInterface userInterface) {
		this.userInterface = userInterface;
		taskList = new ArrayList<Task>();
		readFromFile();
	}
	
	public boolean readFromFile() {
		try {
			File file = new File(fileName);
			if(file.exists()) {
				userInterface.displayMessage("File exists."); 
				BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
				String line = bufferedReader.readLine();
				String[] splitedTaskInfo;
				while(line != null) {
					userInterface.displayMessage("Line: "+line);
					splitedTaskInfo = line.split("\\|");
					taskList.add(convertStringToTask(splitedTaskInfo));
					line = bufferedReader.readLine();
				}
				bufferedReader.close();
				return true;
			} else {
				userInterface.displayMessage("File did not exist."); 
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName), true));
				bufferedWriter.close();
				userInterface.displayMessage("File created."); 
				return false;
			}
		} catch(Exception ex) {
			userInterface.displayMessage("Error reading file! Please re-start program."); 
			return false;
		} 
	}
	
	private Task convertStringToTask(String[] splitedTaskInfo) {
		Task task = new Task();
		userInterface.displayMessage(splitedTaskInfo[0]);
		userInterface.displayMessage(splitedTaskInfo[1]);
		userInterface.displayMessage(splitedTaskInfo[2]);
		userInterface.displayMessage(splitedTaskInfo[3]);
		userInterface.displayMessage(splitedTaskInfo[4]);
		userInterface.displayMessage(splitedTaskInfo[5]);
		userInterface.displayMessage(splitedTaskInfo[6]);
		
		
		if(!splitedTaskInfo[0].equals("")) { //type
			task.setType(splitedTaskInfo[0]); 
		} 
		if(!splitedTaskInfo[1].equals("")) { //task name
			task.setTaskName(splitedTaskInfo[1]);
		} 
		if(!splitedTaskInfo[2].equals("")) { //date
			task.setDate(splitedTaskInfo[2]);
		} 
		if(!splitedTaskInfo[3].equals("")) { //start time
			task.setStartTime(splitedTaskInfo[3]);
		} 
		if(!splitedTaskInfo[4].equals("")) { //end time
			task.setEndTime(splitedTaskInfo[4]);
		} 
		if(!splitedTaskInfo[5].equals("")) { //deadline
			task.setDeadline(splitedTaskInfo[5]);
		} 
		if(!splitedTaskInfo[6].equals("")) { //location
			task.setLocation(splitedTaskInfo[6]);
		}
		
		return task;
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
	
	public boolean writeTaskListToFile() {
		if(taskList.size() == 0) { 
			//clear text file
			//BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName), false));
			//bufferedWriter.write(task.getType()+"|"+task.getName()+"|"+task.getDate()+"|"+task.getStartTime()+"|"+task.getEndTime()+"|"+task.getDeadline()+"|"+task.getLocation());
			//bufferedWriter.close();
			return true;
		} else if(taskList.size() > 0) { //write text file
			return true;
		} else { 
			userInterface.displayMessage("Error writing to file!");
			return false;
		}
	}
	
	public boolean writeATaskToFile(Task task) {
		try {
			String lol = convertTaskToString(task);
			userInterface.displayMessage(lol);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName), true));
			bufferedWriter.write(convertTaskToString(task));
			bufferedWriter.newLine();
			bufferedWriter.close();
			return true;
		} catch(Exception ex) {
			userInterface.displayMessage("Error writing to file!");
			return false;
		}
	}
	
	private String convertTaskToString(Task task) {
		String convertedTask = "";
		
		if(task.getType() == null) {
			convertedTask += "";
		} else {
			convertedTask += task.getType();
		}
		
		convertedTask += "|";
		
		if(task.getName() == null) {
			convertedTask += "";
		} else {
			convertedTask += task.getName();
		}
		
		convertedTask += "|";
		
		if(task.getDate() == null) {
			convertedTask += "";
		} else {
			convertedTask += task.getDate();
		}
		
		convertedTask += "|";
		
		if(task.getStartTime() == null) {
			convertedTask += "";
		} else {
			convertedTask += task.getStartTime();
		}
		
		convertedTask += "|";
		
		if(task.getEndTime() == null) {
			convertedTask += "";
		} else {
			convertedTask += task.getEndTime();
		}
		
		convertedTask += "|";
		
		if(task.getDeadline() == null) {
			convertedTask += "";
		} else {
			convertedTask += task.getDeadline();
		}
		
		convertedTask += "|";
		
		if(task.getLocation() == null) {
			convertedTask += "";
		} else {
			convertedTask += task.getLocation();
		}
		
		//userInterface.displayMessage(convertedTask);
		return convertedTask;
	}
	
	public boolean clearFile(String fileName) {
		return false;
	}
}
