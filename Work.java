import com.sun.net.httpserver.Authenticator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.*;


public class Work {


    public static Connection con;
    static String username = "root";
    static String password = "Yousaf0421";
    public static final String workers = "com.mysql.jdbc.workers";
    public static final String complaints = "com.mysql.jdbc.complain";
    public static final String databaseUrl = "JDBC:MYSQL://LOCALHOST:3306/drengene";
    static Scanner scan = new Scanner(System.in);


    public static void main(String[] args) throws SQLException{

        boolean program = true;

        while (program){

            System.out.println("1. se alle workers");
            System.out.println("2. se alle complaints");
            System.out.println("3. søg efter en worker og se deres complaint");
            System.out.println("4. se alle complaints inden for en bestem dato");
            System.out.println("5. oprette en complaint");
            System.out.println("6. tilføje et worker til databasen");
            System.out.println("7. slette en worker fra databasen");
            System.out.println("8. afslutte programmet");

            int choice = scan.nextInt();

            switch (choice){

                case 1:
                    visWorkers();
                    break;
                case 2:
                    visComplaint();
                    break;
                case 3:
                    System.out.println("hvilken person vil du søge efter? Kun fornavn");
                    String name = scan.next();
                    findPerson(name);

                    break;
                case 4:
                    System.out.println("hvilken dato skal det starte fra");
                    System.out.println("år?");
                    int fromYear = scan.nextInt();
                    System.out.println("måned?");
                    int fromMonth = scan.nextInt();
                    System.out.println("dag?");
                    int fromDay = scan.nextInt();
                    System.out.println("nu til dato");
                    System.out.println("år");
                    int toYear = scan.nextInt();
                    System.out.println("måned");
                    int toMonth = scan.nextInt();
                    System.out.println("dag?");
                    int toDay = scan.nextInt();
                    withinDates(fromYear, fromMonth, fromDay, toYear, toMonth, toDay);

                    break;
                case 5:
                    System.out.println("whats the complaint");
                    scan.nextLine();
                    String complaint = scan.nextLine();
                    System.out.println("whats the id");
                    int id = scan.nextInt();
                    System.out.println("date year?");
                    int year = scan.nextInt();
                    System.out.println("date month");
                    int month = scan.nextInt();
                    System.out.println("date day?");
                    int day = scan.nextInt();
                    createComplaint(complaint, id, year, month, day);
                    break;
                case 6:
                    System.out.println("first name");
                    String firstName = scan.next();
                    System.out.println("last name");
                    String lastName = scan.next();
                    System.out.println("id?");
                    int idToAdd = scan.nextInt();
                    createWorker(firstName, lastName, idToAdd);
                    break;
                case 7:
                    System.out.println("whats the persons name");
                    String nameToDelete = scan.next();
                    removeWorker(nameToDelete);
                    break;
                case 8:
                    program = false;
                    break;
                default:
                    System.out.println("forkert valg");


            }





        }


    }



    public static void visWorkers() throws SQLException{

        try {
            con = DriverManager.getConnection(databaseUrl, username, password);
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select *\n" +
                    "from workers;");

            while (rs.next()){
                System.out.println("Fornavn: " + rs.getString("Navn"));
                System.out.println("Eftenavn: " + rs.getString("efternavn"));
                System.out.println("Id: " + rs.getInt("id"));
                System.out.println();
            }


        } catch (SQLException sqlex){
            System.out.println(sqlex.getMessage());
            con.close();
        }


    }


    public static void visComplaint() throws  SQLException{

        try {

            con = DriverManager.getConnection(databaseUrl, username, password);
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select *\n" +
                    "from complain;");
            while (rs.next()){
                System.out.println("Complaint: " + rs.getString("complaint_description"));
                System.out.println("id: " + rs.getInt("id"));
                System.out.println("Dato for complaint: " + rs.getDate("date_of_event"));
                System.out.println();
            }


        } catch (SQLException sqlex){
            System.out.println(sqlex.getMessage());
            con.close();
        }
    }


    public static void findPerson(String name) throws SQLException{

        try{
            con = DriverManager.getConnection(databaseUrl, username, password);
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT *\n" +
                    "FROM workers w\n" +
                    "JOIN complain c USING (id)\n" +
                    "WHERE w.Navn = '" + name + "';");
            while (rs.next()){
                System.out.println("Complaints: " + rs.getString("complaint_description"));
                System.out.println("Dato: " + rs.getDate("date_of_event"));
                System.out.println();
            }



        } catch (SQLException sqlex){
            System.out.println(sqlex.getMessage());
            con.close();
        }

    }


    public static void withinDates(int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay)
    throws SQLException{
        try {

            String fromDate = fromYear + "-" + fromMonth + "-" + fromDay;
            String toDate = toYear + "-" + toMonth + "-" + toDay;

            con = DriverManager.getConnection(databaseUrl, username, password);
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT *\n" +
                    "FROM complain\n" +
                    "WHERE date_of_event BETWEEN '" + fromDate + "' AND '" + toDate + "';");

                    while (rs.next()){
                        System.out.println("Complaint: " + rs.getString("complaint_description"));
                        System.out.println("id: " + rs.getInt("id"));
                        System.out.println("Dato for complaint: " + rs.getDate("date_of_event"));
                    }


        } catch (SQLException sqlex){
            System.out.println(sqlex.getMessage());
            con.close();
        }

    }


    public static void createComplaint(String complaint, int id, int year, int month, int day)throws SQLException{

        try {

            String date = year + "-" + month + "-" + day;

            con = DriverManager.getConnection(databaseUrl, username, password);
            Statement s = con.createStatement();
            int addingRow = s.executeUpdate("INSERT INTO complain(complaint_description, id, date_of_event)\n" +
                    "VALUES(" + "'"+ complaint + "'" + ", " + id + ", '" +  date + "');");
            System.out.println("added row");

        } catch (SQLException sqlex){
            System.out.println(sqlex.getMessage());
            con.close();
        }
    }

    public static void createWorker(String firstName, String lastName, int id) throws SQLException{
        try {

            con = DriverManager.getConnection(databaseUrl, username, password);
            Statement s = con.createStatement();
            int rowAdded = s.executeUpdate("INSERT INTO workers(Navn, efternavn, id)\n" +
                    "VALUES('" +  firstName + "', '" +  lastName + "', " + id + ");");
            System.out.println("row added");


        } catch (SQLException sqlex){
            System.out.println(sqlex.getMessage());
            con.close();
        }


    }


    public static void removeWorker(String firstName) throws SQLException{

        try {

            con = DriverManager.getConnection(databaseUrl, username, password);
            Statement s = con.createStatement();
            int removeRow = s.executeUpdate("DELETE FROM workers\n" +
                    "WHERE NAVN = '" +  firstName + "';");
            System.out.println("removed worker");
        } catch (SQLException sqlex){
            System.out.println(sqlex.getMessage());
            con.close();
        }
    }

}
