package readReports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBcheckData 
{
	Connection con;
	Statement stmt;
	ResultSet rs;
	
	DBConnector dbc = new DBConnector();
	
	public String[] getMoneyDB()
	{
		String[] returnMoney = new String[2];
		returnMoney[0] = dbc.MONEY_DB;
		returnMoney[1] = dbc.MONEY_DB_QUERY;
		return returnMoney;
	}
	
	public String[] getVoteDB()
	{
		String[] returnVote = new String[2];
		returnVote[0] = dbc.VOTING_DB;
		returnVote[1] = dbc.VOTING_DB_QUERY;
		return returnVote;
	}
	
	public void getMoney(ResultSet rs, String playername)
	{
		try {
			if(rs.getString(1).equalsIgnoreCase(playername))
			{
				readReports.Main.outputTextDatabase.append("MONEY: " + returnNull( rs.getString(2) ) + "\n");
				readReports.Main.money_user.add( returnNull( rs.getString(2) ) );
			}
		} catch (SQLException e) {
			System.out.println("Fehler bei der Datenausgabe!");
		}
	}
	
	public void getVote(ResultSet rs, String playername)
	{
		try {
			if(rs.getString(2).equalsIgnoreCase(playername))
			{
				//uuid,PlayerName,LastOnline,LastVotes
				readReports.Main.outputTextDatabase.append("VOTE: " + "uuid:" + returnNull( rs.getString(1) ) + ",playername:" + returnNull( rs.getString(2) ) + 
						",lastonline:" + returnNull( rs.getString(3) ) + ",lastvotes:" + returnNull( rs.getString(4) ) + "\n");
				readReports.Main.votes.add(0, returnNull( rs.getString(1) ));
				readReports.Main.votes.add(1, returnNull( rs.getString(2) ));
				readReports.Main.votes.add(2, returnNull( rs.getString(3) ));
				readReports.Main.votes.add(3, returnNull( rs.getString(4) ));
			}
		} catch (SQLException e) {
			System.out.println("Fehler bei der Datenausgabe!");
		}
	}
	
	public String returnNull(String value)
	{
		if(value == null)
			value = "KEINE DATEN VORHANDEN";
		return value;
	}
	
	public void loadData(String playername, String database)
	{	
		String[] dbs = new String[2];
		switch(database)
		{
			case "money":
				dbs[0] = getMoneyDB()[0];
				dbs[1] = getMoneyDB()[1];
				break;
			case "vote":
				dbs[0] = getVoteDB()[0];
				dbs[1] = getVoteDB()[1];
				break;
		}
		System.out.println("Versuche die Verbindung zur Datenbank aufzubauen...");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//con = DriverManager.getConnection(url, username, password);
			con = DriverManager.getConnection(dbc.MYSQL + dbc.URL + dbc.PORT + dbs[0] + dbc.MODIFIKATOREN, dbc.USERNAME, dbc.PASSWORD);
			stmt = con.createStatement();
			rs = stmt.executeQuery(dbs[1]);//TODO
			while(rs.next()) 
			{
				if(database.contains("money"))
					getMoney(rs, playername);
				if(database.contains("vote"))
					getVote(rs, playername);
			}
				 
		} catch (ClassNotFoundException | SQLException e1) {
			System.out.println("Die Verbindung konnte NICHT aufgebaut werden!");
			e1.printStackTrace();
		}
		closeConnection();
	}
	
	private void closeConnection()
	{
		System.out.println("Die Verbindung wird beendet!");
		try {
			if(con != null)
			{
				con.close();
				con = null;
			}
			if(stmt != null)
			{
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			System.out.println("Die Verbindung konnte nicht beendet werden!");
		}
	}
}

