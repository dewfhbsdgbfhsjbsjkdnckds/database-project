// Software server, that can connect to a payment gateway, and a software for accounts management that manages accounts and password
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
        String url = "jdbc:postgresql:mydb";
        Properties properties = new Properties();
        String username;
        properties.setProperty("user", );
        properties.setProperty("password", "");

        Connection connection = DriverManager.getConnection(url, properties);


        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("\nCreate Password: ");
        String password = scanner.nextLine();
        // java strong random number generator
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        // comment

        // third parameter determines the strength
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 0x100000, 128);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = secretKeyFactory.generateSecret(keySpec).getEncoded();

        // salt is unique for each user
        // todo instead of using a map, store this data inside a database
        Map<String, List<byte[]>> passwordsMap = new HashMap<>();
        passwordsMap.put(username, List.of(hash, salt));

        System.out.println("\nTest your password");
        String testPassword = scanner.nextLine();
        scanner.close();

        KeySpec newkeySpec = new PBEKeySpec(testPassword.toCharArray(), passwordsMap.get(username).get(1), 0x100000, 128);
        byte[] newHash = secretKeyFactory.generateSecret(newkeySpec).getEncoded();
        // arrays.equals is needed here because otherwise java would compare memory addresses, like pointers, to check for equality
        // newhash == hash will always return false, as they have different memory addresses
        if (Arrays.equals(newHash, passwordsMap.get(username).get(0))){
           System.out.println("signed in");
        }
        System.out.println("hash is:               " + Arrays.toString(newHash));
        System.out.println("test password hash is: " + Arrays.toString(passwordsMap.get(username).get(0)));
    }
}