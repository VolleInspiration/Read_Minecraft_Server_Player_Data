package readReports;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.awt.*;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import java.util.zip.GZIPInputStream;

@SuppressWarnings("serial")
public class Main extends JPanel implements ActionListener
{
	static GraphicsConfiguration gc;
	static JTextField inputName;
	static JTextField einstellungen_logfiles_dir_name;
	//static JPopupMenu inputNameMenu;
	static JButton btn_suche, btn_refresh;
	static JButton btn_Einstellungen, einstellungen_btn_logfilespath_changed;
	static JButton btn_exportData, btn_exportData_OK;
	static JTextArea outputTextLog, outputTextDatabase;
	static JScrollPane scrollPaneLog, scrollPaneDatabase;
	static JLabel statusText, statusField, fullStatusText, fullstatusField;
	static JLabel einstellungen_logfiles_dir, einstellungen_money_db_label, einstellungen_vote_db_label;
	static JLabel einstellungen_uuid_login, einstellungen_logins, einstellungen_server_commands, einstellungen_chat;
	static JCheckBox einstellungen_money_chkbox, einstellungen_vote_chkbox;
	static JCheckBox einstellungen_uuid_login_chkbox, einstellungen_logins_chkbox, einstellungen_server_commands_chkbox, einstellungen_chat_chkbox;
	static Settings settings;
	static JFrame frame;
	static JFrame frame_einstellungen = new JFrame("Einstellungen");
	static JFrame frame_exportData_OK = new JFrame();
	static ImageIcon icon;
	static DBcheckData money, vote;
	
	static Preferences prefs = Preferences.userNodeForPackage(readReports.Main.class);
	static final String PREF_MONEY_SELECTED = "PREF_MONEY_SELECTED";
	static final String PREF_VOTE_SELECTED = "PREF_VOTE_SELECTED";
	static final String PREF_UUID_LOGIN_SELECTED = "PREF_UUID_LOGIN_SELECTED";
	static final String PREF_LOGIN_SELECTED = "PREF_LOGIN_SELECTED";
	static final String PREF_SERVER_COMMANDS_SELECTED = "PREF_SERVER_COMMANDS_SELECTED";
	static final String PREF_CHAT_SELECTED = "PREF_CHAT_SELECTED";
	static final String PREF_LOGFILES_DIR = "logfiles";
	static String money_value = "false", vote_value = "false", logfilesdir_value = "logfiles", uuid_login_value = "false",
			login_value = "false", server_commands_value = "false", chat_value = "false";
	
	static ArrayList<String> uuid_login = new ArrayList<String>();
	static ArrayList<String> logins = new ArrayList<String>();
	static ArrayList<String> servercommands = new ArrayList<String>();
	static ArrayList<String> outgoingChat = new ArrayList<String>();
	static ArrayList<String> money_user = new ArrayList<String>();
	static ArrayList<String> votes = new ArrayList<String>();
	
	static exportData epd = new exportData();
	
	public Main() 
	{
	    
	}
	
