import java.awt.BorderLayout;
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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

public class SapphireManagerGUI {
	private final static String MESSAGE_CURRENTLY_EDITING = "Currently Editing:";
	private final static String MESSAGE_HELP = "Enter F1 for a list of commands.";
	private final static String MESSAGE_NO_TASK_TO_DISPLAY_TODAY = "You have no tasks due today.\n";
	private final static String MESSAGE_WELCOME = "Welcome to Sapphire Manager!";
	private final static String MESSAGE_TASKS_FOUND = "Existing tasks found: ";
	private final static String MESSAGE_TODAY_TASK_TITLE = "Today's task:";
	
	private final static String PROMPT_FOR_NUMBER = "Enter a number: ";
	private final static String PROMPT_FOR_EDITS = "Enter your edits: ";
	private final static String TASKS_FOUND_MESSAGE = "Existing tasks found: ";
	private final static String TODAY_TASK_TITLE = "Today's task:\n";
	private final static String WELCOME_MESSAGE = "Welcome to Sapphire Manager!";
	private final static String WRONG_COMMAND_ENTERED = "Wrong command entered!\nEnter F1 for a list of commands.";
	private final static String SPLIT_LINE = "------------------------------------------";
	
	private final static String WRONG_COMMAND_ENTERED = "Wrong command entered! Enter F1 for a list of commands.";

	private static ArrayList<Task> todaysTasks, matchedTasks;
	private static CommandExecutor myExecutor;
	private static SapphireManagerGUI window;
	private static String todaysDate = "", currentTask = "";
	private static Task taskToBeEdited;
	
	private static JTextArea displayBox;
	private JFrame sapphireMgrFrame;
	private JLabel helpTip;
	private JScrollPane scrollPane;
	private JTextField inputBox;
	private Toolkit toolkit;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					todaysTasks = new ArrayList<Task>();
					window = new SapphireManagerGUI();
					todaysDate = getTodaysDate();

					myExecutor = new CommandExecutor();
					
					todaysTasks = myExecutor.getTodaysTasks(todaysDate);
					if(todaysTasks.isEmpty()) {
						displayBox.append(MESSAGE_NO_TASK_TO_DISPLAY_TODAY);
					} else {
						if(todaysTasks.size() == 1) {
							displayBox.append(MESSAGE_TODAY_TASK_TITLE);
							todaysTasks.get(0).printTaskDetails(1, window);
						} else {
							displayBox.append(MESSAGE_TODAY_TASK_TITLE);
							for(int i=0; i<todaysTasks.size(); i++) {
								todaysTasks.get(i).printTaskDetails((i+1), window);
							}
						}
					}

					window.sapphireMgrFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private SapphireManagerGUI() {	
		initialize();
		start();
	}

	public static SapphireManagerGUI getInstance() {
		if(window == null) {
			window = new SapphireManagerGUI();
		} 
		return window;
	}

	private void initialize() {		
		initializeSapphireManager();
		initializeDisplayBoxInScrollPane();
		initializeTextField();
		initializeHelpTip();
		contentPaneDisplay();		
	}

	private void start() {
		displayWelcomeMessage();
		sapphireManagerFrameListener();
		textFieldListener();
		displayBoxListener();

	}

