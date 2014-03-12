import java.util.ArrayList;

/**
 * @author Cai Di
 */

public class Parser {

	public String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[0];
	}

	private int lastCmdIndex(int from, int date, int loc, int deadline) {
		int index = Math.max(from, Math.max(date, Math.max(loc, deadline)));
		return index;
	}

	public void parse(String userInput, Task myTask) {

		// store the task name
		String cmd = "";
		int indexOfFirstSlash = userInput.trim().indexOf("/");
		if (indexOfFirstSlash == 0) {
			cmd = userInput.trim();
		}
		else if(indexOfFirstSlash != -1) {
			String taskName = userInput.substring(0, indexOfFirstSlash);
			taskName = taskName.trim();
			myTask.setName(taskName);

			cmd = userInput.substring(indexOfFirstSlash);
		}
		else {
			String taskName = userInput.trim();
			myTask.setName(taskName);
		}
		
		// new cmd string without the task name
		// String cmd = cmd.substring(indexOfFirstSlash);

		int indexOfFrom = cmd.indexOf("/from");
		int indexOfDate = cmd.indexOf("/on");
		int indexOfLocation = cmd.indexOf("/loc");
		int indexOfDeadline = cmd.indexOf("/at");

		int index = lastCmdIndex(indexOfFrom, indexOfDate,
				indexOfLocation, indexOfDeadline);
        try{
		if(indexOfFrom != -1){
			String[] temp = cmd.split("/from");
			String startTime = temp[1].trim().substring(0,4);
			myTask.setStartTime(startTime);
			temp[1] = temp[1].trim().substring(4).trim();
			temp[1] = temp[1].substring(2).trim();
			String endTime = temp[1].substring(0,4);
			myTask.setEndTime(endTime);
			cmd = temp[0] + temp[1].substring(4).trim();
		}
		
		if(indexOfDate != -1){
			String[] temp = cmd.split("/on");
			String date = temp[1].trim().substring(0,6);
			myTask.setDate(date);
			temp[1] = temp[1].trim().substring(6).trim();
			cmd = temp[0]+temp[1];
			
			
		}
		if(indexOfDeadline != -1){
			String[] temp = cmd.split("/at");
			String deadline = temp[1].trim().substring(0,4);
			myTask.setDeadline(deadline);
			temp[1] = temp[1].trim().substring(4).trim();
			cmd = temp[0]+temp[1];
			
		}
		if(indexOfLocation != -1){
			String location = cmd.split("/loc")[1].trim();
			myTask.setLocation(location);
			
		}
        }catch(StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e){
        	System.out.println("invalid input");
        }

		parseType(myTask);

	}

	// set task type
	private void parseType(Task myTask) {

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
