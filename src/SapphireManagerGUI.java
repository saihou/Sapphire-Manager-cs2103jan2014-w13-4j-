/**
 * @author Teck Sheng (Dex)
 * This GUI class handles the input and output to and from user and system
 */

//JAVA-AWT libraries
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.Toolkit;

//JAVA-UTIL LIBRARIES
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//JAVAX-SWING LIBRARIES
import javax.swing.BoxLayout;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.FlowLayout;

//SapphireManagerGUI CLASS
public class SapphireManagerGUI {
	//SYSTEM MESSAGES
	private final static String MESSAGE_HELP = "Enter F1 for a list of commands.";
	private final static String MESSAGE_NL = "\n";
	private final static String MESSAGE_SPLIT_LINE = "-------------------------------------------------------------------------------------------------------------------"+MESSAGE_NL;
	private final static String MESSAGE_INVALID_COMMAND = "No such command! Press F1 for help.";

	//HELPO MESSAGES	
	//HELPO MESSAGES - OPTIONS
	private final static String HELPO_OPTIONS = "/[options]";
	private final static String HELPO_OPTIONS_AT = "/at";
	private final static String HELPO_OPTIONS_BY = "/by";
	private final static String HELPO_OPTIONS_AT_BY = "/(at|by)";
	private final static String HELPO_OPTIONS_CLEAR_ALL = "all";
	private final static String HELPO_OPTIONS_FROM = "/from";
	private final static String HELPO_OPTIONS_LOC = "/loc";
	private final static String HELPO_OPTIONS_MARK = "/mark";
	private final static String HELPO_OPTIONS_ON = "/on";
	private final static String HELPO_OPTIONS_REMOVE = "/rm";

	//HELPO MESSAGES - ACTIONS 
	private final static String HELPO_ACTIONS_ADD = "add";
	private final static String HELPO_ACTIONS_CLEAR = "clear";
	private final static String HELPO_ACTIONS_CREATE = "create";
	private final static String HELPO_ACTIONS_DEL = "del";
	private final static String HELPO_ACTIONS_DELETE = "delete";
	private final static String HELPO_ACTIONS_DISPLAY = "display";
	private final static String HELPO_ACTIONS_EDIT = "edit";
	private final static String HELPO_ACTIONS_EXIT = "exit";
	private final static String HELPO_ACTIONS_FIND = "find";
	private final static String HELPO_ACTIONS_NEW = "new";
	private final static String HELPO_ACTIONS_QUIT = "quit";
	private final static String HELPO_ACTIONS_REMOVE = "remove";
	private final static String HELPO_ACTIONS_SEARCH = "search";
	private final static String HELPO_ACTIONS_SHOW = "show";
	private final static String HELPO_ACTIONS_UNDO = "undo";
	private final static String HELPO_ACTIONS_UPDATE = "update";

	//HELPO MESSAGES - FORMATS
	private final static String HELPO_FORMAT_TASK_NO = "[task number(#)]";
	private final static String HELPO_FORMAT_AT_BY = "[time]";
	private final static String HELPO_FORMAT_ON = "[date]";
	private final static String HELPO_FORMAT_DATE = "<-- date in DDMMYY format";
	private final static String HELPO_FORMAT_DISPLAY = "[overdue|today|done|undone|memos|all]";
	private final static String HELPO_FORMAT_EDIT = "[name(if any)] /[options]";
	private final static String HELPO_FORMAT_FROM_TO = "[time] to [time]";
	private final static String HELPO_FORMAT_LOC = "[location name]";
	private final static String HELPO_FORMAT_MARK = "[done|undone]";
	private final static String HELPO_FORMAT_REMOVE = "[date|time|loc]";
	private final static String HELPO_FORMAT_SEARCH = "[task name|date]";
	private final static String HELPO_FORMAT_TASK_NAME = "[task name]";
	private final static String HELPO_FORMAT_TIME = "<-- time in HHMM (24-hours) format";

	//NECESSARY DECLARATIONS
	private static boolean systemFeedbackStatus;
	private static CommandExecutor myExecutor;
	private static SapphireManagerGUI guiWindow;
	private static ActionListener timeListener;
	private static Timer timer = new Timer(5000, timeListener);;

	//GUI COMPONENT DECLARATIONS
	private JFrame guiFrame;
	private JLabel logoLabel;
	private JPanel datePanel;
	private JPanel helpoPanel;
	private JPanel inputBoxPanel;
	private JPanel logoPanel;
	private static JScrollPane scrollPane;
	private Toolkit toolkit;
	private static JLabel dateLabel;
	private static JLabel helpo;
	private static JTextField inputBox;
	private static JTextPane displayBox;

	//MAIN METHOD
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					guiWindow = new SapphireManagerGUI(); //instantiate GUI window
					myExecutor = new CommandExecutor(); //instantiate CommandExecutor

					Result result = myExecutor.doUserOperation("display today");
					printResults(result);

