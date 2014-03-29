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

		//default value is null
		for (int i = 0; i < taskDetails.length; i++) {
			taskDetails[i] = null;
		}
		
		input = extractName(input);
		
		if(input.indexOf("/") == -1){
			return;
		}
		
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
			taskDetails[0] = userInput.substring(0, indexOfFirstSlash).trim();
			userInput = userInput.substring(indexOfFirstSlash);
		}

		// have task name but no slash
		else {
			taskDetails[0] = userInput.trim();
			userInput = taskDetails[0];
		}
		return userInput;
	}
	
	private void extractDuration(String inputFragment) {
		taskDetails[1] = getFirstWord(inputFragment.substring(4));
		//if (!ValidationCheck.isValidTime(taskDetails[1]))
		//	invalidFeedBack = "Input starting time is not valid.";

		try {
			inputFragment = inputFragment.split("to")[1].trim();
			taskDetails[2] = getFirstWord(inputFragment);
			//if (!ValidationCheck.isValidTime(taskDetails[2])) {
			if (!ValidationCheck.isValidDuration(taskDetails[1], taskDetails[2])) {
				invalidFeedBack = "Input duration is not valid.";
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
