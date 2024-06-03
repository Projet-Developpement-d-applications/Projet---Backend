package projet.conquerants.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import projet.conquerants.Controller.UtilisateurController;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Service
public class RSADecoderService {

    private String PRIVATE_KEY = System.getenv("PRIVATE_KEY");
    private Logger logger = LoggerFactory.getLogger(RSADecoderService.class);

    public String decrypt(String encryptedText) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

        // Remove the PEM header and footer
        String privateKeyString = PRIVATE_KEY
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s+", ""); // Remove whitespace characters

        // Decode the Base64-encoded private key
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);

        java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // Parse the decoded bytes into a PrivateKey object
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

        // Initialize the cipher for decryption
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // Decrypt the encrypted data
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Convert the decrypted bytes to a string (assuming UTF-8 encoding)
        return new String(decryptedBytes, "UTF-8");
    }
}
