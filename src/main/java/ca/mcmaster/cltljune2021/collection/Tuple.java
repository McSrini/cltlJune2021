/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.collection;

import static ca.mcmaster.cltljune2021.Constants.*;

/**
 *
 * @author tamvadss
 */
public class Tuple implements Comparable<Tuple>  {
        
    public String varName ;
    public double coefficient;
    
    public Tuple ( ){
         
    }
    
    public Tuple (String varName ,double coefficient){
        this.coefficient = coefficient;
        this.varName = varName;
    }
 
    public int compareTo(Tuple anotherTuple) {    
        int result = ZERO;
        double val =  Math.abs(anotherTuple.coefficient) -  Math.abs (this.coefficient );
        if (val > ZERO) result = ONE;
        if (val < ZERO) result = -ONE;
        return result;
    } 

    
    
    
}
