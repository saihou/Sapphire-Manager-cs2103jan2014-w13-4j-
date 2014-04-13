//@author A0085159W
/*
 * Command pattern: This is one of the "Receiver" classes.
 * 
 * This class is created to analyze user input string.  
 * It extracts task details and information from user input string.
 */

public class CommandParser {
	private static final String ERROR_NO_TASK_NAME = "ERROR: No task name.";
	private static final String ERROR_COMMAND_FROM_AND_AT_ARE_MUTUALLY_EXCLUSIVE = "ERROR: Command /from and /at are mutually exclusive.";
	private static final String ERROR_INPUT_COMMAND_IS_NOT_VALID = "ERROR: Input Command is not valid.";
	private static final String ERROR_MORE_THAN_ONE_COMMAND_RM_IS_DETECTED = "ERROR: More than one command /rm is detected.";
	private static final String ERROR_MORE_THAN_ONE_COMMAND_LOC_IS_DETECTED = "ERROR: More than one command /loc is detected.";
	private static final String ERROR_MORE_THAN_ONE_COMMAND_MARK_IS_DETECTED = "ERROR: More than one command /mark is detected.";
	private static final String ERROR_MORE_THAN_ONE_COMMAND_ON_IS_DETECTED = "ERROR: More than one command /on is detected.";
	private static final String ERROR_MORE_THAN_ONE_COMMAND_AT_IS_DETECTED = "ERROR: More than one command /at is detected.";
	private static final String ERROR_MORE_THAN_ONE_COMMAND_FROM_IS_DETECTED = "ERROR: More than one command /from is detected.";
	private static final String ERROR_COMMAND_KEYWORD_IS_MISSING = "ERROR: Command keyword is missing.";
	private static final String ERROR_LOCATION_MUST_START_WITH_A_LETTER = "ERROR: Location must start with a letter.";
	private static final String ERROR_EMPTY_INPUT = "ERROR: Empty input!";
	private static final String ERROR_INVALID_NUMBER_OF_KEYWORDS = "ERROR: Invalid number of keywords!!";
	private static final String ERROR_INVALID_KEYWORDS_ENTERED = "ERROR: Invalid keyword(s) entered!";

	private static final int INDICATOR_INVALID = 0;
	private static final int INDICATOR_LOCATION = 1;
	private static final int INDICATOR_TIME = 2;
	private static final int INDICATOR_DATE = 3;
	private static final int INDICATOR_MAX = 4;
	
	private static final String OVERDUE = "overdue";
	private static final String MEMOS = "memos";
	private static final String ALL = "all";
	private static final String DONE = "done";
	private static final String UNDONE = "undone";
	private static final String TODAY = "today";
	
	//null constructor
	public CommandParser(){
	}
    
	//@author A0097812X
	/**
	 * @param userCommand
	 * @return displayType, "all", "overdue", "memos", "undone", "done" or "today" or error msg
	 */
	public String parseDisplayType(String userCommand) {
		String displayType = "";
		
		userCommand = prepareUserCommandForParsing(userCommand);
		
		boolean isAllKeywordPresent = userCommand.equals(ALL);
		boolean isUndoneKeywordPresent = userCommand.equals(UNDONE) || userCommand.equals("");
		boolean isDoneKeywordPresent = userCommand.equals(DONE);
		boolean isTodayKeywordPresent = userCommand.equals(TODAY);
		boolean isOverdueKeywordPresent = userCommand.equals(OVERDUE);
		boolean isMemosKeywordPresent = userCommand.equals(MEMOS);
		
		displayType = determineDisplayType(isUndoneKeywordPresent,
				isDoneKeywordPresent, isTodayKeywordPresent, isAllKeywordPresent,
				isOverdueKeywordPresent, isMemosKeywordPresent); 
		
		return displayType;
	}

	/**
	 * @return error message if number of keywords > 1,
	 * 		"undone" if number of keywords == 0,
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
			displayType = ERROR_INVALID_NUMBER_OF_KEYWORDS;
			return displayType;
		}
		
		if (numberOfKeywordsPresent < 1) {
			displayType = ERROR_INVALID_KEYWORDS_ENTERED;
			return displayType;
		}
		
		if (isAllKeywordPresent) {
			displayType = ALL;
		} else if (isDoneKeywordPresent) {
			displayType = DONE;
		} else if (isTodayKeywordPresent) {
			displayType = TODAY;
		} else if (isOverdueKeywordPresent) {
			displayType = OVERDUE;
		} else if (isMemosKeywordPresent) {
			displayType = MEMOS;
		} else { 
			//default
			displayType = UNDONE;
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
	/**
	 * @param userCommand
	 * @return displayType, "all" or "done" or error msg
	 */
	public String parseClearType(String userCommand) {
		String clearType = "";
		
		userCommand = prepareUserCommandForParsing(userCommand);
		
		boolean isAllKeywordPresent = userCommand.equals(ALL);
		boolean isDoneKeywordPresent = userCommand.equals(DONE) || userCommand.equals("");
		
		clearType = determineClearType(isAllKeywordPresent, isDoneKeywordPresent); 
		
		return clearType;
	}
	
