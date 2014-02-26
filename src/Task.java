class Task {
	
	private String startTime;
	private String endTime;
	private String deadline;
	private String location;
	private String taskName;
	private String date;
	private String type;
	
	
	public Task(){
	}

	void setStartTime(String time){
		startTime = time;
	}
	
	void setEndTime(String time){
		endTime = time;
	}
	
	void setDeadline(String time){
		deadline = time;
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
	void setType(String typeOfTask){
		type = typeOfTask;
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
	String getDeadline(){
		return deadline;
	}
	String getLocation(){
		return location;
	}
	String getType(){
		return type;
	}

}
