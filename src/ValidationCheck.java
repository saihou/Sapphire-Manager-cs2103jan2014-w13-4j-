import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * ValidationCheck
 * This static class will check for errors from user input
 */

//@author Sai Hou
public class ValidationCheck {
	
	public static boolean isValidDate(String input) {
		boolean isValid = validateDate(input);
		return isValid;
	}
	
	public static boolean isValidDuration(String from, String to) {
		boolean isValid = validateDuration(from, to);
		return isValid;
	}
	
	public static boolean isValidTime(String input) {
		boolean isValid = validateTime(input);
		return isValid;
	}
	
	public static boolean isValidChoice(String input, int sizeOfList) {
		boolean isValid = validateChoice(input, sizeOfList);
		return isValid;
	}

	public static boolean isValidOperation(String operation) {
		boolean isValid = validateOperation(operation);
		return isValid;
	}
	
	public static boolean isValidStatus(String status) {
		boolean isValid = validateStatus(status);
		return isValid;
	}
	
	public static boolean isValidLocation(String loc) {
		boolean isValid = validateLocation(loc);
		return isValid;
	}

	//@author Cai Di
	private static boolean validateStatus(String status) {
		if (status.compareTo("done") == 0 || status.compareTo("undone") == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//@author Sai Hou
	private static boolean validateChoice(String input, int sizeOfList) {
		try {
			int choiceEntered = Integer.parseInt(input);
			
			if (choiceEntered > sizeOfList || choiceEntered < 1) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	private static boolean validateOperation(String operation) {
		operation = operation.toLowerCase();
		switch (operation) {
			case "add" :
			case "create" :
			case "new" :
			case "delete" :
			case "remove" :
			case "del" :
			case "display" :
			case "show" :
			case "edit" :
			case "update" :
			case "undo" :
			case "clear" :
			case "find" :
			case "search" :
			case "exit" :
			case "quit" :
				//all valid operations should fall through to return true
				return true;
			default :
				return false;
			}
	}
	
	private static boolean validateDate(String date) {
		if (date.length() != 6) {
			return false;
		}
		
		String DATE_FORMAT = "ddMMyy";
        try {
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFormat.setLenient(false);
            dateFormat.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
	}
	
	private static boolean validateTime(String input) {
		if (input.length() != 4) {
			return false;
		}
		
		try {	
			int time = Integer.parseInt(input);
			int mins = time % 100;
			int hours = time / 100;
			
			if (mins > 59 || hours > 23 || mins < 0 || hours < 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	private static boolean validateDuration(String from, String to) {
		try {
			int start = Integer.parseInt(from);
			int end = Integer.parseInt(to);
			
			return ( ((end - start) > 0) && validateTime(from) && validateTime(to) );
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private static boolean validateLocation(String loc) {
		boolean isValid = false;
		
		if (loc.trim().length() == 0) {
			return isValid;
		}
		
		char firstChar = loc.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'z') {
			isValid = true;
		} else {
			isValid = false;
		}
		return isValid;
	}
}
