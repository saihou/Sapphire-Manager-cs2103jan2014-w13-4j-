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
	
	public void println(String line){
		System.out.println(line);
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
	 * 1. Top level of order: tasks with date/timing before tasks with no set timing
	 * 		1.1 Tasks with no set timing appear at the bottom of entire displayed list 
	 * 2. Next level of order: by date
	 * 		Under each date:
	 * 		2.1 Full day tasks should appear at the top under each date
	 * 		2.2 Tasks with timings should follow chronologically 
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Task t) {	//NOTE : HAVE NOT REFACTORED YET.
		String tType = t.getType();
		
		// Compare tasks with date/timing vs tasks with no set timing first
		if (this.type.compareTo(tType) != 0) {
			if (this.type.compareTo("noSetTiming") == 0){
				return 1;
			} else if (tType.compareTo("noSetTiming") == 0) {
				return -1;
			} 
		} else {
			// Compare by date and within each, compare by type again
			String thisDate = reverseDate(this.date);
			String tDate = reverseDate(t.getDate());
			
			if (thisDate.compareTo(tDate) != 0) {
				return thisDate.compareTo(tDate);
			} else {
				// They have the same date, so compare type
				if (this.type.compareTo("fullDay") == 0) {
					return -1;
				} else if (tType.compareTo("fullDay") == 0) {
					return 1;
				} else {
					// Compare Time
					int thisTime = Integer.parseInt(this.startTime);
					int tTime = Integer.parseInt(t.getStartTime());
					return thisTime - tTime;
				}
			}
		}
		
		return 0;
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
			reversedDate.concat(toAppend);
		}
		return reversedDate;
	}

}