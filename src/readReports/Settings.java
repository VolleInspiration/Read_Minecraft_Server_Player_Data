package readReports;

import java.io.File;

public class Settings 
{
	Main main = new Main();
	
	public File pathSettingsFolder = new java.io.File("settings");
	
	public static File pathLogfilesFolder = new java.io.File(readReports.Main.prefs.get(readReports.Main.PREF_LOGFILES_DIR, readReports.Main.logfilesdir_value));//main.einstellungen_logfiles_dir_name.getText());
	
	public static String[] serverLogsPath = {pathLogfilesFolder + "/" + "Bungee",
			pathLogfilesFolder + "/" + "Hub", pathLogfilesFolder + "/" + "CB1"};
	public static File output = new java.io.File( "output" );
	public static File outputgz = new java.io.File( "outputGZ" );
	public static File serverLogPathBungee = new java.io.File( serverLogsPath[0] );
	public static File serverLogPathHub = new java.io.File( serverLogsPath[1] );
	public static File serverLogPathCB1 = new java.io.File( serverLogsPath[2] );
	
	public void checkDirectoryAndFiles()
	{
		System.out.println("inititlize system settings...");
		
		if(pathSettingsFolder.mkdir())
			pathSettingsFolder.mkdir();
		if(pathLogfilesFolder.mkdir())
			pathLogfilesFolder.mkdir();
		if(serverLogPathBungee.mkdir())
			serverLogPathBungee.mkdir();
		if(serverLogPathHub.mkdir())
			serverLogPathHub.mkdir();
		if(serverLogPathCB1.mkdir())
			serverLogPathCB1.mkdir();
		if(output.mkdir())
			output.mkdir();
		if(outputgz.mkdir())
			outputgz.mkdir();
		
		createSettingsFile();
	}
	
	private void createSettingsFile()
	{
		
	}
}
