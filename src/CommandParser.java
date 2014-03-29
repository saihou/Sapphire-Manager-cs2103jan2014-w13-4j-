/**
 * @author Cai Di
 * This class is created to analyze user input string.  
 * It extracts task details and information from user input string.
 */

import java.util.ArrayList;

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
	 * 
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
	 * 
	 * @param isFutureKeywordPresent
	 * @param isPastKeywordPresent
	 * @param isTodayKeywordPresent
	 * @param isAllKeywordPresent
	 * @return error message if number of keywords > 1,
	 * 		"all" if number of keywords == 0,
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
		
		if (isFutureKeywordPresent) {
			displayType = "future";
		} else if (isPastKeywordPresent) {
			displayType = "past";
		} else if (isTodayKeywordPresent) {
			displayType = "today";
		} else {
			displayType = "all";
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
	public String invalidFeedBack;
	public String[] taskDetails;

	/*
	 * taskDetials[] 
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
			taskDetails[0] = userInput.substring(0, indexOfFirstSlash);
			userInput = userInput.substring(indexOfFirstSlash);
		}

		// have task name but no slash
		else {
			taskDetails[0] = userInput.trim();
			userInput = null;
		}
		
		return userInput;
	}

	// now use validitycheck 
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

	private boolean isValidStatus(String status) {
		if (status.compareTo("done") == 0 || status.compareTo("undone") == 0) {
			return true;
		} else {
			return false;
		}
	}

	private void extractDuration(String inputFragment) {
		taskDetails[1] = getFirstWord(inputFragment.substring(4));
		if (!ValidationCheck.isValidTime(taskDetails[1]))
			invalidFeedBack = "Input starting time is not valid.";

		try {
			inputFragment = inputFragment.split("to")[1].trim();
			taskDetails[2] = getFirstWord(inputFragment);
			if (!ValidationCheck.isValidTime(taskDetails[2]))
				invalidFeedBack = "Input ending time is not valid.";
		} catch (ArrayIndexOutOfBoundsException e) {
			invalidFeedBack = "Input starting time without ending time.";
		}
	}

	private void extractDeadline(String inputFragment) {
		taskDetails[1] = getFirstWord(inputFragment.substring(2));
		if (!ValidationCheck.isValidTime(taskDetails[1]))
			invalidFeedBack = "Input deadline time is not valid.";
	}

	private void extractDate(String inputFragment) {
		taskDetails[4] = getFirstWord(inputFragment.substring(2));
		if (!ValidationCheck.isValidDate(taskDetails[4]))
			invalidFeedBack = "Input date is not valid";
	}

	private void extractStatus(String inputFragment) {
		taskDetails[5] = getFirstWord(inputFragment.substring(4));
		if (!isValidStatus(taskDetails[5]))
			invalidFeedBack = "Input status is not valid";
	}

	private void extractLocation(String inputFragment) {
		taskDetails[6] = inputFragment.substring(3).trim();
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
