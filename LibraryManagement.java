/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package librarymanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Administrator
 */
public class LibraryManagement {

    private static void newstaff(int staffid, String name, String email, String department, String gender,int userid) throws ClassNotFoundException
    {
    try {
        String url = "jdbc:mysql://localhost:3306/javacodeproject";  
        String username = "root";  
        String password = "Ishwarya@2004";
        Class.forName("com.mysql.cj.jdbc.Driver");
   
        try (Connection con = DriverManager.getConnection(url, username, password)) {
        // Insert into the 'user' table first
           String query1 = "INSERT INTO user(userid) VALUES (?)";
           PreparedStatement pst2 = con.prepareStatement(query1);
            pst2.setInt(1, userid);
            pst2.executeUpdate();
       
        // Now insert into the 'newstaf' table
            String query = "INSERT INTO newstaf(staffid, name, email, department, gender, userid) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query);
            pst1.setInt(1, staffid);
            pst1.setString(2, name);
            pst1.setString(3, email);
            pst1.setString(4, department);
            pst1.setString(5, gender);
            pst1.setInt(6, userid);
            pst1.executeUpdate();
       
            System.out.println("Staff added successfully");
    }
} catch (SQLException ex) {
    Logger.getLogger(LibraryManagement.class.getName()).log(Level.SEVERE, null, ex);
    System.out.println("Error adding staff to the database");
}
       
    }

