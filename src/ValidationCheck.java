import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * ValidationCheck
 * This static class will check for errors from user input
 * @author Sai Hou
 *
 */

public class ValidationCheck {
	
	public static boolean isValidDate(String input) {
		boolean isValid = validateDate(input);
		return isValid;
	}
	
	public static boolean isValidDuration(String from, String to) {
		boolean isValid = ((!from.equals(to)) && validateTime(from) && validateTime(to));
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
	
	/**
	 * @author Cai Di
	 */
	private static boolean validateStatus(String status) {
		if (status.compareTo("done") == 0 || status.compareTo("undone") == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @author Sai Hou
	 */
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
				//all valid operations should fallthrough to return true
				return true;
			default :
				return false;
			}
	}
	
	private static boolean validateDate(String date) 
	{
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
		
		try {
			if (input.length() != 4) {
				return false;
			}
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
	
}
