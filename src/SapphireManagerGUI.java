//@author A0097706U
//This GUI class handles the input and output to and from user and system

//JAVA-AWT LIBRARIES
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
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
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
//import javax.swing.Timer;
import java.awt.FlowLayout;
import java.io.File;

//SapphireManagerGUI CLASS
public class SapphireManagerGUI {
	//@author A0097706U
	//SYSTEM MESSAGES
	private final static String MESSAGE_HELP = "Press F1 for help or F3 to display today's tasks.";
	private final static String MESSAGE_NL = "\n";
	private final static String MESSAGE_SPLIT_LINE = "---------------------------------------------------------------------------------------------------------------"+MESSAGE_NL;
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
	private static CommandExecutor myExecutor;
	private static SapphireManagerGUI guiWindow;
	//private static ActionListener timeListener;
	//private static Timer timer = new Timer(5000, timeListener);;

	//GUI COMPONENT DECLARATIONS
	private Dimension toolkit;
	private JFrame guiFrame;
	private JLabel logoLabel;
	private JPanel datePanel;
	private JPanel helpoPanel;
	private JPanel inputBoxPanel;
	private JPanel logoPanel;
	private static JLabel dateLabel;
	private static JLabel helpo;
	private static JScrollPane scrollPane;
	private static JTextField inputBox;
	private static JTextPane displayBox;

