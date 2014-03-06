class Task {
	
	private String startTime;
	private String endTime;
	private String deadline;
	private String location;
	private String taskName;
	private String date;
	private String type;
	
	public Task(String type, String taskName, String date, String startTime,
			String endTime, String deadline, String location){
		this.type     = type;
		this.taskName = taskName;
		this.date     = date;
		this.startTime = startTime;
		this.endTime  = endTime;
		this.deadline = deadline;
		this.location = location;
	}

	public Task(){
	}
	
	public void setTaskName(String name){
		taskName = name;
	}
	public void setDate(String day){
		date = day;
	}

	public void setStartTime(String time){
		startTime = time;
	}
	
	public void setEndTime(String time){
		endTime = time;
	}
	
	public void setDeadline(String time){
		deadline = time;
	}

	public void setLocation(String loc){
		location = loc;
	}

	public void setType(String typeOfTask){
		type = typeOfTask;
	}
	
	public String getName() {
		return taskName;
	}
	public String getDate(){
	    return date;
	}
	public String getStartTime(){
		return startTime;
	}
	public String getEndTime(){
		return endTime;
	}
	public String getDeadline(){
		return deadline;
	}
	public String getLocation(){
		return location;
	}
	public String getType(){
		return type;
	}
	
	public void print(Object line){
		System.out.println(line);
	}

	public void printTaskDetails(){
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
