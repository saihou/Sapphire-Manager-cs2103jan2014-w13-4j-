/**
 * @author Teck Sheng (Dex)
 * This GUI class handles the display of of system to user
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;


public class SapphireManagerGUI {
	private final static String MESSAGE_CURRENTLY_EDITING = "Currently Editing:";
	private final static String MESSAGE_HELP = "Enter F1 for a list of commands.";
	private final static String MESSAGE_NO_TASK_TO_DISPLAY_TODAY = "You have no tasks due today.\n";
	private final static String MESSAGE_WELCOME = "Welcome to Sapphire Manager!";
	private final static String MESSAGE_TASKS_FOUND = "Existing tasks found: ";
	private final static String MESSAGE_TODAY_TASK_TITLE = "Today's tasks:\n";
	
	private final static String PROMPT_FOR_NUMBER = "Enter a number: ";
	private final static String PROMPT_FOR_EDITS = "Enter your edits: ";

	private final static String SPLIT_LINE = "------------------------------------------";
	
	//private final static String WRONG_COMMAND_ENTERED = "Wrong command entered! Enter F1 for a list of commands.";
	
	private static ArrayList<Task> todaysTasks;
	private static CommandExecutor myExecutor;
	private static SapphireManagerGUI guiWindow;
	//private static String todaysDate = "";
	
	//private static JOptionPane exitPane;
	private static JTextArea displayBox;
	
	private JFrame guiFrame;
	private JLabel helpTip;
	private JScrollPane scrollPane;
	private JTextField inputBox;
	private Toolkit toolkit;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					todaysTasks = new ArrayList<Task>();
					guiWindow = new SapphireManagerGUI();

					myExecutor = new CommandExecutor();
					
					todaysTasks = myExecutor.getTodaysTasks();
					if(todaysTasks.isEmpty()) {
						displayBox.append(MESSAGE_NO_TASK_TO_DISPLAY_TODAY);
					} else {
						displayBox.append(myExecutor.executeDisplayCommand("/today"));
						/*if(todaysTasks.size() == 1) {
						displayBox.append(MESSAGE_TODAY_TASK_TITLE);
						displayBox.append(myExecutor.executeDisplayCommand("/today"));
						/*if(todaysTasks.size() == 1) {
							displayBox.append(MESSAGE_TODAY_TASK_TITLE);
						} else {
							displayBox.append(MESSAGE_TODAY_TASK_TITLE);
							for(int i=0; i<todaysTasks.size(); i++) {
								//todaysTasks.get(i).printTaskDetails((i+1), window);
							}
						}*/
					}

					guiWindow.guiFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SapphireManagerGUI() {	
		initialize();
		start();
	}

	private void initialize() {		
		initializeSapphireManager();
		initializeDisplayBoxInScrollPane();
		initializeInputBox();
		initializeHelpTip();
		contentPaneDisplay();	
	}

	private void start() {
		displayWelcomeMessage();
		guiFrameListener();
		textFieldListener();
		displayBoxListener();
	}

	private void initializeSapphireManager() {
		guiFrame = new JFrame();
		guiFrame.getContentPane().setBackground(Color.WHITE);
		guiFrame.setTitle("Sapphire Manager");
		guiFrame.setBounds(100, 100, 300, 450);
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setResizable(false);
		toolkit = Toolkit.getDefaultToolkit();
		int x = toolkit.getScreenSize().width-guiFrame.getWidth();
		int y = toolkit.getScreenSize().height-guiFrame.getHeight()-40;
		guiFrame.setLocation(x, y);
	}

	private void initializeDisplayBoxInScrollPane() {
		displayBox = new JTextArea(5, 30);
		scrollPane = new JScrollPane(displayBox);

		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(380, 380));
		displayBox.setBackground(Color.WHITE);
		displayBox.setBorder(null);
		displayBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		displayBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		displayBox.setEditable(false);
		displayBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayBox.setLineWrap(true);
		displayBox.setMargin(new Insets(5, 5, 5, 5));
		displayBox.setTabSize(2);
		displayBox.setWrapStyleWord(true);

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}

	private void initializeInputBox() {
		inputBox = new JTextField();
		
		inputBox.requestFocus();
		inputBox.setPreferredSize(new Dimension(380, 50));
		inputBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	}
	
	private void initializeHelpTip() {
		helpTip = new JLabel();
		
		helpTip.setBackground(Color.WHITE);
		helpTip.setLabelFor(inputBox);
		helpTip.setText("");
	}

	private void contentPaneDisplay() {
		guiFrame.getContentPane().setLayout(new BoxLayout(guiFrame.getContentPane(), BoxLayout.Y_AXIS));
		guiFrame.getContentPane().add(scrollPane, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(helpTip, Component.LEFT_ALIGNMENT);
		guiFrame.getContentPane().add(inputBox, Component.CENTER_ALIGNMENT);
	}

	private void textFieldListener() {
		inputBox.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {
				updateScrollBar();
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!inputBox.getText().trim().equals("")) {
						String userCommand = readCommandFromUser();
						
						String systemFeedback = "";
						String phase = "";
						ArrayList<Task> matchedTasks = null;
						
						systemFeedback = myExecutor.doUserOperation(userCommand);
						
						phase = myExecutor.getPhase();
						switch (phase) {
							case "editPhase1" :
								matchedTasks = myExecutor.getCurrentTaskList();
								displayExistingTasksFound(matchedTasks);
								break;
							case "editPhase2" :
								Task myTask = myExecutor.getCurrentTask();
								displayCurrentlyEditingSequence(myTask);
								break;
							case "deletePhase1" :
								matchedTasks = myExecutor.getCurrentTaskList();
								displayExistingTasksFound(matchedTasks);
								break;
							default:
								if (!systemFeedback.startsWith("Error")) {
									printToDisplay(systemFeedback);
								}
								break;
						}
						inputBox.setText("");
					}
				} else if(e.getKeyCode() == KeyEvent.VK_F1) {
					printToDisplay("Help");
					displayHelp();
				} else if(e.getKeyCode() == KeyEvent.VK_F5) {
					displayBox.setText("");
				} else if(e.getKeyCode() == KeyEvent.VK_F2) {
					guiFrame.setState(Frame.ICONIFIED);
				} else if(e.getKeyCode() == KeyEvent.VK_F3) {
					guiFrame.setState(Frame.NORMAL);
				} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					printToDisplay("Sapphire Manager will now exit.");
					System.exit(0);
					/*
					int n = JOptionPane.showConfirmDialog(
						    new Frame(),
						    "Confirm exit?",
						    "Exit confirmation",
						    JOptionPane.YES_NO_OPTION);
					System.out.println("n: "+n);
					if(n == 0) {						
						System.exit(1);
					}*/
				} else if(e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue()-25);
				} else if(e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue()+25);
				} else if(e.getKeyCode() == KeyEvent.VK_HOME) {
					scrollPane.getVerticalScrollBar().setValue(0);
				} else if(e.getKeyCode() == KeyEvent.VK_END) {
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				}
			}

			@Override
			public void keyPressed(KeyEvent a) {
				if(a.getKeyCode() == KeyEvent.VK_PAGE_UP) {
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue()-20);
				} else if(a.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue()+20);
				}
			}
		});
	}

	private void guiFrameListener() {
		guiFrame.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				inputBox.requestFocusInWindow();
			}
		});

		guiFrame.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				inputBox.requestFocusInWindow();
			}
		});
	}

	private void displayBoxListener() {
		displayBox.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateScrollBar();
			}
		});

		displayBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				inputBox.requestFocusInWindow();
				updateScrollBar();
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				inputBox.requestFocusInWindow();
				updateScrollBar();
			}
		});
	}

	private void updateScrollBar() {
		displayBox.setCaretPosition(displayBox.getDocument().getLength());
		DefaultCaret caret = (DefaultCaret)displayBox.getCaret();  
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);  
	}

	public void printToDisplay(String message) {
		displayBox.append(message+"\n");
	}

	public void displayWelcomeMessage() {
		printToDisplay(MESSAGE_WELCOME);
		printToDisplay("Today's date: "+getTodayDate());
		printToDisplay("------------------------------------------");
		printToDisplay(MESSAGE_HELP);
		printToDisplay("------------------------------------------");
	}

	public void displayHelp() {
		printToDisplay(SPLIT_LINE);
		printToDisplay("Available Commands");
		printToDisplay(SPLIT_LINE);
		printToDisplay("1. add [task name] [options]");
		printToDisplay("2. delete [task name (in part or in full)]");
		printToDisplay("3. edit [task name (in part or in full)]");
		printToDisplay("4. display [all | today | past | future]");
		printToDisplay("5. undo");
		printToDisplay("6. search [task name | category | date]");
		printToDisplay("7. clear");
		printToDisplay("8. exit | quit");
		printToDisplay(SPLIT_LINE);
		printToDisplay("- Options:");
		printToDisplay("\tDate: '/on [date]'");
		printToDisplay("\tTime: '/from [time] to [time]' or \n\t\t   '/at [time]'");
		printToDisplay("\tLocation: '/loc [location name]'");
		printToDisplay("\tCategory: '/c [one-word-name]'");
		printToDisplay("\tReminder: '/r [time]'");
		printToDisplay("\t*Time: 4-digit 24 hours [1159]: 11.59am; [2359]: 11.59pm");
		printToDisplay("\t*Date: 6-digit [DDMMYYYY]");
	}
	
	public void displaySingleTask(Task taskToDisplay) {	
		String taskDetails = taskToDisplay.getAllTaskDetails();
		printToDisplay(taskDetails);
	}

	public void displayTasksGivenList(ArrayList<Task> taskList) {
		if(taskList.size() <= 0) {
			printToDisplay("No tasks available.");
		} else {
			printToDisplay("Tasks available:");
			int count = 1;
			for(Task task: taskList) {
				printToDisplay(count++ + ". ");
				String taskDetails = task.getAllTaskDetails();
				printToDisplay(taskDetails);
			}
		}
	}

	public void displayExistingTasksFound(ArrayList<Task> taskList) {
		if (taskList != null && taskList.size() != 0) {
			printToDisplay(MESSAGE_TASKS_FOUND);
			displayTasksGivenList(taskList);
			printToDisplay(PROMPT_FOR_NUMBER);
		}
		else {
			//printToDisplay(MESSAGE_NO_SEARCH_RESULTS);
		}
	}

	public void displayCurrentlyEditingSequence(Task taskBeingEdited) {
		printToDisplay(MESSAGE_CURRENTLY_EDITING);
		displaySingleTask(taskBeingEdited);
		printToDisplay(PROMPT_FOR_EDITS);
	}

	private String getTodayDate() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar date = Calendar.getInstance();
		return dateFormatter.format(date.getTime());
	}
	/*
	private static String getTodaysDate(){
		SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyy");
		Calendar date = Calendar.getInstance();
		return dateFormatter.format(date.getTime());
	}
	*/
	private String readUserInput() {
		String userInput = null;
		if(!inputBox.equals(null)) {
			userInput = inputBox.getText();
		}
		return userInput;
	}

	public String readCommandFromUser() {
		//printToDisplay(PROMPT_FOR_COMMAND);
		return readUserInput();
	}

	public String readUserEdits() {
		return readUserInput();
	}

	public int readUserChoice() {
		//textField.setText("");
		String userInput = readUserInput();
		int userChoice = Integer.parseInt(userInput);
		return userChoice;
	}
	
}
