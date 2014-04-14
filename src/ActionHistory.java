//@author A0101252A
import java.util.ArrayDeque;
 
/*
 *	This class stores the history of the last user action in a stack.
 *	It will be referred to by the undo function in the CommandExecutor class.
 */

public class ActionHistory<E> {
	
	private ArrayDeque<E> historyOfCommands;
	
	public ActionHistory() {
		historyOfCommands = new ArrayDeque<E>();
	}
	
	public void push(E command) {
		historyOfCommands.push(command);
	}
	
	public E pop() {
		return historyOfCommands.pop();
	}
	
	public E peek() {
		return historyOfCommands.peek();
	}
	
	public boolean isEmpty() {
		return historyOfCommands.isEmpty();
	}
	
	//@author A0101252A-unused
	//Unused because we changed to use command pattern halfway through.
	/*
	private static ActionHistory instance = null;
	
	private String lastAction;
	private Task referenceToLastTask;
	private Task copyOfLastTask;
	
	private ActionHistory(){
		this.lastAction = null;
	}
	public static ActionHistory getInstance() {
		if (instance == null) {
			instance = new ActionHistory();
			return instance;
		}
		else {
			return instance;
		}
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
	
	public String getLastAction(){
		return this.lastAction;
	}
	
	public Task getReferenceToLastTask(){
		return this.referenceToLastTask;
	}
	
	public Task getCopyOfLastTask(){
		return this.copyOfLastTask;
	}
	*/
}
