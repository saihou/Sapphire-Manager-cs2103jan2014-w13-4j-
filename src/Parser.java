import java.util.ArrayList;

/**
 * @author Cai Di
 */

public class Parser {

	public String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[0];
	}

	private int lastCmdIndex(int from, int to, int date, int loc, int deadline) {
		int index = Math.max(from,
				Math.max(to, Math.max(date, Math.max(loc, deadline))));
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
		int indexOfTo = cmd.indexOf("to");
		int indexOfDate = cmd.indexOf("/on");
		int indexOfLocation = cmd.indexOf("/loc");
		int indexOfDeadline = cmd.indexOf("/at");

		int index = lastCmdIndex(indexOfFrom, indexOfTo, indexOfDate,
				indexOfLocation, indexOfDeadline);

		try {
			if (indexOfDeadline != -1) {
				String[] temp = cmd.split("/at");
				String deadLine = getFirstWord(temp[1]);
				myTask.setDeadline(deadLine);
				if (indexOfDeadline != index) {
					cmd = temp[0] + cmd.split(deadLine)[1];
					cmd = cmd.trim();
				} else
					cmd = temp[0].trim();
			}

			// store the starting time
			if (indexOfFrom != -1) {
				String[] temp = cmd.split("/from");
				String startTime = getFirstWord(temp[1]);
				myTask.setStartTime(startTime);
				if (indexOfFrom != index) {
					cmd = temp[0] + cmd.split(startTime)[1];
					cmd = cmd.trim();
				} else
					cmd = temp[0].trim();
			}

			// store the ending time
			if (indexOfTo != -1) {
				String[] temp = cmd.split("to");
				String endTime = getFirstWord(temp[1]);
				myTask.setEndTime(endTime);
				if (indexOfTo != index) {
					cmd = temp[0] + cmd.split(endTime)[1];
					cmd = cmd.trim();
				} else
					cmd = temp[0].trim();
			}

			// store the date
			if (indexOfDate != -1) {
				String[] temp = cmd.split("/on");
				String date = getFirstWord(temp[1]);
				myTask.setDate(date);
				if (indexOfDate != index) {
					cmd = temp[0] + cmd.split(date)[1];
					cmd = cmd.trim();
				} else
					cmd = temp[0].trim();
			}
			// store the location
			if (indexOfLocation != -1) {
				System.out.println(cmd);
				String location = cmd.split("/loc")[1].trim();
				if (cmd.indexOf("/") != -1)
					location = location.split("/")[0].trim();

				myTask.setLocation(location);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
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
