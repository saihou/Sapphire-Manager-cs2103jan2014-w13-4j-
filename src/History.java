/**
 * 
 */

/**
 * @author Si Rui
 *	This class stores the history of the last user action, a pointer to the last task 
 *	that was added, edited or deleted, and a copy of the details of that last task. 
 *	This class will be referred to by the undo function in the Executor class.
 *
 *	Possible requests: set and get lastAction, pointerToLastTask and copyOfLastTask
 *
 */
public class History {
	private String lastAction;
	private Task pointerToLastTask;
	private Task copyOfLastTask;
	private boolean undoWasCalled;
	
	public History(){
		this.lastAction = null;
		this.undoWasCalled = false;
	}
	
	public void setLastAction(String lastAction){
		this.lastAction = lastAction;
	}
	
	public void setPointerToLastTask(Task pointerToLastTask){
		this.pointerToLastTask = pointerToLastTask;
	}
	
	public void setCopyOfLastTask(Task copyOfLastTask){
		this.copyOfLastTask = copyOfLastTask;
	}
	
	public void setUndoIsCalled(boolean undoWasCalled){
		this.undoWasCalled = undoWasCalled;
	}
	
	public String getLastAction(){
		return this.lastAction;
	}
	
	public Task getPointerToLastTask(){
		return this.pointerToLastTask;
	}
	
	public Task getCopyOfLastTask(){
		return this.copyOfLastTask;
	}
	
	public boolean getUndoWasCalled(){
		return this.undoWasCalled;
	}
}
