import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class Storage {
	private ArrayList<Task> taskList;
	
	public boolean readFromFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = br.readLine();
			String[] splitedTaskInfo;
			while(line != null) {
				splitedTaskInfo = line.split("|");
				taskList.add(new Task(splitedTaskInfo[0].trim(), splitedTaskInfo[1].trim(), splitedTaskInfo[2].trim(), splitedTaskInfo[3].trim(), splitedTaskInfo[4].trim(), splitedTaskInfo[5].trim(), splitedTaskInfo[6].trim()));
				line = br.readLine();
			}
			br.close();
			return true;
		} catch(Exception ex) {
			System.out.println("Error reading file! Please re-start program.");
			return false;
		} 
	}
	
	public ArrayList<Task> getTaskList() {
		return taskList;
	}
	
	public boolean writeTaskListToFile() {
		if(taskList.size() == 0) {
			return true;
		} else if(taskList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean writeATaskToFile(Task task) {
		return true;
	}
	
	public boolean clearFile(String fileName) {
		return false;
	}
}
