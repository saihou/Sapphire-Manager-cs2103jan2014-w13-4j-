import java.util.ArrayDeque;
import java.util.Queue;


public class Result {
	int highlightIndexI;
	int highlightIndexJ;

	//stores system feedback messages 
	//eg. "successfully added XXX", "invalid task number!", etc.
	String systemFeedback;
	
	//each header has a body, each body has tasks
	Queue<String> headings;
	Queue<Queue<String>> body;
	Queue<String> currentHeading;
	
	boolean success;
	
	public Result() {
		headings = new ArrayDeque<String>();
		body = new ArrayDeque<Queue<String>>();
		currentHeading = new ArrayDeque<String>();
		highlightIndexI = -1;
		highlightIndexJ = -1;
		systemFeedback = "default";
		success = false;
	}
	
	public Result(int indexI, int indexJ, String feedback, boolean success) {
		headings = new ArrayDeque<String>();
		body = new ArrayDeque<Queue<String>>();
		currentHeading = new ArrayDeque<String>();
		highlightIndexI = indexI;
		highlightIndexJ = indexJ;
		systemFeedback = feedback;
		this.success = success;
	}
	
	public Result(String sysFeedback) {
		this();
		systemFeedback = sysFeedback;
	}
	
	public boolean equals(Result r) {
		boolean isEqual = true;
		if (highlightIndexI != r.getHighlightIndexI()) {
			isEqual = false;
		}
		if (highlightIndexJ != r.getHighlightIndexJ()) {
			isEqual = false;
		}
		if (success != r.isSuccessful()) {
			isEqual = false;
		}
		if (systemFeedback.equals(r.getSystemFeedback())) {
			isEqual = false;
		}
		return isEqual;
	}
	
	public void pushNewHeadingText(String heading) {
		headings.offer(heading);
	}
	
	public void pushTaskToCurrentHeading(String details) {
		currentHeading.offer(details);
	}
	
	public void savePreviousHeading() {
		if (currentHeading.size() > 0) {
			body.offer(currentHeading);
			currentHeading = new ArrayDeque<String>();
		}
	}
	
	public void saveHighlightIndex() {
		highlightIndexI = headings.size()-1;
		highlightIndexJ = currentHeading.size()-1;
	}
	
	public void setSystemFeedback(String s) {
		systemFeedback = s;
	}
	
	public void setSuccess(boolean s) {
		success = s;
	}
	
	public Queue<String> getHeadings() {
		return headings;
	}
	
	public Queue<Queue<String>> getBody() {
		return body;
	}
	
	public boolean isSuccessful() {
		return success;
	}
	
	public int getHighlightIndexI() {
		return highlightIndexI;
	}
	
	public int getHighlightIndexJ() {
		return highlightIndexJ;
	}
	
	public String getSystemFeedback() {
		return systemFeedback;
	}
	
	public void printHighlightIndex() {
		System.out.println("highlightIndexI"+highlightIndexI);
		System.out.println("highlightIndexJ"+highlightIndexJ);
	}
	
	public void printResult() {
		int numOfHeadings = headings.size();
		for (int i = 0; i < numOfHeadings; i++) {
			String heading = headings.poll();
			System.out.print(heading);
			headings.offer(heading);
			
			Queue<String> bodyOfThisHeading = body.poll();
			
			int numOfTasks = bodyOfThisHeading.size();
			for (int j = 0; j < numOfTasks ; j++) {
				
				if (i == highlightIndexI && j == highlightIndexJ) {
					System.out.println("-----HIGHLIGHT ME!!!!!!-----");
				}
				String task = bodyOfThisHeading.poll();
				System.out.print(task);
				bodyOfThisHeading.offer(task);
			}
		}
	}
	
	public String getResult() {
		StringBuilder sb = new StringBuilder();
		
		int numOfHeadings = headings.size();
		for (int i = 0; i < numOfHeadings; i++) {
			String heading = headings.poll();
			sb.append(heading);
			headings.offer(heading);
			
			Queue<String> bodyOfThisHeading = body.poll();
			
			int numOfTasks = bodyOfThisHeading.size();
			for (int j = 0; j < numOfTasks ; j++) {
				
				if (i == highlightIndexI && j == highlightIndexJ) {
					sb.append("-HIGHLIGHT ME!-");
				}
				String task = bodyOfThisHeading.poll();
				sb.append(task);
				bodyOfThisHeading.offer(task);
			}
		}
		return sb.toString();
	}
}
