import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Member {
	private String mem_ID;
	private String fName;
	private String lName;
	private String street;
	private String city;
	private String phone;
	private String sDate;
	private static Statement stmt;
	DbConnection conn;
	public Member(String i, String s, String l, String t, String c, String p, String sd)
	{
		try {
			stmt = DbConnection.accessDbConnection().getConnection().createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		this.fName = s;
		this.lName = l;
		this.street = t;
		this.city = c;
		this.phone = p;
		this.mem_ID = i;
		this.sDate = sd;
	}
	public String getFName()
	{
		return fName;
	}
	public String getLName()
	{
		return lName;
	}
	public String getStreet()
	{
		return street;
	}
	public String getCity()
	{
		return city;
	}
	public String getPhone()
	{
		return phone;
	}
	public String getStartDate()
	{
		return sDate;
	}
	public int getMemberId()
	{
		return Integer.parseInt(mem_ID);
	}
	public static List<Member> getAll() {
		List<Member> l = new ArrayList<>();
		try {
			stmt = DbConnection.accessDbConnection().getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("Select * FROM MEMBER");
			while (rs.next())
			{
				l.add(new Member(rs.getString("member_id"),
						rs.getString("fname"),
						rs.getString("lname"),
						rs.getString("street"),
						rs.getString("city"),
						rs.getString("phone"),
						rs.getString("start_Date")));
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		return l;
	}
	public List<Title> getMyRented()
	{
		List<Title> l = new ArrayList<>();
		try {
			stmt = DbConnection.accessDbConnection().getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(""
					+ "SELECT * FROM title WHERE title_id in "
					+ "(SELECT title_id FROM copy WHERE copy_id in "
					+ "(SELECT copy_id FROM rental WHERE member_id="+mem_ID+"))");
			while (rs.next())
			{
				l.add(new Title(rs.getInt("title_id"),
						rs.getString("title_Name"),
						rs.getString("published_Date")));
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		return l;
	}
	public String toString()
	{
		return "Member "+mem_ID+" "+fName+" "+lName+"\n"+
				street+", "+city+"\n"+phone+"\n"+"since "+sDate;
	}
	public void rent(int j) {
		try {
			stmt = DbConnection.accessDbConnection().getConnection().createStatement();
			int cid = stmt.executeQuery("Select copy_id FROM copy WHERE title_id="+j).getInt("copy_id");
			ResultSet rs = stmt.executeQuery(""
					+ "INSERT INTO rental VALUES("
					+ "checkout_date=CURRENT_TIMESTAMP,"
					+ "copy_id="+cid+","
					+ "member_id="+mem_ID);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
