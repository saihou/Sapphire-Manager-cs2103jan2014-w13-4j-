/**
 * @author Cai Di
 * This class is created to analyze user input string.  
 * It extracts task details and information from user input string.
 */

public class CommandParser {
	
	// get instance of gui @Dex

	// constructor
	public CommandParser(){
	}
    
	// returns the first word of String userInput 
	public String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[0];
	}
	/**
	 * @param userCommand
	 * @return displayType, "all", "future", "past" or "today"
	 * @author Sai Hou
	 */
	public String parseDisplayType(String userCommand) {
		String displayType = "";
		
		userCommand = prepareUserCommandForParsing(userCommand);
		
		boolean isAllKeywordPresent = userCommand.contains("/all");
		boolean isFutureKeywordPresent = userCommand.contains("/future");
		boolean isPastKeywordPresent = userCommand.contains("/past");
		boolean isTodayKeywordPresent = userCommand.contains("/today");
		
		displayType = determineDisplayType(isFutureKeywordPresent,
				isPastKeywordPresent, isTodayKeywordPresent, isAllKeywordPresent); 
		
		return displayType;
	}

	/**
	 * @param isFutureKeywordPresent
	 * @param isPastKeywordPresent
	 * @param isTodayKeywordPresent
	 * @param isAllKeywordPresent
	 * @return error message if number of keywords > 1,
	 * 		"future" if number of keywords == 0,
	 * 		the respective display type otherwise.
	 */
	private String determineDisplayType(boolean isFutureKeywordPresent,
			boolean isPastKeywordPresent, boolean isTodayKeywordPresent,
			boolean isAllKeywordPresent) {
		String displayType;
		
		int numberOfKeywordsPresent = getNumberOfKeywordsPresent(isAllKeywordPresent, isFutureKeywordPresent,
				isPastKeywordPresent, isTodayKeywordPresent);
		
		assert numberOfKeywordsPresent >=0;
		
		if (numberOfKeywordsPresent > 1 ) {
			displayType = "Invalid number of keywords!!";
			return displayType;
		}
		
		if (isAllKeywordPresent) {
			displayType = "all";
		} else if (isPastKeywordPresent) {
			displayType = "past";
		} else if (isTodayKeywordPresent) {
			displayType = "today";
		} else {
			displayType = "future";
		}
		return displayType;
	}

	private int getNumberOfKeywordsPresent(boolean isAllKeywordPresent,
			boolean isFutureKeywordPresent, boolean isPastKeywordPresent,
			boolean isTodayKeywordPresent) {
		return (isAllKeywordPresent ? 1:0) + 
				(isFutureKeywordPresent ? 1:0) +
				(isPastKeywordPresent ? 1:0) + 
				(isTodayKeywordPresent ? 1:0);
	}

	private String prepareUserCommandForParsing(String userCommand) {
		return userCommand.trim().toLowerCase();
	}
	
	
	/**
	 * @author Cai Di
	 */
	protected String invalidFeedBack;
	protected String[] taskDetails;

	/*
	 * taskDetails[] 
	 * [0] = task name 
	 * [1] = task start time 
	 * [2] = task end time
	 * [3] = task date 
	 * [4] = task done/undone 
	 * [5] = task location
	 */

	public void extractTaskDetails(String input) {
		taskDetails = new String[6];
		invalidFeedBack = null;

		input = extractName(input);
		
		String[] temp = input.split("/");
		
		for (int i = 0; i < temp.length; i++) {
			// exit method if input is not valid
			if (invalidFeedBack != null)
				return;
			
			switch (getFirstWord(temp[i])) {
			case "from":
				extractDuration(temp[i]);
				break;
			case "at":
				extractDeadline(temp[i]);
				break;
			case "on":
				extractDate(temp[i]);
				break;
			case "mark":
				extractStatus(temp[i]);
				break;
			case "loc":
				extractLocation(temp[i]);
				break;
			}
		}
	}

	private String extractName(String userInput) {
		int indexOfFirstSlash = userInput.trim().indexOf("/");

		// no task name but have slash
		if (indexOfFirstSlash == 0) {
			userInput = userInput.trim();
			taskDetails[0] = null;
		}

		// have task name and have slash
		else if (indexOfFirstSlash != -1) {
			taskDetails[0] = userInput.substring(0, indexOfFirstSlash);
			userInput = userInput.substring(indexOfFirstSlash);
		}

		// have task name but no slash
		else {
			taskDetails[0] = userInput.trim();
			userInput = taskDetails[0];
		}
		
		return userInput;
	}
