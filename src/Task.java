class Task implements Comparable<Task> {
	private String type, name, date, startTime, endTime, location;
	boolean isDone;
	
	public Task() {
	}
	
	public Task(Task newTask) {
		setType(newTask.getType());
		setName(newTask.getName());
		setDate(newTask.getDate());
		setStartTime(newTask.getStartTime());
		setEndTime(newTask.getEndTime());
		setLocation(newTask.getLocation());	
		setIsDone(newTask.getIsDone());
	}
	
	public Task(String type, String name, String date, String startTime, String endTime, String location, boolean isDone){
		setType(type);
		setName(name);
		setDate(date);
		setStartTime(startTime);
		setEndTime(endTime);
		setLocation(location);
		setIsDone(isDone);
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public void setName(String name){
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
	public String getTaskDetails() {
		String taskDetails = this.name + '\n';
		assert taskDetails != null;
		
		if(this.type.equals("setDuration")){
			taskDetails += "\tTime: " + this.startTime + " to " + this.endTime + '\n';
		} else if(this.type.equals("targetedTime")){
			taskDetails += "\tTime: " + this.startTime + '\n';
		}
		
		if(getLocation() != null){
			taskDetails += "\tLocation: " + this.location + '\n';
		}
		
		return taskDetails;
	}
/*
	public void printTaskDetails(int num, SapphireManagerGUI gui){
		//Print task name which all tasks will have, followed by date/time if any,
		//followed by other optional variables
		gui.printToDisplay("\t"+num+". "+getName());
		
		if(getType().equals("fullDay")){
			gui.printToDisplay("\tDate: " + getDate());
		} else if(getType().equals("setDuration")){
			gui.printToDisplay("\tDate: " + getDate());
			gui.printToDisplay("\tTime: " + getStartTime() + " to " + getEndTime());
		} else if(getType().equals("targetedTime")){
			gui.printToDisplay("\tDate: " + getDate());
			gui.printToDisplay("\tTime: " + getDeadline());
		} else {
			gui.printToDisplay("\tTo be completed during free-time.");
		}
		
		if(getLocation() != null){
			gui.printToDisplay("\tLocation: " + getLocation());
		}
		
		gui.printToDisplay("");
	}
*/
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
			String thisDate = reverseDate(this.date);
			String tDate = reverseDate(t.getDate());
			
			// If both tasks have timing, compare date
			/*if(thisDate != null && tDate != null){
				thisDate = reverseDate(thisDate);
				tDate = reverseDate(tDate);
				if (thisDate.compareTo(tDate) != 0) {
					return thisDate.compareTo(tDate);
				}
			}*/
			
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
	
	/* 
	 * @author Si Rui
	 * Reverse specified date String from DDMMYY to YYMMDD format to directly compare values  
	 * using pre-defined compareTo function in Java String class.
	 */
	private String reverseDate(String originalDate) {
		String reversedDate = "";
		for (int i = 6; i > 0; i -= 2){
			String toAppend = originalDate.substring(i-2, i);
			reversedDate += toAppend;
		}
		return reversedDate;
	}
	/*
	private void println(String line){
		System.out.println(line);
	}
	*/
	/**
	 * @author Teck Sheng (Dex)
	 */
	public boolean comparedTo(Task newTask) {
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