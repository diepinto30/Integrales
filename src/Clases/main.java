/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Clases;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author ing
 */
public class main {

    public static void main(String[] args) throws ScriptException {
        
        
        Evaluacion e= new Evaluacion();
        String n=e.evaluar("sen(x)");
        for(int i=0;i<2;i++){System.out.println(""+i);}
        System.out.println("A: "+n);
        System.out.println("B: "+e.calculo(n,"90"));
        System.out.println("C: "+Math.sin((2*Math.PI)/180));
        ScriptEngineManager script = new ScriptEngineManager();
      ScriptEngine js = script.getEngineByName("JavaScript");
        System.out.println(js.eval("2*2").toString());
        
    }
    
}
