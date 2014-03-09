/**
 * @author Cai Di
 */

public class Parser {
		
	public String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[0];
	}
	
	public void parse(String userInput, Task myTask) {

		//store the task name
		String cmd="";
		int indexOfFirstSlash = userInput.indexOf("/");
		if (indexOfFirstSlash != -1) {
			String taskName = userInput.substring(0, indexOfFirstSlash);
			taskName = taskName.trim();			
			myTask.setName(taskName);
			cmd = userInput.split("/")[1];
		}
		else {
			myTask.setName(userInput.substring(0));
		}
		
		//new cmd string without the task name
		//String cmd = cmd.substring(indexOfFirstSlash);
		
		int indexOfFrom = cmd.indexOf("from");
		int indexOfTo = cmd.indexOf("to");
		int indexOfDate = cmd.indexOf("on");
		int indexOfLocation = cmd.indexOf("loc");
		int indexOfDeadline = cmd.indexOf("at");
		
		//store the deadline
		if(indexOfDeadline != -1){
			String[] temp = cmd.split("at");
			String deadLine = getFirstWord(temp[1]);
			myTask.setDeadline(deadLine);
			cmd = temp[0]+cmd.split(deadLine)[1];
			cmd = cmd.trim();
		}
		
		//store the starting time
		if (indexOfFrom != -1){
			String[] temp = cmd.split("from");
			String startTime = getFirstWord(temp[1]);
			myTask.setStartTime(startTime);
			cmd = temp[0]+cmd.split(startTime)[1];
			cmd = cmd.trim();
		}
		
		//store the ending time
		if (indexOfTo != -1){
			String[] temp = cmd.split("to");
			String endTime = getFirstWord(temp[1]);
			myTask.setEndTime(endTime);
			cmd = temp[0]+cmd.split(endTime)[1];
			cmd = cmd.trim();
		}
		//store the date
		if (indexOfDate != -1){
			String[] temp = cmd.split("on");
			String date = getFirstWord(temp[1]);
			myTask.setDate(date);
			cmd = temp[0]+cmd.split(date)[1];
			cmd = cmd.trim();
		}
		//store the location
		if (indexOfLocation != -1){
			String location = cmd.split("loc")[1].trim();
			myTask.setLocation(location);
		}
		
		parseType(myTask);
		
	}
	
	//set task type
	private void parseType(Task myTask){
		
		if(myTask.getDeadline() != null){
			myTask.setType("targetedTime");
		}
		
	    else if(myTask.getStartTime() == null && myTask.getDeadline() == null && myTask.getDate() == null){
			myTask.setType("noSetTimeing");
		}
		
		else if(myTask.getStartTime() == null && myTask.getDate() != null){
			myTask.setType("fullDay");
		}
		
		else if(myTask.getStartTime() != null){
			myTask.setType("setDuration");
		}

	}


}
