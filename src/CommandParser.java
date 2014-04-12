/**
 * @author Cai Di
 * This class is created to analyze user input string.  
 * It extracts task details and information from user input string.
 */

public class CommandParser {
	// constructor
	public CommandParser(){
	}
    
	// returns the first word of String userInput 
	public String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[0];
	}
	
	/**
	 * @param userCommand
	 * @return displayType, "all", "undone", "done" or "today"
	 */
	 //@author Sai Hou
	public String parseDisplayType(String userCommand) {
		String displayType = "";
		
		userCommand = prepareUserCommandForParsing(userCommand);
		
		boolean isAllKeywordPresent = userCommand.equals("all");
		boolean isUndoneKeywordPresent = userCommand.equals("undone") || userCommand.equals("");
		boolean isDoneKeywordPresent = userCommand.equals("done");
		boolean isTodayKeywordPresent = userCommand.equals("today");
		boolean isOverdueKeywordPresent = userCommand.equals("overdue");
		boolean isMemosKeywordPresent = userCommand.equals("memos");
		
		displayType = determineDisplayType(isUndoneKeywordPresent,
				isDoneKeywordPresent, isTodayKeywordPresent, isAllKeywordPresent,
				isOverdueKeywordPresent, isMemosKeywordPresent); 
		
		return displayType;
	}

	/**
	 * @param isUndoneKeywordPresent
	 * @param isDoneKeywordPresent
	 * @param isTodayKeywordPresent
	 * @param isAllKeywordPresent
	 * @param isOverdueKeywordPresent
	 * @param isMemoesKeywordPresent
	 * @return error message if number of keywords > 1,
	 * 		"future" if number of keywords == 0,
	 * 		the respective display type otherwise.
	 */
	private String determineDisplayType(boolean isUndoneKeywordPresent,
			boolean isDoneKeywordPresent, boolean isTodayKeywordPresent,
			boolean isAllKeywordPresent, boolean isOverdueKeywordPresent,
			boolean isMemosKeywordPresent) {
		String displayType = "";
		
		int numberOfKeywordsPresent = getNumberOfKeywordsPresent(isAllKeywordPresent, isUndoneKeywordPresent,
				isDoneKeywordPresent, isTodayKeywordPresent, isOverdueKeywordPresent, isMemosKeywordPresent);
		
		assert numberOfKeywordsPresent >= 0;
		
		if (numberOfKeywordsPresent > 1 ) {
			displayType = "ERROR: Invalid number of keywords!!";
			return displayType;
		}
		
		if (numberOfKeywordsPresent < 1) {
			displayType = "ERROR: Invalid keyword(s) entered!";
			return displayType;
		}
		
		if (isAllKeywordPresent) {
			displayType = "all";
		} else if (isDoneKeywordPresent) {
			displayType = "done";
		} else if (isTodayKeywordPresent) {
			displayType = "today";
		} else if (isOverdueKeywordPresent) {
			displayType = "overdue";
		} else if (isMemosKeywordPresent) {
			displayType = "memos";
		} else { 
			//default
			displayType = "undone";
		}
		return displayType;
	}

	private int getNumberOfKeywordsPresent(boolean isAllKeywordPresent,
			boolean isUndoneKeywordPresent, boolean isDoneKeywordPresent,
			boolean isTodayKeywordPresent, boolean isOverdueKeywordPresent,
			boolean isMemosKeywordPresent) {
		return (isAllKeywordPresent ? 1:0) + 
				(isUndoneKeywordPresent ? 1:0) +
				(isDoneKeywordPresent ? 1:0) + 
				(isTodayKeywordPresent ? 1:0) +
				(isOverdueKeywordPresent ? 1:0) +
				(isMemosKeywordPresent ? 1:0);
	}

	public String parseClearType(String userCommand) {
		String clearType = "";
		
		userCommand = prepareUserCommandForParsing(userCommand);
		
		boolean isAllKeywordPresent = userCommand.equals("all");
		boolean isDoneKeywordPresent = userCommand.equals("done") || userCommand.equals("");
		
		clearType = determineClearType(isAllKeywordPresent, isDoneKeywordPresent); 
		
		return clearType;
	}
	
	private String determineClearType(boolean isAllKeywordPresent, boolean isDoneKeywordPresent) {
		String clearType = "";
		
		if (isAllKeywordPresent) {
			clearType = "all";
		} else if (isDoneKeywordPresent) {
			//default
			clearType = "done";
		} else {
			clearType = "ERROR: Invalid keyword(s) entered!";
		}
		return clearType;
	}
	
	private String prepareUserCommandForParsing(String userCommand) {
		return userCommand.trim().toLowerCase();
	}
	
	/**
	 * 
	 * @param userCommand
	 * @return boolean array
	 * fieldsToRemove[1] = true | indicates removal of location
	 * fieldsToRemove[2] = true | indicates removal of time
	 * fieldsToRemove[3] = true | indicates removal of date
	 * fieldsToRemove[0] = true | indicates parsing failed, i.e. user typed nonsense
	 */
	public boolean [] extractFieldsToRemove(String userCommand) {
		boolean [] fieldsToRemove = new boolean[4];
		int countInvalidKeywords = 0;
		
		String[] arrayOfKeywords = prepareArrayOfKeywords(userCommand);

		for (String keyword : arrayOfKeywords) {
			switch (keyword) {
			case "loc" : 
				fieldsToRemove[1] = true;
				break;
			case "time" :
				fieldsToRemove[2] = true;
				break;
			case "date" :
				fieldsToRemove[3] = true;
				break;
			default :
				countInvalidKeywords++;
				//not a removal keyword
				break;
			}
		}
		
		if (countInvalidKeywords > 0) {
			fieldsToRemove[0] = true;
		}
		
		return fieldsToRemove;
	}

	private String [] prepareArrayOfKeywords(String userCommand) {
		userCommand = userCommand.trim();
		String [] splitUserCommand = userCommand.split("/rm");
		if (splitUserCommand.length > 1) {
			String stringOfKeywords = userCommand.split("/rm")[1];
			if (stringOfKeywords != "") {
				stringOfKeywords = stringOfKeywords.toLowerCase().trim();
			}
			return stringOfKeywords.split("\\s+");
		} else {
			return new String[0];
		}
	}

	//@author Cai Di
	 
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
	
	// extract task details and assigned to taskDetails[][] 
	// Any invalid input detected will be reflected in invalidFeedBack String
	public void extractTaskDetailsForEdit(String input) {
		taskDetails = new String[6];
		invalidFeedBack = null;

		//default value is null
		for (int i = 0; i < taskDetails.length; i++) {
			taskDetails[i] = null;
		}

		if (isSuppliedInputEmpty(input)) {
			return;
		}
		
		input = extractName(input);
		
		
		if(input.indexOf("/") == -1){
			return;
		}
		
		String[] temp = input.split("/");
		
		if(temp.length == 0){
			invalidFeedBack = "ERROR: Command keyword is missing.";
			return;
		}
		
		boolean fromExist = false;
		boolean atExist = false;
		
		for (int i = 1; i < temp.length; i++) {
			// exit method if input is not valid
			if (invalidFeedBack != null)
				return;
			
			boolean isCommand = false;
			
			switch (getFirstWord(temp[i])) {
				case "from":
					isCommand = true;
					fromExist = true;
					extractDuration(temp[i]);
					break;
				case "by":
					//fall through
				case "at":
					isCommand = true;
					atExist = true;
					extractDeadline(temp[i]);
					break;
				case "on":
					isCommand = true;
					extractDate(temp[i]);
					break;
				case "mark":
					isCommand = true;
					extractStatus(temp[i]);
					break;
				case "loc":
					isCommand = true;
					extractLocation(temp[i]);
					break;
				case "rm":
					isCommand = true;
				}
			
			if(isCommand == false) {
				invalidFeedBack = "ERROR: Input Command is not valid.";
				return;
			}
		}
		if(fromExist == true && atExist == true){
			invalidFeedBack = "ERROR: Command /from and /at are mutually exclusive.";
			return;
		}
	}
	/*
	 * input with a "/" but no command keyword is not valid
	 * input with multiple "/" e.g. "///" is not allowed
	 * input with empty task name is not allowed
	 * input commands must be mutually exclusive to themselves
	 * input commands "/at" and "/from" cannot be existed at the same time
	 */
	public void extractTaskDetailsForAdd(String input) {
		taskDetails = new String[6];
		invalidFeedBack = null;

		//default value is null
		for (int i = 0; i < taskDetails.length; i++) {
			taskDetails[i] = null;
		}

		if (isSuppliedInputEmpty(input)) {
			return;
		}
		
		// extract task name
		input = extractName(input);
		
		// if there is no task name, input is not valid
		if(taskDetails[0] == null) {
			invalidFeedBack = "ERROR: No task name.";
			return;
		}
		
		// if there is no "/" after task name, return
		if(input.indexOf("/") == -1){
			return;
		}
		
		String[] temp = input.split("/");
		
		// if input string is "////" input command is not valid
		if(temp.length == 0){
			invalidFeedBack = "ERROR: Command keyword is missing.";
			return;
		}
		
		boolean fromExist = false;
		boolean atExist = false;
		int numOfFrom = 0,
			numOfAt   = 0,
			numOfOn   = 0,
			numOfMark = 0,
			numOfLoc  = 0;
		
		for (int i = 1; i < temp.length; i++) {
			// exit method if input is not valid
			if (invalidFeedBack != null)
				return;
			
			boolean isCommand = false;
			
			switch (getFirstWord(temp[i])) {
				case "from":
					isCommand = true;
					fromExist = true;
					numOfFrom++;
					extractDuration(temp[i]);
					if(numOfFrom > 1){
						invalidFeedBack = "ERROR: More than one command /from is detected.";
						return;
					}
					break;
				case "by":
					//fall through
				case "at":
					isCommand = true;
					atExist = true;
					numOfAt++;
					extractDeadline(temp[i]);
					if(numOfAt > 1){
						invalidFeedBack = "ERROR: More than one command /at is detected.";
						return;
					}
					break;
				case "on":
					isCommand = true;
					numOfOn++;
					extractDate(temp[i]);
					if(numOfOn > 1){
						invalidFeedBack = "ERROR: More than one command /on is detected.";
						return;
					}
					break;
				case "mark":
					isCommand = true;
					numOfMark++;
					extractStatus(temp[i]);
					if(numOfMark > 1){
						invalidFeedBack = "ERROR: More than one command /mark is detected.";
						return;
					}
					break;
				case "loc":
					isCommand = true;
					numOfLoc++;
					extractLocation(temp[i]);
					if(numOfLoc > 1){
						invalidFeedBack = "ERROR: More than one command /loc is detected.";
						return;
					}
					break;
				}
			
			// if any segment is not start with a command isCommand == false
			if(isCommand == false){
				invalidFeedBack = "ERROR: Input Command is not valid.";
				return;
			}
		}
		
		// if command /from and /at was keyed in at the same time 
		if(fromExist == true && atExist == true){
			invalidFeedBack = "ERROR: Command /from and /at are mutually exclusive.";
			return;
		}
	}

	// extract task name
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
	
	// extract task duration
	
	private void extractDuration(String inputFragment) {
		taskDetails[1] = getFirstWord(inputFragment.substring(4));
		//if (!ValidationCheck.isValidTime(taskDetails[1]))
		//	invalidFeedBack = "Input starting time is not valid.";

		try {
			inputFragment = inputFragment.split("to")[1].trim();
			taskDetails[2] = getFirstWord(inputFragment);
			//if (!ValidationCheck.isValidTime(taskDetails[2])) {
			if (!ValidationCheck.isValidDuration(taskDetails[1], taskDetails[2])) {
				invalidFeedBack = "ERROR: Input duration is not valid.";
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			invalidFeedBack = "ERROR: Input starting time without ending time.";
		}
	}
	
	// extract task deadline

	private void extractDeadline(String inputFragment) {
		taskDetails[1] = getFirstWord(inputFragment.substring(2));
		if (!ValidationCheck.isValidTime(taskDetails[1])) {
			invalidFeedBack = "ERROR: Input deadline time is not valid.";
		}
		
		if(inputFragment.indexOf("to") != -1){
			invalidFeedBack = "ERROR: Input ending time without starting time.";
		}
	}
	
	// extract task date

	private void extractDate(String inputFragment) {
		taskDetails[3] = getFirstWord(inputFragment.substring(2));
		if (!ValidationCheck.isValidDate(taskDetails[3])) {
			invalidFeedBack = "ERROR: Input date is not valid.";
		}
		
		if(inputFragment.indexOf("to") != -1){
			invalidFeedBack = "ERROR: Input ending time without starting time.";
		}
	}
	
	// extract task status 

	private void extractStatus(String inputFragment) {
		taskDetails[4] = getFirstWord(inputFragment.substring(4));
		if (!ValidationCheck.isValidStatus(taskDetails[4])) {
			invalidFeedBack = "ERROR: Input status is not valid.";
		}
	}
	
	// extract task location

	private void extractLocation(String inputFragment) {
		inputFragment = inputFragment.substring(3).trim();
		
		if (ValidationCheck.isValidLocation(inputFragment)) {
			taskDetails[5] = inputFragment;
		} else {
			invalidFeedBack = "ERROR: Location must start with a letter.";
		}
	}
	
	
	//@author Sai Hou
	private boolean isSuppliedInputEmpty(String input) {
		if (input.trim().equals("")) {
			invalidFeedBack = "ERROR: Empty input!";
			return true;
		}
		return false;
	}
}
