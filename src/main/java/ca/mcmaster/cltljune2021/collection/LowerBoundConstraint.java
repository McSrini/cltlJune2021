/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.cltljune2021.collection;

import static ca.mcmaster.cltljune2021.Constants.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public class LowerBoundConstraint {
    public   List<Tuple> tuples =  new ArrayList<Tuple>();
    
    public double lowerBound ;
    
    
    public void printMe (){
        for (Tuple tuple: tuples){
            System.out.print("("+tuple.varName + "," + tuple.coefficient+ ")") ;
        }
        System.out.println(" >= "+ lowerBound) ;
    }
    
    public void add (List<Tuple> newTuples) {
        tuples.addAll(newTuples) ;
        Collections.sort(tuples);
        
        
    }
    
    public void remove (String var, int fixing) {
          
        Tuple first =  tuples.get(ZERO);
                  
        tuples.remove(first);
        this.lowerBound -= first.coefficient*fixing;
    }
    
    public String getVariableToBranch () {
        
        return  tuples.get(ZERO).varName;
          
    }
    
    //clone
    public LowerBoundConstraint createCopy () {
        LowerBoundConstraint newConstr = new LowerBoundConstraint ();
        newConstr.lowerBound = this.lowerBound;
        newConstr.tuples.addAll (this.tuples) ;
        return newConstr;
    }
    
    public boolean isGuaranteed_Feasible () {
        double lowestPossibleLHS = ZERO;
        for (Tuple tuple : tuples){
            if (tuple.coefficient < ZERO){
                lowestPossibleLHS += tuple.coefficient ;
            }
        }
        return lowestPossibleLHS >= lowerBound;
    }
    
    public boolean isGuaranteed_InFeasible () {
        double highestPossibleLHS = ZERO;
        for (Tuple tuple : tuples){
            if (tuple.coefficient > ZERO){
                highestPossibleLHS += tuple.coefficient ;
            }
        }
        return highestPossibleLHS < lowerBound;
    }
     
}
