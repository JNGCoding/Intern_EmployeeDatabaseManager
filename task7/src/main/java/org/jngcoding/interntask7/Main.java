package org.jngcoding.interntask7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

@SuppressWarnings("unused")
public class Main {
    // & Database Settings
    private static final String USERNAME = "root";
    private static final String DATABASE_NAME = "INTERNSHIP_TASK";
    private static final String TABLE_NAME = "EMPLOYEES";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;

    // ^ Password should be kept hidden. Preferred to read from a "secrets.txt" file or something.
    private static final String PASSWORD = "***********";

    // & Program Variables
    private static boolean RunFlag = true;
    private static final Scanner Input = new Scanner(System.in);

    // & Functions
    public static void ExecuteStatement(Connection connection, String COMMAND) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(COMMAND);
        } catch (SQLException exception) { System.out.println(exception.getMessage()); }
    }

    public static void AddNewRecord(Connection connection, int ID, String name, int Salary) {
        try (PreparedStatement prstmt = connection.prepareStatement("INSERT INTO EMPLOYEES (ID, NAME, SALARY) VALUES (?, ?, ?);")) {
            prstmt.setInt(1, ID);
            prstmt.setString(2, name);
            prstmt.setInt(3, Salary);
            prstmt.executeUpdate();
        } catch (SQLException exception) { System.out.println(exception.getMessage()); }
    }

    public static void RemoveRecord(Connection connection, int ID) {
        try (PreparedStatement prstmt = connection.prepareStatement("DELETE FROM EMPLOYEES WHERE ID = ?;")) {
            prstmt.setInt(1, ID);
            prstmt.executeUpdate();
        } catch (SQLException exception) { System.out.println(exception.getMessage()); }
    }

    public static void UpdateRecord(Connection connection, int ID, int NewSalary) {
        try (PreparedStatement prstmt = connection.prepareStatement("UPDATE EMPLOYEES SET SALARY = ? WHERE ID = ?;")) {
            prstmt.setInt(1, NewSalary);
            prstmt.setInt(2, ID);
            prstmt.executeUpdate();
        } catch (SQLException exception) { System.out.println(exception.getMessage()); }
    }

    public static void PrintResultSet(ResultSet set) {
        try {
            while (set.next()) {
                System.out.printf("Employee(ID = %d, Name = %s, Salary = %d)\n", set.getInt("ID"), set.getString("NAME"), set.getInt("SALARY"));
            }
        } catch (SQLException exception) { System.out.println(exception.getMessage()); }
    } 

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            while (RunFlag) {
                System.out.println(
                """
                Press the corresponding number to execute that operation:
                1) Add an employee record.
                2) Remove an employee record.
                3) View an employee record.
                4) Update the salary of an employee.
                5) Print EMPLOYEES Table.
                6) Exit. 
                """
                );
                System.out.print("Enter Option: ");
                int option = Input.nextInt();

                switch (option) {
                    case 1 -> {
                        System.out.print("Enter Employee ID: ");
                        int id = Input.nextInt();
                        System.out.print("Enter Employee Name: ");
                        String name = Input.next();
                        System.out.print("Enter Employee Salary: ");
                        int salary = Input.nextInt();
                        AddNewRecord(connection, id, name, salary);
                    }

                    case 2 -> {
                        System.out.print("Enter Employee ID: ");
                        int id = Input.nextInt();
                        RemoveRecord(connection, id);
                    }

                    case 3 -> {
                        System.out.print("Enter Employee ID: ");
                        int id = Input.nextInt();
                        try (Statement st = connection.createStatement()) {
                            ResultSet rs = st.executeQuery("SELECT * FROM EMPLOYEES WHERE ID = " + id + ";");
                            PrintResultSet(rs);
                        }
                    }


                    case 4 -> {
                        System.out.print("Enter Employee ID: ");
                        int id = Input.nextInt();
                        System.out.print("Enter Employee Salary: ");
                        int salary = Input.nextInt();
                        UpdateRecord(connection, id, salary);
                    }

                    case 5 -> {
                        try (Statement st = connection.createStatement()) {
                            ResultSet rs = st.executeQuery("SELECT * FROM EMPLOYEES;");
                            PrintResultSet(rs);
                        }
                    }


                    case 6 -> {
                        RunFlag = false;
                    }

                    default -> {
                        System.out.println("Invalid Operation.");
                    }
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }
}