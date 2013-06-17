/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package raizprimaria;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import testeprimalidade.TestePrimalidade;

/**
 *
 * @author Hericoelho
 */
public class FuncoesAuxiliaresRaiz {
    //array que contera vai conter os fatores primos
    static ArrayList<BigInteger> s=new ArrayList<BigInteger>();
    static ArrayList<BigInteger> primos=new ArrayList<BigInteger>();
    
    public FuncoesAuxiliaresRaiz(){
        s.clear();        
    }
    public void limpaListaFatoresPrimos(){
        s.clear(); 
    }
    public ArrayList<BigInteger> getS() {
        return s;
    }

  
    //calcula o totiente de euler|calcula a quantidade de numeros coprimos de num
   public BigInteger totienteDeEuler(BigInteger num){ 
    BigInteger count=BigInteger.ZERO;
    for(BigInteger a=BigInteger.ONE;a.compareTo(num)<0;a=a.add(BigInteger.ONE)){ 
      if(num.gcd(a).compareTo(BigInteger.ONE)==0){
        count=count.add(BigInteger.ONE);
       }
    }
    return(count);
}
   //descobre que são os fatores primos de n
   public void AcharPrimos(BigInteger n) {
       //variaveis necessarias
       BigInteger total=BigInteger.ONE;
       BigInteger dois=BigInteger.ONE.add(BigInteger.ONE);
       
    BigInteger aux = n;   
    BigInteger x = n;
    BigInteger resto;
    //div[0] sempre =2  ja div[1]= 
    BigInteger [] div = {dois,BigInteger.ONE};
    int k=0;
  
    //se n é dois adiciona 2 e sai
    if(x.compareTo(dois)==0){
        s.add(x);
        return ;
    }  
        //enquanto x <1 repita  
    while(x.compareTo(BigInteger.ONE)>0){  
        //calcula o resto de x/div[k]
        resto = x.mod(div[k]);
      //Se o resto da divisão de x/div[k]  =0
         if(resto.compareTo(BigInteger.ZERO) == 0){
          //Enquanto o resto da divisão de x/div[k]  =0   repita
          while(resto.compareTo(BigInteger.ZERO) == 0){
            //x/div[kl]
           x = x.divide(div[k]);
           //total*div[k]
            total=total.multiply(div[k]);
            //resto de x/div[k]
            resto = x.mod(div[k]);
          }             
          //add div[k]a lista de fatores primos pois ele é um dos divisores de n
          s .add(div[k]);         
          //divisao recebe a multiplicação dos fatores retantes a calcular 
          BigInteger divisao =n.divide(total);
          //Se divisao é primo então divisao é o ultimo fator primo de n adiciona e sai
          if(TestePrimalidade.testePrimalidade(divisao)){
              s.add(divisao);
               return;
          } 
       
        }
         //Se o resto da divisao não é zero ele não é um fator primo de n
        else {
             //k=1 para pegar só os impares
            k=1;
            do{
                //adicona 2 ao numero anterior para pegar só os impares
                div[k] =div[k].add(dois); 
                //enquanto div[k] não é primo repete a instrução anterior
           }while(!TestePrimalidade.testePrimalidade(div[k]));
                    
        } 
    }        
   
  }
  

    
}
