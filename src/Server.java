
import diffiehellman.AcordoChaves;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import raizprimaria.RaizPrimaria;
import testeprimalidade.TestePrimalidade;

public class Server extends JFrame {
	private JTextField enter;
	private JTextArea display;
	ObjectOutputStream output;
	ObjectInputStream input;
	Key key = null;

	public Server() {
		super("Server");

		Container c = getContentPane();

		enter = new JTextField();
		enter.setEnabled(false);
		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendData(e.getActionCommand());
			}
		});
		c.add(enter, BorderLayout.NORTH);

		display = new JTextArea();
		c.add(new JScrollPane(display), BorderLayout.CENTER);

		setSize(300, 150);
		show();
	}

	public void runServer() {
		ServerSocket server;
		Socket connection;
		// BigInteger key = null;
		try {
			server = new ServerSocket(5050, 100);
			while (true) {
				//abre conexao
				display.setText("Waiting for connection\n");
				connection = server.accept();
				display.append("Connection, received from: " + connection.getInetAddress().getHostName());
				output = new ObjectOutputStream(connection.getOutputStream());
				output.flush();
				input = new ObjectInputStream(connection.getInputStream());
				display.append("\nGot I/O streams\n");
				String message = "SERVER>>> Connection successful";
				output.writeObject(message);
				output.flush();
				enter.setEnabled(true);

				//begin RSA
				//envia nome do cifrador cifrado com a chave privada
				LoaderKeyPair loader = new LoaderKeyPair();
				KeyPair keyPair = loader.LoadKeyPair("C:/testes", "RSA");
				byte[] encryptText = RSA.encrypt(keyPair.getPrivate(), "Cifrador:AES/ECB/PKCS5Padding");
				output.writeObject(encryptText);
				output.flush();
				//end RSA
				
				Request request = (Request) input.readObject();
				display.append("\n" + request.toString());
				display.setCaretPosition(display.getText().length());
				if (request.type.equals(TypeRequest.INICIARDIFFIEHELLMAN)) {
					Request ok = new Request(TypeRequest.OK);
					output.writeObject(ok);
					output.flush();
					display.append(request.type.toString());
					display.setCaretPosition(display.getText().length());
					
					key = diffieHellman();
					
					 //Begin Cifrador
		        	 Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		        	 aes.init(Cipher.DECRYPT_MODE, key);
		        	 do {              
		        		 try {
		        			 byte[] ciphertext =(byte[]) input.readObject();
		        			 String cleartext = new String(aes.doFinal(ciphertext));
		        			 display.append( "\n CLIENT >>> " + new String(ciphertext) );                           
		        			 display.setCaretPosition(display.getText().length() );
		        			 display.append( "\n CLIENT (mensagem decifrada)>>> " + cleartext );
		        			 display.setCaretPosition(display.getText().length() );
		        		 }
		        		 catch ( Exception e ) {
		        			 e.printStackTrace();
		        		 }
		        	 } while (true );
		        	 //End Cifrador
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendData( String s )
	   {
	      try {
	    	  Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
	    	  aes.init(Cipher.ENCRYPT_MODE, key);
	    	  byte[] ciphertext = aes.doFinal(s.getBytes());
	    	  output.writeObject(ciphertext);
	    	  output.flush();
	    	  display.append( "\nSERVER>>>" + s );
	      }
	      catch ( Exception e ) {
	    	  e.printStackTrace();
	      }
	   }

	public static void main(String args[]) {
		Server app = new Server();

		app.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		app.runServer();
	}

	public Key diffieHellman() throws IOException, ClassNotFoundException {
		// ...

		/*
		 * try { String passphrase = "chave gerada pelo acordo"; MessageDigest
		 * digest; digest = MessageDigest.getInstance("SHA");
		 * digest.update(passphrase.getBytes()); SecretKeySpec key = new
		 * SecretKeySpec(digest.digest(), 0, 16, "AES"); return key; } catch
		 * (NoSuchAlgorithmException e) { e.printStackTrace(); return null; }
		 */
		// TODO code application logic here
		/*
		 * Criando instancias de RaizPrimaria que calcula a raizPriamria de um
		 * numero e de TestePrimalidade que gerará um numero primo
		 */
		RaizPrimaria raiz = new RaizPrimaria();
		TestePrimalidade prim = new TestePrimalidade();

		/*
		 * Chama metodo que que gerará um numero primo e armazena em primo
		 */
		BigInteger primo = prim.Primo();
		display.append("\nSERVER>>> Primo gerado :" + primo.toString());
		output.writeObject(primo.toString());
		output.flush();
		/*
		 * chama metodo que calculara a primeira raiz primitiva de primo exceto
		 * 2 e armazena em raizPrimaria
		 */
		BigInteger raizPrimaria = raiz.raizPrimaria(primo);
		display.append("\nSERVER>>> Raiz primaria calculada :" + raizPrimaria.toString());
		output.writeObject(raizPrimaria.toString());
		output.flush();
		/*
		 * Cria dois individuos que desejam aplicar o difiie ellman
		 */
		AcordoChaves alice = new AcordoChaves("alice");

		/*
		 * alice e bob dão inicio ao diffie hellman e claculam o numero a ser
		 * trocado e armazenam em ya e yb
		 */
		BigInteger ya = alice.acordoChaves(primo, raizPrimaria);
		display.append("\nSERVER>>> Ya :" + ya.toString());

		/*
		 * Com o numero enviado pelo individuo oposto o metodo gera chaves da
		 * continuida a diffiehellman e calcula a chave
		 */

		output.writeObject(ya.toString());
		output.flush();

		BigInteger yb = new BigInteger((String) input.readObject());
		alice.geraChaves(yb);
		display.append("\nSERVER>>>chave :" + alice.getChave().toString());

		/*
		 * Para alice e bob chama o metodo que imprime os seus dados na tela
		 */
		alice.mostraDados();

		
		byte[] key = alice.getChave().toString().getBytes("UTF-8");
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit
			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
			return secretKeySpec;
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			return null;
		}
	}
}