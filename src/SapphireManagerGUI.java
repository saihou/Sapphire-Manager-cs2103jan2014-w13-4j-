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
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;


public class SapphireManagerGUI {
	private final static String MESSAGE_CURRENTLY_EDITING = "Currently Editing:";
	private final static String MESSAGE_HELP = "Enter F1 for a list of commands.";
	private final static String MESSAGE_NO_TASK_TO_DISPLAY_TODAY = "You have no tasks due today.\n";
	private final static String MESSAGE_WELCOME = "Welcome to Sapphire Manager!";
	private final static String MESSAGE_TASKS_FOUND = "Existing tasks found: ";
	private final static String MESSAGE_TODAY_TASK_TITLE = "Today's task:\n";
	
	private final static String PROMPT_FOR_NUMBER = "Enter a number: ";
	private final static String PROMPT_FOR_EDITS = "Enter your edits: ";

	private final static String SPLIT_LINE = "------------------------------------------";
	
	//private final static String WRONG_COMMAND_ENTERED = "Wrong command entered! Enter F1 for a list of commands.";
	
	private static ArrayList<Task> todaysTasks;
	private static CommandExecutor myExecutor;
	private static SapphireManagerGUI guiWindow;
	//private static String todaysDate = "";
	
	//private static JOptionPane exitPane;
	//private static JTextArea displayBox2;
	private static JTextPane displayBox;
	
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
						displaySystemText(MESSAGE_NO_TASK_TO_DISPLAY_TODAY);
					} else {
						displaySystemText(MESSAGE_TODAY_TASK_TITLE);
						displayNormalText(myExecutor.executeDisplayCommand("/today"));
						//appendToPane(myExecutor.executeDisplayCommand("/today"), Color.RED);
						/*if(todaysTasks.size() == 1) {
						displayBox.append(MESSAGE_TODAY_TASK_TITLE);
						displayBox.append(myExecutor.executeDisplayCommand("/today"));
						/*if(todaysTasks.size() == 1) {
						displayBox.append(MESSAGE_TODAY_TASK_TITLE);
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
		guiFrame.setBounds(100, 100, 600, 600);
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setResizable(false);
		toolkit = Toolkit.getDefaultToolkit();
		int x = toolkit.getScreenSize().width-guiFrame.getWidth();
		int y = toolkit.getScreenSize().height-guiFrame.getHeight()-40;
		guiFrame.setLocation(x, y);
	}
	/*
	private void initializeDisplayBoxInScrollPane() {
		displayBox2 = new JTextArea(5, 30);
		scrollPane = new JScrollPane(displayBox2);

		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(380, 380));
		displayBox2.setBackground(Color.WHITE);
		displayBox2.setBorder(null);
		displayBox2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		displayBox2.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		displayBox2.setEditable(false);
		displayBox2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayBox2.setLineWrap(true);
		displayBox2.setMargin(new Insets(5, 5, 5, 5));
		displayBox2.setTabSize(2);
		displayBox2.setWrapStyleWord(true);

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}
	*/
	private void initializeDisplayBoxInScrollPane() {
		displayBox = new JTextPane();
		scrollPane = new JScrollPane(displayBox);
		
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		displayBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(380, 650));
		displayBox.setBackground(Color.BLACK);
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}

	private void initializeInputBox() {
		inputBox = new JTextField();
		
		inputBox.requestFocus();
		inputBox.setPreferredSize(new Dimension(380, 35));
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
						System.out.println(systemFeedback);
						
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
									displaySystemText(systemFeedback);
									//printToDisplay(systemFeedback);
								}
								break;
						}
						inputBox.setText("");
					}
				} else if(e.getKeyCode() == KeyEvent.VK_F1) {
					//printToDisplay("Help");
					displayHelp();
				} else if(e.getKeyCode() == KeyEvent.VK_F5) {
					appendToPane(" ", Color.BLACK, true);
				} else if(e.getKeyCode() == KeyEvent.VK_F2) {
					guiFrame.setState(Frame.ICONIFIED);
				} else if(e.getKeyCode() == KeyEvent.VK_F3) {
					guiFrame.setState(Frame.NORMAL);
				} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
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
	
	public static void displayNormalText(String message) {
		appendToPane(message, Color.WHITE, false);
	}
	
	public static void displaySystemText(String message) {
		appendToPane(message, new Color(0xff6c00), false);
	}
	
	public void displayHelpoText(String message) {
		//
		//appendToPane(message, Color.CYAN);
	}
	
	public static void displayHightlightText(String message) {
		appendToPane(message, Color.CYAN, false);
	}
	
	public void printToDisplay(String message) {
		appendToPane(message, Color.WHITE, false);
	}
	
	private static void appendToPane(String message, Color color, boolean toAppend) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,  StyleConstants.Foreground, color);
		
		aset = sc.addAttribute(aset,  StyleConstants.FontFamily,  "Segoe UI");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
		
		int length = displayBox.getDocument().getLength();
		displayBox.setCaretPosition(length);
		displayBox.setCharacterAttributes(aset, toAppend);
		displayBox.replaceSelection(message+"\n");
	}

	public void displayWelcomeMessage() {
		displaySystemText(MESSAGE_WELCOME);
		displaySystemText("Today's date: "+getTodayDate());
		displaySystemText("------------------------------------------");
		displaySystemText(MESSAGE_HELP);
		displaySystemText("------------------------------------------");
	}

	public void displayHelp() {
		displaySystemText(SPLIT_LINE);
		displaySystemText("Available Commands");
		displaySystemText(SPLIT_LINE);
		displaySystemText("1. add [task name] [options]");
		displaySystemText("2. delete [task name (in part or in full)]");
		displaySystemText("3. edit [task name (in part or in full)]");
		displaySystemText("4. display [all | today | past | future]");
		displaySystemText("5. undo");
		displaySystemText("6. search [task name | category | date]");
		displaySystemText("7. clear");
		displaySystemText("8. exit | quit");
		displaySystemText(SPLIT_LINE);
		displaySystemText("- Options:");
		displaySystemText("\tDate: '/on [date]'");
		displaySystemText("\tTime: '/from [time] to [time]' or \n\t\t   '/at [time]'");
		displaySystemText("\tLocation: '/loc [location name]'");
		displaySystemText("\tCategory: '/c [one-word-name]'");
		displaySystemText("\tReminder: '/r [time]'");
		displaySystemText("\t*Time: 4-digit 24 hours [1159]: 11.59am; [2359]: 11.59pm");
		displaySystemText("\t*Date: 6-digit [DDMMYYYY]");
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
