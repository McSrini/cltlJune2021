/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.collection;

import static ca.mcmaster.cltljune2021.Constants.ONE;

/**
 *
 * @author tamvadss
 */
public class Tuple implements Comparable<Tuple>  {
        
    public String varName ;
    public double coefficient;
 
    public int compareTo(Tuple anotherTuple) {
        return  (this.coefficient) > (anotherTuple.coefficient)?   ONE : -ONE;
    }

    
    
}
