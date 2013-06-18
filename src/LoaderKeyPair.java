import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class LoaderKeyPair {

	public static void main(String args[]) {

		LoaderKeyPair adam = new LoaderKeyPair();

		try {
			String path = "C:/testes";
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			KeyPair generatedKeyPair = keyGen.genKeyPair();
			System.out.println("Generated Key Pair");
			adam.dumpKeyPair(generatedKeyPair);
			adam.SaveKeyPair(path, generatedKeyPair);
			KeyPair loadedKeyPair = adam.LoadKeyPair(path, "RSA");
			System.out.println("Loaded Key Pair");
			adam.dumpKeyPair(loadedKeyPair);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private void dumpKeyPair(KeyPair keyPair) {
		PublicKey pub = keyPair.getPublic();
		System.out.println("Public Key: " + getHexString(pub.getEncoded()));
		PrivateKey priv = keyPair.getPrivate();
		System.out.println("Private Key: " + getHexString(priv.getEncoded()));
	}


	private String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
 
	public void SaveKeyPair(String path, KeyPair keyPair) throws IOException {
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
 
		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		FileOutputStream fos = new FileOutputStream(path + "/public.key");
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();
 
		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		fos = new FileOutputStream(path + "/private.key");
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();
	}
 
	public KeyPair LoadKeyPair(String path, String algorithm)
			throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		// Read Public Key.
		path = getClass().getResource(path).getPath();
		path = path.replaceAll("%20", " ");
		File filePublicKey = new File(path + "../public.key");
		FileInputStream fis = new FileInputStream(path + "../public.key");
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();
 
		// Read Private Key.
		File filePrivateKey = new File(path + "../private.key");
		fis = new FileInputStream(path + "../private.key");
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();
 
		// Generate KeyPair.
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
 
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
 
		return new KeyPair(publicKey, privateKey);
	}
}
