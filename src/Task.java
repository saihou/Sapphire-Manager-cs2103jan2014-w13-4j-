//@author Cai Di

/*
 * Command pattern: This is one of the "Receiver" classes.
 */
class Task implements Comparable<Task> {
	private static final String TASK_TYPE_FULL_DAY = "fullDay";
	private static final String TASK_TYPE_NO_SET_TIMING = "noSetTiming";
	
	private String type, name, date, startTime, endTime, location;
	private DateTimeConfiguration dateTimeConfig;
	boolean isDone;
	
	private final static String SPACING = "      ";
	
	public Task() {
		dateTimeConfig = new DateTimeConfiguration();
	}
	
	public Task(Task newTask) {
		setType(newTask.getType());
		setName(newTask.getName());
		setDate(newTask.getDate());
		setStartTime(newTask.getStartTime());
		setEndTime(newTask.getEndTime());
		setLocation(newTask.getLocation());	
		setIsDone(newTask.getIsDone());
		dateTimeConfig = new DateTimeConfiguration();
	}
	
	public Task(String type, String name, String date, String startTime, String endTime, String location, boolean isDone){
		setType(type);
		setName(name);
		setDate(date);
		setStartTime(startTime);
		setEndTime(endTime);
		setLocation(location);
		setIsDone(isDone);
		dateTimeConfig = new DateTimeConfiguration();
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public void setName(String name){
		name = capitalizeString(name);
		this.name = name;
	}
	
	public void setDate(String date){
		this.date = date;
	}

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}
	
	public void setEndTime(String endTime){
		this.endTime = endTime;
	}

	public void setLocation(String location){
		if (location != null){
			location = capitalizeString(location);
		}
		this.location = location;
	}
	
	//@author A0097706U
	public void setIsDone(boolean isDone) {
		this.isDone = isDone;
	}
	
	public String getType(){
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDate(){
	    return date;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public String getLocation() {
		return location;
	}
	
	public boolean getIsDone() {
		return isDone;
	}
	
	//@author A0101252A
	/*
	 * Returns a String that contains all task details in order for display Command :
	 * Task name which all tasks must have, followed by time and then optional details.
	 */
		private String getTaskDetails(boolean haveDate) {
		String taskDetails = this.name + '\n';
		
		assert taskDetails != null;
		
		if (haveDate && !this.type.equals(TASK_TYPE_NO_SET_TIMING)) {
			taskDetails += SPACING + dateTimeConfig.getDateForDisplay(this.date) + '\n';
		}
		if (this.type.equals("setDuration")) {
			taskDetails += SPACING + "From " + dateTimeConfig.getTimeForDisplay(this.startTime) +
							" to " + dateTimeConfig.getTimeForDisplay(this.endTime) + '\n';
		} else if (this.type.equals("targetedTime")){
			taskDetails += SPACING + "At/By " + 
							dateTimeConfig.getTimeForDisplay(this.startTime) + '\n';
		}
		if (getLocation() != null) {
			taskDetails += SPACING + this.location + '\n';
		}
		return taskDetails;
	}
	
	public String getTaskDetailsWithoutDate() {
		return getTaskDetails(false);
	}
	
	public String getAllTaskDetails(){
		return getTaskDetails(true);
	}

	/**
	 * This function allows Collections objects that store Task to sort it according to these rules:
	 *  
	 * 1. Tasks with timing sorted by date. Under each date:
	 * 		1.1 Full day tasks should appear at the top under each date
	 * 		1.2 Tasks with timings should follow chronologically 
	 * 2. Tasks with no set timing appear at the bottom of entire displayed list alphabetically
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Task t) {
		// Compare tasks with date/timing vs tasks with no set timing
		String taskType = t.getType();
		
		if (this.type.equals(TASK_TYPE_NO_SET_TIMING) && taskType.equals(TASK_TYPE_NO_SET_TIMING)) {
			// If both are noSetTiming Tasks, list alphabetically by name
			return compareAlphabetically(t);
		} else if (this.type.equals(TASK_TYPE_NO_SET_TIMING)){
			// else if either is noSetTiming task, return that task is larger
			return 1;
		} else if (taskType.equals(TASK_TYPE_NO_SET_TIMING)) {
			return -1;
		} else {
			// both not noSetTiming tasks i.e. both have dates, then compare by date
			return compareByDate(t, taskType);
		}
	}

	private int compareByDate(Task t, String taskType) {
		// Compare by date and within each, compare by type again
		String thisDate = dateTimeConfig.reverseDate(this.date);
		String tDate = dateTimeConfig.reverseDate(t.getDate());
		
		if (thisDate.equals(tDate)) {
			// They have the same date, so compare type
			return compareType(t, taskType);
		} else {
			return thisDate.compareTo(tDate);
		}
	}

	private int compareType(Task t, String taskType) {
		if (this.type.compareTo(TASK_TYPE_FULL_DAY) == 0) {
			return -1;
		} else if (taskType.compareTo(TASK_TYPE_FULL_DAY) == 0) {
			return 1;
		} else {
			//both not fullDay tasks, so compare time
			return compareTime(t);
		}
	}

	private int compareTime(Task t) {
		String thisTime = this.startTime;
		String tTime = t.getStartTime();
		return thisTime.compareTo(tTime);
	}

	private int compareAlphabetically(Task t) {
		String tName = t.getName();
		return this.name.compareTo(tName);
	}
	
	private String capitalizeString(String name) {
		return (Character.toUpperCase(name.charAt(0)) + name.substring(1));
	}
	
	//@author A0097706U
	public boolean equals(Task newTask) {
		if(!getType().equals(newTask.getType())) {
			return false;
		}
		if(!getName().equals(newTask.getName())) {
			return false;
		}
		if(!getDate().equals(newTask.getDate())) {
			return false;
		}
		if(!getStartTime().equals(newTask.getStartTime())) {
			return false;
		}
		if(!getEndTime().equals(newTask.getEndTime())) {
			return false;
		}
		if(!getLocation().equals(newTask.getLocation())) {
			return false;
		}
		if(getIsDone() != newTask.getIsDone()) {
			return false;
		}
		return true;
		
	}
}