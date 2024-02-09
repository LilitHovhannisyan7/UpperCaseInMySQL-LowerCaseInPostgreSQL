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

//    I have any difficulties here
    private static void saveMySQL(List<String> strings) throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database_name", "username", "password");
        String sql = "INSERT INTO uppercase_words (words) VALUES (?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        for (String word : strings) {
            statement.setString(1, word);
            statement.executeUpdate();
        }
        conn.close();
    }


    private static void savePostgreSQL(List<String> strings) throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/database_name", "username", "password");
        String sql = "INSERT INTO lowercase_words (word) VALUES (?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        for (String word : strings) {
            statement.setString(1, word);
            statement.executeUpdate();
        }
        conn.close();
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


            System.out.println("Uppercase words");
            for (String word : uppercase) {
                System.out.println(word);
            }
            System.out.println("Lowercase words");
            for (String word : lowercase) {
                System.out.println(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
