
import diffiehellman.AcordoChaves;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import org.bouncycastle.util.encoders.Base64;

import raizprimaria.RaizPrimaria;
import testeprimalidade.TestePrimalidade;
import diffiehellman.AcordoChaves;

public class Client extends JFrame {
   Key key = null;
   private JTextField enter;
   private JTextArea display;
   ObjectOutputStream output;
   ObjectInputStream input;
   String message = "";

   public Client()
   {
      super( "Client" );

      Container c = getContentPane();

      enter = new JTextField();
      enter.setEnabled( false );
      enter.addActionListener(
         new ActionListener() {
            public void actionPerformed( ActionEvent e )
            {
               sendData( e.getActionCommand() );
            }
         }
      );
      c.add( enter, BorderLayout.NORTH );

      display = new JTextArea();
      c.add( new JScrollPane( display ),
             BorderLayout.CENTER );

      setSize( 300, 150 );
      show();
   }

   public void runClient() throws IOException 
   {
      Socket client;
    
      try {
         display.setText( "Attempting connection\n" );
         client = new Socket( InetAddress.getByName( "127.0.0.1" ), 5050 );
         display.append( "Connected to: " + client.getInetAddress().getHostName() );
         output = new ObjectOutputStream( client.getOutputStream() );
         output.flush();
         input = new ObjectInputStream( client.getInputStream() );
         display.append( "\nGot I/O streams\n" );
         enter.setEnabled( true );
         String okConnection = null;
         while(okConnection==null){
        	 okConnection = (String) input.readObject();
         }
         display.append( okConnection );
         //Begin - RSA
         //recebe nome do cifrador que devera ser usado
         LoaderKeyPair loader = new LoaderKeyPair();
		 KeyPair keyPair = loader.LoadKeyPair("C:/testes", "RSA");
		
         byte[] encripted = (byte[]) input.readObject();
         String decriptedMsg = RSA.decrypt(keyPair.getPublic(), encripted);
         System.out.println(decriptedMsg);
         //end - RSA  
         String NomeCifrador = decriptedMsg.replace("Cifrador:", "");
         if(NomeCifrador.equals("AES/ECB/PKCS5Padding")){
        	 //Begin -  Diffie
        	 Request request = new Request(TypeRequest.INICIARDIFFIEHELLMAN);
        	 output.writeObject( request );
        	 output.flush();
        	 display.append(  "\n" + request.type.toString() );
        	 display.setCaretPosition( display.getText().length() );
        	 Request ok = (Request) input.readObject();
        	 display.append( "\n" + ok.type.toString() );
        	 display.setCaretPosition( display.getText().length() );
        	 if(ok.type.equals(TypeRequest.OK)){
        		 key = diffieHellman();
        	 }
        	 //End Diffie
        	 //Begin Cifrador
        	 Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        	 aes.init(Cipher.DECRYPT_MODE, key);
        	 do {              
        		 try {
        			 byte[] ciphertext =(byte[]) input.readObject();
        			 String cleartext = new String(aes.doFinal(ciphertext));
        			 display.append( "\n SERVER >>> " + new String(ciphertext) );                 
        			 display.setCaretPosition(display.getText().length() );
        			 display.append( "\n SERVER (mensagem decifrada)>>> " + cleartext );
        			 display.setCaretPosition(display.getText().length() );
        		 }
        		 catch ( Exception e ) {
        			 e.printStackTrace();
        		 }
        	 } while (true );
        	 //End Cifrador
         }
      }
      catch ( Exception e ) {
    	  e.printStackTrace();
      }
   }

   private Key diffieHellman() throws IOException, ClassNotFoundException {
	 // TODO code application logic here
      /*Criando instancias de RaizPrimaria que calcula a raizPriamria de um numero
       * e de TestePrimalidade que gerará um numero primo
       */
        RaizPrimaria raiz = new RaizPrimaria();
        TestePrimalidade prim =new TestePrimalidade();
        
        /*Chama metodo que que gerará um numero primo 
         * e armazena em primo
         */
       BigInteger primo = new BigInteger((String)input.readObject());        
       /*chama metodo que calculara a primeira raiz primitiva de primo
        * exceto 2 e armazena em raizPrimaria
        */
       BigInteger raizPrimaria=new BigInteger((String)input.readObject());
       
        /*Cria dois individuos que desejam aplicar o difiie ellman
         * 
         */
        AcordoChaves bob = new AcordoChaves("bob");
      
        
        /*alice e bob dão inicio ao diffie hellman e claculam o numero a ser trocado
         * e armazenam em ya e yb
         */
        BigInteger ya=bob.acordoChaves(primo, raizPrimaria);
        display.append( "\nSERVER>>> yb :"+ya.toString() );
        
        
        
        /*Com o numero enviado pelo individuo oposto o metodo gera chaves 
         * da continuida a diffiehellman  e calcula a chave
         */
    
        output.writeObject(ya.toString());
         output.flush();
    
        BigInteger yb=new BigInteger((String)input.readObject());
       
        bob.geraChaves(yb);
        display.append( "\nSERVER>>> chave :"+bob.getChave().toString());
     
        /*
         * Para alice e bob chama o metodo que imprime os seus dados na tela 
         */
        bob.mostraDados();
        
        
        byte[] key = bob.getChave().toString().getBytes("UTF-8");
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

private void sendData( String s )
   {
      try {
    	  Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
    	  aes.init(Cipher.ENCRYPT_MODE, key);
    	  byte[] ciphertext = aes.doFinal(s.getBytes());
    	  output.writeObject(ciphertext);
    	  output.flush();
    	  display.append( "\nCLIENT>>>" + s );
      }
      catch ( Exception e ) {
    	  e.printStackTrace();
      }
   }

   public static void main( String args[] ) throws IOException
   {
      Client app = new Client();

      app.addWindowListener(
         new WindowAdapter() {
            public void windowClosing( WindowEvent e )
            {
               System.exit( 0 );
            }
         }
      );

      app.runClient();
   }
}
