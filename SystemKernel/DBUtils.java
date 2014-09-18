package SystemKernel;    
/** 
 * @author Happut-WangFei
 * @version 
 * @time 2011-10-26 下午6:41:54
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;

import Model.UserChatData;
import Model.UserManagerData;
import View.MainWindow;

public class DBUtils {
	private Connection getConn(){
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String url="jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=db/Database.mdb";			
			Connection con = DriverManager.getConnection(url, "", "");			
			return con;
			
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public void CreateMessageTable(String name){
		try {
			Connection con=getConn();
			Statement sm=con.createStatement();
			sm.executeUpdate("create table "+name+
					"(IP VARCHAR(30)," +
					"ID VARCHAR(30)," +
					"CHATTIME VARCHAR(30)," +
					"MESSAGE VARCHAR(100))");
			sm.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
		}
	}
	public void InsertManagerData(UserManagerData umd){
		try {
			Connection con=getConn();
			Statement sm = con.createStatement();
			sm.executeUpdate(  "insert into UserManager values('"+umd.getIp()+"','"+umd.getId()+"','"+umd.getChattime()+"','"+umd.getChattable()+"')");
			sm.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void InsertChatData(String table,UserChatData ucd){
		try {
			Connection con=getConn();
			Statement sm = con.createStatement();
			sm.executeUpdate("insert into "+table+" values('"+ucd.getIp()+"','"+ucd.getId()+"','"+ucd.getChattime()+"','"+ucd.getMessage()+"')");
			sm.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public boolean CheckTableExist(String table){
		try {
			Connection con=getConn();
			Statement sm = con.createStatement();
			ResultSet rs=sm.executeQuery("select * from UserManager where chattable='"+table+"'");
			if(!rs.next()){
				rs.close();
				sm.close();
				con.close();
				return false;
			}else{
				rs.close();
				sm.close();
				con.close();
				return true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
	}
	public void updateManagerTime(String ip,String id,String chattime){
		try {
			Connection con=getConn();
			Statement sm = con.createStatement();
			sm.executeUpdate("update UserManager set chattime='"+chattime+"',id='"+id+"' where ip='"+ip+"'");	
			sm.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	public LinkedList<UserManagerData> QueryManagerData(){
		LinkedList<UserManagerData> ll=new LinkedList<UserManagerData>();
		try {
			Connection con=getConn();
			Statement sm = con.createStatement();
			ResultSet rs=sm.executeQuery("select * from UserManager");
			while(rs.next()){
				UserManagerData umd=new UserManagerData();
				umd.setIp(rs.getString("IP"));
				umd.setId(rs.getString("ID"));
				umd.setChattime(rs.getString("CHATTIME"));
				umd.setChattable(rs.getString("CHATTABLE"));
				ll.add(umd);
			}
			rs.close();
			sm.close();
			con.close();
			return ll;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ll;
		}
	}

	public LinkedList<UserChatData> QueryChatData(String table){
		LinkedList<UserChatData> ll=new LinkedList<UserChatData>();
		try {
			Connection con=getConn();
			Statement sm = con.createStatement();
			ResultSet rs=sm.executeQuery("select * from "+table);
			while(rs.next()){
				UserChatData ucd=new UserChatData();
				ucd.setIp(rs.getString("IP"));
				ucd.setId(rs.getString("ID"));
				ucd.setChattime(rs.getString("CHATTIME"));
				ucd.setMessage(rs.getString("MESSAGE"));
				ll.add(ucd);
			}
			rs.close();
			sm.close();
			con.close();
			return ll;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ll;
		}
	}	
	
	public static void main(String[] args){
		DBUtils db=new DBUtils();
		String table="127_0_0_1";
////		System.out.println(!db.CheckTableExist(table));
		if(!db.CheckTableExist(table)){
			//不存在，就建立新表对应条目
			UserManagerData umd=new UserManagerData();
			umd.setIp("127.0.0.1");
			umd.setId("wangfei");
			umd.setChattime("");
			umd.setChattable(table);
			
			//插入新表对应项目
			db.InsertManagerData(umd);
			//创建对应表
			db.CreateMessageTable(table);
		}	
		
		UserChatData ucd=new UserChatData();
		ucd.setIp("127.0.0.1");
		ucd.setId("wangfei3");
		ucd.setChattime("2010-1-1");
		ucd.setMessage("wafadsfdsafdsafdsaf");
		
//		//更新管理表中的最后时间
		db.updateManagerTime(ucd.getIp(),ucd.getId(), ucd.getChattime());
//			
//		//将记录写到相应表
		db.InsertChatData(table, ucd);		
		LinkedList<UserManagerData> ll=db.QueryManagerData();
		
		for(int i=0;i<ll.size();i++){
			UserManagerData umd=ll.get(i);
			System.out.println(umd.getIp()+umd.getId()+umd.getChattime()+umd.getChattable());
		}
	}
}
  
