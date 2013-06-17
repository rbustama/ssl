import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;


public class RSA {
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

	    Security.addProvider(new BouncyCastleProvider());

	    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	    keyPairGenerator.initialize(1024);
	    KeyPair keyPair = keyPairGenerator.genKeyPair();

	    String text = "this is the input text";
	    byte[] encripted;
	    System.out.println("input:\n" + text);
	    encripted = encrypt(keyPair.getPublic(), text);
	    System.out.println("cipher:\n" + Base64.toBase64String(encripted));
	    System.out.println("decrypt:\n" + decrypt(keyPair.getPrivate(), encripted));        
	}

	static byte[] encrypt(Key pubkey, String text) {
	    try {
	        Cipher rsa;
	        rsa = Cipher.getInstance("RSA");
	        rsa.init(Cipher.ENCRYPT_MODE, pubkey);
	        return rsa.doFinal(text.getBytes());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	static String decrypt(Key decryptionKey, byte[] buffer) {
	    try {
	        Cipher rsa;
	        rsa = Cipher.getInstance("RSA");
	        rsa.init(Cipher.DECRYPT_MODE, decryptionKey);
	        byte[] utf8 = rsa.doFinal(buffer);
	        return new String(utf8, "UTF8");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
