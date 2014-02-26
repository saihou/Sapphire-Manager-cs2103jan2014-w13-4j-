class Task {
	
	private int    startTime;
	private int    endTime;
	private String location;
	private String taskName;
	private int date;
	
	public Task(){
	}

	void setStartTime(int time){
		startTime = time;
	}
	
	void setEndTime(int time){
		endTime = time;
	}
	
	void setDate(int day){
		date = day;
	}
	void setLocation(String loc){
		location = loc;
	}
	void setTaskName(String name){
		taskName = name;
	}
	String getName() {
		return taskName;
	}
	int getDate(){
	    return date;
	}
	int getStartTime(){
		return startTime;
	}
	int getEndTime(){
		return endTime;
	}
	String getLocation(){
		return location;
	}

}
