import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

public class ValidationCheck {
	
	public static boolean isValidDate(String input) {
		boolean isValid = validateDate(input);
		return isValid;
	}
	
	public static boolean isValidTime(String input) {
		boolean isValid = validateTime(input);
		return isValid;
	}
	
	public static boolean isValidChoice(String input, int sizeOfList) {
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
	
	public static boolean isValidOperation(String operation) {
		operation = operation.toLowerCase();
		
		switch (operation) {
			case "add" :
				//fallthrough
			case "delete" :
				//fallthrough
			case "display" :
				//fallthrough
			case "edit" :
				//fallthrough
			case "update" :
				//fallthrough
			case "undo" :
				//fallthrough
			case "clear" :
				//fallthrough
			case "search" :
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
			
			if (mins > 59 || hours > 23) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
}
