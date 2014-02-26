class Task {
	
	private String startTime;
	private String endTime;
	private String location;
	private String taskName;
	private String date;
	
	public Task(){
	}

	void setStartTime(String time){
		startTime = time;
	}
	
	void setEndTime(String time){
		endTime = time;
	}
	
	void setDate(String day){
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
	String getDate(){
	    return date;
	}
	String getStartTime(){
		return startTime;
	}
	String getEndTime(){
		return endTime;
	}
	String getLocation(){
		return location;
	}

}