/*
	private boolean isFourDigitInteger(String time) {
		boolean feedBack = true;
		if (time.length() != 4) {
			feedBack = false;
		} else {
			try {
				Integer.parseInt(time);
			} catch (NumberFormatException e) {
				feedBack = false;
			}
		}
		return feedBack;
	}

	private boolean isSixDigitInteger(String date) {
		boolean feedBack = true;
		if (date.length() != 6) {
			feedBack = false;
		} else {
			try {
				Integer.parseInt(date);
			} catch (NumberFormatException e) {
				feedBack = false;
			}
		}
		return feedBack;
	}
*/
	private void extractDuration(String inputFragment) {
		taskDetails[1] = getFirstWord(inputFragment.substring(4));
		if (!ValidationCheck.isValidTime(taskDetails[1]))
			invalidFeedBack = "Input starting time is not valid.";

		try {
			inputFragment = inputFragment.split("to")[1].trim();
			taskDetails[2] = getFirstWord(inputFragment);
			if (!ValidationCheck.isValidTime(taskDetails[2])) {
				invalidFeedBack = "Input ending time is not valid.";
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			invalidFeedBack = "Input starting time without ending time.";
		}
	}

	private void extractDeadline(String inputFragment) {
		taskDetails[1] = getFirstWord(inputFragment.substring(2));
		if (!ValidationCheck.isValidTime(taskDetails[1])) {
			invalidFeedBack = "Input deadline time is not valid.";
		}
	}

	private void extractDate(String inputFragment) {
		taskDetails[3] = getFirstWord(inputFragment.substring(2));
		if (!ValidationCheck.isValidDate(taskDetails[3])) {
			invalidFeedBack = "Input date is not valid";
		}
	}

	private void extractStatus(String inputFragment) {
		taskDetails[4] = getFirstWord(inputFragment.substring(4));
		if (!ValidationCheck.isValidStatus(taskDetails[4])) {
			invalidFeedBack = "Input status is not valid";
		}
	}

	private void extractLocation(String inputFragment) {
		taskDetails[5] = inputFragment.substring(3).trim();
	}	
}

	/**
	 * @author Cai Di
	 */

/*
    // Analyze userInput and assign task details to myTask object
	public String parse(String userInput, Task myTask) {
		String systemFeedback = "";
		
		// String command is the update version of userInput string after each extraction
		String command = "";
        
		// set task name update command to userInput string without task name
		command = setTaskName(userInput, myTask, command);
        
		// set task details to myTask object
		setTaskDetails(myTask, command);
        
		// call parseType method to set the type of the task
		setType(myTask);

		return systemFeedback;
	}
    
	//stub
	public String[] parse() {
		String[] stub = new String[5];
		
		for (int i = 0;i < stub.length; i++) {
			stub[i] = null;
		}
		
		stub[0] = "Task name";
		stub[1] = "270314";
		
		return stub;
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
		int indexOfIsDone   = command.indexOf("/mark");

		try {
			// set start & end time
			command = setDuration(myTask, command, indexOfFrom);

			// set task date
			command = setDate(myTask, command, indexOfDate);

			// set task deadline
			command = setDeadline(myTask, command, indexOfDeadline);
			
			// mark task as done/undone
			command = setIsDone(myTask, command, indexOfIsDone);

			// set task location
			setLocation(myTask, command, indexOfLocation);
			
		} catch (StringIndexOutOfBoundsException
				| ArrayIndexOutOfBoundsException e) {
			System.out.println("User input is not valid!"); 
		}
	}
    
	// set task location
	private void setLocation(Task myTask, String command, int indexOfLocation) {
		if (indexOfLocation != -1) {
			String location = command.split("/loc")[1].trim();
			myTask.setLocation(location);
		}
	}
	
	// mark task as done/undone
	private String setIsDone(Task myTask, String command, int indexOfIsDone) {
		if(indexOfIsDone != -1) {
			String[] temp = command.split("/mark");
			
			String isDone = getFirstWord(temp[1].trim());
			if(isDone.compareTo("done")==0){
				myTask.setIsDone(true);
				temp[1] = temp[1].trim().substring(4).trim();
				command = temp[0] + temp[1];
			}
			else{
				myTask.setIsDone(false);
				temp[1] = temp[1].trim().substring(6);
				command = temp[0] + temp[1];
			}
		}
		return command;
	}
   
	// set task deadline
	private String setDeadline(Task myTask, String command, int indexOfDeadline) {
		if (indexOfDeadline != -1) {
			String[] temp = command.split("/at");

			String deadline = temp[1].trim().substring(0, 4);
			
			boolean isTimeValid = ValidationCheck.isValidTime(deadline);
			System.out.println("DeadlineTimeValidate:"+isTimeValid);
			
			myTask.setStartTime(deadline);

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
			
			boolean isDateValid = ValidationCheck.isValidDate(date);
			System.out.println("DateValidate:"+isDateValid);
			
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
			
			boolean isTimeValid = ValidationCheck.isValidTime(startTime);
			System.out.println("StartTimeValidate:"+isTimeValid);
			
			myTask.setStartTime(startTime);

			// extract start time and update temp[1]
			temp[1] = temp[1].trim().substring(4).trim();
			temp[1] = temp[1].substring(2).trim();

			String endTime = temp[1].substring(0, 4);
			
			isTimeValid = ValidationCheck.isValidTime(endTime);
			System.out.println("EndTimeValidate:"+isTimeValid);
			
			myTask.setEndTime(endTime);

			// extract start & end time and update String command 
			command = temp[0] + temp[1].substring(4).trim();
		}
		return command;
	}

	// set task type
	private void setType(Task myTask) {

		if (myTask.getStartTime() != null  && myTask.getEndTime() == null) {
			myTask.setType("targetedTime");
		}

	    else if (myTask.getStartTime() == null && myTask.getEndTime() == null
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
}

*/