					guiWindow.guiFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//initializes and opens everything needed for the entire program 
	public SapphireManagerGUI() {	
		initialize();
		start();
	}

	//initialize and open the program in the following order
	private void initialize() {		
		initializeSapphireManager();
		initializeLogoInPanel();
		initializeDateLabelInPanel();
		initializeDisplayBoxInScrollPane();
		initializeHelpoInPanel();
		initializeInputBoxInPanel();
		initializeTimer();
		contentPaneDisplay();	
	}

	//displays messages and start listeners
	private void start() {
		displayWelcomeMessage();
		guiFrameListener();
		inputBoxListener();
		displayBoxListener();
		helpoListener();		
		timerListener();
	}

	//build the main frame for GUI 
	private void initializeSapphireManager() {
		guiFrame = new JFrame();
		guiFrame.setBounds(100, 100, 450, 700);
		guiFrame.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 14));
		guiFrame.getContentPane().setBackground(new Color(0x231F20));
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setResizable(false);
		guiFrame.setTitle("Sapphire Manager");	
		//sets the initial display position of the program
		toolkit = Toolkit.getDefaultToolkit();
		int x = toolkit.getScreenSize().width-guiFrame.getWidth();
		int y = toolkit.getScreenSize().height-guiFrame.getHeight()-40;
		guiFrame.setLocation(x, y);
	}

	//initializes date label within a panel
	private void initializeDateLabelInPanel() {
		dateLabel = new JLabel();
		datePanel = new JPanel();
		datePanel.add(dateLabel);
		
		datePanel.setBackground(new Color(0x231F20));
		datePanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		datePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));				
		datePanel.setMaximumSize(new Dimension(450, 40));
		datePanel.setPreferredSize(new Dimension(350, 30));
		
		dateLabel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		dateLabel.setForeground(Color.WHITE);
		dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dateLabel.setHorizontalTextPosition(SwingConstants.CENTER);		
		dateLabel.setPreferredSize(new Dimension(350, 30));
		dateLabel.setVerticalAlignment(SwingConstants.TOP);
	}

	//initializes display box within a scroll pane
	private void initializeDisplayBoxInScrollPane() {
		displayBox = new JTextPane();
		scrollPane = new JScrollPane(displayBox);
		
		displayBox.setBackground(new Color(0x231F20));
		displayBox.setBorder(null);
		displayBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		displayBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		displayBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayBox.setMaximumSize(new Dimension(450, 350));
		
		scrollPane.setBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMaximumSize(new Dimension(425, 500));
		scrollPane.setPreferredSize(new Dimension(425, 470));
	}

	//initializes input text box within a panel
	private void initializeInputBoxInPanel() {	
		inputBox = new JTextField();
		inputBoxPanel = new JPanel();
		inputBoxPanel.add(inputBox);

		inputBoxPanel.setBackground(Color.WHITE);
		inputBoxPanel.setBorder(null);
		inputBoxPanel.setMaximumSize(new Dimension(450, 35));
		inputBoxPanel.setPreferredSize(new Dimension(400, 30));

		inputBox.requestFocus();
		inputBox.setBackground(SystemColor.window);
		inputBox.setBorder(null);
		inputBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		inputBox.setHorizontalAlignment(SwingConstants.LEFT);
		inputBox.setPreferredSize(new Dimension(425, 20));
	}

	//initializes helpo label within a panel
	private void initializeHelpoInPanel() {
		systemFeedbackStatus = false;
		helpo = new JLabel();
		helpoPanel = new JPanel();
		helpoPanel.add(helpo);

		helpoPanel.setBackground(Color.WHITE);
		helpoPanel.setMaximumSize(new Dimension(450, 35));
		helpoPanel.setPreferredSize(new Dimension(450, 35));
		
		helpo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		helpo.setHorizontalAlignment(SwingConstants.LEFT);
		helpo.setPreferredSize(new Dimension(425, 30));
		
		displayToHelpo(MESSAGE_HELP);
	}

	//initializes logo image within a panel
	private void initializeLogoInPanel() {
		logoLabel = new JLabel("");
		logoPanel = new JPanel();
		logoPanel.add(logoLabel);

		logoPanel.setBackground(new Color(0x231F20));
		logoPanel.setMaximumSize(new Dimension(450, 50));
		logoPanel.setPreferredSize(new Dimension(380, 40));
		ImageIcon icon = new ImageIcon(getClass().getResource("img/logo-trans.png"));
		logoLabel.setIcon(icon);
	}
	
	//initializes the timer
	private static void initializeTimer() {
		systemFeedbackStatus = true;
		timer = new Timer(4000, timeListener);
		timer.start();
	}

	//sets up the frame in Box Layout with components in the following order
	//***ordering is important!
	private void contentPaneDisplay() {
		guiFrame.getContentPane().setLayout(new BoxLayout(guiFrame.getContentPane(), BoxLayout.Y_AXIS));		
		guiFrame.getContentPane().add(logoPanel, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(datePanel, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(scrollPane, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(helpoPanel, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(inputBoxPanel, Component.CENTER_ALIGNMENT);
	}

	//Listener for input box
	private void inputBoxListener() {
		inputBox.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {
				//updateScrollBar();
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!inputBox.getText().trim().equals("")) {
						String userCommand = readCommandFromUser();
						Result result = myExecutor.doUserOperation(userCommand);
						printResults(result);

						if(helpo.getText().trim().equals("")) {
							displayToHelpo(MESSAGE_HELP);
						}
						inputBox.setText("");
					}
				} else if(e.getKeyCode() == KeyEvent.VK_F1) {
					clearDisplayBox();
					displayHelp();
					displayToHelpo("Displaying Help List");
				} else if(e.getKeyCode() == KeyEvent.VK_F5) {
					clearDisplayBox();
				} else if(e.getKeyCode() == KeyEvent.VK_F2) {
					guiFrame.setState(Frame.ICONIFIED);
				} else if(e.getKeyCode() == KeyEvent.VK_F3) {
					guiFrame.setState(Frame.NORMAL);
				} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				} else if(e.getKeyCode() == KeyEvent.VK_PAGE_UP || e.getKeyCode() == KeyEvent.VK_UP) {
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue()-25);
				} else if(e.getKeyCode() == KeyEvent.VK_PAGE_DOWN || e.getKeyCode() == KeyEvent.VK_DOWN) {
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue()+25);
				} else if(e.getKeyCode() == KeyEvent.VK_HOME) {
					scrollPane.getVerticalScrollBar().setValue(0);
				} else if(e.getKeyCode() == KeyEvent.VK_END) {
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				} else {
				}
				System.out.println("LOLOL");
			}

			@Override
			public void keyPressed(KeyEvent a) {
				if(a.getKeyCode() == KeyEvent.VK_PAGE_UP || a.getKeyCode() == KeyEvent.VK_UP) {
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue()-20);
				} else if(a.getKeyCode() == KeyEvent.VK_PAGE_DOWN || a.getKeyCode() == KeyEvent.VK_DOWN) {
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue()+20);
				}
			}
		});
	}

	//Listener for GUI Frame
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

	//Listener for display box
	private void displayBoxListener() {
		displayBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				inputBox.requestFocus();
			}
		});
	}

	private void timerListener() {
		timeListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				systemFeedbackStatus = false;
				if(!timer.isRunning()) {
					displayToHelpo(MESSAGE_HELP);
					displayWelcomeMessage();
				}
			}
		};
	}

	//Listener for Helpo
	private void helpoListener() {
		inputBox.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent arg0) {
				String userInput = inputBox.getText().toLowerCase().trim();
				if(userInput.equalsIgnoreCase("")) {
					if(inputBox.getText().trim().equals("") && !systemFeedbackStatus) {
						displayToHelpo(MESSAGE_HELP);
					} else {
						systemFeedbackStatus = false;
						displayToHelpo(helpo.getText());
					}
				} else if(userInput.startsWith("a")) { //add
					displayToHelpo(helpoAdd(userInput));
				} else if(userInput.startsWith("c")) { //create or clear
					displayToHelpo(helpoC(userInput));
				} else if(userInput.startsWith("d")) { //del or delete or display
					displayToHelpo(helpoD(userInput));
				} else if(userInput.startsWith("e")) { //edit or exit
					displayToHelpo(helpoE(userInput));
				} else if(userInput.startsWith("f")) { //find
					displayToHelpo(helpoSearch(userInput));
				} else if(userInput.startsWith("n")) { //new
					displayToHelpo(helpoAdd(userInput));
				} else if(userInput.startsWith("q")) { //quit
					displayToHelpo(helpoExit(userInput));
				} else if(userInput.startsWith("r")) { //remove
					displayToHelpo(helpoDelete(userInput));
				} else if(userInput.startsWith("s")) { //search or show
					displayToHelpo(helpoS(userInput));
				} else if(userInput.startsWith("u")) { //update or undo
					displayToHelpo(helpoU(userInput));
				} else {
					displayToHelpo("Wrong command entered!");
				}
			}
		});
	}
	/*
	//Pushes scroll bar to display the latest
	private void updateScrollBar() {
		displayBox.setCaretPosition(displayBox.getDocument().getLength());
		DefaultCaret caret = (DefaultCaret)displayBox.getCaret();  
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);  
	}
	 */
	//read user's input
	private String readUserInput() {
		String userInput = null;
		if(!inputBox.equals(null)) {
			userInput = inputBox.getText();
		}
		return userInput;
	}

	//read user's command
	private String readCommandFromUser() {
		return readUserInput();
	}

	//displays system message
	private static void displaySystemMessage(String message) {
		appendToDisplayBox(message, new Color(0xff6c00), "Trebuchet MS", 14, false);
	}

	//displays system message
	private static void displaySystemMessage2(String message) {
		appendToDisplayBox(message, new Color(0xff6c55), "Trebuchet MS", 14, false);
	}
	
	private static void displayOverdueTasks(String message) {
		appendToDisplayBox(message, Color.RED, "Trebuchet MS", 14, false);
	}
	
	private static void displayTodayTasks(String message) {
		appendToDisplayBox(message, Color.YELLOW, "Trebuchet MS", 14, false);
	}
	
	private static void displayThisWeekTasks(String message) {
		appendToDisplayBox(message, new Color(0xCCFFF), "Trebuchet MS", 14, false);
	}
	
	private static void displayNextWeekTasks(String message) {
		appendToDisplayBox(message, Color.MAGENTA, "Trebuchet MS", 14, false);
	}
	
	private static void displayMemos(String message) {
		appendToDisplayBox(message, Color.PINK, "Trebuchet MS", 14, false);
	}
	
	private static void displayDoneTasks(String message) {
		appendToDisplayBox(message, Color.GRAY, "Trebuchet MS", 14, false);
	}

	//displays highlighted message
	private static void displayHighlightMessage(String message) {
		appendToDisplayBox(message, new Color(0x00FF00), "Trebuchet MS", 14, true);
	}

	//displays to helpo label
	private static void displayToHelpo(String message) {
		helpo.setText(message);
	}

	private static void displayToDateLabel() {
		dateLabel.setText("TODAY'S DATE: "+getTodayDate());
	}

	//displays welcome message
	private void displayWelcomeMessage() {
		displayToDateLabel();
	}

	//displays the latest messages
	private static void appendToDisplayBox(String message, Color color, String fontName, int fontSize, boolean isBold) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,  StyleConstants.Foreground, color);

		aset = sc.addAttribute(aset, StyleConstants.FontSize, fontSize);
		aset = sc.addAttribute(aset, StyleConstants.FontFamily,  fontName);
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
		aset = sc.addAttribute(aset, StyleConstants.Bold, isBold);

		//int length = displayBox.getDocument().getLength();
		//System.out.println("Length: "+length);
		//displayBox.setCaretPosition(length);
		displayBox.setCharacterAttributes(aset, false);
		displayBox.replaceSelection(message);
	}

	//clears the display box
	private static void clearDisplayBox() {
		displayBox.setText("");
	}

	//displays available commands and keyboard shortcuts
	private void displayHelp() {
		displaySystemMessage("Commands:"+MESSAGE_NL);
		displaySystemMessage(MESSAGE_SPLIT_LINE);
		displaySystemMessage("1) To add a new task:"+MESSAGE_NL);
		displaySystemMessage("   add|create|new [Task Name] /[*Options]"+MESSAGE_NL);
		displaySystemMessage("   eg. add task /on [Date^]"+MESSAGE_NL);
		displaySystemMessage2("2) To delete a task:"+MESSAGE_NL);
		displaySystemMessage2("   del|delete|remove [Task Number(#)]"+MESSAGE_NL);
		displaySystemMessage2("   eg. del 1"+MESSAGE_NL);
		displaySystemMessage("3) To edit task:"+MESSAGE_NL);
		displaySystemMessage("   edit|update [Task Number(#)] /[*Options]"+MESSAGE_NL);
		displaySystemMessage("   eg. edit 1 /on [Date^]"+MESSAGE_NL);
		displaySystemMessage2("4) To display task list:"+MESSAGE_NL);
		displaySystemMessage2("   display|show overdue|today|done|undone|memos|all"+MESSAGE_NL);
		displaySystemMessage2("   eg. display all"+MESSAGE_NL);
		displaySystemMessage("5) To undo the last action"+MESSAGE_NL);
		displaySystemMessage("   (only applicable to add, edit, delete commands):"+MESSAGE_NL);
		displaySystemMessage("   undo"+MESSAGE_NL);
		displaySystemMessage2("6) To search for a task:"+MESSAGE_NL);
		displaySystemMessage2("   find|search [Task Name or Date^]"+MESSAGE_NL);
		displaySystemMessage2("   eg. find task"+MESSAGE_NL);
		displaySystemMessage("7a) To clear entire DONE tasks:"+MESSAGE_NL);
		displaySystemMessage("    clear"+MESSAGE_NL);
		displaySystemMessage("7b) To clear entire task list:"+MESSAGE_NL);
		displaySystemMessage("    clear all"+MESSAGE_NL);
		displaySystemMessage2("8) To exit the program:"+MESSAGE_NL);
		displaySystemMessage2("   exit|quit or 'Esc'-button"+MESSAGE_NL);	
		displaySystemMessage(MESSAGE_NL);
		displaySystemMessage("Options:"+MESSAGE_NL);
		displaySystemMessage(MESSAGE_SPLIT_LINE);
		displaySystemMessage("a) To insert date: /on [Date^]"+MESSAGE_NL);
		displaySystemMessage2("b) To insert time duration: /from [Time~] to [Time~]"+MESSAGE_NL);
		displaySystemMessage("c) To insert a deadline time: /at [Time~] or /by [Time~]"+MESSAGE_NL);
		displaySystemMessage2("d) To insert a location: /loc [Name of Location]"+MESSAGE_NL);
		displaySystemMessage("e) To mark a task as done: /mark done or /mark undone"+MESSAGE_NL);
		displaySystemMessage2("f) To remove options (Edit Command only): /rm date|time|loc"+MESSAGE_NL);
		displaySystemMessage(MESSAGE_NL);
		displaySystemMessage("Legends:"+MESSAGE_NL);
		displaySystemMessage(MESSAGE_SPLIT_LINE);
		displaySystemMessage("^: Date - 6-digit [DDMMYY], eg. 010114, 311214"+MESSAGE_NL);
		displaySystemMessage("~: Time - 4-digit [HHMM], 24-hours, eg. 1200, 2359"+MESSAGE_NL);
		displaySystemMessage(MESSAGE_NL);
		displaySystemMessage("Keyboard Shortcuts:"+MESSAGE_NL);
		displaySystemMessage(MESSAGE_SPLIT_LINE);
		displaySystemMessage("a) ESC: Exit Program"+MESSAGE_NL);
		displaySystemMessage("b) F1: Help List"+MESSAGE_NL);
		displaySystemMessage("c) F2: Minimize Program"+MESSAGE_NL);
		displaySystemMessage("d) F5: Clear Screen"+MESSAGE_NL);
		displaySystemMessage("e) Up/Down Arrow, Page Up/Down: Scroll Display Box"+MESSAGE_NL);
		displaySystemMessage("f) Home/End: Scroll to Top and Bottom of Display Box"+MESSAGE_NL);
	}

	//Helpo - Keywords that starts with 'C' - Create/Clear
	private String helpoC(String userInput) {
		if(userInput.equals("c")) {
			return "create "+HELPO_FORMAT_TASK_NAME+" "+HELPO_OPTIONS+" | "+HELPO_ACTIONS_CLEAR;
		} else if(userInput.startsWith("cr")) {
			return helpoAdd(userInput);
		} else if(userInput.startsWith("cl")) {
			return helpoClear(userInput);
		} else {
			return MESSAGE_HELP;
		}
	}

	//Helpo - Keywords that starts with 'D' - Del(ete)/Display
	private String helpoD(String userInput) {
		if(userInput.equals("d")) {
			return HELPO_ACTIONS_DELETE+" "+HELPO_FORMAT_TASK_NO+" | "+HELPO_ACTIONS_DISPLAY;
		} else if(userInput.startsWith("di")) {
			return helpoDisplay(userInput);
		} else if(userInput.startsWith("de")) {
			return helpoDelete(userInput);
		} else {
			return MESSAGE_HELP;
		}
	}

	//Helpo - Keywords that starts with 'E' - Edit/Exit
	private String helpoE(String userInput) {
		if(userInput.equals("e")) {
			return HELPO_ACTIONS_EDIT+" | "+HELPO_ACTIONS_EXIT; 
		} else if(userInput.startsWith("ed")) {
			return helpoEdit(userInput);
		} else if(userInput.startsWith("ex")) {
			return helpoExit(userInput);
		} else {
			return MESSAGE_HELP;
		}
	}

	//Helpo - Keywords that starts with 'S' - Search/Show
	private String helpoS(String userInput) {
		if(userInput.equals("s")) {
			return HELPO_ACTIONS_SEARCH+" | "+HELPO_ACTIONS_SHOW; 
		} else if(userInput.startsWith("se")) {
			return helpoSearch(userInput);
		} else if(userInput.startsWith("sh")) {
			return helpoDisplay(userInput);
		} else {
			return MESSAGE_HELP;
		}
	}

	//Helpo - Keywords that starts with 'U' - Update/Undo
	private String helpoU(String userInput) {
		if(userInput.equals("u")) {
			return HELPO_ACTIONS_UPDATE+" | "+HELPO_ACTIONS_UNDO;
		} else if(userInput.startsWith("up")) {
			return helpoEdit(userInput);
		} else if(userInput.startsWith("un")) {
			return helpoUndo(userInput);
		} else {
			return MESSAGE_HELP;
		}
	}

	//Helpo - Add/Create/New
	private String helpoAdd(String userInput) {
		String entered = "";
		switch(userInput) {
		case "a":
		case "ad":
		case "add": entered = HELPO_ACTIONS_ADD; break;
		case "n":
		case "ne":
		case "new": entered = HELPO_ACTIONS_NEW; break;
		case "c":
		case "cr":
		case "cre":
		case "crea":
		case "creat":
		case "create": entered = HELPO_ACTIONS_CREATE; break;
		default: 
			String[] splited = userInput.split(" ");
			if(splited[0].equals(HELPO_ACTIONS_ADD) || splited[0].equals(HELPO_ACTIONS_CREATE) || splited[0].equals(HELPO_ACTIONS_NEW)) {
				entered = splited[0];
			} else {
				return MESSAGE_HELP;
			}
		}
		if(userInput.contains(" /")) {
			return helpoOptions(entered, userInput);
		}
		return entered+" "+HELPO_FORMAT_TASK_NAME+" "+HELPO_OPTIONS;
	}

	//Helpo - Clear
	private String helpoClear(String userInput) {
		if(userInput.equals("c") || userInput.equals("cl") || userInput.equals("cle") || userInput.equals("clea") || userInput.startsWith("clear")) {
			return HELPO_ACTIONS_CLEAR+" ("+HELPO_OPTIONS_CLEAR_ALL+"-clear everything (no confirmation))";
		}
		return MESSAGE_HELP;
	}

	//Helpo - Del(ete)/Remove
	private String helpoDelete(String userInput) {
		String entered = "";
		switch(userInput) {
		case "d":
		case "de":
		case "del": entered = HELPO_ACTIONS_DEL; break;
		case "dele": 
		case "delet":
		case "delete": entered = HELPO_ACTIONS_DELETE; break; 
		case "r":
		case "re":
		case "rem":
		case "remo":
		case "remov":
		case "remove": entered = HELPO_ACTIONS_REMOVE; break;
		default: 
			String[] splited = userInput.split(" ");
			if(splited[0].equals(HELPO_ACTIONS_DEL) || splited[0].equals(HELPO_ACTIONS_DELETE) || splited[0].equals(HELPO_ACTIONS_REMOVE)) {
				entered = splited[0];
			} else {
				return MESSAGE_HELP;
			}
		}
		return entered+" "+HELPO_FORMAT_TASK_NO;
	}

	//Helpo - Display/Show
	private String helpoDisplay(String userInput) {
		String entered = "";
		switch(userInput) {
		case "d":
		case "di":
		case "dis":
		case "disp": 
		case "displ":
		case "displa":
		case "display": entered = HELPO_ACTIONS_DISPLAY; break; 
		case "s":
		case "sh":
		case "sho":
		case "show": entered = HELPO_ACTIONS_SHOW; break;
		default: 
			String[] splited = userInput.split(" ");
			if(splited[0].equals(HELPO_ACTIONS_DISPLAY) || splited[0].equals(HELPO_ACTIONS_SHOW)) {
				entered = splited[0];
			} else {
				return MESSAGE_HELP;
			}
		}
		return entered+" "+HELPO_FORMAT_DISPLAY;
	}

	//Helpo - Edit
	private String helpoEdit(String userInput) {
		String entered = "";
		switch(userInput) {
		case "e":
		case "ed":
		case "edi":
		case "edit": entered = HELPO_ACTIONS_EDIT; break;
		case "u":
		case "up":
		case "upd":
		case "upda":
		case "updat":
		case "update": entered = HELPO_ACTIONS_UPDATE; break;
		default: 
			String[] splited = userInput.split(" ");
			if(splited[0].equals(HELPO_ACTIONS_EDIT) || splited[0].equals(HELPO_ACTIONS_UPDATE)) {
				entered = splited[0];
			} else {
				return MESSAGE_HELP;
			}
		}
		if(userInput.contains(" /")) {
			return helpoOptions(entered, userInput);
		}
		return entered+" "+HELPO_FORMAT_TASK_NO+" "+HELPO_FORMAT_EDIT;
	}

	//Helpo - Edit/Quit
	private String helpoExit(String userInput) {
		String entered = "";
		switch(userInput) {
		case "e":
		case "ex":
		case "exi":
		case "exit": entered = HELPO_ACTIONS_EXIT; break; 
		case "q":
		case "qu":
		case "qui":  
		case "quit": entered = HELPO_ACTIONS_QUIT; break;
		default: 
			String[] splited = userInput.split(" ");
			if(splited[0].equals(HELPO_ACTIONS_EXIT) || splited[0].equals(HELPO_ACTIONS_QUIT)) {
				entered = splited[0];
			} else {
				return MESSAGE_HELP;
			}
		}
		return entered;
	}

	//Helpo - Options
	private String helpoOptions(String action, String userInput) {
		if(action.equals(HELPO_ACTIONS_ADD) || action.equals(HELPO_ACTIONS_CREATE) || action.equals(HELPO_ACTIONS_NEW) || action.equals(HELPO_ACTIONS_EDIT) || action.equals(HELPO_ACTIONS_UPDATE)) {
			Pattern p = Pattern.compile(action+"(.+?)[/\\/w]");
			Matcher m = p.matcher(userInput);

			if(m.find() && m.group(1).trim().equals("")) {
				return "Please enter a task name";
			} else {
				Pattern pOn = Pattern.compile("(/on)( )([0-9]{6})");
				Matcher mOn = pOn.matcher(userInput);

				Pattern pLoc = Pattern.compile("(/loc)( )(\\w{0,15})");
				Matcher mLoc = pLoc.matcher(userInput);

				Pattern pFrom = Pattern.compile("(/from)( )([0-9]{4})");
				Matcher mFrom = pFrom.matcher(userInput);

				Pattern pTo = Pattern.compile("(to)([0-9]{4})");
				Matcher mTo = pTo.matcher(userInput);

				Pattern pAt = Pattern.compile("(/at)( )([0-9]{0,4})");
				Matcher mAt = pAt.matcher(userInput);

				Pattern pBy = Pattern.compile("(/by)( )([0-9]{0,4})");
				Matcher mBy = pBy.matcher(userInput);

				Pattern pMark = Pattern.compile("(/mark)( )([a-zA-Z]{0,6})");
				Matcher mMark = pMark.matcher(userInput);

				Pattern pRemove = Pattern.compile("(/rm)( )([a-zA-Z]{0,4})");
				Matcher mRemove = pRemove.matcher(userInput);

				if(userInput.endsWith("/")) {
					String options = HELPO_OPTIONS_ON+" "+HELPO_OPTIONS_FROM+" OR "+HELPO_OPTIONS_AT_BY+" "+HELPO_OPTIONS_LOC+" "+HELPO_OPTIONS_MARK;
					if(action.equalsIgnoreCase("edit")) {
						options += " "+HELPO_OPTIONS_REMOVE;
					}
					return options; 
				} else if(userInput.endsWith("/o") || userInput.endsWith("/on") && !mOn.find()) {
					return HELPO_OPTIONS_ON+" "+" "+HELPO_FORMAT_ON+" "+HELPO_FORMAT_DATE;
				} else if(userInput.endsWith("/l") || userInput.endsWith("/lo") || userInput.endsWith("/loc") && !mLoc.find()) {
					return HELPO_OPTIONS_LOC+" "+HELPO_FORMAT_LOC;
				} else if(userInput.endsWith("/f") || userInput.endsWith("/fr") || userInput.endsWith("/fro") || userInput.endsWith("/from") && !mFrom.find()) {
					return HELPO_OPTIONS_FROM+" "+HELPO_FORMAT_FROM_TO+" "+HELPO_FORMAT_TIME;
				} else if(userInput.endsWith("to") && !mTo.find()) {
					return HELPO_OPTIONS_FROM+" "+HELPO_FORMAT_FROM_TO+" "+HELPO_FORMAT_TIME;
				} else if(userInput.endsWith("/a") || userInput.endsWith("/at") && !mAt.find()) {
					return HELPO_OPTIONS_AT+" "+HELPO_FORMAT_AT_BY+" "+HELPO_FORMAT_TIME;
				} else if(userInput.endsWith("/b") || userInput.endsWith("/by") && !mBy.find()) {
					return HELPO_OPTIONS_BY+" "+HELPO_FORMAT_AT_BY+" "+HELPO_FORMAT_TIME;
				} else if(userInput.endsWith("/m") || userInput.endsWith("/ma") || userInput.endsWith("/mar") || userInput.endsWith("/mark") && !mMark.find()) {
					return HELPO_OPTIONS_MARK+" "+HELPO_FORMAT_MARK;
				} else if((userInput.endsWith("/r") || userInput.endsWith("/rm") && !mRemove.find()) && action.equals("edit")) {
					return HELPO_OPTIONS_REMOVE+" "+HELPO_FORMAT_REMOVE;
				}
			}
		} 
		return helpo.getText();
	}

	//Helpo - Find/Search
	private String helpoSearch(String userInput) {
		String entered = "";
		switch(userInput) {
		case "f":
		case "fi":
		case "fin":
		case "find": entered = HELPO_ACTIONS_FIND; break;
		case "s":
		case "se":
		case "sea":
		case "sear":
		case "searc":
		case "search": entered = HELPO_ACTIONS_SEARCH; break;
		default: 
			String[] splited = userInput.split(" ");
			if(splited[0].equals("find") || splited[0].equals("search")) {
				entered = splited[0];
			} else {
				return MESSAGE_HELP;
			}
		}
		return entered+" "+HELPO_FORMAT_SEARCH;
	}

	//Helpo - Undo
	private String helpoUndo(String userInput) {
		if(userInput.equals("u") || userInput.equals("un") || userInput.equals("und") || userInput.startsWith("undo")) {
			return HELPO_ACTIONS_UNDO;
		}
		return MESSAGE_HELP;
	}

	//Get Today's Date
	private static String getTodayDate() {
		DateTimeConfiguration dTC = new DateTimeConfiguration();
		System.out.println(dTC.getTodaysDate());
		return dTC.getDateForDisplay(dTC.getTodaysDate());
	}

	//Printing of Tasks
	private static void printResults(Result result) {
		Queue<String> headings = result.getHeadings();
		Queue<Queue<String>> body = result.getBody();
		int highlightIndexI = result.getHighlightIndexI();
		int highlightIndexJ = result.getHighlightIndexJ();

		//system feedback
		displayToHelpo(result.getSystemFeedback());

		if(result.getSystemFeedback().equals(MESSAGE_INVALID_COMMAND)) {
			initializeTimer();
		} else {
			initializeTimer();
			clearDisplayBox();

			int numOfHeadings = headings.size();
			for (int i = 0; i < numOfHeadings; i++) {
				String heading = headings.poll();
				//heading
				if(heading.contains("Overdue")) {
					displayOverdueTasks(heading);
					headings.offer(heading);

					Queue<String> bodyOfThisHeading = body.poll();

					int numOfTasks = bodyOfThisHeading.size();
					for (int j = 0; j < numOfTasks ; j++) {
						String task = bodyOfThisHeading.poll();
						if (i == highlightIndexI && j == highlightIndexJ) {
							//highlighted task
							displayHighlightMessage(task);
						}
						else {
							//task
							displayOverdueTasks(task);
						}
						bodyOfThisHeading.offer(task);
					}
					displaySystemMessage(MESSAGE_NL);
				} else if(heading.contains("Today")) {
					displayTodayTasks(heading);
					headings.offer(heading);

					Queue<String> bodyOfThisHeading = body.poll();

					int numOfTasks = bodyOfThisHeading.size();
					for (int j = 0; j < numOfTasks ; j++) {
						String task = bodyOfThisHeading.poll();
						if (i == highlightIndexI && j == highlightIndexJ) {
							//highlighted task
							displayHighlightMessage(task);
						}
						else {
							//task
							displayTodayTasks(task);
						}
						bodyOfThisHeading.offer(task);
					}
					displaySystemMessage(MESSAGE_NL);
				} else if(heading.contains("This")) {
					displayThisWeekTasks(heading);
					headings.offer(heading);

					Queue<String> bodyOfThisHeading = body.poll();

					int numOfTasks = bodyOfThisHeading.size();
					for (int j = 0; j < numOfTasks ; j++) {
						String task = bodyOfThisHeading.poll();
						if (i == highlightIndexI && j == highlightIndexJ) {
							//highlighted task
							displayHighlightMessage(task);
						}
						else {
							//task
							displayThisWeekTasks(task);
						}
						bodyOfThisHeading.offer(task);
					}
					displaySystemMessage(MESSAGE_NL);
				} else if(heading.contains("More")) {
					displayNextWeekTasks(heading);
					headings.offer(heading);

					Queue<String> bodyOfThisHeading = body.poll();

					int numOfTasks = bodyOfThisHeading.size();
					for (int j = 0; j < numOfTasks ; j++) {
						String task = bodyOfThisHeading.poll();
						if (i == highlightIndexI && j == highlightIndexJ) {
							//highlighted task
							displayHighlightMessage(task);
						}
						else {
							//task
							displayNextWeekTasks(task);
						}
						bodyOfThisHeading.offer(task);
					}
					displaySystemMessage(MESSAGE_NL);
				} else if(heading.contains("Memo")) {
					displayMemos(heading);
					headings.offer(heading);

					Queue<String> bodyOfThisHeading = body.poll();

					int numOfTasks = bodyOfThisHeading.size();
					for (int j = 0; j < numOfTasks ; j++) {
						String task = bodyOfThisHeading.poll();
						if (i == highlightIndexI && j == highlightIndexJ) {
							//highlighted task
							displayHighlightMessage(task);
						}
						else {
							//task
							displayMemos(task);
						}
						bodyOfThisHeading.offer(task);
					}
					displaySystemMessage(MESSAGE_NL);
				} else if(heading.contains("Completed")) {
					displayDoneTasks(heading);
					headings.offer(heading);

					Queue<String> bodyOfThisHeading = body.poll();

					int numOfTasks = bodyOfThisHeading.size();
					for (int j = 0; j < numOfTasks ; j++) {
						String task = bodyOfThisHeading.poll();
						if (i == highlightIndexI && j == highlightIndexJ) {
							//highlighted task
							displayHighlightMessage(task);
						}
						else {
							//task
							displayDoneTasks(task);
						}
						bodyOfThisHeading.offer(task);
					}
					displaySystemMessage(MESSAGE_NL);
				} else {
					displaySystemMessage(heading);
					headings.offer(heading);

					Queue<String> bodyOfThisHeading = body.poll();

					int numOfTasks = bodyOfThisHeading.size();
					for (int j = 0; j < numOfTasks ; j++) {
						String task = bodyOfThisHeading.poll();
						if (i == highlightIndexI && j == highlightIndexJ) {
							//highlighted task
							displayHighlightMessage(task);
						}
						else {
							//task
							displaySystemMessage(task);
						}
						bodyOfThisHeading.offer(task);
					}
					displaySystemMessage(MESSAGE_NL);
				}
			}
		}
	}
}