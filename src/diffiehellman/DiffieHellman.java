/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package diffiehellman;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import raizprimaria.RaizPrimaria;
import testeprimalidade.TestePrimalidade;

/**
 *
 * @author Hericoelho
 */
public class DiffieHellman {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
      /*Criando instancias de RaizPrimaria que calcula a raizPriamria de um numero
       * e de TestePrimalidade que gerará um numero primo
       */
        RaizPrimaria raiz = new RaizPrimaria();
        TestePrimalidade prim =new TestePrimalidade();
        
        /*Chama metodo que que gerará um numero primo 
         * e armazena em primo
         */
       BigInteger primo = prim.Primo();        
       /*chama metodo que calculara a primeira raiz primitiva de primo
        * exceto 2 e armazena em raizPrimaria
        */
       BigInteger raizPrimaria=raiz.raizPrimaria(primo);
       
        /*Cria dois individuos que desejam aplicar o difiie ellman
         * 
         */
        AcordoChaves alice = new AcordoChaves("alice");
        AcordoChaves bob = new AcordoChaves("bob");
        
        /*alice e bob dão inicio ao diffie hellman e claculam o numero a ser trocado
         * e armazenam em ya e yb
         */
        BigInteger ya=alice.acordoChaves(primo, raizPrimaria);
        BigInteger yb=bob.acordoChaves(primo, raizPrimaria);
        
        /*Com o numero enviado pelo individuo oposto o metodo gera chaves 
         * da continuida a diffiehellman  e calcula a chave
         */
        alice.geraChaves(yb);
        bob.geraChaves(ya);
        /*
         * Para alice e bob chama o metodo que imprime os seus dados na tela 
         */
        alice.mostraDados();
        bob.mostraDados();
    }   
        
    
  
}
