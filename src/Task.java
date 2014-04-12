class Task implements Comparable<Task> {
	private String type, name, date, startTime, endTime, location;
	private DateTimeConfiguration dateTimeConfig;
	boolean isDone;
	
	private final static String spacing = "      ";
	
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
	/**
	 * @author Teck Sheng (Dex)
	 */
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
	/**
	 * @author Teck Sheng (Dex)
	 */
	public boolean getIsDone() {
		return isDone;
	}
	
	/**
	 * @author Si Rui
	 * Returns a String that contains all task details in order for display Command :
	 * Task name which all tasks must have, followed by time and then optional details.
	 */
	private String getTaskDetails(boolean haveDate) {
		String taskDetails = this.name + '\n';
		assert taskDetails != null;
		
		if (haveDate && !this.type.equals("noSetTiming")) {
			taskDetails += spacing + dateTimeConfig.getDateForDisplay(this.date) 
					+ " <" + dateTimeConfig.getDayForDisplay(this.date) + ">" + '\n';
		}
		
		if (this.type.equals("setDuration")) {
			taskDetails += spacing + "From " + dateTimeConfig.getTimeForDisplay(this.startTime) +
							" to " + dateTimeConfig.getTimeForDisplay(this.endTime) + '\n';
		} else if (this.type.equals("targetedTime")){
			taskDetails += spacing + "At/By " + 
							dateTimeConfig.getTimeForDisplay(this.startTime) + '\n';
		}
		
		if (getLocation() != null) {
			taskDetails += spacing + this.location + '\n';
		}
		
		return taskDetails;
	}
	
	public String getTaskDetailsWithoutDate() {
		return getTaskDetails(false);
	}
	
	public String getAllTaskDetails(){
		return getTaskDetails(true);
	}

	/* 
	 * @author Si Rui
	 * This function allows Collections objects that store Task to sort it according to these rules:
	 *  
	 * 1. Tasks with timing sorted by date. Under each date:
	 * 		2.1 Full day tasks should appear at the top under each date
	 * 		2.2 Tasks with timings should follow chronologically 
	 * 2. Tasks with no set timing appear at the bottom of entire displayed list alphabetically
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Task t) {	//NOTE : HAVE NOT REFACTORED YET.
		
		String tType = t.getType();
		// Compare tasks with date/timing vs tasks with no set timing
		if (this.type.equals("noSetTiming") && tType.equals("noSetTiming")) {
			// If both are noSetTiming Tasks, list alphabetically by name
			String tName = t.getName();
			return this.name.compareTo(tName);
			// else if either is noSetTiming task, return that task is larger
		} else if (this.type.equals("noSetTiming")){
				return 1;
		} else if (tType.equals("noSetTiming")) {
			return -1;
		} else {
			// Compare by date and within each, compare by type again
			String thisDate = dateTimeConfig.reverseDate(this.date);
			String tDate = dateTimeConfig.reverseDate(t.getDate());
			
			if (thisDate.equals(tDate)){
				// They have the same date, so compare type
				if (this.type.compareTo("fullDay") == 0) {
					return -1;
				} else if (tType.compareTo("fullDay") == 0) {
					return 1;
				} else {
					// Compare Time
					String thisTime = this.startTime;
					String tTime = t.getStartTime();
					return thisTime.compareTo(tTime);
				}
			} else {
				return thisDate.compareTo(tDate);
			}
		}
	}
	
	private String capitalizeString(String name) {
		return (Character.toUpperCase(name.charAt(0)) + name.substring(1));
	}
	
	/*
	/*private void println(String line){
		System.out.println(line);
	}*/
	
	
	/**
	 * @author Teck Sheng (Dex)
	 */
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