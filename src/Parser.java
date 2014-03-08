/**
 * @author Cai Di
 */

public class Parser {
		
	public String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[0];
	}
	
	public void parse(String cmd, Task myTask) {

		//store the task name
		String cmdWithoutTaskName="";
		int indexOfFirstSlash = cmd.indexOf("/");
		if (indexOfFirstSlash != -1) {
			String taskName = cmd.substring(0, indexOfFirstSlash);
			taskName = taskName.trim();			
			myTask.setTaskName(taskName);
			cmdWithoutTaskName = cmd.substring(indexOfFirstSlash);
		}
		else {
			myTask.setTaskName(cmd.substring(0));
		}
		
		//new cmd string without the task name
		//String cmdWithoutTaskName = cmd.substring(indexOfFirstSlash);
		
		int indexOfFrom = cmdWithoutTaskName.indexOf("from");
		int indexOfTo = cmdWithoutTaskName.indexOf("to");
		int indexOfDate = cmdWithoutTaskName.indexOf("on");
		int indexOfLocation = cmdWithoutTaskName.indexOf("loc");
		int indexOfDeadline = cmdWithoutTaskName.indexOf("at");
		
		if(indexOfFrom == -1 && indexOfDeadline ==-1 && indexOfDate == -1)
			myTask.setType("noSetTiming");
		if(indexOfFrom == -1 && indexOfDate != -1)
			myTask.setType("fullDay");
		if(indexOfFrom != -1 && indexOfDate == -1)
			myTask.setType("setDuration");
		if(indexOfDeadline != -1)
			myTask.setType("targetedTime");
		//store the deadline
		if(indexOfDeadline != -1)
			myTask.setDeadline(getFirstWord(cmdWithoutTaskName.substring(indexOfFrom+2)));
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


}
