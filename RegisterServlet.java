package in.ineuron.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(
		urlPatterns = { "/reg" }, 
		initParams = { 
				@WebInitParam(name = "url", value = "jdbc:mysql://localhost:3306/kaviyadb1"), 
				@WebInitParam(name = "user", value = "root"), 
				@WebInitParam(name = "password", value = "3412")
		})
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection=null;
	private PreparedStatement pstmt=null;
	int rowCount=0;
	
	static {
		System.out.println("Register Servlet is loading...");
	}
	
	public RegisterServlet()
	{
		System.out.println("Register Servlet is instantiation...");
	}
	
	public void init()throws ServletException
	{
		System.out.println("Register Servlet initialization...");
		
		ServletConfig config=getServletConfig();
		
		String url=config.getInitParameter("url");
		String user=config.getInitParameter("user");
		String password=config.getInitParameter("password");
		

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection=DriverManager.getConnection(url,user,password);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name=request.getParameter("name");
		String password=request.getParameter("pass");
		String email=request.getParameter("emailId");
		String gender=request.getParameter("gender");
		String country=request.getParameter("country");
		
		String sqlInsertQuery="insert into student(name,password,email,gender,country) values(?,?,?,?,?)";
		
		try {
			if(connection!=null)
			{
				pstmt=connection.prepareStatement(sqlInsertQuery);
				if(pstmt!=null)
				{
					pstmt.setString(1, name);
					pstmt.setString(2, password);
					pstmt.setString(3, email);
					pstmt.setString(4, gender);
					pstmt.setString(5, country);
					
					rowCount=pstmt.executeUpdate();
				}
			}
			
		}catch(SQLException se)
		{
			se.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		out.println("<html><head><title>OutputPage</title></head>");
		out.println("<body>");
		if(rowCount==1)
		{
			out.println("<h1 style='color:green',text-align:center;>Registered Succesfuly</h1>");
		}
		else {
			out.println("<h1 style='color:red',text-align:center;>Registration Failed</h1>");
			out.println("<a href='./register.html' style='text-align:center';>Register again</a>");
		}
	
	}

}
