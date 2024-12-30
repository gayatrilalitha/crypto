import java.io.*;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

public class BlowFish {
    public static void main(String[] args) {
        try {
            // Generate Blowfish Key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
            keyGenerator.init(128);
            Key secretKey = keyGenerator.generateKey();

            // Initialize Cipher for Encryption
            Cipher cipherOut = Cipher.getInstance("Blowfish/CFB/NoPadding");
            cipherOut.init(Cipher.ENCRYPT_MODE, secretKey);

            // Print IV
            byte[] iv = cipherOut.getIV();
            if (iv != null) {
                System.out.println("Initialization Vector: " + Base64.getEncoder().encodeToString(iv));
            }

            // Open File Streams for Encryption
            FileInputStream fin = new FileInputStream("input.txt");
            FileOutputStream fout = new FileOutputStream("output_encrypted.txt");
            CipherOutputStream cout = new CipherOutputStream(fout, cipherOut);

            // Read and Encrypt File
            int input;
            while ((input = fin.read()) != -1) {
                cout.write(input);
            }

            // Close Streams (Important)
            fin.close();
            cout.close();  // Ensures all bytes are flushed to the file

            System.out.println("File Encrypted Successfully!");

            // Initialize Cipher for Decryption
            Cipher cipherIn = Cipher.getInstance("Blowfish/CFB/NoPadding");
            cipherIn.init(Cipher.DECRYPT_MODE, secretKey, cipherOut.getParameters());

            // Open File Streams for Decryption
            FileInputStream encryptedFin = new FileInputStream("output_encrypted.txt");
            CipherInputStream cin = new CipherInputStream(encryptedFin, cipherIn);
            FileOutputStream decryptedFout = new FileOutputStream("output_decrypted.txt");

            // Read and Decrypt File
            while ((input = cin.read()) != -1) {
                decryptedFout.write(input);
            }

            // Close Streams
            cin.close();
            decryptedFout.close();

            System.out.println("File Decrypted Successfully!");

        } catch (FileNotFoundException e) {
            System.err.println("File not found. Ensure input.txt exists.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