	/**
	 * @return error message if keyword is invalid,
	 * 		the respective display type otherwise.
	 */
	private String determineClearType(boolean isAllKeywordPresent, boolean isDoneKeywordPresent) {
		String clearType = "";
		
		if (isAllKeywordPresent) {
			clearType = ALL;
		} else if (isDoneKeywordPresent) {
			clearType = DONE;
		} else {
			clearType = ERROR_INVALID_KEYWORDS_ENTERED;
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
		boolean [] fieldsToRemove = new boolean[INDICATOR_MAX];
		int countInvalidKeywords = 0;
		
		String[] arrayOfKeywords = prepareArrayOfKeywords(userCommand);

		for (String keyword : arrayOfKeywords) {
			switch (keyword) {
			case "loc" : 
				fieldsToRemove[INDICATOR_LOCATION] = true;
				break;
			case "time" :
				fieldsToRemove[INDICATOR_TIME] = true;
				break;
			case "date" :
				fieldsToRemove[INDICATOR_DATE] = true;
				break;
			default :
				countInvalidKeywords++;
				//not a removal keyword
				break;
			}
		}
		
		if (countInvalidKeywords > 0) {
			fieldsToRemove[INDICATOR_INVALID] = true;
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

	//@author  A0085159W
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
			invalidFeedBack = ERROR_COMMAND_KEYWORD_IS_MISSING;
			return;
		}
		
		boolean fromExist = false;
		boolean atExist = false;
		int numOfFrom = 0,
				numOfAt   = 0,
				numOfOn   = 0,
				numOfMark = 0,
				numOfLoc  = 0,
				numOfRm   = 0;
		
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
					// more than one command /from is detected
					if(numOfFrom > 1){
						invalidFeedBack = ERROR_MORE_THAN_ONE_COMMAND_FROM_IS_DETECTED;
						return;
					}
					extractDuration(temp[i]);
					break;
				case "by":
					//fall through
				case "at":
					isCommand = true;
					atExist = true;
					numOfAt++;
					// more than one command /at is detected
					if(numOfAt > 1){
						invalidFeedBack = ERROR_MORE_THAN_ONE_COMMAND_AT_IS_DETECTED;
						return;
					}
					extractDeadline(temp[i]);
					break;
				case "on":
					isCommand = true;
					numOfOn++;
					// more than one command /on is detected
					if(numOfOn > 1){
						invalidFeedBack = ERROR_MORE_THAN_ONE_COMMAND_ON_IS_DETECTED;
						return;
					}
					extractDate(temp[i]);
					break;
				case "mark":
					isCommand = true;
					numOfMark++;
					// more than one command /mark is detected
					if(numOfMark > 1){
						invalidFeedBack = ERROR_MORE_THAN_ONE_COMMAND_MARK_IS_DETECTED;
						return;
					}
					extractStatus(temp[i]);
					break;
				case "loc":
					isCommand = true;
					numOfLoc++;
					// more than one command /loc is detected
					if(numOfLoc > 1){
						invalidFeedBack = ERROR_MORE_THAN_ONE_COMMAND_LOC_IS_DETECTED;
						return;
					}
					extractLocation(temp[i]);
					numOfLoc++;
					break;
				case "rm":
					numOfRm++;
					// more than one command /rm is detected
					if(numOfRm > 1){
						invalidFeedBack = ERROR_MORE_THAN_ONE_COMMAND_RM_IS_DETECTED;
						return;
					}
					isCommand = true;
				}
			
			if(isCommand == false) {
				invalidFeedBack = ERROR_INPUT_COMMAND_IS_NOT_VALID;
				return;
			}
		}
		
		if(fromExist == true && atExist == true){
			invalidFeedBack = ERROR_COMMAND_FROM_AND_AT_ARE_MUTUALLY_EXCLUSIVE;
			return;
		}
		
	}
	/* 
	 * @author  A0085159W
	 */
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
			invalidFeedBack = ERROR_NO_TASK_NAME;
			return;
		}
		
		// if there is no "/" after task name, return
		if(input.indexOf("/") == -1){
			return;
		}
		
		String[] temp = input.split("/");
		
		// if input string is "////" input command is not valid
		if(temp.length == 0){
			invalidFeedBack = ERROR_COMMAND_KEYWORD_IS_MISSING;
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
						invalidFeedBack = ERROR_MORE_THAN_ONE_COMMAND_FROM_IS_DETECTED;
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
						invalidFeedBack = ERROR_MORE_THAN_ONE_COMMAND_AT_IS_DETECTED;
						return;
					}
					break;
				case "on":
					isCommand = true;
					numOfOn++;
					extractDate(temp[i]);
					if(numOfOn > 1){
						invalidFeedBack = ERROR_MORE_THAN_ONE_COMMAND_ON_IS_DETECTED;
						return;
					}
					break;
				case "mark":
					isCommand = true;
					numOfMark++;
					extractStatus(temp[i]);
					if(numOfMark > 1){
						invalidFeedBack = ERROR_MORE_THAN_ONE_COMMAND_MARK_IS_DETECTED;
						return;
					}
					break;
				case "loc":
					isCommand = true;
					numOfLoc++;
					extractLocation(temp[i]);
					if(numOfLoc > 1){
						invalidFeedBack = ERROR_MORE_THAN_ONE_COMMAND_LOC_IS_DETECTED;
						return;
					}
					break;
				}
			
			// if any segment is not start with a command isCommand == false
			if(isCommand == false){
				invalidFeedBack = ERROR_INPUT_COMMAND_IS_NOT_VALID;
				return;
			}
		}
		
		// if command /from and /at was keyed in at the same time 
		if(fromExist == true && atExist == true){
			invalidFeedBack = ERROR_COMMAND_FROM_AND_AT_ARE_MUTUALLY_EXCLUSIVE;
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
			invalidFeedBack = ERROR_LOCATION_MUST_START_WITH_A_LETTER;
		}
	}
	
	// returns the first word of String userInput 
	public String getFirstWord(String userInput) {
		return userInput.trim().split("\\s+")[0];
	}
	
	//@author A0097812X
	private boolean isSuppliedInputEmpty(String input) {
		if (input.trim().equals("")) {
			invalidFeedBack = ERROR_EMPTY_INPUT;
			return true;
		}
		return false;
	}
}
