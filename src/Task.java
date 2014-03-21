class Task {
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
	
	public Task(String type, String name, String date, String startTime, String endTime, String deadline, String location){
		setType(type);
		setName(name);
		setDate(date);
		setStartTime(startTime);
		setEndTime(endTime);
		setLocation(location);
		setIsDone(false);
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
}