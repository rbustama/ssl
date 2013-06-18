package programa;

import java.math.BigInteger;
import java.security.AlgorithmParameterGenerator;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.xml.bind.DatatypeConverter;

public class testeDH {

   public static void main(String[] args) {
      try {
         /////////////////////////////// Server generates spec and sends to client
         AlgorithmParameterGenerator servAPG = AlgorithmParameterGenerator.getInstance("DH");
         servAPG.init(512);

         DHParameterSpec servParamSpec = servAPG.generateParameters().getParameterSpec(DHParameterSpec.class);

         BigInteger servp = servParamSpec.getP();
         BigInteger servg = servParamSpec.getG();
         System.out.println("P:" + servp + "\n\nG:" + servg);
         /////////////////////////////// Client recieves p & g and creates spec
         BigInteger clip = servp;
         BigInteger clig = servg;

         DHParameterSpec cliParamSpec = new DHParameterSpec(clip, clig);
         /////////////////////////////// Server generates keypair
         KeyPairGenerator servKPG = KeyPairGenerator.getInstance("DH");
         servKPG.initialize(servParamSpec);
         KeyPair servKP = servKPG.generateKeyPair();

         String servPub = byteToHex(servKP.getPublic().getEncoded());
         System.out.println("\nServer PKI:" + servPub);
         /////////////////////////////// Client generates keypair
         KeyPairGenerator cliKPG = KeyPairGenerator.getInstance("DH");
         cliKPG.initialize(cliParamSpec);
         KeyPair cliKP = cliKPG.generateKeyPair();

         String cliPub = byteToHex(cliKP.getPublic().getEncoded());
         System.out.println("\nClient PKI:" + cliPub);
         /////////////////////////////// PKI exchange and convert back
         DHPublicKey servCliPub = (DHPublicKey) KeyFactory.getInstance("DH").generatePublic(
                 new X509EncodedKeySpec(hexToByte(cliPub)));

         DHPublicKey cliServPub = (DHPublicKey) KeyFactory.getInstance("DH").generatePublic(
                 new X509EncodedKeySpec(hexToByte(servPub)));
         /////////////////////////////// Server generates secret
         KeyAgreement servKA = KeyAgreement.getInstance("DH");
         servKA.init(servKP.getPrivate(), servParamSpec);
         servKA.doPhase(servCliPub, true);
         byte[] servSecret = servKA.generateSecret();
         System.out.println("\nServer S:" + byteToHex(servSecret));
         /////////////////////////////// Client gens secret
         KeyAgreement cliKA = KeyAgreement.getInstance("DH");
         cliKA.init(cliKP.getPrivate(), cliParamSpec);
         cliKA.doPhase(cliServPub, true);
         byte[] cliSecret = cliKA.generateSecret();
         System.out.println("\nClient S:" + byteToHex(cliSecret));
         ////////////////////////////// Check
		if (Arrays.equals(cliSecret, servSecret)) System.out.println("\nMATCH!");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private static String byteToHex(byte[] array) {
      return DatatypeConverter.printHexBinary(array);
   }

   private static byte[] hexToByte(String string) {
      return DatatypeConverter.parseHexBinary(string);
   }
}