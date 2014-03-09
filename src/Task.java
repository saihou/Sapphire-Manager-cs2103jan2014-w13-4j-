class Task {
	
	private String type, name, date, startTime, endTime, deadline, location;
	
	public Task() {
	}
	
	public Task(Task newTask){
		setType(newTask.getType());
		setName(newTask.getName());
		setDate(newTask.getDate());
		setStartTime(newTask.getStartTime());
		setEndTime(newTask.getEndTime());
		setDeadline(newTask.getDeadline());
		setLocation(newTask.getLocation());		
	}
	
	public Task(String type, String name, String date, String startTime, String endTime, String deadline, String location){
		setType(type);
		setName(name);
		setDate(date);
		setStartTime(startTime);
		setEndTime(endTime);
		setDeadline(deadline);
		setLocation(location);
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
	
	public void setDeadline(String deadline){
		this.deadline = deadline;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public void setType(String type){
		this.type = type;
	}
	
	public String getName() {
		return name;
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
	
	public void println(String line){
		System.out.println(line);
	}

	public void printTaskDetails(){
		//Print task name which all tasks will have, followed by date/time if any,
		//followed by other optional variables
		
		println(getName());
		
		if(getType().equals("fullDay")){
			println("Date: " + getDate());
		} else if(getType().equals("setDuration")){
			println("Date: " + getDate());
			println("Time: " + getStartTime() + " to " + getEndTime());
		} else if(getType().equals("targetedTime")){
			println("Date: " + getDate());
			println("Time: " + getDeadline());
		} else {
			println("Date: " + getDate());
			println("Does not belong to any category!");
		}
		
		if(getLocation() != null){
			println("Location: " + getLocation());
		}
	}

}
