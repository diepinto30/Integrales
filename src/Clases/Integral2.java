package Clases;
/**
 *
 * @author ing
 */

public class Integral2 {
  private String h0;
  private String h2;
  private String area;
  private String Fa, Fb;
    public String CalcularIntegral(String funcion, String h0, String h2){
   
long time_start, time_end;
time_start = System.currentTimeMillis();

      Evaluacion e= new Evaluacion();
      String ecuacion=e.evaluar(funcion);
      this.h0=h0;
      this.h2=h2;
      int cantidadparticiones=1000;
      double coef= (Double.parseDouble(h2)-Double.parseDouble(h0))/(cantidadparticiones*3);
      double part=(Double.parseDouble(h2)-Double.parseDouble(h0))/cantidadparticiones;
      double sumaPar=0;
      double sumaImpar=0;
      Fa=e.calculo(ecuacion,this.h0);
      Fb=e.calculo(ecuacion,this.h2);
      int j=1;
      for(Double i=Double.parseDouble(this.h0)+part;i<Double.parseDouble(this.h2);i+=part){
      if(j%2==0){sumaImpar=sumaImpar+Double.parseDouble(e.calculo(ecuacion,String.valueOf(i)));
      }
      else{sumaPar=sumaPar+Double.parseDouble(e.calculo(ecuacion, String.valueOf(i)));
      }
      j++;
      }
      double Final=0;
      Final=(coef)*(Double.parseDouble(Fa)+Double.parseDouble(Fb)+(4*sumaPar)+(sumaImpar*2));
      area= String.valueOf(Redondear(Final));
time_end = System.currentTimeMillis();
System.out.println("El calculo tomo "+ (double)( time_end - time_start )/1000 +" segundos");
      return this.area;
  }

  public String getArea() {
    return area;
  }
  
  
    public double Redondear(double numero){
        long redondeo= 1000000000;
        return Math.rint(numero *redondeo )/redondeo;
        
    }
  
}
