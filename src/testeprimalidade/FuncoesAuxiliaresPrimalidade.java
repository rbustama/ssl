/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testeprimalidade;

import java.math.BigInteger;

/**
 *
 * @author Hericoelho
 */
public class FuncoesAuxiliaresPrimalidade {
    //variaveis necessarias  
   private BigInteger d;
   private BigInteger s;
//gets e sets
    public BigInteger getD() {
        if(d.compareTo(BigInteger.ZERO)==0)
            return BigInteger.ONE;
        else
        return d;
    }

    public void setD(BigInteger d) {
        this.d = d;
    }

    public BigInteger getS() {
        return s;
    }

    public void setS(BigInteger s) {
        this.s = s;
    }

 //metodo que calcula s e d de um numero n          
  public void CalcularSD(BigInteger n) {
      /*seta d com 1 para o caso de n for composto a apenas por potencia de 2
       * e s com 0 para o caso de n não for composto por 2 ou por uma potencia de 2
       */       
        d=BigInteger.ONE;
        s=BigInteger.ZERO;
       //variaveis aux necessarias
        BigInteger x = n;
        BigInteger resto;
        //inicia div com 2
        BigInteger div = BigInteger.ONE.add(BigInteger.ONE);
        //inicia exp com 0
        BigInteger exp = BigInteger.ZERO;
        //Se x=2 s=1 e d=1
        if(x.compareTo(BigInteger.ONE.add(BigInteger.ONE))==0)  {
         s=BigInteger.ONE;   
            return; 
        }
        //enquanto x>1
        while(x.compareTo(BigInteger.ONE)>0){
            /*resto é igual a resto de x a div
             * div=2
             */            
          resto = x.mod(div);
          //Se resto==0
          if(resto.compareTo(BigInteger.ZERO)==0){
              //Enquanto resto ==0
              while(resto.compareTo(BigInteger.ZERO)==0){
                  // x = quociente da divisão de x / div
               x = x.divideAndRemainder(div)[0];
               //exp ++
               exp=exp.add(BigInteger.ONE);
               //resto recebe o resto da divisão de x/div
               resto = x.mod(div);
              }  // endwhile
              //Se exp>0 s é igual a exp 
               if(exp.compareTo(BigInteger.ZERO)>0)
                  s=exp;
            
          }
          //se x não for divisivel por div então d=x
          else {
               d=x;
               return;
               
          } 
        } 
    
  }
}
