import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Title {
	private int title_id;
	private String name;
	private String pubdate;
	public Title(int i, String s, String t)
	{
		title_id = i;
		name = s;
		pubdate = t;
	}
	public int getid()
	{
		return title_id;
	}
	public String getName()
	{
		return name;
	}
	public String getDate()
	{
		return pubdate;
	}
	public static List<Title> getAllTitles()
	{
		Statement st;
		List<Title> t = new ArrayList<>();
		try {
		st = DbConnection.accessDbConnection().getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM title");
		while(rs.next())
		{
			t.add(new Title(rs.getInt("title_id"), rs.getString("title_Name"), rs.getString("published_Date")));
		}
		st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return t;
	}
	public static List<Title> getAvailableTitles()
	{
		Statement st;
		List<Title> t = new ArrayList<>();
		try {
		st = DbConnection.accessDbConnection().getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM title WHERE title.title_id in (SELECT title_id FROM copy WHERE status='IN STORE')");
		while (rs.next())
		{
			t.add(new Title(rs.getInt("title_id"),rs.getString("title_Name"),rs.getString("published_Date")));
		}
		st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return t;
	}
	void checkOut(Title t)
	{
		Statement st;
		try {
			DbConnection conn = DbConnection.accessDbConnection();
			Connection c = conn.getConnection();
			st = c.createStatement();
			st.executeUpdate("UPDATE COPY SET status='CHECKED OUT' WHERE title_id=" + t.title_id +";");
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void checkIn(Title t)
	{
		Statement st;
		try {
			DbConnection conn = DbConnection.accessDbConnection();
			Connection c = conn.getConnection();
			st = c.createStatement();
			st.executeUpdate("UPDATE COPY SET status='IN STORE' WHERE title_id=" + t.title_id +";");
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	public String toString()
	{
		return "id "+title_id+" "+name+" published"+pubdate;
	}
}
