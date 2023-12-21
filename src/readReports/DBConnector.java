package readReports;

public class DBConnector 
{
	final String MYSQL = "jdbc:mysql://";
	final String URL = "123.123.123.123";
	final String PORT = ":3306";
	
	final String MODIFIKATOREN = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	final String USERNAME = "minecraft_sql";
	final String PASSWORD = "DEIN_PASSWORT";
	
	final String MONEY_DB = "/player_inventories";
	final String MONEY_DB_QUERY = "SELECT player_name,money FROM essentials_userdata";
	
	final String VOTING_DB = "/vote";
	final String VOTING_DB_QUERY = "SELECT uuid,PlayerName,LastOnline,LastVotes FROM cb1_votingplugin_users";
}
