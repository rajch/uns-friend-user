package com.example.starter;

import java.nio.charset.Charset;
import java.sql.Connection;

//import org.json.JSONException;
import io.vertx.core.json.JsonObject;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;


public class DBClass {
	private static Connection dbconnection;
	public DBClass() {
		try {
			dbconnection = DriverManager.getConnection("jdbc:mysql://129.221.92.199:3307/userdb", "root", "something");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}  
public JsonObject registerUser(String name, String email, String password) {
	JsonObject obj = null;
	try {
		
	    
		StringBuilder sqlquery = new StringBuilder();
		sqlquery.append("INSERT INTO userdb.user(username,userid,emailid,password) VALUES('");
		sqlquery.append(name+"','"+email+"','"+email+"','"+password+"')");
		System.out.println(sqlquery.toString());
		PreparedStatement ps = dbconnection.prepareStatement(sqlquery.toString());
		ResultSet rs = ps.executeQuery();
		JsonObject tokenObj = new JsonObject();
		tokenObj.put("name", name)
				.put("email", email)
				.put("userid", email); 
		//TODO: generate userID
		obj = new JsonObject();
		obj.put("Token", tokenObj);
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return obj;
}

public JsonObject authenticateUser(String email, String password) {
	JsonObject obj = null;
	try {
		PreparedStatement ps = dbconnection.prepareStatement("select  * from userdb.user");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String emailid = rs.getString("emailid");
			String userPwd = rs.getString("password");
			String userName = rs.getString("username");
			String userID = rs.getString("userid");
			
			System.out.println(rs);
			if(emailid.equals(email)  && userPwd.equals(password)) {
				JsonObject tokenObj = new JsonObject();
				tokenObj.put("name", userName)
				.put("email", emailid)
				.put("userid", userID); 
				//TODO: generate userID
				obj = new JsonObject();
				obj.put("Token", tokenObj);
				break;
			}
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return obj;
}
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//		DBClass db = new DBClass();
//		JsonObject obj = db.authenticateUser("pramodh.email", "pramodh");
//		System.out.println(obj.toString());
//		JsonObject obj1 = db.registerUser("Ratna1", "ratna.email1", "ratna.password");
//		System.out.println(obj1.toString());
//		
//	}

}
