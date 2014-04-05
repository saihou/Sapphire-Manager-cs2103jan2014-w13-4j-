import java.util.ArrayList;
import java.util.Collections;


public class SearchCommand extends DisplayCommand {
	
	private final static String MESSAGE_NO_SEARCH_RESULTS = "No records found!";
	
	public SearchCommand(ArrayList<Task> current) {
		super();
		currentTaskList = current;
	}
	
	/**
	 * @author Cai Di
	 */
	@Override
	public void execute(String taskName) {
		Search seeker = new Search(allTasks);
		boolean isSearchingByDate = false;

		taskName = taskName.trim();

		try {
			Integer.parseInt(taskName);
			if (taskName.length() == 6) {
				isSearchingByDate = true;
			}
			else {
				isSearchingByDate = false;
			}
		} catch(NumberFormatException e) {
			isSearchingByDate = false;
		}

		Collections.sort(allTasks);

		if(isSearchingByDate) {
			currentTaskList = seeker.searchByDate(taskName);
		}
		else {
			currentTaskList = seeker.searchByName(taskName);
		}

		if (currentTaskList.size() > 0) {
			systemFeedback = "Search results:\n" + formDisplayText();
		}
		else {
			systemFeedback = MESSAGE_NO_SEARCH_RESULTS;
		}
	}
}
