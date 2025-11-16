package org.example;// Software server, that can connect to a payment gateway, and a software for accounts management that manages accounts and password
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql:mydb";
        Properties properties = new Properties();
        File file = new File("secrets.env");
        String dbUsername;
        String dbPassword;
        String usersTable;
        try (Scanner secrets = new Scanner(file)) {
            dbUsername = secrets.nextLine();
            dbPassword = secrets.nextLine();
            usersTable = secrets.nextLine();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        properties.setProperty("user", dbUsername);
        properties.setProperty("password", dbPassword);

        Connection connection = DriverManager.getConnection(url, properties);
        PreparedStatement statement = connection.prepareStatement()


        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("\nCreate Password: ");
        String password = scanner.nextLine();
        createUser(username, password, usersTable, statement);

        // salt is unique for each user
        // todo instead of using a map, store this data inside a database

        System.out.println("\nTest your password");
        String testPassword = scanner.nextLine();
        scanner.close();

        // arrays.equals is needed here because otherwise java would compare memory addresses, like pointers, to check for equality
        // newhash == hash will always return false, as they have different memory addresses
    }
    public static void createUser(String user, String pass, String table, Statement statement){
        // java strong random number generator
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        byte[] hash;

        // third parameter determines the strength
        KeySpec keySpec = new PBEKeySpec(pass.toCharArray(), salt, 0x100000, 128);
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = secretKeyFactory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        System.out.println(hash.toString());
        // put hash and salt into database
        try {
            statement.executeUpdate("INSERT INTO " + table + "(name, hash, salt) VALUES ( '" + user + "', '" + hash + "', '" + salt + "')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean testLogin(String user, String pass){

        return false;
    }
}