/**
 * @author Cai Di
 * This class is created to analyze user input string.  
 * It extracts task details and information from user input string.
 */

import java.util.ArrayList;

public class CommandParser {
	// get instance of gui @Dex
	//private SapphireManagerGUI gui = SapphireManagerGUI.getInstance();

	// constructor
	public CommandParser(){
	}
    
	// returns the first word of String userInput 
	public String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[0];
	}
    
	// Analyze userInput and assign task details to myTask object
	public void parse(String userInput, Task myTask) {
		// String command is the update version of userInput string after each extraction
		String command = "";
        
		// set task name update command to userInput string without task name
		command = setTaskName(userInput, myTask, command);
        
		// set task details to myTask object
		setTaskDetails(myTask, command);
        
		// call parseType method to set the type of the task
		setType(myTask);

	}
    
	// set task name and update String command to userInput without task name
	private String setTaskName(String userInput, Task myTask, String command) {
		int indexOfFirstSlash = userInput.trim().indexOf("/");

		// no task name but have slash
		if (indexOfFirstSlash == 0) {
			command = userInput.trim();
		}

		// have task name and have slash
		else if (indexOfFirstSlash != -1) {
			String taskName = userInput.substring(0, indexOfFirstSlash);
			taskName = taskName.trim();
			myTask.setName(taskName);

			command = userInput.substring(indexOfFirstSlash);
		}

		// have task name but no slash
		else {
			String taskName = userInput.trim();
			myTask.setName(taskName);
		}
		return command;
	}
    
	// set task date, deadline, location, duration
	private void setTaskDetails(Task myTask, String command) {
		// index of the various commands in the command string
		int indexOfFrom     = command.indexOf("/from");
		int indexOfDate     = command.indexOf("/on");
		int indexOfLocation = command.indexOf("/loc");
		int indexOfDeadline = command.indexOf("/at");

		try {
			// set start & end time
			command = setDuration(myTask, command, indexOfFrom);

			// set task date
			command = setDate(myTask, command, indexOfDate);

			// set task deadline
			command = setDeadline(myTask, command, indexOfDeadline);

			// set task location
			setLocation(myTask, command, indexOfLocation);
			
		} catch (StringIndexOutOfBoundsException
				| ArrayIndexOutOfBoundsException e) {
			System.out.println("Uer input is not valid!"); 
		}
	}
    
	//set task location
	private void setLocation(Task myTask, String command, int indexOfLocation) {
		if (indexOfLocation != -1) {
			String location = command.split("/loc")[1].trim();
			myTask.setLocation(location);
		}
	}
    
	// set task deadline
	private String setDeadline(Task myTask, String command, int indexOfDeadline) {
		if (indexOfDeadline != -1) {
			String[] temp = command.split("/at");

			String deadline = temp[1].trim().substring(0, 4);
			myTask.setDeadline(deadline);

			temp[1] = temp[1].trim().substring(4).trim();
			command = temp[0] + temp[1];
		}
		return command;
	}
    
	// set task date
	private String setDate(Task myTask, String command, int indexOfDate) {
		if (indexOfDate != -1) {
			String[] temp = command.split("/on");

			String date = temp[1].trim().substring(0, 6);
			myTask.setDate(date);

			temp[1] = temp[1].trim().substring(6).trim();
			command = temp[0] + temp[1];

		}
		return command;
	}
    
	//set the start & end time 
	private String setDuration(Task myTask, String command, int indexOfFrom) {
		if (indexOfFrom != -1) {
			// split the command string into two Sting temp[0] & temp[1]
			String[] temp = command.split("/from");

			// extract first 4 digits as starting time
			temp[1] = temp[1].trim();
			String startTime = temp[1].substring(0, 4);
			myTask.setStartTime(startTime);

			// extract start time and update temp[1]
			temp[1] = temp[1].trim().substring(4).trim();
			temp[1] = temp[1].substring(2).trim();

			String endTime = temp[1].substring(0, 4);
			myTask.setEndTime(endTime);

			// extract start & end time and update String command 
			command = temp[0] + temp[1].substring(4).trim();
		}
		return command;
	}

	// set task type
	private void setType(Task myTask) {

		if (myTask.getDeadline() != null) {
			myTask.setType("targetedTime");
		}

		else if (myTask.getStartTime() == null && myTask.getDeadline() == null
				&& myTask.getDate() == null) {
			myTask.setType("noSetTiming");
		}

		else if (myTask.getStartTime() == null && myTask.getDate() != null) {
			myTask.setType("fullDay");
		}

		else if (myTask.getStartTime() != null) {
			myTask.setType("setDuration");
		}

	}

	public boolean checkIfUserChoiceIsValid(ArrayList<Task> matchedTasks) {
		// stub
		return true;
	}

}
