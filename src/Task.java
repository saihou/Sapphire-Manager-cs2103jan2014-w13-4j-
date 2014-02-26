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
	
	void setTaskName(String name){
		taskName = name;
	}
	void setDate(String day){
		date = day;
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

	void setLocation(String loc){
		location = loc;
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
	
	void print(Object line){
		System.out.println(line);
	}

	void printTaskDetails(){
		//Print task name which all tasks will have, followed by date/time if any,
		//followed by other optional variables
		
		print(taskName);
		
		if(type.compareTo("fullDay")==0){
			print("Date: " + date);
		}else if(type.compareTo("setDuration")==0){
			print("Date: " + date);
			print("Time: " + startTime + " to " + endTime);
		}else if(type.compareTo("targetedTime")==0){
			print("Date: " + date);
			print("Time: " + deadline);
		}
		
		if(location!=null){
			print("Location: " + location);
		}
	}

}
