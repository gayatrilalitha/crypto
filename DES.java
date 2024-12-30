import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DES {

    private static final String UNICODE_FORMAT = "UTF8";
    private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private Cipher cipher;
    private SecretKey key;
    private String myEncryptionKey = "ThisIsSecretEncryptionKey"; // 24 bytes for 3DES

    public DES() throws Exception {
        // Convert the encryption key to bytes
        byte[] keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        // Create a DESedeKeySpec (Triple DES)
        DESedeKeySpec myKeySpec = new DESedeKeySpec(keyAsBytes);
        // Create SecretKeyFactory for the DESede encryption scheme
        SecretKeyFactory mySecretKeyFactory = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);
        // Initialize the Cipher object with the DESede algorithm
        cipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
        // Generate the key
        key = mySecretKeyFactory.generateSecret(myKeySpec);
    }

    // Encrypt method
    public String encrypt(String unencryptedString) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            // Return the encrypted data encoded as a Base64 string
            return Base64.getEncoder().encodeToString(encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Decrypt method
    public String decrypt(String encryptedString) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.getDecoder().decode(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            return new String(plainText, UNICODE_FORMAT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Main method to test encryption and decryption
    public static void main(String args[]) throws Exception {
        // Create an instance of the DES class
        DES myEncryptor = new DES();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // Take input from the user
        System.out.print("Enter the string to encrypt: ");
        String stringToEncrypt = br.readLine();

        // Encrypt the string
        String encryptedString = myEncryptor.encrypt(stringToEncrypt);
        System.out.println("Encrypted String: " + encryptedString);

        // Decrypt the string
        String decryptedString = myEncryptor.decrypt(encryptedString);
        System.out.println("Decrypted String: " + decryptedString);
    }
}