	private void initializeSapphireMgr() {
		sapphireMgrFrame = new JFrame();
		sapphireMgrFrame.getContentPane().setBackground(Color.WHITE);
		sapphireMgrFrame.setTitle("Sapphire Manager");
		sapphireMgrFrame.setBounds(100, 100, 300, 450);
		sapphireMgrFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sapphireMgrFrame.setResizable(false);
		toolkit = Toolkit.getDefaultToolkit();
		int x = toolkit.getScreenSize().width-sapphireMgrFrame.getWidth();
		int y = toolkit.getScreenSize().height-sapphireMgrFrame.getHeight()-40;
		sapphireMgrFrame.setLocation(x, y);
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

	private void initializeTextField() {
		inputBox = new JTextField();
		
		inputBox.requestFocus();
		inputBox.setPreferredSize(new Dimension(380, 50));
		inputBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	}
	
	private void initializeHelpTip() {
		helpTip = new JLabel();
		helpTip.setBackground(Color.WHITE);
		helpTip.setLabelFor(inputBox);
		helpTip.setText("helpo");
	}

	private void contentPaneDisplay() {
		sapphireMgrFrame.getContentPane().setLayout(new BoxLayout(sapphireMgrFrame.getContentPane(), BoxLayout.Y_AXIS));
		sapphireMgrFrame.getContentPane().add(scrollPane, Component.CENTER_ALIGNMENT);
		sapphireMgrFrame.getContentPane().add(helpTip, Component.LEFT_ALIGNMENT);
		sapphireMgrFrame.getContentPane().add(inputBox, Component.CENTER_ALIGNMENT);
	}

	private void textFieldListener() {
		inputBox.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {
				updateScrollBar();
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!inputBox.getText().trim().equals("")) {
						String userCommand = readCommandFromUser();
						
						String operation = getFirstWord(userCommand);

						if(operation.equalsIgnoreCase("exit") || operation.equalsIgnoreCase("quit")) {
							printToDisplay("Sapphire Manager will now exit.");
							System.exit(0);
						} else if(operation.equalsIgnoreCase("add")) {
							myExecutor.executeAddCommand(userCommand.substring(4));
						} else if(operation.equalsIgnoreCase("display")) {
							myExecutor.executeDisplayCommand(userCommand.substring(7));
						} else if(operation.equalsIgnoreCase("undo")) {
							myExecutor.executeUndoCommand();
						} else if(operation.equalsIgnoreCase("clear")) {
							printToDisplay("All tasks cleared.");
							myExecutor.executeClearCommand();
						} else if(operation.equalsIgnoreCase("delete") && currentTask == "") {
							currentTask = "delete";
							matchedTasks = myExecutor.searchByName(userCommand.substring(7));
							displayExistingTasksFound(matchedTasks);
						} else if(currentTask.equalsIgnoreCase("delete")) {
							myExecutor.executeDeleteCommand2(matchedTasks);
							currentTask = "";
						} else if(operation.equalsIgnoreCase("edit") && currentTask == "") {
							currentTask = "edit1";
							matchedTasks = myExecutor.searchByName(userCommand.substring(5));
							displayExistingTasksFound(matchedTasks);
						} else if(currentTask.equalsIgnoreCase("edit1")) {
							currentTask = "edit2";
							taskToBeEdited = myExecutor.executeEditCommand2(matchedTasks);
							displayCurrentlyEditingSequence(taskToBeEdited);
						} else if(currentTask.equalsIgnoreCase("edit2")) {
							printToDisplay("Entered: '"+textField.getText()+"'.");
							String userModifications = readUserEdits();
							myExecutor.executeEditCommand3(taskToBeEdited, userModifications);
							currentTask = "";
						} else if(operation.equalsIgnoreCase("?")) {
							displayHelp();
						} else {
							printToDisplay(WRONG_COMMAND_ENTERED);
						}
						inputBox.setText("");
					}
				} else if(e.getKeyCode() == KeyEvent.VK_F1) {
					displayHelp();
				} else if(e.getKeyCode() == KeyEvent.VK_F5) {
					displayBox.setText("");
				} else if(e.getKeyCode() == KeyEvent.VK_F2) {
					sapphireMgrFrame.setState(Frame.ICONIFIED);
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

	private void sapphireManagerListener() {
		frmSapphireManager.addFocusListener(new FocusAdapter() {
	private void sapphireManagerFrameListener() {
		sapphireMgrFrame.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				inputBox.requestFocusInWindow();
			}
		});

		sapphireMgrFrame.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				inputBox.requestFocusInWindow();
			}
		});
		sapphireMgrFrame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_F3) {
					sapphireMgrFrame.setState(Frame.NORMAL);
				}
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
		printToDisplay(SPLIT_LINE);
		printToDisplay(HELP_MESSAGE);
		printToDisplay(SPLIT_LINE);
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
	}

	public void displaySingleTask(Task taskToDisplay) {	
		taskToDisplay.printTaskDetails(1, window);
	}

	public void displayTasksGivenList(ArrayList<Task> taskList) {
		if(taskList.size() <= 0) {
			printToDisplay("No tasks available.");
		} else {
			printToDisplay("Tasks available:");
			int count = 1;
			for(Task task: taskList) {
				//printToDisplay(count++ + ". ");
				task.printTaskDetails(count++, window);
			}
		}
	}

	public void displayExistingTasksFound(ArrayList<Task> taskList) {
		if (taskList != null) {
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

	private static String getTodaysDate(){
		SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyy");
		Calendar date = Calendar.getInstance();
		return dateFormatter.format(date.getTime());
	}

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
	private String getFirstWord(String str) {
		return str.trim().split("\\s+")[0];
	}
	
}
