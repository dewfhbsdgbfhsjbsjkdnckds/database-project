package org.example;// Software server, that can connect to a payment gateway, and a software for accounts management that manages accounts and password
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
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


        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("\nCreate Password: ");
        String password = scanner.nextLine();
        createUser(username, password, usersTable, connection);

        // salt is unique for each user
        // todo instead of using a map, store this data inside a database

        System.out.println("\nTest your password");
        String testPassword = scanner.nextLine();
        scanner.close();

        // arrays.equals is needed here because otherwise java would compare memory addresses, like pointers, to check for equality
        // newhash == hash will always return false, as they have different memory addresses
    }
    // instead of passing the database connection as an argument, i can create a database class that has a public function to get the database connection
    public static void createUser(String username, String pass, String table, Connection connection){
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
            System.out.println(Arrays.toString(hash));
            System.out.println(Arrays.toString(salt));
            // todo postgre does NOT like 0x00, so uh idk maybe ill use binary or something
            String hashString = new String(hash, );
            String saltString = new String(salt, StandardCharsets.UTF_8);
            // put hash and salt into database
            String sqlStatement = "INSERT INTO " + table + " (name, hash, salt) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setString(1, username);
            statement.setString(2, hashString);
            statement.setString(3, saltString);
            statement.execute();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean testLogin(String user, String pass){

        return false;
    }
}
