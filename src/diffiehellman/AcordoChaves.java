/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package diffiehellman;

import java.math.BigInteger;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author Hericoelho
 */
public class AcordoChaves {
    //Dados do endividuo
     BigInteger x ;
    BigInteger primo;
    BigInteger raizPrimaria;
    BigInteger chave;
    String nome;
//Contrutor
   public AcordoChaves(String _nome) {
        nome=_nome;
        x=BigInteger.ZERO;
        primo=BigInteger.ZERO;
        raizPrimaria=BigInteger.ZERO;
        chave=BigInteger.ZERO;
        x=BigInteger.ZERO;
    }
//Getters
    public BigInteger getX() {
        return x;
    }

    public BigInteger getPrimo() {
        return primo;
    }

    public BigInteger getRaizPrimaria() {
        return raizPrimaria;
    }

    public BigInteger getChave() {
        return chave;
    }
   
  /*
   * Aqui aplicasse metade do diffiehellman em que ele gera um numero privado(secreto)
   * calcula e retorna ((raizPrimaria^x) mod primo)
   */  
    public BigInteger acordoChaves(BigInteger _primo, BigInteger _raizPrimaria){
        primo=_primo;
        raizPrimaria=_raizPrimaria;
        Random random = new Random();
         x =new BigInteger((int)(Math.random()*13), random); 
         x=x.add(BigInteger.ONE);
         return raizPrimaria.modPow(x,primo); 
        
    }
    /*
     * Segunda Parte de diffiehellman com um numero recebido do outro individuo chmado aqui de y
     * ele calcula a chave com v^(numero privado) mod primo
     */
    public  void geraChaves(BigInteger y){
        chave=y.modPow(x, primo);
    }
    /*
     * Metodo que mostra os dados
     */
    public void mostraDados(){
        JOptionPane.showMessageDialog(null,"Nome : "+nome+"\nPrimo : "+primo+
                " \n Raiz  primaria : "+raizPrimaria+"\n Numero escolhido : "+x+"\n Chave : "+chave);
    }
    
}