    private static void newStudent(String name, String email, int rollnumber, String gender, int year,int userid) throws ClassNotFoundException, SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/javacodeproject";  
            String username = "root";  
            String password = "Ishwarya@2004";
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(url, username, password)) {

                String queryUser = "INSERT INTO user(userid) VALUES(?)";
                // Query to insert into newstud table
                String queryStudent = "INSERT INTO newstud(name, email, rollnumber, gender, year, userid) VALUES(?, ?, ?, ?, ?, ?)";

                PreparedStatement pstUser = con.prepareStatement(queryUser);
                pstUser.setInt(1, userid);
                pstUser.executeUpdate();
                PreparedStatement pstStudent = con.prepareStatement(queryStudent);
                pstStudent.setString(1, name);
                pstStudent.setString(2, email);
                pstStudent.setInt(3, rollnumber);
                pstStudent.setString(4, gender);
                pstStudent.setInt(5, year);
                pstStudent.setInt(6, userid);  
                pstStudent.executeUpdate();

                System.out.println("Student added successfully!");

            } catch (SQLException ex) {
                Logger.getLogger(LibraryManagement.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error adding student to the database");
            }

        } catch (ClassNotFoundException ex) {
            System.out.println("MySQL driver not found");
        }
   
    }
    private static void takebook(int bid,String fromdate,String todate,int copiestaken)
    {
         String url = "jdbc:mysql://localhost:3306/javacodeproject";  
    String username = "root";  
    String password = "Ishwarya@2004";

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            // Insert into takebook
            String insertQuery = "INSERT INTO takebook(bid, fromdate, todate, copiestaken) VALUES (?, ?, ?, ?)";
            PreparedStatement pstInsert = con.prepareStatement(insertQuery);
            pstInsert.setInt(1, bid);
            pstInsert.setString(2, fromdate);
            pstInsert.setString(3, todate);
            pstInsert.setInt(4, copiestaken);
            pstInsert.executeUpdate();

            // Update the book count in the book table
            String updateQuery = "UPDATE book SET bookcount = bookcount - ? WHERE bookid = ?";
            PreparedStatement pstUpdate = con.prepareStatement(updateQuery);
            pstUpdate.setInt(1, copiestaken);  // decrease quantity by copiestaken
            pstUpdate.setInt(2, bid);  // specify which book to update
           
            int rowsUpdated = pstUpdate.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book taken successfully");
            } else {
                System.out.println("No book found with the given ID or not enough copies available");
            }

        }
    } catch (SQLException ex) {
        Logger.getLogger(LibraryManagement.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("Error taking the book from the database");
    } catch (ClassNotFoundException ex) {
        System.out.println("JDBC Driver not found");
    }
     
     
    }
    public static void bookreturn(int bookid,int noofcop)throws ClassNotFoundException, SQLException
    {
       String url = "jdbc:mysql://localhost:3306/javacodeproject";  
    String username = "root";  
    String password = "Ishwarya@2004";

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            // Insert into bookreturn table
            String insertQuery = "INSERT INTO bookreturn(bookid, booktoreturn) VALUES (?, ?)";
            PreparedStatement pstInsert = con.prepareStatement(insertQuery);
            pstInsert.setInt(1, bookid);
            pstInsert.setInt(2, noofcop);
            pstInsert.executeUpdate();

            // Update the quantity in the book table
            String updateQuery = "UPDATE book SET bookcount = bookcount + ? WHERE bookid = ?";
            PreparedStatement pstUpdate = con.prepareStatement(updateQuery);
            pstUpdate.setInt(1, noofcop);  // Increase quantity by the number of copies returned
            pstUpdate.setInt(2, bookid);  // Specify which book to update
            pstUpdate.executeUpdate();

            // Remove the entry from the takebook table
            String deleteQuery = "DELETE FROM takebook WHERE bid = ? AND copiestaken = ?";
            PreparedStatement pstDelete = con.prepareStatement(deleteQuery);
            pstDelete.setInt(1, bookid);
            pstDelete.setInt(2, noofcop); // Assuming you are returning the same number of copies taken
            int rowsAffected = pstDelete.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book returned successfully");
            } else {
                System.out.println("No matching entry found in takebook table for deletion");
            }

        }
    } catch (SQLException ex) {
        Logger.getLogger(LibraryManagement.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("Error returning the book to the database");
    } catch (ClassNotFoundException ex) {
        System.out.println("JDBC Driver not found");
    }
    }
    public static void newbook(int bookid,int bookcount) throws ClassNotFoundException, SQLException{
       
        try {
            String url = "jdbc:mysql://localhost:3306/javacodeproject";  
            String username = "root";  
            String password = "Ishwarya@2004";
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(url, username, password)) {
                String query = "INSERT INTO book(bookid, bookcount) VALUES (?, ?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, bookid);
                pst.setInt(2, bookcount);
               
                pst.executeUpdate();
                System.out.println("Book added successfully");
            }
    } catch (SQLException ex) {
        Logger.getLogger(LibraryManagement.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("Error adding book to the database");
    }
       
    }
   
    /**
     * @param args the command line arguments
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter choice: ");
        int choice=sc.nextInt();
        sc.nextLine();
        switch(choice){
            case 1:
                System.out.print("Enter bookid: ");
                int bookid = sc.nextInt();
                sc.nextLine(); // Clear the buffer after nextInt()
                System.out.println("Enter fromdate (YYYY-MM-DD): ");
                String fromdate = sc.nextLine();
                System.out.println("Enter todate (YYYY-MM-DD): ");
                String todate = sc.nextLine(); // Read the full line for date
                System.out.println("Enter copiestaken: ");
                int copiestaken = sc.nextInt();
                takebook(bookid, fromdate, todate, copiestaken);
                break;
            case 2:
                System.out.println("Enter student name: ");
                String studname = sc.nextLine();
                System.out.println("Enter student email: ");
                String studemail = sc.nextLine();
                System.out.println("Enter Student rollnumber: ");
                int stroll = sc.nextInt();
                sc.nextLine();
                System.out.println("Enter Student gender: ");
                String genderr = sc.nextLine();
                System.out.println("Enter year: ");
                int yearr = sc.nextInt();
                System.out.println("Enter userid: ");
                int usersid = sc.nextInt();
                newStudent(studname, studemail, stroll, genderr, yearr, usersid);
                break;
            case 3:
                System.out.println("Enter staffid: ");
                int staffidd=sc.nextInt();
                sc.nextLine();
                System.out.println("Enter staffname: ");
                String namee=sc.nextLine();
                System.out.println("Enter email: ");
                String emaill=sc.nextLine();
                System.out.println("Enter staff department : ");
                String departmentt=sc.next();
                System.out.println("Enter gender : ");
                String genders=sc.next();
                System.out.println("Enter userid");
                int useridd=sc.nextInt();
                newstaff(staffidd,namee,emaill,departmentt,genders,useridd);
                break;
            case 4:
                System.out.print("Enter bookid: ");
                int newbookid=sc.nextInt();
                System.out.print("Enter bookcount: ");
                int newbookcount=sc.nextInt();
                newbook (newbookid,newbookcount);
                break;
            case 5:
                System.out.print("Enter the book to be returned: ");
                int bookidd=sc.nextInt();
                System.out.println("Enter no of books to be returned: ");
                int noofbook=sc.nextInt();
                bookreturn(bookidd,noofbook);
               
        }
       
       
    }
   
}