	//@author A0097706U
	//MAIN METHOD
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				initializeMain();
			}
		});
	}

	//@author A0097706U
	//initializes main 
	private static void initializeMain() {
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

	//@author A0097706U
	//initializes and opens everything needed for the entire program 
	private SapphireManagerGUI() {	
		initialize();
		start();
	}

	//@author A0097706U
	//initialize and open the program in the following order
	private void initialize() {		
		initializeSapphireManager();
		initializeLogoInPanel();
		initializeDateLabelInPanel();
		initializeDisplayBoxInScrollPane();
		initializeHelpoInPanel();
		initializeInputBoxInPanel();
		//startTimer();
		contentPaneDisplay();	
	}

	//@author A0097706U
	//displays messages and start listeners
	private void start() {
		displayToDateLabel();
		guiFrameListener();
		inputBoxListener();
		displayBoxListener();
		helpoListener();		
		//timerListener();
	}

	//@author A0097706U
	//build the main frame for GUI
	private void initializeSapphireManager() {
		guiFrame = new JFrame();

		guiFrame.setBounds(100, 100, 450, 700);
		guiFrame.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 14));
		guiFrame.getContentPane().setBackground(new Color(0x231F20));
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setResizable(false);
		guiFrame.setTitle("Sapphire Manager");	

		//sets favicon to program
		ImageIcon icon = new ImageIcon(getClass().getResource("img/logoIcon.png"));
		guiFrame.setIconImage(icon.getImage());

		//sets the initial display position of the program
		toolkit = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int)((toolkit.getWidth() - guiFrame.getWidth()) /2);
		//int y = (int)((toolkit.getHeight() - guiFrame.getHeight()) /2);
		guiFrame.setLocation(x, 5);
	}

	//@author A0097706U
	//initializes date label within a panel
	private void initializeDateLabelInPanel() {
		dateLabel = new JLabel();
		dateLabel.setVerifyInputWhenFocusTarget(false);
		dateLabel.setRequestFocusEnabled(false);
		dateLabel.setInheritsPopupMenu(false);
		dateLabel.setFocusable(false);
		dateLabel.setFocusTraversalKeysEnabled(false);
		datePanel = new JPanel();
		datePanel.setFocusTraversalKeysEnabled(false);
		datePanel.setFocusable(false);
		datePanel.setOpaque(false);
		datePanel.setRequestFocusEnabled(false);
		datePanel.setVerifyInputWhenFocusTarget(false);
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

	//@author A0097706U
	//initializes display box within a scroll pane
	private void initializeDisplayBoxInScrollPane() {
		displayBox = new JTextPane();
		displayBox.setFocusable(false);
		displayBox.setFocusCycleRoot(false);
		displayBox.setFocusTraversalKeysEnabled(false);
		scrollPane = new JScrollPane(displayBox);

		scrollPane.setBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMaximumSize(new Dimension(425, 500));
		scrollPane.setPreferredSize(new Dimension(425, 470));

		displayBox.setBackground(new Color(0x231F20));
		displayBox.setBorder(null);
		displayBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		displayBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		displayBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayBox.setMaximumSize(new Dimension(450, 350));
	}

	//@author A0097706U
	//initializes input text box within a panel
	private void initializeInputBoxInPanel() {	
		inputBox = new JTextField();
		inputBoxPanel = new JPanel();
		inputBoxPanel.setVerifyInputWhenFocusTarget(false);
		inputBoxPanel.setRequestFocusEnabled(false);
		inputBoxPanel.setFocusTraversalKeysEnabled(false);
		inputBoxPanel.setFocusable(false);
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
		inputBox.setPreferredSize(new Dimension(425, 25));
	}

	//@author A0097706U
	//initializes helpo label within a panel
	private void initializeHelpoInPanel() {
		helpo = new JLabel();
		helpo.setForeground(Color.WHITE);
		helpoPanel = new JPanel();
		helpoPanel.setOpaque(false);
		helpoPanel.setRequestFocusEnabled(false);
		helpoPanel.setVerifyInputWhenFocusTarget(false);
		helpoPanel.setFocusable(false);
		helpoPanel.setFocusTraversalKeysEnabled(false);
		helpoPanel.add(helpo);

		helpoPanel.setBackground(Color.WHITE);
		helpoPanel.setMaximumSize(new Dimension(450, 35));
		helpoPanel.setPreferredSize(new Dimension(450, 35));

		helpo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		helpo.setHorizontalAlignment(SwingConstants.LEFT);
		helpo.setPreferredSize(new Dimension(425, 30));

		displayToHelpo(MESSAGE_HELP);
	}

	//@author A0097706U
	//initializes logo image within a panel
	private void initializeLogoInPanel() {
		logoLabel = new JLabel("");
		logoLabel.setInheritsPopupMenu(false);
		logoLabel.setRequestFocusEnabled(false);
		logoLabel.setVerifyInputWhenFocusTarget(false);
		logoLabel.setFocusTraversalKeysEnabled(false);
		logoLabel.setFocusable(false);
		logoPanel = new JPanel();
		logoPanel.setEnabled(false);
		logoPanel.setFocusable(false);
		logoPanel.setFocusTraversalKeysEnabled(false);
		logoPanel.add(logoLabel);

		logoPanel.setBackground(new Color(0x231F20));
		logoPanel.setMaximumSize(new Dimension(450, 50));
		logoPanel.setPreferredSize(new Dimension(380, 40));

		ImageIcon icon = new ImageIcon(getClass().getResource("img/logo-trans.png"));
		logoLabel.setIcon(icon);
	}

	//@author A0097706U-unused
	//reasons for not using: buggy and insufficient time to fix
	//initializes the timer
	//private static void startTimer() {
	//	timer 
	//	if(!timer.isRunning()) {
	//		System.out.println("Start timer");
	//		timer.start();
	//	} else {
	//		System.out.println("Restart timer");
	//		timer.restart();
	//	}
	//	System.out.println("4) Timer is running?" +timer.isRunning());
	//}

	//@author A0097706U
	//sets up the frame in Box Layout with components in the following order
	//ordering is important!
	private void contentPaneDisplay() {
		guiFrame.getContentPane().setLayout(new BoxLayout(guiFrame.getContentPane(), BoxLayout.Y_AXIS));		
		guiFrame.getContentPane().add(logoPanel, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(datePanel, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(scrollPane, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(helpoPanel, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(inputBoxPanel, Component.CENTER_ALIGNMENT);
	}

	//@author A0097706U
	//set display to the top
	private void setDisplayToTop() {
		displayBox.setCaretPosition(0);
	}

	//@author A0097706U-unused
	//not used because there are no cases that requires it
	//set display to the bottom
	//private void setDisplayToBottom() {
	//	displayBox.setCaretPosition(displayBox.getDocument().getLength());
	//}

	//@author A0097706U
	//Listener for input box
	private void inputBoxListener() {
		inputBox.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {
				keyboardExecution(e); 
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

	//@author A0097706U
	//Keyboard execution
	private void keyboardExecution(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			inputExecution();
		} else if(e.getKeyCode() == KeyEvent.VK_F1) {
			clearDisplayBox();
			displayHelp();
			displayToHelpo("Displaying Help List");
			setDisplayToTop();
		} else if(e.getKeyCode() == KeyEvent.VK_F5) {
			clearDisplayBox();
			displayToHelpo("Screen cleared");
		} else if(e.getKeyCode() == KeyEvent.VK_F2) {
			guiFrame.setState(Frame.ICONIFIED);
		} else if(e.getKeyCode() == KeyEvent.VK_F3) {
			printResults(myExecutor.doUserOperation("display today"));
			setDisplayToTop();
		} else if(e.getKeyCode() == KeyEvent.VK_F10) {
			openTextFile();
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
		}
	}

	//@author A0097706U
	//Executes user's input
	private void inputExecution() {
		if(!inputBox.getText().trim().equals("")) {
			String userCommand = readCommandFromUser();
			Result result = myExecutor.doUserOperation(userCommand);
			printResults(result);

			if(helpo.getText().trim().equals("")) {
				displayToHelpo(MESSAGE_HELP);
			}
			setDisplayToTop();
			inputBox.setText("");
		}
	}

	//@author A0097706U
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

	//@author A0097706U
	//Listener for display box
	private void displayBoxListener() {
		displayBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				inputBox.requestFocus();
			}
		});
	}

	//@author A0097706U-unused
	//reasons for not using: buggy and insufficient time to fix
	//private void timerListener() {
	//	timeListener = new ActionListener() {
	//		public void actionPerformed(ActionEvent e) {
	//			System.out.println("1) Timer is running? "+timer.isRunning());
	//			timer.stop();
	//			System.out.println("2) Timer is running? "+timer.isRunning());
	//			if(!timer.isRunning()) {
	//				System.out.println("3) Timer is running? "+timer.isRunning());
	//				displayToHelpo(MESSAGE_HELP);
	//				displayWelcomeMessage();
	//			}
	//		}
	//	};
	//}

	//@author A0097706U
	//Listener for Helpo
	private void helpoListener() {
		inputBox.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent arg0) {
				String userInput = inputBox.getText().toLowerCase().trim();
				if(userInput.equals("")) {
					if(inputBox.getText().trim().equals("") && helpo.getText().equals("")) {
						displayToHelpo(MESSAGE_HELP);
					} else {
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
					displayToHelpo(MESSAGE_INVALID_COMMAND);
				}
			}
		});
	}

	//@author A0097706U
	//read user's input
	private String readUserInput() {
		String userInput = null;
		if(!inputBox.equals(null)) {
			userInput = inputBox.getText();
		}
		return userInput;
	}

	//@author A0097706U
	//read user's command
	private String readCommandFromUser() {
		return readUserInput();
	}

	//author A0097706U
	//opens the text document
	private void openTextFile() {
		Desktop desktop = Desktop.getDesktop();
		try {
			File file = new File(System.getProperty("user.dir")+"/mytextfile.txt");
			desktop.open(file);
			displayPopUpMsg("Do not modify the file!");
		} catch (Exception ex) {
			displayPopUpMsg("File not found!");
		}
		System.out.println(System.getProperty("user.dir"));
	}

	//@author A0097706U
	//display pop up message for errors
	private void displayPopUpMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

	//@author A0097706U
	//displays to helpo label
	private static void displayToHelpo(String message) {
		helpo.setText(message);
	}

	//@author A0097706U
	//displays today's date
	private static void displayToDateLabel() {
		dateLabel.setText("TODAY'S DATE: "+getTodayDate());
	}

	//@author A0097706U
	//Get Today's Date
	private static String getTodayDate() {
		DateTimeConfiguration dTC = new DateTimeConfiguration();
		System.out.println(dTC.getTodaysDate());
		return dTC.getDateForDisplay(dTC.getTodaysDate());
	}

	//@author A0097706U
	//displays system message
	private static void displaySystemMessage(String message) {
		appendToDisplayBox(message, new Color(0xff6c00), "Trebuchet MS", 14, false);
	}

	//@author A0097706U
	//displays system message
	private static void displaySystemMessage2(String message) {
		appendToDisplayBox(message, new Color(0xff6c55), "Trebuchet MS", 14, false);
	}

	//@author A0097706U
	//displays highlighted message in the selected color
	private static void displayHighlightMessage(String message) {
		appendToDisplayBox(message, new Color(0x00FF00), "Trebuchet MS", 14, true);
	}

	//@author A0097706U
	//displays overdue tasks in the selected color
	private static void displayOverdueTasks(String message) {
		appendToDisplayBox(message, Color.RED, "Trebuchet MS", 14, false);
	}

	//@author A0097706U
	//displays today's tasks in the selected color
	private static void displayTodayTasks(String message) {
		appendToDisplayBox(message, new Color(0xff6c00), "Trebuchet MS", 14, false);
	}

	//@author A0097706U
	//displays this week's tasks in the selected color
	private static void displayThisWeekTasks(String message) {
		appendToDisplayBox(message, new Color(0xfd8c38), "Trebuchet MS", 14, false);
	}

	//@author A0097706U 
	//displays next week's tasks in the selected color
	private static void displayNextWeekTasks(String message) {
		appendToDisplayBox(message, new Color(0xfba76c), "Trebuchet MS", 14, false);
	}

	//@author A0097706U
	//displays memos in the selected color
	private static void displayMemos(String message) {
		appendToDisplayBox(message, new Color(0xf9bc90), "Trebuchet MS", 14, false);
	}

	//@author A0097706U
	//displays completed tasks in the selected color
	private static void displayDoneTasks(String message) {
		appendToDisplayBox(message, new Color(0xf9d1b6), "Trebuchet MS", 14, false);
	}

	//@author A0097706U
	//displays the latest messages
	private static void appendToDisplayBox(String message, Color color, String fontName, int fontSize, boolean isBold) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,  StyleConstants.Foreground, color);

		aset = sc.addAttribute(aset, StyleConstants.FontSize, fontSize);
		aset = sc.addAttribute(aset, StyleConstants.FontFamily,  fontName);
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
		aset = sc.addAttribute(aset, StyleConstants.Bold, isBold);

		displayBox.setCharacterAttributes(aset, false);
		displayBox.replaceSelection(message);
	}

	//@author A0097706U
	//clears the display box
	private static void clearDisplayBox() {
		displayBox.setText("");
	}

	//@author A0097706U
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
		displaySystemMessage2("   exit|quit or 'ESC'-button"+MESSAGE_NL);	
		displaySystemMessage(MESSAGE_NL);
		displaySystemMessage("Options:"+MESSAGE_NL);
		displaySystemMessage(MESSAGE_SPLIT_LINE);
		displaySystemMessage("a) To insert date: /on [Date^]"+MESSAGE_NL);
		displaySystemMessage2("b) To insert time duration: /from [Time~] to [Time~]"+MESSAGE_NL);
		displaySystemMessage("c) To insert a deadline time: /at [Time~] or /by [Time~]"+MESSAGE_NL);
		displaySystemMessage2("d) To insert a location: /loc [Name of Location]"+MESSAGE_NL);
		displaySystemMessage("e) To mark a task as done (Edit Command): /mark done or /mark undone"+MESSAGE_NL);
		displaySystemMessage2("f) To remove options (Edit Command): /rm date|time|loc"+MESSAGE_NL);
		displaySystemMessage(MESSAGE_NL);
		displaySystemMessage("Legends:"+MESSAGE_NL);
		displaySystemMessage(MESSAGE_SPLIT_LINE);
		displaySystemMessage("Date^: 6-digit [DDMMYY], eg. 010114, 311214"+MESSAGE_NL);
		displaySystemMessage("Time~: 4-digit [HHMM], 24-hours, eg. 1200, 2359"+MESSAGE_NL);
		displaySystemMessage(MESSAGE_NL);
		displaySystemMessage("Keyboard Shortcuts:"+MESSAGE_NL);
		displaySystemMessage(MESSAGE_SPLIT_LINE);
		displaySystemMessage("a) ESC: Exit Program"+MESSAGE_NL);
		displaySystemMessage("b) F1: Help List"+MESSAGE_NL);
		displaySystemMessage("c) F2: Minimize Program"+MESSAGE_NL);
		displaySystemMessage("d) F3: Display today's tasks"+MESSAGE_NL);
		displaySystemMessage("e) F5: Clear Screen"+MESSAGE_NL);
		displaySystemMessage("f) F10: Opens file of tasks."+MESSAGE_NL);
		displaySystemMessage("g) Up/Down Arrow, Page Up/Down: Scroll Display Box"+MESSAGE_NL);
		displaySystemMessage("h) Home/End: Scroll to Top and Bottom of Display Box"+MESSAGE_NL);
	}

	//@author A0097706U
	//Helpo - Keywords that starts with 'C' - Create/Clear
	private String helpoC(String userInput) {
		if(userInput.equals("c")) {
			return "create "+HELPO_FORMAT_TASK_NAME+" "+HELPO_OPTIONS+" | "+HELPO_ACTIONS_CLEAR;
		} else if(userInput.startsWith("cr")) {
			return helpoAdd(userInput);
		} else if(userInput.startsWith("cl")) {
			return helpoClear(userInput);
		} else {
			return MESSAGE_INVALID_COMMAND;
		}
	}

	//@author A0097706U
	//Helpo - Keywords that starts with 'D' - Del(ete)/Display
	private String helpoD(String userInput) {
		if(userInput.equals("d")) {
			return HELPO_ACTIONS_DELETE+" "+HELPO_FORMAT_TASK_NO+" | "+HELPO_ACTIONS_DISPLAY;
		} else if(userInput.startsWith("di")) {
			return helpoDisplay(userInput);
		} else if(userInput.startsWith("de")) {
			return helpoDelete(userInput);
		} else {
			return MESSAGE_INVALID_COMMAND;
		}
	}

	//@author A0097706U
	//Helpo - Keywords that starts with 'E' - Edit/Exit
	private String helpoE(String userInput) {
		if(userInput.equals("e")) {
			return HELPO_ACTIONS_EDIT+" | "+HELPO_ACTIONS_EXIT; 
		} else if(userInput.startsWith("ed")) {
			return helpoEdit(userInput);
		} else if(userInput.startsWith("ex")) {
			return helpoExit(userInput);
		} else {
			return MESSAGE_INVALID_COMMAND;
		}
	}

	//@author A0097706U
	//Helpo - Keywords that starts with 'S' - Search/Show
	private String helpoS(String userInput) {
		if(userInput.equals("s")) {
			return HELPO_ACTIONS_SEARCH+" | "+HELPO_ACTIONS_SHOW; 
		} else if(userInput.startsWith("se")) {
			return helpoSearch(userInput);
		} else if(userInput.startsWith("sh")) {
			return helpoDisplay(userInput);
		} else {
			return MESSAGE_INVALID_COMMAND;
		}
	}

	//@author A0097706U
	//Helpo - Keywords that starts with 'U' - Update/Undo
	private String helpoU(String userInput) {
		if(userInput.equals("u")) {
			return HELPO_ACTIONS_UPDATE+" | "+HELPO_ACTIONS_UNDO;
		} else if(userInput.startsWith("up")) {
			return helpoEdit(userInput);
		} else if(userInput.startsWith("un")) {
			return helpoUndo(userInput);
		} else {
			return MESSAGE_INVALID_COMMAND;
		}
	}

	//@author A0097706U
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
				return MESSAGE_INVALID_COMMAND;
			}
		}
		if(userInput.contains(" /")) {
			return helpoOptions(entered, userInput);
		}
		return entered+" "+HELPO_FORMAT_TASK_NAME+" "+HELPO_OPTIONS;
	}

	//@author A0097706U
	//Helpo - Clear
	private String helpoClear(String userInput) {
		if(userInput.equals("c") || userInput.equals("cl") || userInput.equals("cle") || userInput.equals("clea") || userInput.startsWith("clear")) {
			return HELPO_ACTIONS_CLEAR+" ("+HELPO_OPTIONS_CLEAR_ALL+"-clear everything (no confirmation))";
		}
		return MESSAGE_INVALID_COMMAND;
	}

	//@author A0097706U
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
				return MESSAGE_INVALID_COMMAND;
			}
		}
		return entered+" "+HELPO_FORMAT_TASK_NO;
	}

	//@author A0097706U
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
				return MESSAGE_INVALID_COMMAND;
			}
		}
		return entered+" "+HELPO_FORMAT_DISPLAY;
	}

	//@author A0097706U
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
				return MESSAGE_INVALID_COMMAND;
			}
		}
		if(userInput.contains(" /")) {
			return helpoOptions(entered, userInput);
		}
		return entered+" "+HELPO_FORMAT_TASK_NO+" "+HELPO_FORMAT_EDIT;
	}

	//@author A0097706U
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
				return MESSAGE_INVALID_COMMAND;
			}
		}
		return entered;
	}

	//@author A0097706U
	//Helpo - Options
	private String helpoOptions(String action, String userInput) {
		if(action.equals(HELPO_ACTIONS_ADD) || action.equals(HELPO_ACTIONS_CREATE) || action.equals(HELPO_ACTIONS_NEW) || action.equals(HELPO_ACTIONS_EDIT) || action.equals(HELPO_ACTIONS_UPDATE)) {
			Pattern p = Pattern.compile(action+"(.+?)[/\\/w]");
			Matcher m = p.matcher(userInput);

			if(m.find() && m.group(1).trim().equals("") && action.equals("add")) {
				return "Please enter a task name";
			} else {
				return addEditOptions(action, userInput);
			}
		} 
		return helpo.getText();
	}

	//@author A0097706U
	//Options for Add and Edit
	private String addEditOptions(String action, String userInput) {
		Pattern pOn = Pattern.compile("(/on)( )([0-9]{6})");
		Matcher mOn = pOn.matcher(userInput);

		Pattern pLoc = Pattern.compile("(/loc)( )(\\w{0,30})");
		Matcher mLoc = pLoc.matcher(userInput);

		Pattern pFrom = Pattern.compile("(/from)( )([0-9]{10})");
		Matcher mFrom = pFrom.matcher(userInput);

		Pattern pTo = Pattern.compile("(to)([0-9]{8})");
		Matcher mTo = pTo.matcher(userInput);

		Pattern pAt = Pattern.compile("(/at)( )([0-9]{0,10})");
		Matcher mAt = pAt.matcher(userInput);

		Pattern pBy = Pattern.compile("(/by)( )([0-9]{0,10})");
		Matcher mBy = pBy.matcher(userInput);

		Pattern pMark = Pattern.compile("(/mark)( )([a-zA-Z]{0,6})");
		Matcher mMark = pMark.matcher(userInput);

		Pattern pRemove = Pattern.compile("(/rm)( )([a-zA-Z]{0,5})");
		Matcher mRemove = pRemove.matcher(userInput);

		if(userInput.endsWith("/")) {
			String options = HELPO_OPTIONS_ON+" "+HELPO_OPTIONS_FROM+" OR "+HELPO_OPTIONS_AT_BY+" "+HELPO_OPTIONS_LOC;					
			if(action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("update")) {
				options += " "+HELPO_OPTIONS_MARK+" "+HELPO_OPTIONS_REMOVE;
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
		} else if((action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("update")) && (userInput.endsWith("/m") || userInput.endsWith("/ma") || userInput.endsWith("/mar") || userInput.endsWith("/mark") && !mMark.find())) {
			return HELPO_OPTIONS_MARK+" "+HELPO_FORMAT_MARK;
		} else if((action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("update")) && ((userInput.endsWith("/r") || userInput.endsWith("/rm") && !mRemove.find()) && action.equals("edit"))) {
			return HELPO_OPTIONS_REMOVE+" "+HELPO_FORMAT_REMOVE;
		} else {
			switch(locateLastOption(userInput)) {
			case "/on": 
				return HELPO_OPTIONS_ON+" "+" "+HELPO_FORMAT_ON+" "+HELPO_FORMAT_DATE;
			case "/loc": 
				return HELPO_OPTIONS_LOC+" "+HELPO_FORMAT_LOC;
			case "/from": 
				return HELPO_OPTIONS_FROM+" "+HELPO_FORMAT_FROM_TO+" "+HELPO_FORMAT_TIME;
			case "to": 
				return HELPO_OPTIONS_FROM+" "+HELPO_FORMAT_FROM_TO+" "+HELPO_FORMAT_TIME;
			case "/at": 
				return HELPO_OPTIONS_FROM+" "+HELPO_FORMAT_FROM_TO+" "+HELPO_FORMAT_TIME;
			case "/by": 
				return HELPO_OPTIONS_BY+" "+HELPO_FORMAT_AT_BY+" "+HELPO_FORMAT_TIME;
			case "/mark": 
				if(action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("update")) {
					return HELPO_OPTIONS_MARK+" "+HELPO_FORMAT_MARK;
				}
			case "/rm": 
				if(action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("update")) {
					return HELPO_OPTIONS_REMOVE+" "+HELPO_FORMAT_REMOVE;
				}
			default: 
				return MESSAGE_INVALID_COMMAND;
			}
		}
	}
	
	//@author A0097706U
	//check for last input option
	private String locateLastOption(String userInput) {
		int[] optionLastIndex = new int[9];
		int lastInput = 8;

		optionLastIndex[0] = userInput.indexOf("/on");
		optionLastIndex[1] = userInput.indexOf("/loc");
		optionLastIndex[2] = userInput.indexOf("/from");
		optionLastIndex[3] = userInput.indexOf("/to");
		optionLastIndex[4] = userInput.indexOf("/at");
		optionLastIndex[5] = userInput.indexOf("/by");
		optionLastIndex[6] = userInput.indexOf("/mark");
		optionLastIndex[7] = userInput.indexOf("/rm");


		for(int i=0; i<optionLastIndex.length; i++) {
			if(optionLastIndex[lastInput] < optionLastIndex[i]) {
				lastInput = i;
			}
		}
		
		System.out.println(lastInput);
		switch(lastInput) {
		case 0: lastInput = 8; return "/on"; 
		case 1: lastInput = 8; return "/loc"; 
		case 2: lastInput = 8; return "/from"; 
		case 3: lastInput = 8; return "/to"; 
		case 4: lastInput = 8; return "/at"; 
		case 5: lastInput = 8; return "/by"; 
		case 6: lastInput = 8; return "/mark"; 
		case 7: lastInput = 8; return "/rm"; 
		default: return "";
		}
	}

	//@author A0097706U
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
				return MESSAGE_INVALID_COMMAND;
			}
		}
		return entered+" "+HELPO_FORMAT_SEARCH;
	}

	//@author A0097706U
	//Helpo - Undo
	private String helpoUndo(String userInput) {
		if(userInput.equals("u") || userInput.equals("un") || userInput.equals("und") || userInput.startsWith("undo")) {
			return HELPO_ACTIONS_UNDO;
		}
		return MESSAGE_INVALID_COMMAND;
	}

	//@author A0097706U
	//Printing of Tasks
	private static void printResults(Result result) {
		Queue<String> headings = result.getHeadings();
		Queue<Queue<String>> body = result.getBody();
		int highlightIndexI = result.getHighlightIndexI();
		int highlightIndexJ = result.getHighlightIndexJ();

		//system feedback
		displayToHelpo(result.getSystemFeedback());
		System.out.println(result.getSystemFeedback());
		if(result.getSystemFeedback().equals("Displaying tasks for today.")) {
			displayToHelpo(helpo.getText()+" "+MESSAGE_HELP);
		}

		if(!result.getSystemFeedback().contains("ERROR")) {
			clearDisplayBox();
			if(result.getSystemFeedback().startsWith("You have no")) {
				displayHighlightMessage(result.getSystemFeedback()+"\nPress F1 for help or F3 to display today's tasks.");
			}

			printResults(headings, body, highlightIndexI, highlightIndexJ);
		}


	}

	//@author A0097706U
	//print the task list based on respective results
	private static void printResults(Queue<String> headings, Queue<Queue<String>> body, int highlightIndexI, int highlightIndexJ) {
		for (int i = 0; i < headings.size(); i++) {
			String heading = headings.poll();
			//heading
			if(heading.contains("Overdue")) {
				printOverdueTask(headings, body, highlightIndexI, highlightIndexJ, i, heading);
			} else if(heading.contains("Today")) {
				printTodayTask(headings, body, highlightIndexI, highlightIndexJ, i, heading);
			} else if(heading.contains("This")) {
				printThisWeekTask(headings, body, highlightIndexI, highlightIndexJ, i, heading);
			} else if(heading.contains("More")) {
				printOverAWeekTask(headings, body, highlightIndexI, highlightIndexJ, i, heading);
			} else if(heading.contains("Memo")) {
				printMemoTask(headings, body, highlightIndexI, highlightIndexJ, i, heading);
			} else if(heading.contains("Completed")) {
				printCompletedTask(headings, body, highlightIndexI, highlightIndexJ, i, heading);
			} else {
				printOtherTask(headings, body, highlightIndexI, highlightIndexJ, i, heading);
			}
			displaySystemMessage(MESSAGE_NL);
		}
	}

	//@author A0097706U
	//print overdue tasks
	private static void printOverdueTask(Queue<String> headings, Queue<Queue<String>> body, int highlightIndexI, int highlightIndexJ, int i, String heading) {
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
	}

	//@author A0097706U
	//print today's tasks
	private static void printTodayTask(Queue<String> headings, Queue<Queue<String>> body, int highlightIndexI, int highlightIndexJ, int i, String heading) {
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
	}

	//@author A0097706U
	//print this week's tasks
	private static void printThisWeekTask(Queue<String> headings, Queue<Queue<String>> body, int highlightIndexI, int highlightIndexJ, int i, String heading) {
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
	}

	//@author A0097706U
	//print over a week's tasks
	private static void printOverAWeekTask(Queue<String> headings, Queue<Queue<String>> body, int highlightIndexI, int highlightIndexJ, int i, String heading) {
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
	}

	//@author A0097706U
	//print memo tasks
	private static void printMemoTask(Queue<String> headings, Queue<Queue<String>> body, int highlightIndexI, int highlightIndexJ, int i, String heading) {
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
	}

	//@author A0097706U
	//print completed tasks
	private static void printCompletedTask(Queue<String> headings, Queue<Queue<String>> body, int highlightIndexI, int highlightIndexJ, int i, String heading) {
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
	}

	//@author A0097706U
	//any other kind of tasks will be printed here - unlikely to be activated
	private static void printOtherTask(Queue<String> headings, Queue<Queue<String>> body, int highlightIndexI, int highlightIndexJ, int i, String heading) {
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
	}
}