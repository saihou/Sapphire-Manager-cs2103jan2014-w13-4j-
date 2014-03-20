/**
 * @author Si Rui
 *	This class stores the history of the last user action, a pointer to the last task 
 *	that was added, edited or deleted, and a copy of the details of that last task. 
 *	This class will be referred to by the undo function in the Executor class.
 *
 *	Possible requests: set and get lastAction, pointerToLastTask and copyOfLastTask
 *
 */
public class ActionHistory {
	private String lastAction;
	private Task referenceToLastTask;
	private Task copyOfLastTask;
	//private boolean undoWasCalled;
	
	public ActionHistory(){
		this.lastAction = null;
		//this.undoWasCalled = false;
	}
	
	public void setLastAction(String lastAction){
		this.lastAction = lastAction;
	}
	
	public void setReferenceToLastTask(Task referenceToLastTask){
		this.referenceToLastTask = referenceToLastTask;
	}
	
	public void setCopyOfLastTask(Task copyOfLastTask){
		this.copyOfLastTask = copyOfLastTask;
	}
	
	/*public void setUndoWasCalled(boolean undoWasCalled){
		this.undoWasCalled = undoWasCalled;
	}*/
	
	public String getLastAction(){
		return this.lastAction;
	}
	
	public Task getReferenceToLastTask(){
		return this.referenceToLastTask;
	}
	
	public Task getCopyOfLastTask(){
		return this.copyOfLastTask;
	}
	
	/*public boolean getUndoWasCalled(){
		return this.undoWasCalled;
	}*/
}