	public static void main(String s[]) 
	{
		System.gc();
		icon = new ImageIcon(Main.class.getClassLoader().getResource("server-icon.png"));
		
		frame = new JFrame(gc);
		frame.setIconImage(icon.getImage());
		inputName = new JTextField("Spielername", 10);
		inputName.setBounds(12, 12, 150, 22);
		btn_suche = new JButton("Laden...");
		btn_suche.setBounds(174, 12, 80, 22);
		statusText = new JLabel("Status:");
		statusText.setBounds(266, 12, 50, 22);
		statusField = new JLabel();
		statusField.setBounds(310, 12, 120, 22);
		statusField.setText("nicht gestartet");
		statusField.setForeground(Color.BLUE);
		fullStatusText = new JLabel("Gesamt-Status:");
		fullStatusText.setBounds(12, 655, 90, 22);
		fullstatusField = new JLabel();
		fullstatusField.setBounds(105, 655, 60, 22);
		fullstatusField.setText("---");
		fullstatusField.setForeground(Color.GRAY);
		btn_exportData = new JButton("Daten exportieren");
		btn_exportData.setBounds(605, 12, 150, 20);
		//btn_exportData_OK = new JButton("OK");
		btn_refresh = new JButton("Leeren");
		btn_refresh.setBounds(766, 12, 80, 20);
		btn_Einstellungen = new JButton("Einstellungen");
		btn_Einstellungen.setBounds(856, 12, 150, 20);
		outputTextLog = new JTextArea();
		outputTextLog.setEditable(false);
		scrollPaneLog = new JScrollPane(outputTextLog);
		scrollPaneLog.setBounds(12, 46, 995, 300);
		outputTextDatabase = new JTextArea();
		outputTextDatabase.setEditable(false);
		scrollPaneDatabase = new JScrollPane(outputTextDatabase);
		scrollPaneDatabase.setBounds(12, 356, 995, 300);
		
		frame.setTitle("MC-CrazyCraft.de - Userdata");
		frame.setSize(1024, 768);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.add(inputName);
	    frame.add(btn_suche);
	    frame.add(statusText);
	    frame.add(statusField);
	    frame.add(btn_exportData);
	    frame.add(btn_refresh);
	    frame.add(btn_Einstellungen);
	    frame.add(scrollPaneLog);
	    frame.add(scrollPaneDatabase);
	    frame.add(fullStatusText);
	    frame.add(fullstatusField);
	    frame.setLayout(null);
	    
	    frame.setVisible(true);
	    
	    //EINSTELLUNGEN
	    if(logfilesdir_value == "")
	    	logfilesdir_value = "logfiles";
	    einstellungen_logfiles_dir = new JLabel("Log-Dateien:");
	    einstellungen_logfiles_dir.setBounds(12, 12, 100, 22);
	    einstellungen_logfiles_dir_name = new JTextField(prefs.get(PREF_LOGFILES_DIR, logfilesdir_value));
	    einstellungen_logfiles_dir_name.setBounds(105, 12, 100, 22);
	    einstellungen_logfiles_dir_name.setEditable(false);
	    einstellungen_btn_logfilespath_changed = new JButton("Ändern");
	    einstellungen_btn_logfilespath_changed.setBounds(215, 12, 80, 22);
	    einstellungen_btn_logfilespath_changed.setEnabled(false);
	    
	    einstellungen_money_db_label = new JLabel("Geld abrufen:");
		einstellungen_money_db_label.setBounds(12, 40, 100, 22);
		einstellungen_money_chkbox = new JCheckBox();
		einstellungen_money_chkbox.setBounds(100, 40, 20, 22);
		
		einstellungen_uuid_login = new JLabel("UUID-Login:");
		einstellungen_uuid_login.setBounds(150, 40, 100, 22);
		einstellungen_uuid_login_chkbox = new JCheckBox();
		einstellungen_uuid_login_chkbox.setBounds(230, 40, 20, 22);
		
		einstellungen_logins = new JLabel("Logins:");
		einstellungen_logins.setBounds(150, 68, 100, 22);
		einstellungen_logins_chkbox = new JCheckBox();
		einstellungen_logins_chkbox.setBounds(230, 68, 20, 22);
		
		einstellungen_server_commands = new JLabel("Commands:");
		einstellungen_server_commands.setBounds(150, 96, 100, 22);
		einstellungen_server_commands_chkbox = new JCheckBox();
		einstellungen_server_commands_chkbox.setBounds(230, 96, 20, 22);
		
		einstellungen_chat = new JLabel("Chat:");
		einstellungen_chat.setBounds(150, 124, 100, 22);
		einstellungen_chat_chkbox = new JCheckBox();
		einstellungen_chat_chkbox.setBounds(230, 124, 20, 22);
		
		einstellungen_vote_db_label = new JLabel("Votes abrufen:");
		einstellungen_vote_db_label.setBounds(12, 68, 100, 22);
		einstellungen_vote_chkbox = new JCheckBox();
		einstellungen_vote_chkbox.setBounds(100, 68, 20, 22);
		
		settings = new Settings();
		settings.checkDirectoryAndFiles();
		try {
			if(prefs.get(PREF_MONEY_SELECTED, money_value).contains("false"))
				einstellungen_money_chkbox.setSelected(false);
			else if(prefs.get(PREF_MONEY_SELECTED, money_value).contains("true"))
				einstellungen_money_chkbox.setSelected(true);
			
			if(prefs.get(PREF_VOTE_SELECTED, vote_value).contains("false"))
				einstellungen_vote_chkbox.setSelected(false);
			else if(prefs.get(PREF_VOTE_SELECTED, vote_value).contains("true"))
				einstellungen_vote_chkbox.setSelected(true);
			
			if(prefs.get(PREF_UUID_LOGIN_SELECTED, uuid_login_value).contains("false"))
				einstellungen_uuid_login_chkbox.setSelected(false);
			else if(prefs.get(PREF_UUID_LOGIN_SELECTED, uuid_login_value).contains("true"))
				einstellungen_uuid_login_chkbox.setSelected(true);
			
			if(prefs.get(PREF_LOGIN_SELECTED, login_value).contains("false"))
				einstellungen_logins_chkbox.setSelected(false);
			else if(prefs.get(PREF_LOGIN_SELECTED, login_value).contains("true"))
				einstellungen_logins_chkbox.setSelected(true);
			
			if(prefs.get(PREF_SERVER_COMMANDS_SELECTED, server_commands_value).contains("false"))
				einstellungen_server_commands_chkbox.setSelected(false);
			else if(prefs.get(PREF_SERVER_COMMANDS_SELECTED, server_commands_value).contains("true"))
				einstellungen_server_commands_chkbox.setSelected(true);
			
			if(prefs.get(PREF_CHAT_SELECTED, chat_value).contains("false"))
				einstellungen_chat_chkbox.setSelected(false);
			else if(prefs.get(PREF_CHAT_SELECTED, chat_value).contains("true"))
				einstellungen_chat_chkbox.setSelected(true);
		}catch(Exception e) 
		{
			e.printStackTrace();
		}
		//
	    
	    inputName.addMouseListener(new MouseAdapter() 
	    {
	    	@Override
			public void mouseClicked(MouseEvent e) 
	    	{
				inputName.setText("");
			}
		});
	    
	    inputName.addKeyListener(new KeyListener() 
	    {	
			@Override
			public void keyPressed(KeyEvent e){}
			@Override
			public void keyReleased(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					suche();
			}
			@Override
			public void keyTyped(KeyEvent e) {}
		});
	    
	    btn_suche.addActionListener(new ActionListener() 
	    {	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				suche();
			}
		});
	    
