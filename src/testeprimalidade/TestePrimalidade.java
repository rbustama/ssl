/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testeprimalidade;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Hericoelho
 */
public class TestePrimalidade {
    
    /**
     * @param args the command line arguments
     */
   
   public BigInteger Primo(){
        BigInteger expon= BigInteger.valueOf(6);
        //chama o metodo que gera um numero e armazena na variavel n, levando como parametro o numero de casas
        BigInteger n=geraNumero(expon.subtract(BigInteger.ONE));
        /*Repetição:só deixara de repetir quando o numero passar pelo teste de primalidade na função testePrimalidade 
         * Caso não seja primo e gera outro numero e testa novamente , isso se repete até que o numero gerado seja primo
         */
       
        while(!testePrimalidade(n)){         
        n=geraNumero(expon.subtract(BigInteger.ONE));
        }
        //retorna o numero primo       
        return n;
   }
    //Metodo onde é testado se o numero é primo(Miller-Rabin)
    public static boolean testePrimalidade(BigInteger n){
        //Se n=2 é primo e retorna true         
        if(n.compareTo(BigInteger.ONE.add(BigInteger.ONE))==0)
            return true;
            
        //m=n-1
         BigInteger m=n.subtract(BigInteger.ONE);
        //Instancia um objeto da classe de FunçõesAuxiliares         
        FuncoesAuxiliaresPrimalidade f = new FuncoesAuxiliaresPrimalidade();
        //chama metodo que calcula o s e o d de m
        f.CalcularSD(m);
        //Pega s e d de m, anteriormenete calculado em f.CalcularSD
        BigInteger s=f.getS();
        BigInteger d=f.getD();
        //gera um a que a=n-(((n-1)*(aleatório(de 0 à  1)))+2) 
        BigInteger a = BigInteger.ONE.add(BigInteger.ONE);
        /* aqui começa de fato o teste do miller-rabin
         * Se (a^d)mod n ==1
         * retorna true é primo
         */
        if( a.modPow(d, n).compareTo(BigInteger.ONE)==0)           
            return true;
        //incia dois com o valor de 1
        BigInteger dois =BigInteger.ONE;
        //se o numero for maior que 0
        if(s.signum()>0)       
        //pra i de 1 a s-1 repita    
        for(BigInteger i=BigInteger.ZERO;i.compareTo(s)<=0;i=i.add(BigInteger.ONE)){
            //Se a^((2^i)*d)mod n == m mod n
            if((a.modPow(dois.multiply(d), n)).compareTo((n.subtract(BigInteger.ONE)).mod(n))==0)                
                return true;
             //dois = dois*2 
         dois=dois.multiply(BigInteger.ONE.add(BigInteger.ONE));
        }    
            
        //Se não foi verdadeiro pra nenhuma condição anterior então  posso afirmar que ele é não primo 
        //retorna falso
        return false;
    }
    //metodo que gera um numero aleatório
    public static BigInteger geraNumero(BigInteger n){ 
        Random random = new Random();
        
        //declara um big integer chamado resultado
        BigInteger resultado=BigInteger.ZERO ;
        //repetição
        do{
            //inicia b com 1 b é base pro numero aleatório
     BigInteger b = BigInteger.ONE;
     /*para i =0 ate i<n faça
      *base para gerar um numero aleatório com n casas 
      */ 
     //Se n>0 
     if(n.compareTo(BigInteger.ZERO)>0){
         //para i=0 enquanto i<n faça
        for(BigInteger i=BigInteger.ZERO;i.compareTo(n)<0;i=i.add(BigInteger.ONE))
            //b=b*10
            b=b.multiply(BigInteger.TEN);
        
             /*o numero aleatório com 100 digitos
              * enquanto numero for menor que a base gere outro numero
              */ 
        while(resultado.compareTo(b)<=0){
            BigInteger teste =new BigInteger("100000");//000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
            resultado= new BigInteger(teste.bitLength(),random);
         
        }
     }
     else
         /*se n=0 resultado deve estar entre 1 e 9 
          * deve contonar o fato de b não ter magnitude apenas repressentado com sinal=0 
          */          
         resultado = b.add(BigInteger.valueOf((long)(Math.random()*10)));
            //repita enquanto resultado for menor que 2
        }while(resultado.compareTo(BigInteger.ONE.add(BigInteger.ONE))<0);
         return resultado;
    }
}
