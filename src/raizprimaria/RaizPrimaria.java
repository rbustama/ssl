/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package raizprimaria;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Hericoelho
 */
public class RaizPrimaria {
     private static BigInteger b;
   

    /**
     * @param args the command line arguments
     */
 
   public BigInteger raizPrimaria(BigInteger t){      
       return calculaPrimeiraRaizPrimaria(t);
        
    }
    /*Calculo da raiz primitiva para todo numero  i|(1...n-1)
     * e para todos os fatores primos p(k)(k=1...*) calcula-se a formula 
     * i^((totiente de euler)/p(k))mod n |(k=1...*) se para um i e para todos 
     * p(k)`s nenhum deles resultar em 1, então i é uma raiz primitiva
     */
    public static void calculaRaizPrimaria(BigInteger n){
        //variáveis auxiliares
        FuncoesAuxiliaresRaiz aux=new FuncoesAuxiliaresRaiz();
        aux.AcharPrimos(n.subtract(BigInteger.ONE));//aux.totienteDeEuler(n));
       // System.out.println("passou fatores");
        ArrayList<BigInteger> r =new ArrayList<BigInteger>();
        ArrayList<BigInteger> raizPrimarias =new ArrayList<BigInteger>();
        //for para percorrer 1...n-1
           for(BigInteger i=BigInteger.ONE;i.compareTo(n)<0;i=i.add(BigInteger.ONE)){
            r.clear();
            Iterator<BigInteger> a = aux.getS().iterator();
            while(a.hasNext()){                
                BigInteger s = a.next();
                BigInteger d = aux.totienteDeEuler(n).divide(s);
                r.add(i.modPow(d, n));
                //System.out.println("r: "+(i.pow(d.signum()).mod(n)).toString()+"\nprimo : "+s.toString()+"\n divisão :"+d.toString()+"\nd signum: "+d.intValue());
            }
            /* se no arraylist não conter o 1 então i é um raiz primitiva
             * e adiciona o i na lista  de raízes primitivas
             */
         if(!r.contains(BigInteger.ONE)){
          raizPrimarias.add(i);  
         
         }
            
        }
           
        
    }
   
    /*Igual ao calculaRaizPrimaria, porem quando este encontra a primeira raiz primitiva
     * ele exibi ela e termina
     */
     public static BigInteger calculaPrimeiraRaizPrimaria(BigInteger n){
        /*Cria instancia de FuncoesAuxiliaresRaiz
         * que contem algumas funcoes auxiliares para o calculo da raiz primaria
         */
         FuncoesAuxiliaresRaiz aux=new FuncoesAuxiliaresRaiz();
        //Metodo e calcula os fatores primos de n-1
        aux.AcharPrimos(n.subtract(BigInteger.ONE));
        ArrayList<BigInteger> r =new ArrayList<BigInteger>();
   
        //repita de 1 a n-1
        for(BigInteger i=BigInteger.ONE;i.compareTo(n)<0;i=i.add(BigInteger.ONE)){
            //limpa lista r
            r.clear();
            //para todos os fatores primos faça
            Iterator<BigInteger> fatoresPrimos = aux.getS().iterator();            
            while(fatoresPrimos.hasNext()){                
                BigInteger s = fatoresPrimos.next();
                //"totiente de euler de um numero primo"   (n-1)/s(s é um dos fatores primos )
                BigInteger d = n.subtract(BigInteger.ONE).divide(s);
                //calula r^(d) mod n
                r.add(i.modPow(d, n));
             
               
            }
            //se para um i com nenhum fator primo de n resultou em 1 então i é uma raiz primaria
         if(!r.contains(BigInteger.ONE)){
                System.out.println("A primeira raiz Primaria deste numero é : "+i);
                return i;
         }
            
        }
        return null;
       
    
}
}
