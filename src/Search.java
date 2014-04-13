//@author A0097812X
import java.util.ArrayList;

/*
* Description: This class provides the searching methods 
* and will return the results in the form of an ArrayList.
*/
public class Search {
	ArrayList<Task> searchList;
	
	public Search(ArrayList<Task> searchList) {
		this.searchList = searchList;
	}
	
	public ArrayList<Task> searchByName(String name) {
		name = name.trim();
		ArrayList<Task> matchedTasks = new ArrayList<Task>();

		for (Task t : searchList) {
			String taskNameInLowerCase = t.getName().toLowerCase();
			name = name.toLowerCase();

			if (taskNameInLowerCase.contains(name)) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
	}
	
	public ArrayList<Task> searchByStatus(boolean status) {
		ArrayList<Task> matchedTasks = new ArrayList<Task>();
		
		for (Task t : searchList) {
			if ( (t.getIsDone() == status) ) {
				matchedTasks.add(t);
			}
		}
		return matchedTasks;
	}
	
	//@author A0101252A
	protected ArrayList<Task> searchByDate(String date) {
		ArrayList<Task> matchedTasks = new ArrayList<Task>();

		for (Task t : searchList) {
			String taskDate = t.getDate();

			if (taskDate != null) {
				if (taskDate.equals(date)) {
					matchedTasks.add(t);
				}
			}
		}
		return matchedTasks;
	}
}
