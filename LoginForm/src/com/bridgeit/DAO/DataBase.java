package com.bridgeit.DAO;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bridgeit.Model.UserDetails;


public class DataBase 
{
	final String JDBC_Driver = "com.mysql.jdbc.Driver";
	final String DB_Url = "jdbc:mysql://localhost:3306/akash";
	
	//Database credential
	final String username = "root";
	final String password = "root";
	Connection connection=null;
	ResultSet resultSet=null;
	
	public UserDetails getEmail(UserDetails userDetails)
	{
		try {
			Class.forName(JDBC_Driver);
			connection = DriverManager.getConnection(DB_Url,username,password);
			String query = "select * from registration where email=? and password =?"; 
			PreparedStatement prepareStatement = connection.prepareStatement(query);
			prepareStatement.setString(1, userDetails.getEmail());
			prepareStatement.setString(2, userDetails.getPassword());
			resultSet= prepareStatement.executeQuery();
			boolean result=resultSet.next();
			if (!result) 
			{ 
				System.out.println("Sorry, you are not a registered user! Please sign up first"); 
				userDetails.setValid(false); 
			} //if user exists set the isValid variable to true 
			else if (result) 
			{ 
				String name = resultSet.getString(1); 
				String mobile = resultSet.getString(4);
				userDetails.setName(name); 
				userDetails.setMobile(mobile);
				userDetails.setValid(true); 
			} 
	
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userDetails;
		
	}
	public boolean insertUser(UserDetails user)
	{
		PreparedStatement prepareStatement = null;
		String userEmail=null;
		String userMobile = null;
		boolean flag=true;
		try
		{
			Class.forName(JDBC_Driver);
			connection = DriverManager.getConnection(DB_Url,username,password);
			Statement statement = connection.createStatement();
			//Query to insert new data in database 
			prepareStatement = connection.prepareStatement("insert into registration (name,email,password,mobilenumber) values(?,?,?,?)");
			// reading data from signup.jsp page
			String email="";
			String mobile ="";
			ResultSet rs = statement.executeQuery("select * from registration where email='"+user.getEmail()+"' or mobilenumber='"+user.getMobile()+"'");
			while (rs.next())
			{
				email = rs.getString(2);
				mobile = rs.getString(4);
			}
			if(!(email.equals(user.getEmail()) || mobile.equals(user.getMobile())))
			{
				System.out.println("true");
				prepareStatement.setString(1, user.getName());
				prepareStatement.setString(2, user.getEmail());
				prepareStatement.setString(3, user.getPassword());
				prepareStatement.setString(4, user.getMobile());
				prepareStatement.executeUpdate();
			}
			else
			{
				flag=false;
			}
			
			//setting values to attribute
			
		}
		catch(java.sql.SQLException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally{
			if(prepareStatement!=null)
				try {
					prepareStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return flag;
	}
}