	    btn_exportData.addActionListener(new ActionListener()
	    {	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(outputTextLog.getText().isEmpty() && outputTextDatabase.getText().isEmpty())
				{
					changeStatusStatus(statusField, "Kein Spieler geladen!", Color.RED);
				}
				else
				{
					epd.exportDatas(inputName.getText(), uuid_login, logins, servercommands, outgoingChat, money_user, votes);
					finish(frame_exportData_OK, btn_exportData_OK);
				}
				cleanup();
			}
		});
	    
	    btn_refresh.addActionListener(new ActionListener() 
	    {	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				outputTextLog.setText("");
				outputTextDatabase.setText("");
				cleanup();
			}
		});
	    
	    btn_Einstellungen.addActionListener(new ActionListener() 
	    {	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				frame.setVisible(false);
				einstellungen_open_window();	
			}
		});
	    
	    einstellungen_logfiles_dir_name.getDocument().addDocumentListener(new DocumentListener() 
	    {	
			@Override
			public void removeUpdate(DocumentEvent e) 
			{
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				einstellungen_btn_logfilespath_changed.setEnabled(true);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				
			}
		});
	    
	    einstellungen_btn_logfilespath_changed.addActionListener(new ActionListener() 
	    {	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				logfilesdir_value = einstellungen_logfiles_dir_name.getText();
				prefs.put(PREF_LOGFILES_DIR, logfilesdir_value);
				settings.checkDirectoryAndFiles();
				einstellungen_btn_logfilespath_changed.setEnabled(false);
			}
		});
	    
	    einstellungen_money_chkbox.addItemListener(new ItemListener() 
		{
			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					money_value = "true";
					prefs.put(PREF_MONEY_SELECTED, money_value);
					einstellungen_money_chkbox.setSelected(true);
				}
				else
				{
					money_value = "false";
					prefs.put(PREF_MONEY_SELECTED, money_value);
					einstellungen_money_chkbox.setSelected(false);
				}
			}
		});
	    
	    einstellungen_vote_chkbox.addItemListener(new ItemListener() 
	    {	
			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					vote_value = "true";
					prefs.put(PREF_VOTE_SELECTED, vote_value);
					einstellungen_vote_chkbox.setSelected(true);
				}
				else
				{
					vote_value = "false";
					prefs.put(PREF_VOTE_SELECTED, vote_value);
					einstellungen_vote_chkbox.setSelected(false);
				}
			}
		});
	    //PREF_UUID_LOGIN_SELECTED
	    einstellungen_uuid_login_chkbox.addItemListener(new ItemListener() 
	    {	
			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					uuid_login_value = "true";
					prefs.put(PREF_UUID_LOGIN_SELECTED, uuid_login_value);
					einstellungen_uuid_login_chkbox.setSelected(true);
				}
				else
				{
					uuid_login_value = "false";
					prefs.put(PREF_UUID_LOGIN_SELECTED, uuid_login_value);
					einstellungen_uuid_login_chkbox.setSelected(false);
				}
			}
		});
	    //PREF_LOGIN_SELECTED
	    einstellungen_logins_chkbox.addItemListener(new ItemListener()
	    {	
			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					login_value = "true";
					prefs.put(PREF_LOGIN_SELECTED, login_value);
					einstellungen_logins_chkbox.setSelected(true);
				}
				else
				{
					login_value = "false";
					prefs.put(PREF_LOGIN_SELECTED, login_value);
					einstellungen_logins_chkbox.setSelected(false);
				}
			}
		});
	    //PREF_SERVER_COMMANDS_SELECTED
	    einstellungen_server_commands_chkbox.addItemListener(new ItemListener()
	    {
			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					server_commands_value = "true";
					prefs.put(PREF_SERVER_COMMANDS_SELECTED, server_commands_value);
					einstellungen_server_commands_chkbox.setSelected(true);
				}
				else
				{
					server_commands_value = "false";
					prefs.put(PREF_SERVER_COMMANDS_SELECTED, server_commands_value);
					einstellungen_server_commands_chkbox.setSelected(false);
				}
			}
		});
	    //PREF_CHAT_SELECTED
	    einstellungen_chat_chkbox.addItemListener(new ItemListener()
	    {	
			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					chat_value = "true";
					prefs.put(PREF_CHAT_SELECTED, chat_value);
					einstellungen_chat_chkbox.setSelected(true);
				}
				else
				{
					chat_value = "false";
					prefs.put(PREF_CHAT_SELECTED, chat_value);
					einstellungen_chat_chkbox.setSelected(false);
				}
			}
		});
	    frame_einstellungen.addWindowListener(new WindowAdapter() 
	    {
	    	@Override
	    	public void windowClosing(WindowEvent e)
	    	{
	    		frame.setVisible(true);
	    	}
		});
	}
	
	private static void finish(JFrame frame, JButton btn)
	{
		frame.setLayout(null);
		JLabel lbl = new JLabel();
		btn = new JButton();
		lbl.setText("Die Spielerdaten wurden erfolgreich exportiert!");
		lbl.setBounds(80, 20, 300, 25);
		btn.setText("OK"); // TODO
		btn.setBounds(200, 65, 40, 40);
		
		frame.setTitle("Spielerdaten exportiert!");
		frame.add(lbl);
		frame.add(btn);
		frame.setSize(450, 150);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		System.out.println(btn.getText());
		
		btn.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
	}
	
	private static void changeStatusStatus(JLabel label, String text, Color color)
	{
		label.setText(text);
		label.setForeground(color);
		label.paintImmediately(label.getVisibleRect());
	}
	
	public static void einstellungen_open_window()
	{
		System.out.println("öffne Einstellungen...");
		frame_einstellungen.setIconImage(icon.getImage());
		frame_einstellungen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame_einstellungen.setTitle("Einstellungen");
		frame_einstellungen.setSize(600, 400);
		frame_einstellungen.setLocationRelativeTo(null);
		frame_einstellungen.setResizable(false);
		frame_einstellungen.setAlwaysOnTop(true);
		
		frame_einstellungen.add(einstellungen_logfiles_dir);
		frame_einstellungen.add(einstellungen_logfiles_dir_name);
		frame_einstellungen.add(einstellungen_btn_logfilespath_changed);
		frame_einstellungen.add(einstellungen_money_db_label);
		frame_einstellungen.add(einstellungen_money_chkbox);
		frame_einstellungen.add(einstellungen_vote_db_label);
		frame_einstellungen.add(einstellungen_vote_chkbox);
		frame_einstellungen.add(einstellungen_uuid_login);
		frame_einstellungen.add(einstellungen_uuid_login_chkbox);
		frame_einstellungen.add(einstellungen_logins);
		frame_einstellungen.add(einstellungen_logins_chkbox);
		frame_einstellungen.add(einstellungen_server_commands);
		frame_einstellungen.add(einstellungen_server_commands_chkbox);
		frame_einstellungen.add(einstellungen_chat);
		frame_einstellungen.add(einstellungen_chat_chkbox);
		
		frame_einstellungen.setLayout(null);
		frame_einstellungen.setVisible(true);
	}
	
	public static void suche()
	{
		String playerName = inputName.getText();
		if(playerName.length() == 0 || playerName.contains("Spielername")) 
		{
			fullstatusField.setText("Spielername leer");
			fullstatusField.setForeground(Color.orange);
			return;
		}
		
		changeStatusStatus(statusField, "Logfiles", Color.GREEN);
		findAllData(Settings.pathLogfilesFolder, inputName.getText());
		changeStatusStatus(statusField, "nicht gestartet", Color.BLUE);
		
		fullstatusField.setText("OK");
		fullstatusField.setForeground(Color.GREEN);
		
		if(einstellungen_money_chkbox.isSelected())
		{
			money = new DBcheckData();
			money.loadData(inputName.getText(), "money");
		}
		if(einstellungen_vote_chkbox.isSelected())
		{
			vote = new DBcheckData();
			vote.loadData(inputName.getText(), "vote");
		}
	}
	
	static String filteredLogFileString = "";
	public static String filterLogfileStrings(String log, String username)
	{
		if(log.contains("UUID of player " + username + " is") && einstellungen_uuid_login_chkbox.isSelected())
		{
			filteredLogFileString = "UUID:" + log + "\n";
			uuid_login.add(filteredLogFileString);
		}
		else if(log.contains(username + "[/") && log.contains("logged in with entity id") && einstellungen_logins_chkbox.isSelected())
		{
			filteredLogFileString = "LOG IN:" + log + "\n";
			logins.add(filteredLogFileString);
		}
		else if(log.contains(username + " issued server command:") && einstellungen_server_commands_chkbox.isSelected())
		{
			filteredLogFileString = "SERVER COMMAND:" + log + "\n";
			servercommands.add(filteredLogFileString);
		}
		else if(log.contains("[Async Chat Thread -") && log.contains(username + " |") && einstellungen_chat_chkbox.isSelected())
		{
			filteredLogFileString = "CHAT:" + log + "\n";
			outgoingChat.add(filteredLogFileString);
		}
		return filteredLogFileString;
	}
	
	public static String cutFileNames(String filename)
	{
		String[] cuttedFileName = filename.split("\\.");
		return cuttedFileName[0];
	}
	
	public static void findAllData(File directory, String username)
	{
		ArrayList<File> server = new ArrayList<>();
		server.add(new java.io.File("logfiles/Bungee"));
		server.add(new java.io.File("logfiles/Hub"));
		server.add(new java.io.File("logfiles/CB1"));
		server.add(new java.io.File("logfiles/Testserver"));
				
		if( (server.get(0).listFiles().length == 0) &&
				(server.get(1).listFiles().length == 0) &&
				(server.get(2).listFiles().length == 0))
		{
			outputTextLog.append("KEINE DATEIEN GEFUNDEN!\nBitte überprüfe den Pfad in den Einstellungen. Standard: logfiles/\nUnd starte das Programm neu!");
		}
		else
		{
			for(int i = 0; i < server.size(); i++)
			{
				for(File fileNames : server.get(i).listFiles()) 
				{
					if(!fileNames.isDirectory())
					{
						if(fileNames.getName().endsWith(".gz"))
						{
							try {
								GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(server.get(i).toString() +"/"+ fileNames.getName()));
								BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
								String line;
								while((line = br.readLine()) != null)
								{
									if(line.contains(username))
									{
										String newline = new String(line.getBytes("ISO-8859-1"), "UTF-8");
										String value = filterLogfileStrings(newline, username);
										if(value != "")
											outputTextLog.append("SERVER:" + server.get(i).toString() + ",FILEDATE:" + cutFileNames(fileNames.getName()) + ",\n" + value );
										//outputTextLog.append(newline + "\n");
									}
								}
								gzip.close();
								br.close();
								line = null;
							}catch (Exception e) {
								e.printStackTrace();
							}
						}
						else if (fileNames.getName().endsWith(".log"))
						{
							BufferedReader reader;
							try {
								reader = new BufferedReader(new FileReader( server.get(i).toString() +"/"+ fileNames.getName()));
								String line;
								while((line = reader.readLine()) != null)
								{
									if(line.contains(username))
									{
										String newline = new String(line.getBytes("ISO-8859-1"), "UTF-8");
										String value = filterLogfileStrings(newline, username);
										if(value != "")
											outputTextLog.append("SERVER:" + server.get(i).toString() + ",FILEDATE:" + cutFileNames(fileNames.getName()) + ",\n" + value );
										//outputTextLog.append(newline + "\n");
									}
								}
								reader.close();
								line = null;
							}catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			
			System.out.println("USERNAME: " + username);
		}
	}
	
	public static void cleanup()
	{
		uuid_login.clear();
		logins.clear();
		servercommands.clear();
		outgoingChat.clear();
		money_user.clear();
		votes.clear();
	}
	
	public void windowClosing(WindowEvent e)
	{
		/*
		try {
			prefs.clear();
		} catch (BackingStoreException e1) {
			e1.printStackTrace();
		}
		*/
		System.exit(0);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {

		
	}
}













/*for(File fileNames : directory.listFiles())
{
	if(!fileNames.isDirectory())
	{
		if(fileNames.getName().endsWith(".gz"))
		{
			try {
				GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(new java.io.File( prefs.get(PREF_LOGFILES_DIR, logfilesdir_value) ) + "/" + fileNames.getName()));
				BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
				String line;
				while((line = br.readLine()) != null)
				{
					if(line.contains(username))
					{
						String newline = new String(line.getBytes("ISO-8859-1"), "UTF-8");
						String value = filterLogfileStrings(newline, username);
						if(value != "")
							outputTextLog.append( "FILEDATE: " + cutFileNames(fileNames.getName()) + "\n " + filterLogfileStrings(newline, username) );
						//outputTextLog.append(newline + "\n");
					}
				}
				gzip.close();
				br.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (fileNames.getName().endsWith(".log"))
		{
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(new java.io.File( prefs.get(PREF_LOGFILES_DIR, logfilesdir_value) ) + "/" + fileNames.getName()));
				String line;
				while((line = reader.readLine()) != null)
				{
					if(line.contains(username))
					{
						String newline = new String(line.getBytes("ISO-8859-1"), "UTF-8");
						String value = filterLogfileStrings(newline, username);
						if(value != "")
							outputTextLog.append( "FILEDATE: " + cutFileNames(fileNames.getName()) + "\n " + filterLogfileStrings(newline, username) );
						//outputTextLog.append(newline + "\n");
					}
				}
				reader.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	System.out.println("Ordner");
}*/
