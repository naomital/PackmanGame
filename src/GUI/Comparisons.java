package GUI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**Comparisons - compers the scores in the data base
 *  and findes the highest and sconed highest for the specific game that was played
 * @author Adi and Naomi
 *
 */
public class Comparisons {
	private int maxPoint=0;
	private int theBiggest=0;
	private int almostTheBiggest=0;
	private String gameName;
	private String gameNameInt;

	/**
	 * Points - compers the scores in the data base
	 *  and findes the highest and sconed highest for the specific game that was played.
	 *  the function conects to the server and finds the data 
	 * @param numOfGame - the name u=in int of the game.
	 * @return - return a string of the data we have found.
	 */
	public String Points(int numOfGame) {
		if(numOfGame==2128259830) {
			gameNameInt="2128259830";
			gameName="Ex4_OOP_example1";
		}else if(numOfGame==1149748017) {
			gameNameInt="1149748017";
			gameName="Ex4_OOP_example2";
		}else if(numOfGame==683317070) {
			gameNameInt="683317070";
			gameName="Ex4_OOP_example3";
		}else if(numOfGame==1193961129) {
			gameNameInt="1193961129";
			gameName="Ex4_OOP_example4";
		}else if(numOfGame==1577914705) {
			gameNameInt="1577914705";
			gameName="Ex4_OOP_example5";
		}else if(numOfGame==1315066918) {
			gameNameInt="1315066918";
			gameName="Ex4_OOP_example6";
		}else if(numOfGame==1377331871) {
			gameNameInt="1377331871";
			gameName="Ex4_OOP_example7";
		}else if(numOfGame==-1377331871) {
			gameNameInt="-1377331871";
			gameName="Ex4_OOP_example8";
		}else if(numOfGame==919248096) {
			gameNameInt="919248096";
			gameName="Ex4_OOP_example9";
		}	
		String answer;
		String jdbcUrl="jdbc:mysql://ariel-oop.xyz:3306/oop";
		String jdbcUser="student";
		String jdbcPassword="student";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			resultSet=statement.executeQuery(allCustomersQuery);
			while(resultSet.next())
			{

				if(resultSet.getInt("FirstID")==207642638 && resultSet.getInt("SecondID")==313245268) 
					if(resultSet.getString("SomeDouble").equals(gameNameInt)) 
						if(maxPoint<resultSet.getInt("Point")) 
							maxPoint=resultSet.getInt("Point");
			}
			resultSet=statement.executeQuery(allCustomersQuery);
			while(resultSet.next())
			{ 
				if(resultSet.getString("SomeDouble").equals(gameNameInt)) 
					if(theBiggest<resultSet.getInt("Point")) {
						almostTheBiggest=theBiggest;
						theBiggest=resultSet.getInt("Point");
					}
			}
			resultSet.close();		
			statement.close();		
			connection.close();		
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
		answer="Name of the game:"+gameName+":  The highest point for this game:"+theBiggest+"\t"
				+" The almost highest point:"+almostTheBiggest;
		return answer;

	}

}


