/**
 * @author Teck Sheng (Dex)
 * This GUI class handles the display of of system to user
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import java.awt.SystemColor;

import javax.swing.JPanel;

import java.awt.ComponentOrientation;
import java.awt.Cursor;

import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SapphireManagerGUI {
	private final static String MESSAGE_CURRENTLY_EDITING = "Currently Editing:";
	private final static String MESSAGE_HELP = "Enter F1 for a list of commands.";
	private final static String MESSAGE_NO_TASK_TO_DISPLAY_TODAY = "You have no tasks due today.\n";
	private final static String MESSAGE_WELCOME = "Welcome to Sapphire Manager!";
	private final static String MESSAGE_TASKS_FOUND = "Existing tasks found: ";
	private final static String MESSAGE_TODAY_TASK_TITLE = "Today's tasks:\n";
	private final static String MESSAGE_WRONG_INPUT = "Wrong input.";

	private final static String PROMPT_FOR_NUMBER = "Enter a number: ";
	private final static String PROMPT_FOR_EDITS = "Enter your edits: ";

	private final static String SPLIT_LINE = "------------------------------------------";

	private static ArrayList<Task> todaysTasks;
	private static CommandExecutor myExecutor;
	private static SapphireManagerGUI guiWindow;
	private static JTextPane displayBox;

	private JFrame guiFrame;
	private JLabel helpTip;
	private JScrollPane scrollPane;
	private JTextField inputBox;
	private Toolkit toolkit;
	private JLabel logoLabel;
	private JPanel helpTipPanel;
	private JPanel logoPanel;
	private JPanel inputBoxPanel;

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
		initializeLogoInPanel();
		initializeDisplayBoxInScrollPane();
		initializeHelpTipInPanel();
		initializeInputBoxInPanel();
		contentPaneDisplay();	
	}

	private void start() {
		displayWelcomeMessage();
		guiFrameListener();
		textFieldListener();
		displayBoxListener();
		helpTipListener();
	}

	private void initializeSapphireManager() {
		guiFrame = new JFrame();
		guiFrame.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 14));
		guiFrame.getContentPane().setBackground(new Color(0x231F20));
		guiFrame.setTitle("Sapphire Manager");
		guiFrame.setBounds(100, 100, 400, 600);
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setResizable(false);
		toolkit = Toolkit.getDefaultToolkit();
		int x = toolkit.getScreenSize().width-guiFrame.getWidth();
		int y = toolkit.getScreenSize().height-guiFrame.getHeight()-40;
		guiFrame.setLocation(x, y);
	}

	private void initializeDisplayBoxInScrollPane() {
		displayBox = new JTextPane();
		displayBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				inputBox.requestFocus();
			}
		});
		displayBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		displayBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		scrollPane = new JScrollPane(displayBox);
		scrollPane.setWheelScrollingEnabled(false);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(380, 450));
		scrollPane.setBorder(null);

		displayBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		displayBox.setPreferredSize(new Dimension(380, 440));
		displayBox.setMargin(new Insets(20, 20, 20, 20));
		displayBox.setBackground(new Color(0x231F20));
		displayBox.setBorder(null);
	}

	private void initializeInputBoxInPanel() {
		inputBoxPanel = new JPanel();
		inputBox = new JTextField();
		inputBoxPanel.add(inputBox);

		inputBox.setBackground(SystemColor.window);
		inputBox.setHorizontalAlignment(SwingConstants.LEFT);
		inputBox.requestFocus();
		inputBox.setPreferredSize(new Dimension(380, 25));
		inputBoxPanel.setPreferredSize(new Dimension(400, 25));
		inputBox.setBorder(null);
		inputBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		inputBoxPanel.setBorder(null);
		inputBoxPanel.setBackground(Color.WHITE);
	}

	private void initializeHelpTipInPanel() {
		helpTipPanel = new JPanel();
		helpTip = new JLabel();
		helpTipPanel.add(helpTip);

		helpTipPanel.setPreferredSize(new Dimension(380, 30));
		helpTipPanel.setBackground(Color.WHITE);
		helpTip.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		helpTip.setHorizontalAlignment(SwingConstants.LEFT);
		helpTip.setPreferredSize(new Dimension(380, 30));
		displayHelpoText(getTodayDate());
	}

	private void initializeLogoInPanel() {
		logoPanel = new JPanel();
		logoLabel = new JLabel("");
		ImageIcon icon = new ImageIcon("img/logo-trans.png");
		//ImageIcon icon = new ImageIcon("http://i58.tinypic.com/2edpqa0.jpg");
		/*Image image = null;
		try {
			URL url = new URL("https://onedrive.live.com/redir?resid=FDAC10ED2A7F277D!11000&authkey=!AMXEMlxnhTmgGVk&v=3&ithint=photo%2c.png");
			image = ImageIO.read(url);
		} catch (Exception e) {
			System.out.println("No image");
		}*/
		//logoLabel = new JLabel(new ImageIcon(image));
		logoPanel.add(logoLabel);
		logoPanel.setBackground(new Color(0x231F20));
		logoLabel.setIcon(icon);

	}

	private void contentPaneDisplay() {
		guiFrame.getContentPane().setLayout(new BoxLayout(guiFrame.getContentPane(), BoxLayout.Y_AXIS));		
		guiFrame.getContentPane().add(logoPanel, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(scrollPane, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(helpTipPanel, Component.CENTER_ALIGNMENT);
		guiFrame.getContentPane().add(inputBoxPanel);
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
						System.out.println(phase);
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
							}
							break;
						}
						inputBox.setText("");
					}
				} else if(e.getKeyCode() == KeyEvent.VK_F1) {
					displayHelp();
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
				}
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
	}

	private void helpTipListener() {
		inputBox.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				String userInput = inputBox.getText().toLowerCase();

				if(userInput.equals("")) {
					helpTip.setText(MESSAGE_HELP);
				} else if(userInput.startsWith("a")) { //add
					helpTip.setText(inputAdd(userInput));
				} else if(userInput.startsWith("c")) { //clear
					helpTip.setText(inputClear(userInput));
				} else if(userInput.startsWith("d")) { //delete or display
					helpTip.setText(inputD(userInput));
				} else if(userInput.startsWith("e")) { //edit
					helpTip.setText(inputEdit(userInput));
				} else if(userInput.startsWith("s")) { //search
					helpTip.setText(inputSearch(userInput));
				} else if(userInput.startsWith("u")) { //undo
					helpTip.setText(inputUndo(userInput));
				} else {
					helpTip.setText(MESSAGE_HELP);
				}

				//splitAndReadInput(userInput);

				/*
				//String addPattern1 = "[(aA)|(aAdD)|(aAdDdD)]"; //indicates to find the word "add"
				String addPattern1 = "[a][d][d]";
				//String addPattern2 = "^[aA][dD]"; //indicates to find the word "add"
				//String addPattern3 = "^[aA]"; //indicates to find the word "add"
				Pattern pattern = Pattern.compile(addPattern1);
				Matcher matcher = pattern.matcher(userInput);
				//if(Pattern.compile(addPattern1).matcher(userInput) != null) {
				if(matcher.find()) {
					helpTip.setText("add [task name] [options]");
					if(userInput.endsWith("/")) {
						helpTip.setText("LOL");
					}
					//System.out.println(matcher.group(0));
				} else {
					helpTip.setText("Match not found");
					System.out.println("Match not found");
				}*/

				/*
				if(userInput.equals("a") || userInput.equals("ad") || userInput.startsWith("add")) {
					helpTip.setText("add [task name] [options]");
					System.out.println("A: "+userInput);
					if(userInput.endsWith("/")) {
						//int firstSlash = userInput.indexOf("/");
						//helpTip.setText(userInput.substring(0, firstSlash)+"/on [date]");
						helpTip.setText("/on [date] /from [time] to [time] /loc [location name]");
						System.out.println("B: "+userInput);
					}

					//if(userInput.endsWith("/on") || userInput.endsWith("/o")) {
					if(userInput.endsWith("/o") || userInput.endsWith("/on")) {
						//helpTip.setText("/on [date] /from [time] to [time] /loc [location name]");
						helpTip.setText("options: /on [date]: 6 digit");
					} 

					if(userInput.endsWith("/f") || userInput.endsWith("/fr") || userInput.endsWith("/fro") || userInput.endsWith("/from")) {
						helpTip.setText("options: /from [time] to [time]");
					}
				} else if(userInput.equals("c") || userInput.equals("cl") || userInput.equals("cle") || userInput.equals("clea") || userInput.startsWith("clear")) {
					helpTip.setText("clear");
					System.out.println("A: "+userInput);
				} else {
					if(!userInput.equals("")) {
						helpTip.setText(MESSAGE_WRONG_INPUT+" "+MESSAGE_HELP);
					}
				}

				//clear all tasks
				if(userInput.equals("c") || userInput.equals("cl") || userInput.equals("cle") || userInput.equals("clea") || userInput.startsWith("clear")) {
					helpTip.setText("clear");
					System.out.println("A: "+userInput);
				} else {
					if(!userInput.equals("")) {
						helpTip.setText(MESSAGE_WRONG_INPUT+" "+MESSAGE_HELP);
					}
				}

				if(userInput.equals("u") || userInput.equals("un") || userInput.equals("und") || userInput.startsWith("undo")) {
					helpTip.setText("undo");
					System.out.println("A: "+userInput);
				} else {
					if(!userInput.equals("")) {
						helpTip.setText(MESSAGE_WRONG_INPUT+" "+MESSAGE_HELP);
					}
				}

				if(userInput.equals("e") || userInput.equals("ex") || userInput.equals("exi") || userInput.startsWith("exit")) {
					helpTip.setText("exit");
					System.out.println("A: "+userInput);
				} else {
					if(!userInput.equals("")) {
						helpTip.setText(MESSAGE_WRONG_INPUT+" "+MESSAGE_HELP);
					}
				}

				if(userInput.equals("q") || userInput.equals("qu") || userInput.equals("qui") || userInput.startsWith("quit")) {
					helpTip.setText("quit");
					System.out.println("A: "+userInput);
				} else {
					if(!userInput.equals("")) {
						helpTip.setText(MESSAGE_WRONG_INPUT+" "+MESSAGE_HELP);
					}
				}*/
			}
		});
	}

	private String inputAdd(String userInput) {
		if(userInput.equals("a") || userInput.equals("ad") || userInput.startsWith("add")) {
			if(userInput.contains("/")) {
				return inputOptions("add", userInput);
			}
			return "add [task name] [options]";
		}
		return MESSAGE_HELP;
	}

	private String inputOptions(String action, String userInput) {
		if(action.equals("add")) {
			Pattern p1 = Pattern.compile("add(.+?)[/\\/w]");
			Matcher m1 = p1.matcher(userInput);

			if(m1.find() && m1.group(1).trim().equals("")) {
				return "Please enter a task name";
			} else {
				if(userInput.endsWith("/")) {
					return "/on [date] /from [time] to [time] /loc [name of location]";
				} else if((userInput.endsWith("/o") || userInput.endsWith("/on"))) {
					Pattern p2 = Pattern.compile("^(/on)\\s*\\d{0,6}");
					//Pattern p2 = Pattern.compile("(/on|/o)[\\s]");
					Matcher m2 = p2.matcher(userInput);
					int count = 0;
					/*while(m2.find()) {
						System.out.println("GG "+count+"."+m2.group(count++)+"."+m2.find());
					}*/
					System.out.println("m.find() = "+m2.find());
					if(m2.find()) {
						System.out.println("LOL");
						return "/on [date] <-- date to be in DDMMYY format";
					} else {
						return "type properly leh!";
					}
					/*
					p = Pattern.compile("/on\\s[\\d]{0,3}");
					//p = Pattern.compile("(/on)[\\s](.)*(\\d)(.)*");
					//p = Pattern.compile("(/on)[\\s+]([0-9]{0,6})");
					m = p.matcher(userInput);
					//System.out.println("A: "+p.toString());
					count = 0;
					while(m.find()) {
						System.out.println("F:"+count+"."+m.group(count++)+"."+m.find());
					} */
				} else if(userInput.endsWith("/f") || userInput.endsWith("/fr") || userInput.endsWith("/fro") || userInput.endsWith("/from")) {
					return "/from [time] to [time] <-- time to be in HHMM (24-hours) format";
				} else if(userInput.endsWith("/l") || userInput.endsWith("/lo") || userInput.endsWith("/loc")) {
					return "/loc [location name]";
				}
			}

			/*
			if(userInput.endsWith("/") && m.find()) {
				System.out.println("Found: "+m.group(1).trim()+"|");
				if(m.group(1).trim().equals("")) {
					return "Please enter a task name";
				} else {
					return "/on [date] /from [time] to [time] /loc [name of location]";
				}
			}*/
		} else {

		}
		return "add [task name] [options]";
	}

	private String inputClear(String userInput) {
		if(userInput.equals("c") || userInput.equals("cl") || userInput.equals("cle") || userInput.equals("clea") || userInput.startsWith("clear")) {
			return "clear";
		}
		return MESSAGE_HELP;
	}

	private String inputD(String userInput) {
		if(userInput.equals("d")) {
			return "delete [task name] | display [past|today|future]";
		} else if(userInput.startsWith("di")) {
			return inputDisplay(userInput);
		} else if(userInput.startsWith("de")) {
			return inputDelete(userInput);
		} else {
			return MESSAGE_HELP;
		}
	}

	private String inputDisplay(String userInput) {
		if(userInput.equals("d") || userInput.equals("di") || userInput.equals("dis") || userInput.equals("disp") || userInput.equals("displ") || userInput.equals("displa") || userInput.startsWith("display")) {
			return "display [past|today|future]";
		}
		return MESSAGE_HELP;
	}

	private String inputDelete(String userInput) {
		if(userInput.equals("d") || userInput.equals("de") || userInput.equals("del") || userInput.equals("dele") || userInput.equals("delet") || userInput.startsWith("delete")) {
			return "delete [task name]";
		}
		return MESSAGE_HELP;
	}

	private String inputUndo(String userInput) {
		if(userInput.equals("u") || userInput.equals("un") || userInput.equals("und") || userInput.startsWith("undo")) {
			return "undo";
		}
		return MESSAGE_HELP;
	}

	private String inputSearch(String userInput) {
		if(userInput.equals("s") || userInput.equals("se") || userInput.equals("sea") || userInput.equals("sear")|| userInput.equals("searc") || userInput.startsWith("search")) {
			return "search [task name | date]";
		}
		return MESSAGE_HELP;
	}

	private String inputEdit(String userInput) {
		if(userInput.equals("e") || userInput.equals("ed") || userInput.equals("edi") || userInput.startsWith("edit")) {
			return "edit [task name]";
		}
		return MESSAGE_HELP;
	}

	/*
	private String splitAndReadInput(String userInput) {
		String[] splited = userInput.split("(?!^)");
		System.out.println(SPLIT_LINE);
		for(int i=0; i<splited.length; i++) {
			System.out.println(splited[i]);
		}
		System.out.println(SPLIT_LINE);
		return "";
	}*/

	private void updateScrollBar() {
		displayBox.setCaretPosition(displayBox.getDocument().getLength());
		DefaultCaret caret = (DefaultCaret)displayBox.getCaret();  
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);  
	}

	private static void clearDisplayBox() {
		displayBox.setText("");
	}

	public static void displayNormalText(String message) {
		appendToPane(message, Color.WHITE);
	}

	public static void displaySystemText(String message) {
		appendToPane(message, new Color(0xff6c00));
	}

	public void displayHelpoText(String message) {
		helpTip.setText(MESSAGE_HELP);
	}

	public static void displayHightlightText(String message) {
		appendToPane(message, Color.CYAN);
	}

	private static void appendToPane(String message, Color color) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,  StyleConstants.Foreground, color);

		aset = sc.addAttribute(aset,  StyleConstants.FontFamily,  "Segoe UI");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		int length = displayBox.getDocument().getLength();
		displayBox.setCaretPosition(length);
		displayBox.setCharacterAttributes(aset, false);
		displayBox.replaceSelection(message+"\n");
	}

	public void displayWelcomeMessage() {
		displaySystemText(MESSAGE_WELCOME+
				"\nToday's Date: "+
				getTodayDate()+"\n"+
				SPLIT_LINE);
	}

	public void displayHelp() {
		displaySystemText("Available Commands");
		displaySystemText(SPLIT_LINE);
		displaySystemText("1. add [task name] [options]");
		displaySystemText("2. delete [task name (in part or in full)]");
		displaySystemText("3. edit [task name (in part or in full)]");
		displaySystemText("4. display [all | today | past | future]");
		displaySystemText("5. undo");
		displaySystemText("6. search [task name | date]");
		displaySystemText("7. clear");
		displaySystemText("8. exit | quit");
		displaySystemText(SPLIT_LINE);
		displaySystemText("- Options:");
		displaySystemText("   Date: '/on [date]'");
		displaySystemText("   Time: '/from [time] to [time]' or \n            '/at [time]'");
		displaySystemText("   Location: '/loc [location name]'");
		displaySystemText("   Mark as done: '/mark [done | undone]'");
		//displaySystemText("   Category: '/c [one-word-name]'");
		//displaySystemText("   Reminder: '/r [time]'");
		displaySystemText("   *Time: 4-digit 24 hours [1159]: 11.59am; [2359]: 11.59pm");
		displaySystemText("   *Date: 6-digit [DDMMYYYY]");
	}

	public void displaySingleTask(Task taskToDisplay) {	
		String taskDetails = taskToDisplay.getAllTaskDetails();
		displayNormalText(taskDetails);
	}

	public void displayTasksGivenList(ArrayList<Task> taskList) {
		if(taskList.size() <= 0) {
			displaySystemText("No tasks available.");
		} else {
			displaySystemText("Tasks available:");
			int count = 1;
			for(Task task: taskList) {
				displayNormalText(count++ + ". ");
				String taskDetails = task.getAllTaskDetails();
				displayNormalText(taskDetails);
			}
		}
	}

	public void displayExistingTasksFound(ArrayList<Task> taskList) {
		if (taskList != null && taskList.size() != 0) {
			displaySystemText(MESSAGE_TASKS_FOUND);
			displayTasksGivenList(taskList);
			displaySystemText(PROMPT_FOR_NUMBER);
		}
	}

	public void displayCurrentlyEditingSequence(Task taskBeingEdited) {
		displaySystemText(MESSAGE_CURRENTLY_EDITING);
		displaySingleTask(taskBeingEdited);
		displaySystemText(PROMPT_FOR_EDITS);
	}

	private String getTodayDate() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
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
		return readUserInput();
	}

	public String readUserEdits() {
		return readUserInput();
	}

	public int readUserChoice() {
		String userInput = readUserInput();
		int userChoice = Integer.parseInt(userInput);
		return userChoice;
	}

}