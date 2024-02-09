package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static void toLists(String input, List<String> uppercase, List<String> lowercase) {
        String[] words = input.split(" ");
        for (String word : words) {
            if (Character.isUpperCase(word.charAt(0))) {
                uppercase.add(word);
            } else {
                lowercase.add(word);
            }
        }
    }


//    I have any difficulties when I try to work with databases

    private static void saveMySQL(List<String> strings) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database1", "username", "password");

        String dropTableSQL = "DROP TABLE IF EXISTS uppercase";
        Statement dropTableStatement = conn.createStatement();
        dropTableStatement.execute(dropTableSQL);

        String createTableSQL = "CREATE TABLE IF NOT EXISTS uppercase (words VARCHAR(255))";
        Statement createTableStatement = conn.createStatement();
        createTableStatement.execute(createTableSQL);

        String sql = "INSERT INTO uppercase (words) VALUES (?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        for (String word : strings) {
            statement.setString(1, word);
            statement.executeUpdate();
        }
        conn.close();
    }

    private static void savePostgreSQL(List<String> strings) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/database2", "postgres", "4652343L");

        String dropTableSQL = "DROP TABLE IF EXISTS lowercase";
        Statement dropTableStatement = conn.createStatement();
        dropTableStatement.execute(dropTableSQL);


        String createTableSQL = "CREATE TABLE IF NOT EXISTS lowercase (words VARCHAR(255))";
        Statement createTableStatement = conn.createStatement();
        createTableStatement.execute(createTableSQL);

        String sql = "INSERT INTO lowercase (words) VALUES (?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        for (String word : strings) {
            statement.setString(1, word);
            statement.executeUpdate();
        }
        conn.close();
    }



    private static List<String> readMySQL() throws SQLException {
        List<String> words = new ArrayList<>();
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database1", "username", "password");
        String sql = "SELECT words FROM uppercase";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            words.add(resultSet.getString("words"));
        }
        conn.close();
        return words;
    }

    private static List<String> readPostgreSQL() throws SQLException {
        List<String> words = new ArrayList<>();
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/database2", "postgres", "4652343L");
        String sql = "SELECT words FROM lowercase";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            words.add(resultSet.getString("words"));
        }
        conn.close();
        return words;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your input string:");
        String input = scanner.nextLine();
        scanner.close();

        List<String> uppercase = new ArrayList<>();
        List<String> lowercase = new ArrayList<>();
        toLists(input, uppercase, lowercase);

        try {
            saveMySQL(uppercase);
            savePostgreSQL(lowercase);

            List<String> savedUppercaseWords = readMySQL();
            List<String> savedLowercaseWords = readPostgreSQL();


            System.out.println("Saved Uppercase words:");
            for (String word : savedUppercaseWords) {
                System.out.println(word);
            }
            System.out.println("Saved Lowercase words:");
            for (String word : savedLowercaseWords) {
                System.out.println(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}


