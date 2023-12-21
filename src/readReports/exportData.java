package readReports;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class exportData 
{
	public void exportDatas(String username, ArrayList<String> uuid_login, ArrayList<String> logins, ArrayList<String> servercommands, 
			ArrayList<String> outgoingChat, ArrayList<String> money_user, ArrayList<String> votes)
	{
		System.out.println("starting export data...");
		
		if(readReports.Main.einstellungen_uuid_login_chkbox.isSelected())
			createUUIDLogins(username, uuid_login);
		if(readReports.Main.einstellungen_logins_chkbox.isSelected())
			createLogins(username, logins);
		if(readReports.Main.einstellungen_server_commands_chkbox.isSelected())
			createCommands(username, servercommands);
		if(readReports.Main.einstellungen_chat_chkbox.isSelected())
			createChat(username, outgoingChat);
		if(readReports.Main.einstellungen_money_chkbox.isSelected())
			createMoney(username, money_user);
		if(readReports.Main.einstellungen_vote_chkbox.isSelected())
			createVotes(username, votes);
		
		compressGzipFile(username);
		cleanup();
	}
	
	public void createUUIDLogins(String username, ArrayList<String> uuid_login)
	{
		String res = "";
		for(int i = 0; i < uuid_login.size(); i++)
		{
			//if(Pattern.matches("(UUID:)\\[[0-9:]*\\]\\s\\[(LoginProcessingThread\\/INFO)\\]:\\s(UUID of player)\\s[\\/]*[\\sa-zA-Z0-9[:punct:]]*", uuid_login.get(i)))
				res += uuid_login.get(i);
		}
		createCSVFile(username + "_uuid_login", res);
	}
	
	public void createLogins(String username, ArrayList<String> logins)
	{
		String res = "";
		for(int i = 0; i < logins.size(); i++)
		{
			if(Pattern.matches("(LOG IN:)\\[[0-9:]*\\]\\s\\[(Server thread\\/INFO)\\]:\\s\\w*\\[\\/[0-9.:]*\\]\\s(logged in with entity id\\s)[0-9]*\\sat\\s[\\[\\]\\(\\)0-9a-zA-Z.\\-,\\s]*", logins.get(i)))
				res += logins.get(i);
		}
		createCSVFile(username + "_logins", res);
	}
	
	public void createCommands(String username, ArrayList<String> servercommands)
	{
		String res = "";
		for(int i = 0; i < servercommands.size(); i++)
		{
			if(Pattern.matches("(SERVER COMMAND:)\\[[0-9:]*\\]\\s\\[(Server thread\\/INFO)\\]:\\s\\w*\\s(issued server command: )[\\/]*[\\sa-zA-Z0-9[:punct:]]*", servercommands.get(i)))
				res += servercommands.get(i);
		}
		createCSVFile(username + "_commands", res);
	}
	
	public void createChat(String username, ArrayList<String> outgoingChat)
	{
		String res = "";
		for(int i = 0; i < outgoingChat.size(); i++)
		{
			res += outgoingChat.get(i);
			if(i < outgoingChat.size())
				res += ",";
			if(Pattern.matches("(SERVER:logfiles)\\\\\\w[0-9A-Za-z]*,(FILEDATE:)[A-Za-z0-9-]*,[CHAT:]*\\[[0-9:]*\\]\\s\\[[a-zA-Z\\s-#0-9\\/]*\\]:[\\s\\[\\]A-Za-z:\\|]*", outgoingChat.get(i)) )//TODO
				res += "\n";
		}
		createCSVFile(username + "_chat", res);
	}

	public void createMoney(String username, ArrayList<String> money_user)
	{
		try {
			money_user.get(0);
		}catch (Exception e) {
			money_user.add(0, "0");
		}
		createCSVFile(username + "_money", money_user.get(0));
	}
	
	public void createVotes(String username, ArrayList<String> votes)
	{
		String res = "";
		for(int i = 0; i < votes.size() ; i++) 
		{
			if(i > 0 && Pattern.matches("[0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}", votes.get(i)))
				res += "\n";
			else if(i < votes.size() && i > 0)
				res += ",";
			res += votes.get(i);
		}
		createCSVFile(username + "_votes", res);
	}
	
	private String dataSavingString()
	{
		
		return "---------------------- Datenspeicherung ------------------------\n"
				+ "- Grund: Erkennen von Fehlern, welche vom Spieler oder während seiner Spielzeit auftraten\n"
				+ "- Löschung der Daten: voraussichtlich innerhalb 30 Tagen\n"
				+ "---------------------- Datenspeicherung ------------------------\n";
	}
	
	public void createCSVFile(String filename, String inputstring)
	{
		try
		{
			File file = new File("output/" + filename + ".csv"); //TODO
			if(file.createNewFile())
			{
				System.out.println("Datei erstellt: " + file.getName());
				writeInFile(file, inputstring);
			}
		}catch (Exception e) {}
	}
	
	public void writeInFile(File file, String inputstring)
	{

		FileWriter writer;
		try {
			writer = new FileWriter("output/" + file.getName()); //TODO
			writer.write(dataSavingString());
			writer.write(inputstring);
			writer.close();
		} catch (IOException e) {
		}
	}
	
	private void compressGzipFile(String username)
	{
		String DIRECTORY = "output";
		
		TarArchiveOutputStream tarOs = null;
		try {
			//File src = new File(DIRECTORY);
			FileOutputStream fos = new FileOutputStream("outputGZ/" + username + ".tar.gz");//src.getAbsolutePath().concat(".tar.gz"));
			
			GZIPOutputStream gos = new GZIPOutputStream(new BufferedOutputStream(fos));
			
			tarOs = new TarArchiveOutputStream(gos);
		    addFilesToTarGZ(DIRECTORY, "", tarOs); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		   try {
		       tarOs.close();
		      } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		      }
		  }
	}
	
	public void addFilesToTarGZ(String filePath, String parent, TarArchiveOutputStream tarArchive) throws IOException 
	{
		File file = new File(filePath);
		// Create entry name relative to parent file path 
		System.out.println(filePath);
		String entryName = parent + file.getName();
		// add tar ArchiveEntry
		
		tarArchive.putArchiveEntry(new TarArchiveEntry(file, entryName));
		if(file.isFile())
		{
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			// Write file content to archive
			IOUtils.copy(bis, tarArchive);
			tarArchive.closeArchiveEntry();
			bis.close();
		}
		else if(file.isDirectory())
		{
			// no need to copy any content since it is
			// a directory, just close the outputstream
			tarArchive.closeArchiveEntry();
			// 	for files in the directories
			for(File f : file.listFiles())
			{        
				// recursively call the method for all the subdirectories
				addFilesToTarGZ(f.getAbsolutePath(), entryName + File.separator, tarArchive);
			}
		}          
	}
	
	private void cleanup()
	{
		File dir = new java.io.File("output");
		for(File file: dir.listFiles()) 
		    if (!file.isDirectory()) 
		        file.delete();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
