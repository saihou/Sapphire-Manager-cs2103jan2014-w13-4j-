//@author A0097812X
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/*
* Command pattern: This is one of the "Receiver" classes.
* 
* Description: This class serves as a user-defined data structure that
* will store the result of every Command. It will be returned to GUI,
* where it will use the Get methods to retrieve the results.
*/

public class Result {
	//stores system feedback messages 
	//eg. "Successfully added XX", "invalid task number!", etc.
	private String systemFeedback;
	private int highlightIndexI;
	private int highlightIndexJ;
	private boolean success;
	
	private Queue<String> headings;
	private Queue<Queue<String>> body;
	private Queue<String> currentHeading;
	
	public Result() {
		headings = new ArrayDeque<String>();
		body = new ArrayDeque<Queue<String>>();
		currentHeading = new ArrayDeque<String>();
		highlightIndexI = -1;
		highlightIndexJ = -1;
		systemFeedback = "default";
		success = false;
	}
	
	public Result(int indexI, int indexJ, String feedback, ArrayDeque<String> headings, 
			ArrayDeque<Queue<String>> body, boolean success) {
		this.headings = headings;
		this.body = body;
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
	
	//@author A0101252A
	/*
	 * Compares two Result objects.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @return True if both objects contain the same information, false otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Result)) {
			return false;
		} else {
			Result r = (Result)object;
			boolean isEqual = true;
			
			System.out.println(highlightIndexI + " " +  r.getHighlightIndexI());
			if (highlightIndexI != r.getHighlightIndexI()) {
				isEqual = false;
			}
			
			System.out.println(highlightIndexJ + " " +  r.getHighlightIndexJ());
			if (highlightIndexJ != r.getHighlightIndexJ()) {
				isEqual = false;
			}
			
			System.out.println(systemFeedback);
			System.out.println(r.getSystemFeedback());
			if (!systemFeedback.equals(r.getSystemFeedback())) {
				isEqual = false;
			}
			
			System.out.println("Checking results headings");
			if (!Arrays.equals(headings.toArray(), r.getHeadings().toArray())) {
				isEqual = false;
			}
			
			System.out.println("Checking results body");
			if (body.size() != r.getBody().size()) {
				isEqual = false;
			}
			
			System.out.println(this.body);
			System.out.println(r.getBody());
			
			for (int i = 0 ; i < body.size() ; i++) {
				Queue<String> thisBodySegment = body.poll();
				Queue<String> otherBodySegment = r.getBody().poll();
				
				if (!Arrays.equals(thisBodySegment.toArray(), otherBodySegment.toArray())) {
					isEqual = false;
				}
			}
			
			System.out.println("Results are equal: " + isEqual);
			return isEqual;
		}
	}
	
	//@author A0097812X
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
	
	public void setHighlightIndices(int i, int j) {
		highlightIndexI = i;
		highlightIndexJ = j;
	}
}
